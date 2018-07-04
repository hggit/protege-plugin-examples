package com.similar2.matcher.ontology.io;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.io.OWLXMLOntologyFormat;
import org.semanticweb.owlapi.io.RDFXMLOntologyFormat;
import org.semanticweb.owlapi.model.AddAxiom;
import org.semanticweb.owlapi.model.AddImport;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLFunctionalObjectPropertyAxiom;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectIntersectionOf;
import org.semanticweb.owlapi.model.OWLObjectPropertyExpression;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyChange;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLDocumentFormat;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.semanticweb.owlapi.vocab.OWLRDFVocabulary;

import com.similar2.matcher.ontology.model.IAristotelianOntology;
import com.similar2.matcher.ontology.model.IAristotelianOntologyClass;
import com.similar2.matcher.ontology.model.IAttributeDescription;
import com.similar2.matcher.ontology.model.IEnumeratedProperty;
import com.similar2.matcher.ontology.model.IPrimaryEntityClass;
import com.similar2.matcher.ontology.model.IPrimaryEnumeratedClass;
import com.similar2.matcher.ontology.model.exceptions.AristotelianOntologyWriterException;
import com.similar2.matcher.ontology.model.impl.NamedAndCommentedEntity;

/**
 * class for saving aristotelian ontologies.
 * 
 * @author Anthony Hombiat (Anthony Hombiat@imag.fr)
 * @author Philippe Genoud (Philippe.Genoud@imag.fr)
 */
public class OntologyWriter {

	// language tags for the IAnnotations
	private String[] _languages = { "en", "fr" };

	private OWLOntology _ontology;
	private OWLOntologyManager _manager = OWLManager.createOWLOntologyManager();
	private OWLDataFactory _factory = _manager.getOWLDataFactory();
	private OWLDocumentFormat _format;

	/**
	 * creates a writer for a given Aristotelian Ontology
	 * 
	 * @param ao
	 *            the aristotelian ontology associated with this writer
	 * 
	 * @throws AristotelianOntologyWriterException
	 */
	public OntologyWriter(IAristotelianOntology ao)
			throws AristotelianOntologyWriterException {
		try {
			IRI ontologyIRI = IRI.create(ao.getOntologyIRI());
			_ontology = _manager.createOntology(ontologyIRI);
			_format = ao.getFormat();
			addImportsDeclarations(ao);
			addPrimaryEntityClasses(ao);
			addPrimaryEnumeratedClasses(ao);
			addEnumeratedProperties(ao);
			addRestrictionsOnGenera(ao);
		} catch (OWLOntologyCreationException e) {
			throw new AristotelianOntologyWriterException("AO-CORE ERROR "
					+ e.getMessage(), e);
		}
	}



	/**
	 * writes the aristotelian ontology associated to this writer to the
	 * specified output stream.
	 * 
	 * @param out
	 *            the output stream where the ontology will be saved to
	 * @throws AristotelianOntologyWriterException
	 */
	public void writeAristotelianOntology(OutputStream out)
			throws AristotelianOntologyWriterException {
		try {
//            // We can save the ontology in a different format
//            // Lets save the ontology in owl/xml format
//           // OWLXMLOntologyFormat owlxmlFormat = new OWLXMLOntologyFormat();
//           RDFXMLOntologyFormat savingFormat = new RDFXMLOntologyFormat();
//            if(_format.isPrefixOWLOntologyFormat()) {
//                savingFormat.copyPrefixesFrom(_format.asPrefixOWLOntologyFormat());
//            }
//			_manager.saveOntology(_ontology, savingFormat, out);
			_manager.saveOntology(_ontology, _format, out);
		} catch (OWLOntologyStorageException e) {
			throw new AristotelianOntologyWriterException(e.getMessage(), e);
		}
	}

	/**
	 * writes the aristotelian ontology associated to this writer to the
	 * a file using the specified filePath to determine where the ontology
	 * should be saved.
	 * @param filePath the file where the ontology should be saved to.
	 * @throws AristotelianOntologyWriterException
	 */
	public void writeAristotelianOntology(String filePath)
			throws AristotelianOntologyWriterException {
		try {
			writeAristotelianOntology(new FileOutputStream(new File(filePath)));
		} catch (IOException e) {
			throw new AristotelianOntologyWriterException(e.getMessage(), e);
		}
	}
	
	/**
	 * Adds the direct imports of the given aristotelian ontology in the
	 * owl ontology
	 * 
	 * @param ao
	 */
	private void addImportsDeclarations(IAristotelianOntology ao) {
		
		List<OWLOntologyChange> importsToAdd = new ArrayList<OWLOntologyChange>();
		
		for (IRI importedOntologyIRI : ao.getImportedOntologyIRIs()) {
			importsToAdd.add(new AddImport(_ontology,
					_factory.getOWLImportsDeclaration(importedOntologyIRI)));
		}
		
		// We now use the manager to apply the change
		_manager.applyChanges(importsToAdd);
	}

	/**
	 * Adds the primary entity classes of the given aristotelian ontology in the
	 * owl ontology
	 * 
	 * @param ao
	 * @throws AristotelianOntologyWriterException
	 */
	private void addPrimaryEntityClasses(IAristotelianOntology ao)
			throws AristotelianOntologyWriterException {
		IRI iri = AristotelianOntologyVocabulary.PRIMARY_ENTITY_CLASS.getIRI();
		OWLClass primaryEntityClass = _factory.getOWLClass(iri);
		List<OWLOntologyChange> axiomsToAdd = new ArrayList<OWLOntologyChange>();

		for (IPrimaryEntityClass pec : ao.getPrimaryEntityClasses()) {

			OWLNamedIndividual ind = _factory.getOWLNamedIndividual(IRI
					.create(pec.getFQName()));
//			OWLNamedIndividual ind = _factory.getOWLNamedIndividual(IRI
//					.create(pec.getId()));
			OWLAxiom clsAssertAx = _factory.getOWLClassAssertionAxiom(
					primaryEntityClass, ind);

			axiomsToAdd.add(new AddAxiom(_ontology, clsAssertAx));
			axiomsToAdd.addAll(getSubClassesChanges(pec));
			axiomsToAdd.addAll(getAnnotationsChanges(
					(NamedAndCommentedEntity) pec, _factory.getRDFSLabel()));
			axiomsToAdd.addAll(getAnnotationsChanges(
					(NamedAndCommentedEntity) pec, _factory.getRDFSComment()));

		}

		// We now use the manager to apply the change
		_manager.applyChanges(axiomsToAdd);

	}

	/**
	 * Adds the primary enumerated classes of the given aristotelian ontology in
	 * the owl ontology
	 * 
	 * @param ao
	 * @throws AristotelianOntologyWriterException
	 */
	private void addPrimaryEnumeratedClasses(IAristotelianOntology ao)
			throws AristotelianOntologyWriterException {
		IRI iri = AristotelianOntologyVocabulary.PRIMARY_ENUMERATED_CLASS
				.getIRI();
		OWLClass primaryEnumeratedClass = _factory.getOWLClass(iri);
		List<OWLOntologyChange> addAxioms = new ArrayList<OWLOntologyChange>();

		for (IPrimaryEnumeratedClass pec : ao.getPrimaryEnumeratedClasses()) {
			OWLNamedIndividual ind = _factory.getOWLNamedIndividual(IRI
					.create(pec.getFQName()));
			OWLAxiom clsAssertAx = _factory.getOWLClassAssertionAxiom(
					primaryEnumeratedClass, ind);

			addAxioms.add(new AddAxiom(_ontology, clsAssertAx));

			addAxioms.addAll(getSubClassesChanges(pec));
			addAxioms.addAll(getAnnotationsChanges(
					(NamedAndCommentedEntity) pec, _factory.getRDFSLabel()));
			addAxioms.addAll(getAnnotationsChanges(
					(NamedAndCommentedEntity) pec, _factory.getRDFSComment()));
		}

		// We now use the manager to apply the change
		_manager.applyChanges(addAxioms);

	}

	/**
	 * Adds the enumerated properties of the given aristotelian ontology in the
	 * owl ontology
	 * 
	 * @param ao
	 * @throws AristotelianOntologyWriterException
	 */
	private void addEnumeratedProperties(IAristotelianOntology ao)
			throws AristotelianOntologyWriterException {
		IRI iri = AristotelianOntologyVocabulary.ENUMERATED_PROPERTY.getIRI();
		OWLClassExpression enumeratedProperty = _factory.getOWLClass(iri);
		List<OWLOntologyChange> addAxioms = new ArrayList<OWLOntologyChange>();

		for (IEnumeratedProperty ep : ao.getEnumeratedProperties()) {

			OWLNamedIndividual epInd = _factory.getOWLNamedIndividual(IRI
					.create(ep.getFQName()));

			OWLAxiom assertAx = _factory.getOWLClassAssertionAxiom(
					enumeratedProperty, epInd);

			addAxioms.add(new AddAxiom(_ontology, assertAx));
			addAxioms.addAll(getAnnotationsChanges(
					(NamedAndCommentedEntity) ep, _factory.getRDFSLabel()));
			addAxioms.addAll(getAnnotationsChanges(
					(NamedAndCommentedEntity) ep, _factory.getRDFSComment()));

			OWLObjectPropertyExpression prop = _factory
					.getOWLObjectProperty(IRI.create(ep.getFQName()));

			OWLClassExpression domain = _factory.getOWLClass(IRI.create(ep
					.getDomain().getFQName()));
			OWLAxiom domainAxiom = _factory.getOWLObjectPropertyDomainAxiom(
					prop, domain);

			OWLClassExpression range = _factory.getOWLClass(IRI.create(ep
					.getRange().getFQName()));
			OWLAxiom rangeAxiom = _factory.getOWLObjectPropertyRangeAxiom(prop,
					range);

			addAxioms.add(new AddAxiom(_ontology, domainAxiom));
			addAxioms.add(new AddAxiom(_ontology, rangeAxiom));

			if (ep.isFunctional()) {
				OWLFunctionalObjectPropertyAxiom functAxiom = _factory
						.getOWLFunctionalObjectPropertyAxiom(prop);
				addAxioms.add(new AddAxiom(_ontology, functAxiom));
			}
		}

		// We now use the manager to apply the change
		_manager.applyChanges(addAxioms);

	}

	/**
	 * Gets the subclasses of the given entity and returns the corresponding owl
	 * ontology changes containing the add axioms about the subclass
	 * relationships
	 * 
	 * @param entity
	 * @return a collection of owl ontology changes containing the add axioms
	 *         about the subclasses
	 */
	private Collection<OWLOntologyChange> getSubClassesChanges(
			IAristotelianOntologyClass entity) {
		Set<OWLOntologyChange> changes = new HashSet<OWLOntologyChange>();

		OWLAxiom superClsAxiom = null;

		if (entity.getSuperClasses() == null) {
			OWLClass thing = _factory.getOWLClass(OWLRDFVocabulary.OWL_THING
					.getIRI());
			OWLClass cls = _factory.getOWLClass(IRI.create(entity.getFQName()));
			superClsAxiom = _factory.getOWLSubClassOfAxiom(cls, thing);
			changes.add(new AddAxiom(_ontology, superClsAxiom));
		} else {
			for (IAristotelianOntologyClass entityCls : entity
					.getSuperClasses()) {
				OWLClass superCls = _factory.getOWLClass(IRI.create(entityCls
						.getFQName()));
				OWLClass cls = _factory.getOWLClass(IRI.create(entity
						.getFQName()));
				superClsAxiom = _factory.getOWLSubClassOfAxiom(cls, superCls);
				changes.add(new AddAxiom(_ontology, superClsAxiom));
			}
		}

		return changes;
	}

	/**
	 * Gets the subclasses of the given entity and returns the corresponding owl
	 * ontology changes containing the add axioms about the equivalence
	 * relationships between the given primary entity class and the class
	 * defined by the restriction on the genus via the dimensions
	 * (EnumeratedProperty plus PrimaryEnumeratedClass).
	 * 
	 * @param entity
	 * @param entityCls
	 * @return a collection of owl ontology changes containing the equivalence
	 *         axioms with restriction on genus
	 * @throws AristotelianOntologyWriterException
	 */
	private void addRestrictionsOnGenera(IAristotelianOntology ao)
			throws AristotelianOntologyWriterException {

		List<OWLOntologyChange> addAxioms = new ArrayList<OWLOntologyChange>();

		for (IPrimaryEntityClass pec : ao.getGenera()) {
			createRestrictionsRecursively(pec, pec, addAxioms);
		}

		// We now use the manager to apply the change
		_manager.applyChanges(addAxioms);

	}

	private void createRestrictionsRecursively(IAristotelianOntologyClass pec,
			IPrimaryEntityClass genus, List<OWLOntologyChange> addAxioms) {

		// creates genus
		OWLClass genusCls = _factory.getOWLClass(IRI.create(genus.getFQName()));

		// OWLNamedIndividual entityClsInd =
		// _factory.getOWLNamedIndividual(IRI.create(pec.getFQName()));

		// creates Aristotelian class to be defined by restrictions on genus
		OWLClassExpression entityClsExp = _factory.getOWLClass(IRI.create(pec
				.getFQName()));

		Set<OWLClassExpression> restrictions = new HashSet<OWLClassExpression>();
		restrictions.add(genusCls);

		if (pec instanceof IPrimaryEntityClass) {

			for (IAttributeDescription attr : ((IPrimaryEntityClass) pec)
					.getAttributesDescriptions()) {

				// creates property
				OWLObjectPropertyExpression objProp = _factory
						.getOWLObjectProperty(IRI.create(attr.getProperty()
								.getFQName()));

				// creates restriction for each attribute
				for (IPrimaryEnumeratedClass enumCls : attr.getValues()) {
					// OWLNamedIndividual value =
					// _factory.getOWLNamedIndividual(IRI.create(enumCls.getFQName()));
					// OWLAxiom objPropAx =
					// _factory.getOWLObjectPropertyAssertionAxiom(objProp,
					// entityClsInd, value);
					// changes.add(new AddAxiom(_ontology, objPropAx));

					if (enumCls != null) {

						System.out.println(enumCls.getId());

						// creates value owl class
						OWLClassExpression valueCls = _factory.getOWLClass(IRI
								.create(enumCls.getFQName()));

						// creates restriction with given property and value
						OWLClassExpression someValueExp = _factory
								.getOWLObjectSomeValuesFrom(objProp, valueCls);

						restrictions.add(someValueExp);

					}
				}
			}

			OWLObjectIntersectionOf intersecAx = _factory
					.getOWLObjectIntersectionOf(restrictions);

			OWLAxiom equiAx = _factory.getOWLEquivalentClassesAxiom(
					entityClsExp, intersecAx);
			addAxioms.add(new AddAxiom(_ontology, equiAx));

			if (((IPrimaryEntityClass) pec).getSubClasses() != null) {
				for (IAristotelianOntologyClass subCls : ((IPrimaryEntityClass) pec)
						.getSubClasses())
					if (pec.isLeaf())
						return;
					else
						createRestrictionsRecursively(subCls, genus, addAxioms);
			}
		}
		return;
	}

	/**
	 * Gets the annotations of the given type for the given entity and returns
	 * the corresponding owl ontology changes containing the add axioms about
	 * annotations
	 * 
	 * @param entity
	 * @param property
	 * @return a collection of owl ontology changes containing the add axioms
	 *         about the annotations
	 */
	private Collection<OWLOntologyChange> getAnnotationsChanges(
			NamedAndCommentedEntity entity, OWLAnnotationProperty property) {
		Collection<OWLOntologyChange> changes = new HashSet<OWLOntologyChange>();

		if (property.equals(_factory.getRDFSLabel())) {
			for (String s : _languages) {
				if (entity.getLabel(s) != null) {
					// Add labels to the class in the ontology
					OWLLiteral l = _factory
							.getOWLLiteral(entity.getLabel(s), s);
					OWLAnnotation anno = _factory.getOWLAnnotation(property, l);
					OWLAxiom axiom = _factory.getOWLAnnotationAssertionAxiom(
							IRI.create(entity.getFQName()), anno);
					changes.add(new AddAxiom(_ontology, axiom));
				}
			}
			return changes;
		}

		if (property.equals(_factory.getRDFSComment())) {
			for (String s : _languages) {
				if (entity.getComment(s) != null) {
					// Add labels to the class in the ontology
					OWLLiteral l = _factory.getOWLLiteral(entity.getComment(s),
							s);
					OWLAnnotation anno = _factory.getOWLAnnotation(property, l);
					OWLAxiom axiom = _factory.getOWLAnnotationAssertionAxiom(
							IRI.create(entity.getFQName()), anno);
					changes.add(new AddAxiom(_ontology, axiom));
				}
			}
			return changes;
		}
		return null;
	}

}