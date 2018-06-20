package edu.stanford.bmir.protege.examples.view;

import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.protege.editor.owl.model.OWLModelManager;
import org.protege.editor.owl.model.event.EventType;
import org.protege.editor.owl.model.event.OWLModelManagerListener;

import org.semanticweb.owlapi.model.OWLClass;

public class Metrics extends JPanel {

    //private JButton refreshButton = new JButton("Refresh");

    private JLabel textComponent = new JLabel();

    private OWLModelManager modelManager;


    /*private ActionListener refreshAction = e -> recalculate();
    
    private OWLModelManagerListener modelListener = event -> {
        if (event.getType() == EventType.ACTIVE_ONTOLOGY_CHANGED) {
            refresh(getOWLWorkspace().getOWLSelectionModel().getLastSelectedClass());
        }
    };*/
    
    public Metrics(OWLModelManager modelManager, OWLClass cla) {
    	this.modelManager = modelManager;
        refresh(cla);
        
        //modelManager.addListener(modelListener);
        //refreshButton.addActionListener(refreshAction);
        
        
        add(textComponent);
        //add(refreshButton);
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
    	
    	
        if(selectedClass!=null) textComponent.setText("Selected Class : " + selectedClass.getIRI().getFragment());
    }
}
