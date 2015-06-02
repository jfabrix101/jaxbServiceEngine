package it.jaxbservice;

import it.jaxbservice.util.XProperties;

import java.util.HashMap;
import java.util.Map;

public class JaxbServiceConfigSingleton {

	public static Map<String, JaxbServiceBeanInfo> serviceMap = new HashMap<String, JaxbServiceBeanInfo>();
	public static String webinf_FolderPath = null;
	public static XProperties conf = new XProperties();

}
