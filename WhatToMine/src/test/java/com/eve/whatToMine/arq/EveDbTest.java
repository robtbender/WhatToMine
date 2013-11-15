package com.eve.whatToMine.arq;

import static org.junit.Assert.*;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.eve.whatToMine.db.EveDb;

@RunWith(Arquillian.class)
public class EveDbTest {
	
	@Deployment
	public static JavaArchive createDeployment() {
		JavaArchive ja = ShrinkWrap.create(JavaArchive.class);
		ja.addClasses(EveDb.class);
		ja.addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
		System.out.println(ja.toString(true));
		return ja;
	}

	@Inject
	private EveDb eveDb;

	@Test
	public void testConnectionStatus() {
		assertEquals("Up", eveDb.getConnectionStatus());
	}

	@Test
	public void testEveDBSimpleQuery() {
		long solarSystemID = -1;
		ResultSet resultSet = eveDb.executeQuery(String.format(EveDb.GETSYTEMIDQUERY, "Stou"));
		try {
	        while (resultSet.next()) {
	        	solarSystemID = resultSet.getLong("solarSystemID");
	        }
        } catch (SQLException e) {
	        e.printStackTrace();
        }
		assertEquals(30005333, solarSystemID);
	}
	
	@Test
	public void testEveDBSimpleQueryNoDataFound() {
		long solarSystemID = -1;
		ResultSet resultSet = eveDb.executeQuery(String.format(EveDb.GETSYTEMIDQUERY, "ABCD"));
		try {
	        while (resultSet.next()) {
	        	solarSystemID = resultSet.getLong("solarSystemID");
	        }
	        resultSet = null;
        } catch (SQLException e) {
	        e.printStackTrace();
        }
		assertEquals(-1, solarSystemID);
	}


}
