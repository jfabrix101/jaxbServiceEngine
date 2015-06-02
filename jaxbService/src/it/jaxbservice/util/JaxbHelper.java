package it.jaxbservice.util;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.SchemaOutputResolver;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.Result;
import javax.xml.transform.stream.StreamResult;

import org.apache.log4j.Logger;

public class JaxbHelper {

	private static Map<String, JAXBContext> jaxbContextMap = new HashMap<String, JAXBContext>();
	private static Logger logger = Logger.getLogger("JaxbHelper");
	
	public static String toXML(Object obj) {
		if (obj == null) return null;
		String className = obj.getClass().getName();
		Class clazz = obj.getClass();
		JAXBContext jc = jaxbContextMap.get(className);
		try {
			if (jc == null) {
				jc  = JAXBContext.newInstance(clazz);
				jaxbContextMap.put(className, jc);
			}
			
			Marshaller marshaller = jc.createMarshaller();
	        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
	        
	        StringWriter sw = new StringWriter();
	        marshaller.marshal(obj, sw);
	        return sw.toString();
		} catch (Exception e) {
			logger.error("Exception mashalling jaxb class: " + className);
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Restituisce un oggetto a partire dalla sua rappresentazione XML
	 * @param xml - File XML (formalmente corretto - vedi rappresentazione JAXB)
	 * @param className - Nome completo della classe in cui deve essere convertito
	 * 
	 * @return null - se l'operazione non va a buon fine o l'xml non e' valido
	 */
	public static Object fromXml(String xml, String className) {
		if (xml == null) return null;
		JAXBContext jc = jaxbContextMap.get(className);
		try {
			if (jc == null) {
				Class clazz = Class.forName(className);
				jc  = JAXBContext.newInstance(clazz);
				jaxbContextMap.put(className, jc);
			}
			
			Unmarshaller unmarshaller = jc.createUnmarshaller();
	        
			Object obj = unmarshaller.unmarshal(new  StringReader(xml));  
			return obj;
		} catch (Exception e) {
			logger.error("Exception unmashalling xml for class: " + className);
			e.printStackTrace();
			return null;
		}
	}
	
	
	public static String toXSD(String className) {
		try {
			Class clazz = Class.forName(className);
			JAXBContext jaxbContext = JAXBContext.newInstance(clazz);
			final StringWriter sw = new StringWriter();
			SchemaOutputResolver sor = new SchemaOutputResolver(){
				@Override
				public Result createOutput(String namespaceUri,	String suggestedFileName) throws IOException {
					StreamResult result = new StreamResult(sw);
					result.setSystemId("liferayService-" + namespaceUri + "-" + suggestedFileName);
					return result;
				}
			};
			jaxbContext.generateSchema(sor);
			return sw.toString();
		} catch (Exception e) {
			logger.error("Error generating schema. " + e.getClass().getName() + " - " + e.getMessage());
			return "Error generating schema: " + e.getMessage();
		}
	}
	
}
