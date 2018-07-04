/**
 * 
 */
package com.similar2.matcher.ontology.model.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.similar2.matcher.ontology.model.IAristotelianOntologyClass;
import com.similar2.matcher.ontology.model.IAttributeDescription;
import com.similar2.matcher.ontology.model.IDetailedDescription;
import com.similar2.matcher.ontology.model.IEnumeratedProperty;
import com.similar2.matcher.ontology.model.IPrimaryEntityClass;
import com.similar2.matcher.ontology.model.IPrimaryEnumeratedClass;
import com.similar2.matcher.ontology.model.IValuePair;

/**
 * @author Philippe Genoud (Philippe.Genoud@imag.fr)
 */
public class DetailedDescription implements IDetailedDescription {
	
	private IPrimaryEntityClass entity;

	private List<IValuePair> properValues = new ArrayList<IValuePair>();

	private List<IValuePair> inheritedValues = new ArrayList<IValuePair>();

	public DetailedDescription(IPrimaryEntityClass entity) {
		
		this.entity = entity;

		IAristotelianOntologyClass[] parents = entity.getSuperClasses().toArray(
				new IAristotelianOntologyClass[entity.getSuperClasses().size()]);
		// sort alphabetically the parents
		Arrays.sort(parents);
		for (IAttributeDescription attr : entity.getAttributesDescriptions()) {
			IEnumeratedProperty property = attr.getProperty();
			for (IPrimaryEnumeratedClass value : attr.getValues()) {
				// create a new ValuePair <property, value>
				IValuePair valuePair = new ValuePair(property, value);
				boolean inherited = false;
				for (int i = 0; i < parents.length; i++) {
					IPrimaryEntityClass parent = (IPrimaryEntityClass) parents[i];
					if (parent.hasAttribute(property,value)) {
						inherited = true;
						// add this ValuePair to the inherited ones
						inheritedValues.add(valuePair);
						// mark origin has parent an put value pair in parent list
						valuePair.addParent(parent);
						// look if other parents have this value
						for (int j = i+1; j < parents.length; j++) {
							IPrimaryEntityClass otherParent = (IPrimaryEntityClass) parents[j];
							if (otherParent.hasAttribute(property,value)) {
								// add an other parent origin to this ValuePair
								valuePair.addParent(otherParent);
							}
						}
			            // this value has been treated go to next value
						break;
					}
				}
				if (! inherited) {
					// add the value to the proper values list
					properValues.add(valuePair);
				}
			}
		}
	}

	/**
	 * @return the entity
	 */
	public IPrimaryEntityClass getEntity() {
		return entity;
	}

	/**
	 * @return the properValues
	 */
	public List<IValuePair> getProperValues() {
		return properValues;
	}

	/**
	 * @return the inheritedValues
	 */
	public List<IValuePair> getInheritedValues() {
		return inheritedValues;
	}

}
