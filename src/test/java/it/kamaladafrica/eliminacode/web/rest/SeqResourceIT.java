package it.kamaladafrica.eliminacode.web.rest;

import it.kamaladafrica.eliminacode.EliminacodeApp;
import it.kamaladafrica.eliminacode.domain.Seq;
import it.kamaladafrica.eliminacode.repository.SeqRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link SeqResource} REST controller.
 */
@SpringBootTest(classes = EliminacodeApp.class)

@AutoConfigureMockMvc
@WithMockUser
public class SeqResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Long DEFAULT_STEP = 1L;
    private static final Long UPDATED_STEP = 2L;

    private static final Long DEFAULT_NEXT_VALUE = 1L;
    private static final Long UPDATED_NEXT_VALUE = 2L;

    @Autowired
    private SeqRepository seqRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSeqMockMvc;

    private Seq seq;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Seq createEntity(EntityManager em) {
        Seq seq = new Seq()
            .name(DEFAULT_NAME)
            .step(DEFAULT_STEP)
            .nextValue(DEFAULT_NEXT_VALUE);
        return seq;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Seq createUpdatedEntity(EntityManager em) {
        Seq seq = new Seq()
            .name(UPDATED_NAME)
            .step(UPDATED_STEP)
            .nextValue(UPDATED_NEXT_VALUE);
        return seq;
    }

    @BeforeEach
    public void initTest() {
        seq = createEntity(em);
    }

    @Test
    @Transactional
    public void createSeq() throws Exception {
        int databaseSizeBeforeCreate = seqRepository.findAll().size();

        // Create the Seq
        restSeqMockMvc.perform(post("/api/seqs")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(seq)))
            .andExpect(status().isCreated());

        // Validate the Seq in the database
        List<Seq> seqList = seqRepository.findAll();
        assertThat(seqList).hasSize(databaseSizeBeforeCreate + 1);
        Seq testSeq = seqList.get(seqList.size() - 1);
        assertThat(testSeq.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testSeq.getStep()).isEqualTo(DEFAULT_STEP);
        assertThat(testSeq.getNextValue()).isEqualTo(DEFAULT_NEXT_VALUE);
    }

    @Test
    @Transactional
    public void createSeqWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = seqRepository.findAll().size();

        // Create the Seq with an existing ID
        seq.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSeqMockMvc.perform(post("/api/seqs")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(seq)))
            .andExpect(status().isBadRequest());

        // Validate the Seq in the database
        List<Seq> seqList = seqRepository.findAll();
        assertThat(seqList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = seqRepository.findAll().size();
        // set the field null
        seq.setName(null);

        // Create the Seq, which fails.

        restSeqMockMvc.perform(post("/api/seqs")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(seq)))
            .andExpect(status().isBadRequest());

        List<Seq> seqList = seqRepository.findAll();
        assertThat(seqList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStepIsRequired() throws Exception {
        int databaseSizeBeforeTest = seqRepository.findAll().size();
        // set the field null
        seq.setStep(null);

        // Create the Seq, which fails.

        restSeqMockMvc.perform(post("/api/seqs")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(seq)))
            .andExpect(status().isBadRequest());

        List<Seq> seqList = seqRepository.findAll();
        assertThat(seqList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkNextValueIsRequired() throws Exception {
        int databaseSizeBeforeTest = seqRepository.findAll().size();
        // set the field null
        seq.setNextValue(null);

        // Create the Seq, which fails.

        restSeqMockMvc.perform(post("/api/seqs")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(seq)))
            .andExpect(status().isBadRequest());

        List<Seq> seqList = seqRepository.findAll();
        assertThat(seqList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllSeqs() throws Exception {
        // Initialize the database
        seqRepository.saveAndFlush(seq);

        // Get all the seqList
        restSeqMockMvc.perform(get("/api/seqs?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(seq.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].step").value(hasItem(DEFAULT_STEP.intValue())))
            .andExpect(jsonPath("$.[*].nextValue").value(hasItem(DEFAULT_NEXT_VALUE.intValue())));
    }
    
    @Test
    @Transactional
    public void getSeq() throws Exception {
        // Initialize the database
        seqRepository.saveAndFlush(seq);

        // Get the seq
        restSeqMockMvc.perform(get("/api/seqs/{id}", seq.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(seq.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.step").value(DEFAULT_STEP.intValue()))
            .andExpect(jsonPath("$.nextValue").value(DEFAULT_NEXT_VALUE.intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingSeq() throws Exception {
        // Get the seq
        restSeqMockMvc.perform(get("/api/seqs/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSeq() throws Exception {
        // Initialize the database
        seqRepository.saveAndFlush(seq);

        int databaseSizeBeforeUpdate = seqRepository.findAll().size();

        // Update the seq
        Seq updatedSeq = seqRepository.findById(seq.getId()).get();
        // Disconnect from session so that the updates on updatedSeq are not directly saved in db
        em.detach(updatedSeq);
        updatedSeq
            .name(UPDATED_NAME)
            .step(UPDATED_STEP)
            .nextValue(UPDATED_NEXT_VALUE);

        restSeqMockMvc.perform(put("/api/seqs")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedSeq)))
            .andExpect(status().isOk());

        // Validate the Seq in the database
        List<Seq> seqList = seqRepository.findAll();
        assertThat(seqList).hasSize(databaseSizeBeforeUpdate);
        Seq testSeq = seqList.get(seqList.size() - 1);
        assertThat(testSeq.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testSeq.getStep()).isEqualTo(UPDATED_STEP);
        assertThat(testSeq.getNextValue()).isEqualTo(UPDATED_NEXT_VALUE);
    }

    @Test
    @Transactional
    public void updateNonExistingSeq() throws Exception {
        int databaseSizeBeforeUpdate = seqRepository.findAll().size();

        // Create the Seq

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSeqMockMvc.perform(put("/api/seqs")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(seq)))
            .andExpect(status().isBadRequest());

        // Validate the Seq in the database
        List<Seq> seqList = seqRepository.findAll();
        assertThat(seqList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteSeq() throws Exception {
        // Initialize the database
        seqRepository.saveAndFlush(seq);

        int databaseSizeBeforeDelete = seqRepository.findAll().size();

        // Delete the seq
        restSeqMockMvc.perform(delete("/api/seqs/{id}", seq.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Seq> seqList = seqRepository.findAll();
        assertThat(seqList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
