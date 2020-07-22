package com.tigo.ea.tbb.verifydebtor.restconsumer;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

import org.apache.commons.codec.binary.Base64;
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
public class VivaRestConsumer {
	
	private final Class<?> clazz = this.getClass();
	@Autowired
	private Environment env;
	@Autowired
	private ConsumerAppUtil appUtil;
	private Logger log4j = LoggerFactory.getLogger(clazz);

	
	public GenericDto executeGet(GenericDto request) {
		GenericDto responseService = null;
		HttpHeaders headers = new HttpHeaders();
		RestTemplate restTemplate = new RestTemplate();
		TimeChronometer time = new TimeChronometer();
		
		
		try {
			
			String user = env.getProperty(Constants.VIVA_AUTH_USER);
			String pass = env.getProperty(Constants.VIVA_AUTH_PASS);
			
			headers = basicAuth(user, pass);
			headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));	
			HttpEntity<GenericDto> httpEntity = new HttpEntity<>(request, headers);	
			String url=env.getProperty("uri.consumer.service.operator.viva");
			
			time.start();
			appUtil.info(Constants.CATEGORY_TARGET, "", clazz, ConsumerAppUtil.getMethodName(),
					"Request servicio " + ConsumerAppUtil.getMethodName(), "", request.getStringProperty(Constants.PARAMETER_NRODOCUMENTO), 0L);
			
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
			//throw e;
		} catch (AppServiceException serviceException) {
			time.stop();
			appUtil.info(Constants.CATEGORY_SERVICE, responseService, clazz, ConsumerAppUtil.getMethodName(),
					"Response exception",
					"codigo: " + serviceException.getCode() + " detalle: " + serviceException.getMessage(), "",
					time.elapsedMilisUntilLastStop());

			//throw serviceException;
		} catch (Exception e) {
			time.stop();
			appUtil.error(Constants.CATEGORY_TARGET, clazz, ConsumerAppUtil.getMethodName(),
					ConsumerAppUtil.getMethodName() + " : Error consumiendo servicio "
							+ ConsumerAppUtil.getMethodName(),
					"Error de servicio", "", time.elapsedMilisUntilLastStop(), "", e);
			//throw new AppServiceException(env.getProperty(Constants.CODE_SERVICE),
			//		String.format(env.getProperty(Constants.MSJ_SERVICE), ConsumerAppUtil.getMethodName()));
		}
		return responseService;
	}
	
	private HttpHeaders basicAuth(String user, String pass) throws UnsupportedEncodingException {
		String plainCreds = user + ":" + pass;
		byte[] plainCredsBytes = plainCreds.getBytes("UTF-8");
		byte[] base64CredsBytes = Base64.encodeBase64(plainCredsBytes);
		String base64Creds = new String(base64CredsBytes, "UTF-8");

		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Basic " + base64Creds);
		return headers;
	}


}
