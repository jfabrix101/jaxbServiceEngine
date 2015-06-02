package it.jaxbservice.example;

import it.jaxbservice.JaxbService;
import it.jaxbservice.JaxbServiceException;

public class SimpleExample extends JaxbService{

	
	@Override
	public Object execute(Object input) throws JaxbServiceException {
		
		debug("Starting " + getServiceName());
		SimpleExampleRequest req = (SimpleExampleRequest) input;
		
		String inputMessage = req.getInputMessage();
		
		SimpleExampleResponse resp = new SimpleExampleResponse();
		resp.setTimeStamp(System.currentTimeMillis());
		
		String confValue = conf.getProperty("welcomeMessage");
		if (confValue == null) { debug("welcome messager not found"); confValue = ""; }
		
		resp.setOutputMessage(confValue + " " + inputMessage);
		
		return resp;
		
	}
	
	
}
