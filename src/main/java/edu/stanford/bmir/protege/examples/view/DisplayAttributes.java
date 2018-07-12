package edu.stanford.bmir.protege.examples.view;

import java.awt.event.ActionListener;
import java.util.*;
import java.util.stream.Collectors;
import java.util.Set;
import java.util.stream.Stream;
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
import com.similar2.matcher.ontology.model.IAristotelianOntology;
import com.similar2.matcher.ontology.io.OntologyReader;
import com.similar2.matcher.ontology.io.ReasonerType;

import com.similar2.matcher.ontology.model.IAristotelianOntology;
import com.similar2.matcher.ontology.model.IAristotelianOntologyClass;
import com.similar2.matcher.ontology.model.IValuePair;
import com.similar2.matcher.ontology.model.IAttributeDescription;
import com.similar2.matcher.ontology.model.IEnumeratedProperty;
import com.similar2.matcher.ontology.model.IPrimaryEntityClass;
import com.similar2.matcher.ontology.model.IPrimaryEnumeratedClass;
import com.similar2.matcher.ontology.model.impl.DetailedDescription;


public class DisplayAttributes extends JPanel {

    //private JButton refreshButton = new JButton("Refresh");

    private JLabel className = new JLabel();
    private JTextArea description;
    private JLabel superClass = new JLabel();
    private JTable differentia;
    private JTable inherited;
    
    private OWLModelManager modelManager;
    IAristotelianOntology ao;

    //private ActionListener refreshAction = e -> recalculate();
    
    private OWLModelManagerListener modelListener = event -> {
        if (event.getType() == EventType.ACTIVE_ONTOLOGY_CHANGED) {
            loadOntology();
        }
    };
    
    public DisplayAttributes(OWLModelManager modelManager, OWLClass cla, IAristotelianOntology ao) {
    	this.modelManager = modelManager;
    	this.ao=ao;
        modelManager.addListener(modelListener);
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
    
    void loadOntology()
    {
    	OntologyReader or=new OntologyReader();
    	try {
    		ao=or.loadAristotelianOntology(modelManager.getActiveOntology(),ReasonerType.HERMIT);
    	}
    	catch(Exception e)
    	{/*
    		File file = new File("X:/himanshu.txt");
    		PrintWriter printWriter = new PrintWriter(file);
    		log.info("Initialise owl view :"+e.getMessage());
    		//log.error(e.getMessage(),e);
    		e.printStackTrace(printWriter);
    		//log.info(java.util.logging.Level.INFO, e.getMessage(), e);
    		log.info("done");
    		printWriter.close();*/
    	}
    }
    
    public void dispose() {
        modelManager.removeListener(modelListener);
        //refreshButton.removeActionListener(refreshAction);
    }
    /*
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
    */
    public void refresh(OWLClass selectedClass) {    	               

        if(selectedClass!=null)
        {
        	className.setText("Selected Class : " + selectedClass.getIRI().getFragment());
        	
        	String desc="";
        	for (OWLAnnotation annotation : EntitySearcher.getAnnotations(selectedClass.getIRI(), modelManager.getActiveOntology()).stream().collect(Collectors.toSet())) {
        		if (annotation.getValue() instanceof OWLLiteral) {
        		OWLLiteral val = (OWLLiteral) annotation.getValue();
        		desc+=val.getLiteral()+" | ";
        		}
        	}      	
        	
        	OWLClass superclass;
        	for(OWLClassExpression oce : EntitySearcher.getEquivalentClasses(selectedClass, modelManager.getActiveOntology()).stream().collect(Collectors.toSet()))
        	{        		        		
        		if(oce.getClassExpressionType().toString().equals("ObjectIntersectionOf"))
        		{
        			for(OWLClassExpression ce : oce.asConjunctSet()) { if(!ce.isAnonymous())//desc+="@";
        			for(OWLClass oc : ce.getClassesInSignature()) superclass=oc;//desc+=oc.toStringID();
        			}
        		}
        	}
        	/*
        	OWLDataFactory factory = modelManager.getOWLDataFactory();
            Set<OWLClassAxiom> tempAx=modelManager.getActiveOntology().getAxioms(selectedClass);
            RestrictionVisitor visitor = new RestrictionVisitor(Collections.singleton(modelManager.getActiveOntology()));
            for(OWLSubClassOfAxiom ax: modelManager.getActiveOntology().getSubClassAxiomsForSubClass(selectedClass)){
                //for(OWLClassExpression nce:ax.getNestedClassExpressions())
                    //if(nce.getClassExpressionType()!=ClassExpressionType.OWL_CLASS)
            	//Set<OWLClass> signature = ax.getClassesInSignature();
            	for(OWLClass oc : ax.getSuperClass().getClassesInSignature()) desc+="@"+oc.toStringID();
                        if(ax.getAxiomType().toString().equals("EquivalentClasses"))
                        {
                        	 desc+="@"+ax.getSuperClass();
                        }
                        //ax.getSuperClass().accept(visitor);
            }
               
            for (OWLObjectPropertyExpression prop:visitor.getRestrictedProperties()) {
            	desc+=" " + prop.toString();
            	}
			*/
        	
        	
                       
            IPrimaryEntityClass ipec=ao.getPrimaryEntityClass(selectedClass.getIRI().getNamespace(),selectedClass.getIRI().getFragment());
            if(ipec!=null) {
            	DetailedDescription dd=new DetailedDescription(ipec);
            List<IValuePair> pval=dd.getProperValues();
            desc+="***"+pval.size();
            DefaultTableModel diffTableModel = (DefaultTableModel) differentia.getModel();
            diffTableModel.setRowCount(0);
            for(IValuePair ivp : pval)
            	{
            		//desc+=ivp.toString();
            		String ar[]=new String[2];
            		ar[0]=((OWLLiteral)EntitySearcher.getAnnotations(IRI.create(ivp.getProperty().getFQName()), modelManager.getActiveOntology()).stream().collect(Collectors.toSet()).iterator().next().getValue()).getLiteral();
            		ar[1]=((OWLLiteral)EntitySearcher.getAnnotations(IRI.create(ivp.getValue().getFQName()), modelManager.getActiveOntology()).stream().collect(Collectors.toSet()).iterator().next().getValue()).getLiteral();
            		diffTableModel.addRow(ar);
            	}
            differentia.setModel(diffTableModel);
            diffTableModel.fireTableDataChanged();
            /*
            List<IValuePair> ival=dd.getInheritedValues();
            desc+="***"+ival.size();
            DefaultTableModel inhTableModel = (DefaultTableModel) inherited.getModel();
            inhTableModel.setRowCount(0);
            for(IValuePair ivp : ival)
            	{
            		//desc+=ivp.toString();
            		String ar[]=new String[2];
            		ar[0]=((OWLLiteral)EntitySearcher.getAnnotations(IRI.create(ivp.getProperty().getFQName()), modelManager.getActiveOntology()).stream().collect(Collectors.toSet()).iterator().next().getValue()).getLiteral();
            		ar[1]=((OWLLiteral)EntitySearcher.getAnnotations(IRI.create(ivp.getValue().getFQName()), modelManager.getActiveOntology()).stream().collect(Collectors.toSet()).iterator().next().getValue()).getLiteral();
            		diffTableModel.addRow(ar);
            	}
            inherited.setModel(inhTableModel);
            inhTableModel.fireTableDataChanged();*/
            
            
            
            }
            description.setText(desc);
        		superClass.setText("Super Class :");
        		
        }
    }
}
