package com.similar2.matcher.ontology.model.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.semanticweb.owlapi.util.OWLDocumentFormatFactoryImpl;
import org.semanticweb.owlapi.formats.RDFXMLDocumentFormatFactory;

import org.semanticweb.owlapi.vocab.PrefixOWLOntologyFormat;
//import org.semanticweb.owlapi.io.RDFOntologyFormat;
import org.semanticweb.owlapi.formats.AbstractRDFPrefixDocumentFormat;
import org.semanticweb.owlapi.model.OWLDocumentFormat;

import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLOntologyFormat;
import org.semanticweb.owlapi.vocab.OWLRDFVocabulary;

import com.similar2.matcher.ontology.model.IAristotelianOntology;
import com.similar2.matcher.ontology.model.IAristotelianOntologyClass;
import com.similar2.matcher.ontology.model.IAttributeDescription;
import com.similar2.matcher.ontology.model.IEnumeratedProperty;
import com.similar2.matcher.ontology.model.IPrimaryEntityClass;
import com.similar2.matcher.ontology.model.IPrimaryEnumeratedClass;
import com.similar2.matcher.ontology.model.LoggerManager;
import com.similar2.matcher.ontology.model.exceptions.EnumeratedPropertyRTException;
import com.similar2.matcher.ontology.model.exceptions.GenusRTException;
import com.similar2.matcher.ontology.model.exceptions.PrimaryEntityClassRTException;
import com.similar2.matcher.ontology.model.exceptions.PrimaryEnumeratedClassRTException;
import com.similar2.matcher.ontology.model.exceptions.ValueHierarchyRTException;

/**
 * implementation class of the IAristotelianOntology interface
 * 
 * @author Philippe Genoud (Philippe.Genoud@imag.fr)
 * 
 */
public class AristotelianOntology implements IAristotelianOntology {

	/**
	 * the set of all root PrimaryEnumerated classes used by this Aristotelian
	 * ontology
	 */
	private SortedSet<IPrimaryEnumeratedClass> _valueHierarchies = new TreeSet<IPrimaryEnumeratedClass>();

	/**
	 * the map of all PrimaryEnumerated classes used by this Aristotelian
	 * ontology under the form <FQName, IPrimaryEnumeratedClass>
	 */
	private Map<String, IPrimaryEnumeratedClass> _primaryEnumeratedClasses = new HashMap<String, IPrimaryEnumeratedClass>();

	/**
	 * the set of EnumeratedProperty classes used by this Aristotelian ontology
	 */
	private Set<IEnumeratedProperty> _enumeratedProperties = new TreeSet<IEnumeratedProperty>();

	/**
	 * the map of all PrimaryEntity classes used by this Aristotelian ontology
	 * under the form <FQName, IPrimaryEntityClass>
	 */
	private Map<String, IPrimaryEntityClass> _primaryEntityClasses = new HashMap<String, IPrimaryEntityClass>();

	/**
	 * the set of all root Primary Entity classes (genus) used by this
	 * Aristotelian ontology
	 */
	private Set<IPrimaryEntityClass> _genera = new HashSet<IPrimaryEntityClass>();

	/**
	 * the set of IRI of the ontologies imported by this ontology
	 */
	private Set<IRI> _importedOntologiesIRIs = new HashSet<IRI>();

	/**
	 * names of the file the ontology comes from can be used when saving it.
	 */
	private String fileName;

	/**
	 * iri of the ontology.
	 */
	private String ontIRI;

	/**
	 * format used at the creation of this ontology. It's either the format used
	 * by OWL API when loading the ontology from an input stream, or a
	 * DefaultOntologyFormat if the ontology is created form scratch.
	 */
	private OWLDocumentFormat format;
	

	/**
	 * creates an new empty Aristotelian Ontology with a given IRI
	 * 
	 * @param ontIRI
	 *            the ontology IRI
	 */
	public AristotelianOntology(String ontIRI) {
		this(ontIRI, (new RDFXMLDocumentFormatFactory()).createFormat());
		format.asPrefixOWLOntologyFormat().setDefaultPrefix(ontIRI);
		// TODO verify that we do'nt need to concat "#" to ontIRI
	}

	/**
	 * creates an new empty Aristotelian Ontology with a given IRI and a given
	 * ontology format (used when an ontology is read from a file through
	 * OWL-API)
	 * 
	 * @param ontIRI
	 *            the ontology IRI
	 * @param format
	 *            the format used with this ontology
	 */
	public AristotelianOntology(String ontIRI, OWLDocumentFormat format) {
		this.setOntologyIRI(ontIRI);
		this.format = format;
	}

	@Override
	public OWLDocumentFormat getFormat() {
		return format;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.similar2.matcher.ontology.model.IAristotelianOntology#getValueHierarchies
	 * ()
	 */
	@Override
	public Set<IPrimaryEnumeratedClass> getValueHierarchies() {
		return _valueHierarchies;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.similar2.matcher.ontology.model.IAristotelianOntology#addValuesHierarchy
	 * (com.similar2.matcher.ontology.model.IPrimaryEnumeratedClass)
	 */
	@Override
	public void addValuesHierarchy(IPrimaryEnumeratedClass valueHierarchy)
			throws ValueHierarchyRTException {
		if (!valueHierarchy.isRoot())
			throw new ValueHierarchyRTException("values hierarchy "
					+ valueHierarchy + " is not a root class");
		if (!_valueHierarchies.contains(valueHierarchy)) {
			_valueHierarchies.add(valueHierarchy);
			LoggerManager.LOGGER.info("\nadded Value Hierarchy {}: {}",
					valueHierarchy.getNameSpace(), valueHierarchy.getId());
		} else
			throw new ValueHierarchyRTException("values hierarchy "
					+ valueHierarchy + " already exists");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.similar2.matcher.ontology.model.IAristotelianOntology#getValueHierarchy
	 * (java.lang.String, java.lang.String)
	 */
	@Override
	public IPrimaryEnumeratedClass getValueHierarchy(String nameSpace, String id) {
		String fqName = buildFQName(nameSpace,id);
		for (IPrimaryEnumeratedClass ipec : _valueHierarchies)
			if (ipec.getId().equals(id))
				if (ipec.getFQName().equals(fqName))
					return ipec;
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.similar2.matcher.ontology.model.IAristotelianOntology#
	 * getNbValueHierarchies()
	 */
	@Override
	public int getNbValueHierarchies() {
		return _valueHierarchies.size();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.similar2.matcher.ontology.model.IAristotelianOntology#
	 * getPrimaryEnumeratedClass(java.lang.String, java.lang.String)
	 */
	@Override
	public IPrimaryEnumeratedClass getPrimaryEnumeratedClass(String nameSpace,
			String id) {
		return _primaryEnumeratedClasses.get(buildFQName(nameSpace, id)); // nameSpace
																			// +
																			// id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.similar2.matcher.ontology.model.IAristotelianOntology#
	 * getPrimaryEnumeratedClass(java.lang.String)
	 */
	@Override
	public IPrimaryEnumeratedClass getPrimaryEnumeratedClass(String fqName) {
		return _primaryEnumeratedClasses.get(fqName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.similar2.matcher.ontology.model.IAristotelianOntology#
	 * addPrimaryEnumeratedClass
	 * (com.similar2.matcher.ontology.model.IPrimaryEnumeratedClass)
	 */
	@Override
	public void addPrimaryEnumeratedClass(IPrimaryEnumeratedClass ipec) {
		if (_primaryEnumeratedClasses.get(ipec.getFQName()) == null) {
			_primaryEnumeratedClasses.put(ipec.getFQName(), ipec);
			LoggerManager.LOGGER.info("\nadded Primary Enumerated Class {}",
					ipec.getId());
		} else
			throw new PrimaryEnumeratedClassRTException(
					"Primary enumerated class " + ipec.getFQName()
							+ " already exists");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.similar2.matcher.ontology.model.IAristotelianOntology#
	 * getNbPrimaryEnumeratedClass()
	 */
	@Override
	public int getNbPrimaryEnumeratedClasses() {
		return _primaryEnumeratedClasses.size();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.similar2.matcher.ontology.model.IAristotelianOntology#
	 * getPrimaryEnumeratedClasses()
	 */
	@Override
	public Collection<IPrimaryEnumeratedClass> getPrimaryEnumeratedClasses() {
		return _primaryEnumeratedClasses.values();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.similar2.matcher.ontology.model.IAristotelianOntology#
	 * getEnumeratedProperties()
	 */
	@Override
	public Set<IEnumeratedProperty> getEnumeratedProperties() {
		return _enumeratedProperties;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.similar2.matcher.ontology.model.IAristotelianOntology#
	 * getEnumeratedProperty(java.lang.String, java.lang.String))
	 */
	@Override
	public IEnumeratedProperty getEnumeratedProperty(String nameSpace, String id) {
		return getEnumeratedProperty(buildFQName(nameSpace, id)); // nameSpace+id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.similar2.matcher.ontology.model.IAristotelianOntology#
	 * getEnumeratedProperty(java.lang.String)
	 */
	@Override
	public IEnumeratedProperty getEnumeratedProperty(String fqName) {
		for (IEnumeratedProperty iep : _enumeratedProperties)
			if (iep.getFQName().equals(fqName))
				return iep;
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.similar2.matcher.ontology.model.IAristotelianOntology#
	 * getNbEnumeratedProperties()
	 */
	@Override
	public int getNbEnumeratedProperties() {
		return _enumeratedProperties.size();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.similar2.matcher.ontology.model.IAristotelianOntology#
	 * addEnumeratedProperty
	 * (com.similar2.matcher.ontology.model.IEnumeratedProperty)
	 */
	@Override
	public void addEnumeratedProperty(IEnumeratedProperty enumeratedProperty) {
		if (!_enumeratedProperties.contains(enumeratedProperty)) {
			_enumeratedProperties.add(enumeratedProperty);
			LoggerManager.LOGGER.info("\nadded Enumerated Property {}",
					enumeratedProperty.getId());
		} else
			throw new EnumeratedPropertyRTException("enumerated property "
					+ enumeratedProperty + " already exists");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.similar2.matcher.ontology.model.IAristotelianOntology#
	 * getPrimaryEntityClasses()
	 */
	@Override
	public Collection<IPrimaryEntityClass> getPrimaryEntityClasses() {
		return _primaryEntityClasses.values();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.similar2.matcher.ontology.model.IAristotelianOntology#
	 * getPrimaryEntityClass(java.lang.String, java.lang.String)
	 */
	@Override
	public IPrimaryEntityClass getPrimaryEntityClass(String nameSpace, String id) {
		//
		return _primaryEntityClasses.get(buildFQName(nameSpace, id));
	}

	private static final String buildFQName(String nameSpace, String id) {
		if (nameSpace == null || nameSpace.length() == 0)
			return "#" + id;
		if (nameSpace.endsWith("#"))
			return nameSpace + id;
		return nameSpace + "#" + id;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.similar2.matcher.ontology.model.IAristotelianOntology#
	 * addPrimaryEntityClass
	 * (com.similar2.matcher.ontology.model.IPrimaryEntityClass)
	 */
	@Override
	public void addPrimaryEntityClass(IPrimaryEntityClass ipec) {
		if (_primaryEntityClasses.get(ipec.getFQName()) == null) {
			_primaryEntityClasses.put(ipec.getFQName(), ipec);
			LoggerManager.LOGGER.info("\nadded Primary Entity Class {}",
					ipec.getId());
		} else
			throw new PrimaryEntityClassRTException("Primary entity class "
					+ ipec.getFQName() + " already exists");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.similar2.matcher.ontology.model.IAristotelianOntology#
	 * getNbPrimaryEntityClasses()
	 */
	@Override
	public int getNbPrimaryEntityClasses() {
		return _primaryEntityClasses.size();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.similar2.matcher.ontology.model.IAristotelianOntology#getGenera()
	 */
	@Override
	public Set<IPrimaryEntityClass> getGenera() {
		return _genera;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.similar2.matcher.ontology.model.IAristotelianOntology#addGenus(com
	 * .similar2.matcher.ontology.model.IPrimaryEntityClass)
	 */
	@Override
	public void addGenus(IPrimaryEntityClass genus) throws GenusRTException {
		if (!genus.isRoot())
			throw new GenusRTException("genus " + genus
					+ " is not a root class");
		if (!_genera.contains(genus)) {
			_genera.add(genus);
			LoggerManager.LOGGER.info("\nadded Genus {}", genus.getId());
		} else
			throw new GenusRTException("genus " + genus + " already exists");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.similar2.matcher.ontology.model.IAristotelianOntology#getNbGenera()
	 */
	@Override
	public int getNbGenera() {
		return _genera.size();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.similar2.matcher.ontology.model.IAristotelianOntology#getGenus(java
	 * .lang.String, java.lang.String)
	 */
	@Override
	public IPrimaryEntityClass getGenus(String nameSpace, String id) {
		String fqName = buildFQName(nameSpace, id);
		for (IPrimaryEntityClass ipec : _genera)
			if (ipec.getId().equals(id)) // first filter (faster)
				if (ipec.getFQName().equals(fqName))
					return ipec;
			
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.similar2.matcher.ontology.model.IAristotelianOntology#getAOClass(
	 * java.lang.String, java.lang.String)
	 */
	@Override
	public IAristotelianOntologyClass getAOClass(String nameSpace, String id) {
		IAristotelianOntologyClass aoClass = getPrimaryEntityClass(nameSpace,
				id);
		if (aoClass == null)
			aoClass = getPrimaryEnumeratedClass(nameSpace, id);
		return aoClass;
	}

	public Collection<String> getVocabulary() {
		Set<String> vocabulary = new HashSet<String>();
		for (IEnumeratedProperty property : getEnumeratedProperties())
			vocabulary.add(property.getId());
		for (IPrimaryEntityClass entityCls : getPrimaryEntityClasses())
			vocabulary.add(entityCls.getId());
		for (IPrimaryEnumeratedClass enumCls : getPrimaryEnumeratedClasses())
			vocabulary.add(enumCls.getId());
		return vocabulary;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.similar2.matcher.ontology.model.IAristotelianOntology#
	 * createEntityClassClone(java.lang.String, java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public IPrimaryEntityClass cloneEntityClass(String id, String nameSpace,
			String cloneId) throws IllegalArgumentException {
		IPrimaryEntityClass clone = new PrimaryEntityClass(
				this.getPrimaryEntityClass(nameSpace, id), cloneId);
		this.addPrimaryEntityClass(clone);
		return clone;
	}

	@Override
	public void propagateAttributes() {

		for (IPrimaryEntityClass genus : this.getGenera()) {
			System.out.println("propagate " + genus.getId());
			for (IAristotelianOntologyClass child : genus.getSubClasses()) {
				propagateParentAttributes(child, genus);
			}
		}

	}

	/**
	 * @param child
	 * @param parent
	 */
	private void propagateParentAttributes(IAristotelianOntologyClass node,
			IPrimaryEntityClass parent) {
		IPrimaryEntityClass entity = (IPrimaryEntityClass) node;

		Set<IAttributeDescription> parentAttributes = parent
				.getAttributesDescriptions();
		for (IAttributeDescription parentAttribute : parentAttributes) {
			IAttributeDescription entityAttribute = entity
					.findAttributeDescription(parentAttribute.getProperty());
			if (entityAttribute == null) {
				entity.addAttributeDescription(parentAttribute.getProperty(),
						parentAttribute.getValues());

			}
		}

		if (!entity.isLeaf()) {
			for (IAristotelianOntologyClass child : entity.getSubClasses()) {
				propagateParentAttributes(child, entity);
			}
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.similar2.matcher.ontology.model.IAristotelianOntology#
	 * changePrimaryEntityClassId
	 * (com.similar2.matcher.ontology.model.IPrimaryEntityClass,
	 * java.lang.String)
	 */
	public void changePrimaryEntityClassId(IPrimaryEntityClass entityClass,
			String id) {
		if (entityClass != null) {
			_primaryEntityClasses.remove(entityClass.getFQName());
			entityClass.setId(id);
			addPrimaryEntityClass(entityClass);
		}
	}

	@Override
	public void changePrimaryEnumeratedClassId(
			IPrimaryEnumeratedClass enumClass, String id) {
		_primaryEnumeratedClasses.remove(enumClass.getFQName());
		enumClass.setId(id);
		addPrimaryEnumeratedClass(enumClass);
	}

	@Override
	public void changeEnumeratedPropertyId(IEnumeratedProperty enumProp,
			String id) {
		_enumeratedProperties.remove(enumProp.getFQName());
		enumProp.setId(id);
		addEnumeratedProperty(enumProp);
	}

	/**
	 * @return the fileName
	 */
	@Override
	public String getFileName() {
		return fileName;
	}

	/**
	 * @param fileName
	 *            the fileName to set
	 */
	@Override
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	/**
	 * @return the ontIRI
	 */
	@Override
	public String getOntologyIRI() {
		return ontIRI;
	}

	/**
	 * @param ontIRI
	 *            the ontIRI to set
	 */
	@Override
	public void setOntologyIRI(String ontIRI) {
		this.ontIRI = ontIRI;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.similar2.matcher.ontology.model.IAristotelianOntology#remove(com.
	 * similar2.matcher.ontology.model.IAristotelianOntologyClass)
	 */
	@Override
	public void remove(IAristotelianOntologyClass cl) {
		cl.remove();
		if (cl instanceof IPrimaryEntityClass) {
			_primaryEntityClasses.remove(cl.getFQName());
		} else if (cl instanceof IPrimaryEnumeratedClass) {
			_primaryEnumeratedClasses.remove(cl.getFQName());
		} else {
			throw new UnsupportedOperationException();
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.similar2.matcher.ontology.model.IAristotelianOntology#addImport(org
	 * .semanticweb.owlapi.model.IRI)
	 */
	@Override
	public void addImport(IRI iri) {
		_importedOntologiesIRIs.add(iri);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.similar2.matcher.ontology.model.IAristotelianOntology#
	 * getImportedOntologyIRIs()
	 */
	@Override
	public Set<IRI> getImportedOntologyIRIs() {
		return _importedOntologiesIRIs;
	}

	@Override
	public void deleteValueHierarchy(String fqName)
			throws PrimaryEnumeratedClassRTException {
		// TODO Auto-generated method stub

	}

	@Override
	public Set<IEnumeratedProperty> findEnumeratedPropertiesByRange(
			String fqRootEnumClassName) {
		Set<IEnumeratedProperty> res = new HashSet<IEnumeratedProperty>();
		for (IEnumeratedProperty iep : _enumeratedProperties)
			if (iep.getRange().getFQName().equals(fqRootEnumClassName))
				res.add(iep);
		return res;
	}

	@Override
	public Set<IEnumeratedProperty> findEnumeratedPropertiesByDomain(
			String fqGenus) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteGenus(String fqEntityClassName) throws GenusRTException {
		// TODO Auto-generated method stub

	}

	@Override
	public Collection<IPrimaryEntityClass> findPrimaryEntityClassesByProperty(
			String fqPropertyName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deletePrimaryEntityClass(String fqPrimaryEntityClassName) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deletePrimaryEntityClassHierarchy(
			String fqPrimaryEntityClassName) {
		// TODO Auto-generated method stub

	}
	
	/**
	 * erase all references to a given PrimaryEnumeratedClass from this ontology
	 * data structures.
	 * @param pec the primary enumerated class to erase.
	 */
	protected void erasePrimaryEnumeratedClass(IPrimaryEnumeratedClass pec) {
		_primaryEnumeratedClasses.remove(pec.getFQName());
		_valueHierarchies.remove(pec);
	}

	@Override
	public void removeValueHiearchySubtree(String parentFQName,
			String childFQName) {
		if (parentFQName.equals(OWLRDFVocabulary.OWL_THING.getIRI().toString())) {
		  removeValueHierarchy(childFQName);
		} else {
			IPrimaryEnumeratedClass parent = this
					.getPrimaryEnumeratedClass(parentFQName);
			IPrimaryEnumeratedClass child = this
					.getPrimaryEnumeratedClass(childFQName);
			if (! child.getSuperClasses().contains(parent)) {
				throw new IllegalArgumentException(childFQName + "is not a child of " + parentFQName);
			}
			// TODO verify that child is a child of parent
			parent.removeSubTree(child);
		}
	}

	/**
	 * TODO this method should be suppressed when we will add a 
	 * PrimaryEnumeratedclass root playing role of THING.
	 * 
	 * removes a whole valueHierarchy
	 * @param rootFQName the FQname of the root value hierarchy
	 */
	private void removeValueHierarchy(String rootFQName) {
		IPrimaryEnumeratedClass root = this
		.getPrimaryEnumeratedClass(rootFQName);
		if (root.hasParent()) {
			throw new IllegalArgumentException(rootFQName + " is not root of a value hierarchy");
		}
		
		// TODO the following line is to avoid java.util.ConcurrentModificationException
		Collection<IAristotelianOntologyClass> subclasses = new ArrayList<IAristotelianOntologyClass>(root.getSubClasses());
		for (IAristotelianOntologyClass subclass : subclasses) {
			IPrimaryEnumeratedClass peSubclass = (IPrimaryEnumeratedClass) subclass;
			root.removeSubTree(peSubclass);
		}
		if (! root.hasParent()) {
			// should be always true
			root.removeFromAODataStructures();
		}
	}
	
//	/**
//	 * remove a primary enumerated class from the ontology data structures
//	 * @param pec the primary enumerated class to remove.
//	 */
//	protected void removePrimaryEnumeratedClass(IPrimaryEnumeratedClass pec) {
//		_primaryEnumeratedClasses.remove(pec.getFQName());
//	}

}