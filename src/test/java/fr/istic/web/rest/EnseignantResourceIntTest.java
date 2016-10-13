package fr.istic.web.rest;

import fr.istic.ProjectTaaGliApp;

import fr.istic.domain.Enseignant;
import fr.istic.repository.EnseignantRepository;
import fr.istic.repository.search.EnseignantSearchRepository;

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
 * Test class for the EnseignantResource REST controller.
 *
 * @see EnseignantResource
 */
@RunWith(SpringRunner.class)

@SpringBootTest(classes = ProjectTaaGliApp.class)

public class EnseignantResourceIntTest {
    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").withZone(ZoneId.of("Z"));
    private static final String DEFAULT_SESAME = "AAAAA";
    private static final String UPDATED_SESAME = "BBBBB";
    private static final String DEFAULT_SEXE = "AAAAA";
    private static final String UPDATED_SEXE = "BBBBB";
    private static final String DEFAULT_NOM = "AAAAA";
    private static final String UPDATED_NOM = "BBBBB";
    private static final String DEFAULT_PRENOM = "AAAAA";
    private static final String UPDATED_PRENOM = "BBBBB";
    private static final String DEFAULT_ADRESSE = "AAAAA";
    private static final String UPDATED_ADRESSE = "BBBBB";
    private static final String DEFAULT_TEL_PRO = "AAAAA";
    private static final String UPDATED_TEL_PRO = "BBBBB";

    private static final Boolean DEFAULT_ACTIF = false;
    private static final Boolean UPDATED_ACTIF = true;

    private static final ZonedDateTime DEFAULT_DATE_MAJ = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_DATE_MAJ = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_DATE_MAJ_STR = dateTimeFormatter.format(DEFAULT_DATE_MAJ);

    @Inject
    private EnseignantRepository enseignantRepository;

    @Inject
    private EnseignantSearchRepository enseignantSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restEnseignantMockMvc;

    private Enseignant enseignant;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        EnseignantResource enseignantResource = new EnseignantResource();
        ReflectionTestUtils.setField(enseignantResource, "enseignantSearchRepository", enseignantSearchRepository);
        ReflectionTestUtils.setField(enseignantResource, "enseignantRepository", enseignantRepository);
        this.restEnseignantMockMvc = MockMvcBuilders.standaloneSetup(enseignantResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Enseignant createEntity(EntityManager em) {
        Enseignant enseignant = new Enseignant()
                .sesame(DEFAULT_SESAME)
                .sexe(DEFAULT_SEXE)
                .nom(DEFAULT_NOM)
                .prenom(DEFAULT_PRENOM)
                .adresse(DEFAULT_ADRESSE)
                .telPro(DEFAULT_TEL_PRO)
                .actif(DEFAULT_ACTIF)
                .dateMaj(DEFAULT_DATE_MAJ);
        return enseignant;
    }

    @Before
    public void initTest() {
        enseignantSearchRepository.deleteAll();
        enseignant = createEntity(em);
    }

    @Test
    @Transactional
    public void createEnseignant() throws Exception {
        int databaseSizeBeforeCreate = enseignantRepository.findAll().size();

        // Create the Enseignant

        restEnseignantMockMvc.perform(post("/api/enseignants")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(enseignant)))
                .andExpect(status().isCreated());

        // Validate the Enseignant in the database
        List<Enseignant> enseignants = enseignantRepository.findAll();
        assertThat(enseignants).hasSize(databaseSizeBeforeCreate + 1);
        Enseignant testEnseignant = enseignants.get(enseignants.size() - 1);
        assertThat(testEnseignant.getSesame()).isEqualTo(DEFAULT_SESAME);
        assertThat(testEnseignant.getSexe()).isEqualTo(DEFAULT_SEXE);
        assertThat(testEnseignant.getNom()).isEqualTo(DEFAULT_NOM);
        assertThat(testEnseignant.getPrenom()).isEqualTo(DEFAULT_PRENOM);
        assertThat(testEnseignant.getAdresse()).isEqualTo(DEFAULT_ADRESSE);
        assertThat(testEnseignant.getTelPro()).isEqualTo(DEFAULT_TEL_PRO);
        assertThat(testEnseignant.isActif()).isEqualTo(DEFAULT_ACTIF);
        assertThat(testEnseignant.getDateMaj()).isEqualTo(DEFAULT_DATE_MAJ);

        // Validate the Enseignant in ElasticSearch
        Enseignant enseignantEs = enseignantSearchRepository.findOne(testEnseignant.getId());
        assertThat(enseignantEs).isEqualToComparingFieldByField(testEnseignant);
    }

    @Test
    @Transactional
    public void checkSesameIsRequired() throws Exception {
        int databaseSizeBeforeTest = enseignantRepository.findAll().size();
        // set the field null
        enseignant.setSesame(null);

        // Create the Enseignant, which fails.

        restEnseignantMockMvc.perform(post("/api/enseignants")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(enseignant)))
                .andExpect(status().isBadRequest());

        List<Enseignant> enseignants = enseignantRepository.findAll();
        assertThat(enseignants).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkNomIsRequired() throws Exception {
        int databaseSizeBeforeTest = enseignantRepository.findAll().size();
        // set the field null
        enseignant.setNom(null);

        // Create the Enseignant, which fails.

        restEnseignantMockMvc.perform(post("/api/enseignants")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(enseignant)))
                .andExpect(status().isBadRequest());

        List<Enseignant> enseignants = enseignantRepository.findAll();
        assertThat(enseignants).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPrenomIsRequired() throws Exception {
        int databaseSizeBeforeTest = enseignantRepository.findAll().size();
        // set the field null
        enseignant.setPrenom(null);

        // Create the Enseignant, which fails.

        restEnseignantMockMvc.perform(post("/api/enseignants")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(enseignant)))
                .andExpect(status().isBadRequest());

        List<Enseignant> enseignants = enseignantRepository.findAll();
        assertThat(enseignants).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkActifIsRequired() throws Exception {
        int databaseSizeBeforeTest = enseignantRepository.findAll().size();
        // set the field null
        enseignant.setActif(null);

        // Create the Enseignant, which fails.

        restEnseignantMockMvc.perform(post("/api/enseignants")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(enseignant)))
                .andExpect(status().isBadRequest());

        List<Enseignant> enseignants = enseignantRepository.findAll();
        assertThat(enseignants).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDateMajIsRequired() throws Exception {
        int databaseSizeBeforeTest = enseignantRepository.findAll().size();
        // set the field null
        enseignant.setDateMaj(null);

        // Create the Enseignant, which fails.

        restEnseignantMockMvc.perform(post("/api/enseignants")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(enseignant)))
                .andExpect(status().isBadRequest());

        List<Enseignant> enseignants = enseignantRepository.findAll();
        assertThat(enseignants).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllEnseignants() throws Exception {
        // Initialize the database
        enseignantRepository.saveAndFlush(enseignant);

        // Get all the enseignants
        restEnseignantMockMvc.perform(get("/api/enseignants?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(enseignant.getId().intValue())))
                .andExpect(jsonPath("$.[*].sesame").value(hasItem(DEFAULT_SESAME.toString())))
                .andExpect(jsonPath("$.[*].sexe").value(hasItem(DEFAULT_SEXE.toString())))
                .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM.toString())))
                .andExpect(jsonPath("$.[*].prenom").value(hasItem(DEFAULT_PRENOM.toString())))
                .andExpect(jsonPath("$.[*].adresse").value(hasItem(DEFAULT_ADRESSE.toString())))
                .andExpect(jsonPath("$.[*].telPro").value(hasItem(DEFAULT_TEL_PRO.toString())))
                .andExpect(jsonPath("$.[*].actif").value(hasItem(DEFAULT_ACTIF.booleanValue())))
                .andExpect(jsonPath("$.[*].dateMaj").value(hasItem(DEFAULT_DATE_MAJ_STR)));
    }

    @Test
    @Transactional
    public void getEnseignant() throws Exception {
        // Initialize the database
        enseignantRepository.saveAndFlush(enseignant);

        // Get the enseignant
        restEnseignantMockMvc.perform(get("/api/enseignants/{id}", enseignant.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(enseignant.getId().intValue()))
            .andExpect(jsonPath("$.sesame").value(DEFAULT_SESAME.toString()))
            .andExpect(jsonPath("$.sexe").value(DEFAULT_SEXE.toString()))
            .andExpect(jsonPath("$.nom").value(DEFAULT_NOM.toString()))
            .andExpect(jsonPath("$.prenom").value(DEFAULT_PRENOM.toString()))
            .andExpect(jsonPath("$.adresse").value(DEFAULT_ADRESSE.toString()))
            .andExpect(jsonPath("$.telPro").value(DEFAULT_TEL_PRO.toString()))
            .andExpect(jsonPath("$.actif").value(DEFAULT_ACTIF.booleanValue()))
            .andExpect(jsonPath("$.dateMaj").value(DEFAULT_DATE_MAJ_STR));
    }

    @Test
    @Transactional
    public void getNonExistingEnseignant() throws Exception {
        // Get the enseignant
        restEnseignantMockMvc.perform(get("/api/enseignants/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateEnseignant() throws Exception {
        // Initialize the database
        enseignantRepository.saveAndFlush(enseignant);
        enseignantSearchRepository.save(enseignant);
        int databaseSizeBeforeUpdate = enseignantRepository.findAll().size();

        // Update the enseignant
        Enseignant updatedEnseignant = enseignantRepository.findOne(enseignant.getId());
        updatedEnseignant
                .sesame(UPDATED_SESAME)
                .sexe(UPDATED_SEXE)
                .nom(UPDATED_NOM)
                .prenom(UPDATED_PRENOM)
                .adresse(UPDATED_ADRESSE)
                .telPro(UPDATED_TEL_PRO)
                .actif(UPDATED_ACTIF)
                .dateMaj(UPDATED_DATE_MAJ);

        restEnseignantMockMvc.perform(put("/api/enseignants")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedEnseignant)))
                .andExpect(status().isOk());

        // Validate the Enseignant in the database
        List<Enseignant> enseignants = enseignantRepository.findAll();
        assertThat(enseignants).hasSize(databaseSizeBeforeUpdate);
        Enseignant testEnseignant = enseignants.get(enseignants.size() - 1);
        assertThat(testEnseignant.getSesame()).isEqualTo(UPDATED_SESAME);
        assertThat(testEnseignant.getSexe()).isEqualTo(UPDATED_SEXE);
        assertThat(testEnseignant.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testEnseignant.getPrenom()).isEqualTo(UPDATED_PRENOM);
        assertThat(testEnseignant.getAdresse()).isEqualTo(UPDATED_ADRESSE);
        assertThat(testEnseignant.getTelPro()).isEqualTo(UPDATED_TEL_PRO);
        assertThat(testEnseignant.isActif()).isEqualTo(UPDATED_ACTIF);
        assertThat(testEnseignant.getDateMaj()).isEqualTo(UPDATED_DATE_MAJ);

        // Validate the Enseignant in ElasticSearch
        Enseignant enseignantEs = enseignantSearchRepository.findOne(testEnseignant.getId());
        assertThat(enseignantEs).isEqualToComparingFieldByField(testEnseignant);
    }

    @Test
    @Transactional
    public void deleteEnseignant() throws Exception {
        // Initialize the database
        enseignantRepository.saveAndFlush(enseignant);
        enseignantSearchRepository.save(enseignant);
        int databaseSizeBeforeDelete = enseignantRepository.findAll().size();

        // Get the enseignant
        restEnseignantMockMvc.perform(delete("/api/enseignants/{id}", enseignant.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean enseignantExistsInEs = enseignantSearchRepository.exists(enseignant.getId());
        assertThat(enseignantExistsInEs).isFalse();

        // Validate the database is empty
        List<Enseignant> enseignants = enseignantRepository.findAll();
        assertThat(enseignants).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchEnseignant() throws Exception {
        // Initialize the database
        enseignantRepository.saveAndFlush(enseignant);
        enseignantSearchRepository.save(enseignant);

        // Search the enseignant
        restEnseignantMockMvc.perform(get("/api/_search/enseignants?query=id:" + enseignant.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(enseignant.getId().intValue())))
            .andExpect(jsonPath("$.[*].sesame").value(hasItem(DEFAULT_SESAME.toString())))
            .andExpect(jsonPath("$.[*].sexe").value(hasItem(DEFAULT_SEXE.toString())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM.toString())))
            .andExpect(jsonPath("$.[*].prenom").value(hasItem(DEFAULT_PRENOM.toString())))
            .andExpect(jsonPath("$.[*].adresse").value(hasItem(DEFAULT_ADRESSE.toString())))
            .andExpect(jsonPath("$.[*].telPro").value(hasItem(DEFAULT_TEL_PRO.toString())))
            .andExpect(jsonPath("$.[*].actif").value(hasItem(DEFAULT_ACTIF.booleanValue())))
            .andExpect(jsonPath("$.[*].dateMaj").value(hasItem(DEFAULT_DATE_MAJ_STR)));
    }
}
