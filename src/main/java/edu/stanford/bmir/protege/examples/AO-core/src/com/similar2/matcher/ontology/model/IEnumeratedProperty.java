package com.similar2.matcher.ontology.model;

import java.util.Collection;

/**
 * Represents an Enumerated property
 * @author Anthony Hombiat
 */
public interface IEnumeratedProperty extends IAristotelianOntologyProperty{
	
	/**
	 * tests if the property is a functional property
	 * @return true if the property is functional
	 */
	public boolean isFunctional();
	
	/**
	 * 
	 * @param functional tru if the property is functional, false else
	 * 
	 */
	public void setFunctional(boolean functional);
	
	/**
	 * Find the PrimaryEntityClasses whose definition includes an attribute for this property
	 * 
	 * @return the set of all IPrimaryEntityClasses that have any corresponding attribute
	 */
	public Collection<IPrimaryEntityClass> findPrimaryEntityClassesByProperty();

}
