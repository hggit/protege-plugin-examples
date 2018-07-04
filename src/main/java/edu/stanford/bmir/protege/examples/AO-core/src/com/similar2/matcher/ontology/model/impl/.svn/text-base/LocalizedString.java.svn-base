package com.similar2.matcher.ontology.model.impl;

import com.similar2.matcher.ontology.model.ILocalizedString;


/**
 * @author Philippe.Genoud (Philippe.Genoud@imag.fr)
 */
public class LocalizedString implements ILocalizedString {

	private static final long serialVersionUID = 1L;
	private String _language;
	private String _string;
	
	/**
	 * default constructor for serialization
	 */
	public LocalizedString() {}

	/**
	 * @param language
	 * @param s
	 */
	public LocalizedString(String language, String s) {
		_language = language;
		_string = s;
	}
	
	/* (non-Javadoc)
	 * @see com.similar2.ace.client.model.ILocalizedString#getLocalization()
	 */
	@Override
	public String getLanguage() {
		return _language;
	}

	/* (non-Javadoc)
	 * @see com.similar2.ace.client.model.ILocalizedString#getString()
	 */
	@Override
	public String getString() {
		return _string;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (! (obj instanceof ILocalizedString))
			return false;
		ILocalizedString ls = (ILocalizedString) obj;
		return _language.equals(ls.getLanguage()) && _string.equals(ls.getString());
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return _language.hashCode() + _string.hashCode();
	}
}