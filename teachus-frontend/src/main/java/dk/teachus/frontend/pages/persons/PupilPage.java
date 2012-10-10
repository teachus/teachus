/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package dk.teachus.frontend.pages.persons;

import dk.teachus.backend.domain.Pupil;
import dk.teachus.frontend.TeachUsSession;
import dk.teachus.frontend.UserLevel;
import dk.teachus.frontend.components.person.PersonPanel;
import dk.teachus.frontend.components.person.PupilPanel;
import dk.teachus.frontend.models.PupilModel;

public class PupilPage extends PersonPage<PupilModel> {
	private static final long serialVersionUID = 1L;

	public PupilPage() {
		this(new PupilModel(((Pupil) TeachUsSession.get().getPerson()).getId()));
	}
	
	public PupilPage(PupilModel pupilModel) {
		super(UserLevel.PUPIL, pupilModel);
	}

	@Override
	public AuthenticatedPageCategory getPageCategory() {
		if (TeachUsSession.get().getUserLevel() == UserLevel.PUPIL) {
			return AuthenticatedPageCategory.SETTINGS;
		} else {
			return AuthenticatedPageCategory.PUPILS;
		}
	}

	@Override
	protected PersonPanel createPersonPanel(String wicketId, PupilModel pupilModel) {
		return new PupilPanel(wicketId, pupilModel);
	}

}
