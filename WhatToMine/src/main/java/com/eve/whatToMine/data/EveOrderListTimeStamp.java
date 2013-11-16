package com.eve.whatToMine.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

public class EveOrderListTimeStamp {

	public static final int MinRefreshHOURS = 4;

	public static final Comparator<EveOrder> ORE_COMPARATOR = new Comparator<EveOrder>() {
		public int compare(EveOrder eveOrder1, EveOrder eveOrder2) {
			if ((eveOrder1.getPrice() / eveOrder1.getEveItem().getEveItemVolume()) > (eveOrder2.getPrice() / eveOrder2.getEveItem().getEveItemVolume())) {
				return -1;
			} else {
				return 1;
			}
		}
	};

	private ArrayList<EveOrder> orderList = new ArrayList<EveOrder>();
	private Date centralMarketTimeStamp;

	public ArrayList<EveOrder> getOrderList() {
		return orderList;
	}

	public void setOrderList(ArrayList<EveOrder> orderList) {
		this.orderList = orderList;
	}

	public EveOrder getMaxOrder() {
		return this.getOrderList().get(0);
	}

	public Date getCentralMarketTimeStamp() {
		return centralMarketTimeStamp;
	}

	public void setCentralMarketTimeStamp(Date date) {
		this.centralMarketTimeStamp = date;
	}

	public void sortOrderList() {
		Collections.sort(this.getOrderList(), ORE_COMPARATOR);
    }
}
