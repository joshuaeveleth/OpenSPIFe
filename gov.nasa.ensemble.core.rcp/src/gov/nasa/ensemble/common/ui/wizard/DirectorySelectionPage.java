/*******************************************************************************
 * Copyright 2014 United States Government as represented by the
 * Administrator of the National Aeronautics and Space Administration.
 * All Rights Reserved.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package gov.nasa.ensemble.common.ui.wizard;

import java.io.File;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public class DirectorySelectionPage extends WizardPage {

	private Text pathText;
	private String labelText = "File Location:";

	public DirectorySelectionPage() {
		super("directory_selection_page");
	}

	public File getSelectedFile() {
		return new File(pathText.getText());
	}

	public void setLabelText(String labelText) {
		this.labelText = labelText;
	}

	public void createControl(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout(1, true);
		composite.setLayout(layout);

		buildFolderChooser(composite);
		buildControls(composite);

		setControl(composite);
	}

	/**
	 * Allow for implementing classes to create more custom controls.
	 */
	protected void buildControls(Composite parent) {
		// no default implementation
	}

	protected Composite buildFolderChooser(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		composite.setLayout(layout);

		GridData folderSelectionData = new GridData(GridData.GRAB_HORIZONTAL | GridData.FILL_HORIZONTAL);
		composite.setLayoutData(folderSelectionData);

		layout.numColumns = 3;

		Label label = new Label(composite, SWT.NULL);
		label.setText(labelText);

		pathText = new Text(composite, SWT.BORDER | SWT.SINGLE);
		pathText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		pathText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				dialogChanged();
			}
		});

		Button browseButton = new Button(composite, SWT.NORMAL);
		browseButton.setFocus();
		browseButton.setText("Browse...");
		browseButton.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent e) {
				DirectoryDialog dialog = new DirectoryDialog(getContainer().getShell());
				String filePath = dialog.open();
				if (filePath != null) {
					// cancelled, or error condition
					pathText.setText(filePath);
				}
			}

			public void widgetDefaultSelected(SelectionEvent e) {/* default impl */
			}
		});
		return composite;
	}

	protected void dialogChanged() {
		String fileName = pathText.getText();
		File file = new File(fileName);
		if (fileName.length() == 0) {
			updateStatus("File location must be specified");
		} else if (!file.isDirectory()) {
			updateStatus("File must be a directory");
		} else if (!file.canWrite()) {
			updateStatus("Do not have the necessary permissions to write to this file, mau be in use");
		} else {
			updateStatus(null);
		}
	}

	protected void updateStatus(String message) {
		setErrorMessage(message);
		setPageComplete(message == null);
	}
}
