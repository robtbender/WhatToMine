package com.eve.whatToMine.arq;

import static org.junit.Assert.*;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.eve.whatToMine.db.EveDb;
import com.eve.whatToMine.db.EveStationDb;
import com.eve.whatToMine.db.EveSystemDb;

@RunWith(Arquillian.class)
public class EveSystemDbTest {

	@Deployment
	public static JavaArchive createDeployment() {
		JavaArchive ja = ShrinkWrap.create(JavaArchive.class);
		ja.addClasses(EveSystemDb.class, EveStationDb.class, EveDb.class);
		ja.addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
		System.out.println(ja.toString(true));
		return ja;
	}

	@Test
	public void testEveSystemNameConstructor() {
		EveSystemDb eveSystemDb = new EveSystemDb("Stou");
		assertEquals("Stou", eveSystemDb.getEveSystemName());
		assertEquals(30005333, eveSystemDb.getEveSystemId());
		assertEquals(10000068, eveSystemDb.getEveRegionId());
		assertEquals(0.5, eveSystemDb.getSecurity(), 0.001);
		boolean found = false;
		for (Long eveStationId : eveSystemDb.getEveStationList()) {
			if (eveStationId == 60007660) {
				found = true;
			}
		}
		assertTrue(found);
		found = false;
		for (Long eveSystemId : eveSystemDb.getJumpSystemList()) {
			if (eveSystemId == 30005330) {
				found = true;
			}
		}
		assertTrue(found);

//		found = false;
//		for (long key : eveSystemDb.getOrderRangeSystem().keySet()) {
//			long eveSystemId = key;
//			int jumpsFromBase = eveSystemDb.getOrderRangeSystem().get(key);
//			// System.out.format("eveSystemId( %s ), jumpsFromBase( %s ). %n", eveSystemId, jumpsFromBase);
//			if ((eveSystemId == 30004973) && (jumpsFromBase == 4)) {
//				found = true;
//			}
//		}
//		assertTrue(found);
//		found = false;
//		for (long key : eveSystemDb.getOrderRangeStation().keySet()) {
//			long eveStationId = key;
//			int jumpsFromBase = eveSystemDb.getOrderRangeStation().get(key);
//			// System.out.format("eveStationId( %s ), jumpsFromBase( %s ). %n", eveStationId, jumpsFromBase);
//			if ((eveStationId == 60009460) && (jumpsFromBase == 5)) {
//				found = true;
//			}
//		}
//		assertTrue(found);
	}

	@Test
	public void testEveSystemNameConstructorFail() {
		EveSystemDb eveSystemDb = new EveSystemDb("RMB FAIL");
		assertEquals("RMB FAIL", eveSystemDb.getEveSystemName());
		assertEquals(EveSystemDb.EveSystemIdEMTPYVALUE, eveSystemDb.getEveSystemId());
		assertEquals(EveSystemDb.EveRegionIdEMTPYVALUE, eveSystemDb.getEveRegionId());
		assertEquals(EveSystemDb.SecurityEMTPYVALUE, eveSystemDb.getSecurity(), 0.001);
		boolean found = false;
		for (Long eveStationId : eveSystemDb.getEveStationList()) {
			if (eveStationId == 60007660) {
				found = true;
			}
		}
		assertFalse(found);
		found = false;
		for (Long eveSystemId : eveSystemDb.getJumpSystemList()) {
			if (eveSystemId == 30005330) {
				found = true;
			}
		}
		assertFalse(found);

	}

	@Test
	public void testEveSystemIdConstructor() {
		EveSystemDb eveSystemDb = new EveSystemDb(30005333);
		assertEquals("Stou", eveSystemDb.getEveSystemName());
		assertEquals(30005333, eveSystemDb.getEveSystemId());
		assertEquals(10000068, eveSystemDb.getEveRegionId());
		assertEquals(0.5, eveSystemDb.getSecurity(), 0.001);
		boolean found = false;
		for (Long eveStationId : eveSystemDb.getEveStationList()) {
			if (eveStationId == 60007660) {
				found = true;
			}
		}
		assertTrue(found);
	}
}
