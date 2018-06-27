package edu.stanford.bmir.protege.examples.view;

import java.awt.BorderLayout;
import org.protege.editor.owl.ui.view.AbstractOWLViewComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.protege.editor.owl.model.selection.OWLSelectionModelListener;

public class AttributesView extends AbstractOWLViewComponent {

    private static final Logger log = LoggerFactory.getLogger(AttributesView.class);

    private DisplayAttributes displayComponent;
    
    OWLSelectionModelListener osmListener=new OWLSelectionModelListener() {
		public void selectionChanged() throws Exception {
			displayComponent.refresh(getOWLWorkspace().getOWLSelectionModel().getLastSelectedClass());
		}    		
	};

    @Override
    protected void initialiseOWLView() throws Exception {
        setLayout(new BorderLayout());
        getOWLWorkspace().getOWLSelectionModel().addListener(osmListener);
        displayComponent = new DisplayAttributes(getOWLModelManager(),getOWLWorkspace().getOWLSelectionModel().getLastSelectedClass());        
        add(displayComponent);
                        
        log.info("***Attributes initialized***");
    }

	@Override
	protected void disposeOWLView() {
		//displayComponent.dispose();
		getOWLWorkspace().getOWLSelectionModel().removeListener(osmListener);
	}
}
