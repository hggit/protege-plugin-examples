package edu.stanford.bmir.protege.examples.view;

import java.awt.BorderLayout;
import java.io.PrintWriter;
import java.io.File;
import org.protege.editor.owl.ui.view.AbstractOWLViewComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.protege.editor.owl.model.selection.OWLSelectionModelListener;
import org.protege.editor.owl.model.OWLModelManager;
import org.protege.editor.owl.model.event.EventType;
import org.protege.editor.owl.model.event.OWLModelManagerListener;

//import org.semanticweb.owlapi.model.OWLOntologyFormat;
import com.similar2.matcher.ontology.io.OntologyReader;
import com.similar2.matcher.ontology.model.IAristotelianOntology;
import com.similar2.matcher.ontology.io.ReasonerType;

public class AttributesView extends AbstractOWLViewComponent {		//Himanshu: Creates the Details section of the tab

    private static final Logger log = LoggerFactory.getLogger(AttributesView.class);

    private DisplayAttributes displayComponent;
    private OWLModelManager modelManager;
    /*
    private OWLModelManagerListener modelListener = event -> {
        if (event.getType() == EventType.ACTIVE_ONTOLOGY_CHANGED) {
        	try{initialiseOWLView();}
        	catch(Exception e){}
        }
    };
    */
    OWLSelectionModelListener osmListener=new OWLSelectionModelListener() {		//Himanshu: Detect user selection
		public void selectionChanged() throws Exception {
			displayComponent.refresh(getOWLWorkspace().getOWLSelectionModel().getLastSelectedClass());
		}    		
	};

    @Override
    protected void initialiseOWLView() throws Exception {
    	//modelManager.addListener(modelListener);
    	OntologyReader or=new OntologyReader();		//Himanshu: Using AO-core to read the ontology
    	IAristotelianOntology ao=null;
    	try {
    	ao=or.loadAristotelianOntology(getOWLModelManager().getActiveOntology(),ReasonerType.HERMIT);
    	}
    	catch(Exception e)
    	{
    		File file = new File("X:/himanshu.txt");
    		PrintWriter printWriter = new PrintWriter(file);
    		log.info("Initialise owl view :"+e.getMessage());
    		//log.error(e.getMessage(),e);
    		e.printStackTrace(printWriter);
    		//log.info(java.util.logging.Level.INFO, e.getMessage(), e);
    		log.info("done");
    		printWriter.close();
    	}
        setLayout(new BorderLayout());
        getOWLWorkspace().getOWLSelectionModel().addListener(osmListener);
        displayComponent = new DisplayAttributes(getOWLModelManager(),getOWLWorkspace().getOWLSelectionModel().getLastSelectedClass(), ao);        
        add(displayComponent);
                        
        log.info("***Attributes initialized***");
    }

	@Override
	protected void disposeOWLView() {
		displayComponent.dispose();
		//modelManager.removeListener(modelListener);
		getOWLWorkspace().getOWLSelectionModel().removeListener(osmListener);
	}
}
