package com.tigo.ea.tbb.verifydebtor.dto;

import java.util.List;

public class AdditionalInfo {
	public AdditionalInfo(List<OperadorStatus> operadores) {
		super();
		this.operadores = operadores;
	}

	private List<OperadorStatus> operadores;

	public List<OperadorStatus> getOperadores() {
		return operadores;
	}

	public void setOperadores(List<OperadorStatus> operadores) {
		this.operadores = operadores;
	}

	public AdditionalInfo() {
		super();
	}
}
