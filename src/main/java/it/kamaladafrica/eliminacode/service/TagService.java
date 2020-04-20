package it.kamaladafrica.eliminacode.service;

import static com.google.zxing.BarcodeFormat.QR_CODE;
import static com.google.zxing.EncodeHintType.ERROR_CORRECTION;
import static com.google.zxing.qrcode.decoder.ErrorCorrectionLevel.M;
import static it.kamaladafrica.eliminacode.service.DataUriUtils.toDataURI;
import static java.util.Collections.singletonMap;
import static org.apache.commons.lang3.ObjectUtils.defaultIfNull;
import static org.springframework.http.MediaType.IMAGE_PNG_VALUE;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.UriComponentsBuilder;

import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import it.kamaladafrica.eliminacode.config.ApplicationProperties;
import it.kamaladafrica.eliminacode.domain.Tag;
import it.kamaladafrica.eliminacode.repository.TagRepository;
import it.kamaladafrica.eliminacode.service.dto.TagDTO;
import it.kamaladafrica.eliminacode.service.dto.TagStatsDTO;
import it.kamaladafrica.eliminacode.service.mapper.TagMapper;

/**
 * Service Implementation for managing {@link Tag}.
 */
@Service
@Transactional
public class TagService {

	private static final Map<EncodeHintType, ?> QR_CODE_HINTS = singletonMap(ERROR_CORRECTION, M);

	private final ApplicationProperties applicationProperties;

	private final Logger log = LoggerFactory.getLogger(TagService.class);

	private final TagRepository tagRepository;

	private final TagMapper tagMapper;

	private final SeqService sequence;

	public TagService(TagRepository tagRepository, TagMapper tagMapper, SeqService sequence,
			ApplicationProperties applicationProperties) {
		this.tagRepository = tagRepository;
		this.tagMapper = tagMapper;
		this.sequence = sequence;
		this.applicationProperties = applicationProperties;
	}

	public TagDTO newTag() {
		Tag tag = new Tag();
		tag.setKey(UUID.randomUUID());
		tag.setProgressivo(sequence.nextVal());
		tag.setStaccato(Instant.now());
		tag = tagRepository.save(tag);
		log.debug("Staccato nuovo tag: {}", tag);
		return toDto(tag);
	}

	private TagDTO toDto(Tag tag) {
		TagDTO dto = tagMapper.toDto(tag);
		final int qrCodeSize = applicationProperties.getQrcodeSize();
		String qrCodeText = UriComponentsBuilder.fromHttpUrl(applicationProperties.getQrcodeUrlTemplate())
				.buildAndExpand(tag.getKey().toString()).toUriString();
		byte[] qrCode = generateQRCode(tag, qrCodeText, qrCodeSize);
		dto.setQrCodeImageUrl(toDataURI(qrCode, IMAGE_PNG_VALUE));
		return dto;
	}

	protected Optional<Instant> getExpiryInstant() {
		return tagRepository.getLastBruciatoInstantOfToday()
				.map(this::computeExpiryInstant);
	}

	private Instant computeExpiryInstant(Instant lastBruciato) {
		return lastBruciato.plus(applicationProperties.getExpiry(), ChronoUnit.MINUTES);
	}

	@Transactional(readOnly = true)
	public TagStatsDTO getStats() {
		log.debug("Request to get stats Tag");

		final Instant expiry = getExpiryInstant().orElse(today().plus(1, ChronoUnit.DAYS));
		final List<Long> fila = tagRepository.findNextProgressivi();
		final double tempo = applicationProperties.getAverageTempo();
		final Long progressivo = sequence.previewNextVal().orElse(1L);

		TagStatsDTO stats = new TagStatsDTO();
		stats.setTempoStimato(tempo);
		stats.setFila(fila);
		stats.setProgressivo(progressivo);
		stats.setTempoLimite(expiry);
		return stats;
	}

	protected boolean isNotExpired(Tag tag) {
		return !isExpired(tag);
	}

	protected boolean isExpired(Tag tag) {
		if (tag.getStaccato().isAfter(today())) {
			return tagRepository.getLastBruciatoOfToday()
					.filter(last -> tag.getProgressivo() < last.getProgressivo())
					.map(Tag::getBruciato)
					.map(this::computeExpiryInstant)
					.map(Instant.now()::isAfter)
					.orElse(false);
		}
		return true;
	}

	private static Instant today() {
		return Instant.now().truncatedTo(ChronoUnit.DAYS);
	}

	@Transactional(readOnly = true)
	public Optional<byte[]> generateQRCode(String key, String url, Integer size) {
		final int s = defaultIfNull(size, applicationProperties.getQrcodeSize());
		return findOneNotExpired(key)
				.map(t -> {
					try {
						QRCodeWriter barcodeWriter = new QRCodeWriter();
						BitMatrix bitMatrix = barcodeWriter.encode(url, QR_CODE, s, s, QR_CODE_HINTS);
						BufferedImage image = MatrixToImageWriter.toBufferedImage(bitMatrix);
						ByteArrayOutputStream os = new ByteArrayOutputStream();
						ImageIO.write(image, "png", os);
						return os.toByteArray();
					} catch (WriterException | IOException e) {
						throw new GenerateQRCodeException(e);
					}
				});
	}

	protected byte[] generateQRCode(Tag tag, String url, Integer size) {
		final int s = defaultIfNull(size, applicationProperties.getQrcodeSize());
		try {
			QRCodeWriter barcodeWriter = new QRCodeWriter();
			BitMatrix bitMatrix = barcodeWriter.encode(url, QR_CODE, s, s, QR_CODE_HINTS);
			BufferedImage image = MatrixToImageWriter.toBufferedImage(bitMatrix);
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			ImageIO.write(image, "png", os);
			return os.toByteArray();
		} catch (WriterException | IOException e) {
			throw new GenerateQRCodeException(e);
		}
	}

	/**
	 * Get one tag by id.
	 *
	 * @param id the id of the entity.
	 * @return the entity.
	 */
	@Transactional(readOnly = true)
	public Optional<TagDTO> findOne(String key) {
		return findOneNotExpired(key).map(this::toDto);
	}

	@Transactional(readOnly = true)
	public boolean checkExpiration(String key) {
		return findOneNotExpired(key).isPresent();
	}

	private void ensureNotExpired(Tag tag) {
		if (!isNotExpired(tag)) {
			throw new ExpiredTagException(tag.getKey().toString());
		}
	}

	private Optional<Tag> findOneNotExpired(String key) {
		Optional<Tag> tag = tagRepository.findByKeyNotBruciatoOfToday(UUID.fromString(key));
		tag.ifPresent(this::ensureNotExpired);
		return tag;
	}

	public void brucia(String key) {
		findOneNotExpired(key).ifPresent(tag -> {
			tag.setBruciato(Instant.now());
			tagRepository.save(tag);
		});
	}

	public void delete(String key) {
		tagRepository.findByKeyNotBruciatoOfToday(UUID.fromString(key))
				.ifPresent(tagRepository::delete);
	}
}
