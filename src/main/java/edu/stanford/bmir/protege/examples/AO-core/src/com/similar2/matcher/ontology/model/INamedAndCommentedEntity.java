package com.similar2.matcher.ontology.model;

public interface INamedAndCommentedEntity extends INamedEntity, ICommentedEntity{

	/**
	 * Removes this named and commented entity from the aristotelian ontology
	 */
	public void remove();
}
