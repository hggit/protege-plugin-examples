package com.similar2.matcher.ontology.model.exceptions;


public class PrimaryEnumeratedClassRTException extends AristotelianOntologyRTException{

	private static final long serialVersionUID = 1L;
	
	public PrimaryEnumeratedClassRTException(){
		super();
	}

	/**
	 * @param mess
	 */
	public PrimaryEnumeratedClassRTException(String mess) {
		super(mess);
	}

	/**
	 * @param mess
	 * @param cause
	 */
	public PrimaryEnumeratedClassRTException(String mess, Throwable cause) {
		super(mess, cause);
	}
	
}