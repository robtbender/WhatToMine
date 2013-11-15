package com.eve.whatToMine.arq;

import static org.junit.Assert.*;

import javax.ejb.EJB;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
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
public class EveStationTest {
	
	@Deployment
	public static JavaArchive createDeployment() {
		JavaArchive ja = ShrinkWrap.create(JavaArchive.class);
		ja.addClasses(EveDataLayer.class, EveDataLayerSingleton.class,
		        EveRegion.class, EveSystem.class, EveStation.class, EveItem.class, EveOre.class, 
		        EveOrder.class, EveOrderListTimeStamp.class, CentralMarket.class,
		        EveRegionDb.class, EveSystemDb.class, EveStationDb.class, EveItemDb.class, EveDb.class);
		ja.addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
		return ja;
	}
	
	@EJB
	private EveDataLayer eveDataLayer;

	@Test
	@InSequence(1)
	public void testEveStationId() {
		EveStation eveStation = new EveStation(new EveStationDb(60007660));
		assertEquals(60007660, eveStation.getEveStationId());
	}
	
	@Test
	@InSequence(2)
	public void testEveStationName() {
		EveStation eveStation = new EveStation(new EveStationDb(60007660));
		assertEquals("Stou IV - Moon 1 - Nurtura Plantation", eveStation.getEveStationName());
	}
	
	@Test
	@InSequence(3)
	public void testEveStationSystem() {
		EveSystem eveSystem = eveDataLayer.getEveSystem("Stou");
		EveStationDb eveStationDb = new EveStationDb(60007660);
		EveStation eveStation = new EveStation(eveStationDb);
		eveStation.setEveSystem(eveDataLayer.getEveSystem(eveStationDb.getEveSystemId()));
		assertEquals(eveSystem, eveStation.getEveSystem());
	}
	
	@Test
	@InSequence(4)
	public void testEveStationEmptyStation() {
		EveStation eveStation = new EveStation(EveStation.EveStationIdEMTPYVALUE, "No valid orders for System");
		assertEquals(EveStation.EveStationIdEMTPYVALUE, eveStation.getEveStationId());
		assertEquals("No valid orders for System", eveStation.getEveStationName());
	}
}
