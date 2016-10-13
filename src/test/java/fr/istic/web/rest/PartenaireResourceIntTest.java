package fr.istic.web.rest;

import fr.istic.ProjectTaaGliApp;

import fr.istic.domain.Partenaire;
import fr.istic.repository.PartenaireRepository;
import fr.istic.repository.search.PartenaireSearchRepository;

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
 * Test class for the PartenaireResource REST controller.
 *
 * @see PartenaireResource
 */
@RunWith(SpringRunner.class)

@SpringBootTest(classes = ProjectTaaGliApp.class)

public class PartenaireResourceIntTest {
    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").withZone(ZoneId.of("Z"));
    private static final String DEFAULT_SIRET = "AAAAA";
    private static final String UPDATED_SIRET = "BBBBB";
    private static final String DEFAULT_SERVICE = "AAAAA";
    private static final String UPDATED_SERVICE = "BBBBB";
    private static final String DEFAULT_REGION = "AAAAA";
    private static final String UPDATED_REGION = "BBBBB";
    private static final String DEFAULT_CODE_ACTIVITY = "AAAAA";
    private static final String UPDATED_CODE_ACTIVITY = "BBBBB";
    private static final String DEFAULT_RUE = "AAAAA";
    private static final String UPDATED_RUE = "BBBBB";
    private static final String DEFAULT_CPLT_RUE = "AAAAA";
    private static final String UPDATED_CPLT_RUE = "BBBBB";
    private static final String DEFAULT_CODE_DEP = "AAAAA";
    private static final String UPDATED_CODE_DEP = "BBBBB";
    private static final String DEFAULT_VILLE = "AAAAA";
    private static final String UPDATED_VILLE = "BBBBB";
    private static final String DEFAULT_TEL_STD = "AAAAA";
    private static final String UPDATED_TEL_STD = "BBBBB";
    private static final String DEFAULT_URL = "AAAAA";
    private static final String UPDATED_URL = "BBBBB";
    private static final String DEFAULT_COMMENTAIRE = "AAAAA";
    private static final String UPDATED_COMMENTAIRE = "BBBBB";
    private static final String DEFAULT_NOM_SIGNATAIRE = "AAAAA";
    private static final String UPDATED_NOM_SIGNATAIRE = "BBBBB";

    private static final Long DEFAULT_EFFECTIF = 1L;
    private static final Long UPDATED_EFFECTIF = 2L;

    private static final ZonedDateTime DEFAULT_DATE_MAJ = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_DATE_MAJ = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_DATE_MAJ_STR = dateTimeFormatter.format(DEFAULT_DATE_MAJ);

    @Inject
    private PartenaireRepository partenaireRepository;

    @Inject
    private PartenaireSearchRepository partenaireSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restPartenaireMockMvc;

    private Partenaire partenaire;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        PartenaireResource partenaireResource = new PartenaireResource();
        ReflectionTestUtils.setField(partenaireResource, "partenaireSearchRepository", partenaireSearchRepository);
        ReflectionTestUtils.setField(partenaireResource, "partenaireRepository", partenaireRepository);
        this.restPartenaireMockMvc = MockMvcBuilders.standaloneSetup(partenaireResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Partenaire createEntity(EntityManager em) {
        Partenaire partenaire = new Partenaire()
                .siret(DEFAULT_SIRET)
                .service(DEFAULT_SERVICE)
                .region(DEFAULT_REGION)
                .codeActivity(DEFAULT_CODE_ACTIVITY)
                .rue(DEFAULT_RUE)
                .cpltRue(DEFAULT_CPLT_RUE)
                .codeDep(DEFAULT_CODE_DEP)
                .ville(DEFAULT_VILLE)
                .telStd(DEFAULT_TEL_STD)
                .url(DEFAULT_URL)
                .commentaire(DEFAULT_COMMENTAIRE)
                .nomSignataire(DEFAULT_NOM_SIGNATAIRE)
                .effectif(DEFAULT_EFFECTIF)
                .dateMaj(DEFAULT_DATE_MAJ);
        return partenaire;
    }

    @Before
    public void initTest() {
        partenaireSearchRepository.deleteAll();
        partenaire = createEntity(em);
    }

    @Test
    @Transactional
    public void createPartenaire() throws Exception {
        int databaseSizeBeforeCreate = partenaireRepository.findAll().size();

        // Create the Partenaire

        restPartenaireMockMvc.perform(post("/api/partenaires")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(partenaire)))
                .andExpect(status().isCreated());

        // Validate the Partenaire in the database
        List<Partenaire> partenaires = partenaireRepository.findAll();
        assertThat(partenaires).hasSize(databaseSizeBeforeCreate + 1);
        Partenaire testPartenaire = partenaires.get(partenaires.size() - 1);
        assertThat(testPartenaire.getSiret()).isEqualTo(DEFAULT_SIRET);
        assertThat(testPartenaire.getService()).isEqualTo(DEFAULT_SERVICE);
        assertThat(testPartenaire.getRegion()).isEqualTo(DEFAULT_REGION);
        assertThat(testPartenaire.getCodeActivity()).isEqualTo(DEFAULT_CODE_ACTIVITY);
        assertThat(testPartenaire.getRue()).isEqualTo(DEFAULT_RUE);
        assertThat(testPartenaire.getCpltRue()).isEqualTo(DEFAULT_CPLT_RUE);
        assertThat(testPartenaire.getCodeDep()).isEqualTo(DEFAULT_CODE_DEP);
        assertThat(testPartenaire.getVille()).isEqualTo(DEFAULT_VILLE);
        assertThat(testPartenaire.getTelStd()).isEqualTo(DEFAULT_TEL_STD);
        assertThat(testPartenaire.getUrl()).isEqualTo(DEFAULT_URL);
        assertThat(testPartenaire.getCommentaire()).isEqualTo(DEFAULT_COMMENTAIRE);
        assertThat(testPartenaire.getNomSignataire()).isEqualTo(DEFAULT_NOM_SIGNATAIRE);
        assertThat(testPartenaire.getEffectif()).isEqualTo(DEFAULT_EFFECTIF);
        assertThat(testPartenaire.getDateMaj()).isEqualTo(DEFAULT_DATE_MAJ);

        // Validate the Partenaire in ElasticSearch
        Partenaire partenaireEs = partenaireSearchRepository.findOne(testPartenaire.getId());
        assertThat(partenaireEs).isEqualToComparingFieldByField(testPartenaire);
    }

    @Test
    @Transactional
    public void checkRegionIsRequired() throws Exception {
        int databaseSizeBeforeTest = partenaireRepository.findAll().size();
        // set the field null
        partenaire.setRegion(null);

        // Create the Partenaire, which fails.

        restPartenaireMockMvc.perform(post("/api/partenaires")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(partenaire)))
                .andExpect(status().isBadRequest());

        List<Partenaire> partenaires = partenaireRepository.findAll();
        assertThat(partenaires).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCodeActivityIsRequired() throws Exception {
        int databaseSizeBeforeTest = partenaireRepository.findAll().size();
        // set the field null
        partenaire.setCodeActivity(null);

        // Create the Partenaire, which fails.

        restPartenaireMockMvc.perform(post("/api/partenaires")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(partenaire)))
                .andExpect(status().isBadRequest());

        List<Partenaire> partenaires = partenaireRepository.findAll();
        assertThat(partenaires).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCodeDepIsRequired() throws Exception {
        int databaseSizeBeforeTest = partenaireRepository.findAll().size();
        // set the field null
        partenaire.setCodeDep(null);

        // Create the Partenaire, which fails.

        restPartenaireMockMvc.perform(post("/api/partenaires")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(partenaire)))
                .andExpect(status().isBadRequest());

        List<Partenaire> partenaires = partenaireRepository.findAll();
        assertThat(partenaires).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkVilleIsRequired() throws Exception {
        int databaseSizeBeforeTest = partenaireRepository.findAll().size();
        // set the field null
        partenaire.setVille(null);

        // Create the Partenaire, which fails.

        restPartenaireMockMvc.perform(post("/api/partenaires")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(partenaire)))
                .andExpect(status().isBadRequest());

        List<Partenaire> partenaires = partenaireRepository.findAll();
        assertThat(partenaires).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDateMajIsRequired() throws Exception {
        int databaseSizeBeforeTest = partenaireRepository.findAll().size();
        // set the field null
        partenaire.setDateMaj(null);

        // Create the Partenaire, which fails.

        restPartenaireMockMvc.perform(post("/api/partenaires")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(partenaire)))
                .andExpect(status().isBadRequest());

        List<Partenaire> partenaires = partenaireRepository.findAll();
        assertThat(partenaires).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllPartenaires() throws Exception {
        // Initialize the database
        partenaireRepository.saveAndFlush(partenaire);

        // Get all the partenaires
        restPartenaireMockMvc.perform(get("/api/partenaires?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(partenaire.getId().intValue())))
                .andExpect(jsonPath("$.[*].siret").value(hasItem(DEFAULT_SIRET.toString())))
                .andExpect(jsonPath("$.[*].service").value(hasItem(DEFAULT_SERVICE.toString())))
                .andExpect(jsonPath("$.[*].region").value(hasItem(DEFAULT_REGION.toString())))
                .andExpect(jsonPath("$.[*].codeActivity").value(hasItem(DEFAULT_CODE_ACTIVITY.toString())))
                .andExpect(jsonPath("$.[*].rue").value(hasItem(DEFAULT_RUE.toString())))
                .andExpect(jsonPath("$.[*].cpltRue").value(hasItem(DEFAULT_CPLT_RUE.toString())))
                .andExpect(jsonPath("$.[*].codeDep").value(hasItem(DEFAULT_CODE_DEP.toString())))
                .andExpect(jsonPath("$.[*].ville").value(hasItem(DEFAULT_VILLE.toString())))
                .andExpect(jsonPath("$.[*].telStd").value(hasItem(DEFAULT_TEL_STD.toString())))
                .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL.toString())))
                .andExpect(jsonPath("$.[*].commentaire").value(hasItem(DEFAULT_COMMENTAIRE.toString())))
                .andExpect(jsonPath("$.[*].nomSignataire").value(hasItem(DEFAULT_NOM_SIGNATAIRE.toString())))
                .andExpect(jsonPath("$.[*].effectif").value(hasItem(DEFAULT_EFFECTIF.intValue())))
                .andExpect(jsonPath("$.[*].dateMaj").value(hasItem(DEFAULT_DATE_MAJ_STR)));
    }

    @Test
    @Transactional
    public void getPartenaire() throws Exception {
        // Initialize the database
        partenaireRepository.saveAndFlush(partenaire);

        // Get the partenaire
        restPartenaireMockMvc.perform(get("/api/partenaires/{id}", partenaire.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(partenaire.getId().intValue()))
            .andExpect(jsonPath("$.siret").value(DEFAULT_SIRET.toString()))
            .andExpect(jsonPath("$.service").value(DEFAULT_SERVICE.toString()))
            .andExpect(jsonPath("$.region").value(DEFAULT_REGION.toString()))
            .andExpect(jsonPath("$.codeActivity").value(DEFAULT_CODE_ACTIVITY.toString()))
            .andExpect(jsonPath("$.rue").value(DEFAULT_RUE.toString()))
            .andExpect(jsonPath("$.cpltRue").value(DEFAULT_CPLT_RUE.toString()))
            .andExpect(jsonPath("$.codeDep").value(DEFAULT_CODE_DEP.toString()))
            .andExpect(jsonPath("$.ville").value(DEFAULT_VILLE.toString()))
            .andExpect(jsonPath("$.telStd").value(DEFAULT_TEL_STD.toString()))
            .andExpect(jsonPath("$.url").value(DEFAULT_URL.toString()))
            .andExpect(jsonPath("$.commentaire").value(DEFAULT_COMMENTAIRE.toString()))
            .andExpect(jsonPath("$.nomSignataire").value(DEFAULT_NOM_SIGNATAIRE.toString()))
            .andExpect(jsonPath("$.effectif").value(DEFAULT_EFFECTIF.intValue()))
            .andExpect(jsonPath("$.dateMaj").value(DEFAULT_DATE_MAJ_STR));
    }

    @Test
    @Transactional
    public void getNonExistingPartenaire() throws Exception {
        // Get the partenaire
        restPartenaireMockMvc.perform(get("/api/partenaires/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePartenaire() throws Exception {
        // Initialize the database
        partenaireRepository.saveAndFlush(partenaire);
        partenaireSearchRepository.save(partenaire);
        int databaseSizeBeforeUpdate = partenaireRepository.findAll().size();

        // Update the partenaire
        Partenaire updatedPartenaire = partenaireRepository.findOne(partenaire.getId());
        updatedPartenaire
                .siret(UPDATED_SIRET)
                .service(UPDATED_SERVICE)
                .region(UPDATED_REGION)
                .codeActivity(UPDATED_CODE_ACTIVITY)
                .rue(UPDATED_RUE)
                .cpltRue(UPDATED_CPLT_RUE)
                .codeDep(UPDATED_CODE_DEP)
                .ville(UPDATED_VILLE)
                .telStd(UPDATED_TEL_STD)
                .url(UPDATED_URL)
                .commentaire(UPDATED_COMMENTAIRE)
                .nomSignataire(UPDATED_NOM_SIGNATAIRE)
                .effectif(UPDATED_EFFECTIF)
                .dateMaj(UPDATED_DATE_MAJ);

        restPartenaireMockMvc.perform(put("/api/partenaires")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedPartenaire)))
                .andExpect(status().isOk());

        // Validate the Partenaire in the database
        List<Partenaire> partenaires = partenaireRepository.findAll();
        assertThat(partenaires).hasSize(databaseSizeBeforeUpdate);
        Partenaire testPartenaire = partenaires.get(partenaires.size() - 1);
        assertThat(testPartenaire.getSiret()).isEqualTo(UPDATED_SIRET);
        assertThat(testPartenaire.getService()).isEqualTo(UPDATED_SERVICE);
        assertThat(testPartenaire.getRegion()).isEqualTo(UPDATED_REGION);
        assertThat(testPartenaire.getCodeActivity()).isEqualTo(UPDATED_CODE_ACTIVITY);
        assertThat(testPartenaire.getRue()).isEqualTo(UPDATED_RUE);
        assertThat(testPartenaire.getCpltRue()).isEqualTo(UPDATED_CPLT_RUE);
        assertThat(testPartenaire.getCodeDep()).isEqualTo(UPDATED_CODE_DEP);
        assertThat(testPartenaire.getVille()).isEqualTo(UPDATED_VILLE);
        assertThat(testPartenaire.getTelStd()).isEqualTo(UPDATED_TEL_STD);
        assertThat(testPartenaire.getUrl()).isEqualTo(UPDATED_URL);
        assertThat(testPartenaire.getCommentaire()).isEqualTo(UPDATED_COMMENTAIRE);
        assertThat(testPartenaire.getNomSignataire()).isEqualTo(UPDATED_NOM_SIGNATAIRE);
        assertThat(testPartenaire.getEffectif()).isEqualTo(UPDATED_EFFECTIF);
        assertThat(testPartenaire.getDateMaj()).isEqualTo(UPDATED_DATE_MAJ);

        // Validate the Partenaire in ElasticSearch
        Partenaire partenaireEs = partenaireSearchRepository.findOne(testPartenaire.getId());
        assertThat(partenaireEs).isEqualToComparingFieldByField(testPartenaire);
    }

    @Test
    @Transactional
    public void deletePartenaire() throws Exception {
        // Initialize the database
        partenaireRepository.saveAndFlush(partenaire);
        partenaireSearchRepository.save(partenaire);
        int databaseSizeBeforeDelete = partenaireRepository.findAll().size();

        // Get the partenaire
        restPartenaireMockMvc.perform(delete("/api/partenaires/{id}", partenaire.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean partenaireExistsInEs = partenaireSearchRepository.exists(partenaire.getId());
        assertThat(partenaireExistsInEs).isFalse();

        // Validate the database is empty
        List<Partenaire> partenaires = partenaireRepository.findAll();
        assertThat(partenaires).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchPartenaire() throws Exception {
        // Initialize the database
        partenaireRepository.saveAndFlush(partenaire);
        partenaireSearchRepository.save(partenaire);

        // Search the partenaire
        restPartenaireMockMvc.perform(get("/api/_search/partenaires?query=id:" + partenaire.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(partenaire.getId().intValue())))
            .andExpect(jsonPath("$.[*].siret").value(hasItem(DEFAULT_SIRET.toString())))
            .andExpect(jsonPath("$.[*].service").value(hasItem(DEFAULT_SERVICE.toString())))
            .andExpect(jsonPath("$.[*].region").value(hasItem(DEFAULT_REGION.toString())))
            .andExpect(jsonPath("$.[*].codeActivity").value(hasItem(DEFAULT_CODE_ACTIVITY.toString())))
            .andExpect(jsonPath("$.[*].rue").value(hasItem(DEFAULT_RUE.toString())))
            .andExpect(jsonPath("$.[*].cpltRue").value(hasItem(DEFAULT_CPLT_RUE.toString())))
            .andExpect(jsonPath("$.[*].codeDep").value(hasItem(DEFAULT_CODE_DEP.toString())))
            .andExpect(jsonPath("$.[*].ville").value(hasItem(DEFAULT_VILLE.toString())))
            .andExpect(jsonPath("$.[*].telStd").value(hasItem(DEFAULT_TEL_STD.toString())))
            .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL.toString())))
            .andExpect(jsonPath("$.[*].commentaire").value(hasItem(DEFAULT_COMMENTAIRE.toString())))
            .andExpect(jsonPath("$.[*].nomSignataire").value(hasItem(DEFAULT_NOM_SIGNATAIRE.toString())))
            .andExpect(jsonPath("$.[*].effectif").value(hasItem(DEFAULT_EFFECTIF.intValue())))
            .andExpect(jsonPath("$.[*].dateMaj").value(hasItem(DEFAULT_DATE_MAJ_STR)));
    }
}
