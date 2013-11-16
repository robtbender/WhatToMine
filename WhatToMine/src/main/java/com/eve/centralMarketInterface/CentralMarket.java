package com.eve.centralMarketInterface;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.eve.whatToMine.cache.EveDataLayer;
import com.eve.whatToMine.cache.EveOre;
import com.eve.whatToMine.cache.EveRegion;
import com.eve.whatToMine.data.EveOrder;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import javax.xml.xpath.XPathExpression;

public class CentralMarket {

	private EveDataLayer eveDataLayer;
	{
		try {
			eveDataLayer = (EveDataLayer) new InitialContext().lookup("java:module/EveDataLayerSingleton");
		} catch (NamingException e) {
			e.printStackTrace();
		}
	}

	private final static Logger LOGGER = Logger.getLogger(CentralMarket.class);
	EveRegion eveRegion;
	EveOre eveOre;
	ArrayList<EveOrder> regionBuyOrderList = null;

	public CentralMarket() {
	}

	private EveRegion getEveRegion() {
		return this.eveRegion;
	}

	private void setEveRegion(EveRegion eveRegion) {
		this.eveRegion = eveRegion;
	}

	private EveOre getEveOre() {
		return this.eveOre;
	}

	private void setEveOre(EveOre eveOre) {
		this.eveOre = eveOre;
	}

	public ArrayList<EveOrder> getRegionBuyOrderList() {
		if ((this.regionBuyOrderList == null) || (this.regionBuyOrderList.size() == 0)) {
			this.regionBuyOrderList = new ArrayList<EveOrder>();
			EveOrder eveOrder = new EveOrder(this.getEveOre(), this.getEveRegion());
			eveOrder.setEmptyOrder();
			this.regionBuyOrderList.add(eveOrder);
		}
		return this.regionBuyOrderList;
	}

	public ArrayList<EveOrder> getRegionBuyOrderList(EveRegion eveRegion, EveOre eveOre) {
		if ((this.regionBuyOrderList == null) || (this.regionBuyOrderList.size() == 0)) {
			this.setRegionBuyOrderList(eveRegion, eveOre);
		}
		return this.regionBuyOrderList;
	}

	private void setRegionBuyOrderList(EveRegion eveRegion, EveOre eveOre) {
		NodeList eveCentralBuyOrders = null;
		EveOrder eveOrder;
		this.setEveRegion(eveRegion);
		this.setEveOre(eveOre);
		this.regionBuyOrderList = new ArrayList<EveOrder>();

		Logger logger = CentralMarket.LOGGER;

		try {
			String urlString = "http://api.eve-central.com/api/quicklook" + "?" +
			        "regionlimit=" + this.getEveRegion().getEveRegionId() +
			        "&" + "typeid=" + this.getEveOre().getEveItemId();
			/*
			 * URL url = new URL(
			 * "http://api.eve-central.com/api/quicklook?regionlimit=10000068&typeid=1230"
			 * );
			 */
			logger.info(String.format("urlString( %s ).", urlString));
			URL url = new URL(urlString);
			InputStream inputStream = url.openStream();
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			dbf.setIgnoringElementContentWhitespace(true);
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(inputStream);
			doc.getDocumentElement().normalize();
			XPathFactory xpathFactory = XPathFactory.newInstance();
			XPathExpression xpathExp = xpathFactory.newXPath().compile("//buy_orders");
			NodeList buyOrderListRoot = (NodeList) xpathExp.evaluate(doc, XPathConstants.NODESET);
			eveCentralBuyOrders = buyOrderListRoot.item(0).getChildNodes();
		} catch (Exception e) {
		}

		try {
			int nodeDetailNum;
			for (int nodeNum = 0; nodeNum < eveCentralBuyOrders.getLength(); nodeNum++) {
				NodeList buyOrderDetailList = eveCentralBuyOrders.item(nodeNum).getChildNodes();
				eveOrder = new EveOrder(this.getEveOre(), this.getEveRegion());
				for (nodeDetailNum = 0; nodeDetailNum < buyOrderDetailList.getLength(); nodeDetailNum++) {
					Node orderDetail = buyOrderDetailList.item(nodeDetailNum);
					if (orderDetail.getNodeName().equals("price")) {
						eveOrder.setPrice(Double.parseDouble(orderDetail.getChildNodes().item(0).getNodeValue()));
					} else if (orderDetail.getNodeName().equals("station")) {
						long eveStationId = Long.parseLong(orderDetail.getChildNodes().item(0).getNodeValue());
						eveOrder.setStation(eveDataLayer.getEveStation(eveStationId));
					} else if (orderDetail.getNodeName().equals("range")) {
						eveOrder.setRange(Long.parseLong(orderDetail.getChildNodes().item(0).getNodeValue()));
					}
				}
				this.regionBuyOrderList.add(eveOrder);
			}
		} catch (Exception e) {
		}
		if ((this.regionBuyOrderList == null) || (this.regionBuyOrderList.size() == 0)) {
			eveOrder = new EveOrder(this.getEveOre(), this.getEveRegion());
			eveOrder.setEmptyOrder();
			this.regionBuyOrderList.add(eveOrder);
		}
	}
}