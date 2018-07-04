package com.similar2.matcher.ontology.model;

import java.util.Collection;
import java.util.Set;

import com.similar2.matcher.ontology.model.exceptions.PrimaryEnumeratedClassRTException;

/**
 * 
 * @author Anthony
 */
public interface IPrimaryEnumeratedClass extends IAristotelianOntologyClass,IEnumeratedClass{
	
	/**
	 * Deletes the corresponding value hierarchy
	 * 
	 * @throws PrimaryEnumeratedClassRTException if a property declares the value hierarchy as range
	 */
	public void deleteValueHierarchy() throws PrimaryEnumeratedClassRTException;
	
	/**
	 * Deletes a given sub-hierarchy of PrimaryEnumeratedClasses
	 * 
	 * @param fqPrimaryEnumeratedClasses
	 * 		fully qualified name of the root of the sub-hierarchy to delete
	 * 
	 */
	public void deleteSubHierarchy(String fqPrimaryEnumeratedClasses);
	
	
	/**
	 * removes from the value hierarchy graph (DAG Direct Acyclic Graph)
	 * the subtree issued from cl where cl is a direct sublclass of
	 * this primary enumerated class.
	 * When a class of the removed tree has no more parent, it is also
	 * removed from the various indexes used by the aristotelian ontology this
	 * class belongs to. 
	 * 
	 * @param cl the root class of the subtree to remove.
	 */
	public void removeSubTree(IPrimaryEnumeratedClass cl);
	

	/**
	 * Returns the set of EnumeratedProperties used in this AristotelianOntology
	 * 		that have the value hierarchy corresponding to this enumerated class as range
	 * 
	 * @return the set of EnumeratedProperties
	 */
	public Set<IEnumeratedProperty> findEnumeratedPropertiesByRange();
	
	/**
	 * Find all PrimaryEntityClasses whose definition includes an attribute 
	 * 		having this PrimaryEnumeratedClass in the range
	 * 
	 * @return  all corresponding PrimaryEntityClasses
	 */
	public Collection<IPrimaryEntityClass> findPrimaryEntityClassesWithThisAttributeValue();
	
	/**
	 * Find all PrimaryEntityClasses whose definition includes an attribute 
	 * 		having this PrimaryEnumeratedClass or any inherited PrimaryEnumeratedClass in the range
	 *  
	 * @return all corresponding PrimaryEntityClasses
	 */
	public Collection<IPrimaryEntityClass> findPrimaryEntityClassesWithAnyInheritedAttributeValue();
	

	public ClassificationRelationship findClassificationRelationshipAmong(Collection<IPrimaryEnumeratedClass> pecs);

	/**
	 * removes all references to this primary enumerated class in the Aristotelian ontology
	 * this class belongs to.
	 */
	public void removeFromAODataStructures();
}
