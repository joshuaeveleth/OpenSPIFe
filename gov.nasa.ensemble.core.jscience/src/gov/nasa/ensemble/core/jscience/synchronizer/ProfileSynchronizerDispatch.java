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
package gov.nasa.ensemble.core.jscience.synchronizer;

import gov.nasa.ensemble.core.jscience.Profile;

import java.util.Collection;
import java.util.LinkedHashSet;

/**
 * Class to group the listener methods together
 */
class ProfileSynchronizerDispatch {
	
	private Collection<ProfileSynchronizerListener> listeners = new LinkedHashSet<ProfileSynchronizerListener>();
	
	void profilesAdded(Collection<Profile> profiles) {
		if (!profiles.isEmpty()) {
			for (ProfileSynchronizerListener l : listeners) {
				l.profilesAdded(profiles);
			}
		}
	}
	
	void profilesRemoved(Collection<Profile> profiles) {
		if (!profiles.isEmpty()) {
			for (ProfileSynchronizerListener l : listeners) {
				l.profilesRemoved(profiles);
			}
		}
	}
	
	void addListener(ProfileSynchronizerListener listener) {
		if (!listeners.contains(listener)) {
			listeners.add(listener);
		}
	}

	void removeListener(ProfileSynchronizerListener listener) {
		if (listeners.contains(listener)) {
			listeners.remove(listener);
		}
	}
	
}
