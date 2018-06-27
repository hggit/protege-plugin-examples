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


import org.protege.editor.owl.model.OWLModelManager;
import org.protege.editor.owl.model.event.EventType;
import org.protege.editor.owl.model.event.OWLModelManagerListener;

import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClassAxiom;
import org.semanticweb.owlapi.model.OWLClassExpression;

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
        		
        		className.setText("Selected Class : " + selectedClass.getIRI().getFragment());
        		description.setText(s);
        		superClass.setText("Super Class :");
        		
        }
    }
}
