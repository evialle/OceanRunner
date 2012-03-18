/**
 * 
 */
package it.freshminutes.oceanrunner.modules.builtin.statistics;

import java.util.List;
import java.util.Map;

/**
 * @author Eric
 *
 */
public interface StatisticsSqlPlug {

	Map<String, StatisticsResult> loadLastTestStatus(List<String> testsToSearch);

}
