package it.jaxbservice.core;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class StringWrapper {

	@XmlElement
	public String value;
	
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	public StringWrapper(String value) {
		super();
		this.value = value;
	}
	
	public StringWrapper() {
	}
}
