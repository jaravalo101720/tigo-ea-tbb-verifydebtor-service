package com.tigo.ea.tbb.verifydebtor.dto;


public class AxsDto {

	private String idTransaccion;
	private String servicio;
	private String tipoDocumento;
	private String nroDocumento;
	
	
	private String estadoDeuda;
	
	public AxsDto(String idTransaccion, String servicio, String tipoDocumento, String nroDocumento,
			String estadoDeuda) {
		super();
		this.idTransaccion = idTransaccion;
		this.servicio = servicio;
		this.tipoDocumento = tipoDocumento;
		this.nroDocumento = nroDocumento;
		this.estadoDeuda = estadoDeuda;
	}
	
	public AxsDto() {
		super();
		
	}

	public String getIdTransaccion() {
		return idTransaccion;
	}

	public void setIdTransaccion(String idTransaccion) {
		this.idTransaccion = idTransaccion;
	}

	public String getServicio() {
		return servicio;
	}

	public void setServicio(String servicio) {
		this.servicio = servicio;
	}

	public String getTipoDocumento() {
		return tipoDocumento;
	}

	public void setTipoDocumento(String tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
	}

	public String getNroDocumento() {
		return nroDocumento;
	}

	public void setNroDocumento(String nroDocumento) {
		this.nroDocumento = nroDocumento;
	}


	public String getEstadoDeuda() {
		return estadoDeuda;
	}

	public void setEstadoDeuda(String estadoDeuda) {
		this.estadoDeuda = estadoDeuda;
	}
	
	
	
	
	
	
}
