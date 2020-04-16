package it.kamaladafrica.eliminacode.service.dto;

import java.io.Serializable;

public class TagStatsDTO implements Serializable {

	private Long fila;
	private Long tempoStimato; // minutes

	public Long getFila() {
		return fila;
	}

	public void setFila(Long fila) {
		this.fila = fila;
	}

	public Long getTempoStimato() {
		return tempoStimato;
	}

	public void setTempoStimato(Long tempoStimato) {
		this.tempoStimato = tempoStimato;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((fila == null) ? 0 : fila.hashCode());
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
		if (tempoStimato == null) {
			if (other.tempoStimato != null)
				return false;
		} else if (!tempoStimato.equals(other.tempoStimato))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "TagStatsDTO [fila=" + fila + ", tempoStimato=" + tempoStimato + "]";
	}

}
