package it.kamaladafrica.eliminacode.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * A Seq.
 */
@Entity
@Table(name = "seq")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Seq implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @NotNull
    @Column(name = "step", nullable = false)
    private Long step;

    @NotNull
    @Column(name = "next_value", nullable = false)
    private Long nextValue;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Seq name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getStep() {
        return step;
    }

    public Seq step(Long step) {
        this.step = step;
        return this;
    }

    public void setStep(Long step) {
        this.step = step;
    }

    public Long getNextValue() {
        return nextValue;
    }

    public Seq nextValue(Long nextValue) {
        this.nextValue = nextValue;
        return this;
    }

    public void setNextValue(Long nextValue) {
        this.nextValue = nextValue;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Seq)) {
            return false;
        }
        return id != null && id.equals(((Seq) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Seq{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", step=" + getStep() +
            ", nextValue=" + getNextValue() +
            "}";
    }
}
