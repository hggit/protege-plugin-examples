package com.similar2.matcher.ontology.model.impl;

import com.similar2.matcher.ontology.model.IAristotelianOntology;
import com.similar2.matcher.ontology.model.IAristotelianOntologyClass;
import com.similar2.matcher.ontology.model.IReferringProperty;

/**
 * 
 * @author Anthony
 */
public class ReferringProperty extends AristotelianOntologyProperty implements IReferringProperty{

	private static final long serialVersionUID = 1L;

	/**
	 * Default constructor for serialization
	 */
	public ReferringProperty(){}
	
	public ReferringProperty(IAristotelianOntology ao,String nameSpace, String id){
		super(ao,nameSpace,id);
	}
	
	public ReferringProperty(IAristotelianOntology ao,String nameSpace, String id, IAristotelianOntologyClass domain, IAristotelianOntologyClass range){
		super(ao,nameSpace, id, domain,range);
	}
	
}
