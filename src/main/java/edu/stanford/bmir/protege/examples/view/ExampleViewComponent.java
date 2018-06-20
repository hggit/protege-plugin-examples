package edu.stanford.bmir.protege.examples.view;

import java.awt.BorderLayout;
import org.protege.editor.owl.ui.view.AbstractOWLViewComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.protege.editor.owl.model.selection.OWLSelectionModelListener;

public class ExampleViewComponent extends AbstractOWLViewComponent {

    private static final Logger log = LoggerFactory.getLogger(ExampleViewComponent.class);

    private Metrics metricsComponent;

    @Override
    protected void initialiseOWLView() throws Exception {
        setLayout(new BorderLayout());
        metricsComponent = new Metrics(getOWLModelManager(),getOWLWorkspace().getOWLSelectionModel().getLastSelectedClass());
        add(metricsComponent, BorderLayout.CENTER);
        
        getOWLWorkspace().getOWLSelectionModel().addListener(new OWLSelectionModelListener() {
			public void selectionChanged() throws Exception {
				metricsComponent.refresh(getOWLWorkspace().getOWLSelectionModel().getLastSelectedClass());
			}    		
    	});
        
        log.info("Attributes initialized");
        
    }

	@Override
	protected void disposeOWLView() {
		metricsComponent.dispose();
	}
}
