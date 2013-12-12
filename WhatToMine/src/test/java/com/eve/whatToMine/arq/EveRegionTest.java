package com.eve.whatToMine.arq;

import static org.junit.Assert.*;

import java.util.Set;

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
import com.eve.whatToMine.data.DatedEveOrderList;
import com.eve.whatToMine.db.EveDb;
import com.eve.whatToMine.db.EveItemDb;
import com.eve.whatToMine.db.EveRegionDb;
import com.eve.whatToMine.db.EveStationDb;
import com.eve.whatToMine.db.EveSystemDb;

@RunWith(Arquillian.class)
public class EveRegionTest {
	
	@Deployment
	public static JavaArchive createDeployment() {
		JavaArchive ja = ShrinkWrap.create(JavaArchive.class);
		ja.addClasses(EveDataLayer.class, EveDataLayerSingleton.class,
				EveOrder.class, DatedEveOrderList.class, CentralMarket.class,
		        EveRegion.class, EveSystem.class, EveStation.class, EveItem.class, EveOre.class,
		        EveRegionDb.class, EveSystemDb.class, EveStationDb.class, EveItemDb.class, EveDb.class);
		ja.addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
		System.out.println(ja.toString(true));
		return ja;
	}
	
	@EJB
	private EveDataLayer eveDataLayer;
	
	@Test
	public void testEveRegionEveRegionDbConstructor(){
		EveRegionDb eveRegionDb = new EveRegionDb(10000068);
		EveRegion eveRegion = new EveRegion(eveRegionDb);
		eveRegion.setEveSystemList(eveRegionDb.getEveSystemList());
		assertEquals(10000068, eveRegion.getEveRegionId());
		assertEquals("Verge Vendor", eveRegion.getEveRegionName());
		boolean found = false;
		for (EveSystem eveSystem : eveRegion.getEveSystemList()) {
			if (eveSystem.getEveSystemId() == 30005333) {
				found = true;
			}
		}
		assertTrue(found);
	}
	
	@Test
	public void testEveRegionEveRegionDataLayer(){
		EveSystem eveAvailSystem =  null;

		EveRegion eveRegion = eveDataLayer.getEveRegion(10000068);
		assertEquals(10000068, eveRegion.getEveRegionId());
		assertEquals("Verge Vendor", eveRegion.getEveRegionName());
		assertTrue(eveRegion.getEveRegionItemMaxOrderList().size() > 0);
		System.out.format("-----%n -- Region Orders -- %n-----%n");
		for( EveOrder eveOrder : eveRegion.getEveRegionItemMaxOrderList()){
			System.out.format("Item( %s ), Station( %s ), Range( %d ), Price( %.2f ).%n", 
					eveOrder.getEveItem().getEveItemName(), eveOrder.getStation().getEveStationName(), eveOrder.getRange(), eveOrder.getPrice());
		}
		boolean found = false;
		for (EveSystem eveSystem : eveRegion.getEveSystemList()) {
			if (eveSystem.getEveSystemId() == 30005333) {
				found = true;
				eveAvailSystem = eveSystem; 
			}
		}
		assertTrue(found);
		assertNotNull(eveRegion.getEveRegionItemList());
		Set<EveOre> eveOreSet = eveRegion.getEveRegionItemList();
		System.out.format("-----%n -- Avail Orders for ( %s )-- %n-----%n", eveAvailSystem.getEveSystemName());
		for( EveOre eveOre : eveOreSet){
			EveOrder eveOrder = eveRegion.getEveRegionItemMaxOrderAvailable(eveAvailSystem,eveOre);
			assertNotNull(eveOrder);
			System.out.format("Item( %s ), Station( %s ), Range( %d ), Price( %.2f ).%n", 
					eveOrder.getEveItem().getEveItemName(), eveOrder.getStation().getEveStationName(), eveOrder.getRange(), eveOrder.getPrice());
		}
	}

}
