package fr.istic.repository.search;

import fr.istic.domain.Enseignant;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Enseignant entity.
 */
public interface EnseignantSearchRepository extends ElasticsearchRepository<Enseignant, Long> {
}
