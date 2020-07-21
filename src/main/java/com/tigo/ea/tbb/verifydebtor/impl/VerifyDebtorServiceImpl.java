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
	
	
     public List<Response> excuteConumerOperator(String idtransaction, String servicio, String tipoDocumento, String nroDocumento) {
    	 List<Response> response= new ArrayList<Response>();
    	 Response responseAxs=new Response();
    	 
    	 List<OperadorStatus> operadores = new ArrayList<>();
			OperadorStatus operador= new OperadorStatus();
			AdditionalInfo addInfo=null;
			GeneralInfo generalInfo=null;
			Data data=null;
			
		 GenericDto responseAxsDto= new GenericDto();
		 responseAxsDto= executeAxs(idtransaction, servicio, tipoDocumento, nroDocumento);
		 
			operador.setOperador(responseAxsDto.getStringProperty(Constants.PARAMETER_OPERATOR));
			operador.setEstadoDeuda(responseAxsDto.getStringProperty(Constants.PARAMETER_ESTADO));	
		 	
			operadores.add(operador);
			generalInfo =new GeneralInfo("0", "Proceso satisfactorio");
			addInfo = new AdditionalInfo(operadores);
			data= new Data (generalInfo, addInfo);
			 
	    	 responseAxs=new Response(data);
	    	 
	    	 response.add(responseAxs);
		
    	 return response;
    	 
     }
     
     
     //
     private GenericDto executeAxs(String idtransaction, String servicio, String tipoDocumento, String nroDocumento) {
    	 GenericDto request= new GenericDto();
    	 GenericDto response= new GenericDto();
    	 //llenar objeto
    	 request.setProperty(Constants.PARAMETER_IDTRANSACTION, idtransaction);
    	 request.setProperty(Constants.PARAMETER_SERVICE, servicio);
    	 request.setProperty(Constants.PARAMETER_TIPODOCUMENTO, tipoDocumento);
    	 request.setProperty(Constants.PARAMETER_NRODOCUMENTO, nroDocumento);
    	 
    	 response= axsRestConsumer.executeGet(request);
    	 
    	 
		return response;
    	 
     }
     
     
}
