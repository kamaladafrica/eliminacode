package it.kamaladafrica.eliminacode.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.Criteria;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;
import io.github.jhipster.service.filter.InstantFilter;
import io.github.jhipster.service.filter.UUIDFilter;

/**
 * Criteria class for the {@link it.kamaladafrica.eliminacode.domain.Tag} entity. This class is used
 * in {@link it.kamaladafrica.eliminacode.web.rest.TagResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /tags?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class TagCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LongFilter progressivo;

    private InstantFilter staccato;

    private InstantFilter bruciato;

    private UUIDFilter key;

    public TagCriteria() {
    }

    public TagCriteria(TagCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.progressivo = other.progressivo == null ? null : other.progressivo.copy();
        this.staccato = other.staccato == null ? null : other.staccato.copy();
        this.bruciato = other.bruciato == null ? null : other.bruciato.copy();
        this.key = other.key == null ? null : other.key.copy();
    }

    @Override
    public TagCriteria copy() {
        return new TagCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public LongFilter getProgressivo() {
        return progressivo;
    }

    public void setProgressivo(LongFilter progressivo) {
        this.progressivo = progressivo;
    }

    public InstantFilter getStaccato() {
        return staccato;
    }

    public void setStaccato(InstantFilter staccato) {
        this.staccato = staccato;
    }

    public InstantFilter getBruciato() {
        return bruciato;
    }

    public void setBruciato(InstantFilter bruciato) {
        this.bruciato = bruciato;
    }

    public UUIDFilter getKey() {
        return key;
    }

    public void setKey(UUIDFilter key) {
        this.key = key;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final TagCriteria that = (TagCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(progressivo, that.progressivo) &&
            Objects.equals(staccato, that.staccato) &&
            Objects.equals(bruciato, that.bruciato) &&
            Objects.equals(key, that.key);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        progressivo,
        staccato,
        bruciato,
        key
        );
    }

    @Override
    public String toString() {
        return "TagCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (progressivo != null ? "progressivo=" + progressivo + ", " : "") +
                (staccato != null ? "staccato=" + staccato + ", " : "") +
                (bruciato != null ? "bruciato=" + bruciato + ", " : "") +
                (key != null ? "key=" + key + ", " : "") +
            "}";
    }

}
