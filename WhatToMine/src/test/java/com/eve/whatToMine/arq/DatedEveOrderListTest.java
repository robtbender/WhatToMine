package com.eve.whatToMine.arq;

import static org.junit.Assert.*;

import java.util.Date;

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
import com.eve.whatToMine.data.DatedEveOrderList;
import com.eve.whatToMine.data.EveOrder;
import com.eve.whatToMine.db.EveDb;
import com.eve.whatToMine.db.EveItemDb;
import com.eve.whatToMine.db.EveRegionDb;
import com.eve.whatToMine.db.EveStationDb;
import com.eve.whatToMine.db.EveSystemDb;

@RunWith(Arquillian.class)
public class DatedEveOrderListTest {

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

	@Test
	public void testSetEveOrderListOrderList() {
		EveRegion eveRegion = eveDataLayer.getEveRegion(10000068);
		EveOre eveOre = eveDataLayer.getEveOre("Veldspar");
		DatedEveOrderList datedEveOrderList = new DatedEveOrderList();
		datedEveOrderList.setCentralMarketTimeStamp(new Date());
		datedEveOrderList.setOrderList(new CentralMarket().getRegionBuyOrderList(eveRegion, eveOre));
		datedEveOrderList.sortOrderList();
		if (datedEveOrderList.getOrderList().size() >= 2) {
			EveOrder eveOrder1 = datedEveOrderList.getOrderList().get(0);
			EveOrder eveOrder2 = datedEveOrderList.getOrderList().get(1);
			assertTrue((eveOrder1.getPrice() / eveOrder1.getEveItem().getEveItemVolume()) >= (eveOrder2.getPrice() / eveOrder2.getEveItem().getEveItemVolume()));
		}
	}
}
