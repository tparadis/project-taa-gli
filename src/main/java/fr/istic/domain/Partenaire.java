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
 * A Partenaire.
 */
@Entity
@Table(name = "partenaire")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "partenaire")
public class Partenaire implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "siret")
    private String siret;

    @Column(name = "service")
    private String service;

    @NotNull
    @Column(name = "region", nullable = false)
    private String region;

    @NotNull
    @Column(name = "code_activity", nullable = false)
    private String codeActivity;

    @Column(name = "rue")
    private String rue;

    @Column(name = "cplt_rue")
    private String cpltRue;

    @NotNull
    @Column(name = "code_dep", nullable = false)
    private String codeDep;

    @NotNull
    @Column(name = "ville", nullable = false)
    private String ville;

    @Column(name = "tel_std")
    private String telStd;

    @Column(name = "url")
    private String url;

    @Column(name = "commentaire")
    private String commentaire;

    @Column(name = "nom_signataire")
    private String nomSignataire;

    @Column(name = "effectif")
    private Long effectif;

    @NotNull
    @Column(name = "date_maj", nullable = false)
    private ZonedDateTime dateMaj;

    @OneToOne
    @JoinColumn(unique = true)
    private User users;

    @OneToMany(mappedBy = "partenaire")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Contact> contacts = new HashSet<>();

    @ManyToOne
    private Etudiant stagiaires;

    @ManyToOne
    private Stage stages;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSiret() {
        return siret;
    }

    public Partenaire siret(String siret) {
        this.siret = siret;
        return this;
    }

    public void setSiret(String siret) {
        this.siret = siret;
    }

    public String getService() {
        return service;
    }

    public Partenaire service(String service) {
        this.service = service;
        return this;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getRegion() {
        return region;
    }

    public Partenaire region(String region) {
        this.region = region;
        return this;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getCodeActivity() {
        return codeActivity;
    }

    public Partenaire codeActivity(String codeActivity) {
        this.codeActivity = codeActivity;
        return this;
    }

    public void setCodeActivity(String codeActivity) {
        this.codeActivity = codeActivity;
    }

    public String getRue() {
        return rue;
    }

    public Partenaire rue(String rue) {
        this.rue = rue;
        return this;
    }

    public void setRue(String rue) {
        this.rue = rue;
    }

    public String getCpltRue() {
        return cpltRue;
    }

    public Partenaire cpltRue(String cpltRue) {
        this.cpltRue = cpltRue;
        return this;
    }

    public void setCpltRue(String cpltRue) {
        this.cpltRue = cpltRue;
    }

    public String getCodeDep() {
        return codeDep;
    }

    public Partenaire codeDep(String codeDep) {
        this.codeDep = codeDep;
        return this;
    }

    public void setCodeDep(String codeDep) {
        this.codeDep = codeDep;
    }

    public String getVille() {
        return ville;
    }

    public Partenaire ville(String ville) {
        this.ville = ville;
        return this;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    public String getTelStd() {
        return telStd;
    }

    public Partenaire telStd(String telStd) {
        this.telStd = telStd;
        return this;
    }

    public void setTelStd(String telStd) {
        this.telStd = telStd;
    }

    public String getUrl() {
        return url;
    }

    public Partenaire url(String url) {
        this.url = url;
        return this;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getCommentaire() {
        return commentaire;
    }

    public Partenaire commentaire(String commentaire) {
        this.commentaire = commentaire;
        return this;
    }

    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }

    public String getNomSignataire() {
        return nomSignataire;
    }

    public Partenaire nomSignataire(String nomSignataire) {
        this.nomSignataire = nomSignataire;
        return this;
    }

    public void setNomSignataire(String nomSignataire) {
        this.nomSignataire = nomSignataire;
    }

    public Long getEffectif() {
        return effectif;
    }

    public Partenaire effectif(Long effectif) {
        this.effectif = effectif;
        return this;
    }

    public void setEffectif(Long effectif) {
        this.effectif = effectif;
    }

    public ZonedDateTime getDateMaj() {
        return dateMaj;
    }

    public Partenaire dateMaj(ZonedDateTime dateMaj) {
        this.dateMaj = dateMaj;
        return this;
    }

    public void setDateMaj(ZonedDateTime dateMaj) {
        this.dateMaj = dateMaj;
    }

    public User getUsers() {
        return users;
    }

    public Partenaire users(User user) {
        this.users = user;
        return this;
    }

    public void setUsers(User user) {
        this.users = user;
    }

    public Set<Contact> getContacts() {
        return contacts;
    }

    public Partenaire contacts(Set<Contact> contacts) {
        this.contacts = contacts;
        return this;
    }

    public Partenaire addContacts(Contact contact) {
        contacts.add(contact);
        contact.setPartenaire(this);
        return this;
    }

    public Partenaire removeContacts(Contact contact) {
        contacts.remove(contact);
        contact.setPartenaire(null);
        return this;
    }

    public void setContacts(Set<Contact> contacts) {
        this.contacts = contacts;
    }

    public Etudiant getStagiaires() {
        return stagiaires;
    }

    public Partenaire stagiaires(Etudiant etudiant) {
        this.stagiaires = etudiant;
        return this;
    }

    public void setStagiaires(Etudiant etudiant) {
        this.stagiaires = etudiant;
    }

    public Stage getStages() {
        return stages;
    }

    public Partenaire stages(Stage stage) {
        this.stages = stage;
        return this;
    }

    public void setStages(Stage stage) {
        this.stages = stage;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Partenaire partenaire = (Partenaire) o;
        if(partenaire.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, partenaire.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Partenaire{" +
            "id=" + id +
            ", siret='" + siret + "'" +
            ", service='" + service + "'" +
            ", region='" + region + "'" +
            ", codeActivity='" + codeActivity + "'" +
            ", rue='" + rue + "'" +
            ", cpltRue='" + cpltRue + "'" +
            ", codeDep='" + codeDep + "'" +
            ", ville='" + ville + "'" +
            ", telStd='" + telStd + "'" +
            ", url='" + url + "'" +
            ", commentaire='" + commentaire + "'" +
            ", nomSignataire='" + nomSignataire + "'" +
            ", effectif='" + effectif + "'" +
            ", dateMaj='" + dateMaj + "'" +
            '}';
    }
}
