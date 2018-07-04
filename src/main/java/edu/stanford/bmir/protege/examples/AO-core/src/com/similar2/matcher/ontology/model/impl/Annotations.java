/**
 * implementation of com.similar2.ace.client.model.IAnnotations.
 * For the moment it is a quite simple class. But it may evolve toward
 * more complexity (for example to avoid multiple annotation for the same
 * language)
 */
package com.similar2.matcher.ontology.model.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.similar2.matcher.ontology.model.IAnnotations;
import com.similar2.matcher.ontology.model.ILocalizedString;

/**
 * @author Philippe.Genoud (Philippe.Genoud@imag.fr)
 */
public class Annotations implements IAnnotations {

	private static final long serialVersionUID = 1L;

	private List<ILocalizedString> _annotations;
	
	/**
	 * empty iterator for empty annotations list
	 */
	private static final Iterator<ILocalizedString> emptyIterator = new Iterator<ILocalizedString>() {
		@Override
		public boolean hasNext() {
			return false;
		}

		@Override
		public ILocalizedString next() {
			throw new UnsupportedOperationException("unsuported operation on an empty iterator");
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException("unsuported operation on an empty iterator");
		}
		
	};

	/**
	 * default constructor for serialization
	 */
	public Annotations() {}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.similar2.matcher.ontology.model.IAnnotations#get(java.lang.String)
	 */
	@Override
	public String getAnnotation(String language) {
		if (_annotations != null) {
			for (ILocalizedString ls : _annotations) {
				if (ls.getLanguage().equals(language))
					return ls.getString();
			}
		}
		return null;
	}

	@Override
	public void addAnnotation(String language, String annotation) {
		if (_annotations == null)
			_annotations = new ArrayList<ILocalizedString>();
		
		ILocalizedString s = null;
		for(ILocalizedString ls : _annotations) {
			if(ls.getLanguage().toString().equals(language.toString())){
				s = ls;
				break;
			}
		}
	    if (s != null) {
			_annotations.remove(s);
	    }
		_annotations.add(new LocalizedString(language, annotation));
	}
	
	@Override
	public void setAnnotations(List<ILocalizedString> annotations) {
		if (_annotations == null)
			_annotations = new ArrayList<ILocalizedString>();
		else
			_annotations.clear();
		for (ILocalizedString ls : annotations)
		     _annotations.add(ls);
	}

	@Override
	public Iterator<ILocalizedString> iterator() {
		if (_annotations != null)
			return _annotations.iterator();
		
		// if there are no annotations return an empty iterator
		return emptyIterator;
	}

}