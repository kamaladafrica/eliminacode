package it.kamaladafrica.eliminacode.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import it.kamaladafrica.eliminacode.domain.Seq;

/**
 * Spring Data repository for the Seq entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SeqRepository extends JpaRepository<Seq, Long> {

	Optional<Seq> findByName(String name);

}
