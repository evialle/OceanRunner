package it.freshminutes.oceanrunner.modules.statistics;

import it.freshminutes.oceanrunner.OceanRunner;

import java.util.Collection;
import java.util.List;

import com.google.common.collect.Lists;

public class DummyDataPlug extends StatisticsDataPlug {

	public DummyDataPlug(OceanRunner oceanRunner) {
		super(oceanRunner);
		// TODO Auto-generated constructor stub
	}


	@Override
	public void storeLastTestStatus(Collection<StatisticsResult> statisticsResultsList) {
		// TODO Auto-generated method stub

	}


	@Override
	public List<StatisticsResult> loadTestStatus(String testsToSearch) {
		return Lists.newArrayList();
	}

}
