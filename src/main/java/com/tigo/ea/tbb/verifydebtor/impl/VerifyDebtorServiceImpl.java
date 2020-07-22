package com.tigo.ea.tbb.verifydebtor.impl;

import java.util.ArrayList;
import java.util.List;

import org.jolokia.detector.GeronimoDetector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.htc.ea.util.dto.GenericDto;
import com.tigo.ea.tbb.verifydebtor.commons.Constants;
import com.tigo.ea.tbb.verifydebtor.dto.AdditionalInfo;
import com.tigo.ea.tbb.verifydebtor.dto.Data;
import com.tigo.ea.tbb.verifydebtor.dto.GeneralInfo;
import com.tigo.ea.tbb.verifydebtor.dto.OperadorStatus;
import com.tigo.ea.tbb.verifydebtor.dto.Response;
import com.tigo.ea.tbb.verifydebtor.restconsumer.AxsRestConsumer;
import com.tigo.ea.tbb.verifydebtor.restconsumer.VivaRestConsumer;


@Component
public class VerifyDebtorServiceImpl {

	@Autowired
	private VivaRestConsumer vivaRestConsumer;
	@Autowired
	private AxsRestConsumer axsRestConsumer;
	
	
     public List<Response> excuteConumerOperator(String idTransaction, String servicio, String tipoDocumento, String nroDocumento) {
    	 List<Response> response= new ArrayList<Response>();
    	 Response responseDto=new Response();
    	 Response responseViva=new Response();
    	 
    	 List<OperadorStatus> operadores = new ArrayList<>();
			OperadorStatus operador= new OperadorStatus();
			OperadorStatus operadorViva= new OperadorStatus();
			AdditionalInfo addInfo=null;
			GeneralInfo generalInfo=null;
			Data data=null;
			
		 GenericDto responseAxsDto= new GenericDto();
		 responseAxsDto= executeAxs(idTransaction, servicio, tipoDocumento, nroDocumento);
		 
		 GenericDto responseVivaDto= new GenericDto();
		 responseVivaDto= executeViva(idTransaction, servicio, tipoDocumento, nroDocumento);
		 
			operador.setOperador(Constants.OPERATOR_AXS);
			operador.setEstadoDeuda(responseAxsDto.getStringProperty(Constants.PARAMETER_ESTADO));	
			
			operadorViva.setOperador(Constants.OPERATOR_VIVA);
			operadorViva.setEstadoDeuda(responseVivaDto.getStringProperty(Constants.PARAMETER_ESTADO_DEUDA));
		 	
			operadores.add(operador);
			operadores.add(operadorViva);
			generalInfo =new GeneralInfo("0", "Proceso satisfactorio");
			addInfo = new AdditionalInfo(operadores);
			data= new Data (generalInfo, addInfo);
			 
			responseDto=new Response(data);  	 
	    	response.add(responseDto);
	    
		
    	 return response;
    	 
     }
     
     private GenericDto executeAxs(String idTransaction, String servicio, String tipoDocumento, String nroDocumento) {
    	 GenericDto request= new GenericDto();
    	 GenericDto response= new GenericDto();
    	 //llenar objeto
    	 request.setProperty(Constants.PARAMETER_IDTRANSACTION, idTransaction);
    	 request.setProperty(Constants.PARAMETER_SERVICE, servicio);
    	 request.setProperty(Constants.PARAMETER_TIPODOCUMENTO, tipoDocumento);
    	 request.setProperty(Constants.PARAMETER_NRODOCUMENTO, nroDocumento);
    	 
    	 response= axsRestConsumer.executeGet(request);
    	 
    	 
		return response;
    	 
     }
     
     private GenericDto executeViva(String idTransaction, String servicio, String tipoDocumento, String nroDocumento) {
    	 GenericDto request= new GenericDto();
    	 GenericDto response= new GenericDto();
    	 
    	 request.setProperty(Constants.PARAMETER_IDTRANSACTION, idTransaction);
    	 request.setProperty(Constants.PARAMETER_SERVICE, servicio);
    	 request.setProperty(Constants.PARAMETER_TIPODOCUMENTO, tipoDocumento);
    	 request.setProperty(Constants.PARAMETER_NRODOCUMENTO, nroDocumento);
    	 
    	 response=vivaRestConsumer.executeGet(request);
    	 
    	 return response;
     }
     
     
}
