package edu.stanford.bmir.protege.examples.view;

import java.awt.event.ActionListener;
import java.util.*;
import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;


import org.protege.editor.owl.model.OWLModelManager;
import org.protege.editor.owl.model.event.EventType;
import org.protege.editor.owl.model.event.OWLModelManagerListener;

import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClassAxiom;
import org.semanticweb.owlapi.model.OWLClassExpression;

public class DisplayAttributes extends JPanel {

    //private JButton refreshButton = new JButton("Refresh");

    private JLabel textComponent = new JLabel();
    JTextArea textArea;

    private OWLModelManager modelManager;


    /*private ActionListener refreshAction = e -> recalculate();
    
    private OWLModelManagerListener modelListener = event -> {
        if (event.getType() == EventType.ACTIVE_ONTOLOGY_CHANGED) {
            refresh(getOWLWorkspace().getOWLSelectionModel().getLastSelectedClass());
        }
    };*/
    
    public DisplayAttributes(OWLModelManager modelManager, OWLClass cla) {
    	this.modelManager = modelManager;
        refresh(cla);
        
        //modelManager.addListener(modelListener);
        //refreshButton.addActionListener(refreshAction);
        
        
        add(textComponent);
        //add(refreshButton);
        
        textArea = new JTextArea(5, 30);
        JScrollPane scrollPane = new JScrollPane(textArea);
        //setPreferredSize(new Dimension(450, 110));
        add(scrollPane, BorderLayout.CENTER);

    }
    
    public void dispose() {
        //modelManager.removeListener(modelListener);
        //refreshButton.removeActionListener(refreshAction);
    }
    
    public void refresh(OWLClass selectedClass) {
        /*int count = modelManager.getActiveOntology().getClassesInSignature().size();
        if (count == 0) {
            count = 1;  // owl:Thing is always there.
        }*/    	
    	       
        
        
        if(selectedClass!=null)
        	{
        	String s="";
            Set<OWLClassAxiom> tempAx=modelManager.getActiveOntology().getAxioms(selectedClass);
            for(OWLClassAxiom ax: tempAx){
                //for(OWLClassExpression nce:ax.getNestedClassExpressions())
                    //if(nce.getClassExpressionType()!=ClassExpressionType.OWL_CLASS)
            	//Set<OWLClass> signature = ax.getClassesInSignature();
                        s+=ax+"*****";
            }
        		
        		textComponent.setText("Selected Class : " + selectedClass.getIRI().getFragment());
        		textArea.append(s);
        		
        	}
    }
}
