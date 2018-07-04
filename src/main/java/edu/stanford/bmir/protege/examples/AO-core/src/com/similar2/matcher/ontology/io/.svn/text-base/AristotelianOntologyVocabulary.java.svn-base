package com.similar2.matcher.ontology.io;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;

/**
 * Collection of constants representing the different entities defined in the
 * ontology http://www.similarto.com/ontologies/matcher/2010/06/ontology.owl
 * 
 * @author Philippe Genoud (Philippe.Genoud@imag.fr)
 */
public enum AristotelianOntologyVocabulary {

	PRIMARY_ENUMERATED_CLASS("PrimaryEnumeratedClass"),

	PRIMARY_ENTITY_CLASS("PrimaryEntityClass"),
	
	SYNONYM_ENTITY_CLASS("SynonymEntityClass"),

	ENUMERATED_PROPERTY("EnumeratedProperty"), 
	
	INTERVAL_ENUMERATEDPROPERTY("IntervalEnumeratedProperty");

	/**
	 * Name space of the Aristotelian metamodel ontology.
	 */
	public static final String NAME_SPACE = "http://www.similarto.com/ontologies/matcher/2010/06/ontology";
	
	
	/**
	 * IRI of the dublin core files used by the meta model ontology
	 */
	public static final IRI DC_ELEMENTS_IRI = IRI.create("http://purl.org/dc/elements/1.1/");
	public static final IRI DC_TERMS_IRI = IRI.create("http://purl.org/dc/terms/");
	/**
	 * IRI of the Aristotelian metamodel ontology
	 */
	public static final IRI META_MODEL_ONTOLOGY_IRI = IRI.create(NAME_SPACE);
	
	/**
	 * OWL API IRI associated with the enum constant.
	 */
	private final IRI iri;

	/** OWL API class associated with the enum constant (if applicable). */
	private final OWLClass owlClass;

	/**
	 * @return OWL API IRI associated with the enum constant.
	 */
	public IRI getIRI() {
		return this.iri;
	}
	
	/**
	 * 
	 * @return the OWL API class corresponding to the enum constant.
	 */
	public OWLClass getOWLClass() {
			return this.owlClass;
	}

	/**
	 * @param name of the enum constant;
	 */
	private AristotelianOntologyVocabulary(final String name) {
		this.iri = IRI.create(NAME_SPACE + "#" + name);
		this.owlClass = OWLManager.getOWLDataFactory().getOWLClass(this.iri);
	}

}
