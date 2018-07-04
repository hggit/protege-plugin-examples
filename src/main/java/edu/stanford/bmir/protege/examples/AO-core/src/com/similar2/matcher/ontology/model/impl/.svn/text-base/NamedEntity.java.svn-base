package com.similar2.matcher.ontology.model.impl;

import com.similar2.matcher.ontology.model.IAnnotations;
import com.similar2.matcher.ontology.model.IAristotelianOntology;
import com.similar2.matcher.ontology.model.ILocalizedString;
import com.similar2.matcher.ontology.model.INamedEntity;
import com.similar2.matcher.ontology.model.LoggerManager;
import com.similar2.matcher.ontology.search.Searcher;

/**
 * @author Philippe.Genoud@imag.fr
 */
public abstract class NamedEntity implements INamedEntity, Comparable<INamedEntity> {

	private static final long serialVersionUID = 1L;
	protected String _nameSpace;
	protected String _id;
	
	/**
	 * the ontology this named entity belongs to
	 */
	protected transient IAristotelianOntology _ao;
	
	/**
	 * the labels associated to this named entity
	 */
	private IAnnotations _labels;

	/**
	 * default constructor for serialization
	 */
	public NamedEntity() {}
	
	/**
	 * @param ao the ontology this named entity belongs to
	 * @param nameSpace name space where id is defined
	 * @param id  id of the named entity
	 */
	public NamedEntity(IAristotelianOntology ao, String nameSpace, String id) {
		_ao = ao;
		_id = id;
		_nameSpace = nameSpace;
		
		//Searcher.getInstance().add(new LocalizedString("default", id), this, SearchDataType.);
	}

	/*
	 * (non-Javadoc)
	 * @see com.similar2.matcher.ontology.model.INamedEntity#getId()
	 */
	@Override
	public String getId() {
		return _id;
	}

	/*
	 * (non-Javadoc)
	 * @see com.similar2.matcher.ontology.model.INamedEntity#getNameSpace()
	 */
	@Override
	public String getNameSpace() {
		if (_nameSpace == null) return "";
		return _nameSpace;
	}
	
	private String getNormalizedNameSpace() {
		if (_nameSpace == null || _nameSpace.length() == 0)
			return "#";
		if (_nameSpace.endsWith("#"))
			return _nameSpace;
		return _nameSpace + "#";
	}

	/*
	 * (non-Javadoc)
	 * @see com.similar2.matcher.ontology.model.INamedEntity#getFQName()
	 */
	@Override
	public final String getFQName() {
		return getNormalizedNameSpace() + getId();
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.similar2.matcher.ontology.model.INamedEntity#getLabels()
	 */
	@Override
	public IAnnotations getLabels() {
		IAnnotations res = _labels;
		
		if (res == null) {
			res = new Annotations();
			res.addAnnotation("default", getLabel("default"));
		}
		
		return res;
	}

	/*
	 * (non-Javadoc)
	 * @see com.similar2.matcher.ontology.model.INamedEntity#getLabel()
	 */
	@Override
	public String getLabel(String language) {
		String res = null;
		if (_labels != null) {
			res =  _labels.getAnnotation(language);
		}
		if (res == null)
			return this.getId();
		else
			return res;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.similar2.matcher.ontology.model.INamedEntity#addLabel(java.lang.String, java.lang.String)
	 */
	@Override
	public void addLabel(String language, String label) {
		if (_labels == null) {
			_labels = new Annotations();
		}
		_labels.addAnnotation(language, label);

		Searcher.getInstance().add(new LocalizedString(language, label), this);
		
		LoggerManager.LOGGER.info("\nadd label \"{}\" for the \"{}\" language",label,language);
	}

	/*
	 * (non-Javadoc)
	 * @see com.similar2.matcher.ontology.model.INamedEntity#setLabels(com.similar2.matcher.ontology.model.IAnnotations)
	 */
	@Override
	public void setLabels(IAnnotations labels) {
		_labels = labels;
	
		for (ILocalizedString localizedString : labels) {
			Searcher.getInstance().add(localizedString, this);
		}
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return getFQName();
	}
	
	@Override
	public int compareTo(INamedEntity entity) {	
		return this.getFQName().compareTo(entity.getFQName());
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 * 
	 * two named entities are equals if they have the same id and the same
	 * name space
	 */
	@Override
	public boolean equals(Object obj) {
		if (this ==  obj) 
			return true;
		
		if (! (obj instanceof INamedEntity))
			return false;
		
		INamedEntity param = (INamedEntity) obj;
		return this._id.equals(param.getId()) && this._nameSpace.equals(param.getNameSpace());
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 * 
	 * based on id and name space to conform to equals
	 */
	@Override
	public int hashCode() {
		return this.getFQName().hashCode();
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.similar2.matcher.ontology.model.INamedEntity#setId(String)
	 */
	@Override
	public void setId(String id){
		this._id = id;

		Searcher.getInstance().add(new LocalizedString("default", id), this);
	}
	
	/* (non-Javadoc)
	 * @see com.similar2.matcher.ontology.model.INamedEntity#remove()
	 */
	@Override
	public void remove() {
		// remove this entity id and its labels from the search store
		Searcher.getInstance().remove(new LocalizedString("default", _id), this);
		//TODO labels
		//TODO comments dans commented entity
	}
	
	/* (non-Javadoc)
	 * @see com.similar2.matcher.ontology.model.INamedEntity#getAO()
	 */
	@Override
	public IAristotelianOntology getAO() {
		return _ao;
	}
}