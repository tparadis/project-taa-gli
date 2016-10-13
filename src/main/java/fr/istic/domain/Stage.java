package fr.istic.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A Stage.
 */
@Entity
@Table(name = "stage")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "stage")
public class Stage implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "date_debut", nullable = false)
    private ZonedDateTime dateDebut;

    @Column(name = "sujet")
    private String sujet;

    @Column(name = "lang")
    private String lang;

    @Column(name = "motscles")
    private String motscles;

    @Column(name = "jours_travaille")
    private Long joursTravaille;

    @Column(name = "salaire")
    private Long salaire;

    @Column(name = "fin_conv")
    private ZonedDateTime finConv;

    @Column(name = "fin_stage")
    private ZonedDateTime finStage;

    @Column(name = "soutenance")
    private ZonedDateTime soutenance;

    @Column(name = "rapport")
    private ZonedDateTime rapport;

    @OneToOne
    @JoinColumn(unique = true)
    private Enseignant referent;

    @OneToOne
    @JoinColumn(unique = true)
    private Contact contact;

    @OneToOne
    @JoinColumn(unique = true)
    private Etudiant etudiant;

    @ManyToOne
    private Partenaire entreprise;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getDateDebut() {
        return dateDebut;
    }

    public Stage dateDebut(ZonedDateTime dateDebut) {
        this.dateDebut = dateDebut;
        return this;
    }

    public void setDateDebut(ZonedDateTime dateDebut) {
        this.dateDebut = dateDebut;
    }

    public String getSujet() {
        return sujet;
    }

    public Stage sujet(String sujet) {
        this.sujet = sujet;
        return this;
    }

    public void setSujet(String sujet) {
        this.sujet = sujet;
    }

    public String getLang() {
        return lang;
    }

    public Stage lang(String lang) {
        this.lang = lang;
        return this;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getMotscles() {
        return motscles;
    }

    public Stage motscles(String motscles) {
        this.motscles = motscles;
        return this;
    }

    public void setMotscles(String motscles) {
        this.motscles = motscles;
    }

    public Long getJoursTravaille() {
        return joursTravaille;
    }

    public Stage joursTravaille(Long joursTravaille) {
        this.joursTravaille = joursTravaille;
        return this;
    }

    public void setJoursTravaille(Long joursTravaille) {
        this.joursTravaille = joursTravaille;
    }

    public Long getSalaire() {
        return salaire;
    }

    public Stage salaire(Long salaire) {
        this.salaire = salaire;
        return this;
    }

    public void setSalaire(Long salaire) {
        this.salaire = salaire;
    }

    public ZonedDateTime getFinConv() {
        return finConv;
    }

    public Stage finConv(ZonedDateTime finConv) {
        this.finConv = finConv;
        return this;
    }

    public void setFinConv(ZonedDateTime finConv) {
        this.finConv = finConv;
    }

    public ZonedDateTime getFinStage() {
        return finStage;
    }

    public Stage finStage(ZonedDateTime finStage) {
        this.finStage = finStage;
        return this;
    }

    public void setFinStage(ZonedDateTime finStage) {
        this.finStage = finStage;
    }

    public ZonedDateTime getSoutenance() {
        return soutenance;
    }

    public Stage soutenance(ZonedDateTime soutenance) {
        this.soutenance = soutenance;
        return this;
    }

    public void setSoutenance(ZonedDateTime soutenance) {
        this.soutenance = soutenance;
    }

    public ZonedDateTime getRapport() {
        return rapport;
    }

    public Stage rapport(ZonedDateTime rapport) {
        this.rapport = rapport;
        return this;
    }

    public void setRapport(ZonedDateTime rapport) {
        this.rapport = rapport;
    }

    public Enseignant getReferent() {
        return referent;
    }

    public Stage referent(Enseignant enseignant) {
        this.referent = enseignant;
        return this;
    }

    public void setReferent(Enseignant enseignant) {
        this.referent = enseignant;
    }

    public Contact getContact() {
        return contact;
    }

    public Stage contact(Contact contact) {
        this.contact = contact;
        return this;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }

    public Etudiant getEtudiant() {
        return etudiant;
    }

    public Stage etudiant(Etudiant etudiant) {
        this.etudiant = etudiant;
        return this;
    }

    public void setEtudiant(Etudiant etudiant) {
        this.etudiant = etudiant;
    }

    public Partenaire getEntreprise() {
        return entreprise;
    }

    public Stage entreprise(Partenaire partenaire) {
        this.entreprise = partenaire;
        return this;
    }

    public void setEntreprise(Partenaire partenaire) {
        this.entreprise = partenaire;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Stage stage = (Stage) o;
        if(stage.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, stage.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Stage{" +
            "id=" + id +
            ", dateDebut='" + dateDebut + "'" +
            ", sujet='" + sujet + "'" +
            ", lang='" + lang + "'" +
            ", motscles='" + motscles + "'" +
            ", joursTravaille='" + joursTravaille + "'" +
            ", salaire='" + salaire + "'" +
            ", finConv='" + finConv + "'" +
            ", finStage='" + finStage + "'" +
            ", soutenance='" + soutenance + "'" +
            ", rapport='" + rapport + "'" +
            '}';
    }
}
