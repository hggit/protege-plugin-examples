/**
 * 
 */
package com.similar2.matcher.ontology.model.impl;

import com.similar2.matcher.ontology.model.IAristotelianOntologyClass;
import com.similar2.matcher.ontology.model.IPrimaryEntityClass;

/**
 * Description of a modification to apply to a taxonomy in order to classify an
 * entity.
 * 
 * An operation is defined by :
 * 
 * - a subject: the taxonomy class to be modified
 * 
 * - an operation type : the nature of the modification to apply to the subject
 * class
 * 
 * - an object : the taxonomy class implied in the operation
 * 
 * Operation types are :
 * 
 * - DETACH_CHILD : the object is removed from the direct subclasses of the
 * subject and conversely the subject is removed from the direct superclasses of
 * the object.
 * 
 * - INSERT_SUBCLASS : the object is added as new direct subclass of the subject
 * 
 * - INSERT_EQUIVALENT_CLASS : insert the subject class in the hierarchy as an
 * equivalent to object class. All the super classes of object class are copied
 * in subject superClasses. All the sub classes of object class are copied in
 * subject subClasses.
 * 
 * @author Philippe Genoud (Philippe.Genoud@imag.fr)
 */
public class TaxonomyModificationOperation {

	public enum OpType {
		DETACH_CHILD, INSERT_SUBCLASS, VERIFY_INSERT_SUBCLASS, INSERT_EQUIVALENT_CLASS,
	}

	private IPrimaryEntityClass subject;
	private IPrimaryEntityClass object;
	private OpType opType;

	public TaxonomyModificationOperation(IPrimaryEntityClass subject,
			IPrimaryEntityClass object, OpType opType) {
		this.subject = subject;
		this.object = object;
		this.opType = opType;
	}

	/**
	 * @return the subject
	 */
	public IPrimaryEntityClass getSubject() {
		return subject;
	}

	/**
	 * @return the object
	 */
	public IPrimaryEntityClass getObject() {
		return object;
	}

	/**
	 * @return the opType
	 */
	public OpType getOpType() {
		return opType;
	}

	/**
	 * performs this operation
	 */
	public void execute() {
		switch (opType) {
		case DETACH_CHILD:
			subject.detachSubclass(object);
			break;
		case INSERT_SUBCLASS:
			subject.addSubClass(object);
			break;
		case VERIFY_INSERT_SUBCLASS:
			if (!object.isSubClassOf(subject))
				subject.addSubClass(object);
			break;
		case INSERT_EQUIVALENT_CLASS:
			insertEquivalentClass();
			break;
		}
	}

	/**
	 * insert the subject class in the hierarchy as an equivalent to object
	 * class. All the super classes of object class are copied in subject
	 * superClasses. All the sub classes of object class are copied in subject
	 * subClasses.
	 */
	private void insertEquivalentClass() {
		for (IAristotelianOntologyClass parent : object.getSuperClasses()) {
			subject.addSuperClass(parent);
		}
		for (IAristotelianOntologyClass child : object.getSubClasses()) {
			subject.addSubClass(child);
		}
	}
}
