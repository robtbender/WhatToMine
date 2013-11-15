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

@RunWith(Arquillian.class)
public class EveStationDbTest {

	@Deployment
	public static JavaArchive createDeployment() {
		JavaArchive ja = ShrinkWrap.create(JavaArchive.class, "eveStationDB.jar");
		ja.addClasses(EveStationDb.class, EveDb.class);
		ja.addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
		return ja;
	}
	
	
	@Test
	public void testEveStationId() {
		EveStationDb eveStationDb = new EveStationDb(60007660);
		assertEquals(60007660, eveStationDb.getEveStationId());
	}
	
	@Test
	public void testEveStationName() {
		EveStationDb eveStationDb = new EveStationDb(60007660);
		assertEquals("Stou IV - Moon 1 - Nurtura Plantation", eveStationDb.getEveStationName());
	}
	
	@Test
	public void testEveStationSystemId() {
		EveStationDb eveStationDb = new EveStationDb(60007660);
		assertEquals(30005333, eveStationDb.getEveSystemId());
	}

}
