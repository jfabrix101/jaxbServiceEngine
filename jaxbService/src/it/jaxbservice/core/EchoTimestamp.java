package it.jaxbservice.core;

import java.util.Date;
import java.util.Locale;





import it.jaxbservice.JaxbService;
import it.jaxbservice.JaxbServiceException;

public class EchoTimestamp extends JaxbService {

	@Override
	public Object execute(Object input) throws JaxbServiceException {
		
		Date dd = new Date();
		debug("Current timestamp :" + dd);
		
		EchoTimestampResponse res = new EchoTimestampResponse();
		res.setLocale(Locale.getDefault().toString());
		res.setTimestamp(dd.toString());	
		res.setMillis(dd.getTime());
		
		return res;
		
	}

}
