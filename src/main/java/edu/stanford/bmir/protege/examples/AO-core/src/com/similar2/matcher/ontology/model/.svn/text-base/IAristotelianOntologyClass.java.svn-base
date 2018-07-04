package com.similar2.matcher.ontology.model;

import java.util.Set;

/**
 * @author Anthony Hombiat
 */
public interface IAristotelianOntologyClass extends INamedAndCommentedEntity {

	/**
	 * get the super classes for this node
	 * 
	 * @return super classes
	 */
	public Set<IAristotelianOntologyClass> getSuperClasses();

	/**
	 * get the subclasses for this node
	 * 
	 * @return subclasses
	 */
	public Set<IAristotelianOntologyClass> getSubClasses();

	/**
	 * add a subClass to this node. this node is automatically added as a super
	 * class of sub class
	 * 
	 * @param subClass
	 *            to add
	 */
	public void addSubClass(IAristotelianOntologyClass subClass);

	/**
	 * add a superClass to this node this node is automatically added as a sub
	 * class of super class
	 * 
	 * @param superClass
	 *            to add
	 */
	public void addSuperClass(IAristotelianOntologyClass superClass);

	/**
	 * tells whether or not this node is a root class
	 * 
	 * @return true if this node is root, false if not
	 */
	public boolean isRoot();

	/**
	 * tells whether or not this node is a leaf class
	 * 
	 * @return true if this node is leaf, false if not
	 */
	public boolean isLeaf();

	/**
	 * tests if this class is a subclass (direct or not) of a class
	 * 
	 * @param clazz
	 *            the class to compare to
	 * @return true if this class is a subclass of clazz false else
	 */
	public boolean isSubClassOf(IAristotelianOntologyClass clazz);

	/**
	 * test if this class is a superclass (direct or not) of an other
	 * 
	 * @param clazz
	 *            the class to compare to
	 * @return true if this class is a superclass of clazz false else
	 */
	public boolean isSuperClassOf(IAristotelianOntologyClass clazz);

	/**
	 * remove child from the direct subclasses of this class. this class is also
	 * removed from the direct superclasses of child. Child subclasses are not
	 * modified.
	 * 
	 * @param child
	 *            the subclass to remove
	 */
	public void detachSubclass(IAristotelianOntologyClass child);
	
	/**
	 * Detach this class from the classes hierarchy.
	 * 
	 * this class is removed from the direct subclasses of all its direct super classes.
	 * It is also removed from the direct superclasses of all its direct subclasses.
	 * if necessary, this class direct subclasses are re attached to this class
	 * former direct superclasses.
	 */
	public void detach();
	
	/**
	 * Removes this class from the aristotelian ontology
	 * 
	 * Detach this class from the classes hierarchy, and cleans data structures
	 * used by this class.
	 */
	public void remove();
	
	
	/**
	 * true if the class is detached from the classes hierarchy
	 * (has no superclass and no subclasses)
	 * @return true if the class is detached, false else
	 */
	public boolean isDetached();

	/**
	 * finds the root of the hierarchy this primary entity class belongs to
	 * 
	 * @return the root of the hierarchy
	 */
	public IAristotelianOntologyClass findRoot();
	
	/**
	 * builds a unique name for a new subclass of this class. 
	 * The name of of new subclass is of the form :
	 * <i>name_of_thisClass</i>_subclass_<i>number</i>
	 * where number is equals to the biggest number already used plus 1.
	 * For example, if the parent class has already subclasses named with
	 * name_of_thisClass_subclass_1
	 * name_of_thisClass_subclass_3
	 * the new subclass will be named with name_of_thisClass_subclass_4
	 * 
	 * @return name of the new subclass.
	 */
	public String createSubClassId();
	

	/**
	 * @return true if this primary enumerated class has at least one
	 *         primary enumerated parent class.
	 */
	public boolean hasParent();
	
}
