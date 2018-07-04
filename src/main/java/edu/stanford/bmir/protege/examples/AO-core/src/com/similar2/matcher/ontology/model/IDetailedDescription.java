/**
 * 
 */
package com.similar2.matcher.ontology.model;

import java.util.List;

/**
 * @author Philippe Genoud (Philippe.Genoud@imag.fr)
 *
 */
public interface IDetailedDescription {

	/**
	 * @return the entity
	 */
	public IPrimaryEntityClass getEntity();

	/**
	 * @return the properValues
	 */
	public List<IValuePair> getProperValues();

	/**
	 * @return the inheritedValues
	 */
	public List<IValuePair> getInheritedValues() ;
}


