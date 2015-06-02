package it.jaxbservice.core;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class EchoTimestampResponse {

	private String timestamp;
	private String locale;
	private long millis;
	
	public String getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
	public String getLocale() {
		return locale;
	}
	public void setLocale(String locale) {
		this.locale = locale;
	}
	public long getMillis() {
		return millis;
	}
	public void setMillis(long millis) {
		this.millis = millis;
	}
	
	
	
	
	
	
}
