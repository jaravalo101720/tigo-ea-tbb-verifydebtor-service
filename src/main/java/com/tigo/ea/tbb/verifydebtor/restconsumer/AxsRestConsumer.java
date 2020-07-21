package com.tigo.ea.tbb.verifydebtor.restconsumer;

import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.htc.ea.util.dto.GenericDto;
import com.htc.ea.util.util.TimeChronometer;
import com.tigo.ea.tbb.verifydebtor.commons.Constants;
import com.tigo.ea.tbb.verifydebtor.util.AppServiceException;
import com.tigo.ea.tbb.verifydebtor.util.ConsumerAppUtil;

@Component
public class AxsRestConsumer {

	private final Class<?> clazz = this.getClass();
	@Autowired
	private Environment env;
	@Autowired
	private ConsumerAppUtil appUtil;
	private Logger log4j = LoggerFactory.getLogger(clazz);

	
	public GenericDto executeGet(GenericDto request ) {
		
		RestTemplate restTemplate = new RestTemplate();
		GenericDto responseService = null;
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		HttpEntity<GenericDto> httpEntity = new HttpEntity<>(request, headers);
		//preguntar tipo de autenticacion en el headers
		

		TimeChronometer time = new TimeChronometer();
		String url=env.getProperty("uri.consumer.service.operator.axs");//http://localhost:8080/axs
		
		try {
			
			time.start();
			
			appUtil.info(Constants.CATEGORY_TARGET, request, clazz, ConsumerAppUtil.getMethodName(),
					"Request servicio " + ConsumerAppUtil.getMethodName(), "", request.getStringProperty(Constants.PARAMETER_NRODOCUMENTO), 0L);
			//preguntar request en data o details
			ResponseEntity<GenericDto> response = restTemplate.exchange(url, HttpMethod.POST,httpEntity, GenericDto.class);

			if (response==null || response.getBody() == null || response.getBody().isEmpty()) {
				throw new AppServiceException(env.getProperty(Constants.CODE_SERVICE), "no data to return");
			} else {
				responseService = response.getBody();
			}
			time.stop();
			appUtil.info(Constants.CATEGORY_TARGET, responseService, clazz, ConsumerAppUtil.getMethodName(),
					"Response servicio " + ConsumerAppUtil.getMethodName(), "", request.getStringProperty(Constants.PARAMETER_NRODOCUMENTO),
					time.elapsedMilisUntilLastStop());

		} catch (HttpStatusCodeException e) {
			time.stop();
			appUtil.error(Constants.CATEGORY_TARGET, clazz, ConsumerAppUtil.getMethodName(),
					"Error consumiendo servicio " + ConsumerAppUtil.getMethodName(), "Error de ejecucion",
					"", time.elapsedMilisUntilLastStop(), "", e);
			throw e;
		} catch (AppServiceException serviceException) {
			time.stop();
			appUtil.info(Constants.CATEGORY_SERVICE, responseService, clazz, ConsumerAppUtil.getMethodName(),
					"Response exception",
					"codigo: " + serviceException.getCode() + " detalle: " + serviceException.getMessage(), "",
					time.elapsedMilisUntilLastStop());
			
			throw serviceException;
		} catch (Exception e) {
			time.stop();
			appUtil.error(Constants.CATEGORY_TARGET, clazz, ConsumerAppUtil.getMethodName(),
					ConsumerAppUtil.getMethodName() + " : Error consumiendo servicio "
							+ ConsumerAppUtil.getMethodName(),
					"Error de servicio", "", time.elapsedMilisUntilLastStop(), "", e);
			throw new AppServiceException(env.getProperty(Constants.CODE_SERVICE),
					String.format(env.getProperty(Constants.MSJ_SERVICE), ConsumerAppUtil.getMethodName()));
		}
		return responseService;
	}
	


}
