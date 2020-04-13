package it.kamaladafrica.eliminacode.service.dto;

import java.time.Instant;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

/**
 * A DTO for the {@link it.kamaladafrica.eliminacode.domain.Tag} entity.
 */
public class TagDTO implements Serializable {
    
    private Long id;

    private Long progressivo;

    @NotNull
    private Instant staccato;

    private Instant bruciato;

    @NotNull
    private UUID key;

    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProgressivo() {
        return progressivo;
    }

    public void setProgressivo(Long progressivo) {
        this.progressivo = progressivo;
    }

    public Instant getStaccato() {
        return staccato;
    }

    public void setStaccato(Instant staccato) {
        this.staccato = staccato;
    }

    public Instant getBruciato() {
        return bruciato;
    }

    public void setBruciato(Instant bruciato) {
        this.bruciato = bruciato;
    }

    public UUID getKey() {
        return key;
    }

    public void setKey(UUID key) {
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

        TagDTO tagDTO = (TagDTO) o;
        if (tagDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), tagDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "TagDTO{" +
            "id=" + getId() +
            ", progressivo=" + getProgressivo() +
            ", staccato='" + getStaccato() + "'" +
            ", bruciato='" + getBruciato() + "'" +
            ", key='" + getKey() + "'" +
            "}";
    }
}
