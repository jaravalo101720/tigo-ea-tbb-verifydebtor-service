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
import com.tigo.ea.tbb.verifydebtor.restconsumer.CotasRestConsumer;
import com.tigo.ea.tbb.verifydebtor.restconsumer.EntelRestConsumer;
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
		 responseAxsDto= executeAxs(idTransaction, servicio, tipoDocumento, nroDocumento);
		 if(responseAxsDto==null || responseAxsDto.isEmpty()) {
			 operador.setOperador(Constants.OPERATOR_AXS);
			 operador.setEstadoDeuda(Constants.ESTADO);	
		 }else {
			 operador.setOperador(Constants.OPERATOR_AXS);
			 operador.setEstadoDeuda(responseAxsDto.getStringProperty(Constants.PARAMETER_ESTADO)); 
		 }
		 	
		 GenericDto responseVivaDto= new GenericDto();
		 responseVivaDto= executeViva(idTransaction, servicio, tipoDocumento, nroDocumento);
		 if(responseVivaDto==null || responseVivaDto.isEmpty()) {
			 operadorViva.setOperador(Constants.OPERATOR_VIVA);
			 operadorViva.setEstadoDeuda(Constants.ESTADO);	
		 }else {
			 operadorViva.setOperador(Constants.OPERATOR_VIVA);
			 operadorViva.setEstadoDeuda(responseVivaDto.getStringProperty(Constants.PARAMETER_ESTADO_DEUDA));
		 }
		 
		 GenericDto responseEntelDto= new GenericDto();
		 responseEntelDto=executeEntel(idTransaction, servicio, tipoDocumento, nroDocumento);
		 if(responseEntelDto==null || responseEntelDto.isEmpty()) {
			 operadorEntel.setOperador(Constants.OPERATOR_ENTEL);
			 operadorEntel.setEstadoDeuda(Constants.ESTADO);
		 }else {
			 operadorEntel.setOperador(Constants.OPERATOR_ENTEL);
			  operadorEntel.setEstadoDeuda(responseEntelDto.getStringProperty(Constants.PARAMETER_ESTADO_DEUDA));
		 }
		  
		 GenericDto responseCotasDto = new GenericDto();
		 responseCotasDto = executeCotas(idTransaction, servicio, tipoDocumento, nroDocumento);
		 if(responseCotasDto==null || responseCotasDto.isEmpty()) {
			 operadotCotas.setOperador(Constants.OPERATOR_COTAS);
			 operadotCotas.setEstadoDeuda(Constants.ESTADO);
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

    	 try {
      		appUtil.debug(Constants.CATEGORY_SERVICE, request, clazz, ConsumerAppUtil.getMethodName(),
      		"Inicia proceso de " + ConsumerAppUtil.getMethodName(), "",request.getStringProperty(Constants.PARAMETER_NRODOCUMENTO) != null ? request.getStringProperty(Constants.PARAMETER_NRODOCUMENTO) : "", 0L);
    	 
      	 request.setProperty(Constants.PARAMETER_IDTRANSACTION, idTransaction);
    	 request.setProperty(Constants.PARAMETER_SERVICE, servicio);
    	 request.setProperty(Constants.PARAMETER_TIPODOCUMENTO, tipoDocumento);
    	 request.setProperty(Constants.PARAMETER_NRODOCUMENTO, nroDocumento);
    	 
    	 response= axsRestConsumer.executeGet(request);

    	 } catch (Exception e) {
    		 appUtil.error(Constants.CATEGORY_SERVICE, clazz, ConsumerAppUtil.getMethodName(),
    		"Error de ejecucion en proceso " + ConsumerAppUtil.getMethodName(), "Error de ejecucion",
    		 request.getStringProperty(Constants.PARAMETER_NRODOCUMENTO) != null ? request.getStringProperty(Constants.PARAMETER_NRODOCUMENTO) : "", 0L,
    		 "", e);
    	 }
    		 
		return response;
    	 
     }
     
     private GenericDto executeViva(String idTransaction, String servicio, String tipoDocumento, String nroDocumento) {
    	 GenericDto request= new GenericDto();
    	 GenericDto response= new GenericDto();
    	
    	 try {
     		appUtil.debug(Constants.CATEGORY_SERVICE, request, clazz, ConsumerAppUtil.getMethodName(),
     		"Inicia proceso de " + ConsumerAppUtil.getMethodName(), "",request.getStringProperty(Constants.PARAMETER_NRODOCUMENTO) != null ? request.getStringProperty(Constants.PARAMETER_NRODOCUMENTO) : "", 0L);
    	
     	 request.setProperty(Constants.PARAMETER_IDTRANSACTION, idTransaction);
    	 request.setProperty(Constants.PARAMETER_SERVICE, servicio);
    	 request.setProperty(Constants.PARAMETER_TIPODOCUMENTO, tipoDocumento);
    	 request.setProperty(Constants.PARAMETER_NRODOCUMENTO, nroDocumento);
    	 
    	 response=vivaRestConsumer.executeGet(request);
    	 
    	 } catch (Exception e) {
    		 appUtil.error(Constants.CATEGORY_SERVICE, clazz, ConsumerAppUtil.getMethodName(),
    		"Error de ejecucion en proceso " + ConsumerAppUtil.getMethodName(), "Error de ejecucion",
    		 request.getStringProperty(Constants.PARAMETER_NRODOCUMENTO) != null ? request.getStringProperty(Constants.PARAMETER_NRODOCUMENTO) : "", 0L,
    		 "", e);
    	 }
    	 return response;
     }
     
     private GenericDto executeEntel(String idTransaction, String servicio, String tipoDocumento, String nroDocumento) {
    	 GenericDto request = new GenericDto();
    	 GenericDto response= new GenericDto();
    	 try {
    		appUtil.debug(Constants.CATEGORY_SERVICE, request, clazz, ConsumerAppUtil.getMethodName(),
    		"Inicia proceso de " + ConsumerAppUtil.getMethodName(), "",request.getStringProperty(Constants.PARAMETER_NRODOCUMENTO) != null ? request.getStringProperty(Constants.PARAMETER_NRODOCUMENTO) : "", 0L);
    	
    	 request.setProperty(Constants.PARAMETER_IDTRANSACTION, idTransaction);
    	 request.setProperty(Constants.PARAMETER_SERVICE, servicio);
    	 request.setProperty(Constants.PARAMETER_TIPODOCUMENTO, tipoDocumento);
    	 request.setProperty(Constants.PARAMETER_NRODOCUMENTO, nroDocumento);
    	 
    	 response=entelRestConsumer.executeGet(request);
    	 
    	 appUtil.debug(Constants.CATEGORY_SERVICE, response, clazz, ConsumerAppUtil.getMethodName(),
		"Finaliza proceso de " + ConsumerAppUtil.getMethodName(), "",
		 request.getStringProperty(Constants.PARAMETER_NRODOCUMENTO) != null ? request.getStringProperty(Constants.PARAMETER_NRODOCUMENTO) : "", 0L);
    		
    	} catch (Exception e) {
    		appUtil.error(Constants.CATEGORY_SERVICE, clazz, ConsumerAppUtil.getMethodName(),
    		"Error de ejecucion en proceso " + ConsumerAppUtil.getMethodName(), "Error de ejecucion",
    		request.getStringProperty(Constants.PARAMETER_NRODOCUMENTO) != null ? request.getStringProperty(Constants.PARAMETER_NRODOCUMENTO) : "", 0L,
    		"", e);
    	}
    		
    	 return response;
     }
     
     
     private GenericDto executeCotas(String idTransaction, String servicio, String tipoDocumento, String nroDocumento) {
    	 GenericDto request = new GenericDto();
    	 GenericDto response= new GenericDto();
    	 
    	 try {
     		appUtil.debug(Constants.CATEGORY_SERVICE, request, clazz, ConsumerAppUtil.getMethodName(),
     		"Inicia proceso de " + ConsumerAppUtil.getMethodName(), "",request.getStringProperty(Constants.PARAMETER_NRODOCUMENTO) != null ? request.getStringProperty(Constants.PARAMETER_NRODOCUMENTO) : "", 0L);
    	 request.setProperty(Constants.PARAMETER_IDTRANSACTION, idTransaction);
    	 request.setProperty(Constants.PARAMETER_SERVICE, servicio);
    	 request.setProperty(Constants.PARAMETER_TIPODOCUMENTO, tipoDocumento);
    	 request.setProperty(Constants.PARAMETER_NRODOCUMENTO, nroDocumento);
    	 
    	 response=cotasRestconsumer.executeGet(request);
    	 
    	 } catch (Exception e) {
     		appUtil.error(Constants.CATEGORY_SERVICE, clazz, ConsumerAppUtil.getMethodName(),
     		"Error de ejecucion en proceso " + ConsumerAppUtil.getMethodName(), "Error de ejecucion",
     		request.getStringProperty(Constants.PARAMETER_NRODOCUMENTO) != null ? request.getStringProperty(Constants.PARAMETER_NRODOCUMENTO) : "", 0L,
     		"", e);
     	}
    	 return response;
     }
}
