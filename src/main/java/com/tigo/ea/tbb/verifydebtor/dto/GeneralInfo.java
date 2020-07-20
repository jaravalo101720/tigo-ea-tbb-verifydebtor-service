package com.tigo.ea.tbb.verifydebtor.dto;

public class GeneralInfo {
	private String codigo = "";

	private String descripcion = "";

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public GeneralInfo(String codigo, String descripcion) {
		super();
		this.codigo = codigo;
		this.descripcion = descripcion;
	}

	public GeneralInfo() {
		super();
	}
}
