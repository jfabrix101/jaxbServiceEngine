package it.jaxbservice;

public class JaxbServiceException extends Exception {

	public JaxbServiceException() {
		super();
	}

	public JaxbServiceException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public JaxbServiceException(String message, Throwable cause) {
		super(message, cause);
	}

	public JaxbServiceException(String message) {
		super(message);
	}

	public JaxbServiceException(Throwable cause) {
		super(cause);
	}

}
