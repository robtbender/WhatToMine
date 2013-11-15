package com.eve.whatToMine.web;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

import com.eve.whatToMine.cache.EveDataLayer;
import com.eve.whatToMine.cache.EveSystem;
import com.eve.whatToMine.cache.EveOre;

import java.io.Serializable;
import java.util.ArrayList;

@Named
@SessionScoped
public class UserSession implements Serializable {

	@EJB
	private EveDataLayer eveDataLayer;

	private static final long serialVersionUID = 1L;
	private String inputEveSystemName;
	private EveSystem eveSystem;
	private boolean eveSystemEntered = false;

	public String getInputEveSystemName() {
		return this.inputEveSystemName;
	}

	public void setInputEveSystemName(String inputEveSystemName) {
		this.inputEveSystemName = inputEveSystemName;
		this.setEveSystem(eveDataLayer.getEveSystem(inputEveSystemName));
		this.getEveSystem().setEveSystemItemMaxAvailableList();
		this.setEveSystemEntered(true);
	}

	public boolean getEveSystemEntered() {
	    return this.eveSystemEntered;
    }

	public void setEveSystemEntered(boolean eveSystemEntered) {
	    this.eveSystemEntered = eveSystemEntered;
    }

	public EveSystem getEveSystem() {
	    return this.eveSystem;
    }

	public void setEveSystem(EveSystem eveSystem) {
	    this.eveSystem = eveSystem;
    }

	public ArrayList<EveOre> getEveOreList() {
	    return eveDataLayer.getEveOreList();
    }

	public void setEveOreList(ArrayList<EveOre> eveOreList) {
    }
}
