package com.similar2.matcher.ontology.model;

import java.io.Serializable;

/**
 * A LocalizedString is a String associated with a locale (language). 
 * 
 * @author Philippe Genoud (Philippe.Genoud@imag.fr)
 */
public interface ILocalizedString extends Serializable {

	/**
	 * returns the language of this String
	 * @return the language
	 */
	public String getLanguage();
	
	/**
	 * returns the String
	 * @return the String
	 */
	public String getString();
}