package com.eve.whatToMine.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.naming.InitialContext;
import javax.naming.NamingException;

//import org.apache.log4j.Logger;


public class EveSystemDb {
	//TODO Add jump list for one jump away and only if in region.

	private EveDb eveDb;
	{
		try {
			eveDb = (EveDb) new InitialContext().lookup("java:module/EveDb");
		} catch (NamingException e) {
			e.printStackTrace();
		}
	}

	public final static String SolarSystemIDQUERY = "SELECT [solarSystemID],[regionID],[security] FROM [ebs_DATADUMP].[dbo].[mapSolarSystems] WHERE [solarSystemName] = '%s'";
	public final static String SolarSystemNameQUERY = "SELECT [solarSystemName],[regionID],[security] FROM [ebs_DATADUMP].[dbo].[mapSolarSystems] WHERE [solarSystemID] = %d";
	public final static String StationListQUERY = "SELECT [stationName],[stationID] FROM [ebs_DATADUMP].[dbo].[staStations] WHERE [solarSystemID] = %d";
	public final static String JumpListQUERY = "SELECT [toSolarSystemID] FROM [ebs_DATADUMP].[dbo].[mapSolarSystemJumps] WHERE [fromSolarSystemID] = %d AND [toRegionID] = %d";
	
	public static final String EveSystemNameEMPTYVALUE = "Eve System Not Yet Initialized";
	public static final long EveSystemIdEMTPYVALUE = -1;
	public static final long EveRegionIdEMTPYVALUE = -1;
	public static final double SecurityEMTPYVALUE = -100.0;
	public static final int MaxOrderRANGE = 5;
	
//	private final static Logger LOGGER = Logger.getLogger(EveSystemDb.class);
	
	private String eveSystemName = EveSystemNameEMPTYVALUE;
	private long eveSystemId = EveSystemIdEMTPYVALUE;
	private long eveRegionId = EveRegionIdEMTPYVALUE;
	private double security = SecurityEMTPYVALUE;
	private ArrayList<Long> eveStationList = new ArrayList<Long>();
	private ArrayList<Long> jumpSystemList = new ArrayList<Long>();
//	private HashMap<Long, Integer> orderRangeSystem = new HashMap<Long, Integer>();
//	private HashMap<Long, Integer> orderRangeStation = new HashMap<Long, Integer>();

	public EveSystemDb(String eveSystemName) {
		this.setEveSystemName(eveSystemName);
		ResultSet resultSet = eveDb.executeQuery(String.format(EveSystemDb.SolarSystemIDQUERY, this.getEveSystemName()));
		try {
			while (resultSet.next()) {
				this.setEveSystemId(resultSet.getLong("solarSystemID"));
				this.setEveRegionId(resultSet.getLong("regionID"));
				this.setSecurity(resultSet.getDouble("security"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		this.setEveStationList(this.getEveSystemId());
		this.setJumpSystemList(this.getEveSystemId(),this.getEveRegionId());
//		this.setOrderRangeSystem(this.getEveSystemId(), 0);
//		this.setOrderRangeStation(this.getOrderRangeSystem());
	}


	public EveSystemDb(long eveSystemId) {
		this.setEveSystemId(eveSystemId);
		ResultSet resultSet = eveDb.executeQuery(String.format(EveSystemDb.SolarSystemNameQUERY, this.getEveSystemId()));
		try {
			while (resultSet.next()) {
				this.setEveSystemName(resultSet.getString("solarSystemName"));
				this.setEveRegionId(resultSet.getLong("regionID"));
				this.setSecurity(resultSet.getDouble("security"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		this.setEveStationList(this.getEveSystemId());
		this.setJumpSystemList(this.getEveSystemId(),this.getEveRegionId());
    }


	public String getEveSystemName() {
		return this.eveSystemName;
	}

	private void setEveSystemName(String eveSystemName) {
		this.eveSystemName = eveSystemName;
	}

	public long getEveSystemId() {
		return this.eveSystemId;
	}

	private void setEveSystemId(long eveSystemId) {
		this.eveSystemId = eveSystemId;
	}

	public long getEveRegionId() {
		return this.eveRegionId;
	}

	private void setEveRegionId(long eveRegionId) {
		this.eveRegionId = eveRegionId;
	}

	public double getSecurity() {
	    return (double)Math.round(this.security * 10) / 10;
    }
	
	private void setSecurity(double security) {
		this.security = security;
    }

	public ArrayList<Long> getEveStationList() {
	    return this.eveStationList;
    }

	private void setEveStationList(long eveSystemId) {
		ResultSet resultSet = eveDb.executeQuery(String.format(EveSystemDb.StationListQUERY, eveSystemId));
		try {
			while (resultSet.next()) {
				this.eveStationList.add(resultSet.getLong("stationID"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
    }
	
	public ArrayList<Long> getJumpSystemList() {
	    return this.jumpSystemList;
    }

	private void setJumpSystemList(long eveSystemId, long eveRegionId) {
		ResultSet resultSet = eveDb.executeQuery(String.format(EveSystemDb.JumpListQUERY, eveSystemId, eveRegionId));
		try {
			while (resultSet.next()) {
				this.jumpSystemList.add(resultSet.getLong("toSolarSystemID"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
    }

//	public HashMap<Long, Integer> getOrderRangeSystem() {
//	    return this.orderRangeSystem;
//    }
//	
//	private void setOrderRangeSystem(long eveSystemId, int jumpsFromBase) {
//		Logger logger = EveSystemDb.LOGGER;
//		
//		logger.info(String.format("eveSystemID( %s ), jumpsFromBase( %d )", eveSystemId, jumpsFromBase));
//		
//		if( jumpsFromBase <= EveSystemDb.MaxOrderRANGE && !(this.orderRangeSystem.containsKey(eveSystemId)) ){
//			this.orderRangeSystem.put(eveSystemId, jumpsFromBase);
//			ResultSet resultSet = eveDb.executeQuery(String.format(EveSystemDb.JumpListQuery, eveSystemId));
//			try {
//				while (resultSet.next()) {
//					this.setOrderRangeSystem(resultSet.getLong("toSolarSystemID"), (jumpsFromBase+1));
//				}
//			} catch (SQLException e) {
//				e.printStackTrace();
//			}
//		}
//    }

//	public HashMap<Long, Integer> getOrderRangeStation() {
//	    return this.orderRangeStation;
//    }
//	
//	private void setOrderRangeStation(HashMap<Long, Integer> orderRangeSystem) {
//		for (long key: orderRangeSystem.keySet()) {
//			long eveSystemId = key;
//			int jumpsFromBase = orderRangeSystem.get(key);
//			ResultSet resultSet = eveDb.executeQuery(String.format(EveSystemDb.StationListQUERY, eveSystemId));
//			try {
//				while (resultSet.next()) {
//					long eveStationId = resultSet.getLong("stationID");
//					this.orderRangeStation.put(eveStationId, jumpsFromBase);
//				}
//			} catch (SQLException e) {
//				e.printStackTrace();
//			}
//		}
//    }



}

