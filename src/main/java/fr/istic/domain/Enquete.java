package fr.istic.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Enquete.
 */
@Entity
@Table(name = "enquete")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "enquete")
public class Enquete implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "date_debut", nullable = false)
    private ZonedDateTime dateDebut;

    @Column(name = "duree_enquete")
    private String dureeEnquete;

    @NotNull
    @Column(name = "lien", nullable = false)
    private String lien;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "enquete_etudiants",
               joinColumns = @JoinColumn(name="enquetes_id", referencedColumnName="ID"),
               inverseJoinColumns = @JoinColumn(name="etudiants_id", referencedColumnName="ID"))
    private Set<Etudiant> etudiants = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getDateDebut() {
        return dateDebut;
    }

    public Enquete dateDebut(ZonedDateTime dateDebut) {
        this.dateDebut = dateDebut;
        return this;
    }

    public void setDateDebut(ZonedDateTime dateDebut) {
        this.dateDebut = dateDebut;
    }

    public String getDureeEnquete() {
        return dureeEnquete;
    }

    public Enquete dureeEnquete(String dureeEnquete) {
        this.dureeEnquete = dureeEnquete;
        return this;
    }

    public void setDureeEnquete(String dureeEnquete) {
        this.dureeEnquete = dureeEnquete;
    }

    public String getLien() {
        return lien;
    }

    public Enquete lien(String lien) {
        this.lien = lien;
        return this;
    }

    public void setLien(String lien) {
        this.lien = lien;
    }

    public Set<Etudiant> getEtudiants() {
        return etudiants;
    }

    public Enquete etudiants(Set<Etudiant> etudiants) {
        this.etudiants = etudiants;
        return this;
    }

    public Enquete addEtudiants(Etudiant etudiant) {
        etudiants.add(etudiant);
        etudiant.getEnquetes().add(this);
        return this;
    }

    public Enquete removeEtudiants(Etudiant etudiant) {
        etudiants.remove(etudiant);
        etudiant.getEnquetes().remove(this);
        return this;
    }

    public void setEtudiants(Set<Etudiant> etudiants) {
        this.etudiants = etudiants;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Enquete enquete = (Enquete) o;
        if(enquete.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, enquete.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Enquete{" +
            "id=" + id +
            ", dateDebut='" + dateDebut + "'" +
            ", dureeEnquete='" + dureeEnquete + "'" +
            ", lien='" + lien + "'" +
            '}';
    }
}
