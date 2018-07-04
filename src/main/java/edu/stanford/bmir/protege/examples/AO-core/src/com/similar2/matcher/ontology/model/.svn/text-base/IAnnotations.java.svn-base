package com.similar2.matcher.ontology.model;

import java.io.Serializable;
import java.util.List;

/**
 * Annotations is a set of String, each String defined for a given language
 * 
 * @author Philippe Genoud (Philippe.Genoud@imag.fr)
 */
public interface IAnnotations extends Serializable, Iterable<ILocalizedString> {
	
	/**
	 * returns the value of the annotation for a given language
	 * @param language the language code ("fr", , "en", ...)
	 * @return value of annotation, null if there is no annotation for
	 * this language
	 */
	public String getAnnotation(String language);
	
	/**
	 * adds an annotation to the list of annotations.
	 * if an annotation has already been set in the language given in parameter,
	 * the previous content of the annotation is replaced by the new one.
	 * @param language
	 * @param content
	 */
	public void addAnnotation(String language, String content);
	
	/**
	 * initializes the annotations
	 * @param annotations list of ILocalizedString defining the annotations
	 */
	public void setAnnotations(List<ILocalizedString> annotations);

}