package com.eve.whatToMine.junit;

import static org.junit.Assert.*;

import java.util.Date;

import org.junit.Test;

import com.eve.whatToMine.data.EveOrderListTimeStamp;

public class EveOrderListTimeStampTest {

	@Test
	public void testEmptyConstructor() {
		EveOrderListTimeStamp eveOrderListTimeStamp = new EveOrderListTimeStamp(); 
		assertNotNull(eveOrderListTimeStamp);
	}
	
	@Test
	public void testEveOrderListIsNotNull() {
		EveOrderListTimeStamp eveOrderListTimeStamp = new EveOrderListTimeStamp(); 
		assertNotNull(eveOrderListTimeStamp.getOrderList());
	}
	
	@Test
	public void testSetEveOrderListTimeStamp() {
		EveOrderListTimeStamp eveOrderListTimeStamp = new EveOrderListTimeStamp();
		eveOrderListTimeStamp.setCentralMarketTimeStamp(new Date());
		assertTrue(eveOrderListTimeStamp.getCentralMarketTimeStamp().getTime() <= (new Date().getTime()));
	}
	
}
