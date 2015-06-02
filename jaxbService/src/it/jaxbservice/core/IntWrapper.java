package it.jaxbservice.core;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class IntWrapper {

	@XmlElement
	public Integer value;

	public IntWrapper(Integer value) {
		super();
		this.value = value;
	}
	
	public IntWrapper() {
	}
	
	public void setValue(Integer value) {
		this.value = value;
	}
	
	public Integer getValue() {
		return value;
	}
	
}
