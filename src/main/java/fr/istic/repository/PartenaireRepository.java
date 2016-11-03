package fr.istic.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import fr.istic.domain.Partenaire;

/**
 * Spring Data JPA repository for the Partenaire entity.
 */
@SuppressWarnings("unused")
public interface PartenaireRepository extends JpaRepository<Partenaire,Long> {
  @Query("select partenaire from Partenaire partenaire where partenaire.users.login = :id")
  Partenaire findPartenaireByCurrentUser(@Param("id") String id);
}
