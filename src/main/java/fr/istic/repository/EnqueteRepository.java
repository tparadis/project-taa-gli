package fr.istic.repository;

import fr.istic.domain.Enquete;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the Enquete entity.
 */
@SuppressWarnings("unused")
public interface EnqueteRepository extends JpaRepository<Enquete,Long> {

    @Query("select distinct enquete from Enquete enquete left join fetch enquete.etudiants")
    List<Enquete> findAllWithEagerRelationships();

    @Query("select enquete from Enquete enquete left join fetch enquete.etudiants where enquete.id =:id")
    Enquete findOneWithEagerRelationships(@Param("id") Long id);

}
