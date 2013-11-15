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
public class EveSystemTest {

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
	public void testEveSystemEveSystemDBConstructor() {
		EveSystemDb eveSystemDb = new EveSystemDb("Stou");
		EveSystem eveSystem = new EveSystem(eveSystemDb);
		eveSystem.setEveRegion(eveDataLayer.getEveRegion(eveSystemDb.getEveRegionId()));
		eveSystem.setEveStationList(eveSystemDb.getEveStationList());
		eveSystem.setJumpSystemList(eveSystemDb.getJumpSystemList());
		assertEquals("Stou", eveSystem.getEveSystemName());
		assertEquals(30005333, eveSystem.getEveSystemId());
		assertEquals(10000068, eveSystem.getEveRegion().getEveRegionId());
		assertEquals(0.5, eveSystem.getSecurity(), 0.01);
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
	public void testEveSystemEveSystemDataLayer() {
		EveSystem eveSystem = eveDataLayer.getEveSystem("Stou");
		eveSystem.setEveSystemItemMaxAvailableList();
		assertEquals("Stou", eveSystem.getEveSystemName());
		assertEquals(30005333, eveSystem.getEveSystemId());
		assertEquals(10000068, eveSystem.getEveRegion().getEveRegionId());
		assertEquals(0.5, eveSystem.getSecurity(), 0.01);
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
		assertNotNull(eveSystem.getEveSystemItemMaxAvailableList());
		assertTrue(eveSystem.getEveSystemItemMaxAvailableList().size() > 0);
		for( EveOrder eveOrder : eveSystem.getEveSystemItemMaxAvailableList()){
			System.out.format("Item( %s ), Station( %s ), Range( %d ), Price( %.2f ).%n", 
					eveOrder.getEveItem().getEveItemName(), eveOrder.getStation().getEveStationName(), eveOrder.getRange(), eveOrder.getPrice());
		}
	}

}