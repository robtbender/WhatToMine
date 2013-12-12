package com.eve.whatToMine.arq;


import static org.junit.Assert.*;

import java.util.Date;

import org.junit.Test;

import com.eve.centralMarketInterface.CentralMarket;
import com.eve.whatToMine.data.DatedEveOrderList;

public class DatedEveOrderListTest {

	@Test
	public void testEmptyConstructor() {
		DatedEveOrderList datedEveOrderList = new DatedEveOrderList(); 
		assertNotNull(datedEveOrderList);
	}
	
	@Test
	public void testEveOrderListIsNotNull() {
		DatedEveOrderList datedEveOrderList = new DatedEveOrderList(); 
		assertNotNull(datedEveOrderList.getOrderList());
	}
	
	@Test
	public void testSetEveOrderListTimeStamp() {
		DatedEveOrderList datedEveOrderList = new DatedEveOrderList();
		datedEveOrderList.setCentralMarketTimeStamp(new Date());
		assertTrue(datedEveOrderList.getCentralMarketTimeStamp().getTime() <= (new Date().getTime()));
	}
	
}

//DatedEveOrderList datedEveOrderList = new DatedEveOrderList();
//datedEveOrderList.setCentralMarketTimeStamp(new Date());
//datedEveOrderList.setOrderList(new CentralMarket().getRegionBuyOrderList(this, eveOre));
//datedEveOrderList.sortOrderList();