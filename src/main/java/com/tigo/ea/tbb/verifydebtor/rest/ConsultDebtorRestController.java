/*package com.tigo.ea.tbb.verifydebtor.rest;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.htc.ea.util.dto.GenericDto;
import com.htc.ea.util.util.TimeChronometer;
import com.htc.ea.util.util.TransactionIdUtil;
import com.htc.ea.util.util.WebUtil;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.tigo.ea.tbb.verifydebtor.commons.Constants;
import com.tigo.ea.tbb.verifydebtor.dto.AdditionalInfo;
import com.tigo.ea.tbb.verifydebtor.dto.Data;
import com.tigo.ea.tbb.verifydebtor.dto.GeneralInfo;
import com.tigo.ea.tbb.verifydebtor.dto.OperadorStatus;
import com.tigo.ea.tbb.verifydebtor.dto.Response;
import com.tigo.ea.tbb.verifydebtor.impl.ServiceLogicImpl;
import com.tigo.ea.tbb.verifydebtor.util.AppServiceException;
import com.tigo.ea.tbb.verifydebtor.util.ConsumerAppUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/operator")
@Api(value = "/operator", tags = "Ejecucion de proceso customerinfo")
public class ConsultDebtorRestController {

	private final Class<?> clazz = this.getClass();

	@Autowired
	private Environment env;
	@Autowired
	private ConsumerAppUtil appUtil;
	@Autowired
	private WebUtil webUtil;
	@Autowired
	private ServiceLogicImpl logicImpl;

	/*
	 * 
	 */
	// @ApiOperation(value = "Ejecucion proceso customerinfo", response =
	// GenericDto.class)
	// @ApiResponses(value = { @ApiResponse(code = 200, message = "Success"),
	// @ApiResponse(code = 409, message = "Error") })
	// @ApiImplicitParams({
	// @ApiImplicitParam(name = "operation-reference-id", required = true,
	// dataType = "string", paramType = "header") })
	// @HystrixCommand(fallbackMethod = "hystrixMethod")
/*  @GetMapping(value = "/verify-debtor", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Response> executeGet(
			@RequestParam(name = "accion") String accion,
			@RequestParam(name = "idTransaccion") String idTransaccion, 
			@RequestParam(name = "canal") String canal,
			@RequestParam(name = "servicio") String servicio,
			@RequestParam(name = "tipoDocumento") String tipoDocumento,
			@RequestParam(name = "nroDocumento") String nroDocumento) {
		ResponseEntity<Response> response = null;
		String loggingTrace = "";
		TransactionIdUtil.begin();
		TimeChronometer time = new TimeChronometer();
		try {
  
			TransactionIdUtil.setMsisdn(nroDocumento);
			this.validate(idTransaccion, accion, "VerifyDebtor");
			appUtil.info(Constants.CATEGORY_SERVICE, "", clazz, ConsumerAppUtil.getMethodName(), "verify-debtor: Request",
					"", "", 0L);
			time.start();
			// GenericDto result = logicImpl.customerInfobyDocumentId(request);
			List<OperadorStatus> operadores = new ArrayList<>();
			operadores.add(new OperadorStatus("VIVA", "CON DEUDA"));
			operadores.add(new OperadorStatus("ENTEL", "SIN DEUDA"));
			GeneralInfo genInfo =new GeneralInfo("0", "Proceso satisfactorio");
			AdditionalInfo addInfo = new AdditionalInfo(operadores);
			Data data = new Data (genInfo, addInfo);
			Response result = new Response(data);
			response = new ResponseEntity<>(result, HttpStatus.OK);
			time.stop();
			appUtil.info(Constants.CATEGORY_SERVICE, response, clazz, ConsumerAppUtil.getMethodName(),
					"verify-debtor: Response", "", "", time.elapsedMilisUntilLastStop());
		} catch (AppServiceException e) {			
		String description = "";
		String code = "";
		code = e.getCode();
		description = e.getMessage();
		
			time.stop();
		} catch (Exception e) {
			e.printStackTrace();
			response = webUtil.createFailResponse(null, loggingTrace,
					Long.parseLong(env.getProperty(Constants.CODE_ERROR)), env.getProperty(Constants.MSJ_ERROR), null);
			time.stop();
			appUtil.error(Constants.CATEGORY_SERVICE, clazz, ConsumerAppUtil.getMethodName(),
					"verify-debtor: Error response", "", "", time.elapsedMilisUntilLastStop(), null, e);

		}
		TransactionIdUtil.end();
		return response;
	}

	// ********************************************************************************************************************************
	// ********************************************************************************************************************************
	// ********************************************************************************************************************************
	// HYSTRIX METHODS
	public ResponseEntity<GenericDto> hystrixMethod(@RequestBody GenericDto request,
			@RequestHeader HttpHeaders headersRequest, Throwable error) {
		TransactionIdUtil.begin();
		String loggingTrace = webUtil.validateHeaders(headersRequest);
		if (loggingTrace.indexOf(com.htc.ea.util.util.Constants.VALIDATE_HEADERS_MSG) > -1)
			return webUtil.createFailResponse(null, loggingTrace, 91L, loggingTrace, null);
		ResponseEntity<GenericDto> response = null;
		response = webUtil.createCustomizedResponse(null, 409, loggingTrace,
				Long.parseLong(env.getProperty(Constants.CODE_ERROR)), "Application bussiness error", null);
		appUtil.info(Constants.CATEGORY_SERVICE, request, clazz, ConsumerAppUtil.getMethodName(),
				"nominacion: hystrix error response", "",
				request.getStringProperty(Constants.PARAMETER_DOCUMENTO) != null
						? request.getStringProperty(Constants.PARAMETER_DOCUMENTO) : "",
				0L);

		TransactionIdUtil.end();
		return response;
	}

	
	private void validate(String idTransaccion, String accion, String validAcction) {
		if (idTransaccion == null || idTransaccion.isEmpty() || idTransaccion.length() < 32) {
			throw new AppServiceException("400", "parametro idTransaccion no valido, debe contener 32 caracteres");
		}
		if (!validAcction.equalsIgnoreCase(accion)) {
			throw new AppServiceException("400", "parametro accion no valido");
		}

	}
}
*/