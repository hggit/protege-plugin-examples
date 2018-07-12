package com.similar2.matcher.ontology.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import java.util.stream.Collectors;
import java.util.Set;
import java.util.stream.Stream;
import org.semanticweb.owlapi.search.EntitySearcher;
//import for pellet
//import org.mindswap.pellet.owlapi.PelletReasonerFactory;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLImportsDeclaration;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectIntersectionOf;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectSomeValuesFrom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.reasoner.ConsoleProgressMonitor;
import org.semanticweb.owlapi.reasoner.Node;
import org.semanticweb.owlapi.reasoner.NodeSet;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.OWLReasonerConfiguration;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;
import org.semanticweb.owlapi.reasoner.SimpleConfiguration;
import org.semanticweb.owlapi.util.DeprecatedOWLEntityCollector;
import org.semanticweb.owlapi.util.SimpleIRIMapper;
import org.semanticweb.owlapi.util.Version;
import org.semanticweb.owlapi.vocab.OWLRDFVocabulary;

//import for fact++
//import uk.ac.manchester.cs.factplusplus.owlapiv3.FaCTPlusPlusReasonerFactory;

import com.similar2.matcher.ontology.model.IAnnotations;
import com.similar2.matcher.ontology.model.IAristotelianOntology;
import com.similar2.matcher.ontology.model.IEnumeratedProperty;
import com.similar2.matcher.ontology.model.IPrimaryEntityClass;
import com.similar2.matcher.ontology.model.IPrimaryEnumeratedClass;
import com.similar2.matcher.ontology.model.exceptions.AristotelianOntologyReaderException;
import com.similar2.matcher.ontology.model.exceptions.EnumeratedPropertyRTException;
import com.similar2.matcher.ontology.model.exceptions.GenusRTException;
import com.similar2.matcher.ontology.model.exceptions.PrimaryEntityClassRTException;
import com.similar2.matcher.ontology.model.impl.Annotations;
import com.similar2.matcher.ontology.model.impl.AristotelianOntology;
import com.similar2.matcher.ontology.model.impl.EnumeratedProperty;
import com.similar2.matcher.ontology.model.impl.PrimaryEntityClass;
import com.similar2.matcher.ontology.model.impl.PrimaryEnumeratedClass;
import org.semanticweb.HermiT.Reasoner;

/**
 * An ontology reader allows to read an aristotelian ontology from a file or an inputstream.
 * 
 * @author Anthony Hombiat (Anthony.Hombiat@imag.fr)
 * @author Philippe Genoud (Philippe.Genoud@imag.fr)
 *
 */
public class OntologyReader {

	// language tags for the IAnnotations
	private String[] _languages = { "en", "fr" };

	// ontology, reasoner, manager and datafactory to manage the owl ontology
	private OWLOntology _ontology;
	private OWLReasoner _reasoner;
	private OWLOntologyManager _manager = OWLManager.createOWLOntologyManager();
	private OWLDataFactory _factory = _manager.getOWLDataFactory();

	private Map<String, OWLNamedIndividual> synonyms = new HashMap<String, OWLNamedIndividual>();

	/**
	 * 
	 */
	public OntologyReader() {
		mapIri(System.getProperty("user.dir"));
	}

	public OntologyReader(String metaOntologyAbsolutePath) {
		mapIri(metaOntologyAbsolutePath);
	}

	/**
	 * creates an IRIMapper to associate matcher meta ontology to an file
	 * (and also to do the same with the dublin core files imported by the
	 * matcher meta ontology).
	 *  
	 * @param metaOntologyAbsolutePath path of the folder containing the meta 
	 *        ontology file
	 */
	private void mapIri(String metaOntologyAbsolutePath) {
		// Mapping from a local file matcher meta ontology
		// which is imported in the wardrobe ontology but
		// not available on the internet until now.

		File metaOntFile = new File(metaOntologyAbsolutePath,
				"ontologies/ontology.owl");
		IRI metaOntIri = AristotelianOntologyVocabulary.META_MODEL_ONTOLOGY_IRI;
		_manager.addIRIMapper(new SimpleIRIMapper(metaOntIri, IRI
				.create(metaOntFile)));
		
		File dcTermsFile = new File(metaOntologyAbsolutePath,
		"ontologies/dcterms.rdf");
		File dcElementsFile = new File(metaOntologyAbsolutePath,
		"ontologies/dcelements.rdf");
		_manager.addIRIMapper(new SimpleIRIMapper(AristotelianOntologyVocabulary.DC_TERMS_IRI, IRI
				.create(dcTermsFile)));
		_manager.addIRIMapper(new SimpleIRIMapper(AristotelianOntologyVocabulary.DC_ELEMENTS_IRI, 
				IRI.create(dcElementsFile)));
	}

	/**
	 * Loads and aristotelian ontology from a OWLOntology object
	 * 
	 * @param ontology
	 *            the OWLOntology object containing the aristotelian ontology
	 *            description in OWL
	 * @return the IAristotelianOntology object representing the aristotelian
	 *         ontology
	 * @throws AristotelianOntologyReaderException
	 * @throws GenusRTException
	 */
	public IAristotelianOntology loadAristotelianOntology(OWLOntology ontology,
			ReasonerType reasonerType) throws GenusRTException,
			AristotelianOntologyReaderException {

		String ontoIRI = ontology.getOntologyID().getOntologyIRI().toString();
		IAristotelianOntology aristotelianOntology = 
			new AristotelianOntology(ontoIRI,_manager.getOntologyFormat(ontology));
		this._ontology = ontology;

		initReasoner(reasonerType);

		loadImports(aristotelianOntology);
		/*
		 * builds the taxonomy of classes Node<OWLClass> thingNode =
		 * reasoner.getTopClassNode(); buildHierarchy(thingNode, reasoner, 0);
		 */

		loadSynonymousEntityClasses(aristotelianOntology);
		// WARNING : the order of loading is important!
		// Primary entity classes and primary enumerated classes
		// must be loaded before enumerated properties because
		// they are used respectively as domain and range of enumerated
		// properties
		loadValueHierarchies(aristotelianOntology);
		 loadPrimaryEntityClasses(aristotelianOntology);
		loadEntitiesHierarchies(aristotelianOntology);

		// stop();
		loadEnumeratedProperties(aristotelianOntology);
		loadAristotelianDescriptions(aristotelianOntology);

		aristotelianOntology.propagateAttributes();
		
		aristotelianOntology.setOntologyIRI(ontology.getOntologyID()
				.getOntologyIRI().toString());
		
		return aristotelianOntology;
	}

	// private void stop() {
	// Scanner sc = new Scanner(System.in);
	// System.out.print("return to ,continue...");
	// sc.nextLine();
	// }

	/**
	 * Loads and aristotelian ontology from an inputstream
	 * 
	 * @param is
	 *            an inputstream containing an aristotelian ontology description
	 *            in owl
	 * @return the IAristotelianOntology object representing the aristotelain
	 *         ontology
	 * 
	 * @throws OWLOntologyCreationException
	 *             if an error as occurred during the file reading
	 */
	public IAristotelianOntology loadAristotelianOntology(InputStream is,
			ReasonerType reasonerType)
			throws AristotelianOntologyReaderException {

		try {
			return loadAristotelianOntology(
					_manager.loadOntologyFromOntologyDocument(is), reasonerType);
		} catch (OWLOntologyCreationException e) {
			throw new AristotelianOntologyReaderException(e.getMessage(), e);
		}
	}

	/**
	 * Loads and aristotelian ontology from an inputstream
	 * 
	 * @param is
	 *            an inputstream containing an aristotelian ontology description
	 *            in owl
	 * @return the IAristotelianOntology object representing the aristotelain
	 *         ontology
	 * 
	 * @throws OWLOntologyCreationException
	 *             if an error as occurred during the file reading
	 */
	public IAristotelianOntology loadAristotelianOntology(String fileName,
			InputStream is, ReasonerType reasonerType)
			throws AristotelianOntologyReaderException {

		try {
			IAristotelianOntology ao = loadAristotelianOntology(
					_manager.loadOntologyFromOntologyDocument(is), reasonerType);
			ao.setFileName(fileName);
			return ao;
		} catch (OWLOntologyCreationException e) {
			throw new AristotelianOntologyReaderException(e.getMessage(), e);
		}
	}

	/**
	 * Loads an aristotelian ontology from a file
	 * 
	 * @param path
	 *            the path of the file
	 * @param fileName
	 *            the name of the file
	 * @param reasonerType
	 * @return the aristotelian ontology loaded
	 * @throws AristotelianOntologyReaderException
	 *             if for any reason the loading has failed.
	 */
	public IAristotelianOntology loadAristotelianOntology(String filePath,
			String fileName, ReasonerType reasonerType)
			throws AristotelianOntologyReaderException {

		try {
			File f = new File(filePath, fileName);
			FileInputStream is = new FileInputStream(f);
			IAristotelianOntology ao = loadAristotelianOntology(f.getName(),
					is, reasonerType);
			return ao;
		}  catch (FileNotFoundException e) {
			throw new AristotelianOntologyReaderException(e.getMessage(), e);
		}
	}

	/**
	 * Loads and aristotelian ontology from an iri
	 * 
	 * @param iri
	 *            an iri pointing to an online aristotelian ontology description
	 *            in owl
	 * @return the IAristotelianOntology object representing the aristotelain
	 *         ontology
	 * 
	 * @throws OWLOntologyCreationException
	 *             if an error as occurred during the file reading
	 */
	public IAristotelianOntology loadAristotelianOntology(String iri,
			ReasonerType reasonerType)
			throws AristotelianOntologyReaderException {

		try {

			IRI ontIri = IRI.create(iri);

			IAristotelianOntology ao = loadAristotelianOntology(
					_manager.loadOntologyFromOntologyDocument(ontIri),
					reasonerType);
		    String decodedIRI = URLDecoder.decode(iri, "UTF-8");
			String[] splittedIri = decodedIRI.split("/");
			String fileName = splittedIri[splittedIri.length - 1];
			ao.setFileName(fileName);
			return ao;
			
		} catch (OWLOntologyCreationException e) {
			throw new AristotelianOntologyReaderException(e.getMessage(), e);
		} catch (UnsupportedEncodingException e) {
			throw new AristotelianOntologyReaderException(e.getMessage(), e);
		}
	}
	
	/**
	 * adds all the imports contained in the given OWL ontology to
	 * the given Aristotelian ontology.
	 * 
	 * @param ao the aristotelian onotology
	 */
	private void loadImports(IAristotelianOntology ao) {
		for (OWLImportsDeclaration importDecl : _ontology.getImportsDeclarations()) {
			ao.addImport(importDecl.getIRI());
		}
	}

	// -----------------------------------------------------------------------
	// Enumerated properties
	// -----------------------------------------------------------------------
	/**
	 * adds all the EnumeratedProperties contained in the given OWL ontology to
	 * the given Aristotelian ontology.
	 * 
	 * @param ao
	 *            the aristotelian onotology
	 */
	private void loadEnumeratedProperties(IAristotelianOntology ao) {

		OWLClass enumeratedProperty = AristotelianOntologyVocabulary.ENUMERATED_PROPERTY
				.getOWLClass();
		OWLClass intervalEnumeratedProperty = AristotelianOntologyVocabulary.INTERVAL_ENUMERATEDPROPERTY
				.getOWLClass();

		Set<OWLNamedIndividual> set = _reasoner.getInstances(
				enumeratedProperty, true).getFlattened();
		set.addAll(_reasoner.getInstances(intervalEnumeratedProperty, true)
				.getFlattened());

		for (OWLNamedIndividual ni : set) {
			// IEnumeratedProperty enumProperty = new EnumeratedProperty(
			// ni.getIRI().getNamespace(),
			// ni.getIRI().getFragment());
			//
			// OWLAnnotationProperty o = _factory
			// .getOWLAnnotationProperty(ni.getIRI());
			// IRI domainIri = _ontology
			// .getAnnotationPropertyDomainAxioms(o).iterator().next().getDomain();
			//
			// IRI rangeIri = _ontology
			// .getAnnotationPropertyRangeAxioms(o).iterator().next().getRange();

			OWLObjectProperty prop = _factory.getOWLObjectProperty(ni.getIRI());

			// create a description for the domain
			String domainName = new String();
			String domainNameSpace = null;
			for (OWLClassExpression desc : EntitySearcher.getDomains(prop,_ontology)) {
				// domainName += desc.toString(); // OWL API 2.0
				domainName += desc.asOWLClass().getIRI().getFragment();
				domainNameSpace = desc.asOWLClass().getIRI().getNamespace();
			}

			// create a description for the range
			String rangeName = new String();
			String rangeNameSpace = null;
			for (OWLClassExpression desc : EntitySearcher.getRanges(prop,_ontology)) {
				// domainName += desc.toString(); // OWL API 2.0
				rangeName += desc.asOWLClass().getIRI().getFragment();
				rangeNameSpace = desc.asOWLClass().getIRI().getNamespace();
			}

			// TODO temporary, maybe have to add thing in any Aristotelian
			// ontology
			IPrimaryEntityClass domain = null;
			if (!domainName.equals(OWLRDFVocabulary.OWL_THING.getIRI()
					.getFragment())) {
				domain = ao.getPrimaryEntityClass(domainNameSpace, domainName);
			} else {
				domain = new PrimaryEntityClass(ao,OWLRDFVocabulary.OWL_THING
						.getIRI().getNamespace(), OWLRDFVocabulary.OWL_THING
						.getIRI().getFragment());
			}

			IPrimaryEnumeratedClass range = null;
			if (!rangeName.equals(OWLRDFVocabulary.OWL_THING.getIRI()
					.getFragment())) {
				range = ao.getPrimaryEnumeratedClass(rangeNameSpace, rangeName);
			} else {
				range = new PrimaryEnumeratedClass(ao,OWLRDFVocabulary.OWL_THING
						.getIRI().getNamespace(), OWLRDFVocabulary.OWL_THING
						.getIRI().getFragment());
			}

			IEnumeratedProperty enumProperty = new EnumeratedProperty(ao,prop
					.getIRI().getNamespace(), prop.getIRI().getFragment());

			enumProperty.setDomain(domain);
			enumProperty.setRange(range);
			System.out.println("domain : " + domain);
			enumProperty.setFunctional(EntitySearcher.isFunctional(prop,_ontology));

			// add labels
			IAnnotations labels = getAnnotations(
					_factory.getOWLClass(ni.getIRI()), _factory.getRDFSLabel());
			if (labels != null)
				enumProperty.setLabels(labels);

			// add comments
			IAnnotations comments = getAnnotations(
					_factory.getOWLClass(ni.getIRI()),
					_factory.getRDFSComment());
			if (comments != null)
				enumProperty.setComments(comments);

			ao.addEnumeratedProperty(enumProperty);
		}

	}

	// -----------------------------------------------------------------------
	// Primary Entity classes
	// -----------------------------------------------------------------------
	public void loadSynonymousEntityClasses(IAristotelianOntology ao) {

		OWLClass primaryEntityClass = AristotelianOntologyVocabulary.SYNONYM_ENTITY_CLASS
				.getOWLClass();
		for (Node<OWLNamedIndividual> n : _reasoner.getInstances(
				primaryEntityClass, true)) {

			for (OWLNamedIndividual ni : n.getEntities()) {
				synonyms.put(ni.getIRI().toString(), ni);
			}
		}
	}

	/**
	 * adds all the PrimaryEntityClasses contained in the given OWL ontology to
	 * the given Aristotelian ontology.
	 * 
	 * @param ao
	 *            the aristotelian onotology
	 */
	public void loadPrimaryEntityClasses(IAristotelianOntology ao) {
		OWLClass primaryEntityClass = AristotelianOntologyVocabulary.PRIMARY_ENTITY_CLASS
				.getOWLClass();
		// TODO this code is never called
		for (Node<OWLNamedIndividual> n : _reasoner.getInstances(
				primaryEntityClass, true)) {
			for (OWLNamedIndividual ni : n.getEntities()) {

				// if (ni.getIRI().getFragment().startsWith("Moly")) {
				// System.out.println("PRIMARY STOP");
				// System.out.println(ni.getIRI());
				// stop();
				// }
				IPrimaryEntityClass ipec = new PrimaryEntityClass(ao,ni.getIRI()
						.getNamespace(), ni.getIRI().getFragment());

				// add labels
				IAnnotations labels = getAnnotations(
						_factory.getOWLClass(ni.getIRI()),
						_factory.getRDFSLabel());
				if (labels != null)
					ipec.setLabels(labels);

				// add comments
				IAnnotations comments = getAnnotations(
						_factory.getOWLClass(ni.getIRI()),
						_factory.getRDFSComment());
				if (comments != null)
				ipec.setComments(comments);

				ao.addPrimaryEntityClass(ipec);

				if (isRoot(ni))
					ao.addGenus(ipec);
			}
		}
	}

	private void loadEntitiesHierarchies(IAristotelianOntology ao)
			throws GenusRTException, AristotelianOntologyReaderException {
		OWLClass primaryEntityClass = AristotelianOntologyVocabulary.PRIMARY_ENTITY_CLASS
				.getOWLClass();

		for (Node<OWLNamedIndividual> n : _reasoner.getInstances(
				primaryEntityClass, true)) {
			for (OWLNamedIndividual ni : n.getEntities()) {
				if (isRoot(ni)) {
					OWLClass entitiesHierarchyRoot = _factory.getOWLClass(ni
							.getIRI());
					ao.addGenus(createPrimaryEntitiesHierarchy(
							entitiesHierarchyRoot, null, ao));
				}
			}
		}
	}

	/**
	 * Constructs the aristotelian description of all the entity classes
	 * 
	 * @param aristotelianOntology
	 * @throws AristotelianOntologyReaderException
	 */
	private void loadAristotelianDescriptions(
			IAristotelianOntology aristotelianOntology)
			throws AristotelianOntologyReaderException {
		for (IPrimaryEntityClass entityClass : aristotelianOntology
				.getPrimaryEntityClasses()) {
			// load the aristotelian description of the class
			loadAristotelianDescription(entityClass, aristotelianOntology);
		}
	}

	private IPrimaryEntityClass createPrimaryEntitiesHierarchy(
			OWLClass owlRootClass, IPrimaryEntityClass superClass,
			IAristotelianOntology ao)
			throws AristotelianOntologyReaderException {

//		if (owlRootClass.getIRI().getFragment().equals("generic_mudstone")) {
//			System.out.println("found generic_mudstone");
//		}

		// look in the aristotelian ontology if there is all ready a Primary
		// entity class corresponding to owlRootClass
		IPrimaryEntityClass pec = ao.getPrimaryEntityClass(owlRootClass
				.getIRI().getNamespace(), owlRootClass.getIRI().getFragment());
		if (pec == null) {
			// create a new Primary Entity class corresponding to
			// owlRootClass root and
			// add it to ao
			pec = new PrimaryEntityClass(ao,owlRootClass.getIRI().getNamespace(),
					owlRootClass.getIRI().getFragment(), superClass);

			// Add the labels to the Primary Entity Class just created
			IAnnotations labels = getAnnotations(owlRootClass,
					_factory.getRDFSLabel());
			if (labels != null)
				pec.setLabels(labels);

			// Add the comments to the Primary Entity Class just created
			IAnnotations comments = getAnnotations(owlRootClass,
					_factory.getRDFSComment());
			if (comments != null)
				pec.setComments(comments);

			// Add the dc:description comments to the Primary Entity Class just created
			IAnnotations dcComments = getAnnotations(owlRootClass,
					_factory.getOWLAnnotationProperty(IRI.create("http://purl.org/dc/elements/1.1/description")));
			if (dcComments != null) {
				pec.addComments(dcComments);
			}

			
			ao.addPrimaryEntityClass(pec);
			// recursively traverse root's children
			for (Node<OWLClass> child : _reasoner.getSubClasses(owlRootClass,
					true)) {
				if (!child.isBottomNode()) { // child != owl:Nothing

					// filter the synonymous classes in the node
					OWLClass primaryEntity = null;
					for (OWLClass c : child.getEntities()) {
						if (synonyms.get(c.getIRI().toString()) == null) {
							// c is not a synonymous class. We assume it is an
							// primary entity class
							primaryEntity = c;
						}
					}
					if (primaryEntity == null) {
						throw new AristotelianOntologyReaderException(
								"No primary entity class for synonym class "
										+ child.getRepresentativeElement()
												.getIRI() + " and its synonyms");
					}
					// recurse with primaryEntity
					createPrimaryEntitiesHierarchy(primaryEntity, pec, ao);
					// createPrimaryEntitiesHierarchy(
					// child.getRepresentativeElement(), pec, ao);
				}
			}
		} else {
			// root is already stored in the ontology
			// just link it with it's parent
			pec.addSuperClass(superClass);
		}
		return pec;

	}

	/**
	 * builds the model of an aristotelian class definition from OWL class.
	 * 
	 * @param entityClass
	 * @param ao
	 *            the Aristotelian ontology in which to load the class
	 *            description
	 * @throws AristotelianOntologyReaderException
	 */
	private void loadAristotelianDescription(IPrimaryEntityClass entityClass,
			IAristotelianOntology ao)
			throws AristotelianOntologyReaderException {

		OWLClass clazz = _factory.getOWLClass(IRI.create(entityClass
				.getFQName()));

		for (OWLClassExpression desc : EntitySearcher.getEquivalentClasses(clazz,_ontology)) {

			if (isAnAristotelianDescription(desc)) {

				// create an IAClassDescription from the class definition as
				// an OWL Object Intersection

				OWLObjectIntersectionOf objIntersection = (OWLObjectIntersectionOf) desc;

				// adds the attributes descriptions to the class description
				OWLClass _genus = null;
				Set<OWLClassExpression> operands = objIntersection
						.getOperands();
				for (OWLClassExpression op : operands) {
					if (op instanceof OWLClass)
						if (_genus == null) {
							_genus = (OWLClass) op;
						} else {
							throw new PrimaryEntityClassRTException("class "
									+ clazz.getIRI() + "is not aristetolian ");
						}
					else if (op instanceof OWLObjectSomeValuesFrom) {
						// classDescr.addAttribute(createAttributeDescription(
						// (OWLObjectSomeValuesFrom) op, languages));
						loadAttributeDescription((OWLObjectSomeValuesFrom) op,
								entityClass, ao);
					} else {
						throw new PrimaryEntityClassRTException("class "
								+ clazz.getIRI() + "is not aristetolian ");
					}
				}

				return; // an aristotelian definition has been found (there
				// should be no other)
			}
		}

	}

	/**
	 * adds an entity class the description of one of its attribute defined by
	 * an OWL Object Some Restriction
	 * 
	 * @param op
	 *            the OWL Object Some Restriction
	 * @param entityClass
	 *            the entity class to which the attribute description is added
	 * @param ao
	 *            the aristotelian ontology the class belongs to
	 * @throws AristotelianOntologyReaderException
	 */
	private void loadAttributeDescription(OWLObjectSomeValuesFrom op,
			IPrimaryEntityClass entityClass, IAristotelianOntology ao)
			throws AristotelianOntologyReaderException {

		// find the enumerated property involved
		IEnumeratedProperty property = ao.getEnumeratedProperty(op
				.getProperty().asOWLObjectProperty().getIRI().toString());
		if (property == null) {
			throw new EnumeratedPropertyRTException(
					"unknown enumerated property "
							+ op.getProperty().asOWLObjectProperty().getIRI()
									.toString());
		}

		// // TODO trace temporaire qu'il faudra supprimer ensuite
		// System.out.println("   Enumerated property : "
		// + op.getProperty().asOWLObjectProperty().getIRI().toString());

		// constructs the list of values
		List<IPrimaryEnumeratedClass> values = new ArrayList<IPrimaryEnumeratedClass>();

		Set<OWLEntity> set = new HashSet<OWLEntity>();
		DeprecatedOWLEntityCollector collector = new DeprecatedOWLEntityCollector(set);
		collector.setCollectClasses(true);
		op.getFiller().accept(collector);
		for (OWLEntity ent : set) {
			if (ent.getIRI().toString()
					.startsWith("http://org.semanticweb.owlapi/error#Error")) {
				System.out.println("error " + ent.getIRI().toString());
				throw new AristotelianOntologyReaderException("OWL API ERROR "
						+ ent.getIRI().toString());
			}
			// find the Primary Enumerated Value description
			IPrimaryEnumeratedClass value = ao.getPrimaryEnumeratedClass(ent
					.getIRI().toString());
			// System.out.println("         valeur : " +
			// ent.getIRI().toString());

			if (value != null)
				values.add(value);
			else {
				// HACK for minerals-500.owl ontology for missing values
				value = new PrimaryEnumeratedClass(ao,ent.getIRI().getNamespace(), ent
						.getIRI().getFragment());
				if(property.getRange()!=null)property.getRange().addSubClass(value);
				values.add(value);
			}
		}

		entityClass.addAttributeDescription(property, values);

	}

	/**
	 * tests if this OWLClassExpression corresponds to an intersection of Genus
	 * class and a list of SomeValuesFrom restrictions
	 * 
	 * @param desc
	 *            the description
	 * @return true if the description corresponds to an intersection of Genus
	 *         class and a list of SomeValuesFrom restrictions
	 */
	private boolean isAnAristotelianDescription(OWLClassExpression desc) {

		// TODO verifier si cette fonction est nécessaire
		if (!(desc instanceof OWLObjectIntersectionOf)) {
			return false;
		}

		OWLClass _genus = null;
		OWLObjectIntersectionOf objIntersection = (OWLObjectIntersectionOf) desc;
		Set<OWLClassExpression> operands = objIntersection.getOperands();
		for (OWLClassExpression op : operands) {
			if (op instanceof OWLClass)
				if (_genus == null) {
					_genus = (OWLClass) op;
				} else {
					return false;
				}
			else if (!(op instanceof OWLObjectSomeValuesFrom)) {
				return false;
			}
		}
		return true;
	}

	// -----------------------------------------------------------------------
	// Values hierarchies
	// -----------------------------------------------------------------------

	/**
	 * adds the root PrimaryEnumeratedClasses contained in the given OWL
	 * ontology to the given Aristotelian ontology. For each root
	 * PrimaryEnumeratedClass, the whole value hierarchy is constructed (that is
	 * , all subclasses (PrimaryEnumeratedClasses) of the root class are
	 * recursively added to the classes tree of the root class).
	 * 
	 * @param ao
	 *            the Aristotelian ontology
	 */
	private void loadValueHierarchies(IAristotelianOntology ao) {
		OWLClass primaryEnumeratedClass = AristotelianOntologyVocabulary.PRIMARY_ENUMERATED_CLASS
				.getOWLClass();

		for (Node<OWLNamedIndividual> n : _reasoner.getInstances(
				primaryEnumeratedClass, true)) {
			for (OWLNamedIndividual ni : n.getEntities()) {
				if (isRoot(ni)) {
					OWLClass valuesHierarchyRoot = _factory.getOWLClass(ni
							.getIRI());
					ao.addValuesHierarchy(createValuesHierarchy(
							valuesHierarchyRoot, null, ao));
				}
			}
		}
	}

	/**
	 * Recursively constructs the DAG (direct acyclic graph) of Primary
	 * Enumerated classes corresponding to a hierarchy of owl classes
	 * 
	 * @param owlRootClass
	 *            the root of the hierarchy of owl classes
	 * @param superClass
	 *            Primary Enumerated class that will be parent of the primary
	 *            enumerated class corresponding to owlRootClass
	 * @param ao
	 *            the aristotelian ontology
	 * 
	 * @return the primary enumerated class corresponding to owlRootClass
	 */
	private IPrimaryEnumeratedClass createValuesHierarchy(
			OWLClass owlRootClass, IPrimaryEnumeratedClass superClass,
			IAristotelianOntology ao) {

		// look in the aristotelian ontology if there is all ready a Primary
		// Enumerated class corresponding to owlRootClass
		IPrimaryEnumeratedClass pec = ao.getPrimaryEnumeratedClass(owlRootClass
				.getIRI().getNamespace(), owlRootClass.getIRI().getFragment());
		if (pec == null) {
			// create a new Primary Enumerated class corresponding to
			// owlRootClass root and
			// add it to ao
			pec = new PrimaryEnumeratedClass(ao,owlRootClass.getIRI().getNamespace(),
					owlRootClass.getIRI().getFragment(), superClass);

			// Add the labels to the Primary Enumerated Class just created
			IAnnotations labels = getAnnotations(owlRootClass,
					_factory.getRDFSLabel());
			if (labels != null)
				pec.setLabels(labels);

			// Add the comments to the Primary Enumerated Class just created
			IAnnotations comments = getAnnotations(owlRootClass,
					_factory.getRDFSComment());
			if (comments != null)
				pec.setComments(comments);

			ao.addPrimaryEnumeratedClass(pec);
			// recursively traverse root's children
			for (Node<OWLClass> child : _reasoner.getSubClasses(owlRootClass,
					true)) {
				if (!child.isBottomNode()) // child != owl:Nothing
					createValuesHierarchy(child.getRepresentativeElement(),
							pec, ao);
			}
		} else {
			// root is already stored in the ontology
			// just link it with it's parent
			pec.addSuperClass(superClass);
		}
		return pec;

	}

	// -----------------------------------------------------------------------
	// utilities
	// -----------------------------------------------------------------------
	/**
	 * get the Annotations of a given type corresponding to a given owl class
	 * 
	 * @param clazz
	 *            the class whose annotations we want
	 * @param annotation
	 *            the type of annotation we want
	 * @return
	 */
	public IAnnotations getAnnotations(OWLClass clazz,
			OWLAnnotationProperty annotation) {

		IAnnotations annotations = null;
		if (_languages != null && _languages.length > 1) {
			for (OWLAnnotation a : EntitySearcher.getAnnotations(clazz,_ontology, annotation).stream().collect(Collectors.toSet())) {
				if (a.getValue() instanceof OWLLiteral) {
					OWLLiteral val = (OWLLiteral) a.getValue();
					// if it's a RDFplain literal we can easily get the language
					// tag
					if (val.isRDFPlainLiteral()) {
						for (String language : _languages) {
							if (val.hasLang(language)) {
								if (annotations == null)
									annotations = new Annotations();
								annotations.addAnnotation(language,
										val.getLiteral());
							}
						}
					}
					// Sometimes RDFplain literals don't have a language tag, so we must always check default.
					if (annotations == null)
						annotations = new Annotations();
					annotations.addAnnotation("default", val.getLiteral());
				}
			}
		}
		return annotations;
	}

	/**
	 * Return true if the given individual has owl THING as parent, false
	 * otherwise
	 * 
	 * @param namedIndividual
	 * @return boolean
	 */
	public boolean isRoot(OWLNamedIndividual namedIndividual) {
		OWLClass namedIndClass = _factory.getOWLClass(namedIndividual.getIRI());
		NodeSet<OWLClass> parents = _reasoner.getSuperClasses(namedIndClass,
				true);
		Set<OWLClass> setOfClasses = parents.getFlattened();
		if ((setOfClasses.size() != 1))
			return false; // clazz has more than one parent
		OWLClass parent = setOfClasses.iterator().next();
		OWLClass thingClass = _manager.getOWLDataFactory().getOWLClass(
				OWLRDFVocabulary.OWL_THING.getIRI());
		if (parent == thingClass)
			return true;
		return false;
	}

	/**
	 * Return true if the given individual has owl NOTHING as child, false
	 * otherwise
	 * 
	 * @param namedIndividual
	 * @return boolean
	 */
	public boolean isLeaf(OWLNamedIndividual namedIndividual) {
		OWLClass namedIndClass = _factory.getOWLClass(namedIndividual.getIRI());
		NodeSet<OWLClass> parents = _reasoner
				.getSubClasses(namedIndClass, true);
		Set<OWLClass> setOfClasses = parents.getFlattened();
		if ((setOfClasses.size() != 1))
			return false; // clazz has more than one parent
		OWLClass parent = setOfClasses.iterator().next();
		OWLClass nothingClass = _factory
				.getOWLClass(OWLRDFVocabulary.OWL_NOTHING.getIRI());
		if (parent == nothingClass)
			return true;
		return false;
	}

	/**
	 * inits the DL reasoner and classifies the ontology
	 * 
	 * @param ontology
	 *            the ontology
	 * @param type
	 *            reasoner type : HermiT, Pellet or FaCT++
	 * @throws Exception
	 */
	private void initReasoner(ReasonerType type) {
		OWLReasonerFactory reasonerFactory = null;
		// HermiT
		if (type == ReasonerType.HERMIT) {
			reasonerFactory = new org.semanticweb.HermiT.Reasoner.ReasonerFactory();
		}
		// FaCT++
		// else if (type == ReasonerType.FACT) {
		// throw new
		// UnsupportedOperationException("FACT++ reasoner not yet supported");
		// reasonerFactory = new
		// uk.ac.manchester.cs.factplusplus.owlapiv3.FaCTPlusPlusReasonerFactory();
		// }
		// Pellet
		// } else {
		// reasonerFactory = new
		// org.mindswap.pellet.owlapi.PelletReasonerFactory();
		// }

		// Create an instance of a buffering (default kind of resoner)
		// OWLReasoner.
		// Attach a progress monitor to the reasoner and set up a configuration
		// that
		// knows about a progress monitor.
		// This will print the reasoner progress out to the console.
		ConsoleProgressMonitor progressMonitor = new ConsoleProgressMonitor();
		OWLReasonerConfiguration config = new SimpleConfiguration(
				progressMonitor);

		// Create a reasoner that will reason over our ontology and its
		// imports closure. Pass in the configuration.
		_reasoner = reasonerFactory.createReasoner(_ontology, config);
		//System.out.println("init reasoner " + getReasonerName() + "."
			//	+ getReasonerVersion());

		// Ask the reasoner to do all the necessary work
		_reasoner.precomputeInferences();

		// OWLClass cl =
		// _factory.getOWLClass(IRI.create("http://www.similarto.com/ontologies/minerals/2010/08/mineralsAT#Molybdate"));
		//
	}

	public String getReasonerName() {
		return _reasoner.getReasonerName().toString();
	}

	public String getReasonerVersion() {
		Version reasonerVersion = _reasoner.getReasonerVersion();
		return reasonerVersion.getMajor() + "." + reasonerVersion.getMinor()
				+ "." + reasonerVersion.getPatch() + "."
				+ reasonerVersion.getBuild();
	}

	public String getBaseUri() {
		return _ontology.getOntologyID().getOntologyIRI().toString();
	}

	public String getNameSpace() {
		return getBaseUri() + "#";
	}

}