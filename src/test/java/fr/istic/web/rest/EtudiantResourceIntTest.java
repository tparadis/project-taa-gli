package fr.istic.web.rest;

import fr.istic.ProjectTaaGliApp;

import fr.istic.domain.Etudiant;
import fr.istic.repository.EtudiantRepository;
import fr.istic.repository.search.EtudiantSearchRepository;

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
 * Test class for the EtudiantResource REST controller.
 *
 * @see EtudiantResource
 */
@RunWith(SpringRunner.class)

@SpringBootTest(classes = ProjectTaaGliApp.class)

public class EtudiantResourceIntTest {
    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").withZone(ZoneId.of("Z"));
    private static final String DEFAULT_NOM = "AAAAA";
    private static final String UPDATED_NOM = "BBBBB";
    private static final String DEFAULT_PRENOM = "AAAAA";
    private static final String UPDATED_PRENOM = "BBBBB";
    private static final String DEFAULT_SEXE = "AAAAA";
    private static final String UPDATED_SEXE = "BBBBB";
    private static final String DEFAULT_RUE = "AAAAA";
    private static final String UPDATED_RUE = "BBBBB";
    private static final String DEFAULT_VILLE = "AAAAA";
    private static final String UPDATED_VILLE = "BBBBB";
    private static final String DEFAULT_CODE_DEP = "AAAAA";
    private static final String UPDATED_CODE_DEP = "BBBBB";
    private static final String DEFAULT_TELEPHONE = "AAAAA";
    private static final String UPDATED_TELEPHONE = "BBBBB";
    private static final String DEFAULT_PREMIER_EMPLOI = "AAAAA";
    private static final String UPDATED_PREMIER_EMPLOI = "BBBBB";
    private static final String DEFAULT_DERNIER_EMPLOI = "AAAAA";
    private static final String UPDATED_DERNIER_EMPLOI = "BBBBB";

    private static final Boolean DEFAULT_RECH_EMP = false;
    private static final Boolean UPDATED_RECH_EMP = true;

    private static final ZonedDateTime DEFAULT_DATE_MAJ = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_DATE_MAJ = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_DATE_MAJ_STR = dateTimeFormatter.format(DEFAULT_DATE_MAJ);

    @Inject
    private EtudiantRepository etudiantRepository;

    @Inject
    private EtudiantSearchRepository etudiantSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restEtudiantMockMvc;

    private Etudiant etudiant;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        EtudiantResource etudiantResource = new EtudiantResource();
        ReflectionTestUtils.setField(etudiantResource, "etudiantSearchRepository", etudiantSearchRepository);
        ReflectionTestUtils.setField(etudiantResource, "etudiantRepository", etudiantRepository);
        this.restEtudiantMockMvc = MockMvcBuilders.standaloneSetup(etudiantResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Etudiant createEntity(EntityManager em) {
        Etudiant etudiant = new Etudiant()
                .nom(DEFAULT_NOM)
                .prenom(DEFAULT_PRENOM)
                .sexe(DEFAULT_SEXE)
                .rue(DEFAULT_RUE)
                .ville(DEFAULT_VILLE)
                .codeDep(DEFAULT_CODE_DEP)
                .telephone(DEFAULT_TELEPHONE)
                .premierEmploi(DEFAULT_PREMIER_EMPLOI)
                .dernierEmploi(DEFAULT_DERNIER_EMPLOI)
                .rechEmp(DEFAULT_RECH_EMP)
                .dateMaj(DEFAULT_DATE_MAJ);
        return etudiant;
    }

    @Before
    public void initTest() {
        etudiantSearchRepository.deleteAll();
        etudiant = createEntity(em);
    }

    @Test
    @Transactional
    public void createEtudiant() throws Exception {
        int databaseSizeBeforeCreate = etudiantRepository.findAll().size();

        // Create the Etudiant

        restEtudiantMockMvc.perform(post("/api/etudiants")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(etudiant)))
                .andExpect(status().isCreated());

        // Validate the Etudiant in the database
        List<Etudiant> etudiants = etudiantRepository.findAll();
        assertThat(etudiants).hasSize(databaseSizeBeforeCreate + 1);
        Etudiant testEtudiant = etudiants.get(etudiants.size() - 1);
        assertThat(testEtudiant.getNom()).isEqualTo(DEFAULT_NOM);
        assertThat(testEtudiant.getPrenom()).isEqualTo(DEFAULT_PRENOM);
        assertThat(testEtudiant.getSexe()).isEqualTo(DEFAULT_SEXE);
        assertThat(testEtudiant.getRue()).isEqualTo(DEFAULT_RUE);
        assertThat(testEtudiant.getVille()).isEqualTo(DEFAULT_VILLE);
        assertThat(testEtudiant.getCodeDep()).isEqualTo(DEFAULT_CODE_DEP);
        assertThat(testEtudiant.getTelephone()).isEqualTo(DEFAULT_TELEPHONE);
        assertThat(testEtudiant.getPremierEmploi()).isEqualTo(DEFAULT_PREMIER_EMPLOI);
        assertThat(testEtudiant.getDernierEmploi()).isEqualTo(DEFAULT_DERNIER_EMPLOI);
        assertThat(testEtudiant.isRechEmp()).isEqualTo(DEFAULT_RECH_EMP);
        assertThat(testEtudiant.getDateMaj()).isEqualTo(DEFAULT_DATE_MAJ);

        // Validate the Etudiant in ElasticSearch
        Etudiant etudiantEs = etudiantSearchRepository.findOne(testEtudiant.getId());
        assertThat(etudiantEs).isEqualToComparingFieldByField(testEtudiant);
    }

    @Test
    @Transactional
    public void checkNomIsRequired() throws Exception {
        int databaseSizeBeforeTest = etudiantRepository.findAll().size();
        // set the field null
        etudiant.setNom(null);

        // Create the Etudiant, which fails.

        restEtudiantMockMvc.perform(post("/api/etudiants")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(etudiant)))
                .andExpect(status().isBadRequest());

        List<Etudiant> etudiants = etudiantRepository.findAll();
        assertThat(etudiants).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPrenomIsRequired() throws Exception {
        int databaseSizeBeforeTest = etudiantRepository.findAll().size();
        // set the field null
        etudiant.setPrenom(null);

        // Create the Etudiant, which fails.

        restEtudiantMockMvc.perform(post("/api/etudiants")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(etudiant)))
                .andExpect(status().isBadRequest());

        List<Etudiant> etudiants = etudiantRepository.findAll();
        assertThat(etudiants).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDateMajIsRequired() throws Exception {
        int databaseSizeBeforeTest = etudiantRepository.findAll().size();
        // set the field null
        etudiant.setDateMaj(null);

        // Create the Etudiant, which fails.

        restEtudiantMockMvc.perform(post("/api/etudiants")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(etudiant)))
                .andExpect(status().isBadRequest());

        List<Etudiant> etudiants = etudiantRepository.findAll();
        assertThat(etudiants).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllEtudiants() throws Exception {
        // Initialize the database
        etudiantRepository.saveAndFlush(etudiant);

        // Get all the etudiants
        restEtudiantMockMvc.perform(get("/api/etudiants?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(etudiant.getId().intValue())))
                .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM.toString())))
                .andExpect(jsonPath("$.[*].prenom").value(hasItem(DEFAULT_PRENOM.toString())))
                .andExpect(jsonPath("$.[*].sexe").value(hasItem(DEFAULT_SEXE.toString())))
                .andExpect(jsonPath("$.[*].rue").value(hasItem(DEFAULT_RUE.toString())))
                .andExpect(jsonPath("$.[*].ville").value(hasItem(DEFAULT_VILLE.toString())))
                .andExpect(jsonPath("$.[*].codeDep").value(hasItem(DEFAULT_CODE_DEP.toString())))
                .andExpect(jsonPath("$.[*].telephone").value(hasItem(DEFAULT_TELEPHONE.toString())))
                .andExpect(jsonPath("$.[*].premierEmploi").value(hasItem(DEFAULT_PREMIER_EMPLOI.toString())))
                .andExpect(jsonPath("$.[*].dernierEmploi").value(hasItem(DEFAULT_DERNIER_EMPLOI.toString())))
                .andExpect(jsonPath("$.[*].rechEmp").value(hasItem(DEFAULT_RECH_EMP.booleanValue())))
                .andExpect(jsonPath("$.[*].dateMaj").value(hasItem(DEFAULT_DATE_MAJ_STR)));
    }

    @Test
    @Transactional
    public void getEtudiant() throws Exception {
        // Initialize the database
        etudiantRepository.saveAndFlush(etudiant);

        // Get the etudiant
        restEtudiantMockMvc.perform(get("/api/etudiants/{id}", etudiant.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(etudiant.getId().intValue()))
            .andExpect(jsonPath("$.nom").value(DEFAULT_NOM.toString()))
            .andExpect(jsonPath("$.prenom").value(DEFAULT_PRENOM.toString()))
            .andExpect(jsonPath("$.sexe").value(DEFAULT_SEXE.toString()))
            .andExpect(jsonPath("$.rue").value(DEFAULT_RUE.toString()))
            .andExpect(jsonPath("$.ville").value(DEFAULT_VILLE.toString()))
            .andExpect(jsonPath("$.codeDep").value(DEFAULT_CODE_DEP.toString()))
            .andExpect(jsonPath("$.telephone").value(DEFAULT_TELEPHONE.toString()))
            .andExpect(jsonPath("$.premierEmploi").value(DEFAULT_PREMIER_EMPLOI.toString()))
            .andExpect(jsonPath("$.dernierEmploi").value(DEFAULT_DERNIER_EMPLOI.toString()))
            .andExpect(jsonPath("$.rechEmp").value(DEFAULT_RECH_EMP.booleanValue()))
            .andExpect(jsonPath("$.dateMaj").value(DEFAULT_DATE_MAJ_STR));
    }

    @Test
    @Transactional
    public void getNonExistingEtudiant() throws Exception {
        // Get the etudiant
        restEtudiantMockMvc.perform(get("/api/etudiants/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateEtudiant() throws Exception {
        // Initialize the database
        etudiantRepository.saveAndFlush(etudiant);
        etudiantSearchRepository.save(etudiant);
        int databaseSizeBeforeUpdate = etudiantRepository.findAll().size();

        // Update the etudiant
        Etudiant updatedEtudiant = etudiantRepository.findOne(etudiant.getId());
        updatedEtudiant
                .nom(UPDATED_NOM)
                .prenom(UPDATED_PRENOM)
                .sexe(UPDATED_SEXE)
                .rue(UPDATED_RUE)
                .ville(UPDATED_VILLE)
                .codeDep(UPDATED_CODE_DEP)
                .telephone(UPDATED_TELEPHONE)
                .premierEmploi(UPDATED_PREMIER_EMPLOI)
                .dernierEmploi(UPDATED_DERNIER_EMPLOI)
                .rechEmp(UPDATED_RECH_EMP)
                .dateMaj(UPDATED_DATE_MAJ);

        restEtudiantMockMvc.perform(put("/api/etudiants")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedEtudiant)))
                .andExpect(status().isOk());

        // Validate the Etudiant in the database
        List<Etudiant> etudiants = etudiantRepository.findAll();
        assertThat(etudiants).hasSize(databaseSizeBeforeUpdate);
        Etudiant testEtudiant = etudiants.get(etudiants.size() - 1);
        assertThat(testEtudiant.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testEtudiant.getPrenom()).isEqualTo(UPDATED_PRENOM);
        assertThat(testEtudiant.getSexe()).isEqualTo(UPDATED_SEXE);
        assertThat(testEtudiant.getRue()).isEqualTo(UPDATED_RUE);
        assertThat(testEtudiant.getVille()).isEqualTo(UPDATED_VILLE);
        assertThat(testEtudiant.getCodeDep()).isEqualTo(UPDATED_CODE_DEP);
        assertThat(testEtudiant.getTelephone()).isEqualTo(UPDATED_TELEPHONE);
        assertThat(testEtudiant.getPremierEmploi()).isEqualTo(UPDATED_PREMIER_EMPLOI);
        assertThat(testEtudiant.getDernierEmploi()).isEqualTo(UPDATED_DERNIER_EMPLOI);
        assertThat(testEtudiant.isRechEmp()).isEqualTo(UPDATED_RECH_EMP);
        assertThat(testEtudiant.getDateMaj()).isEqualTo(UPDATED_DATE_MAJ);

        // Validate the Etudiant in ElasticSearch
        Etudiant etudiantEs = etudiantSearchRepository.findOne(testEtudiant.getId());
        assertThat(etudiantEs).isEqualToComparingFieldByField(testEtudiant);
    }

    @Test
    @Transactional
    public void deleteEtudiant() throws Exception {
        // Initialize the database
        etudiantRepository.saveAndFlush(etudiant);
        etudiantSearchRepository.save(etudiant);
        int databaseSizeBeforeDelete = etudiantRepository.findAll().size();

        // Get the etudiant
        restEtudiantMockMvc.perform(delete("/api/etudiants/{id}", etudiant.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean etudiantExistsInEs = etudiantSearchRepository.exists(etudiant.getId());
        assertThat(etudiantExistsInEs).isFalse();

        // Validate the database is empty
        List<Etudiant> etudiants = etudiantRepository.findAll();
        assertThat(etudiants).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchEtudiant() throws Exception {
        // Initialize the database
        etudiantRepository.saveAndFlush(etudiant);
        etudiantSearchRepository.save(etudiant);

        // Search the etudiant
        restEtudiantMockMvc.perform(get("/api/_search/etudiants?query=id:" + etudiant.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(etudiant.getId().intValue())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM.toString())))
            .andExpect(jsonPath("$.[*].prenom").value(hasItem(DEFAULT_PRENOM.toString())))
            .andExpect(jsonPath("$.[*].sexe").value(hasItem(DEFAULT_SEXE.toString())))
            .andExpect(jsonPath("$.[*].rue").value(hasItem(DEFAULT_RUE.toString())))
            .andExpect(jsonPath("$.[*].ville").value(hasItem(DEFAULT_VILLE.toString())))
            .andExpect(jsonPath("$.[*].codeDep").value(hasItem(DEFAULT_CODE_DEP.toString())))
            .andExpect(jsonPath("$.[*].telephone").value(hasItem(DEFAULT_TELEPHONE.toString())))
            .andExpect(jsonPath("$.[*].premierEmploi").value(hasItem(DEFAULT_PREMIER_EMPLOI.toString())))
            .andExpect(jsonPath("$.[*].dernierEmploi").value(hasItem(DEFAULT_DERNIER_EMPLOI.toString())))
            .andExpect(jsonPath("$.[*].rechEmp").value(hasItem(DEFAULT_RECH_EMP.booleanValue())))
            .andExpect(jsonPath("$.[*].dateMaj").value(hasItem(DEFAULT_DATE_MAJ_STR)));
    }
}
