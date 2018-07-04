package com.similar2.matcher.ontology.model.impl;

import static com.similar2.matcher.ontology.model.ClassificationRelationship.EQUIVALENT;
import static com.similar2.matcher.ontology.model.ClassificationRelationship.NO_RELATION;
import static com.similar2.matcher.ontology.model.ClassificationRelationship.SUBCLASS;
import static com.similar2.matcher.ontology.model.ClassificationRelationship.SUPERCLASS;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import com.similar2.matcher.ontology.model.ClassificationRelationship;
import com.similar2.matcher.ontology.model.ClassificationStatus;
import com.similar2.matcher.ontology.model.IAristotelianOntology;
import com.similar2.matcher.ontology.model.IAristotelianOntologyClass;
import com.similar2.matcher.ontology.model.IAttributeDescription;
import com.similar2.matcher.ontology.model.IDetailedDescription;
import com.similar2.matcher.ontology.model.IEnumeratedProperty;
import com.similar2.matcher.ontology.model.IPrimaryEntityClass;
import com.similar2.matcher.ontology.model.IPrimaryEnumeratedClass;
import com.similar2.matcher.ontology.model.PropertySetRelations;
import com.similar2.matcher.ontology.model.exceptions.GenusRTException;
import com.similar2.matcher.ontology.search.SearchDataType;
import com.similar2.matcher.ontology.search.Searcher;

/**
 * implementation class for IprimaryEntityClass interface
 * 
 * @author Philippe Genoud (Philippe.Genoud@imag.fr)
 * @author Anthony Hombiat
 */
public class PrimaryEntityClass extends AristotelianOntologyClass implements
		IPrimaryEntityClass {

	private static final long serialVersionUID = 1L;

	/**
	 * the classification status of this class used during a classification
	 * process.
	 */
	protected ClassificationStatus classifStatus = ClassificationStatus.NO_STATUS;

	private Set<IAttributeDescription> _attributes = new TreeSet<IAttributeDescription>();

	/**
	 * to cache the Genus class of this primary entity class
	 */
	private IPrimaryEntityClass _genus;

	/**
	 * default constructor for serialization
	 */
	public PrimaryEntityClass() {
	}

	public PrimaryEntityClass(IAristotelianOntology ao,String nameSpace, String id) {
		super(ao,nameSpace, id);
	}

	public PrimaryEntityClass(IAristotelianOntology ao,String nameSpace, String id,
			IPrimaryEntityClass superClass) {
		super(ao,nameSpace, id, superClass);
	}

	/**
	 * creates a new Primary EntityClass by cloning an existing one.
	 * 
	 * @param c
	 *            the class to clone
	 * @param newId
	 *            the id of the new class to create
	 * 
	 */
	public PrimaryEntityClass(IPrimaryEntityClass c, String newId) {

		super(c.getAO(),c.getNameSpace(), newId);

		// copy super and sub classes
		for (IAristotelianOntologyClass superClass : c.getSuperClasses()) {
			this.addSuperClass(superClass);
		}
		for (IAristotelianOntologyClass subClass : c.getSubClasses()) {
			this.addSubClass(subClass);
		}
		// copy attributes
		for (IAttributeDescription attribute : c.getAttributesDescriptions()) {
			ArrayList<IPrimaryEnumeratedClass> values = new ArrayList<IPrimaryEnumeratedClass>(
					attribute.getValues());
			this.addAttributeDescription(attribute.getProperty(), values);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.similar2.matcher.ontology.model.IPrimaryEntityClass#getNbAttributes()
	 */
	@Override
	public int getNbAttributes() {
		return _attributes.size();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.similar2.matcher.ontology.model.IPrimaryEntityClass#
	 * getAttributesDescriptions()
	 */
	@Override
	public Set<IAttributeDescription> getAttributesDescriptions() {
		return _attributes;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.similar2.matcher.ontology.model.IPrimaryEntityClass#
	 * addAttributeDescription
	 * (com.similar2.matcher.ontology.model.IEnumeratedProperty, java.util.List)
	 */
	@Override
	public void addAttributeDescription(IEnumeratedProperty property,
			Collection<IPrimaryEnumeratedClass> values) {

		for (IAttributeDescription a : _attributes) {
			if (a.getProperty().equals(property)) {
				a.addValues(values);
				return; // we are done, it's useless to continue the iteration
			}
		}

		// there is no AttributeDescription for the property in the existing
		// attribute descriptions. Create a new one
		_attributes.add(new AttributeDescription(property, values));
		Searcher.getInstance().add(property.getLabels(), this, SearchDataType.PROPERTY);
		for (IPrimaryEnumeratedClass ipec: values)
		{
			Searcher.getInstance().add(ipec.getLabels(), this, SearchDataType.VALUE);
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.similar2.matcher.ontology.model.IPrimaryEntityClass#
	 * comparePropertySet
	 * (com.similar2.matcher.ontology.model.IPrimaryEntityClass)
	 */
	@Override
	public PropertySetRelations comparePropertySet(IPrimaryEntityClass pec) {
		boolean isPropSetIncluded = this.isPropertySetIncludedIn(pec);
		if (isPropSetIncluded) {
			if (this.getNbAttributes() == pec.getNbAttributes()) {
				// properties set are the same
				return PropertySetRelations.EQUALS;
			}
			// this properties set is included in the properties set of the pec
			// class
			return PropertySetRelations.CONTAINED_BY;
		} else {
			if (((PrimaryEntityClass) pec).isPropertySetIncludedIn(this)) {
				// pec properties set is included in the properties set of this
				// class
				return PropertySetRelations.CONTAINS;
			}
			// there are no inclusion or equality relationship between the
			// two properties sets
			return PropertySetRelations.DIFFERENT_FROM;
		}
	}

	/**
	 * tests if the properties set of this class is included in the properties
	 * set of a given primary entity class
	 * 
	 * @param pec
	 *            the primary entity class to compare to
	 * @return true if the properties set of this class is included in the
	 *         properties set of pec, false else
	 */
	private boolean isPropertySetIncludedIn(IPrimaryEntityClass pec) {
		for (IAttributeDescription attrDescr1 : _attributes) {
			boolean found = false;
			for (IAttributeDescription attrDescr2 : pec
					.getAttributesDescriptions()) {
				if (attrDescr1.getProperty().equals(attrDescr2.getProperty())) {
					found = true;
					break;
				}
			}
			if (!found) {
				return false;
			}
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.similar2.matcher.ontology.model.IPrimaryEntityClass#
	 * computeClassifRelationWith
	 * (com.similar2.matcher.ontology.model.IPrimaryEntityClass)
	 */
	@Override
	public ClassificationRelationship computeClassifRelationWith(
			IPrimaryEntityClass pec) {
		PropertySetRelations propsRelation = comparePropertySet(pec);
		switch (propsRelation) {
		case EQUALS:
			return computeClassifRelationWith1(pec);
		case DIFFERENT_FROM:
			return NO_RELATION;
		case CONTAINED_BY:
			return computeClassifRelationWith2(this, pec);
		case CONTAINS:
			ClassificationRelationship res = computeClassifRelationWith2(pec,
					this);
			if (res == SUPERCLASS)
				res = SUBCLASS;
			return res;
		default:
			throw new UnsupportedOperationException(
					"Classification not yet implemented");
		}

	}

	// when property sets are equals
	private ClassificationRelationship computeClassifRelationWith1(
			IPrimaryEntityClass pec) {

		// since attribute descriptions are sorted according
		// property names, it's possible to traverse both attribute description
		// sets
		// simultaneously
		Iterator<IAttributeDescription> thisPropsIterator = this
				.getAttributesDescriptions().iterator();
		Iterator<IAttributeDescription> pecPropsIterator = pec
				.getAttributesDescriptions().iterator();
		ClassificationRelationship res = EQUIVALENT;
		while (thisPropsIterator.hasNext() && res != NO_RELATION) {
			IAttributeDescription thisAttrDescr = thisPropsIterator.next();
			IAttributeDescription pecAttrDescr = pecPropsIterator.next();
			ClassificationRelationship valuesSetsRelation = thisAttrDescr
					.compareValuesSet(pecAttrDescr);
			switch (valuesSetsRelation) {
			case EQUIVALENT: // do nothing , continue
				break;
			case SUBCLASS: // all the valuesSetsRelation must be EQUIVALENT
							// or SUBCLASS
				if (res == SUPERCLASS)
					res = NO_RELATION;
				else if (res == EQUIVALENT)
					res = SUBCLASS;
				break;
			case SUPERCLASS: // all the valuesSetsRelation must be EQUIVALENT
								// or SUPERCLASS
				if (res == SUBCLASS)
					res = NO_RELATION;
				else if (res == EQUIVALENT)
					res = SUPERCLASS;
				break;
			case NO_RELATION: // stop the iteration
				res = NO_RELATION;
			}
		}
		return res;
	}

	// properties set de pec1 contenu dans celui de pec
	private ClassificationRelationship computeClassifRelationWith2(
			IPrimaryEntityClass pec1, IPrimaryEntityClass pec) {

		// since attribute descriptions are sorted according
		// property names, it's possible to traverse both attribute description
		// sets simultaneously
		Iterator<IAttributeDescription> pec1PropsIterator = pec1
				.getAttributesDescriptions().iterator();
		Iterator<IAttributeDescription> pecPropsIterator = pec
				.getAttributesDescriptions().iterator();
		ClassificationRelationship res = EQUIVALENT;
		while (pec1PropsIterator.hasNext() && res != NO_RELATION) {
			IAttributeDescription thisAttrDescr = pec1PropsIterator.next();
			// find an attribute description in pec attribute descriptions
			// corresponding to the property of thisAttrDescr
			IAttributeDescription pecAttrDescr = null;
			do {
				pecAttrDescr = pecPropsIterator.next();
			} while (!pecAttrDescr.getProperty().equals(
					thisAttrDescr.getProperty()));
			// since the properties set of pec1 is contained in pec's one
			// we are sure that the above iteration will stop correctly

			ClassificationRelationship valuesSetsRelation = thisAttrDescr
					.compareValuesSet(pecAttrDescr);
			switch (valuesSetsRelation) {
			case EQUIVALENT: // do nothing , continue
				break;
			case SUBCLASS: // all the valuesSetsRelation must be EQUIVALENT
							// or SUBCLASS
				res = NO_RELATION;
				// if (res == SUPERCLASS)
				// res = NO_RELATION;
				// else if (res == EQUIVALENT)
				// res = SUBCLASS;
				break;
			case SUPERCLASS: // all the valuesSetsRelation must be EQUIVALENT
								// or SUPERCLASS
				if (res == SUBCLASS)
					res = NO_RELATION;
				else if (res == EQUIVALENT)
					res = SUPERCLASS;
				break;
			case NO_RELATION: // stop the iteration
				res = NO_RELATION;
			}
		}
		if (res == EQUIVALENT)
			return SUPERCLASS; // because pec have more properties
		else
			return res;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.similar2.matcher.ontology.model.IPrimaryEntityClass#classify()
	 */
	@Override
	public void classify() {

		IPrimaryEntityClass root = (IPrimaryEntityClass) this.findRoot();
		// mark all classes in the hierarchy pec belongs to
		// with classification status NO_STATUS
		resetStatus(root);

		this.detach();

		// we classify from the root
		List<TaxonomyModificationOperation> modificationOps = new ArrayList<TaxonomyModificationOperation>();
		this.computeClassifRelationOf(null, root, modificationOps);
		for (TaxonomyModificationOperation op : modificationOps) {
			op.execute();
		}
	}

	public void classifyFrom(IPrimaryEntityClass pec) {

		// mark all classes in the hierarchy pec belongs to
		// with classification status NO_STATUS
		resetStatus((IPrimaryEntityClass) pec.findRoot());

		List<TaxonomyModificationOperation> modificationOps = new ArrayList<TaxonomyModificationOperation>();
		computeClassifRelationOf(null, pec, modificationOps);
		for (TaxonomyModificationOperation op : modificationOps) {
			op.execute();
		}
	}

	/**
	 * resets the ClassificationStatus of a class and of all its descendants to
	 * NO_STATUS
	 * 
	 * @param pec
	 *            the class to reset status
	 */
	private void resetStatus(IPrimaryEntityClass pec) {
		pec.setClassificationStatus(ClassificationStatus.NO_STATUS);
		for (IAristotelianOntologyClass child : pec.getSubClasses()) {
			resetStatus((IPrimaryEntityClass) child);
		}

	}

	public ClassificationStatus computeClassifRelationOf(
			IPrimaryEntityClass parent, IPrimaryEntityClass pec,
			List<TaxonomyModificationOperation> modificationOps) {


		if (pec.getClassificationStatus() == ClassificationStatus.NO_STATUS) {
			ClassificationRelationship classifRel = this
					.computeClassifRelationWith(pec);

			switch (classifRel) {
			case EQUIVALENT:
				// this class is equivalent to pec
				modificationOps
						.add(new TaxonomyModificationOperation(
								this,
								pec,
								TaxonomyModificationOperation.OpType.INSERT_EQUIVALENT_CLASS));
				pec.setClassificationStatus(ClassificationStatus.IS_EQUIVALENT);
				break;
			case NO_RELATION:
				// this class has no hierarchical relation with pec
				pec.setClassificationStatus(ClassificationStatus.HAS_NO_RELATION);

				if (parent == null) {
					// classify from each of the parents of pec
					for (IAristotelianOntologyClass superClass : pec
							.getSuperClasses()) {
						computeClassifRelationOf(null,
								(IPrimaryEntityClass) superClass,
								modificationOps);
					}
				} else {
					PropertySetRelations propsRel = this
							.comparePropertySet(pec);
					if (propsRel == PropertySetRelations.EQUALS
							|| propsRel == PropertySetRelations.CONTAINS) {
						// we must consider if the class is not a super class
						// pec descendants
						for (IAristotelianOntologyClass child : pec
								.getSubClasses()) {
							computeClassifRelationOf(pec,
									(IPrimaryEntityClass) child,
									modificationOps);
						}
					}
				}
				break;
			// throw new UnsupportedOperationException(
			// "NO_RELATION not yet superted in classification");
			case SUBCLASS:
				// this class is a subclass of pec
				pec.setClassificationStatus(ClassificationStatus.IS_SUPERCLASS);
				classifAsSubclassOf(pec, modificationOps);
				break;
			// throw new UnsupportedOperationException(
			// "SUBCLASS not yet superted in classification");
			case SUPERCLASS:
				// this class is a super class of pec
				if (parent != null) {
					pec.setClassificationStatus(ClassificationStatus.IS_SUBCLASS);
					switch (parent.getClassificationStatus()) {
					case IS_SUPERCLASS:
						// this is sub class of pec parent
						// previously traversed
						modificationOps
								.add(new TaxonomyModificationOperation(
										parent,
										pec,
										TaxonomyModificationOperation.OpType.DETACH_CHILD));
						modificationOps
								.add(new TaxonomyModificationOperation(
										this,
										pec,
										TaxonomyModificationOperation.OpType.INSERT_SUBCLASS));
						modificationOps
								.add(new TaxonomyModificationOperation(
										parent,
										this,
										TaxonomyModificationOperation.OpType.INSERT_SUBCLASS));
						break;
					case HAS_NO_RELATION:
						// this is only a super class of pec
						modificationOps
								.add(new TaxonomyModificationOperation(
										this,
										pec,
										TaxonomyModificationOperation.OpType.VERIFY_INSERT_SUBCLASS));
						break;
					default:
						throw new UnsupportedOperationException("the case "
								+ parent.getClassificationStatus()
								+ " should not happen");
					}
				} else {
					// classify from each of the parents of pec
					pec.setClassificationStatus(ClassificationStatus.NO_STATUS);
					for (IAristotelianOntologyClass superClass : pec
							.getSuperClasses()) {
						computeClassifRelationOf(null,
								(IPrimaryEntityClass) superClass,
								modificationOps);
					}
				}
				break;
			} // end switch
		} // end if (pec.getClassificationStatus() ==
			// ClassificationStatus.NO_STATUS)
		else if (pec.getClassificationStatus() == ClassificationStatus.IS_SUBCLASS
				&& parent.getClassificationStatus() == ClassificationStatus.IS_SUPERCLASS) {
			modificationOps.add(new TaxonomyModificationOperation(parent, pec,
					TaxonomyModificationOperation.OpType.DETACH_CHILD));
			modificationOps.add(new TaxonomyModificationOperation(this, pec,
					TaxonomyModificationOperation.OpType.INSERT_SUBCLASS));
			modificationOps.add(new TaxonomyModificationOperation(parent, this,
					TaxonomyModificationOperation.OpType.INSERT_SUBCLASS));
		}

		return pec.getClassificationStatus();
	}

	/**
	 * this is a sublcass of pec
	 * 
	 * @param pec
	 */
	private void classifAsSubclassOf(IPrimaryEntityClass pec,
			List<TaxonomyModificationOperation> modificationOps) {

		boolean hasBeenClassified = false; // true is the primary entity class
											// is a subclass of one of the child
											// of pec

		if (!pec.isLeaf()) {
			for (IAristotelianOntologyClass pecChild : pec.getSubClasses()) {
				ClassificationStatus pecClassifStatus = computeClassifRelationOf(
						pec, (IPrimaryEntityClass) pecChild, modificationOps);
				switch (pecClassifStatus) {
				case IS_EQUIVALENT:
					// pecChild has been classified as equivalent to this
					// it is pointless to compare this
					// with other pecChild siblings
					return;
				case HAS_NO_RELATION:
					break; // go to next child
				case IS_SUPERCLASS:
					hasBeenClassified = true;
					break; // go to next child
				}
			}
		}
		// the class is a SUBCLASS of pec
		// we will have to add it as a child of pec
		if (!hasBeenClassified) {
			modificationOps.add(new TaxonomyModificationOperation(pec, this,
					TaxonomyModificationOperation.OpType.INSERT_SUBCLASS));
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.similar2.matcher.ontology.model.IPrimaryEntityClass#
	 * getClassificationStatus()
	 */
	@Override
	public ClassificationStatus getClassificationStatus() {
		return classifStatus;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.similar2.matcher.ontology.model.IPrimaryEntityClass#
	 * setClassificationStatus
	 * (com.similar2.matcher.ontology.model.ClassificationStatus)
	 */
	@Override
	public void setClassificationStatus(ClassificationStatus status) {
		classifStatus = status;

	}

	@Override
	public void removeAllAttributeDescriptions() {
		_attributes.clear();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.similar2.matcher.ontology.model.IPrimaryEntityClass#getGenus()
	 */
	@Override
	public IPrimaryEntityClass getGenus() {
		if (_genus == null)
			_genus = (IPrimaryEntityClass) findRoot();
		return _genus;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.similar2.matcher.ontology.model.impl.AristotelianOntologyClass#detach
	 * ()
	 */
	@Override
	public void detach() {
		getGenus(); // to ensure than the genus of this class is saved
		// before detaching it
		super.detach();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.similar2.matcher.ontology.model.IPrimaryEntityClass#classifyFromGenus
	 * ()
	 */
	@Override
	public IPrimaryEntityClass classifyFromGenus() {

		IPrimaryEntityClass genus = this.getGenus();
		// mark all classes in the hierarchy pec belongs to
		// with classification status NO_STATUS
		resetStatus(genus);

		this.detach();

		// we classify from the root
		List<TaxonomyModificationOperation> modificationOps = new ArrayList<TaxonomyModificationOperation>();
		this.computeClassifRelationOf(null, genus, modificationOps);

		// exploit the results of the classification to modify the taxonomy
		for (TaxonomyModificationOperation op : modificationOps) {
			if (op.getOpType() == TaxonomyModificationOperation.OpType.INSERT_EQUIVALENT_CLASS) {
				return op.getObject(); // the class this class is equivalent to
			} else
				op.execute();
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.similar2.matcher.ontology.model.IPrimaryEntityClass#
	 * findAttributeDescription
	 * (com.similar2.matcher.ontology.model.IEnumeratedProperty)
	 */
	@Override
	public IAttributeDescription findAttributeDescription(IEnumeratedProperty p) {
		for (IAttributeDescription attr : _attributes) {
			if (attr.getProperty().equals(p)) {
				return attr;
			}
		}
		return null;
	}
	
	@Override
	public IAttributeDescription findAttributeDescriptionById(String property) {
		for (IAttributeDescription attr : _attributes) {
			if (attr.getProperty().getId().equals(property)) {
				return attr;
			}
		}
		return null;
	}

	@Override
	public IDetailedDescription getDetailedDescription() {
		return new DetailedDescription(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.similar2.matcher.ontology.model.IPrimaryEntityClass#findAttribute
	 * (com.similar2.matcher.ontology.model.IEnumeratedProperty,
	 * com.similar2.matcher.ontology.model.IPrimaryEnumeratedClass)
	 */
	@Override
	public boolean hasAttribute(IEnumeratedProperty property,
			IPrimaryEnumeratedClass value) {
		for (IAttributeDescription attr : _attributes) {
			if (attr.getProperty().equals(property)) {
				for (IPrimaryEnumeratedClass v : attr.getValues()) {
					if (v.equals(value))
						return true;
				}
			}
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see com.similar2.matcher.ontology.model.impl.AristotelianOntologyClass#cleanDataStructures()
	 */
	@Override
	protected void cleanDataStructures() {
		_attributes.clear();
	}

	@Override
	public Set<IEnumeratedProperty> findEnumeratedPropertiesByDomain() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deletePrimaryEntityClass() {
		detach();
	}

	@Override
	public void deletePrimaryEntityClassHierarchy() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteGenus() throws GenusRTException {
		// TODO Auto-generated method stub
		
	}

}