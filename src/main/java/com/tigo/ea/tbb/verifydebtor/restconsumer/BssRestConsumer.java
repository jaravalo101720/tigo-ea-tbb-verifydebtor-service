package com.tigo.ea.tbb.verifydebtor.restconsumer;

import java.io.UnsupportedEncodingException;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
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

/**
 * The Class BssRestConsumer.
 *
 * @author Alex Tusell - HTC
 */
@Component
public class BssRestConsumer {

	private final Class<?> clazz = this.getClass();
	@Autowired
	private Environment env;
	@Autowired
	private ConsumerAppUtil appUtil;
	private Logger log4j = LoggerFactory.getLogger(clazz);

	/**
	 * Gets the customer info by document id.
	 *
	 * @param request
	 *            - GenericDto
	 * @return the customer info by document id
	 */
	public GenericDto getCustomerInfoByDocumentId(GenericDto request) {
		GenericDto responseService = null;
		HttpHeaders headers = null;
		RestTemplate restTemplate = new RestTemplate();
		TimeChronometer time = new TimeChronometer();
		try {
			String url = env.getProperty(Constants.BSS_URL_GET_CUSTOMER_INFO);
			String user = env.getProperty(Constants.BSS_AUTH_USER);
			String pass = env.getProperty(Constants.BSS_AUTH_PASS);

			headers = basicAuth(user, pass);
			HttpEntity<String> hea = new HttpEntity<>(headers);
			UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url);// para que es este linea
			request.forEach((k, v) -> builder.queryParam(k, v));// para que es este linea
			String uriBuilder = builder.build().encode().toUriString();// para que es este linea

			time.start();
			appUtil.info(Constants.CATEGORY_TARGET, uriBuilder, clazz, ConsumerAppUtil.getMethodName(),
					"Request servicio " + ConsumerAppUtil.getMethodName(), "", request.getStringProperty(Constants.PARAMETER_DOCUMENTO), 0L);
			
			ResponseEntity<GenericDto> response = restTemplate.exchange(uriBuilder, HttpMethod.GET, hea,
					GenericDto.class);

			if (response==null || response.getBody() == null || response.getBody().isEmpty()) {
				throw new AppServiceException(env.getProperty(Constants.CODE_SERVICE), "CBA not return data");
			} else {
				responseService = response.getBody();
			}
			time.stop();
			appUtil.info(Constants.CATEGORY_TARGET, responseService, clazz, ConsumerAppUtil.getMethodName(),
					"Response servicio " + ConsumerAppUtil.getMethodName(), "", request.getStringProperty(Constants.PARAMETER_DOCUMENTO),
					time.elapsedMilisUntilLastStop());

		} catch (HttpStatusCodeException e) {
			time.stop();
			appUtil.error(Constants.CATEGORY_TARGET, clazz, ConsumerAppUtil.getMethodName(),
					"Error consumiendo servicio " + ConsumerAppUtil.getMethodName(), "Error de ejecucion",
					request.getStringProperty(Constants.PARAMETER_DOCUMENTO), time.elapsedMilisUntilLastStop(), "", e);
			throw e;
		} catch (AppServiceException serviceException) {
			time.stop();
			appUtil.info(Constants.CATEGORY_SERVICE, responseService, clazz, ConsumerAppUtil.getMethodName(),
					"Response exception",
					"codigo: " + serviceException.getCode() + " detalle: " + serviceException.getMessage(), "",
					time.elapsedMilisUntilLastStop());

			throw serviceException;
		} catch (Exception e) {
			log4j.error("getCustomerInfoByDocumentId() => ",e);
			time.stop();
			appUtil.error(Constants.CATEGORY_TARGET, clazz, ConsumerAppUtil.getMethodName(),
					ConsumerAppUtil.getMethodName() + " : Error consumiendo servicio "
							+ ConsumerAppUtil.getMethodName(),
					"Error de servicio", request.getStringProperty("msisdn"), time.elapsedMilisUntilLastStop(), "", e);
			throw new AppServiceException(env.getProperty(Constants.CODE_SERVICE),
					String.format(env.getProperty(Constants.MSJ_SERVICE), ConsumerAppUtil.getMethodName()));
		}
		return responseService;
	}

	/**
	 * Basic auth.
	 *
	 * @param user
	 *            the user
	 * @param pass
	 *            the pass
	 * @return the http headers
	 * @throws UnsupportedEncodingException
	 */
	private HttpHeaders basicAuth(String user, String pass) throws UnsupportedEncodingException {
		String plainCreds = user + ":" + pass;
		byte[] plainCredsBytes = plainCreds.getBytes("UTF-8");
		byte[] base64CredsBytes = Base64.encodeBase64(plainCredsBytes);
		String base64Creds = new String(base64CredsBytes, "UTF-8");

		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Basic " + base64Creds);//preguntar si se consumira el token como parametro, me mandaran token o se consumira aqui
		return headers;
	}

}
