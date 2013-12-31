package com.eve.whatToMine.arq;

import static org.junit.Assert.*;

import javax.ejb.EJB;
import javax.inject.Inject;

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
import com.eve.whatToMine.web.UserSession;

@RunWith(Arquillian.class)
public class UserSessionTest {

	@Deployment
	public static JavaArchive createDeployment() {
		JavaArchive ja = ShrinkWrap.create(JavaArchive.class, "userSessionTest.jar");
		ja.addClasses(UserSession.class, EveDataLayer.class, EveDataLayerSingleton.class, 
				EveRegion.class, EveSystem.class, EveStation.class, EveItem.class, EveOre.class, 
				EveOrder.class, DatedEveOrderList.class, CentralMarket.class,
				EveRegionDb.class, EveSystemDb.class, EveStationDb.class, EveItemDb.class, EveDb.class);
		ja.addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
		return ja;
	}

	@Inject
	private UserSession userSession;
	
	@Test
	public void testEveSystemName() {
		userSession.setInputEveSystemName("Stou");
		assertEquals("Stou", userSession.getInputEveSystemName());
	}

	@Test
	public void testEveSystemEnteredPre() {
		assertFalse(userSession.getEveSystemEntered());
	}

	@Test
	public void testEveSystemEnteredPost() {
		userSession.setInputEveSystemName("Stou");
		assertTrue(userSession.getEveSystemEntered());
	}
	
	@Test
	public void testEveSystem() {
		userSession.setInputEveSystemName("Stou");
		assertEquals("Stou", userSession.getEveSystem().getEveSystemName());
		assertEquals(30005333, userSession.getEveSystem().getEveSystemId());
		assertEquals(10000068, userSession.getEveSystem().getEveRegion().getEveRegionId());
		boolean found = false;
		for( EveStation eveStation : userSession.getEveSystem().getEveStationList() ){
			if(eveStation.getEveStationId() == 60007660){
				found = true;
			}
		}
		assertTrue(found);
		assertTrue(userSession.getEveSystem().getEveRegion().getEveRegionItemMaxOrderList().size() > 0);
		assertTrue(userSession.getEveSystem().getEveSystemItemMaxAvailableList().size() > 0);
	}
}
