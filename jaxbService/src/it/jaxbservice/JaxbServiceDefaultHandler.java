package it.jaxbservice;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class JaxbServiceDefaultHandler extends DefaultHandler {

	private List<JaxbServiceBeanInfo> list = new ArrayList<JaxbServiceBeanInfo>();
	private Logger logger = Logger.getLogger(getClass());
	
	public void clear() { list.clear(); }

	private boolean checkInputOutputClass = true;
	public void init() {
		checkInputOutputClass = JaxbServiceConfigSingleton.conf.getAsBoolean("checkInputOutputClass");
	}
	
	public List<JaxbServiceBeanInfo> getResultList() {
		return list;
	}
	
	JaxbServiceBeanInfo current = null;
	
	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) 
	throws SAXException {
		super.startElement(uri, localName, qName, attributes);
		String tagname = localName;
	    
		if ("service".equals(tagname)) {
			current = new JaxbServiceBeanInfo();
			Properties attr = getAsProperties(attributes);
			current.setDescription(attr.getProperty("description"));
			current.setServiceName(attr.getProperty("name"));
			String secure = attr.getProperty("secure");
			if (secure == null) secure = "false";
			current.setSecure(("true".equalsIgnoreCase(secure)));
			return;
		}
		
		if ("param".equals(tagname)) {
			Properties attr = getAsProperties(attributes);
			current.getParams().setProperty(attr.getProperty("name"), attr.getProperty("value"));
			return;
		}
	}
	
	public void endElement(String uri, String localName, String qName) 
	throws SAXException {
		String tagname = localName;
		String insideText = text.toString();
		if (insideText != null) insideText = insideText.trim();
		text = new StringBuilder("");
		
		if ("service".equalsIgnoreCase(tagname)) {
			if (current.valid) list.add(current);
			else logger.warn("*** Invalid service definition for serviceName : " + current.getServiceName());
			current = null;
			return;
		}
		if ("inputClass".equalsIgnoreCase(tagname)) {
			current.setInputClassName(insideText);
			if (checkInputOutputClass)  current.valid = checkClass(insideText);
			return;
		}
		if ("outputClass".equalsIgnoreCase(tagname)) {
			current.setOutputClassName(insideText);
			if (checkInputOutputClass)  current.valid = checkClass(insideText);
			return;
		}
		if ("implementationClass".equalsIgnoreCase(tagname)) {
			current.setServiceClassName(insideText);
			if (checkInputOutputClass)  current.valid = checkClass(insideText);
			return;
		}
		
		
	};
	
	private Properties getAsProperties(Attributes attributes) {
		Properties attr = new Properties();
		int numAttr = attributes.getLength();
		for(int i=0; i<numAttr; i++) {
			String name = attributes.getQName(i);
			String value = attributes.getValue(i);
			attr.put(name, value);
		}
		return attr;
	}
	
	StringBuilder text = new StringBuilder();
	public void characters(char[] arg1, int arg2, int arg3) {
        String s = new String(arg1, arg2, arg3);
        s.replaceAll("\\s+", " ");
		text.append(s);
	}
	
	
	private boolean checkClass(String className) {
		// Una classe vuota è considerata valida
		if (StringUtils.isEmpty(className)) return true;
		try {
			Class.forName(className);
			return true;
		} catch (Exception e) {
			logger.warn("*** Invalid className : " + e.getMessage());
			return false;
		}
	}
}
