package com.eve.whatToMine.cache;

import com.eve.whatToMine.db.EveItemDb;

public class EveItem {
	
	public static final String EveItemNameEMPTYVALUE = "Eve System Not Yet Initialized";
	public static final long EveItemIdEMPTYVALUE = -1;
	public static final double EveItemVolumeEMPTYVALUE = -1.0;

	private String eveItemName;
	private double eveItemVolume;
	private long eveItemId;
	
	public EveItem(EveItemDb eveItemDb) {
		this.setEveItemName(eveItemDb.getEveItemName());
		this.setEveItemId(eveItemDb.getEveItemId());
		this.setEveItemVolume(eveItemDb.getEveItemVolume());
    }

	public String getEveItemName() {
	    return this.eveItemName;
    }

	public void setEveItemName(String eveItemName) {
	    this.eveItemName = eveItemName;
    }

	public double getEveItemVolume() {
	    return this.eveItemVolume;
    }

	public void setEveItemVolume(double eveItemVolume) {
	    this.eveItemVolume = eveItemVolume;
    }

	public long getEveItemId() {
	    return this.eveItemId;
    }
	
	private void setEveItemId(long eveItemId) {
		this.eveItemId = eveItemId;
    }
}
