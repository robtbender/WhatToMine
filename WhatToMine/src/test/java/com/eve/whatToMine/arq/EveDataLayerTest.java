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
public class EveDataLayerTest {

	@Deployment
	public static JavaArchive createDeployment() {
		JavaArchive ja = ShrinkWrap.create(JavaArchive.class);
		ja.addClasses(EveDataLayer.class, EveDataLayerSingleton.class,
		        EveRegion.class, EveSystem.class, EveStation.class, EveItem.class, EveOre.class,
		        EveOrder.class, EveOrderListTimeStamp.class, CentralMarket.class,
		        EveRegionDb.class, EveSystemDb.class, EveStationDb.class, EveItemDb.class, EveDb.class);
		ja.addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
		System.out.println(ja.toString(true));
		return ja;
	}

	@EJB
	private EveDataLayer eveDataLayer;

	@Test
	@InSequence(1)
	public void testEveDataLayerExists() {
		assertNotNull(eveDataLayer);
	}

	@Test
	@InSequence(2)
	public void testEveDataLayerNewSystemExists() {
		assertFalse(eveDataLayer.eveSystemExists("Stou"));
	}

	@Test
	@InSequence(3)
	public void testEveDataLayerNewSystem() {
		EveSystem eveSystem = eveDataLayer.getEveSystem("Stou");
		assertEquals("Stou", eveSystem.getEveSystemName());
	}

	@Test
	@InSequence(4)
	public void testEveDataLayerSystemPersists() {
		assertTrue(eveDataLayer.eveSystemExists("Stou"));
		EveSystem eveSystem = eveDataLayer.getEveSystem("Stou");
		assertEquals("Stou", eveSystem.getEveSystemName());
	}

	@Test
	@InSequence(5)
	public void testEveDataLayerNewSystemIdExists() {
		assertFalse(eveDataLayer.eveSystemExists(30003843));
	}

	@Test
	@InSequence(6)
	public void testEveDataLayerNewSystemId() {
		EveSystem eveSystem = eveDataLayer.getEveSystem(30003843);
		assertEquals("Avaux", eveSystem.getEveSystemName());
	}

	@Test
	@InSequence(7)
	public void testEveDataLayerSystemIdPersists() {
		assertTrue(eveDataLayer.eveSystemExists(30003843));
		EveSystem eveSystem = eveDataLayer.getEveSystem(30003843);
		assertEquals("Avaux", eveSystem.getEveSystemName());
	}

	@Test
	@InSequence(8)
	public void testEveDataLayerNewStationExists() {
		assertFalse(eveDataLayer.eveStationExists(60012739));
	}

	@Test
	@InSequence(9)
	public void testEveDataLayerNewStation() {
		EveStation eveStation = eveDataLayer.getEveStation(60012739);
		assertEquals(60012739, eveStation.getEveStationId());
		assertEquals("Tar III - Secure Commerce Commission Depository", eveStation.getEveStationName());
	}

	@Test
	@InSequence(10)
	public void testEveDataLayerStationPersists() {
		assertTrue(eveDataLayer.eveStationExists(60012739));
		EveStation eveStation = eveDataLayer.getEveStation(60012739);
		assertEquals("Tar III - Secure Commerce Commission Depository", eveStation.getEveStationName());
	}

	@Test
	@InSequence(11)
	public void testEveDataLayerNewItemDoesNotExist() {
		assertFalse(eveDataLayer.eveItemExists("Isogen"));
	}

	@Test
	@InSequence(12)
	public void testEveDataLayerNewItem() {
		EveItem eveItem = eveDataLayer.getEveItem("Isogen");
		assertEquals("Isogen", eveItem.getEveItemName());
		assertEquals(37, eveItem.getEveItemId());
		assertEquals(0.01, eveItem.getEveItemVolume(), 0.0001);
	}

	@Test
	@InSequence(13)
	public void testEveDataLayerNewItemPersists() {
		assertTrue(eveDataLayer.eveItemExists("Isogen"));
		EveItem eveItem = eveDataLayer.getEveItem("Isogen");
		assertEquals(37, eveItem.getEveItemId());
	}

	@Test
	@InSequence(14)
	public void testEveDataLayerOre() {
		EveOre eveOre = eveDataLayer.getEveOre("Scordite");
		assertEquals("Scordite", eveOre.getEveItemName());
		assertEquals(1228, eveOre.getEveItemId());
		assertEquals(0.15, eveOre.getEveItemVolume(), 0.001);
		assertEquals(EveOre.HighSecORE, eveOre.getSecAvailabilty());
	}

	@Test
	@InSequence(16)
	public void testEveDataLayerNewRegionDoesNotExist() {
		assertFalse(eveDataLayer.eveRegionExists(10000052));
	}

	@Test
	@InSequence(17)
	public void testEveDataLayerNewRegion() {
		EveRegion eveRegion = eveDataLayer.getEveRegion(10000052);
		assertEquals("Kador", eveRegion.getEveRegionName());
	}

	@Test
	@InSequence(18)
	public void testEveDataLayerNewRegionPersists() {
		assertTrue(eveDataLayer.eveRegionExists(10000052));
		EveRegion eveRegion = eveDataLayer.getEveRegion(10000052);
		assertEquals("Kador", eveRegion.getEveRegionName());
	}

	@Test
	@InSequence(19)
	public void testEveSystemAsExpected() {
		EveSystem eveSystem = eveDataLayer.getEveSystem("Stou");
		assertEquals("Stou", eveSystem.getEveSystemName());
		assertEquals(30005333, eveSystem.getEveSystemId());
		boolean found = false;
		for (EveStation eveStation : eveSystem.getEveStationList()) {
			if (eveStation.getEveStationId() == 60007660) {
				found = true;
			}
		}
		assertTrue(found);
		found = false;
		for (EveSystem eveJumpSystem : eveSystem.getJumpSystemList()) {
			if (eveJumpSystem.getEveSystemId() == 30005330) {
				found = true;
			}
		}
		assertTrue(found);
	}

	@Test
	@InSequence(20)
	public void testEveDataLayerGetOreList() {
		assertTrue(eveDataLayer.getEveOreList().size() > 10);
	}
}
