package com.similar2.matcher.ontology.model.impl;

import com.similar2.matcher.ontology.model.IAristotelianOntology;
import com.similar2.matcher.ontology.model.IIsPartOfEntityProperty;
import com.similar2.matcher.ontology.model.IAristotelianOntologyClass;

/**
 * 
 * @author Anthony
 */
public class IsPartOfEntityProperty extends AristotelianOntologyProperty implements IIsPartOfEntityProperty{

	private static final long serialVersionUID = 1L;

	/**
	 * Default constructor for serialization
	 */
	public IsPartOfEntityProperty(){}
	
	public IsPartOfEntityProperty(IAristotelianOntology ao,String nameSpace, String id){
		super(ao,nameSpace,id);
	}
	
	public IsPartOfEntityProperty(IAristotelianOntology ao,String nameSpace, String id, IAristotelianOntologyClass domain, IAristotelianOntologyClass range){
		super(ao,nameSpace, id, domain,range);
	}
	
}
