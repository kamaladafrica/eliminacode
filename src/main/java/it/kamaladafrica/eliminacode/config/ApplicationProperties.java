package it.kamaladafrica.eliminacode.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Properties specific to Eliminacode.
 * <p>
 * Properties are configured in the {@code application.yml} file. See
 * {@link io.github.jhipster.config.JHipsterProperties} for a good example.
 */
@ConfigurationProperties(prefix = "application", ignoreUnknownFields = false)
public class ApplicationProperties {

	private int qrcodeSize = 160;
	private double averageTempo = 2.5; // minuti
	private int expiry = 15; // minuti
	private String qrcodeUrlTemplate = null;

	public String getQrcodeUrlTemplate() {
		return qrcodeUrlTemplate;
	}

	public void setQrcodeUrlTemplate(String qrcodeUrlTemplate) {
		this.qrcodeUrlTemplate = qrcodeUrlTemplate;
	}

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

	public int getExpiry() {
		return expiry;
	}

	public void setExpiry(int expiry) {
		this.expiry = expiry;
	}

}
