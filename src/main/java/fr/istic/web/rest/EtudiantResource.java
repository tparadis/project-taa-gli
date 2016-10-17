package fr.istic.web.rest;

import com.codahale.metrics.annotation.Timed;
import fr.istic.domain.Etudiant;

import fr.istic.repository.EtudiantRepository;
import fr.istic.repository.search.EtudiantSearchRepository;
import fr.istic.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Etudiant.
 */
@RestController
@RequestMapping("/api")
public class EtudiantResource {

    private final Logger log = LoggerFactory.getLogger(EtudiantResource.class);

    @Inject
    private EtudiantRepository etudiantRepository;

    @Inject
    private EtudiantSearchRepository etudiantSearchRepository;

    /**
     * POST  /etudiants : Create a new etudiant.
     *
     * @param etudiant the etudiant to create
     * @return the ResponseEntity with status 201 (Created) and with body the new etudiant, or with status 400 (Bad Request) if the etudiant has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/etudiants",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Etudiant> createEtudiant(@Valid @RequestBody Etudiant etudiant) throws URISyntaxException {
        log.debug("REST request to save Etudiant : {}", etudiant);
        if (etudiant.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("etudiant", "idexists", "A new etudiant cannot already have an ID")).body(null);
        }
        Etudiant result = etudiantRepository.save(etudiant);
        etudiantSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/etudiants/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("etudiant", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /etudiants : Updates an existing etudiant.
     *
     * @param etudiant the etudiant to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated etudiant,
     * or with status 400 (Bad Request) if the etudiant is not valid,
     * or with status 500 (Internal Server Error) if the etudiant couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/etudiants",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Etudiant> updateEtudiant(@Valid @RequestBody Etudiant etudiant) throws URISyntaxException {
        log.debug("REST request to update Etudiant : {}", etudiant);
        if (etudiant.getId() == null) {
            return createEtudiant(etudiant);
        }
        Etudiant result = etudiantRepository.save(etudiant);
        etudiantSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("etudiant", etudiant.getId().toString()))
            .body(result);
    }

    /**
     * GET  /etudiants : get all the etudiants.
     *
     * @param filter the filter of the request
     * @return the ResponseEntity with status 200 (OK) and the list of etudiants in body
     */
    @RequestMapping(value = "/etudiants",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Etudiant> getAllEtudiants(@RequestParam(required = false) String filter) {
        if ("stage-is-null".equals(filter)) {
            log.debug("REST request to get all Etudiants where stage is null");
            return StreamSupport
                .stream(etudiantRepository.findAll().spliterator(), false)
                .filter(etudiant -> etudiant.getStage() == null)
                .collect(Collectors.toList());
        }
        log.debug("REST request to get all Etudiants");

        //changement de la requete
        //on recupere seulement les etudiants lié au user connecté
        List<Etudiant> etudiants = etudiantRepository.findByUserIsCurrentUser();
        return etudiants;
    }

    /**
     * GET  /etudiants/:id : get the "id" etudiant.
     *
     * @param id the id of the etudiant to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the etudiant, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/etudiants/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Etudiant> getEtudiant(@PathVariable Long id) {
        log.debug("REST request to get Etudiant : {}", id);
        Etudiant etudiant = etudiantRepository.findOne(id);
        return Optional.ofNullable(etudiant)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /etudiants/:id : delete the "id" etudiant.
     *
     * @param id the id of the etudiant to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/etudiants/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteEtudiant(@PathVariable Long id) {
        log.debug("REST request to delete Etudiant : {}", id);
        etudiantRepository.delete(id);
        etudiantSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("etudiant", id.toString())).build();
    }

    /**
     * SEARCH  /_search/etudiants?query=:query : search for the etudiant corresponding
     * to the query.
     *
     * @param query the query of the etudiant search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/etudiants",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Etudiant> searchEtudiants(@RequestParam String query) {
        log.debug("REST request to search Etudiants for query {}", query);
        return StreamSupport
            .stream(etudiantSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }


}
