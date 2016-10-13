package fr.istic.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
 * A Etudiant.
 */
@Entity
@Table(name = "etudiant")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "etudiant")
public class Etudiant implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "nom", nullable = false)
    private String nom;

    @NotNull
    @Column(name = "prenom", nullable = false)
    private String prenom;

    @Column(name = "sexe")
    private String sexe;

    @Column(name = "rue")
    private String rue;

    @Column(name = "ville")
    private String ville;

    @Column(name = "code_dep")
    private String codeDep;

    @Column(name = "telephone")
    private String telephone;

    @Column(name = "premier_emploi")
    private String premierEmploi;

    @Column(name = "dernier_emploi")
    private String dernierEmploi;

    @Column(name = "rech_emp")
    private Boolean rechEmp;

    @NotNull
    @Column(name = "date_maj", nullable = false)
    private ZonedDateTime dateMaj;

    @OneToOne
    @JoinColumn(unique = true)
    private User users;

    @OneToMany(mappedBy = "stagiaires")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Partenaire> partenaires = new HashSet<>();

    @OneToOne(mappedBy = "etudiant")
    @JsonIgnore
    private Stage stage;

    @ManyToMany(mappedBy = "etudiants")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Enquete> enquetes = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public Etudiant nom(String nom) {
        this.nom = nom;
        return this;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public Etudiant prenom(String prenom) {
        this.prenom = prenom;
        return this;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getSexe() {
        return sexe;
    }

    public Etudiant sexe(String sexe) {
        this.sexe = sexe;
        return this;
    }

    public void setSexe(String sexe) {
        this.sexe = sexe;
    }

    public String getRue() {
        return rue;
    }

    public Etudiant rue(String rue) {
        this.rue = rue;
        return this;
    }

    public void setRue(String rue) {
        this.rue = rue;
    }

    public String getVille() {
        return ville;
    }

    public Etudiant ville(String ville) {
        this.ville = ville;
        return this;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    public String getCodeDep() {
        return codeDep;
    }

    public Etudiant codeDep(String codeDep) {
        this.codeDep = codeDep;
        return this;
    }

    public void setCodeDep(String codeDep) {
        this.codeDep = codeDep;
    }

    public String getTelephone() {
        return telephone;
    }

    public Etudiant telephone(String telephone) {
        this.telephone = telephone;
        return this;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getPremierEmploi() {
        return premierEmploi;
    }

    public Etudiant premierEmploi(String premierEmploi) {
        this.premierEmploi = premierEmploi;
        return this;
    }

    public void setPremierEmploi(String premierEmploi) {
        this.premierEmploi = premierEmploi;
    }

    public String getDernierEmploi() {
        return dernierEmploi;
    }

    public Etudiant dernierEmploi(String dernierEmploi) {
        this.dernierEmploi = dernierEmploi;
        return this;
    }

    public void setDernierEmploi(String dernierEmploi) {
        this.dernierEmploi = dernierEmploi;
    }

    public Boolean isRechEmp() {
        return rechEmp;
    }

    public Etudiant rechEmp(Boolean rechEmp) {
        this.rechEmp = rechEmp;
        return this;
    }

    public void setRechEmp(Boolean rechEmp) {
        this.rechEmp = rechEmp;
    }

    public ZonedDateTime getDateMaj() {
        return dateMaj;
    }

    public Etudiant dateMaj(ZonedDateTime dateMaj) {
        this.dateMaj = dateMaj;
        return this;
    }

    public void setDateMaj(ZonedDateTime dateMaj) {
        this.dateMaj = dateMaj;
    }

    public User getUsers() {
        return users;
    }

    public Etudiant users(User user) {
        this.users = user;
        return this;
    }

    public void setUsers(User user) {
        this.users = user;
    }

    public Set<Partenaire> getPartenaires() {
        return partenaires;
    }

    public Etudiant partenaires(Set<Partenaire> partenaires) {
        this.partenaires = partenaires;
        return this;
    }

    public Etudiant addPartenaire(Partenaire partenaire) {
        partenaires.add(partenaire);
        partenaire.setStagiaires(this);
        return this;
    }

    public Etudiant removePartenaire(Partenaire partenaire) {
        partenaires.remove(partenaire);
        partenaire.setStagiaires(null);
        return this;
    }

    public void setPartenaires(Set<Partenaire> partenaires) {
        this.partenaires = partenaires;
    }

    public Stage getStage() {
        return stage;
    }

    public Etudiant stage(Stage stage) {
        this.stage = stage;
        return this;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public Set<Enquete> getEnquetes() {
        return enquetes;
    }

    public Etudiant enquetes(Set<Enquete> enquetes) {
        this.enquetes = enquetes;
        return this;
    }

    public Etudiant addEnquetes(Enquete enquete) {
        enquetes.add(enquete);
        enquete.getEtudiants().add(this);
        return this;
    }

    public Etudiant removeEnquetes(Enquete enquete) {
        enquetes.remove(enquete);
        enquete.getEtudiants().remove(this);
        return this;
    }

    public void setEnquetes(Set<Enquete> enquetes) {
        this.enquetes = enquetes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Etudiant etudiant = (Etudiant) o;
        if(etudiant.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, etudiant.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Etudiant{" +
            "id=" + id +
            ", nom='" + nom + "'" +
            ", prenom='" + prenom + "'" +
            ", sexe='" + sexe + "'" +
            ", rue='" + rue + "'" +
            ", ville='" + ville + "'" +
            ", codeDep='" + codeDep + "'" +
            ", telephone='" + telephone + "'" +
            ", premierEmploi='" + premierEmploi + "'" +
            ", dernierEmploi='" + dernierEmploi + "'" +
            ", rechEmp='" + rechEmp + "'" +
            ", dateMaj='" + dateMaj + "'" +
            '}';
    }
}
