package it.kamaladafrica.eliminacode.web.rest;

import it.kamaladafrica.eliminacode.domain.Seq;
import it.kamaladafrica.eliminacode.repository.SeqRepository;
import it.kamaladafrica.eliminacode.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link it.kamaladafrica.eliminacode.domain.Seq}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class SeqResource {

    private final Logger log = LoggerFactory.getLogger(SeqResource.class);

    private static final String ENTITY_NAME = "seq";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SeqRepository seqRepository;

    public SeqResource(SeqRepository seqRepository) {
        this.seqRepository = seqRepository;
    }

    /**
     * {@code POST  /seqs} : Create a new seq.
     *
     * @param seq the seq to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new seq, or with status {@code 400 (Bad Request)} if the seq has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/seqs")
    public ResponseEntity<Seq> createSeq(@Valid @RequestBody Seq seq) throws URISyntaxException {
        log.debug("REST request to save Seq : {}", seq);
        if (seq.getId() != null) {
            throw new BadRequestAlertException("A new seq cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Seq result = seqRepository.save(seq);
        return ResponseEntity.created(new URI("/api/seqs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /seqs} : Updates an existing seq.
     *
     * @param seq the seq to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated seq,
     * or with status {@code 400 (Bad Request)} if the seq is not valid,
     * or with status {@code 500 (Internal Server Error)} if the seq couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/seqs")
    public ResponseEntity<Seq> updateSeq(@Valid @RequestBody Seq seq) throws URISyntaxException {
        log.debug("REST request to update Seq : {}", seq);
        if (seq.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Seq result = seqRepository.save(seq);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, seq.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /seqs} : get all the seqs.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of seqs in body.
     */
    @GetMapping("/seqs")
    public List<Seq> getAllSeqs() {
        log.debug("REST request to get all Seqs");
        return seqRepository.findAll();
    }

    /**
     * {@code GET  /seqs/:id} : get the "id" seq.
     *
     * @param id the id of the seq to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the seq, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/seqs/{id}")
    public ResponseEntity<Seq> getSeq(@PathVariable Long id) {
        log.debug("REST request to get Seq : {}", id);
        Optional<Seq> seq = seqRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(seq);
    }

    /**
     * {@code DELETE  /seqs/:id} : delete the "id" seq.
     *
     * @param id the id of the seq to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/seqs/{id}")
    public ResponseEntity<Void> deleteSeq(@PathVariable Long id) {
        log.debug("REST request to delete Seq : {}", id);
        seqRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
