package com.similar2.matcher.ontology.model;

import java.io.Serializable;

public interface ICommentedEntity extends Serializable, Comparable<INamedEntity>{

	/**
	 * returns the name space of this entity
	 * @return the name space of this entity
	 */
	public String getNameSpace();

	/**
	 * returns the Id of this entity
	 * @return the Id of this entity
	 */
	public String getId();

	/**
	 * returns the fully qualified name of this entity
	 * @return the fully qualified name (concatenation of name space and
	 *         local name)
	 */
	public String getFQName();
	
	/**
	 * adds a comment for a given language
	 * @param language the language of the comment
	 * @param comment the comment to add
	 */
	public void addComment(String language, String comment);

	/**
	 * adds a list of comments for a given language
	 * @param language the language of the comment
	 * @param comment the comment to add
	 */
	public void addComments(IAnnotations comments);

	/**
	 * initializes the labels of this named entity
	 * @param labels
	 */
	public void setComments(IAnnotations comments);
	
	/**
	 * returns the label of this entity
	 * @param language
	 *            the language of the label
	 * 
	 * @return the label of this entity if it exist for this language, null if
	 *         it does not exist
	 */
	public String getComment(String language);
	
}
