package com.eve.whatToMine.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.annotation.Resource;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Stateless;
import javax.sql.DataSource;

import org.apache.log4j.Logger;

@Stateless
public class EveDb {

	@Resource(mappedName = "java:jboss/datasources/WhatToMineDS")
	DataSource eveSystemDB;

	private final static Logger LOGGER = Logger.getLogger(EveDb.class);

	public final static String GETSYTEMIDQUERY = "SELECT [solarSystemID] FROM [ebs_DATADUMP].[dbo].[mapSolarSystems] WHERE [solarSystemName] = '%s'";
	
	private long startTime;
	private long stopTime;
	
	@PostConstruct
	private void setStartTime(){
		Logger logger = EveDb.LOGGER;

		startTime = System.currentTimeMillis();
		logger.info(String.format("Starting EveDB connector, time start in millis( %d ).", startTime));
	}
	
	@PreDestroy
	private void setEndTime(){
		Logger logger = EveDb.LOGGER;

		stopTime = System.currentTimeMillis();
		logger.info(String.format("Destroying EveDB connector, end time in millis( %d ), runtime( %d ).", stopTime, (stopTime - startTime)));
	}


	public String getConnectionStatus() {
		try {
			@SuppressWarnings("unused")
			ResultSet resultSet = eveSystemDB.getConnection().getMetaData().getClientInfoProperties();
		} catch (SQLException e) {
			e.printStackTrace();
			return "Failure";
		}
		return "Up";
	}

	public ResultSet executeQuery(String SQLString) {
		Logger logger = EveDb.LOGGER;

		ResultSet resultSet = null;
		logger.info(String.format("SQLString( %s ).", SQLString));

		Connection connection = null;
		try {
			connection = eveSystemDB.getConnection();
			Statement statement = eveSystemDB.getConnection().createStatement();
			resultSet = statement.executeQuery(SQLString);
		} catch (SQLException e) {
			logger.error(String.format("Critical error attempting to query to the eveSystemDB DataSource, cause( %s ).", e.getCause().toString()));
		} finally {
			try {
	            connection.close();
            } catch (SQLException e) {
            	logger.error(String.format("Critical error attempting to close the connection to the eveSystemDB DataSource, cause( %s ).", e.getCause().toString()));
            }
		}
		return resultSet;
	}
}
