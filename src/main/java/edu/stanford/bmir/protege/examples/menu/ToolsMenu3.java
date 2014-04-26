package edu.stanford.bmir.protege.examples.menu;

import java.awt.event.ActionEvent;

import javax.swing.JOptionPane;

import org.apache.log4j.Logger;

import org.protege.editor.owl.ui.action.ProtegeOWLAction;

public class ToolsMenu3 extends ProtegeOWLAction {
	public static Logger LOGGER = Logger.getLogger(ToolsMenu3.class);


	public void initialise() throws Exception {

	}


	public void dispose() throws Exception {

	}


	public void actionPerformed(ActionEvent event) {
		StringBuffer message = new StringBuffer("This menu is under the tools menu but is in a separate category from the above two.\n");
		message.append("The active ontology has ");
		message.append(getOWLModelManager().getActiveOntology().getClassesInSignature().size());
		message.append(" classes.");
		JOptionPane.showMessageDialog(getOWLWorkspace(), message.toString());	
	}

}