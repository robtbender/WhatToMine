package com.eve.whatToMine.cache;

import java.util.ArrayList;
import java.util.Collections;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import com.eve.whatToMine.data.EveOrder;
import com.eve.whatToMine.data.DatedEveOrderList;
import com.eve.whatToMine.db.EveSystemDb;

public class EveSystem {

	private EveDataLayer eveDataLayer;
	{
		try {
			eveDataLayer = (EveDataLayer) new InitialContext().lookup("java:module/EveDataLayerSingleton");
		} catch (NamingException e) {
			e.printStackTrace();
		}
	}

	public static final String EveSystemNameNOTENTERED = "Eve System Not Yet Entered";
	public static final long EveSystemIdNOTENTERED = -1;
	public static final double SecurityNOTENTERED = -100.00;

	private String eveSystemName = EveSystemNameNOTENTERED;
	private long eveSystemId = EveSystemIdNOTENTERED;
	private EveRegion eveRegion;
	private double security = SecurityNOTENTERED;
	private ArrayList<EveStation> eveStationList = new ArrayList<EveStation>();
	private ArrayList<EveSystem> jumpSystemList = new ArrayList<EveSystem>();
	private ArrayList<EveOrder> eveSystemItemMaxAvailableList = new ArrayList<EveOrder>();
	
	public EveSystem(EveSystemDb eveSystemDb) {
		this.setEveSystemName(eveSystemDb.getEveSystemName());
		this.setEveSystemId(eveSystemDb.getEveSystemId());
		this.setSecurity(eveSystemDb.getSecurity());
	}

	public String getEveSystemName() {
		return this.eveSystemName;
	}

	private void setEveSystemName(String eveSystemName) {
		this.eveSystemName = eveSystemName;
	}

	public long getEveSystemId() {
		return this.eveSystemId;
	}

	private void setEveSystemId(long eveSystemId) {
		this.eveSystemId = eveSystemId;
	}

	public EveRegion getEveRegion() {
	    return this.eveRegion;
    }
	
	public void setEveRegion(EveRegion eveRegion) {
	   this.eveRegion = eveRegion;
    }

	public double getSecurity() {
		return this.security;
	}

	private void setSecurity(double security) {
		this.security = security;
	}

	public ArrayList<EveStation> getEveStationList() {
		return this.eveStationList;
	}

	public void setEveStationList(ArrayList<Long> eveDbStationList) {
		EveStation eveStation = null;
		for (long eveStationId : eveDbStationList) {
			eveStation = eveDataLayer.getEveStation(eveStationId);
			this.eveStationList.add(eveStation);
		}
	}

	public ArrayList<EveSystem> getJumpSystemList() {
		return this.jumpSystemList;
	}

	public void setJumpSystemList(ArrayList<Long> eveDbJumpSystemList) {
		EveSystem eveSystem = null;
		for (long eveJumpSystemId : eveDbJumpSystemList) {
			eveSystem = eveDataLayer.getEveSystem(eveJumpSystemId);
			this.jumpSystemList.add(eveSystem);
		}
	}

	public ArrayList<EveOrder> getEveSystemItemMaxAvailableList() {
	    return this.eveSystemItemMaxAvailableList;
    }

	public void setEveSystemItemMaxAvailableList() {
		EveOrder eveMaxAvailableItemOrder;
		this.eveSystemItemMaxAvailableList = new ArrayList<EveOrder>();
		for(EveOre eveOre : this.getEveRegion().getEveRegionItemList()){
			eveMaxAvailableItemOrder = this.getEveRegion().getEveRegionItemMaxOrderAvailable(this,eveOre);
			this.eveSystemItemMaxAvailableList.add(eveMaxAvailableItemOrder);
		}

		Collections.sort(this.eveSystemItemMaxAvailableList, DatedEveOrderList.ORE_COMPARATOR);
    }

}
