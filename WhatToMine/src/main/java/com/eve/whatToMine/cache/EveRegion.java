package com.eve.whatToMine.cache;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Set;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import com.eve.centralMarketInterface.CentralMarket;
import com.eve.whatToMine.data.EveOrder;
import com.eve.whatToMine.data.EveOrderListTimeStamp;
import com.eve.whatToMine.db.EveRegionDb;

public class EveRegion {

	private EveDataLayer eveDataLayer;
	{
		try {
			eveDataLayer = (EveDataLayer) new InitialContext().lookup("java:module/EveDataLayerSingleton");
		} catch (NamingException e) {
			e.printStackTrace();
		}
	}

	public static final String EveRegionNameEMPTYVALUE = "Eve Region Not Yet Initialized";
	public static final long EveRegionIdEMPTYVALUE = -1;

	private String eveItemName = EveRegionNameEMPTYVALUE;
	private long eveRegionId = EveRegionIdEMPTYVALUE;
	private ArrayList<EveSystem> eveSystemList = new ArrayList<EveSystem>();
	private HashMap<EveOre, EveOrderListTimeStamp> eveItemOrderHashMap = new HashMap<EveOre, EveOrderListTimeStamp>();
	private ArrayList<EveOrder> eveRegionItemMaxOrderList = new ArrayList<EveOrder>();

	public EveRegion(EveRegionDb eveRegionDb) {
		this.setEveRegionName(eveRegionDb.getEveRegionName());
		this.setEveRegionId(eveRegionDb.getEveRegionId());
	}

	public String getEveRegionName() {
		return this.eveItemName;
	}

	private void setEveRegionName(String eveItemName) {
		this.eveItemName = eveItemName;
	}

	public long getEveRegionId() {
		return this.eveRegionId;
	}

	private void setEveRegionId(long eveRegionId) {
		this.eveRegionId = eveRegionId;
	}

	public ArrayList<EveSystem> getEveSystemList() {
		return this.eveSystemList;
	}

	public void setEveSystemList(ArrayList<Long> eveSystemIdList) {
		for (long eveSystemId : eveSystemIdList) {
			this.eveSystemList.add(eveDataLayer.getEveSystem(eveSystemId));
		}
	}

	public ArrayList<EveOrder> getEveRegionItemMaxOrderList() {
		if ((this.eveRegionItemMaxOrderList == null) || (this.eveRegionItemMaxOrderList.size() == 0)) {
			this.setEveRegionItemMaxOrderList();
		}
		return this.eveRegionItemMaxOrderList;
	}
	
	public void setEveRegionItemMaxOrderList() {
		EveOre eveOre;
		EveOrder eveMaxItemOrder;
		
		this.eveRegionItemMaxOrderList = new ArrayList<EveOrder>();
		for (String oreString : EveOre.TestORE) {
			eveOre = eveDataLayer.getEveOre(oreString);
			eveMaxItemOrder = this.getEveItemOrders(eveOre).get(0);
			this.eveRegionItemMaxOrderList.add(eveMaxItemOrder);
		}
		if (this.eveRegionItemMaxOrderList.size() == 0) {
			eveOre = eveDataLayer.getEveOre("Veldspar");
			EveOrder eveOrder = new EveOrder(eveOre, this);
			eveOrder.setEmptyOrder();
			this.eveRegionItemMaxOrderList.add(eveOrder);
		}
		Collections.sort(this.eveRegionItemMaxOrderList, EveOrderListTimeStamp.ORE_COMPARATOR);
	}

	private ArrayList<EveOrder> getEveItemOrders(EveOre eveOre) {
		EveOrderListTimeStamp eveOrderListTimeStamp = this.getEveItemOrderHashMap().get(eveOre);

		if((eveOrderListTimeStamp == null)){
			this.setEveItemOrders(eveOre);
			eveOrderListTimeStamp = this.getEveItemOrderHashMap().get(eveOre);
		}else{
			long timeDiff = ((new Date().getTime()) - eveOrderListTimeStamp.getCentralMarketTimeStamp().getTime()) / 3600000;
			if (timeDiff > EveOrderListTimeStamp.MinRefreshHOURS) {
				this.setEveItemOrders(eveOre);
			}
		}
		return eveOrderListTimeStamp.getOrderList();
	}

	private void setEveItemOrders(EveOre eveOre) {
		EveOrderListTimeStamp eveOrderListTimeStamp = new EveOrderListTimeStamp();
		eveOrderListTimeStamp.setCentralMarketTimeStamp(new Date());
		eveOrderListTimeStamp.setOrderList(new CentralMarket().getRegionBuyOrderList(this, eveOre));
		eveOrderListTimeStamp.sortOrderList();
		this.getEveItemOrderHashMap().put(eveOre, eveOrderListTimeStamp);
	}

	public HashMap<EveOre, EveOrderListTimeStamp> getEveItemOrderHashMap() {
	    return this.eveItemOrderHashMap;
    }

	public Set<EveOre> getEveRegionItemList() {
	    return this.getEveItemOrderHashMap().keySet();
    }

	public EveOrder getEveRegionItemMaxOrderAvailable(EveSystem eveSystem, EveOre eveOre) {
		ArrayList<EveOrder> eveRegionItemOrderAvailableList = new ArrayList<>();
		
		EveOrder eveOrder = new EveOrder(eveOre, this);
		eveOrder.setEmptyOrder();
		
		for( EveOrder eveOrderLoop : this.getEveItemOrders(eveOre)){
			if(this.orderIsInRange(eveOrderLoop, eveSystem)){
				eveRegionItemOrderAvailableList.add(eveOrderLoop);
			}
		}
		if( eveRegionItemOrderAvailableList.size() > 0 ){
			Collections.sort(eveRegionItemOrderAvailableList, EveOrderListTimeStamp.ORE_COMPARATOR);
			eveOrder = eveRegionItemOrderAvailableList.get(0);
		}
	    return eveOrder;
    }

	private boolean orderIsInRange(EveOrder eveOrder, EveSystem toEveSystem) {
		boolean orderIsInRange = false;
		boolean rangeFound = false;
		boolean visitedSetContainsEveSystem;
		boolean unVisitedSetContainsEveSystem;
		boolean visitedSetContainsToEveSystem;

		if(eveOrder.getStation().getEveSystem().equals(toEveSystem)){
			return true;
		}
		
		if(eveOrder.getRange() >= 32767){
			return true;
		}

		ArrayList<DijkstraEveSystem> unVisitedSet = new ArrayList<>();
		ArrayList<DijkstraEveSystem> visitedSet = new ArrayList<>();
		
		unVisitedSet.add(new DijkstraEveSystem(eveOrder.getStation().getEveSystem(),0));
		Collections.sort(unVisitedSet, EveRegion.distanceCOMPARATOR);
		while( !rangeFound ){
			for( EveSystem eveSystem : unVisitedSet.get(0).getEveSystem().getJumpSystemList()){
				visitedSetContainsEveSystem = false;
				for (DijkstraEveSystem des : visitedSet) {
					if (des.getEveSystem().equals(eveSystem)){
						visitedSetContainsEveSystem = true;
						break;
					}
				}
				unVisitedSetContainsEveSystem = false;
				for (DijkstraEveSystem des : unVisitedSet) {
					if (des.getEveSystem().equals(eveSystem)){
						unVisitedSetContainsEveSystem = true;
						break;
					}
				}
				if( (!visitedSetContainsEveSystem) && (!unVisitedSetContainsEveSystem) ) {
					unVisitedSet.add(new DijkstraEveSystem(eveSystem,unVisitedSet.get(0).getDistance()+1));
				}
			}
			visitedSet.add(unVisitedSet.get(0));
			unVisitedSet.remove(0);
			visitedSetContainsToEveSystem = false;
			for (DijkstraEveSystem des : visitedSet) {
				if (des.getEveSystem().equals(toEveSystem)){
					visitedSetContainsToEveSystem = true;
					if( des.getDistance() <= eveOrder.getRange()){
						orderIsInRange = true;
					}
					break;
				}
			}
			if( visitedSetContainsToEveSystem ){
				rangeFound = true;
			}
			Collections.sort(unVisitedSet, EveRegion.distanceCOMPARATOR);
		}
	    return orderIsInRange;
	}
	
	private class DijkstraEveSystem {
		private EveSystem eveSystem;
		private int distance;

		public DijkstraEveSystem(EveSystem eveSystem, int distance) {
			this.setEveSystem(eveSystem);
			this.setDistance(distance);
		}

		public EveSystem getEveSystem() {
			return this.eveSystem;
		}

		public void setEveSystem(EveSystem eveSystem) {
			this.eveSystem = eveSystem;
		}

		public int getDistance() {
			return distance;
		}

		public void setDistance(int distance) {
			this.distance = distance;
		}
	}

	public static final Comparator<DijkstraEveSystem> distanceCOMPARATOR = new Comparator<DijkstraEveSystem>() {
		public int compare(DijkstraEveSystem dijkstraEveSystem1, DijkstraEveSystem dijkstraEveSystem2) {
			if ((dijkstraEveSystem1.getDistance() > dijkstraEveSystem1.getDistance())) {
				return -1;
			} else if ((dijkstraEveSystem1.getDistance() < dijkstraEveSystem1.getDistance())) {
				return 1;
			} else {
				return 0;
			}
		}
	};
}
