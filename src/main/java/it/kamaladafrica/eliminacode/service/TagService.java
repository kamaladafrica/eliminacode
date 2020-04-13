package it.kamaladafrica.eliminacode.service;

import java.time.Instant;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.kamaladafrica.eliminacode.domain.Tag;
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
	 * Get one tag by id.
	 *
	 * @param id the id of the entity.
	 * @return the entity.
	 */
	@Transactional(readOnly = true)
	public Optional<TagDTO> findOne(String key) {
		return tagRepository.findByKey(key)
				.map(tagMapper::toDto);
	}

	public void brucia(String key) {
		tagRepository.findByKey(key)
				.ifPresent(tag -> {
					tag.setBruciato(Instant.now());
					tagRepository.save(tag);
				});
	}
}
