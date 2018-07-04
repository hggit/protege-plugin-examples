package com.similar2.matcher.ontology.model;

import java.io.Serializable;
import java.util.Collection;
import java.util.Set;

/**
 * description of an attribute defining an Aristetolian class
 * 
 * @author Philippe.Genoud (Philippe.Genoud@imag.fr)
 * 
 */
public interface IAttributeDescription extends Serializable,
		Comparable<IAttributeDescription> {

	/**
	 * returns the property associated with this attribute
	 * 
	 * @return the property
	 */
	public IEnumeratedProperty getProperty();

	/**
	 * returns the number of values for this attribute. (the attribute can be
	 * defined by a conjunction of values)
	 * 
	 * @return number of values
	 */
	public int getNbValues();

	// /**
	// * returns the descriptions of the values of this attribute sorted in
	// * alphabetical order according to the display names of the values.
	// *
	// * @return descriptions of the values of this attribute
	// */
	// public IPrimaryEnumeratedClass[] getValues();

	public Set<IPrimaryEnumeratedClass> getValues();

	/**
	 * adds a value to the set of values for this attribute
	 * 
	 * @param value
	 *            the value to add
	 */
	public void addValue(IPrimaryEnumeratedClass value);

	/**
	 * adds a collection of values to the set of values for this attribute
	 * 
	 * @param values
	 *            the collection of values to add
	 */
	public void addValues(Collection<IPrimaryEnumeratedClass> values);

	/**
	 * Compares this attributes description to an other assuming they apply to
	 * the same property.
	 * 
	 * @param attrDescr
	 *            the attribute description set to compare to
	 * @return the classification relation between first and second values sets.
	 * 
	 *         NO_RELATION: if the two values sets do not imply a inheritance
	 *         relation
	 * 
	 *         EQUIVALENT: if the two values sets are equivalents
	 * 
	 *         SUBCLASS: if the first values set defines a subclass of the class
	 *         described by the second values set.
	 * 
	 *         SUPERCLASS: if the second values set defines a superclass of the
	 *         class described by the second values set.
	 */
	public ClassificationRelationship compareValuesSet(
			IAttributeDescription attrDescr);

}
