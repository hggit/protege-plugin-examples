package edu.stanford.bmir.protege.examples.view;

import java.awt.event.ActionListener;
import java.util.*;
import java.awt.BorderLayout;
import javax.swing.*;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JSeparator;
import javax.swing.JScrollPane;
import javax.swing.BoxLayout;
import javax.swing.table.TableModel;
import javax.swing.table.DefaultTableModel;

import org.semanticweb.owlapi.util.OWLClassExpressionVisitorAdapter;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;
import org.semanticweb.owlapi.model.OWLAnnotationValue;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.protege.editor.owl.model.OWLModelManager;
import org.protege.editor.owl.model.event.EventType;
import org.protege.editor.owl.model.event.OWLModelManagerListener;
import org.semanticweb.owlapi.search.EntitySearcher;

import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClassAxiom;
import org.semanticweb.owlapi.model.OWLClassExpression;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.io.OWLXMLOntologyFormat;
import org.semanticweb.owlapi.io.StreamDocumentTarget;
import org.semanticweb.owlapi.io.StringDocumentSource;
import org.semanticweb.owlapi.io.StringDocumentTarget;
import org.semanticweb.owlapi.model.AddAxiom;
import org.semanticweb.owlapi.model.AddOntologyAnnotation;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLAnnotationObjectVisitorEx;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassAssertionAxiom;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLDataPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLDataPropertyRangeAxiom;
import org.semanticweb.owlapi.model.OWLDataRange;
import org.semanticweb.owlapi.model.OWLDatatype;
import org.semanticweb.owlapi.model.OWLDatatypeDefinitionAxiom;
import org.semanticweb.owlapi.model.OWLDatatypeRestriction;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLException;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLObjectPropertyExpression;
import org.semanticweb.owlapi.model.OWLObjectSomeValuesFrom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLSubClassOfAxiom;
import org.semanticweb.owlapi.model.PrefixManager;
import org.semanticweb.owlapi.model.RemoveAxiom;
import org.semanticweb.owlapi.model.SWRLAtom;
import org.semanticweb.owlapi.model.SWRLClassAtom;
import org.semanticweb.owlapi.model.SWRLObjectPropertyAtom;
import org.semanticweb.owlapi.model.SWRLRule;
import org.semanticweb.owlapi.model.SWRLVariable;
import org.semanticweb.owlapi.profiles.OWL2DLProfile;
import org.semanticweb.owlapi.profiles.OWLProfileReport;
import org.semanticweb.owlapi.profiles.OWLProfileViolation;
import org.semanticweb.owlapi.reasoner.ConsoleProgressMonitor;
import org.semanticweb.owlapi.reasoner.InferenceType;
import org.semanticweb.owlapi.reasoner.Node;
import org.semanticweb.owlapi.reasoner.NodeSet;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.OWLReasonerConfiguration;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;
import org.semanticweb.owlapi.reasoner.SimpleConfiguration;
import org.semanticweb.owlapi.reasoner.structural.StructuralReasonerFactory;
import org.semanticweb.owlapi.util.AutoIRIMapper;
import org.semanticweb.owlapi.util.DefaultPrefixManager;
import org.semanticweb.owlapi.util.InferredAxiomGenerator;
import org.semanticweb.owlapi.util.InferredOntologyGenerator;
import org.semanticweb.owlapi.util.InferredSubClassAxiomGenerator;
import org.semanticweb.owlapi.util.OWLClassExpressionVisitorAdapter;
import org.semanticweb.owlapi.util.OWLEntityRemover;
import org.semanticweb.owlapi.util.OWLObjectVisitorExAdapter;
import org.semanticweb.owlapi.util.OWLOntologyMerger;
import org.semanticweb.owlapi.util.OWLOntologyWalker;
import org.semanticweb.owlapi.util.OWLOntologyWalkerVisitor;
import org.semanticweb.owlapi.util.SimpleIRIMapper;
import org.semanticweb.owlapi.vocab.OWL2Datatype;
import org.semanticweb.owlapi.vocab.OWLFacet;
import org.semanticweb.owlapi.vocab.OWLRDFVocabulary;


public class DisplayAttributes extends JPanel {

    //private JButton refreshButton = new JButton("Refresh");

    private JLabel className = new JLabel();
    private JTextArea description;
    private JLabel superClass = new JLabel();
    private JTable differentia;
    private JTable inherited;
    
    private OWLModelManager modelManager;


    /*private ActionListener refreshAction = e -> recalculate();
    
    private OWLModelManagerListener modelListener = event -> {
        if (event.getType() == EventType.ACTIVE_ONTOLOGY_CHANGED) {
            refresh(getOWLWorkspace().getOWLSelectionModel().getLastSelectedClass());
        }
    };*/
    
    public DisplayAttributes(OWLModelManager modelManager, OWLClass cla) {
    	this.modelManager = modelManager;        
        //modelManager.addListener(modelListener);
        //refreshButton.addActionListener(refreshAction);
        BoxLayout boxlayout = new BoxLayout(this, BoxLayout.Y_AXIS);
        setLayout(boxlayout);
        
        add(className);
        //add(refreshButton);
        
        description = new JTextArea();
        description.setEditable(false);
        description.setLineWrap(true);
        description.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(description);
        //setPreferredSize(new Dimension(450, 110));
        add(scrollPane);
        add(superClass);
        add(new JSeparator(SwingConstants.HORIZONTAL));
        
        add(new JLabel("Differentia"));
        String[] col= {"Property","Value"};
        
        differentia = new JTable() {
            public boolean isCellEditable(int nRow, int nCol) {
                return false;
            }
        };
        DefaultTableModel diffTableModel = (DefaultTableModel) differentia.getModel();
        diffTableModel.setColumnIdentifiers(col);
        //diffTableModel.addRow(col);
        differentia.setModel(diffTableModel);
        add(differentia.getTableHeader());
        add(differentia);
        
        add(new JSeparator(SwingConstants.HORIZONTAL));
        
        inherited = new JTable() {
            public boolean isCellEditable(int nRow, int nCol) {
                return false;
            }
        };
        
        add(new JLabel("Inherited from Superclass"));
        DefaultTableModel inhTableModel = (DefaultTableModel) inherited.getModel();
        inhTableModel.setColumnIdentifiers(col);
        //inhTableModel.addRow(col);
        inherited.setModel(inhTableModel);
        add(inherited.getTableHeader());
        add(inherited);
        
        refresh(cla);
    }
    
    public void dispose() {
        //modelManager.removeListener(modelListener);
        //refreshButton.removeActionListener(refreshAction);
    }
    
    private static class RestrictionVisitor extends OWLClassExpressionVisitorAdapter {
        private final Set<OWLClass> processedClasses;
        private final Set<OWLObjectPropertyExpression> restrictedProperties;
        private final Set<OWLOntology> onts;

        public RestrictionVisitor(Set<OWLOntology> onts) {
            restrictedProperties = new HashSet<OWLObjectPropertyExpression>();
            processedClasses = new HashSet<OWLClass>();
            this.onts = onts;
        }

        public Set<OWLObjectPropertyExpression> getRestrictedProperties() {
            return restrictedProperties;
        }

        @Override
        public void visit(OWLClass desc) {
            // avoid cycles
            if (!processedClasses.contains(desc)) {
                // If we are processing inherited restrictions then
                // we recursively visit named supers.
                processedClasses.add(desc);
                for (OWLOntology ont : onts) {
                    for (OWLSubClassOfAxiom ax : ont.getSubClassAxiomsForSubClass(desc)) {
                        ax.getSuperClass().accept(this);
                    }
                }
            }
        }

        @Override
        public void visit(OWLObjectSomeValuesFrom desc) {
            // This method gets called when a class expression is an
            // existential (someValuesFrom) restriction and it asks us to visit
            // it
            restrictedProperties.add(desc.getProperty());
        }
    }
    
    public void refresh(OWLClass selectedClass) {    	               

        if(selectedClass!=null)
        {
        	className.setText("Selected Class : " + selectedClass.getIRI().getFragment());
        	
        	String desc="";
        	for (OWLAnnotation annotation : EntitySearcher.getAnnotations(selectedClass.getIRI(), modelManager.getActiveOntology())) {
        		if (annotation.getValue() instanceof OWLLiteral) {
        		OWLLiteral val = (OWLLiteral) annotation.getValue();
        		desc+=val.getLiteral()+" | ";
        		}
        	}
        	
        	
        	

        	
        	OWLDataFactory factory = modelManager.getOWLDataFactory();
            Set<OWLClassAxiom> tempAx=modelManager.getActiveOntology().getAxioms(selectedClass);
            RestrictionVisitor visitor = new RestrictionVisitor(Collections.singleton(modelManager.getActiveOntology()));
            for(OWLSubClassOfAxiom ax: modelManager.getActiveOntology().getSubClassAxiomsForSubClass(selectedClass)){
                //for(OWLClassExpression nce:ax.getNestedClassExpressions())
                    //if(nce.getClassExpressionType()!=ClassExpressionType.OWL_CLASS)
            	//Set<OWLClass> signature = ax.getClassesInSignature();
                        if(ax.getAxiomType().toString().equals("EquivalentClasses"))
                        {
                        	 
                        }
                        ax.getSuperClass().accept(visitor);
            }
            
            
            
            for (OWLObjectPropertyExpression prop:visitor.getRestrictedProperties()) {
            	desc+=" " + prop;
            	}

            
            
            description.setText(desc);
        		superClass.setText("Super Class :");
        		
        }
    }
}
