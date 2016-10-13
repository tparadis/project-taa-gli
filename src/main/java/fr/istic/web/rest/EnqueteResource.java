package fr.istic.web.rest;

import com.codahale.metrics.annotation.Timed;
import fr.istic.domain.Enquete;

import fr.istic.repository.EnqueteRepository;
import fr.istic.repository.search.EnqueteSearchRepository;
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
 * REST controller for managing Enquete.
 */
@RestController
@RequestMapping("/api")
public class EnqueteResource {

    private final Logger log = LoggerFactory.getLogger(EnqueteResource.class);
        
    @Inject
    private EnqueteRepository enqueteRepository;

    @Inject
    private EnqueteSearchRepository enqueteSearchRepository;

    /**
     * POST  /enquetes : Create a new enquete.
     *
     * @param enquete the enquete to create
     * @return the ResponseEntity with status 201 (Created) and with body the new enquete, or with status 400 (Bad Request) if the enquete has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/enquetes",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Enquete> createEnquete(@Valid @RequestBody Enquete enquete) throws URISyntaxException {
        log.debug("REST request to save Enquete : {}", enquete);
        if (enquete.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("enquete", "idexists", "A new enquete cannot already have an ID")).body(null);
        }
        Enquete result = enqueteRepository.save(enquete);
        enqueteSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/enquetes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("enquete", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /enquetes : Updates an existing enquete.
     *
     * @param enquete the enquete to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated enquete,
     * or with status 400 (Bad Request) if the enquete is not valid,
     * or with status 500 (Internal Server Error) if the enquete couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/enquetes",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Enquete> updateEnquete(@Valid @RequestBody Enquete enquete) throws URISyntaxException {
        log.debug("REST request to update Enquete : {}", enquete);
        if (enquete.getId() == null) {
            return createEnquete(enquete);
        }
        Enquete result = enqueteRepository.save(enquete);
        enqueteSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("enquete", enquete.getId().toString()))
            .body(result);
    }

    /**
     * GET  /enquetes : get all the enquetes.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of enquetes in body
     */
    @RequestMapping(value = "/enquetes",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Enquete> getAllEnquetes() {
        log.debug("REST request to get all Enquetes");
        List<Enquete> enquetes = enqueteRepository.findAllWithEagerRelationships();
        return enquetes;
    }

    /**
     * GET  /enquetes/:id : get the "id" enquete.
     *
     * @param id the id of the enquete to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the enquete, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/enquetes/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Enquete> getEnquete(@PathVariable Long id) {
        log.debug("REST request to get Enquete : {}", id);
        Enquete enquete = enqueteRepository.findOneWithEagerRelationships(id);
        return Optional.ofNullable(enquete)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /enquetes/:id : delete the "id" enquete.
     *
     * @param id the id of the enquete to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/enquetes/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteEnquete(@PathVariable Long id) {
        log.debug("REST request to delete Enquete : {}", id);
        enqueteRepository.delete(id);
        enqueteSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("enquete", id.toString())).build();
    }

    /**
     * SEARCH  /_search/enquetes?query=:query : search for the enquete corresponding
     * to the query.
     *
     * @param query the query of the enquete search 
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/enquetes",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Enquete> searchEnquetes(@RequestParam String query) {
        log.debug("REST request to search Enquetes for query {}", query);
        return StreamSupport
            .stream(enqueteSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }


}
