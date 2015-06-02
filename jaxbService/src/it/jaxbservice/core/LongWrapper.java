package it.jaxbservice.core;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class LongWrapper {

	@XmlElement
	public Long value;

	public LongWrapper(Long value) {
		super();
		this.value = value;
	}
	
	public LongWrapper() {
		// TODO Auto-generated constructor stub
	}
	
	public void setValue(Long value) {
		this.value = value;
	}
	
	public Long getValue() {
		return value;
	}
	
}
