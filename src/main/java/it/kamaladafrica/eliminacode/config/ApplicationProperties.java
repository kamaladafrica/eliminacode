package it.kamaladafrica.eliminacode.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Properties specific to Eliminacode.
 * <p>
 * Properties are configured in the {@code application.yml} file.
 * See {@link io.github.jhipster.config.JHipsterProperties} for a good example.
 */
@ConfigurationProperties(prefix = "application", ignoreUnknownFields = false)
public class ApplicationProperties {

	private int qrcodeSize = 160;
	private double averageTempo = 2.5; // minuti

	public double getAverageTempo() {
		return averageTempo;
	}

	public void setAverageTempo(double averageTempo) {
		this.averageTempo = averageTempo;
	}

	public int getQrcodeSize() {
		return qrcodeSize;
	}

	public void setQrcodeSize(int qrcodeSize) {
		this.qrcodeSize = qrcodeSize;
	}

}
