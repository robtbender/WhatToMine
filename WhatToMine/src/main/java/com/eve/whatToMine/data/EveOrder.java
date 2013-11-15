package com.eve.whatToMine.data;

import com.eve.whatToMine.cache.EveItem;
import com.eve.whatToMine.cache.EveRegion;
import com.eve.whatToMine.cache.EveStation;

public class EveOrder {

	public static final long EveItemIdNoVALUE = -1;

	private EveItem eveItem;
	private EveRegion eveRegion;
	private double price = -1.00;
	private EveStation eveStation = null;
	private long range = -10;

	public EveOrder(EveItem eveItem, EveRegion eveRegion) {
		this.setEveItem(eveItem);
		this.setEveRegion(eveRegion);
	}

	public EveItem getEveItem() {
		return this.eveItem;
	}

	private void setEveItem(EveItem eveItem) {
		this.eveItem = eveItem;
	}

	public EveRegion getEveRegion() {
		return this.eveRegion;
	}

	private void setEveRegion(EveRegion eveRegion) {
		this.eveRegion = eveRegion;
	}

	public void setEmptyOrder() {
		EveStation eveStation = new EveStation(EveStation.EveStationIdEMTPYVALUE, "No valid orders for System");
		eveStation.setEveSystem(this.getEveRegion().getEveSystemList().get(0));
		this.setStation(eveStation);
	}

	public double getPrice() {
		return this.price;
	}

	public void setPrice(double price) {
		this.price = price;
	}
	
	public EveStation getStation() {
		return this.eveStation;
    }

	public void setStation(EveStation eveStation) {
		this.eveStation  = eveStation;
    }

	public long getRange() {
	    return this.range;
    }
	
	public void setRange(long range) {
	    this.range = range;
    }

}
