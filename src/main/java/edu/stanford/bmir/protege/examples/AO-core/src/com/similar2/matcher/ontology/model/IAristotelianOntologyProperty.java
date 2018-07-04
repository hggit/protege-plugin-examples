package com.similar2.matcher.ontology.model;

/**
 * Super type for object properties used in an aristotelian ontology
 * 
 * @author Anthony Hombiat
 */
public interface IAristotelianOntologyProperty extends INamedAndCommentedEntity{
	
	/**
	 * returns this property's domain
	 * @return domain
	 */
	public IAristotelianOntologyClass getDomain();
	
	/**
	 * returns this property's range
	 * @return range
	 */
	public IAristotelianOntologyClass getRange();
	
	/**
	 * sets this property's domain
	 * @param the property domain
	 */
	public void setDomain(IAristotelianOntologyClass domain);
	
	/**
	 * sets this property's range
	 * @param the property range
	 */
	public void setRange(IAristotelianOntologyClass range);

}
