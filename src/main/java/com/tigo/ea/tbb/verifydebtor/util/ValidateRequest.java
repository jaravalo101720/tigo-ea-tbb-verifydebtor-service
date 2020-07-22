package com.tigo.ea.tbb.verifydebtor.util;

import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import com.htc.ea.util.dto.GenericDto;
import com.tigo.ea.tbb.verifydebtor.commons.Constants;

/**
 * @author HTC-Daniel
 *
 */
@Component
public class ValidateRequest {
	
	private final Class<?> clazz = this.getClass();
	@Autowired
	private Environment env;
	@Autowired
	private ConsumerAppUtil appUtil;
	
	
	
	/**
	 * @param request
	 * @return
	 */
	public GenericDto createRequest(GenericDto request, Map<String, Integer> requestDefinition, Map<String, String> requestMatch){
		GenericDto requestService = null;
		
		try{
			if(request == null || request.isEmpty()){
				//no ser recibieron los parametros del servicio
				throw new AppServiceException(env.getProperty(Constants.CODE_MANDATORY), "No se recibieron los parametros del servicio");
			}
			
			requestService = new GenericDto();			
//			//obtenemos las key necesarias para el servicio
//			Set<String> keyParameters = requestMatch.keySet();
			
			//iteramos la keys para formar request
			for(Entry<String, String> entry : requestMatch.entrySet()){
				
				//validamos si se recibio el parametro
				if(request.getStringProperty(entry.getKey()) != null ){
					
					//validamos formato del campo si es necesario
					if(requestDefinition.get(entry.getValue()) != null){
						
						//comparamos campo de entrada vrs campo de definicion obtenido por llave match
						if(request.getStringProperty(entry.getKey()).length() == requestDefinition.get(entry.getValue())){
							//si cumple el formato agregamos al request
							requestService.put(entry.getValue(), request.getStringProperty(entry.getKey()).trim());
						}else{
							//no se cumplio el formato enviamos error con llave recibida de la fachada
							throw new AppServiceException(env.getProperty(Constants.CODE_FORMAT), String.format(env.getProperty(Constants.MSJ_FORMAT), entry.getKey()));
						}						
					}else{
						// si no se valida longitud agregamos como parametro request por default
						requestService.put(entry.getValue(), request.getStringProperty(entry.getKey()).trim());
					}					
				}else{
					throw new AppServiceException(env.getProperty(Constants.CODE_MANDATORY), String.format(env.getProperty(Constants.MSJ_MANDATORY), entry.getKey()));
				}
			}					
		}catch (AppServiceException serviceException) {
		      appUtil.info(Constants.CATEGORY_SERVICE,requestService, clazz, ConsumerAppUtil.getMethodName(), "Response exception", "codigo: "+serviceException.getCode()+" detalle: "+serviceException.getMessage(), "", 0L);
			throw serviceException;
		}
		catch (Exception e) {
			e.printStackTrace();
			appUtil.error(Constants.CATEGORY_SERVICE, clazz, ConsumerAppUtil.getMethodName(), "nominacion: Error validando request", "",  request.getStringProperty("nroCuenta") != null ? request.getStringProperty("nroCuenta") : "", 0, null, e);
			throw new AppServiceException(env.getProperty(Constants.CODE_EXECUTION), String.format(env.getProperty(Constants.MSJ_EXECUTION), "validando request"));
		}
		
		return requestService;
	}
	
	public  boolean stringValidation(String... values){
        Boolean validation = false;
        if(values  != null){
          int errors = 0;
            for (String aux : values) {
              if(aux == null || aux.isEmpty())
                  errors++;
            }              
            if (errors > 0) 
                validation = true;              
          }else{
              validation = true;
          }        
          return validation;
  }
	

}
