package fr.istic.repository;

import fr.istic.domain.Etudiant;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Etudiant entity.
 */
@SuppressWarnings("unused")
public interface EtudiantRepository extends JpaRepository<Etudiant,Long> {
    //on cherche l'etudiant lié au User connecté
    @Query("select etudiant from Etudiant etudiant where etudiant.users.login = ?#{principal.username}")
    List<Etudiant> findByUserIsCurrentUser();
}
