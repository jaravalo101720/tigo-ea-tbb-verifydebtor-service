package com.tigo.ea.tbb.verifydebtor.dto;

public class Response {

	private Data data;

	public Data getData() {
		return data;
	}

	public void setData(Data data) {
		this.data = data;
	}

	public Response(Data data) {
		super();
		this.data = data;
	}

	public Response() {
		super();
	}

}
