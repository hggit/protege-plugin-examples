package com.similar2.matcher.ontology.model.impl;

import java.util.Collections;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import com.similar2.matcher.ontology.model.IAristotelianOntology;
import com.similar2.matcher.ontology.model.IAristotelianOntologyClass;

/**
 * 
 * 
 * @author Philippe Genoud (Philippe.Genoud@imag.fr) - Anthony Hombiat
 */
public abstract class AristotelianOntologyClass extends NamedAndCommentedEntity
				implements IAristotelianOntologyClass {

	private static final long serialVersionUID = 1L;

	/**
	 * set of super classes
	 */
	protected SortedSet<IAristotelianOntologyClass> _superClasses = null;

	/**
	 * set of sub classes
	 */
	protected SortedSet<IAristotelianOntologyClass> _subClasses = null;

	/**
	 * default constructor for serialization
	 */
	protected AristotelianOntologyClass() {
	}

	/**
	 * Creates a new root class
	 * @param ao the ontology this named entity belongs to
	 * @param nameSpace
	 *            name space where the class is defined
	 * @param id
	 *            id of the class in the given name space
	 */
	protected AristotelianOntologyClass(IAristotelianOntology ao, String nameSpace, String id) {
		super(ao,nameSpace, id);
	}

	/**
	 * Creates a new class
	 * @param ao the ontology this named entity belongs to
	 * @param nameSpace
	 *            name space where the class is defined
	 * @param id
	 *            id of the class in the given name space
	 * @param superClass
	 *            super class of this class
	 */
	protected AristotelianOntologyClass(IAristotelianOntology ao,String nameSpace, String id,
										IAristotelianOntologyClass superClass) {
		super(ao,nameSpace, id);
		if (superClass != null) {
			addSuperClass(superClass);
			// superClass.addSubClass(this);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.similar2.matcher.ontology.model.IMatcherOntologyClass#getSuperClasses ()
	 */
	@Override
	public Set<IAristotelianOntologyClass> getSuperClasses() {
		if (_superClasses != null)
			return _superClasses;
		else
			return Collections.emptySet();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.similar2.matcher.ontology.model.IMatcherOntologyClass#getSubClasses()
	 */
	@Override
	public Set<IAristotelianOntologyClass> getSubClasses() {
		if (_subClasses != null)
			return _subClasses;
		else
			return Collections.emptySet();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.similar2.matcher.ontology.model.IMatcherOntologyClass#addSubClass
	 * (com.similar2.matcher.ontology.model.IMatcherOntologyClass)
	 */
	@Override
	public void addSubClass(IAristotelianOntologyClass subClass) {
		atomicAddSubClass(subClass);
		((AristotelianOntologyClass) subClass).atomicAddSuperClass(this);
	}

	/**
	 * add a subclass to this class. This operation is "atomic", that means only the link between
	 * this class and its subclass is created. the superclass link between the subclass and this
	 * class is not created (unlike addsSubClass).
	 * 
	 * @param superClass
	 *            the super class to add.
	 */
	private void atomicAddSubClass(IAristotelianOntologyClass subClass) {
		if (_subClasses == null) {
			_subClasses = new TreeSet<IAristotelianOntologyClass>();
		}
		_subClasses.add(subClass);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.similar2.matcher.ontology.model.IMatcherOntologyClass#addSuperClass
	 * (com.similar2.matcher.ontology.model.IMatcherOntologyClass)
	 */
	@Override
	public void addSuperClass(IAristotelianOntologyClass superClass) {
		this.atomicAddSuperClass(superClass);
		((AristotelianOntologyClass) superClass).atomicAddSubClass(this);
	}

	/**
	 * add a superclass to this class. This operation is "atomic", that means only the link between
	 * this class and its superclass is created. the subclass link between the superclass and this
	 * class is not created (unlike addsupperClass).
	 * 
	 * @param superClass
	 *            the super class to add.
	 */
	private void atomicAddSuperClass(IAristotelianOntologyClass superClass) {
		if (_superClasses == null) {
			_superClasses = new TreeSet<IAristotelianOntologyClass>();
		}
		_superClasses.add(superClass);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.similar2.matcher.ontology.model.IMatcherOntologyClass#isRoot()
	 */
	@Override
	public boolean isRoot() {
		return _superClasses == null || _superClasses.size() == 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.similar2.matcher.ontology.model.IMatcherOntologyClass#isLeaf()
	 */
	@Override
	public boolean isLeaf() {
		return _subClasses == null || _subClasses.size() == 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.similar2.matcher.ontology.model.IAristotelianOntologyClass#isSubClassOf
	 * (com.similar2.matcher.ontology.model.IAristotelianOntology)
	 */
	@Override
	public boolean isSubClassOf(IAristotelianOntologyClass clazz) {
		if (!clazz.isLeaf()) {
			// test if this is a direct subclass of clazz
			if (clazz.getSubClasses().contains(this))
				return true;
			// recursively test if this is a subclass of clazz subclasses

			for (IAristotelianOntologyClass cl : clazz.getSubClasses()) {
				if (isSubClassOf(cl))
					return true;
			}
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.similar2.matcher.ontology.model.IAristotelianOntologyClass#isSuperClassOf
	 * (com.similar2.matcher.ontology.model.IAristotelianOntology)
	 */
	@Override
	public boolean isSuperClassOf(IAristotelianOntologyClass clazz) {
		return clazz.isSubClassOf(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.similar2.matcher.ontology.model.IAristotelianOntologyClass#detachSubclass
	 * (com.similar2.matcher.ontology.model.IAristotelianOntologyClass)
	 */
	@Override
	public void detachSubclass(IAristotelianOntologyClass child) {
		_subClasses.remove(child);
		((AristotelianOntologyClass) child)._superClasses.remove(this);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.similar2.matcher.ontology.model.IAristotelianOntologyClass#detach()
	 */
	@Override
	public void detach() {
		// detach this from its super classes
		for (IAristotelianOntologyClass superClass : this.getSuperClasses()) {
			((AristotelianOntologyClass) superClass)._subClasses.remove(this);
		}
		// detach this from its subclasses
		for (IAristotelianOntologyClass subClass : this.getSubClasses()) {
			((AristotelianOntologyClass) subClass)._superClasses.remove(this);

		}
		// if necessary re attach this subclasses to this former superclasses
		for (IAristotelianOntologyClass subClass : this.getSubClasses()) {
			for (IAristotelianOntologyClass superClass : this.getSuperClasses()) {
				if (!subClass.isSubClassOf(superClass)) {
					superClass.addSubClass(subClass);
				}
			}
		}

		this._subClasses = null;
		this._superClasses = null;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.similar2.matcher.ontology.model.IAristotelianOntologyClass#remove()
	 */
	@Override
	public void remove() {
		super.remove();
		detach();
		cleanDataStructures();
	}

	/**
	 * cleans the data structures used by this class. this method is called when the class is
	 * removed.
	 */
	protected abstract void cleanDataStructures();

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.similar2.matcher.ontology.model.IAristotelianOntologyClass#isDetached()
	 */
	@Override
	public boolean isDetached() {
		return this._subClasses == null && this._superClasses == null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.similar2.matcher.ontology.model.IAristotelianOntologyClass#findRoot()
	 */
	@Override
	public IAristotelianOntologyClass findRoot() {
		if (isRoot())
			return this;

		return this.getSuperClasses().iterator().next().findRoot();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.similar2.matcher.ontology.model.IAristotelianOntologyClass#createSubClassId()
	 */
	@Override
	public String createSubClassId() {
		int number = 0;
		String prefix = this.getId() + "_subclass_";
		for (IAristotelianOntologyClass cl : getSubClasses()) {
			if (cl.getId().startsWith(prefix)) {
				int subClassNumber = Integer.parseInt(cl.getId().substring(prefix.length()));
				if (subClassNumber > number)
					number = subClassNumber;
			}
		}
		return prefix + (number + 1);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.similar2.matcher.ontology.model.IAristotelianOntologyClass#hasParent()
	 */
	@Override
	public boolean hasParent() {
		return getSuperClasses().size() > 0;
	}
}
