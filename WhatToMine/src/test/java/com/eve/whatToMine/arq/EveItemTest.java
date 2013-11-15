package com.eve.whatToMine.arq;

import static org.junit.Assert.*;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.eve.whatToMine.cache.EveItem;
import com.eve.whatToMine.db.EveDb;
import com.eve.whatToMine.db.EveItemDb;

@RunWith(Arquillian.class)
public class EveItemTest {

	@Deployment
	public static JavaArchive createDeployment() {
		JavaArchive ja = ShrinkWrap.create(JavaArchive.class);
		ja.addClasses(EveItem.class, EveItemDb.class, EveDb.class);
		ja.addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
		System.out.println(ja.toString(true));
		return ja;
	}
	
	@Test
	public void testEveItemEveItemDbConstructor(){
		EveItem eveItem = new EveItem(new EveItemDb("Veldspar"));
		assertEquals("Veldspar", eveItem.getEveItemName());
		assertEquals(1230,eveItem.getEveItemId());
		assertEquals(0.1,eveItem.getEveItemVolume(), 0.001);
	}
}
