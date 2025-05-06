package com.democrud.entity;

import java.util.Map;

public class ImportResult {


	private Map<String, Integer> recordStats;

	public Map<String, Integer> getRecordStats() {
		return recordStats;
	}

	public void setRecordStats(Map<String, Integer> recordStats) {
		this.recordStats = recordStats;
	}

	public ImportResult(Map<String, Integer> recordStats) {
		super();
		this.recordStats = recordStats;
	}

	public ImportResult() {
		super();

	}




}
