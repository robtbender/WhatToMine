package com.eve.whatToMine.db;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.naming.InitialContext;
import javax.naming.NamingException;

public class EveStationDb {

    private EveDb eveDb;
	{
		try {
			eveDb = (EveDb) new InitialContext().lookup("java:module/EveDb");
		} catch (NamingException e) {
			e.printStackTrace();
		}
	}
	
	public final static String StationNameQUERY = "SELECT [stationName],[solarSystemID] FROM [ebs_DATADUMP].[dbo].[staStations] WHERE [stationID] = '%d'";
	
	public static final String EveStationNameEMPTYVALUE = "Eve System Not Yet Initialized";
	public static final long EveStationIdEMTPYVALUE = -1;
	public static final long EveSystemIdEMTPYVALUE = -1;

	private String eveStationName = EveStationNameEMPTYVALUE;
	private long eveStationId = EveStationIdEMTPYVALUE;
	private long eveSystemId = EveSystemIdEMTPYVALUE;

	public EveStationDb(long eveStationId) {
		this.setEveStationId(eveStationId);
		ResultSet resultSet = eveDb.executeQuery(String.format(EveStationDb.StationNameQUERY, eveStationId));
		try {
			while (resultSet.next()) {
				this.setEveStationName(resultSet.getString("stationName"));
				this.setEveSystemId(resultSet.getLong("solarSystemID"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
    }

	public long getEveStationId() {
	    return this.eveStationId;
    }

	private void setEveStationId(long eveStationId) {
		this.eveStationId = eveStationId;
    }

	public String getEveStationName() {
	    return this.eveStationName;
    }

	private void setEveStationName(String eveStationName) {
		this.eveStationName = eveStationName;
	}

	public long getEveSystemId() {
	    return this.eveSystemId;
    }
	
	private void setEveSystemId(long eveSystemId) {
	    this.eveSystemId = eveSystemId;
    }
}