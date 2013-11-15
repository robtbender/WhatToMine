package com.eve.whatToMine.arq;

import static org.junit.Assert.*;

import java.util.ArrayList;
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
import com.eve.whatToMine.data.EveOrder;
import com.eve.whatToMine.data.EveOrderListTimeStamp;
import com.eve.whatToMine.db.EveDb;
import com.eve.whatToMine.db.EveItemDb;
import com.eve.whatToMine.db.EveRegionDb;
import com.eve.whatToMine.db.EveStationDb;
import com.eve.whatToMine.db.EveSystemDb;

@RunWith(Arquillian.class)
public class CentralMarketTest {

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
	public void testCentralMarket() {
		EveRegion eveRegion = eveDataLayer.getEveRegion(10000068);
		EveOre eveOre = eveDataLayer.getEveOre("Veldspar");
		CentralMarket centralMarket = new CentralMarket(eveRegion, eveOre);
		assertNotNull(centralMarket.getRegionBuyOrderList());
		assertTrue(centralMarket.getRegionBuyOrderList().getCentralMarketTimeStamp().getTime() <= (new Date().getTime()));
		assertTrue(centralMarket.getRegionBuyOrderList().getOrderList().size() > 1);
		if( centralMarket.getRegionBuyOrderList().getOrderList().size() > 0){
			EveOrder eveOrder1 = centralMarket.getRegionBuyOrderList().getOrderList().get(0);
			EveOrder eveOrder2 = centralMarket.getRegionBuyOrderList().getOrderList().get(1);
			assertTrue((eveOrder1.getPrice()/eveOrder1.getEveItem().getEveItemVolume()) >= (eveOrder2.getPrice()/eveOrder2.getEveItem().getEveItemVolume()));
		}
	}

	@Test
	public void testCentralMarketEmptyConstructor() {
		EveRegion eveRegion = eveDataLayer.getEveRegion(10000068);
		EveOre eveOre = eveDataLayer.getEveOre("Veldspar");
		ArrayList<EveOrder> eveOrderList = new ArrayList<EveOrder>();
		eveOrderList = new CentralMarket().getRegionBuyOrderList(eveRegion, eveOre);
		assertNotNull(eveOrderList);
		assertTrue(eveOrderList.size() > 1);
		if( eveOrderList.size() >= 2){
			EveOrder eveOrder1 = eveOrderList.get(0);
			EveOrder eveOrder2 = eveOrderList.get(1);
			assertTrue((eveOrder1.getPrice()/eveOrder1.getEveItem().getEveItemVolume()) >= (eveOrder2.getPrice()/eveOrder2.getEveItem().getEveItemVolume()));
		}
	}
}
