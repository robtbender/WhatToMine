package com.eve.whatToMine.cache;

import java.util.ArrayList;
import java.util.List;

import com.eve.whatToMine.db.EveItemDb;

public class EveOre extends EveItem {

	public static final String HighSecORE = "High";
	public static final String LowSecORE = "Low";
	public static final String NullSecORE = "Null";
	public static final String NoSecDefinedORE = "No Security Defined";
	public static final String[] HighSecOreLIST =
	{ "Veldspar", "Concentrated Veldspar", "Dense Veldspar",
	        "Scordite", "Condensed Scordite", "Massive Scordite",
	        "Plagioclase", "Azure Plagioclase", "Rich Plagioclase",
	        "Pyroxeres", "Solid Pyroxeres", "Viscous Pyroxeres",
	        "Omber", "Silvery Omber", "Golden Omber" };
	public static final String[] LowSecOreLIST =
	{ "Kernite", "Luminous Kernite", "Fiery Kernite",
	        "Jaspet", "Pure Jaspet", "Pristine Jaspet",
	        "Hemorphite", "Vivid Hemorphite", "Radiant Hemorphite",
	        "Hedbergite", "Vitric Hedbergite", "Glazed Hedbergite" };
	public static final String[] NullSecOreLIST =
	{ "Gneiss", "Iridescent Gneiss", "Prismatic Gneiss",
	        "Dark Ochre", "Onyx Ochre", "Obsidian Ochre",
	        "Crokite", "Sharp Crokite", "Crystalline Crokite",
	        "Spodumain", "Bright Spodumain", "Gleaming Spodumain",
	        "Bistot", "Triclinic Bistot", "Monoclinic Bistot",
	        "Mercoxit", "Magma Mercoxit", "Vitreous Mercoxit" };
	public static final String[] AllORE = EveOre.join(HighSecOreLIST, LowSecOreLIST, NullSecOreLIST);
	public static final String[] TestORE = { "Veldspar", "Concentrated Veldspar", "Dense Veldspar" };
	public static final String[] SingleTestORE = { "Veldspar" };

	private String secAvailabilty = "*";

	public EveOre(EveItemDb eveItemDb) {
		super(eveItemDb);
		this.setSecAvailabilty(this);
	}

	public String getSecAvailabilty() {
		return secAvailabilty;
	}

	public void setSecAvailabilty(EveOre eveOre) {
		for (String oreString : HighSecOreLIST) {
			if (oreString.equals(eveOre.getEveItemName())) {
				this.secAvailabilty = HighSecORE;
			}
		}
		if (this.secAvailabilty.equals("*")) {
			for (String oreString : LowSecOreLIST) {
				if (oreString.equals(eveOre.getEveItemName())) {
					this.secAvailabilty = LowSecORE;
				}
			}
		}
		if (this.secAvailabilty.equals("*")) {
			for (String oreString : NullSecOreLIST) {
				if (oreString.equals(eveOre.getEveItemName())) {
					this.secAvailabilty = NullSecORE;
				}
			}
		}
		if (this.secAvailabilty.equals("*")) {
			this.secAvailabilty = NoSecDefinedORE;
		}
	}

	private static String[] join(String[]... arrays) {
		int size = 0;
		for (String[] array : arrays) {
			size += array.length;
		}
		List<String> list = new ArrayList<String>(size);
		for (String[] array : arrays) {
			list.addAll(java.util.Arrays.asList(array));
		}
		return (String[]) list.toArray(new String[size]);
	}

}
