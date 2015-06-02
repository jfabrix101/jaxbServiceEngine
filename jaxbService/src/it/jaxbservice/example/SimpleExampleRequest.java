package it.jaxbservice.example;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class SimpleExampleRequest {

	@XmlElement
	private String inputMessage;
	
	public SimpleExampleRequest() {
	}
	
	public String getInputMessage() {
		return inputMessage;
	}
	
	public void setInputMessage(String inputMessage) {
		this.inputMessage = inputMessage;
	}
	
}
