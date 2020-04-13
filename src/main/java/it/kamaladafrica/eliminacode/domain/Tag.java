package it.kamaladafrica.eliminacode.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.Objects;
import java.time.Instant;
import java.util.UUID;

/**
 * A Tag.
 */
@Entity
@Table(name = "tag")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Tag implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "progressivo")
    private Long progressivo;

    @NotNull
    @Column(name = "staccato", nullable = false)
    private Instant staccato;

    @Column(name = "bruciato")
    private Instant bruciato;

    @NotNull
    @Column(name = "jhi_key", nullable = false)
    private UUID key;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProgressivo() {
        return progressivo;
    }

    public Tag progressivo(Long progressivo) {
        this.progressivo = progressivo;
        return this;
    }

    public void setProgressivo(Long progressivo) {
        this.progressivo = progressivo;
    }

    public Instant getStaccato() {
        return staccato;
    }

    public Tag staccato(Instant staccato) {
        this.staccato = staccato;
        return this;
    }

    public void setStaccato(Instant staccato) {
        this.staccato = staccato;
    }

    public Instant getBruciato() {
        return bruciato;
    }

    public Tag bruciato(Instant bruciato) {
        this.bruciato = bruciato;
        return this;
    }

    public void setBruciato(Instant bruciato) {
        this.bruciato = bruciato;
    }

    public UUID getKey() {
        return key;
    }

    public Tag key(UUID key) {
        this.key = key;
        return this;
    }

    public void setKey(UUID key) {
        this.key = key;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Tag)) {
            return false;
        }
        return id != null && id.equals(((Tag) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Tag{" +
            "id=" + getId() +
            ", progressivo=" + getProgressivo() +
            ", staccato='" + getStaccato() + "'" +
            ", bruciato='" + getBruciato() + "'" +
            ", key='" + getKey() + "'" +
            "}";
    }
}
