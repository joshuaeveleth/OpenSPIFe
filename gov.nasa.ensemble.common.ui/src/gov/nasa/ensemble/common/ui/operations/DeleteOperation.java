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
/**
 * 
 */
package gov.nasa.ensemble.common.ui.operations;

import gov.nasa.ensemble.common.operation.AbstractEnsembleUndoableOperation;
import gov.nasa.ensemble.common.ui.IStructureLocation;
import gov.nasa.ensemble.common.ui.IStructureModifier;
import gov.nasa.ensemble.common.ui.transfers.ITransferable;

public class DeleteOperation extends AbstractEnsembleUndoableOperation {
	
	private final ITransferable transferable;
	private final IStructureModifier modifier;
	private final IStructureLocation location;
	
	public DeleteOperation(ITransferable transferable, IStructureModifier modifier) {
		this("delete", transferable, modifier);
	}
	
	protected DeleteOperation(String label, ITransferable transferable, IStructureModifier modifier) {
		super(label);
		this.modifier = modifier;
		this.transferable = transferable;
		this.location = (transferable != null ? modifier.getLocation(transferable) : null);
	}
	
	@Override
	protected void dispose(UndoableState state) {
		if (state == null) {
			return;
		}
		switch (state) {
		case DONE:
			transferable.dispose();
			break;
		case UNEXECUTED:
		case UNDONE:
			// transferable still in the document
			break;
		case FAILED:
			// don't know how to recover here 
		}
	}
	
	protected final ITransferable getObjects() {
		return transferable;
	}

	protected boolean somethingSelected() {
		return ((transferable != null) && (location != null)); 
	}

	@Override
	public boolean isExecutable() {
		return somethingSelected();
	}
	
	@Override
	protected void execute() {
		doit();
	}

	@Override
	protected void undo() {
		modifier.add(transferable, location);
	}

	@Override
	protected void redo() throws Throwable {
		doit();
	}
	
	protected void doit() {
		modifier.remove(transferable, location);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder(DeleteOperation.class.getSimpleName());
		builder.append(":");
		builder.append(String.valueOf(transferable));
		builder.append(" from ");
		builder.append(String.valueOf(location));
		return builder.toString();
	}
	
}
