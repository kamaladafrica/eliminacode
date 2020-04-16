package it.kamaladafrica.eliminacode.repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import it.kamaladafrica.eliminacode.domain.Tag;

/**
 * Spring Data repository for the Tag entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TagRepository extends JpaRepository<Tag, Long>, JpaSpecificationExecutor<Tag> {
	Optional<Tag> findByKey(UUID key);

	Page<Tag> findAllByStaccatoGreaterThanEqualAndBruciatoIsNotNull(Instant staccato, Pageable page);

	Page<Tag> findAllByStaccatoGreaterThanEqualAndBruciatoIsNull(Instant staccato, Pageable page);

	List<Tag> findTop1000ByBruciatoIsNotNullOrderByBruciatoDesc();

	List<Tag> findAllByStaccatoGreaterThanEqualOrderByProgressivoDesc(Instant staccato);

}
