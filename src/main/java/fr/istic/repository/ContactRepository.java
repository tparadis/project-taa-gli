package fr.istic.repository;

import fr.istic.domain.Contact;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Contact entity.
 */
@SuppressWarnings("unused")
public interface ContactRepository extends JpaRepository<Contact,Long> {

}
