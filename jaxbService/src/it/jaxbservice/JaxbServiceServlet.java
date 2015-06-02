package it.jaxbservice;

import it.jaxbservice.core.ReloadServices;
import it.jaxbservice.util.JaxbHelper;
import it.jaxbservice.util.XProperties;

import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;



public class JaxbServiceServlet extends HttpServlet {

	Logger logger = Logger.getLogger(getClass());
	
	
	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		
		logger.info("init() - Startng JSE (JaxbServiceEngine) ver. 1.0 ");
		
		JaxbServiceConfigSingleton.webinf_FolderPath = config.getServletContext().getRealPath("WEB-INF");
		String serviceFolderPath = config.getServletContext().getRealPath("WEB-INF/services");

		// Richiama il servizio di reload (per centralizzare il meccanismo)
		JaxbService reloadService = new ReloadServices();
		reloadService.servletContext = config.getServletContext();
		try {
			reloadService.execute(null);
		} catch (JaxbServiceException e) {
			logger.fatal("Exception loading/initializing services... " + e.getMessage());
			e.printStackTrace();
			throw new ServletException(e);
		}
		
	}
	

	/**
	 * Evade la richiesta di servizio (sia GET che POST)
	 */
	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		String res = "";
		try {
			res = executeService(req, resp);
			if (res == null) res = "";
		} catch (JaxbServiceException e) {
			res = "ServiceException: " + e.getMessage();
			logger.warn(res);
		}
		
		resp.setContentType("application/json");
		resp.getOutputStream().write(res.getBytes(), 0, res.length());
	}
	
	
	/**
	 * Invocazione dinamica del servzio
	 * @param req
	 * @param resp
	 * @return XML (unmarshall) di rispsota
	 * @throws JaxbServiceException
	 */
	private String executeService(HttpServletRequest req, HttpServletResponse resp) 
	throws JaxbServiceException {
		String res = null;

		String threadId = "" + System.currentTimeMillis();
		threadId = threadId.substring(6);  // Solo le ultime 7 cifre
		
		Map<String, String[]> maps = req.getParameterMap();
		Properties params = new Properties();
		for (Entry<String, String[]> en : maps.entrySet()) {
			String[] val = en.getValue();
			if (val == null) continue;
			params.put(en.getKey(), val[0]);
		}
		
		String serviceName = params.getProperty("serviceName");
		if (serviceName == null) throw new JaxbServiceException("Invalid (or null) serviceName parameter");
		
		JaxbServiceBeanInfo serviceInfo = JaxbServiceConfigSingleton.serviceMap.get(serviceName);
		if (serviceInfo == null) throw new JaxbServiceException("Invalid serviceName - service definition not found !");
		
		String payload = params.getProperty("payload");
		if (payload == null) payload = "";
		

		boolean trace = false;
		if ("true".equalsIgnoreCase(params.getProperty("traceMode"))) trace = true;
		
		try {
			
			trace(threadId, "Executing " + serviceInfo.getServiceName() 
					+ " service. Class " + serviceInfo.getServiceClassName()
					+ " - RequestID = " + threadId);
			
			Class serviceClass = Class.forName(serviceInfo.getServiceClassName());
			if (trace) trace(threadId, "ServiceClass: " + serviceClass);
			
			JaxbService impl = (JaxbService)serviceClass.newInstance();
			impl.threadId = threadId;
			impl.init(serviceInfo.getParams());
			impl.servletContext = getServletContext();
			impl.inputClassName = serviceInfo.getInputClassName();
			impl.outputClassName = serviceInfo.getOutputClassName();
			impl.serviceName = serviceInfo.getServiceName();
			impl.conf = new XProperties(serviceInfo.getParams());
			
			if (trace && logger.isDebugEnabled()) {
				trace(threadId, "Created service class instance!");
				trace(threadId, "inputClass: " + serviceInfo.getInputClassName());
				trace(threadId, "outputClass: " + serviceInfo.getOutputClassName());
				trace(threadId, "payload : " + payload);
			}
			
			
			Object inputObj = null;
			Object outputObj = null;

			// Se c'è un paylod ma non è previsto in tipo in Input: Errore
			if (!StringUtils.isEmpty(payload) && StringUtils.isEmpty(impl.inputClassName)) {
				throw new JaxbServiceException("Invalid input - According to service definizio, the service don't requires an input");
			}
			
			// Se non c'è un payload ma è previsto un TIPO in input : Errore
			if (StringUtils.isEmpty(payload) && !StringUtils.isEmpty(impl.inputClassName)) {
				throw new JaxbServiceException("Invalid input - According to service definizio, the service requires an input. Payload is empty");
			}
			
			if (payload != null) {
				try {
					trace(threadId, "Casting object from XML to" + impl.inputClassName);
					inputObj = JaxbHelper.fromXml(payload, impl.inputClassName);
					trace(threadId, "Input instance created: " + inputObj);
				} catch (Exception e) {
					trace(threadId, "Exception converting input in object of type " + impl.inputClassName);
					e.printStackTrace();
					throw new JaxbServiceException(e);
				}
				
			}
			
			long serviceTime = System.currentTimeMillis();
			outputObj = impl.execute(inputObj);
			serviceTime = -(serviceTime - System.currentTimeMillis());
			trace(threadId, "Service excecuted in " + serviceTime + " millis");
			
			if (trace && outputObj != null) trace(threadId, "outputObj : " + outputObj.toString());
			
			if (outputObj != null) {
					
				res = outputObj.toString();
				try {
					if (StringUtils.isNotEmpty(impl.outputClassName)) res = JaxbHelper.toXML(outputObj);
				} catch (Exception e) {
					throw new JaxbServiceException("Exception castin the outoput. Exptected " 
							+ impl.outputClassName + ", found object of type " 
							+ outputObj.getClass().getName() + " !!");
				}
			} else res = "null";
			
			if (trace) trace(threadId, "xmlOutput : " + res);
			
			
			
		} catch (Exception e) {
			logger.error("Generic error executing service: " + e.getClass().getName() + " - " + e.getMessage());
			e.printStackTrace();
			throw new JaxbServiceException(e.getMessage());
		}
			
		return res;
	}
	
	
	private void trace(String threadId, String msg) {
		logger.debug("[" + threadId + "] - " + msg);
	}
}
