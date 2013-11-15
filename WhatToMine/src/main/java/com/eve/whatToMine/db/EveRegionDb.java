package com.eve.whatToMine.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.naming.InitialContext;
import javax.naming.NamingException;

public class EveRegionDb {

	private EveDb eveDb;
	{
		try {
			eveDb = (EveDb) new InitialContext().lookup("java:module/EveDb");
		} catch (NamingException e) {
			e.printStackTrace();
		}
	}

	public final static String RegionIdQUERY = "SELECT [regionName] FROM [ebs_DATADUMP].[dbo].[mapRegions] WHERE [regionID] = ' %d'";
	public final static String SolarSystemQUERY = "SELECT [solarSystemID] FROM [ebs_DATADUMP].[dbo].[mapSolarSystems] WHERE [regionID] = '%d'";

	public static final String EveRegionNameEMPTYVALUE = "Eve Region Not Yet Initialized";
	public static final long EveRegionIdEMPTYVALUE = -1;

	private String eveRegionName = EveRegionNameEMPTYVALUE;
	private long eveRegionId = EveRegionIdEMPTYVALUE;
	private ArrayList<Long> eveSystemList = new ArrayList<Long>(); 

	public EveRegionDb(long eveRegionId) {
		this.setEveRegionId(eveRegionId);
		this.setEveRegionName();
		this.setEveSystemList();
	}

	public String getEveRegionName() {
		return this.eveRegionName;
	}

	private void setEveRegionName() {
		ResultSet resultSet = eveDb.executeQuery(String.format(EveRegionDb.RegionIdQUERY, this.getEveRegionId()));
		try {
			while (resultSet.next()) {
				this.eveRegionName = resultSet.getString("regionName");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public long getEveRegionId() {
		return this.eveRegionId;
	}

	private void setEveRegionId(long eveRegionId) {
		this.eveRegionId = eveRegionId;
	}

	public ArrayList<Long> getEveSystemList() {
	    return this.eveSystemList;
    }

	private void setEveSystemList() {
		ResultSet resultSet = eveDb.executeQuery(String.format(EveRegionDb.SolarSystemQUERY, this.getEveRegionId()));
		try {
			while (resultSet.next()) {
				this.eveSystemList.add(resultSet.getLong("solarSystemID"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
    }
}
