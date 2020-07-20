package com.tigo.ea.tbb.verifydebtor.impl;

import java.io.IOException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.htc.ea.util.dto.GenericDto;
import com.tigo.ea.tbb.verifydebtor.commons.Constants;
import com.tigo.ea.tbb.verifydebtor.restconsumer.BssRestConsumer;
import com.tigo.ea.tbb.verifydebtor.util.AppServiceException;
import com.tigo.ea.tbb.verifydebtor.util.ConsumerAppUtil;
import com.tigo.ea.tbb.verifydebtor.util.ValidateRequest;

/**
 * @author Alex Tusell - HTC
 *
 */
@Component
public class ServiceLogicImpl {

	private final Class<?> clazz = this.getClass();
	@Autowired
	private Environment env;
	@Autowired
	private ConsumerAppUtil appUtil;
	@Autowired
	private BssRestConsumer restConsumer;

	@Autowired
	private ValidateRequest validateRequest;

	/**
	 * @param request
	 * @return
	 */
	public GenericDto customerInfobyDocumentId(GenericDto request) {
		GenericDto response = new GenericDto();

		try {
			appUtil.debug(Constants.CATEGORY_SERVICE, request, clazz, ConsumerAppUtil.getMethodName(),
					"Inicia proceso de " + ConsumerAppUtil.getMethodName(), "",request.getStringProperty(Constants.PARAMETER_DOCUMENTO) != null ? request.getStringProperty(Constants.PARAMETER_DOCUMENTO) : "", 0L);

			
			appUtil.debug(Constants.CATEGORY_SERVICE, response, clazz, ConsumerAppUtil.getMethodName(),
					"Finaliza proceso de " + ConsumerAppUtil.getMethodName(), "",
					request.getStringProperty(Constants.PARAMETER_DOCUMENTO) != null ? request.getStringProperty(Constants.PARAMETER_DOCUMENTO) : "", 0L);
		} catch (HttpStatusCodeException e) {
			System.out.println(e.getResponseBodyAsString());
			if (e.getResponseBodyAsString().contains("cbaStatusCode")) {
				try {
					response = new ObjectMapper().readValue(e.getResponseBodyAsString(), GenericDto.class);
				} catch (IOException e1) {
					throw new AppServiceException(env.getProperty(Constants.CODE_FORMAT),
							String.format(env.getProperty(Constants.MSJ_EXECUTION), "parseando respuesta de error"));
				}
				appUtil.info(Constants.CATEGORY_TARGET, e.getResponseBodyAsString(), clazz,ConsumerAppUtil.getMethodName(), "Response exception",
						"codigo: " + e.getStatusCode() + " detalle: " + e.getMessage(), "", 0L);
				return response;
			} else {
				throw new AppServiceException(env.getProperty(Constants.CODE_SERVICE),
						String.format(env.getProperty(Constants.MSJ_SERVICE), ConsumerAppUtil.getMethodName()));
			}
		} catch (AppServiceException e) {
			appUtil.info(Constants.CATEGORY_SERVICE, response, clazz, ConsumerAppUtil.getMethodName(),
					"Response exception", "codigo: " + e.getCode() + " detalle: " + e.getMessage(), "", 0L);

			throw e;
		}
		catch (Exception e) {
			e.printStackTrace();
			appUtil.error(Constants.CATEGORY_SERVICE, clazz, ConsumerAppUtil.getMethodName(),
					"Error de ejecucion en proceso " + ConsumerAppUtil.getMethodName(), "Error de ejecucion",
					request.getStringProperty(Constants.PARAMETER_DOCUMENTO) != null ? request.getStringProperty(Constants.PARAMETER_DOCUMENTO) : "", 0L,
							"", e);
			throw new AppServiceException(env.getProperty(Constants.CODE_EXECUTION), String
					.format(env.getProperty(Constants.MSJ_EXECUTION), "en proceso " + ConsumerAppUtil.getMethodName()));
		}
		return response;
	}

}
