package it.freshminutes.oceanrunner.modules.builtin.statistics;

import it.freshminutes.oceanrunner.OceanRunner;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Maps;

public class DummyDataPlug extends StatisticsDataPlug {

	public DummyDataPlug(OceanRunner oceanRunner) {
		super(oceanRunner);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Map<String, StatisticsResult> loadLastTestStatus(List<String> testsToSearch) {
		// TODO Auto-generated method stub
		return Maps.newHashMap();
	}

	@Override
	public void storeLastTestStatus(Collection<StatisticsResult> statisticsResultsList) {
		// TODO Auto-generated method stub

	}

}
