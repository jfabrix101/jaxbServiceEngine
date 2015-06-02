package it.jaxbservice.core;

import java.io.File;
import java.io.FileReader;
import java.io.InputStream;
import java.util.Map.Entry;

import javax.servlet.ServletException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.log4j.Logger;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import it.jaxbservice.JaxbService;
import it.jaxbservice.JaxbServiceBeanInfo;
import it.jaxbservice.JaxbServiceConfigSingleton;
import it.jaxbservice.JaxbServiceDefaultHandler;
import it.jaxbservice.JaxbServiceException;

public class ReloadServices extends JaxbService {

	private Logger logger = Logger.getLogger(getClass());
	
	@Override
	public Object execute(Object input) throws JaxbServiceException {
		
		try {
			// loading confProperties
			logger.debug("Loading conf.properties ....");
			InputStream confIS = getClass().getClassLoader().getResourceAsStream("conf.properties");
			JaxbServiceConfigSingleton.conf.load(confIS);
			if (logger.isDebugEnabled()) {
				logger.debug("Loaded configuration file");
				for (Entry entry : JaxbServiceConfigSingleton.conf.entrySet()) {
					String k = entry.getKey().toString();
					String v = entry.getValue().toString();
					logger.debug(" --> " + k + " = " + v);
				}
			}
			
		} catch (Exception e) {
			logger.warn("Error loading configuration file: " + e.getMessage());
		}
		
		SAXParserFactory spf = SAXParserFactory.newInstance();
	    spf.setNamespaceAware(true);
	    XMLReader xmlReader = null;
	    JaxbServiceDefaultHandler parser = new JaxbServiceDefaultHandler();
	    try {
	    	SAXParser saxParser = spf.newSAXParser();
	    	xmlReader = saxParser.getXMLReader();
	    	xmlReader.setContentHandler(parser);
	    } catch (Exception e) {
	    	e.printStackTrace();
	    	throw new JaxbServiceException("Unable to create a SAX parser: " + e.getMessage());
	    }
		
	    String serviceFolderPath = getServletContext().getRealPath("WEB-INF/services");
		File fileServices[] = new File(serviceFolderPath).listFiles();
		if (fileServices != null) for (File f : fileServices) {
			logger.info("parsing file : " + f.getAbsolutePath());
			try {
				parser.clear();
				xmlReader.parse(new InputSource(new FileReader(f)));

				for (JaxbServiceBeanInfo serviceBean : parser.getResultList()) {
					logger.debug(" +-- Service " + serviceBean.getServiceName() + " - " + serviceBean.getDescription());
					JaxbServiceConfigSingleton.serviceMap.put(serviceBean.getServiceName(), serviceBean);
				}
				
			} catch (Exception ee) {
				logger.error("Error parsing file " + f.getAbsolutePath() + ": " + ee.getMessage());
			}
		}
		
		logger.info("Found " + JaxbServiceConfigSingleton.serviceMap.size() + " service definitions. - Jaxb Service Engine ready !!");
		
		return new BooleanWrapper(true);
	}

}
