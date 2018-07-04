package com.similar2.matcher.ontology.model;

import java.io.Serializable;

/**
 * A NamedEntity has a unique name (id) defined in a name space. A named entity
 * can have various labels in different languages.
 * 
 * @author Philippe Genoud (Philippe.Genoud@imag.fr)
 */
public interface INamedEntity extends Serializable, Comparable<INamedEntity> {

	/**
	 * returns the name space of this entity
	 * 
	 * @return the name space of this entity
	 */
	public String getNameSpace();

	/**
	 * returns the Id of this entity
	 * 
	 * @return the Id of this entity
	 */
	public String getId();

	/**
	 * returns the fully qualified name of this entity
	 * 
	 * @return the fully qualified name (concatenation of name space and local
	 *         name)
	 */
	public String getFQName();
	
	/**
	 * @return the aristotelian ontology this named entity belongs to
	 */
	public IAristotelianOntology getAO();

	/**
	 * adds a label for a given language
	 * 
	 * @param language
	 *            the language of the label
	 * @param label
	 *            the comment to add
	 */
	public void addLabel(String language, String label);

	/**
	 * initializes the labels of this named entity
	 * 
	 * @param labels
	 */
	public void setLabels(IAnnotations labels);

	/**
	 * returns all the label of this entity
	 *  
	 * @return the labels of this entity for each language that is defined, or
	 *         the id of the entity if it does not have any labels
	 */
	public IAnnotations getLabels();

	/**
	 * returns the label of this entity
	 * 
	 * @param language
	 *            the language of the label
	 * 
	 * @return the label of this entity if it exist for this language, or
	 * 		   the id of the entity if it does not have a label for the given language
	 */
	public String getLabel(String language);

	/**
	 * sets a new id (the given id) for the entity.
	 * 
	 * @param id
	 */
	public void setId(String id);
	
	/**
	 * Removes this named entity from the aristotelian ontology
	 */
	public void remove();

}