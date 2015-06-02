package it.jaxbservice;

import java.util.Collections;
import java.util.Properties;

import javax.servlet.ServletContext;

import org.apache.log4j.Logger;

import it.jaxbservice.util.XProperties;

/**
 * Classe base per l'eseposizione del servizio
 * @author Fabrizio Russo
 *
 */
public abstract class JaxbService {

	protected XProperties conf = null;
	String inputClassName, outputClassName, serviceName;
	ServletContext servletContext;
	
	
	private Logger logger = Logger.getLogger(getClass());
	
	String threadId = "";
	
	protected void debug(String msg) {
		logger.debug("[" + threadId + "] - " + msg);
	}
	
	public String getInputClassName() { return inputClassName; }
	public String getServiceName() { return serviceName; }
	public String getOutputClassName() { return outputClassName; }
	
	protected void init(Properties prop) {
		if (prop == null) conf = new XProperties();
		else conf = new XProperties(conf);
	}
	
	public ServletContext getServletContext() { return servletContext; }
	
	public XProperties getConfigProperties() {
		return JaxbServiceConfigSingleton.conf; 
	}
	
	public abstract Object execute(Object input) throws JaxbServiceException;
	
	
}
