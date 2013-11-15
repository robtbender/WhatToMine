package com.eve.whatToMine.cache;

import java.util.ArrayList;
import java.util.HashMap;

import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Singleton;

import org.apache.log4j.Logger;

import com.eve.whatToMine.cache.EveSystem;
import com.eve.whatToMine.db.EveItemDb;
import com.eve.whatToMine.db.EveRegionDb;
import com.eve.whatToMine.db.EveStationDb;
import com.eve.whatToMine.db.EveSystemDb;

@Singleton
//@Lock(LockType.READ)
public class EveDataLayerSingleton implements EveDataLayer {

	private HashMap<Long, EveSystem> eveSystemHashMap = new HashMap<Long, EveSystem>();
	private HashMap<Long, EveStation> eveStationHashMap = new HashMap<Long, EveStation>();
	private HashMap<String, EveItem> eveItemHashMap = new HashMap<String, EveItem>();
	private HashMap<Long, EveRegion> eveRegionHashMap = new HashMap<Long, EveRegion>();
	private ArrayList<EveOre> eveOreList = new ArrayList<EveOre>();

	{
		this.setEveOreList();
	}

	private final static Logger LOGGER = Logger.getLogger(EveDataLayerSingleton.class);

	@Override
    public boolean eveSystemExists(long eveSystemId) {
		return eveSystemHashMap.containsKey(eveSystemId);
    }

	@Override
    public EveSystem getEveSystem(long eveSystemId) {
		Logger logger = EveDataLayerSingleton.LOGGER;

		EveSystem eveSystem;
		if (this.eveSystemExists(eveSystemId)) {
			eveSystem = eveSystemHashMap.get(eveSystemId);
			logger.info(String.format("System Id in Data Cache( %s ).  Saved some time!!!", eveSystem.getEveSystemName()));
		} else {
			EveSystemDb eveSystemDb = new EveSystemDb(eveSystemId);
			eveSystem = new EveSystem(eveSystemDb);
			this.addEveSystem(eveSystem);
			eveSystem.setEveRegion(this.getEveRegion(eveSystemDb.getEveRegionId()));
			eveSystem.setEveStationList(eveSystemDb.getEveStationList());
			eveSystem.setJumpSystemList(eveSystemDb.getJumpSystemList());
		}
		return eveSystem;
    }

	@Override
	public boolean eveSystemExists(String eveSystemName) {
		boolean found = false;
		for( long keyEveSystemId : eveSystemHashMap.keySet() ){
			if( eveSystemHashMap.get(keyEveSystemId).getEveSystemName().equals(eveSystemName)){
				found = true;
				break;
			}
		}
	    return found;
	}

	@Override
	public EveSystem getEveSystem(String eveSystemName) {
		Logger logger = EveDataLayerSingleton.LOGGER;
		EveSystem eveSystem =  null;
		if (this.eveSystemExists(eveSystemName)) {
			for( long keyEveSystemId : eveSystemHashMap.keySet() ){
				if( eveSystemHashMap.get(keyEveSystemId).getEveSystemName().equals(eveSystemName)){
					eveSystem = eveSystemHashMap.get(keyEveSystemId);
					break;
				}
			}
			logger.info(String.format("System Name in Data Cache( %s ).  Saved some time!!!", eveSystem.getEveSystemName()));
		} else {
			EveSystemDb eveSystemDb = new EveSystemDb(eveSystemName);
			eveSystem = new EveSystem(eveSystemDb);
			this.addEveSystem(eveSystem);
			eveSystem.setEveRegion(this.getEveRegion(eveSystemDb.getEveRegionId()));
			eveSystem.setEveStationList(eveSystemDb.getEveStationList());
			eveSystem.setJumpSystemList(eveSystemDb.getJumpSystemList());
		}
	    return eveSystem;
	    //TODO What to do if EveSystem is null?
	}

	@Lock(LockType.WRITE)
	private void addEveSystem(EveSystem eveSystem) {
		Logger logger = EveDataLayerSingleton.LOGGER;
		logger.info(String.format("Adding Eve System to the Data Cache( %s ).", eveSystem.getEveSystemName()));
		this.eveSystemHashMap.put(eveSystem.getEveSystemId(), eveSystem);
	}

	@Override
	public boolean eveStationExists(long eveStationId) {
		return eveStationHashMap.containsKey(eveStationId);
	}

	@Override
	public EveStation getEveStation(long eveStationId) {
		Logger logger = EveDataLayerSingleton.LOGGER;

		EveStation eveStation;
		if (this.eveStationExists(eveStationId)) {
			eveStation = eveStationHashMap.get(eveStationId);
			logger.info(String.format("Station by ID in Data Cache( %s ).  Saved some time!!!", eveStation.getEveStationName()));
		} else {
			EveStationDb eveStationDb = new EveStationDb(eveStationId);
			eveStation = new EveStation(eveStationDb);
			this.addEveStation(eveStation);
			eveStation.setEveSystem(this.getEveSystem(eveStationDb.getEveSystemId()));
		}
		return eveStation;
	}

	@Lock(LockType.WRITE)
	public void addEveStation(EveStation eveStation) {
		Logger logger = EveDataLayerSingleton.LOGGER;
		logger.info(String.format("Adding Eve Station to the Data Cache( %s ).", eveStation.getEveStationName()));
		this.eveStationHashMap.put(eveStation.getEveStationId(), eveStation);
	}

	@Override
	public boolean eveItemExists(String eveItemName) {
		return eveItemHashMap.containsKey(eveItemName);
	}

	@Override
	public EveItem getEveItem(String eveItemName) {
		Logger logger = EveDataLayerSingleton.LOGGER;

		EveItem eveItem;
		if (this.eveItemExists(eveItemName)) {
			eveItem = eveItemHashMap.get(eveItemName);
			logger.info(String.format("Eve Item in Data Cache( %s ).  Saved some time!!!", eveItem.getEveItemName()));
		} else {
			eveItem = new EveItem(new EveItemDb(eveItemName));
			this.addEveItem(eveItem);
		}
		return eveItem;
	}

	@Lock(LockType.WRITE)
	private void addEveItem(EveItem eveItem) {
		Logger logger = EveDataLayerSingleton.LOGGER;
		logger.info(String.format("Adding Eve Item to the Data Cache( %s ).", eveItem.getEveItemName()));
		this.eveItemHashMap.put(eveItem.getEveItemName(), eveItem);
	}

	@Override
	public EveOre getEveOre(String eveOreName) {
		Logger logger = EveDataLayerSingleton.LOGGER;

		EveOre eveOre;
		if (this.eveItemExists(eveOreName)) {
			if (this.eveItemHashMap.get(eveOreName).getClass() == EveOre.class) {
				eveOre = (EveOre) eveItemHashMap.get(eveOreName);
				logger.info(String.format("Eve Ore in Data Cache( %s ).  Saved some time!!!", eveOre.getEveItemName()));
			} else {
				eveOre = null;
				logger.error(String.format("Something is very wrong, attempt to get Ore, but class is not Ore class( %s ).", eveOreName));
			}
		} else {
			eveOre = new EveOre(new EveItemDb(eveOreName));
			this.addEveItem(eveOre);
		}
		return eveOre;
	}

	@Override
	public boolean eveRegionExists(long eveRegionId) {
		return eveRegionHashMap.containsKey(eveRegionId);
	}

	@Override
	public EveRegion getEveRegion(long eveRegionId) {
		Logger logger = EveDataLayerSingleton.LOGGER;

		EveRegion eveRegion;
		if (this.eveRegionExists(eveRegionId)) {
			eveRegion = eveRegionHashMap.get(eveRegionId);
			logger.info(String.format("Eve Region in Data Cache( %d ).  Saved some time!!!", eveRegion.getEveRegionId()));
		} else {
			EveRegionDb eveRegionDb = new EveRegionDb(eveRegionId);
			eveRegion = new EveRegion(eveRegionDb);
			this.addEveRegion(eveRegion);
			eveRegion.setEveSystemList(eveRegionDb.getEveSystemList());
			eveRegion.setEveRegionItemMaxOrderList();
		}
		return eveRegion;
	}

	@Lock(LockType.WRITE)
	private void addEveRegion(EveRegion eveRegion) {
		Logger logger = EveDataLayerSingleton.LOGGER;
		logger.info(String.format("Adding Eve Region to the Data Cache( %s ).", eveRegion.getEveRegionName()));
		this.eveRegionHashMap.put(eveRegion.getEveRegionId(), eveRegion);
	}

	@Override
	public boolean eveOreExists(String eveOreName) {
		return this.eveItemExists(eveOreName);
	}

	@Override
	public ArrayList<EveOre> getEveOreList() {
		return this.eveOreList;
	}

	@Lock(LockType.WRITE)
	private void setEveOreList() {
		ArrayList<EveOre> eveOreList = new ArrayList<EveOre>();
		for (String oreString : EveOre.AllORE) {
			EveOre eveOre = this.getEveOre(oreString);
			eveOreList.add(eveOre);
		}
		this.eveOreList = eveOreList;
	}
}