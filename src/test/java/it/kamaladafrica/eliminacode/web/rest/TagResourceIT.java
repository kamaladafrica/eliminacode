package it.kamaladafrica.eliminacode.web.rest;

import it.kamaladafrica.eliminacode.EliminacodeApp;
import it.kamaladafrica.eliminacode.domain.Tag;
import it.kamaladafrica.eliminacode.repository.TagRepository;
import it.kamaladafrica.eliminacode.service.TagService;
import it.kamaladafrica.eliminacode.service.dto.TagDTO;
import it.kamaladafrica.eliminacode.service.mapper.TagMapper;
import it.kamaladafrica.eliminacode.service.dto.TagCriteria;
import it.kamaladafrica.eliminacode.service.TagQueryService;

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
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link TagResource} REST controller.
 */
@SpringBootTest(classes = EliminacodeApp.class)

@AutoConfigureMockMvc
@WithMockUser
public class TagResourceIT {

    private static final Long DEFAULT_PROGRESSIVO = 1L;
    private static final Long UPDATED_PROGRESSIVO = 2L;
    private static final Long SMALLER_PROGRESSIVO = 1L - 1L;

    private static final Instant DEFAULT_STACCATO = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_STACCATO = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_BRUCIATO = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_BRUCIATO = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final UUID DEFAULT_KEY = UUID.randomUUID();
    private static final UUID UPDATED_KEY = UUID.randomUUID();

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private TagMapper tagMapper;

    @Autowired
    private TagService tagService;

    @Autowired
    private TagQueryService tagQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTagMockMvc;

    private Tag tag;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Tag createEntity(EntityManager em) {
        Tag tag = new Tag()
            .progressivo(DEFAULT_PROGRESSIVO)
            .staccato(DEFAULT_STACCATO)
            .bruciato(DEFAULT_BRUCIATO)
            .key(DEFAULT_KEY);
        return tag;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Tag createUpdatedEntity(EntityManager em) {
        Tag tag = new Tag()
            .progressivo(UPDATED_PROGRESSIVO)
            .staccato(UPDATED_STACCATO)
            .bruciato(UPDATED_BRUCIATO)
            .key(UPDATED_KEY);
        return tag;
    }

    @BeforeEach
    public void initTest() {
        tag = createEntity(em);
    }

    @Test
    @Transactional
    public void createTag() throws Exception {
        int databaseSizeBeforeCreate = tagRepository.findAll().size();

        // Create the Tag
        TagDTO tagDTO = tagMapper.toDto(tag);
        restTagMockMvc.perform(post("/api/tags").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(tagDTO)))
            .andExpect(status().isCreated());

        // Validate the Tag in the database
        List<Tag> tagList = tagRepository.findAll();
        assertThat(tagList).hasSize(databaseSizeBeforeCreate + 1);
        Tag testTag = tagList.get(tagList.size() - 1);
        assertThat(testTag.getProgressivo()).isEqualTo(DEFAULT_PROGRESSIVO);
        assertThat(testTag.getStaccato()).isEqualTo(DEFAULT_STACCATO);
        assertThat(testTag.getBruciato()).isEqualTo(DEFAULT_BRUCIATO);
        assertThat(testTag.getKey()).isEqualTo(DEFAULT_KEY);
    }

    @Test
    @Transactional
    public void createTagWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = tagRepository.findAll().size();

        // Create the Tag with an existing ID
        tag.setId(1L);
        TagDTO tagDTO = tagMapper.toDto(tag);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTagMockMvc.perform(post("/api/tags").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(tagDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Tag in the database
        List<Tag> tagList = tagRepository.findAll();
        assertThat(tagList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkStaccatoIsRequired() throws Exception {
        int databaseSizeBeforeTest = tagRepository.findAll().size();
        // set the field null
        tag.setStaccato(null);

        // Create the Tag, which fails.
        TagDTO tagDTO = tagMapper.toDto(tag);

        restTagMockMvc.perform(post("/api/tags").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(tagDTO)))
            .andExpect(status().isBadRequest());

        List<Tag> tagList = tagRepository.findAll();
        assertThat(tagList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkKeyIsRequired() throws Exception {
        int databaseSizeBeforeTest = tagRepository.findAll().size();
        // set the field null
        tag.setKey(null);

        // Create the Tag, which fails.
        TagDTO tagDTO = tagMapper.toDto(tag);

        restTagMockMvc.perform(post("/api/tags").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(tagDTO)))
            .andExpect(status().isBadRequest());

        List<Tag> tagList = tagRepository.findAll();
        assertThat(tagList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllTags() throws Exception {
        // Initialize the database
        tagRepository.saveAndFlush(tag);

        // Get all the tagList
        restTagMockMvc.perform(get("/api/tags?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tag.getId().intValue())))
            .andExpect(jsonPath("$.[*].progressivo").value(hasItem(DEFAULT_PROGRESSIVO.intValue())))
            .andExpect(jsonPath("$.[*].staccato").value(hasItem(DEFAULT_STACCATO.toString())))
            .andExpect(jsonPath("$.[*].bruciato").value(hasItem(DEFAULT_BRUCIATO.toString())))
            .andExpect(jsonPath("$.[*].key").value(hasItem(DEFAULT_KEY.toString())));
    }
    
    @Test
    @Transactional
    public void getTag() throws Exception {
        // Initialize the database
        tagRepository.saveAndFlush(tag);

        // Get the tag
        restTagMockMvc.perform(get("/api/tags/{id}", tag.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(tag.getId().intValue()))
            .andExpect(jsonPath("$.progressivo").value(DEFAULT_PROGRESSIVO.intValue()))
            .andExpect(jsonPath("$.staccato").value(DEFAULT_STACCATO.toString()))
            .andExpect(jsonPath("$.bruciato").value(DEFAULT_BRUCIATO.toString()))
            .andExpect(jsonPath("$.key").value(DEFAULT_KEY.toString()));
    }


    @Test
    @Transactional
    public void getTagsByIdFiltering() throws Exception {
        // Initialize the database
        tagRepository.saveAndFlush(tag);

        Long id = tag.getId();

        defaultTagShouldBeFound("id.equals=" + id);
        defaultTagShouldNotBeFound("id.notEquals=" + id);

        defaultTagShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultTagShouldNotBeFound("id.greaterThan=" + id);

        defaultTagShouldBeFound("id.lessThanOrEqual=" + id);
        defaultTagShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllTagsByProgressivoIsEqualToSomething() throws Exception {
        // Initialize the database
        tagRepository.saveAndFlush(tag);

        // Get all the tagList where progressivo equals to DEFAULT_PROGRESSIVO
        defaultTagShouldBeFound("progressivo.equals=" + DEFAULT_PROGRESSIVO);

        // Get all the tagList where progressivo equals to UPDATED_PROGRESSIVO
        defaultTagShouldNotBeFound("progressivo.equals=" + UPDATED_PROGRESSIVO);
    }

    @Test
    @Transactional
    public void getAllTagsByProgressivoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        tagRepository.saveAndFlush(tag);

        // Get all the tagList where progressivo not equals to DEFAULT_PROGRESSIVO
        defaultTagShouldNotBeFound("progressivo.notEquals=" + DEFAULT_PROGRESSIVO);

        // Get all the tagList where progressivo not equals to UPDATED_PROGRESSIVO
        defaultTagShouldBeFound("progressivo.notEquals=" + UPDATED_PROGRESSIVO);
    }

    @Test
    @Transactional
    public void getAllTagsByProgressivoIsInShouldWork() throws Exception {
        // Initialize the database
        tagRepository.saveAndFlush(tag);

        // Get all the tagList where progressivo in DEFAULT_PROGRESSIVO or UPDATED_PROGRESSIVO
        defaultTagShouldBeFound("progressivo.in=" + DEFAULT_PROGRESSIVO + "," + UPDATED_PROGRESSIVO);

        // Get all the tagList where progressivo equals to UPDATED_PROGRESSIVO
        defaultTagShouldNotBeFound("progressivo.in=" + UPDATED_PROGRESSIVO);
    }

    @Test
    @Transactional
    public void getAllTagsByProgressivoIsNullOrNotNull() throws Exception {
        // Initialize the database
        tagRepository.saveAndFlush(tag);

        // Get all the tagList where progressivo is not null
        defaultTagShouldBeFound("progressivo.specified=true");

        // Get all the tagList where progressivo is null
        defaultTagShouldNotBeFound("progressivo.specified=false");
    }

    @Test
    @Transactional
    public void getAllTagsByProgressivoIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        tagRepository.saveAndFlush(tag);

        // Get all the tagList where progressivo is greater than or equal to DEFAULT_PROGRESSIVO
        defaultTagShouldBeFound("progressivo.greaterThanOrEqual=" + DEFAULT_PROGRESSIVO);

        // Get all the tagList where progressivo is greater than or equal to UPDATED_PROGRESSIVO
        defaultTagShouldNotBeFound("progressivo.greaterThanOrEqual=" + UPDATED_PROGRESSIVO);
    }

    @Test
    @Transactional
    public void getAllTagsByProgressivoIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        tagRepository.saveAndFlush(tag);

        // Get all the tagList where progressivo is less than or equal to DEFAULT_PROGRESSIVO
        defaultTagShouldBeFound("progressivo.lessThanOrEqual=" + DEFAULT_PROGRESSIVO);

        // Get all the tagList where progressivo is less than or equal to SMALLER_PROGRESSIVO
        defaultTagShouldNotBeFound("progressivo.lessThanOrEqual=" + SMALLER_PROGRESSIVO);
    }

    @Test
    @Transactional
    public void getAllTagsByProgressivoIsLessThanSomething() throws Exception {
        // Initialize the database
        tagRepository.saveAndFlush(tag);

        // Get all the tagList where progressivo is less than DEFAULT_PROGRESSIVO
        defaultTagShouldNotBeFound("progressivo.lessThan=" + DEFAULT_PROGRESSIVO);

        // Get all the tagList where progressivo is less than UPDATED_PROGRESSIVO
        defaultTagShouldBeFound("progressivo.lessThan=" + UPDATED_PROGRESSIVO);
    }

    @Test
    @Transactional
    public void getAllTagsByProgressivoIsGreaterThanSomething() throws Exception {
        // Initialize the database
        tagRepository.saveAndFlush(tag);

        // Get all the tagList where progressivo is greater than DEFAULT_PROGRESSIVO
        defaultTagShouldNotBeFound("progressivo.greaterThan=" + DEFAULT_PROGRESSIVO);

        // Get all the tagList where progressivo is greater than SMALLER_PROGRESSIVO
        defaultTagShouldBeFound("progressivo.greaterThan=" + SMALLER_PROGRESSIVO);
    }


    @Test
    @Transactional
    public void getAllTagsByStaccatoIsEqualToSomething() throws Exception {
        // Initialize the database
        tagRepository.saveAndFlush(tag);

        // Get all the tagList where staccato equals to DEFAULT_STACCATO
        defaultTagShouldBeFound("staccato.equals=" + DEFAULT_STACCATO);

        // Get all the tagList where staccato equals to UPDATED_STACCATO
        defaultTagShouldNotBeFound("staccato.equals=" + UPDATED_STACCATO);
    }

    @Test
    @Transactional
    public void getAllTagsByStaccatoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        tagRepository.saveAndFlush(tag);

        // Get all the tagList where staccato not equals to DEFAULT_STACCATO
        defaultTagShouldNotBeFound("staccato.notEquals=" + DEFAULT_STACCATO);

        // Get all the tagList where staccato not equals to UPDATED_STACCATO
        defaultTagShouldBeFound("staccato.notEquals=" + UPDATED_STACCATO);
    }

    @Test
    @Transactional
    public void getAllTagsByStaccatoIsInShouldWork() throws Exception {
        // Initialize the database
        tagRepository.saveAndFlush(tag);

        // Get all the tagList where staccato in DEFAULT_STACCATO or UPDATED_STACCATO
        defaultTagShouldBeFound("staccato.in=" + DEFAULT_STACCATO + "," + UPDATED_STACCATO);

        // Get all the tagList where staccato equals to UPDATED_STACCATO
        defaultTagShouldNotBeFound("staccato.in=" + UPDATED_STACCATO);
    }

    @Test
    @Transactional
    public void getAllTagsByStaccatoIsNullOrNotNull() throws Exception {
        // Initialize the database
        tagRepository.saveAndFlush(tag);

        // Get all the tagList where staccato is not null
        defaultTagShouldBeFound("staccato.specified=true");

        // Get all the tagList where staccato is null
        defaultTagShouldNotBeFound("staccato.specified=false");
    }

    @Test
    @Transactional
    public void getAllTagsByBruciatoIsEqualToSomething() throws Exception {
        // Initialize the database
        tagRepository.saveAndFlush(tag);

        // Get all the tagList where bruciato equals to DEFAULT_BRUCIATO
        defaultTagShouldBeFound("bruciato.equals=" + DEFAULT_BRUCIATO);

        // Get all the tagList where bruciato equals to UPDATED_BRUCIATO
        defaultTagShouldNotBeFound("bruciato.equals=" + UPDATED_BRUCIATO);
    }

    @Test
    @Transactional
    public void getAllTagsByBruciatoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        tagRepository.saveAndFlush(tag);

        // Get all the tagList where bruciato not equals to DEFAULT_BRUCIATO
        defaultTagShouldNotBeFound("bruciato.notEquals=" + DEFAULT_BRUCIATO);

        // Get all the tagList where bruciato not equals to UPDATED_BRUCIATO
        defaultTagShouldBeFound("bruciato.notEquals=" + UPDATED_BRUCIATO);
    }

    @Test
    @Transactional
    public void getAllTagsByBruciatoIsInShouldWork() throws Exception {
        // Initialize the database
        tagRepository.saveAndFlush(tag);

        // Get all the tagList where bruciato in DEFAULT_BRUCIATO or UPDATED_BRUCIATO
        defaultTagShouldBeFound("bruciato.in=" + DEFAULT_BRUCIATO + "," + UPDATED_BRUCIATO);

        // Get all the tagList where bruciato equals to UPDATED_BRUCIATO
        defaultTagShouldNotBeFound("bruciato.in=" + UPDATED_BRUCIATO);
    }

    @Test
    @Transactional
    public void getAllTagsByBruciatoIsNullOrNotNull() throws Exception {
        // Initialize the database
        tagRepository.saveAndFlush(tag);

        // Get all the tagList where bruciato is not null
        defaultTagShouldBeFound("bruciato.specified=true");

        // Get all the tagList where bruciato is null
        defaultTagShouldNotBeFound("bruciato.specified=false");
    }

    @Test
    @Transactional
    public void getAllTagsByKeyIsEqualToSomething() throws Exception {
        // Initialize the database
        tagRepository.saveAndFlush(tag);

        // Get all the tagList where key equals to DEFAULT_KEY
        defaultTagShouldBeFound("key.equals=" + DEFAULT_KEY);

        // Get all the tagList where key equals to UPDATED_KEY
        defaultTagShouldNotBeFound("key.equals=" + UPDATED_KEY);
    }

    @Test
    @Transactional
    public void getAllTagsByKeyIsNotEqualToSomething() throws Exception {
        // Initialize the database
        tagRepository.saveAndFlush(tag);

        // Get all the tagList where key not equals to DEFAULT_KEY
        defaultTagShouldNotBeFound("key.notEquals=" + DEFAULT_KEY);

        // Get all the tagList where key not equals to UPDATED_KEY
        defaultTagShouldBeFound("key.notEquals=" + UPDATED_KEY);
    }

    @Test
    @Transactional
    public void getAllTagsByKeyIsInShouldWork() throws Exception {
        // Initialize the database
        tagRepository.saveAndFlush(tag);

        // Get all the tagList where key in DEFAULT_KEY or UPDATED_KEY
        defaultTagShouldBeFound("key.in=" + DEFAULT_KEY + "," + UPDATED_KEY);

        // Get all the tagList where key equals to UPDATED_KEY
        defaultTagShouldNotBeFound("key.in=" + UPDATED_KEY);
    }

    @Test
    @Transactional
    public void getAllTagsByKeyIsNullOrNotNull() throws Exception {
        // Initialize the database
        tagRepository.saveAndFlush(tag);

        // Get all the tagList where key is not null
        defaultTagShouldBeFound("key.specified=true");

        // Get all the tagList where key is null
        defaultTagShouldNotBeFound("key.specified=false");
    }
    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTagShouldBeFound(String filter) throws Exception {
        restTagMockMvc.perform(get("/api/tags?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tag.getId().intValue())))
            .andExpect(jsonPath("$.[*].progressivo").value(hasItem(DEFAULT_PROGRESSIVO.intValue())))
            .andExpect(jsonPath("$.[*].staccato").value(hasItem(DEFAULT_STACCATO.toString())))
            .andExpect(jsonPath("$.[*].bruciato").value(hasItem(DEFAULT_BRUCIATO.toString())))
            .andExpect(jsonPath("$.[*].key").value(hasItem(DEFAULT_KEY.toString())));

        // Check, that the count call also returns 1
        restTagMockMvc.perform(get("/api/tags/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTagShouldNotBeFound(String filter) throws Exception {
        restTagMockMvc.perform(get("/api/tags?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTagMockMvc.perform(get("/api/tags/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingTag() throws Exception {
        // Get the tag
        restTagMockMvc.perform(get("/api/tags/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTag() throws Exception {
        // Initialize the database
        tagRepository.saveAndFlush(tag);

        int databaseSizeBeforeUpdate = tagRepository.findAll().size();

        // Update the tag
        Tag updatedTag = tagRepository.findById(tag.getId()).get();
        // Disconnect from session so that the updates on updatedTag are not directly saved in db
        em.detach(updatedTag);
        updatedTag
            .progressivo(UPDATED_PROGRESSIVO)
            .staccato(UPDATED_STACCATO)
            .bruciato(UPDATED_BRUCIATO)
            .key(UPDATED_KEY);
        TagDTO tagDTO = tagMapper.toDto(updatedTag);

        restTagMockMvc.perform(put("/api/tags").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(tagDTO)))
            .andExpect(status().isOk());

        // Validate the Tag in the database
        List<Tag> tagList = tagRepository.findAll();
        assertThat(tagList).hasSize(databaseSizeBeforeUpdate);
        Tag testTag = tagList.get(tagList.size() - 1);
        assertThat(testTag.getProgressivo()).isEqualTo(UPDATED_PROGRESSIVO);
        assertThat(testTag.getStaccato()).isEqualTo(UPDATED_STACCATO);
        assertThat(testTag.getBruciato()).isEqualTo(UPDATED_BRUCIATO);
        assertThat(testTag.getKey()).isEqualTo(UPDATED_KEY);
    }

    @Test
    @Transactional
    public void updateNonExistingTag() throws Exception {
        int databaseSizeBeforeUpdate = tagRepository.findAll().size();

        // Create the Tag
        TagDTO tagDTO = tagMapper.toDto(tag);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTagMockMvc.perform(put("/api/tags").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(tagDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Tag in the database
        List<Tag> tagList = tagRepository.findAll();
        assertThat(tagList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteTag() throws Exception {
        // Initialize the database
        tagRepository.saveAndFlush(tag);

        int databaseSizeBeforeDelete = tagRepository.findAll().size();

        // Delete the tag
        restTagMockMvc.perform(delete("/api/tags/{id}", tag.getId()).with(csrf())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Tag> tagList = tagRepository.findAll();
        assertThat(tagList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
