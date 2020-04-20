package it.kamaladafrica.eliminacode.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

import javax.validation.constraints.NotNull;

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

	private String qrCodeImageUrl;

	public String getQrCodeImageUrl() {
		return qrCodeImageUrl;
	}

	public void setQrCodeImageUrl(String qrCodeImageUrl) {
		this.qrCodeImageUrl = qrCodeImageUrl;
	}

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
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((bruciato == null) ? 0 : bruciato.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((key == null) ? 0 : key.hashCode());
		result = prime * result + ((progressivo == null) ? 0 : progressivo.hashCode());
		result = prime * result + ((qrCodeImageUrl == null) ? 0 : qrCodeImageUrl.hashCode());
		result = prime * result + ((staccato == null) ? 0 : staccato.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TagDTO other = (TagDTO) obj;
		if (bruciato == null) {
			if (other.bruciato != null)
				return false;
		} else if (!bruciato.equals(other.bruciato))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (key == null) {
			if (other.key != null)
				return false;
		} else if (!key.equals(other.key))
			return false;
		if (progressivo == null) {
			if (other.progressivo != null)
				return false;
		} else if (!progressivo.equals(other.progressivo))
			return false;
		if (qrCodeImageUrl == null) {
			if (other.qrCodeImageUrl != null)
				return false;
		} else if (!qrCodeImageUrl.equals(other.qrCodeImageUrl))
			return false;
		if (staccato == null) {
			if (other.staccato != null)
				return false;
		} else if (!staccato.equals(other.staccato))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "TagDTO [id=" + id + ", progressivo=" + progressivo + ", staccato=" + staccato + ", bruciato=" + bruciato
				+ ", key=" + key + ", qrCodeImageUrl=" + qrCodeImageUrl + "]";
	}

}
