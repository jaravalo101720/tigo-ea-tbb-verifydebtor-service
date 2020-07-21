/*package com.tigo.ea.tbb.verifydebtor.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.htc.ea.util.dto.GenericDto;
import com.tigo.ea.tbb.verifydebtor.commons.Constants;
import com.tigo.ea.tbb.verifydebtor.dto.AdditionalInfo;
import com.tigo.ea.tbb.verifydebtor.dto.Data;
import com.tigo.ea.tbb.verifydebtor.dto.GeneralInfo;
import com.tigo.ea.tbb.verifydebtor.dto.OperadorStatus;
import com.tigo.ea.tbb.verifydebtor.dto.Response;
import com.tigo.ea.tbb.verifydebtor.restconsumer.BssRestConsumer;
import com.tigo.ea.tbb.verifydebtor.restconsumer.VivaRestConsumer;
import com.tigo.ea.tbb.verifydebtor.util.AppServiceException;
import com.tigo.ea.tbb.verifydebtor.util.ConsumerAppUtil;

@Component
public class VerifyDebtorServiceImpl {

	private final Class<?> clazz = this.getClass();
	@Autowired
	private Environment env;
	@Autowired
	private ConsumerAppUtil appUtil;
	@Autowired
	private BssRestConsumer restConsumer;
	@Autowired
	private VivaRestConsumer vivaRestConsumer;
	
	public Response getConsumer(String idTransaccion,String servicio, String nroDocumento) {
		Response response= null;
		List<OperadorStatus> operadores = new ArrayList<>();
		OperadorStatus operador= new OperadorStatus();
		AdditionalInfo addInfo=null;
		GeneralInfo generalInfo=null;
		Data data=null;
		
		GenericDto consumerViva= getConsumerViva(idTransaccion,servicio, nroDocumento);
		if(consumerViva.isEmpty()) {
			operador.setOperador("VIVA");
			operador.setEstadoDeuda("SIN DEUDA");		
		}else{
			operador.setOperador("VIVA");
			operador.setEstadoDeuda("CON DEUDA");	
		}
		operadores.add(operador);
		//operadores.add(new OperadorStatus("VIVA", "CON DEUDA"));
		//operadores.add(new OperadorStatus("ENTEL", "SIN DEUDA"));
		generalInfo =new GeneralInfo("0", "Proceso satisfactorio");
		addInfo = new AdditionalInfo(operadores);
		data= new Data (generalInfo, addInfo);
		 response = new Response(data);
		 
		 return response;
	}
	
	
	private GenericDto getConsumerViva(String idTransaccion,String servicio, String nroDocumento) {
	
		GenericDto response= new GenericDto();
		try {
			
		response= vivaRestConsumer.executeGet(idTransaccion,servicio, nroDocumento);
		
		} catch (Exception e) {		
			appUtil.error(Constants.CATEGORY_SERVICE, clazz, ConsumerAppUtil.getMethodName(),
					"Error de ejecucion en proceso " + ConsumerAppUtil.getMethodName(), "Error de ejecucion",
					 "", 0L,
							"", e);
			throw new AppServiceException(env.getProperty(Constants.CODE_EXECUTION), String
					.format(env.getProperty(Constants.MSJ_EXECUTION), "en proceso " + ConsumerAppUtil.getMethodName()));
		}
		
		
		
		return response;
	}
	
}
*/