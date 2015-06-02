package it.jaxbservice.core;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class BooleanWrapper {

	@XmlElement
	public Boolean value;
	
	public BooleanWrapper() {
	}
	
	public BooleanWrapper(boolean val) {
		this.value = val;
	}
	
	public Boolean getValue() {
		return value;
	}
	
	public void setValue(Boolean value) {
		this.value = value;
	}
}
