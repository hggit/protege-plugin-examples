package com.similar2.matcher.ontology.model.impl;

import com.similar2.matcher.ontology.model.IAnnotations;
import com.similar2.matcher.ontology.model.IAristotelianOntology;
import com.similar2.matcher.ontology.model.ILocalizedString;
import com.similar2.matcher.ontology.model.INamedAndCommentedEntity;
import com.similar2.matcher.ontology.search.SearchDataType;
import com.similar2.matcher.ontology.search.Searcher;

/**
 * @author Philippe.Genoud (Philippe.Genoud@imag.fr)
 */
public abstract class NamedAndCommentedEntity extends NamedEntity implements INamedAndCommentedEntity{

	private static final long serialVersionUID = 1L;
	
	/**
	 * the comments associated to this named entity
	 */
	private IAnnotations _comments;

	/**
	 * default constructor for serialization
	 */
	public NamedAndCommentedEntity() {}

	/**
	 * @param ao the ontology this named entity belongs to
	 * @param nameSpace
	 * @param id
	 */
	public NamedAndCommentedEntity(IAristotelianOntology ao,String nameSpace, String id) {
		super(ao,nameSpace, id);
	}
	
	public String getComment(String language) {
		if (_comments != null) {
			return _comments.getAnnotation(language);
		}
		return null;
	}
	
	public void addComment(String language, String label) {
		if (_comments == null) {
			_comments = new Annotations();
		}
		_comments.addAnnotation(language, label);
		
		Searcher.getInstance().add(new LocalizedString(language, label), this, SearchDataType.DESCRIPTION);
	}

	public void addComments(IAnnotations comments) {
		for (ILocalizedString localizedString : comments) {
			this.addComment(localizedString.getLanguage(), localizedString.getString());
		}
	}

	public void setComments(IAnnotations comments) {
		_comments = comments;	

		for (ILocalizedString localizedString : comments) {
			Searcher.getInstance().add(localizedString, this, SearchDataType.DESCRIPTION);
		}
	}

}