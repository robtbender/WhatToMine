package com.eve.whatToMine.arq;

import static org.junit.Assert.*;

import javax.ejb.EJB;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.eve.centralMarketInterface.CentralMarket;
import com.eve.whatToMine.cache.EveDataLayer;
import com.eve.whatToMine.cache.EveDataLayerSingleton;
import com.eve.whatToMine.cache.EveItem;
import com.eve.whatToMine.cache.EveOre;
import com.eve.whatToMine.cache.EveRegion;
import com.eve.whatToMine.cache.EveStation;
import com.eve.whatToMine.cache.EveSystem;
import com.eve.whatToMine.data.EveOrder;
import com.eve.whatToMine.data.EveOrderListTimeStamp;
import com.eve.whatToMine.db.EveDb;
import com.eve.whatToMine.db.EveItemDb;
import com.eve.whatToMine.db.EveRegionDb;
import com.eve.whatToMine.db.EveStationDb;
import com.eve.whatToMine.db.EveSystemDb;

@RunWith(Arquillian.class)
public class EveOrderTest {
	
	@Deployment
	public static JavaArchive createDeployment() {
		JavaArchive ja = ShrinkWrap.create(JavaArchive.class);
		ja.addClasses(EveDataLayer.class, EveDataLayerSingleton.class,
				EveOrder.class, EveOrderListTimeStamp.class, CentralMarket.class,
		        EveRegion.class, EveSystem.class, EveStation.class, EveItem.class, EveOre.class,
		        EveRegionDb.class, EveSystemDb.class, EveStationDb.class, EveItemDb.class, EveDb.class);
		ja.addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
		System.out.println(ja.toString(true));
		return ja;
	}
	
	@EJB
	private EveDataLayer eveDataLayer;

	@Test
	public void testEveOrderItemIdConstructor() {
		EveOre eveOre = eveDataLayer.getEveOre("Veldspar"); 
		EveRegion eveRegion = eveDataLayer.getEveRegion(10000068);
		EveOrder eveOrder = new EveOrder(eveOre, eveRegion);
		assertEquals(1230, eveOrder.getEveItem().getEveItemId());
		assertEquals(10000068, eveOrder.getEveRegion().getEveRegionId());
	}
	
	@Test
	public void testEmptyEveOrder() {
		EveOre eveOre = eveDataLayer.getEveOre("Veldspar");
		EveRegion eveRegion = eveDataLayer.getEveRegion(10000068);
		EveOrder eveOrder = new EveOrder(eveOre, eveRegion);
		eveOrder.setEmptyOrder();
		assertEquals(1230, eveOrder.getEveItem().getEveItemId());
	}
	
	@Test
	public void testEveOrderPrice() {
		EveOre eveOre = eveDataLayer.getEveOre("Veldspar");
		EveRegion eveRegion = eveDataLayer.getEveRegion(10000068);
		EveOrder eveOrder = new EveOrder(eveOre, eveRegion);
		EveStation eveStation = eveDataLayer.getEveStation(60007660);
		eveOrder.setStation(eveStation);
		eveOrder.setPrice(0.11);
		eveOrder.setRange(4);
		assertEquals(eveStation.getEveStationName(), eveOrder.getStation().getEveStationName());
		assertTrue(eveOrder.getPrice() > 0);
		assertEquals( 4, eveOrder.getRange());
	}


}
