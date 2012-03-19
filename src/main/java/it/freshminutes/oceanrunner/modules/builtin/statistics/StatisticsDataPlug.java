/**
 * Copyright 2012 Eric P. Vialle
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package it.freshminutes.oceanrunner.modules.builtin.statistics;

import it.freshminutes.oceanrunner.OceanRunner;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @author Eric
 *
 */
public abstract class StatisticsDataPlug {
	
	public StatisticsDataPlug(final OceanRunner oceanRunner) {
		super();
	}

	public abstract Map<String, StatisticsResult> loadLastTestStatus(List<String> testsToSearch);

	public abstract void storeLastTestStatus(Collection<StatisticsResult> statisticsResultsList);

}
