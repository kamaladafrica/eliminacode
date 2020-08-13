package it.kamaladafrica.eliminacode.web.rest;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.github.jhipster.service.filter.InstantFilter;
import io.github.jhipster.service.filter.UUIDFilter;
import io.github.jhipster.web.util.ResponseUtil;
import it.kamaladafrica.eliminacode.service.TagQueryService;
import it.kamaladafrica.eliminacode.service.dto.TagCriteria;
import it.kamaladafrica.eliminacode.service.dto.TagDTO;

/**
 * REST controller for managing {@link it.kamaladafrica.eliminacode.domain.Tag}.
 */
@RestController
@RequestMapping("/api/admin")
public class TagAdminResource {

	private final Logger log = LoggerFactory.getLogger(TagAdminResource.class);

	@Value("${jhipster.clientApp.name}")
	private String applicationName;

	private final TagQueryService tagQueryService;

	public TagAdminResource(TagQueryService tagQueryService) {
		this.tagQueryService = tagQueryService;
	}

	/**
	 * {@code GET  /tags} : get all the tags.
	 *
	 * @param criteria the criteria which the requested entities should match.
	 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list
	 *         of tags in body.
	 */
	@GetMapping("/tags")
	public ResponseEntity<List<TagDTO>> getAllTags(TagCriteria criteria) {
		log.debug("REST request to get Tags by criteria: {}", criteria);
		List<TagDTO> entityList = tagQueryService.findByCriteria(criteria);
		return ResponseEntity.ok().body(entityList);
	}

	@GetMapping("/tags/{key}")
	public ResponseEntity<TagDTO> getTag(@PathVariable String key) {
		TagCriteria criteria = new TagCriteria();
		UUIDFilter filter = new UUIDFilter();
		filter.setEquals(UUID.fromString(key));
		criteria.setKey(filter);
		List<TagDTO> entityList = tagQueryService.findByCriteria(criteria);
		return ResponseUtil.wrapOrNotFound(entityList.stream().findAny());
	}

	@GetMapping("/tags/today")
	public ResponseEntity<List<TagDTO>> getTodayTags() {
		TagCriteria criteria = new TagCriteria();
		InstantFilter filter = new InstantFilter();
		filter.setGreaterThanOrEqual(Instant.now().truncatedTo(ChronoUnit.DAYS));
		criteria.setStaccato(filter);
		List<TagDTO> entityList = tagQueryService.findByCriteria(criteria);
		return ResponseEntity.ok().body(entityList);
	}

}
