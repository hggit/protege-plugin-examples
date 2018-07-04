package com.similar2.matcher.ontology.model;

/**
 * Enumerated class defining the different types of classification
 * relationships between an entity and an other entity.
 *  
 * @author Philippe Genoud (Philippe.Genoud@imag.fr)
 */
public enum ClassificationRelationship {
	/**
	 * if the two entities are equivalent (define the same concept)
	 */
	EQUIVALENT{
		@Override
	public ClassificationRelationship inv() {
		return EQUIVALENT;
		}},
	/**
	 * if the entity is a sub class (direct, or indirect) of the
	 * other entity.
	 */
	SUBCLASS{
		@Override
	public ClassificationRelationship inv() {
		return SUPERCLASS;
		}}, 
	
	/**
	 * if the entity is a super class (direct, or indirect) of the
	 * other entity.
	 */
	SUPERCLASS{
		@Override
	public ClassificationRelationship inv() {
		return SUBCLASS;
		}}, 
	
	/**
	 * if the entity has no inheritance relation with the other
	 * entity.
	 */
	NO_RELATION{
		@Override
	public ClassificationRelationship inv() {
		return NO_RELATION;
		}};
	
	
	public abstract ClassificationRelationship inv();
}
