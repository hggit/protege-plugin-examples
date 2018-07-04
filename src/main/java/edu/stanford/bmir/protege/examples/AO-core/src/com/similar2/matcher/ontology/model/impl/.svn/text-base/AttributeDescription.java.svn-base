package com.similar2.matcher.ontology.model.impl;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import com.similar2.matcher.ontology.model.ClassificationRelationship;
import com.similar2.matcher.ontology.model.IAttributeDescription;
import com.similar2.matcher.ontology.model.IEnumeratedProperty;
import com.similar2.matcher.ontology.model.IPrimaryEnumeratedClass;
import static com.similar2.matcher.ontology.model.ClassificationRelationship.*;

/**
 * implementation class for IAttributeDescription
 * 
 * @author Philippe.Genoud (Philippe.Genoud@imag.fr)
 */
public class AttributeDescription implements IAttributeDescription {

	private static final long serialVersionUID = 1L;

	private Set<IPrimaryEnumeratedClass> _values;
	private IEnumeratedProperty _property;

	/**
	 * default constructor for serialization
	 */
	public AttributeDescription() {
	}

	public AttributeDescription(IEnumeratedProperty property,
			Collection<IPrimaryEnumeratedClass> values) {
		_property = property;
		_values = new HashSet<IPrimaryEnumeratedClass>(values);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.similar2.ace.client.model.IAttributeDescription#getNbValues()
	 */
	@Override
	public int getNbValues() {
		return _values.size();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.similar2.ace.client.model.IAttributeDescription#getProperty()
	 */
	@Override
	public IEnumeratedProperty getProperty() {
		return _property;
	}

	public Set<IPrimaryEnumeratedClass> getValues() {
		// IPrimaryEnumeratedClass[] res = _values.toArray(new
		// IPrimaryEnumeratedClass[_values.size()]);
		// // to ensure the values are ordered by classes display names
		// Arrays.sort(res);
		return _values;
	}

	// in order to sort attributes descriptions according property names
	@Override
	public int compareTo(IAttributeDescription arg0) {
		return _property.compareTo(arg0.getProperty());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.similar2.matcher.ontology.model.IAttributeDescription#addValue(com
	 * .similar2.matcher.ontology.model.IPrimaryEnumeratedClass)
	 */
	@Override
	public void addValue(IPrimaryEnumeratedClass value) {
		boolean added = _values.add(value);
		assert added; // you can't add a value that is all ready in the set of
						// values
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.similar2.matcher.ontology.model.IAttributeDescription#addValues(java
	 * .util.List)
	 */
	@Override
	public void addValues(Collection<IPrimaryEnumeratedClass> values) {
		_values.addAll(values);
	}

	@Override
	public ClassificationRelationship compareValuesSet(
			IAttributeDescription attrDescr) {
		int card1 = this.getNbValues();
		int card2 = attrDescr.getNbValues();
		if (card1 < card2) {
			return compareValuesSets1(_values, attrDescr.getValues());
		} else if (card1 == card2) {
			return compareValuesSets2(_values, attrDescr.getValues());
		} else {
			ClassificationRelationship res = compareValuesSets1(
					attrDescr.getValues(), _values);
			if (res == SUPERCLASS)
				return SUBCLASS;
			return res;
		}
	}

	/**
	 * Compares two values sets of the same size (same number of values)
	 * 
	 * @param values1
	 *            the first values set to compare
	 * @param values2
	 *            the second values set to compare
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
	private ClassificationRelationship compareValuesSets2(
			Collection<IPrimaryEnumeratedClass> values1,
			Collection<IPrimaryEnumeratedClass> values2) {
		ClassificationRelationship classifRelation = EQUIVALENT;
		for (IPrimaryEnumeratedClass v1 : values1) {
			switch (v1.findClassificationRelationshipAmong(values2)) {
			case NO_RELATION:
				// there is no class in values2 having an inheritence
				// relation ship with v1
				return NO_RELATION;
			case EQUIVALENT:
				// there is a class in values2 equivalent to v1
				break;
			case SUBCLASS:
				// there is a class C in values2 super classing v1
				// i.e. v1 is a subclass of C
				if (classifRelation == SUPERCLASS)
					return NO_RELATION;
				classifRelation = SUBCLASS;
				break;
			case SUPERCLASS:
				// there is a class C in values2 sub classing v1
				// i.e. v1 is a superclass of C
				if (classifRelation == SUBCLASS)
					return NO_RELATION;
				classifRelation = SUPERCLASS;
			}
		} // end for
		return classifRelation;
	}

	/**
	 * Compares two values sets of different sizes, assuming the size of the
	 * first one is smaller than the size of the second one. (values1.size() <
	 * values2.size()
	 * 
	 * @param values1
	 *            the first values set to compare
	 * @param values2
	 *            the second values set to compare
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
	private ClassificationRelationship compareValuesSets1(
			Collection<IPrimaryEnumeratedClass> values1,
			Collection<IPrimaryEnumeratedClass> values2) {
		for (IPrimaryEnumeratedClass v1 : values1) {
			switch (v1.findClassificationRelationshipAmong(values2)) {
			case NO_RELATION:
				// there is no class in values2 having an inheritance
				// relationship with v1
				return NO_RELATION;

			case SUBCLASS:
				// there is a class C in values2 super classing v1
				// i.e. v1 is a subclass of C
				return NO_RELATION;
			case EQUIVALENT:
				// there is a class in values2 equivalent to v1
			case SUPERCLASS:
				// there is a class C in values2 sub classing v1
				// i.e. v1 is a superclass of C
				break;
			}
		} // end for
		return SUPERCLASS;
	}
}