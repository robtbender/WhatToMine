package com.eve.whatToMine.cache;

import java.util.ArrayList;

import javax.ejb.Local;

import com.eve.whatToMine.cache.EveSystem;

@Local
public interface EveDataLayer {

	boolean eveSystemExists(String eveSystemName);

	EveSystem getEveSystem(String eveSystemName);

	boolean eveSystemExists(long eveSystemId);

	EveSystem getEveSystem(long eveSystemId);

	boolean eveStationExists(long eveStationId);

	EveStation getEveStation(long eveStationId);

	boolean eveItemExists(String eveItemName);

	EveItem getEveItem(String eveItemName);

	boolean eveRegionExists(long eveRegionId);

	EveRegion getEveRegion(long eveRegionId);

	boolean eveOreExists(String eveOreName);

	EveOre getEveOre(String eveOreName);

	ArrayList<EveOre> getEveOreList();


}
