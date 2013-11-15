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
import com.eve.whatToMine.db.EveItemDb;

@RunWith(Arquillian.class)
public class EveItemDbTest {

	@Deployment
	public static JavaArchive createDeployment() {
		JavaArchive ja = ShrinkWrap.create(JavaArchive.class);
		ja.addClasses(EveItemDb.class, EveDb.class);
		ja.addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
		System.out.println(ja.toString(true));
		return ja;
	}

	@Test
	public void testEveItemNamedConstructor() {
		EveItemDb eveItemDb = new EveItemDb("Veldspar");
		assertEquals("Veldspar", eveItemDb.getEveItemName());
		assertEquals(1230,eveItemDb.getEveItemId());
		assertEquals(0.1,eveItemDb.getEveItemVolume(), 0.001);
	}

}
