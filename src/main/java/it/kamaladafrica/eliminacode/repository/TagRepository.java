package it.kamaladafrica.eliminacode.repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import it.kamaladafrica.eliminacode.domain.Tag;

/**
 * Spring Data repository for the Tag entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TagRepository extends JpaRepository<Tag, Long>, JpaSpecificationExecutor<Tag> {

	Optional<Tag> findByKey(UUID key);

	@Query("select t.progressivo from Tag t where t.staccato >= CURRENT_DATE and t.progressivo > nvl((select max(x.progressivo) from Tag x where x.bruciato = (select max(y.bruciato) from Tag y)), 0) order by t.progressivo asc")
	List<Long> findNextProgressivi();

	@Query("select max(t.bruciato) from Tag t where t.staccato >= CURRENT_DATE")
	Optional<Instant> getLastBruciatoInstantOfToday();

	@Query("select t from Tag t where t.staccato >= CURRENT_DATE and t.progressivo = (select max(x.progressivo) from Tag x where x.bruciato = (select max(y.bruciato) from Tag y))")
	Optional<Tag> getLastBruciatoOfToday();

	@Query("select t from Tag t where key = :key and t.staccato >= CURRENT_DATE and t.bruciato is null")
	Optional<Tag> findByKeyNotBruciatoOfToday(@Param("key") UUID key);

}
