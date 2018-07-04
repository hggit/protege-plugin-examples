package com.similar2.matcher.ontology.model.impl;

import com.similar2.matcher.ontology.model.IAristotelianOntology;
import com.similar2.matcher.ontology.model.IAristotelianOntologyClass;

/**
 * 
 * @author Anthony
 */
public class DatatypeProperty extends AristotelianOntologyProperty{

	private static final long serialVersionUID = 1L;

	/**
	 * Default constructor for serialization
	 */
	public DatatypeProperty(){}
	
	public DatatypeProperty(IAristotelianOntology ao,String nameSpace, String id){
		super(ao,nameSpace,id);
	}
	
	public DatatypeProperty(IAristotelianOntology ao,String nameSpace, String id, IAristotelianOntologyClass domain, IAristotelianOntologyClass range){
		super(ao,nameSpace, id, domain,range);
	}
}
