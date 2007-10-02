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
package dk.teachus.frontend.pages.stats;

import java.awt.Color;
import java.awt.Paint;
import java.util.List;

import dk.teachus.backend.domain.Person;
import dk.teachus.frontend.TeachUsSession;
import dk.teachus.frontend.UserLevel;
import dk.teachus.frontend.components.Toolbar;
import dk.teachus.frontend.components.Toolbar.ToolbarItemInterface;
import dk.teachus.frontend.components.jfreechart.PaintedDefaultCategoryDataset;
import dk.teachus.frontend.pages.AuthenticatedBasePage;

public abstract class AbstractStatisticsPage<P extends Person> extends AuthenticatedBasePage {
	
	protected static final Color COLOR_BLUE = new Color(25, 25, 204);
	protected static final Color COLOR_RED = Color.RED;
	protected static final Color COLOR_GREEN = new Color(25, 204, 25);

	public AbstractStatisticsPage(UserLevel userLevel) {
		super(userLevel, true);
		
		List<ToolbarItemInterface> items = getToolbarItems();
		
		add(new Toolbar("toolbar", items)); //$NON-NLS-1$
	}
	
	protected abstract List<ToolbarItemInterface> getToolbarItems();
	
	@SuppressWarnings("unchecked")
	protected P getPerson() {
		return (P) TeachUsSession.get().getPerson();
	}

	protected void appendDataset(PaintedDefaultCategoryDataset toDataset, PaintedDefaultCategoryDataset fromDataset) {
		for (int i = 0; i < fromDataset.getColumnCount(); i++) {
			Comparable<?> columnKey = fromDataset.getColumnKey(i);
			Comparable<?> rowKey = fromDataset.getRowKey(0);
			Paint paint = fromDataset.getPaint(rowKey);
			toDataset.addValue(fromDataset.getValue(rowKey, columnKey), rowKey, columnKey, paint);
		}
	}
	
	@Override
	protected AuthenticatedPageCategory getPageCategory() {
		return AuthenticatedPageCategory.STATISTICS;
	}

}
