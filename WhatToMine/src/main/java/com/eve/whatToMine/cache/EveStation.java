package com.eve.whatToMine.cache;

import com.eve.whatToMine.db.EveStationDb;

public class EveStation {

	public static final String EveStationNameEMPTYVALUE = "Eve System Not Yet Initialized";
	public static final long EveStationIdEMTPYVALUE = -1;
	
	private String eveStationName;
	private long eveStationId;
	private EveSystem eveSystem;

	public EveStation(EveStationDb eveStationDb) {
		this.setEveStationId(eveStationDb.getEveStationId());
		this.setEveStationName(eveStationDb.getEveStationName());
	}

	public EveStation(long eveStationId, String eveStationName) {
		this.setEveStationId(eveStationId);
		this.setEveStationName(eveStationName);
    }

	public String getEveStationName() {
		return this.eveStationName;
	}

	private void setEveStationName(String eveStationName) {
		this.eveStationName = eveStationName;
	}

	public long getEveStationId() {
		return this.eveStationId;
	}

	private void setEveStationId(long eveStationId) {
		this.eveStationId = eveStationId;
	}

	public EveSystem getEveSystem() {
		return this.eveSystem;
	}

	public void setEveSystem(EveSystem eveSystem) {
		this.eveSystem = eveSystem;
	}
}