package com.similar2.matcher.ontology.model;

import java.util.Collection;
import java.util.Set;

import com.similar2.matcher.ontology.model.exceptions.GenusRTException;

/**
 * A primary entity class is defined in an aristotelian manner, by a set of
 * someValuesFrom restrictions on enumerated properties.
 * 
 * The class is defined by set of attributes, each attribute is described as a
 * pair <enumerated property, <set of values> >.
 * 
 * @author Philippe Genoud (Philippe.Genoud@imag.fr)
 * @author Anthony
 */
public interface IPrimaryEntityClass extends IAristotelianOntologyClass,
		IEntityClass {

	/**
	 * returns the number of attributes of the attributes set defining this
	 * class
	 * 
	 * @return the number of attributes
	 */
	public int getNbAttributes();

	/**
	 * returns the list of attributes descriptions defining this class
	 * 
	 * @return the list of attribute descriptions
	 */
	public Set<IAttributeDescription> getAttributesDescriptions();

	/**
	 * Returns the set of EnumeratedProperties used in this AristotelianOntology
	 * 		that have the genus corresponding to this PrimaryEntityClass for domain
	 * 
	 * @return the set of EnumeratedProperties
	 */
	public Set<IEnumeratedProperty> findEnumeratedPropertiesByDomain();
	
	/**
	 * Deletes this PrimaryEntityClass. Subclasses are reclassified.
	 * 
	 * 	the fully qualified name of the PrimaryEntityClass to delete
	 */
	public void deletePrimaryEntityClass();
	
	/**
	 * Deletes this PrimaryEntityClass with all its subclasses
	 * 
	 */
	public void deletePrimaryEntityClassHierarchy();
	
	/**
	 * adds to the attributes set defining this class an attribute <property,
	 * <set of values>> If in this class definition there is already an
	 * attribute for the given property, the set of values is added to the already
	 * defined set of values associated to this property.
	 * 
	 * @param property
	 *            the property for the attribute.
	 * @param values
	 *            the set of values.
	 */
	public void addAttributeDescription(IEnumeratedProperty property,
			Collection<IPrimaryEnumeratedClass> values);

	/**
	 * compares the properties set defining the attributes of this class with
	 * the properties set of an other primary entity class
	 * 
	 * @param pec
	 *            the primary entity class to compare to
	 * @return the relation between to properties sets of the two classes :
	 *         EQUALS if the two properties sets are the same CONTAINS if the
	 *         properties set of the class contains the properties set of the
	 *         pec class CONTAINED if the properties set of the class is
	 *         contained in the properties set of the pec class DIFFERENT else
	 */
	public PropertySetRelations comparePropertySet(IPrimaryEntityClass pec);

	/**
	 * computes the classification relation ship between this class and an other
	 * primary entity class
	 * 
	 * @param pec
	 *            the primary entity class to compare to
	 * @return EQUIVALENT if this class definition is equivalent to pec's class
	 *         definition, SUBCLASS if this class is a sub class of pec,
	 *         SUPERCLASS if this class is a super class of pec, NO_RELATION
	 *         ifthis class has no inheritance relation with pec (this class and
	 *         pec are in distinct branch of the taxonomy)
	 */
	public ClassificationRelationship computeClassifRelationWith(
			IPrimaryEntityClass pec);

	/**
	 * classifies this class in the hierarchy
	 */
	public void classify();

	public void classifyFrom(IPrimaryEntityClass pec);

	/**
	 * returns the tag indicating the status of this class during a
	 * classification process
	 * 
	 * @return the status of the class
	 * 
	 * @see com.similar2.matcher.ontology.model.ClassificationStatus
	 */
	public ClassificationStatus getClassificationStatus();

	/**
	 * sets the tag indicating the status of this class during a classification
	 * process.
	 * 
	 * @param status
	 *            the status to tag the class with
	 * 
	 * @see com.similar2.matcher.ontology.model.ClassificationStatus
	 */
	public void setClassificationStatus(ClassificationStatus status);

	/**
	 * removes all the attributes descriptions
	 */
	public void removeAllAttributeDescriptions();

	/**
	 * return the Genus (root of hierarchy this class belongs to) of this entity
	 * class
	 * 
	 * @return the Genus of this entity class (itself if this class is a Genus
	 *         class). null, if this class is not attached to a hierarchy.
	 */
	public IPrimaryEntityClass getGenus();

	/**
	 * Deletes the genus corresponding to this PrimaryEntityClass
	 * 
	 * @throws GenusRTException if there exists any property declaring the genus for domain
	 */
	public void deleteGenus() throws GenusRTException;
	
	/**
	 * classifies this entity class in its Genus hierarchy. If in the hierarchy
	 * there is an equivalent class the hierarchy is not modified and the
	 * reference of this equivalent class is returned. Otherwise, the hierarchy
	 * is modified and the class is placed in the good position.
	 * 
	 * @return equivalent class reference if it exists in the Genus hierarchy,
	 *         null otherwise
	 */
	public IPrimaryEntityClass classifyFromGenus();

	public IAttributeDescription findAttributeDescription(IEnumeratedProperty p);

	public IAttributeDescription findAttributeDescriptionById(String property);
	
	/**
	 * Test if this entity class has a attribute with a given value for a given
	 * property.
	 * 
	 * @param property
	 *            the property
	 * @param value
	 *            the value
	 *            
	 * @return true if the class has an attribute <property,value>, false else
	 */
	public boolean hasAttribute(IEnumeratedProperty property,
			IPrimaryEnumeratedClass value);

	/**
	 * returns a detailed description (definition) of this class
	 * 
	 * @return the detailed description of this entity class
	 */
	public IDetailedDescription getDetailedDescription();


}
