package fr.istic.repository.search;

import fr.istic.domain.Partenaire;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Partenaire entity.
 */
public interface PartenaireSearchRepository extends ElasticsearchRepository<Partenaire, Long> {
}
