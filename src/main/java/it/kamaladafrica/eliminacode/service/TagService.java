package it.kamaladafrica.eliminacode.service;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import it.kamaladafrica.eliminacode.domain.Tag;
import it.kamaladafrica.eliminacode.domain.Tag_;
import it.kamaladafrica.eliminacode.repository.TagRepository;
import it.kamaladafrica.eliminacode.service.dto.TagDTO;
import it.kamaladafrica.eliminacode.service.mapper.TagMapper;

/**
 * Service Implementation for managing {@link Tag}.
 */
@Service
@Transactional
public class TagService {

	private final Logger log = LoggerFactory.getLogger(TagService.class);

	private final TagRepository tagRepository;

	private final TagMapper tagMapper;

	private final SeqService sequence;

	public TagService(TagRepository tagRepository, TagMapper tagMapper, SeqService sequence) {
		this.tagRepository = tagRepository;
		this.tagMapper = tagMapper;
		this.sequence = sequence;
	}

	/**
	 * Save a tag.
	 *
	 * @param tagDTO the entity to save.
	 * @return the persisted entity.
	 */
	public TagDTO newTag() {
		Tag tag = new Tag();
		tag.setKey(UUID.randomUUID());
		tag.setProgressivo(sequence.nextVal());
		tag.setStaccato(Instant.now());
		tag = tagRepository.save(tag);
		log.debug("Staccato nuovo tag: {}", tag);
		return tagMapper.toDto(tag);
	}

	/**
	 * Get all the tags.
	 *
	 * @return the list of entities.
	 */
	@Transactional(readOnly = true)
	public List<TagDTO> findAll() {
		log.debug("Request to get all Tags");
		return tagRepository.findAll().stream()
				.map(tagMapper::toDto)
				.collect(Collectors.toCollection(LinkedList::new));
	}

	/**
	 * Get current (last bruciato) tag.
	 *
	 * @return the list of entities.
	 */
	@Transactional(readOnly = true)
	public Optional<TagDTO> findCurrentTag() {
		log.debug("Request to get current Tag");
		Pageable pageable = PageRequest.of(0, 1, Sort.by(Order.desc(Tag_.BRUCIATO), Order.desc(Tag_.PROGRESSIVO)));
		Page<Tag> page = tagRepository.findAllByStaccatoGreaterThanEqualAndBruciatoIsNotNull(today(), pageable);
		return page.stream().map(tagMapper::toDto).findAny();
	}

	@Transactional(readOnly = true)
	public Optional<TagDTO> findNextTag() {
		log.debug("Request to get next Tag");
		Pageable pageable = PageRequest.of(0, 1, Sort.by(Order.asc(Tag_.PROGRESSIVO)));
		Page<Tag> page = tagRepository.findAllByStaccatoGreaterThanEqualAndBruciatoIsNull(today(), pageable);
		return page.stream().map(tagMapper::toDto).findAny();
	}

	@Transactional(readOnly = true)
	public Optional<TagDTO> findLastTag() {
		log.debug("Request to get last Tag");
		Pageable pageable = PageRequest.of(0, 1, Sort.by(Order.desc(Tag_.PROGRESSIVO)));
		Page<Tag> page = tagRepository.findAllByStaccatoGreaterThanEqualAndBruciatoIsNull(today(), pageable);
		return page.stream().map(tagMapper::toDto).findAny();
	}

	@Transactional(readOnly = true)
	public byte[] generateQRCode(String key) {
		return tagRepository.findByKey(UUID.fromString(key))
				.filter(t -> t.getBruciato() == null)
				.filter(t -> t.getStaccato().isAfter(today()))
				.map(t -> {
					try {
						Map<EncodeHintType, ErrorCorrectionLevel> hints = new HashMap<>();
						hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
						QRCodeWriter barcodeWriter = new QRCodeWriter();
						BitMatrix bitMatrix = barcodeWriter.encode(key, BarcodeFormat.QR_CODE, 400, 400, hints);
						BufferedImage image = MatrixToImageWriter.toBufferedImage(bitMatrix);
						ByteArrayOutputStream os = new ByteArrayOutputStream();
						ImageIO.write(image, "png", os);
						return os.toByteArray();
					} catch (WriterException | IOException e) {
						throw new GenerateQRCodeException(e);
					}
				})
				.orElseThrow(GenerateQRCodeException::new);
	}

	private static Instant today() {
		return LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant();
	}

	/**
	 * Get one tag by id.
	 *
	 * @param id the id of the entity.
	 * @return the entity.
	 */
	@Transactional(readOnly = true)
	public Optional<TagDTO> findOne(String key) {
		return tagRepository.findByKey(UUID.fromString(key))
				.map(tagMapper::toDto);
	}

	public void brucia(String key) {
		tagRepository.findByKey(UUID.fromString(key))
				.ifPresent(tag -> {
					tag.setBruciato(Instant.now());
					tagRepository.save(tag);
				});
	}
}
