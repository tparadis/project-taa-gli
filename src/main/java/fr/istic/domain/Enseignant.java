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
 * A Enseignant.
 */
@Entity
@Table(name = "enseignant")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "enseignant")
public class Enseignant implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "sesame", nullable = false)
    private String sesame;

    @Column(name = "sexe")
    private String sexe;

    @NotNull
    @Column(name = "nom", nullable = false)
    private String nom;

    @NotNull
    @Column(name = "prenom", nullable = false)
    private String prenom;

    @Column(name = "adresse")
    private String adresse;

    @Column(name = "tel_pro")
    private String telPro;

    @NotNull
    @Column(name = "actif", nullable = false)
    private Boolean actif;

    @NotNull
    @Column(name = "date_maj", nullable = false)
    private ZonedDateTime dateMaj;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSesame() {
        return sesame;
    }

    public Enseignant sesame(String sesame) {
        this.sesame = sesame;
        return this;
    }

    public void setSesame(String sesame) {
        this.sesame = sesame;
    }

    public String getSexe() {
        return sexe;
    }

    public Enseignant sexe(String sexe) {
        this.sexe = sexe;
        return this;
    }

    public void setSexe(String sexe) {
        this.sexe = sexe;
    }

    public String getNom() {
        return nom;
    }

    public Enseignant nom(String nom) {
        this.nom = nom;
        return this;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public Enseignant prenom(String prenom) {
        this.prenom = prenom;
        return this;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getAdresse() {
        return adresse;
    }

    public Enseignant adresse(String adresse) {
        this.adresse = adresse;
        return this;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getTelPro() {
        return telPro;
    }

    public Enseignant telPro(String telPro) {
        this.telPro = telPro;
        return this;
    }

    public void setTelPro(String telPro) {
        this.telPro = telPro;
    }

    public Boolean isActif() {
        return actif;
    }

    public Enseignant actif(Boolean actif) {
        this.actif = actif;
        return this;
    }

    public void setActif(Boolean actif) {
        this.actif = actif;
    }

    public ZonedDateTime getDateMaj() {
        return dateMaj;
    }

    public Enseignant dateMaj(ZonedDateTime dateMaj) {
        this.dateMaj = dateMaj;
        return this;
    }

    public void setDateMaj(ZonedDateTime dateMaj) {
        this.dateMaj = dateMaj;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Enseignant enseignant = (Enseignant) o;
        if(enseignant.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, enseignant.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Enseignant{" +
            "id=" + id +
            ", sesame='" + sesame + "'" +
            ", sexe='" + sexe + "'" +
            ", nom='" + nom + "'" +
            ", prenom='" + prenom + "'" +
            ", adresse='" + adresse + "'" +
            ", telPro='" + telPro + "'" +
            ", actif='" + actif + "'" +
            ", dateMaj='" + dateMaj + "'" +
            '}';
    }
}
