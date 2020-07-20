package com.tigo.ea.tbb.verifydebtor.dto;

public class OperadorStatus {
	public OperadorStatus() {
		super();
	}

	public OperadorStatus(String operador, String estadoDeuda) {
		super();
		this.operador = operador;
		this.estadoDeuda = estadoDeuda;
	}

	public String getOperador() {
		return operador;
	}

	public void setOperador(String operador) {
		this.operador = operador;
	}

	public String getEstadoDeuda() {
		return estadoDeuda;
	}

	public void setEstadoDeuda(String estadoDeuda) {
		this.estadoDeuda = estadoDeuda;
	}

	private String operador = "";

	private String estadoDeuda = "";
}
