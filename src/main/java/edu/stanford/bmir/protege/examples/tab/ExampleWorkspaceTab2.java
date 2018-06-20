package edu.stanford.bmir.protege.examples.tab;

import org.protege.editor.owl.ui.OWLWorkspaceViewsTab;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.protege.editor.owl.model.OWLModelManager;
import org.protege.editor.owl.model.event.EventType;
import org.protege.editor.owl.model.event.OWLModelManagerListener;
import java.awt.event.ActionListener;

public class ExampleWorkspaceTab2 extends OWLWorkspaceViewsTab {

	private static final Logger log = LoggerFactory.getLogger(ExampleWorkspaceTab2.class);
	
	private OWLModelManagerListener modelListener = event -> {
        if (event.getType() == EventType.ACTIVE_ONTOLOGY_CHANGED) {
            //recalculate();
        }
    };

	public ExampleWorkspaceTab2() {
		//setToolTipText("The Aristotle Tab helps you work with Aristotelian ontologies");
	}

    @Override
	public void initialise() {
		super.initialise();
		log.info("Aristotle initialized");
		OWLModelManager modelManager=getOWLModelManager();
		modelManager.addListener(modelListener);
	}

	@Override
	public void dispose() {
		super.dispose();
		log.info("Aristotle disposed");
	}
}
