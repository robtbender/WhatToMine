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
import com.eve.whatToMine.db.EveRegionDb;

@RunWith(Arquillian.class)
public class EveRegionDbTest {

	@Deployment
	public static JavaArchive createDeployment() {
		JavaArchive ja = ShrinkWrap.create(JavaArchive.class);
		ja.addClasses(EveRegionDb.class, EveDb.class);
		ja.addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
		System.out.println(ja.toString(true));
		return ja;
	}
	
	@Test
	public void testEveRegionDbRegionIdConstructor() {
		EveRegionDb eveRegionDb = new EveRegionDb(10000068);
		assertEquals("Verge Vendor", eveRegionDb.getEveRegionName());
		boolean found = false;
		for (long eveStationId : eveRegionDb.getEveSystemList()) {
			if (eveStationId == 30005333) {
				found = true;
			}
		}
		assertTrue(found);
	}
}
