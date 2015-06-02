package it.jaxbservice;

import java.io.Serializable;
import java.util.Properties;

/**
 * Bean per la rappresentazione di un servizio
 * @author GD06548 
 *
 */
public class JaxbServiceBeanInfo implements Serializable {

	private String serviceName;
	private String description;
	private boolean secure;
	private String serviceClassName;
	private String inputClassName;
	private String outputClassName;
	private Properties params = new Properties();
	boolean valid = true;
	
	public String getServiceName() {
		return serviceName;
	}
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public boolean isSecure() {
		return secure;
	}
	public void setSecure(boolean secure) {
		this.secure = secure;
	}
	public String getInputClassName() {
		return inputClassName;
	}
	public void setInputClassName(String inputClassName) {
		this.inputClassName = inputClassName;
	}
	public String getOutputClassName() {
		return outputClassName;
	}
	public void setOutputClassName(String outputClassName) {
		this.outputClassName = outputClassName;
	}
	public Properties getParams() {
		return params;
	}
	public String getServiceClassName() {
		return serviceClassName;
	}
	
	public void setServiceClassName(String serviceClassName) {
		this.serviceClassName = serviceClassName;
	}
	
	public boolean isValid() { return valid; }
	
	
}
