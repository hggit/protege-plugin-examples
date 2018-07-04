package com.similar2.matcher.ontology.model.exceptions;

/**
 * Runtime exceptions concerning values hierarchies
 * @author Philippe Genoud (Philippe.Genoud@imag.fr)
 */
public class GenusRTException extends AristotelianOntologyRTException {

	private static final long serialVersionUID = 6118903425428584212L;

	/**
	 * 
	 */
	public GenusRTException() {
		super();
	}

	/**
	 * @param mess
	 */
	public GenusRTException(String mess) {
		super(mess);
	}

	/**
	 * @param mess
	 * @param cause
	 */
	public GenusRTException(String mess, Throwable cause) {
		super(mess, cause);
	}

}