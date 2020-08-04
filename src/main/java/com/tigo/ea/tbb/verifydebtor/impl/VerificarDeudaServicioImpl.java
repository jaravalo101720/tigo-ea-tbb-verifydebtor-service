package com.tigo.ea.tbb.verifydebtor.impl;

import java.util.ArrayList;
import java.util.List;

import org.jolokia.detector.GeronimoDetector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tigo.ea.tbb.verifydebtor.commons.Constants;
import com.tigo.ea.tbb.verifydebtor.dto.AdditionalInfo;
import com.tigo.ea.tbb.verifydebtor.dto.AxsDto;
import com.tigo.ea.tbb.verifydebtor.dto.Data;
import com.tigo.ea.tbb.verifydebtor.dto.GeneralInfo;
import com.tigo.ea.tbb.verifydebtor.dto.OperadorStatus;
import com.tigo.ea.tbb.verifydebtor.dto.Response;
import com.tigo.ea.tbb.verifydebtor.restconsumer.AxsRestConsumer;
import com.tigo.ea.tbb.verifydebtor.restconsumer.VivaRestConsumer;

@Component
public class VerificarDeudaServicioImpl {

	@Autowired 
	private VivaRestConsumer vivaConsumer;
	
	public List<Response> processOperator(String idTransaccion, String servicio, String tipoDocumento, String nroDocumento) {
		List<Response> listaResponse = new ArrayList<Response>();
		Response response =null;
		List<OperadorStatus> listaOperador= new ArrayList<OperadorStatus>();
		OperadorStatus operadorViva = new OperadorStatus();
		AdditionalInfo addInfo=null;
		GeneralInfo generalInfo= null;
		Data data= null;
		
		try {
			AxsDto axsExecute= axsOperator(idTransaccion, servicio, tipoDocumento, nroDocumento);
			if(axsExecute==null) {
				operadorViva.setOperador("VIVA");
				operadorViva.setEstadoDeuda(null);
			}
			else {
				operadorViva.setOperador("VIVA");
				operadorViva.setEstadoDeuda(axsExecute.getEstadoDeuda());
			}
			listaOperador.add(operadorViva);
			generalInfo = new GeneralInfo("0", "Proceso Satisfactorio");
			addInfo = new AdditionalInfo(listaOperador);
			data = new Data(generalInfo, addInfo);
			
			
			response = new  Response(data);
			listaResponse.add(response);
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		return listaResponse;
	}
	
	
	
	
	
	
	private AxsDto axsOperator(String idTransaccion, String servicio, String tipoDocumento, String nroDocumento) {
		AxsDto response = null;
		
		AxsDto request = new AxsDto();
		request.setIdTransaccion(idTransaccion);
		request.setServicio(servicio);
		request.setTipoDocumento(tipoDocumento);
		request.setNroDocumento(nroDocumento);
		
		response= vivaConsumer.executeGet(request);
		
		return response;
		
	}
}
