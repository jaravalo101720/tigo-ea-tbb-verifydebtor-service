package com.tigo.ea.tbb.verifydebtor.rest;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.htc.ea.util.util.TimeChronometer;
import com.htc.ea.util.util.TransactionIdUtil;
import com.htc.ea.util.util.WebUtil;
import com.tigo.ea.tbb.verifydebtor.commons.Constants;
import com.tigo.ea.tbb.verifydebtor.dto.AdditionalInfo;
import com.tigo.ea.tbb.verifydebtor.dto.Data;
import com.tigo.ea.tbb.verifydebtor.dto.GeneralInfo;
import com.tigo.ea.tbb.verifydebtor.dto.OperadorStatus;
import com.tigo.ea.tbb.verifydebtor.dto.Response;
import com.tigo.ea.tbb.verifydebtor.impl.VerifyDebtorServiceImpl;
import com.tigo.ea.tbb.verifydebtor.util.AppServiceException;
import com.tigo.ea.tbb.verifydebtor.util.ConsumerAppUtil;
import com.tigo.ea.tbb.verifydebtor.util.ValidateRequest;

import io.swagger.annotations.Api;

@RestController
@RequestMapping("/operator")
@Api(value = "/operator", tags = "Ejecucion de proceso customerinfo")
public class VerifyDebtorRestController {
	
	
	private final Class<?> clazz = this.getClass();

	@Autowired
	private Environment env;
	@Autowired
	private ConsumerAppUtil appUtil;
	@Autowired
	private WebUtil webUtil;
	
	@Autowired
    private ValidateRequest validateRequest;
	
	@Autowired
	private VerifyDebtorServiceImpl verifyDebtorService;
	
	@GetMapping(value = "/verify-debtor", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> executeGet(
			@RequestParam(name = "accion") String accion,
			@RequestParam(name = "idTransaccion") String idTransaccion, 
			@RequestParam(name = "canal") String canal,
			@RequestParam(name = "servicio") String servicio,
			@RequestParam(name = "tipoDocumento") String tipoDocumento,
			@RequestParam(name = "nroDocumento") String nroDocumento) {
		
		ResponseEntity<?> response = null;
		List<Response> responseData= null;
		String loggingTrace = "";
		TransactionIdUtil.begin();
		TimeChronometer time = new TimeChronometer();
		try {

			TransactionIdUtil.setMsisdn(nroDocumento);
			
			if(validateRequest.stringValidation(accion, idTransaccion, canal, servicio, tipoDocumento, nroDocumento) ) {
				throw new AppServiceException(env.getProperty("error.code.1"), env.getProperty("tbb.enduser.badrequest"));
			}
			
			this.validate(idTransaccion, accion, "VerifyDebtor");
			appUtil.info(Constants.CATEGORY_SERVICE, "", clazz, ConsumerAppUtil.getMethodName(), "verify-debtor: Request",
					"", "", 0L);
			time.start();
		
			responseData=verifyDebtorService.excuteConumerOperator(idTransaccion, servicio, tipoDocumento, nroDocumento);
			response= new ResponseEntity<>(responseData, HttpStatus.OK);
			
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
	
	private void validate(String idTransaccion, String accion, String validAcction) {
		if (idTransaccion == null || idTransaccion.isEmpty() || idTransaccion.length() < 32) {
			throw new AppServiceException("400", "parametro idTransaccion no valido, debe contener 32 caracteres");
		}
		if (!validAcction.equalsIgnoreCase(accion)) {
			throw new AppServiceException("400", "parametro accion no valido");
		}

	}
}