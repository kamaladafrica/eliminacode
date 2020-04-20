package it.kamaladafrica.eliminacode.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;

public class TagStatsDTO implements Serializable {

	private List<Long> fila;
	private Double tempoStimato; // minutes
	private Instant tempoLimite;
	private Long progressivo;

	public Long getProgressivo() {
		return progressivo;
	}

	public void setProgressivo(Long progressivo) {
		this.progressivo = progressivo;
	}

	public List<Long> getFila() {
		return fila;
	}

	public void setFila(List<Long> fila) {
		this.fila = fila;
	}

	public Double getTempoStimato() {
		return tempoStimato;
	}

	public void setTempoStimato(Double tempoStimato) {
		this.tempoStimato = tempoStimato;
	}

	public Instant getTempoLimite() {
		return tempoLimite;
	}

	public void setTempoLimite(Instant tempoLimite) {
		this.tempoLimite = tempoLimite;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((fila == null) ? 0 : fila.hashCode());
		result = prime * result + ((progressivo == null) ? 0 : progressivo.hashCode());
		result = prime * result + ((tempoLimite == null) ? 0 : tempoLimite.hashCode());
		result = prime * result + ((tempoStimato == null) ? 0 : tempoStimato.hashCode());
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
		TagStatsDTO other = (TagStatsDTO) obj;
		if (fila == null) {
			if (other.fila != null)
				return false;
		} else if (!fila.equals(other.fila))
			return false;
		if (progressivo == null) {
			if (other.progressivo != null)
				return false;
		} else if (!progressivo.equals(other.progressivo))
			return false;
		if (tempoLimite == null) {
			if (other.tempoLimite != null)
				return false;
		} else if (!tempoLimite.equals(other.tempoLimite))
			return false;
		if (tempoStimato == null) {
			if (other.tempoStimato != null)
				return false;
		} else if (!tempoStimato.equals(other.tempoStimato))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "TagStatsDTO [fila=" + fila + ", tempoStimato=" + tempoStimato + ", tempoLimite=" + tempoLimite
				+ ", progressivo=" + progressivo + "]";
	}

}
