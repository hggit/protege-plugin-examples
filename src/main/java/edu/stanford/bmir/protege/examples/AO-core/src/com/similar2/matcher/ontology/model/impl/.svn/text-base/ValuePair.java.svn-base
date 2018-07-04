/**
 * 
 */
package com.similar2.matcher.ontology.model.impl;

import java.util.ArrayList;
import java.util.List;

import com.similar2.matcher.ontology.model.IEnumeratedProperty;
import com.similar2.matcher.ontology.model.IPrimaryEntityClass;
import com.similar2.matcher.ontology.model.IPrimaryEnumeratedClass;
import com.similar2.matcher.ontology.model.IValuePair;

/**
 * @author Philippe Genoud (Philippe.Genoud@imag.fr)
 *
 */
public class ValuePair implements IValuePair{
	
	private IEnumeratedProperty property;
	
	private IPrimaryEnumeratedClass value;
	
	/**
	 * parents having this value for that property
	 */
	private List<IPrimaryEntityClass> parents;
	
	/**
	 * 
	 * @param property
	 * @param value
	 */
	public ValuePair(IEnumeratedProperty property, IPrimaryEnumeratedClass value) {
		this.property = property;
		this.value = value;
	}

	/**
	 * @return the property
	 */
	public IEnumeratedProperty getProperty() {
		return property;
	}

	/**
	 * @return the value
	 */
	public IPrimaryEnumeratedClass getValue() {
		return value;
	}

	/**
	 * @return the parents
	 */
	public List<IPrimaryEntityClass> getParents() {
		return parents;
	}
	
	/**
	 * 
	 * @param parent
	 */
	public void addParent(IPrimaryEntityClass parent) {
		if (parents == null)
			parents = new ArrayList<IPrimaryEntityClass>();
		parents.add(parent);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "<" + property.getId() + "," + value.getId() + ">";
	}
}


