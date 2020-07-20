package com.tigo.ea.tbb.verifydebtor.dto;

public class Data {
	private GeneralInfo generalInfo;
	private AdditionalInfo addInfo;
	public GeneralInfo getGeneralInfo() {
		return generalInfo;
	}
	public void setGeneralInfo(GeneralInfo generalInfo) {
		this.generalInfo = generalInfo;
	}
	public AdditionalInfo getAddInfo() {
		return addInfo;
	}
	public void setAddInfo(AdditionalInfo addInfo) {
		this.addInfo = addInfo;
	}
	public Data() {
		super();
	}
	public Data(GeneralInfo generalInfo, AdditionalInfo addInfo) {
		super();
		this.generalInfo = generalInfo;
		this.addInfo = addInfo;
	}

}
