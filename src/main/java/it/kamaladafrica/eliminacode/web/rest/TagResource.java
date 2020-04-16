package it.kamaladafrica.eliminacode.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import it.kamaladafrica.eliminacode.service.TagService;
import it.kamaladafrica.eliminacode.service.dto.TagDTO;
import it.kamaladafrica.eliminacode.service.dto.TagStatsDTO;

/**
 * REST controller for managing {@link it.kamaladafrica.eliminacode.domain.Tag}.
 */
@RestController
@RequestMapping("/api")
public class TagResource {

	private final Logger log = LoggerFactory.getLogger(TagResource.class);

	private static final String ENTITY_NAME = "tag";

	@Value("${jhipster.clientApp.name}")
	private String applicationName;

	private final TagService tagService;

	public TagResource(TagService tagService) {
		this.tagService = tagService;
	}

	/**
	 * {@code POST  /tags} : Create a new tag.
	 *
	 * @param tagDTO the tagDTO to create.
	 * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with
	 *         body the new tagDTO, or with status {@code 400 (Bad Request)} if the
	 *         tag has already an ID.
	 * @throws URISyntaxException if the Location URI syntax is incorrect.
	 */
	@PostMapping("/tags")
	public ResponseEntity<TagDTO> createTag() throws URISyntaxException {
		log.debug("REST request to save Tag");
		TagDTO result = tagService.newTag();
		return ResponseEntity.created(new URI("/api/tags/" + result.getKey()))
				.headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME,
						result.getKey().toString()))
				.body(result);
	}

	/**
	 * {@code PUT  /tags} : Updates an existing tag.
	 *
	 * @param tagDTO the tagDTO to update.
	 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
	 *         the updated tagDTO, or with status {@code 400 (Bad Request)} if the
	 *         tagDTO is not valid, or with status
	 *         {@code 500 (Internal Server Error)} if the tagDTO couldn't be
	 *         updated.
	 * @throws URISyntaxException if the Location URI syntax is incorrect.
	 */
	@PutMapping("/tags/{key}")
	public ResponseEntity<TagDTO> bruciaTag(@PathVariable String key) throws URISyntaxException {
		log.debug("REST request to brucia Tag : {}", key);
		tagService.brucia(key);
		return ResponseEntity.noContent()
				.headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, key))
				.build();
	}

	/**
	 * {@code PUT  /tags} : Updates an existing tag.
	 *
	 * @param tagDTO the tagDTO to update.
	 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
	 *         the updated tagDTO, or with status {@code 400 (Bad Request)} if the
	 *         tagDTO is not valid, or with status
	 *         {@code 500 (Internal Server Error)} if the tagDTO couldn't be
	 *         updated.
	 * @throws URISyntaxException if the Location URI syntax is incorrect.
	 */
	@DeleteMapping("/tags/{key}")
	public ResponseEntity<TagDTO> eliminaTag(@PathVariable String key) throws URISyntaxException {
		log.debug("REST request to brucia Tag : {}", key);
		tagService.delete(key);
		return ResponseEntity.noContent()
				.headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, key))
				.build();
	}

	/**
	 * {@code PUT  /tags} : Updates an existing tag.
	 *
	 * @param tagDTO the tagDTO to update.
	 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
	 *         the updated tagDTO, or with status {@code 400 (Bad Request)} if the
	 *         tagDTO is not valid, or with status
	 *         {@code 500 (Internal Server Error)} if the tagDTO couldn't be
	 *         updated.
	 * @throws URISyntaxException if the Location URI syntax is incorrect.
	 */
	@GetMapping("/tags/{key}/delete")
	public ResponseEntity<String> bruciaTagUrl(@PathVariable String key) throws URISyntaxException {
		log.debug("REST request to brucia Tag : {}", key);
		tagService.brucia(key);
		return ResponseEntity.ok()
				.contentType(MediaType.TEXT_PLAIN)
				.body("Numero bruciato " + key);
	}

	/**
	 * {@code GET  /tags/:id} : get the "id" tag.
	 *
	 * @param id the id of the tagDTO to retrieve.
	 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
	 *         the tagDTO, or with status {@code 404 (Not Found)}.
	 */
	@GetMapping("/tags/{key}")
	public ResponseEntity<TagDTO> getTag(@PathVariable String key) {
		log.debug("REST request to get Tag : {}", key);
		Optional<TagDTO> tagDTO = tagService.findOne(key);
		return ResponseUtil.wrapOrNotFound(tagDTO);
	}

	/**
	 * {@code GET  /tags/:id} : get the "id" tag.
	 *
	 * @param id the id of the tagDTO to retrieve.
	 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
	 *         the tagDTO, or with status {@code 404 (Not Found)}.
	 */
	@GetMapping("/tags/current")
	public ResponseEntity<TagDTO> getCurrentTag() {
		log.debug("REST request to get current Tag");
		Optional<TagDTO> tagDTO = tagService.findCurrentTag();
		return ResponseUtil.wrapOrNotFound(tagDTO);
	}

	/**
	 * {@code GET  /tags/:id} : get the "id" tag.
	 *
	 * @param id the id of the tagDTO to retrieve.
	 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
	 *         the tagDTO, or with status {@code 404 (Not Found)}.
	 */
	@GetMapping("/tags/next")
	public ResponseEntity<TagDTO> getNextTag() {
		log.debug("REST request to get next Tag");
		Optional<TagDTO> tagDTO = tagService.findNextTag();
		return ResponseUtil.wrapOrNotFound(tagDTO);
	}

	@GetMapping("/tags/last")
	public ResponseEntity<TagDTO> getLastTag() {
		log.debug("REST request to get last Tag");
		Optional<TagDTO> tagDTO = tagService.findLastTag();
		return ResponseUtil.wrapOrNotFound(tagDTO);
	}

	@GetMapping("/stats/{key}")
	public ResponseEntity<TagStatsDTO> getStatsTag(@PathVariable String key) {
		log.debug("REST request to get stats Tag");
		Optional<TagStatsDTO> stats = tagService.getStats(key);
		return stats.map(s -> ResponseEntity.ok().body(s))
				.orElseGet(() -> ResponseEntity.status(HttpStatus.FORBIDDEN).build());
	}

	@GetMapping("/stats")
	public ResponseEntity<TagStatsDTO> getStats() {
		log.debug("REST request to get stats");
		Optional<TagStatsDTO> stats = tagService.getStats(null);
		return ResponseUtil.wrapOrNotFound(stats);
	}

	/**
	 * {@code GET  /tags/:id} : get the "id" tag.
	 *
	 * @param id the id of the tagDTO to retrieve.
	 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
	 *         the tagDTO, or with status {@code 404 (Not Found)}.
	 */
	@GetMapping("/tags/{key}.png")
	public ResponseEntity<byte[]> getCode(@PathVariable String key, HttpServletRequest request) {
		log.debug("REST request qr code Tag : {}", key);
		String url = ServletUriComponentsBuilder.fromRequest(request).replacePath("/api/tags/{key}/delete").build(key)
				.toASCIIString();
		log.debug("url: {}", url);
		byte[] png = tagService.generateQRCode(key, url);
		return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(png);
	}

}
