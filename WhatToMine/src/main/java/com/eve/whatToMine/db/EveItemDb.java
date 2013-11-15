package com.eve.whatToMine.db;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.naming.InitialContext;
import javax.naming.NamingException;

public class EveItemDb {

	private EveDb eveDb;
	{
		try {
			eveDb = (EveDb) new InitialContext().lookup("java:module/EveDb");
		} catch (NamingException e) {
			e.printStackTrace();
		}
	}

	public final static String ItemIdQUERY = "SELECT [typeID],[volume] FROM [ebs_DATADUMP].[dbo].[invTypes] WHERE [typeName] = '%s'";
			
	public static final String EveItemNameEMPTYVALUE = "Eve Item Not Yet Initialized";
	public static final long EveItemIdEMPTYVALUE = -1;
	public static final double EveItemVolumeEMPTYVALUE = -1.0;

	private String eveItemName = EveItemNameEMPTYVALUE;
	private long eveItemId = EveItemIdEMPTYVALUE;
	private double eveItemVolume = EveItemVolumeEMPTYVALUE;

	public EveItemDb(String eveItemName) {
		this.setEveItemName(eveItemName);
		ResultSet resultSet = eveDb.executeQuery(String.format(EveItemDb.ItemIdQUERY, this.getEveItemName()));
		try {
			while (resultSet.next()) {
				this.setEveItemId(resultSet.getLong("typeID"));
				this.setEveItemVolume(resultSet.getDouble("volume"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public String getEveItemName() {
		return this.eveItemName;
	}

	private void setEveItemName(String eveItemName) {
		this.eveItemName = eveItemName;
	}

	public long getEveItemId() {
		return this.eveItemId;
    }

	private void setEveItemId(long eveItemId) {
		this.eveItemId = eveItemId;
    }

	public double getEveItemVolume() {
		return this.eveItemVolume;
    }

	private void setEveItemVolume(double eveItemVolume) {
		this.eveItemVolume = eveItemVolume;
    }
}
