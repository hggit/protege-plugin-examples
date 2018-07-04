package com.similar2.matcher.ontology.model.impl;

import com.similar2.matcher.ontology.model.IAristotelianOntology;
import com.similar2.matcher.ontology.model.IAristotelianOntologyProperty;
import com.similar2.matcher.ontology.model.IAristotelianOntologyClass;

/**
 * @author Anthony Hombiat
 */
public abstract class AristotelianOntologyProperty extends NamedAndCommentedEntity implements IAristotelianOntologyProperty{

	private static final long serialVersionUID = 1L;

	/**
	 * the domain of this property
	 */
	private IAristotelianOntologyClass domain = null;

	/**
	 * the range of this property
	 */
	private IAristotelianOntologyClass range = null;

	/**
	 * default constructor for serialization
	 */
	protected AristotelianOntologyProperty(){}

	/**
	 * @param ao the ontology this named entity belongs to
	 * @param nameSpace
	 * @param id
	 */
	protected AristotelianOntologyProperty(IAristotelianOntology ao,String nameSpace, String id){
		super(ao,nameSpace,id);
	}

	/**
	 * @param ao the ontology this named entity belongs to
	 * @param nameSpace
	 * @param id
	 * @param domain
	 * @param range
	 */
	protected AristotelianOntologyProperty(IAristotelianOntology ao,String nameSpace, String id, IAristotelianOntologyClass domain, IAristotelianOntologyClass range){
		super(ao,nameSpace,id);
		this.domain = domain;
		this.range = range;
	}

	/* (non-Javadoc)
	 * @see com.similar2.matcher.ontology.model.IMatcherOntologyProperty#getDomain()
	 */
	@Override
	public IAristotelianOntologyClass getDomain() {
		return this.domain;
	}

	/* (non-Javadoc)
	 * @see com.similar2.matcher.ontology.model.IMatcherOntologyProperty#getRange()
	 */
	@Override
	public IAristotelianOntologyClass getRange() {
		return this.range;
	}

	/* (non-Javadoc)
	 * @see com.similar2.matcher.ontology.model.IMatcherOntologyProperty#setDomain(com.similar2.matcher.ontology.model.IEntityClass)
	 */
	@Override
	public void setDomain(IAristotelianOntologyClass domain) {
		this.domain = domain;
	}

	/* (non-Javadoc)
	 * @see com.similar2.matcher.ontology.model.IMatcherOntologyProperty#setRange(com.similar2.matcher.ontology.model.IEnumeratedClass)
	 */
	@Override
	public void setRange(IAristotelianOntologyClass range) {
		this.range = range;
	}

}