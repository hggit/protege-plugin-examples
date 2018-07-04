package com.similar2.matcher.ontology.model.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

import javax.naming.OperationNotSupportedException;

import com.similar2.matcher.ontology.model.ClassificationRelationship;
import com.similar2.matcher.ontology.model.IAristotelianOntology;
import com.similar2.matcher.ontology.model.IAristotelianOntologyClass;
import com.similar2.matcher.ontology.model.IEnumeratedProperty;
import com.similar2.matcher.ontology.model.IPrimaryEntityClass;
import com.similar2.matcher.ontology.model.IPrimaryEnumeratedClass;
import com.similar2.matcher.ontology.model.exceptions.PrimaryEnumeratedClassRTException;

public class PrimaryEnumeratedClass extends AristotelianOntologyClass implements
		IPrimaryEnumeratedClass {

	private static final long serialVersionUID = 1L;

	/**
	 * default constructor for serialization
	 */
	public PrimaryEnumeratedClass() {
	}

	public PrimaryEnumeratedClass(IAristotelianOntology ao,String nameSpace, String id) {
		super(ao,nameSpace, id);
	}

	public PrimaryEnumeratedClass(IAristotelianOntology ao,String nameSpace, String id,
			IPrimaryEnumeratedClass superClass) {
		super(ao,nameSpace, id, superClass);
	}

	@Override
	public ClassificationRelationship findClassificationRelationshipAmong(
			Collection<IPrimaryEnumeratedClass> pecs) {
		for (IPrimaryEnumeratedClass pec : pecs) {
			if (this.equals(pec))
				return ClassificationRelationship.EQUIVALENT;
			else if (this.isSubClassOf(pec)) {
				return ClassificationRelationship.SUBCLASS;
			} else if (this.isSuperClassOf(pec)) {
				return ClassificationRelationship.SUPERCLASS;
			}
		}
		return ClassificationRelationship.NO_RELATION;
	}

	/* (non-Javadoc)
	 * @see com.similar2.matcher.ontology.model.impl.AristotelianOntologyClass#cleanDataStructures()
	 */
	@Override
	protected void cleanDataStructures() {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public void deleteSubHierarchy(String fqPrimaryEnumeratedClasses) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Collection<IPrimaryEntityClass> findPrimaryEntityClassesWithThisAttributeValue() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<IPrimaryEntityClass> findPrimaryEntityClassesWithAnyInheritedAttributeValue() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<IEnumeratedProperty> findEnumeratedPropertiesByRange() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteValueHierarchy() throws PrimaryEnumeratedClassRTException {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see com.similar2.matcher.ontology.model.IAristotelianOntologyClass#detach()
	 */
	@Override
	public void detach() {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see com.similar2.matcher.ontology.model.IPrimaryEnumeratedClass#removeSubTree(com.similar2.matcher.ontology.model.IPrimaryEnumeratedClass)
	 */
	@Override
	public void removeSubTree(IPrimaryEnumeratedClass cl) {
		this.detachSubclass(cl);
		Collection<IAristotelianOntologyClass> subclasses = new ArrayList<IAristotelianOntologyClass>(cl.getSubClasses());
		for (IAristotelianOntologyClass clSuclass : subclasses) {
			IPrimaryEnumeratedClass pec = (IPrimaryEnumeratedClass) clSuclass;
			cl.removeSubTree(pec);
		}
		if (! cl.hasParent()) {
			cl.removeFromAODataStructures();
		}
	}

	/* (non-Javadoc)
	 * @see com.similar2.matcher.ontology.model.IPrimaryEnumeratedClass#removeFromAODataStructures()
	 */
	@Override
	public void removeFromAODataStructures() {
		((AristotelianOntology) getAO()).erasePrimaryEnumeratedClass(this);
		// TODO we use this ugly casting because from the moment erasePrimerasePrimaryEnumeratedClass
		// is a protected method
		
	}
}