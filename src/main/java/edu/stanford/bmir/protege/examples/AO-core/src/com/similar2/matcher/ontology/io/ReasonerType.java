/**
 * 
 */
package com.similar2.matcher.ontology.io;

/**
 * Enum defining the different types of reasoners supported by ACE
 * 
 * @author Philippe.Genoud (Philippe.Genoud@imag.fr)
 */
public enum ReasonerType {
	
	FACT, PELLET, HERMIT;
	
	/**
	 * returns the enum value of the reasoner type
	 * corresponding to given String (PELLET, HERMIT, FACT)
	 * @param reasoner the String used to select the reasoner type.
	 * @return the ReasonerType corresponding to the string reasoner. If
	 * reasoner is not one of "PELLET", "HERMIT" or "FACT" the default
	 * reasoner type returned is PELLET.
	 */
	public static ReasonerType reasonerType(String reasoner) {
		String reasonerName = reasoner.toUpperCase();
		if (reasonerName.equals("HERMIT"))
			return ReasonerType.HERMIT;
		else if (reasonerName.equals("FACT"))
			return ReasonerType.FACT;
		else
			return ReasonerType.PELLET;
	}

}
