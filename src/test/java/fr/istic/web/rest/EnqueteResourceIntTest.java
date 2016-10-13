package fr.istic.web.rest;

import fr.istic.ProjectTaaGliApp;

import fr.istic.domain.Enquete;
import fr.istic.repository.EnqueteRepository;
import fr.istic.repository.search.EnqueteSearchRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the EnqueteResource REST controller.
 *
 * @see EnqueteResource
 */
@RunWith(SpringRunner.class)

@SpringBootTest(classes = ProjectTaaGliApp.class)

public class EnqueteResourceIntTest {
    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").withZone(ZoneId.of("Z"));

    private static final ZonedDateTime DEFAULT_DATE_DEBUT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_DATE_DEBUT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_DATE_DEBUT_STR = dateTimeFormatter.format(DEFAULT_DATE_DEBUT);
    private static final String DEFAULT_DUREE_ENQUETE = "AAAAA";
    private static final String UPDATED_DUREE_ENQUETE = "BBBBB";
    private static final String DEFAULT_LIEN = "AAAAA";
    private static final String UPDATED_LIEN = "BBBBB";

    @Inject
    private EnqueteRepository enqueteRepository;

    @Inject
    private EnqueteSearchRepository enqueteSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restEnqueteMockMvc;

    private Enquete enquete;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        EnqueteResource enqueteResource = new EnqueteResource();
        ReflectionTestUtils.setField(enqueteResource, "enqueteSearchRepository", enqueteSearchRepository);
        ReflectionTestUtils.setField(enqueteResource, "enqueteRepository", enqueteRepository);
        this.restEnqueteMockMvc = MockMvcBuilders.standaloneSetup(enqueteResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Enquete createEntity(EntityManager em) {
        Enquete enquete = new Enquete()
                .dateDebut(DEFAULT_DATE_DEBUT)
                .dureeEnquete(DEFAULT_DUREE_ENQUETE)
                .lien(DEFAULT_LIEN);
        return enquete;
    }

    @Before
    public void initTest() {
        enqueteSearchRepository.deleteAll();
        enquete = createEntity(em);
    }

    @Test
    @Transactional
    public void createEnquete() throws Exception {
        int databaseSizeBeforeCreate = enqueteRepository.findAll().size();

        // Create the Enquete

        restEnqueteMockMvc.perform(post("/api/enquetes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(enquete)))
                .andExpect(status().isCreated());

        // Validate the Enquete in the database
        List<Enquete> enquetes = enqueteRepository.findAll();
        assertThat(enquetes).hasSize(databaseSizeBeforeCreate + 1);
        Enquete testEnquete = enquetes.get(enquetes.size() - 1);
        assertThat(testEnquete.getDateDebut()).isEqualTo(DEFAULT_DATE_DEBUT);
        assertThat(testEnquete.getDureeEnquete()).isEqualTo(DEFAULT_DUREE_ENQUETE);
        assertThat(testEnquete.getLien()).isEqualTo(DEFAULT_LIEN);

        // Validate the Enquete in ElasticSearch
        Enquete enqueteEs = enqueteSearchRepository.findOne(testEnquete.getId());
        assertThat(enqueteEs).isEqualToComparingFieldByField(testEnquete);
    }

    @Test
    @Transactional
    public void checkDateDebutIsRequired() throws Exception {
        int databaseSizeBeforeTest = enqueteRepository.findAll().size();
        // set the field null
        enquete.setDateDebut(null);

        // Create the Enquete, which fails.

        restEnqueteMockMvc.perform(post("/api/enquetes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(enquete)))
                .andExpect(status().isBadRequest());

        List<Enquete> enquetes = enqueteRepository.findAll();
        assertThat(enquetes).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkLienIsRequired() throws Exception {
        int databaseSizeBeforeTest = enqueteRepository.findAll().size();
        // set the field null
        enquete.setLien(null);

        // Create the Enquete, which fails.

        restEnqueteMockMvc.perform(post("/api/enquetes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(enquete)))
                .andExpect(status().isBadRequest());

        List<Enquete> enquetes = enqueteRepository.findAll();
        assertThat(enquetes).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllEnquetes() throws Exception {
        // Initialize the database
        enqueteRepository.saveAndFlush(enquete);

        // Get all the enquetes
        restEnqueteMockMvc.perform(get("/api/enquetes?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(enquete.getId().intValue())))
                .andExpect(jsonPath("$.[*].dateDebut").value(hasItem(DEFAULT_DATE_DEBUT_STR)))
                .andExpect(jsonPath("$.[*].dureeEnquete").value(hasItem(DEFAULT_DUREE_ENQUETE.toString())))
                .andExpect(jsonPath("$.[*].lien").value(hasItem(DEFAULT_LIEN.toString())));
    }

    @Test
    @Transactional
    public void getEnquete() throws Exception {
        // Initialize the database
        enqueteRepository.saveAndFlush(enquete);

        // Get the enquete
        restEnqueteMockMvc.perform(get("/api/enquetes/{id}", enquete.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(enquete.getId().intValue()))
            .andExpect(jsonPath("$.dateDebut").value(DEFAULT_DATE_DEBUT_STR))
            .andExpect(jsonPath("$.dureeEnquete").value(DEFAULT_DUREE_ENQUETE.toString()))
            .andExpect(jsonPath("$.lien").value(DEFAULT_LIEN.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingEnquete() throws Exception {
        // Get the enquete
        restEnqueteMockMvc.perform(get("/api/enquetes/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateEnquete() throws Exception {
        // Initialize the database
        enqueteRepository.saveAndFlush(enquete);
        enqueteSearchRepository.save(enquete);
        int databaseSizeBeforeUpdate = enqueteRepository.findAll().size();

        // Update the enquete
        Enquete updatedEnquete = enqueteRepository.findOne(enquete.getId());
        updatedEnquete
                .dateDebut(UPDATED_DATE_DEBUT)
                .dureeEnquete(UPDATED_DUREE_ENQUETE)
                .lien(UPDATED_LIEN);

        restEnqueteMockMvc.perform(put("/api/enquetes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedEnquete)))
                .andExpect(status().isOk());

        // Validate the Enquete in the database
        List<Enquete> enquetes = enqueteRepository.findAll();
        assertThat(enquetes).hasSize(databaseSizeBeforeUpdate);
        Enquete testEnquete = enquetes.get(enquetes.size() - 1);
        assertThat(testEnquete.getDateDebut()).isEqualTo(UPDATED_DATE_DEBUT);
        assertThat(testEnquete.getDureeEnquete()).isEqualTo(UPDATED_DUREE_ENQUETE);
        assertThat(testEnquete.getLien()).isEqualTo(UPDATED_LIEN);

        // Validate the Enquete in ElasticSearch
        Enquete enqueteEs = enqueteSearchRepository.findOne(testEnquete.getId());
        assertThat(enqueteEs).isEqualToComparingFieldByField(testEnquete);
    }

    @Test
    @Transactional
    public void deleteEnquete() throws Exception {
        // Initialize the database
        enqueteRepository.saveAndFlush(enquete);
        enqueteSearchRepository.save(enquete);
        int databaseSizeBeforeDelete = enqueteRepository.findAll().size();

        // Get the enquete
        restEnqueteMockMvc.perform(delete("/api/enquetes/{id}", enquete.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean enqueteExistsInEs = enqueteSearchRepository.exists(enquete.getId());
        assertThat(enqueteExistsInEs).isFalse();

        // Validate the database is empty
        List<Enquete> enquetes = enqueteRepository.findAll();
        assertThat(enquetes).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchEnquete() throws Exception {
        // Initialize the database
        enqueteRepository.saveAndFlush(enquete);
        enqueteSearchRepository.save(enquete);

        // Search the enquete
        restEnqueteMockMvc.perform(get("/api/_search/enquetes?query=id:" + enquete.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(enquete.getId().intValue())))
            .andExpect(jsonPath("$.[*].dateDebut").value(hasItem(DEFAULT_DATE_DEBUT_STR)))
            .andExpect(jsonPath("$.[*].dureeEnquete").value(hasItem(DEFAULT_DUREE_ENQUETE.toString())))
            .andExpect(jsonPath("$.[*].lien").value(hasItem(DEFAULT_LIEN.toString())));
    }
}
