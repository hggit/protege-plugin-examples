/**
 * 
 */
package com.similar2.matcher.ontology.model.exceptions;


/**
 * Runtime exceptions concerning values hierarchies
 * @author Philippe Genoud (Philippe.Genoud@imag.fr)
 */
public class ValueHierarchyRTException extends AristotelianOntologyRTException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6118903425428584212L;

	/**
	 * 
	 */
	public ValueHierarchyRTException() {
		super();
	}

	/**
	 * @param mess
	 */
	public ValueHierarchyRTException(String mess) {
		super(mess);
	}

	/**
	 * @param mess
	 * @param cause
	 */
	public ValueHierarchyRTException(String mess, Throwable cause) {
		super(mess, cause);
	}

}
