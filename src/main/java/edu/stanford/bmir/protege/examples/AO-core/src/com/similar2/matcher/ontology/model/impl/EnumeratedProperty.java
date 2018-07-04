package com.similar2.matcher.ontology.model.impl;

import java.util.Collection;

import com.similar2.matcher.ontology.model.IAristotelianOntology;
import com.similar2.matcher.ontology.model.IEnumeratedProperty;
import com.similar2.matcher.ontology.model.IPrimaryEntityClass;
import com.similar2.matcher.ontology.model.IPrimaryEnumeratedClass;

/**
 * 
 * @author Anthony
 */
public class EnumeratedProperty extends AristotelianOntologyProperty implements IEnumeratedProperty{

	private static final long serialVersionUID = 1L;
	
	protected boolean _isFunctional = false;
	
	/**
	 * default constructor for serialization
	 */
	public EnumeratedProperty(){}
	
	public EnumeratedProperty(IAristotelianOntology ao,String nameSpace, String id){
		super(ao,nameSpace, id);
	}
	
	public EnumeratedProperty(IAristotelianOntology ao,String nameSpace, String id, IPrimaryEntityClass domain, IPrimaryEnumeratedClass range){
		super(ao,nameSpace, id,domain,range);
	}

	/* (non-Javadoc)
	 * @see com.similar2.matcher.ontology.model.IEnumeratedProperty#isFunctional()
	 */
	@Override
	public boolean isFunctional() {
		return _isFunctional;
	}

	/* (non-Javadoc)
	 * @see com.similar2.matcher.ontology.model.IEnumeratedProperty#setFunctional(boolean)
	 */
	@Override
	public void setFunctional(boolean functional) {
		_isFunctional = functional;
	}

	@Override
	public Collection<IPrimaryEntityClass> findPrimaryEntityClassesByProperty() {
		// TODO Auto-generated method stub
		return null;
	}
	

}