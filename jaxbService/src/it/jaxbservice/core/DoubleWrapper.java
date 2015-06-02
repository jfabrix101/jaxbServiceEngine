package it.jaxbservice.core;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class DoubleWrapper {

	@XmlElement
	public Double value;

	public DoubleWrapper(Double value) {
		super();
		this.value = value;
	}
	
	public DoubleWrapper() {
	}
	
	public void setValue(Double value) {
		this.value = value;
	}
	
	public Double getValue() {
		return value;
	}
	
}
