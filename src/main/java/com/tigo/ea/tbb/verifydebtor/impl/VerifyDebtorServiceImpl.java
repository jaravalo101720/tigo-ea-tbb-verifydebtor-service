package com.tigo.ea.tbb.verifydebtor.impl;

import java.util.ArrayList;
import java.util.List;

import org.jolokia.detector.GeronimoDetector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import com.htc.ea.util.dto.GenericDto;

import com.tigo.ea.tbb.verifydebtor.commons.Constants;
import com.tigo.ea.tbb.verifydebtor.dto.AdditionalInfo;
import com.tigo.ea.tbb.verifydebtor.dto.Data;
import com.tigo.ea.tbb.verifydebtor.dto.GeneralInfo;
import com.tigo.ea.tbb.verifydebtor.dto.OperadorStatus;
import com.tigo.ea.tbb.verifydebtor.dto.Response;
import com.tigo.ea.tbb.verifydebtor.restconsumer.AxsRestConsumer;
import com.tigo.ea.tbb.verifydebtor.restconsumer.AxsTokenRestConsumer;
import com.tigo.ea.tbb.verifydebtor.restconsumer.CotasRestConsumer;
import com.tigo.ea.tbb.verifydebtor.restconsumer.CotasTokenRestConsumer;
import com.tigo.ea.tbb.verifydebtor.restconsumer.EntelRestConsumer;
import com.tigo.ea.tbb.verifydebtor.restconsumer.EntelTokenRestConsumer;
import com.tigo.ea.tbb.verifydebtor.restconsumer.VivaRestConsumer;
import com.tigo.ea.tbb.verifydebtor.util.ConsumerAppUtil;


@Component
public class VerifyDebtorServiceImpl {

	private final Class<?> clazz = this.getClass();
	@Autowired
	private Environment env;
	@Autowired
	private ConsumerAppUtil appUtil;
	
	@Autowired
	private VivaRestConsumer vivaRestConsumer;
	@Autowired
	private AxsRestConsumer axsRestConsumer;
	@Autowired
	private EntelRestConsumer entelRestConsumer;
	@Autowired
	private CotasRestConsumer cotasRestconsumer;
	@Autowired
	private AxsTokenRestConsumer axsTokenRestConsumer;
	@Autowired
	private CotasTokenRestConsumer cotasTokenRestConsumer;
	@Autowired 
	private EntelTokenRestConsumer entelTokenRestConsumer;
	
	
     public List<Response> excuteConsumerOperator(String idTransaction, String servicio, String tipoDocumento, String nroDocumento) {
    	 List<Response> response= new ArrayList<Response>();
    	 Response responseDto=new Response();
    	 
    	 List<OperadorStatus> operadores = new ArrayList<>();
			OperadorStatus operador= new OperadorStatus();
			OperadorStatus operadorViva= new OperadorStatus();
			OperadorStatus operadorEntel= new OperadorStatus();
			OperadorStatus operadotCotas = new OperadorStatus();
			AdditionalInfo addInfo=null;
			GeneralInfo generalInfo=null;
			Data data=null;
			
		 GenericDto responseAxsDto= new GenericDto();
		 String responseAxsToken = executeAxsToken(env.getProperty(Constants.AXS_AUTH_USER), env.getProperty(Constants.AXS_AUTH_PASSWORD));
		 
		 responseAxsDto= executeAxs(idTransaction, servicio, tipoDocumento, nroDocumento);
		 if(responseAxsDto==null || responseAxsDto.isEmpty()) {
			 operador.setOperador(Constants.OPERATOR_AXS);
			 operador.setEstadoDeuda(Constants.ESTADO_NULL);	
		 }else {
			 operador.setOperador(Constants.OPERATOR_AXS);
			 operador.setEstadoDeuda(responseAxsDto.getStringProperty(Constants.PARAMETER_ESTADO)); 
			 
		 }
		 	
		 GenericDto responseVivaDto= new GenericDto();
		 responseVivaDto= executeViva(idTransaction, servicio, tipoDocumento, nroDocumento);
		 if(responseVivaDto==null || responseVivaDto.isEmpty()) {
			 operadorViva.setOperador(Constants.OPERATOR_VIVA);
			 operadorViva.setEstadoDeuda(Constants.ESTADO_NULL);	
		 }else {
			 operadorViva.setOperador(Constants.OPERATOR_VIVA);
			 operadorViva.setEstadoDeuda(responseVivaDto.getStringProperty(Constants.PARAMETER_ESTADO_DEUDA));
		 }
		 
		 GenericDto responseEntelDto= new GenericDto();
		 
		 responseEntelDto=executeEntel(idTransaction, servicio, tipoDocumento, nroDocumento);
		 if(responseEntelDto==null || responseEntelDto.isEmpty()) {
			 operadorEntel.setOperador(Constants.OPERATOR_ENTEL);
			 operadorEntel.setEstadoDeuda(Constants.ESTADO_NULL);
		 }else {
			 operadorEntel.setOperador(Constants.OPERATOR_ENTEL);
			  operadorEntel.setEstadoDeuda(responseEntelDto.getStringProperty(Constants.PARAMETER_ESTADO_DEUDA));
		 }
		  
		 GenericDto responseCotasDto = new GenericDto();
		
		 responseCotasDto = executeCotas(idTransaction, servicio, tipoDocumento, nroDocumento);
		 if(responseCotasDto==null || responseCotasDto.isEmpty()) {
			 operadotCotas.setOperador(Constants.OPERATOR_COTAS);
			 operadotCotas.setEstadoDeuda(Constants.ESTADO_NULL);
		 }else {
			 operadotCotas.setOperador(Constants.OPERATOR_COTAS);
			 operadotCotas.setEstadoDeuda(responseCotasDto.getStringProperty(Constants.PARAMETER_ESTADO_DEUDA_COTAS));
		 }
		  
			operadores.add(operador);
			operadores.add(operadorViva);
			operadores.add(operadorEntel);
			operadores.add(operadotCotas);
			
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


      	 request.setProperty(Constants.PARAMETER_IDTRANSACTION_AXS, idTransaction);
    	 request.setProperty(Constants.PARAMETER_SERVICE_AXS, servicio);
    	 request.setProperty(Constants.PARAMETER_TIPODOCUMENTO_AXS, tipoDocumento);
    	 request.setProperty(Constants.PARAMETER_NRODOCUMENTO_AXS, nroDocumento);
    	 
    	 
    	 response= axsRestConsumer.executeGet(request);

    		 
		return response;
    	 
     }
     
     private GenericDto executeViva(String idTransaction, String servicio, String tipoDocumento, String nroDocumento) {
    	 GenericDto request= new GenericDto();
    	 GenericDto response= new GenericDto();
    	
    	 
     	 request.setProperty(Constants.PARAMETER_IDTRANSACTION_VIVA, idTransaction);
    	 request.setProperty(Constants.PARAMETER_SERVICE_VIVA, servicio);
    	 request.setProperty(Constants.PARAMETER_TIPODOCUMENTO_VIVA, tipoDocumento);
    	 request.setProperty(Constants.PARAMETER_NRODOCUMENTO_VIVA, nroDocumento);
    	 
    	 response=vivaRestConsumer.executeGet(request);
    	 
    	 return response;
     }
     
     private GenericDto executeEntel(String idTransaction, String servicio, String tipoDocumento, String nroDocumento) {
    	 GenericDto request = new GenericDto();
    	 GenericDto response= new GenericDto();
   
    	 request.setProperty(Constants.PARAMETER_IDTRANSACTION_ENTEL, idTransaction);
    	 request.setProperty(Constants.PARAMETER_SERVICE_ENTEL, servicio);
    	 request.setProperty(Constants.PARAMETER_TIPODOCUMENTO_ENTEL, tipoDocumento);
    	 request.setProperty(Constants.PARAMETER_NRODOCUMENTO_ENTEL, nroDocumento);
    	 
    	 response=entelRestConsumer.executeGet(request);
    	 
    	
    	 return response;
     }
     
     
     private GenericDto executeCotas(String idTransaction, String servicio, String tipoDocumento, String nroDocumento) {
    	 GenericDto request = new GenericDto();
    	 GenericDto response= new GenericDto();
    	 
    	 request.setProperty(Constants.PARAMETER_IDTRANSACTION_COTAS, idTransaction);
    	 request.setProperty(Constants.PARAMETER_SERVICE_COTAS, servicio);
    	 request.setProperty(Constants.PARAMETER_TIPODOCUMENTO_COTAS, tipoDocumento);
    	 request.setProperty(Constants.PARAMETER_NRODOCUMENTO_COTAS, nroDocumento);
    	 
    	 response=cotasRestconsumer.executeGet(request);
    	 
    	 return response;
     }
     
     private String executeAxsToken(String username, String password) {
    	 GenericDto request = new GenericDto();
    	 GenericDto response = new GenericDto();
    	 String token=null;
    	 
    	request.setProperty(Constants.PARAMETER_AXS_AUTH_TOKEN_USER, username);
    	request.setProperty(Constants.PARAMETER_AXS_AUTH_TOKEN_PASSWORD, password);
    	
    	response= axsTokenRestConsumer.executeGet(request);
    	
    	 token=response.getStringProperty("token");
    	 
		return token;
    	 
     }
     
     private String executeCotasToken(String username, String password) {
    	 String token = null;
    	 GenericDto request = new GenericDto();
    	 GenericDto response= new GenericDto();
    	 
    	 request.setProperty(Constants.PARAMETER_COTAS_AUTH_TOKEN_USER, username);
    	 request.setProperty(Constants.PARAMETER_COTAS_AUTH_TOKEN_PASSWORD, password);
    	 
    	 response=cotasTokenRestConsumer.executeGet(request);
    	 
    	 token=response.getStringProperty("token");
    	 
    	 return token;
    	 
     }
     
     private String executeEntelToken(String username, String password) {
		String token=null;
		GenericDto request = new GenericDto();
		GenericDto response= new GenericDto();
		
		request.setProperty(Constants.PARAMETER_ENTEL_AUTH_TOKEN_USER, username);
		request.setProperty(Constants.PARAMETER_ENTEL_AUTH_TOKEN_PASSWORD, password);
		
		response=entelTokenRestConsumer.executeGet(request);
		
		token=response.getStringProperty("token");
		
    	 
    	 return token;
    	 
     }
}
