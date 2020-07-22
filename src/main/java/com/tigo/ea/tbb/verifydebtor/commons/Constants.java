package com.tigo.ea.tbb.verifydebtor.commons;


/**
 * @author Alex Tusell - HTC
 *
 */
public class Constants
{
	// REDIS
	public static final String REDIS_CONNECTION_IP = "redis.ip";
	public static final String REDIS_CONNECTION_PORT = "redis.port";
	public static final String REDIS_CHANNEL_NAME = "redis.channel.name";

	//LOGGER LEVEL
	public static final String CATEGORY_SERVICE = "service";
	public static final String CATEGORY_CONSUMER = "consumer";
	public static final String CATEGORY_TARGET = "target";
	
	//BSS URL SERVICES
	public static final String BSS_URL_GET_CUSTOMER_INFO = "bss.url.getCustomerInfoByDocumentId";
	public static final String BSS_URL_NOMINATE_EXISTING_CUSTOMER = "bss.url.nominateExistingCustomerTM";
	public static final String BSS_URL_REGISTER_DEVICE_EXISTING = "bss.url.registerDeviceExistingCustomer";
	public static final String BSS_URL_NOMINATE_NEW_CUSTOMER = "bss.url.nominateNewCustomerTM";
	public static final String BSS_URL_REGISTER_DEVICE_NEW = "bss.url.registerDeviceNewCustomer";
	public static final String CBA_STATUS_CODE_NEW_CUSTOMER = "cba.statuscode.newcustomer";
	
	//BSS SERVICES RESPONSE
	public static final String BSS_RESPONSE_GET_CUSTOMER_INFO = "bss.response-params.getCustomerInfoByDocumentId";
	public static final String BSS_RESPONSE_NOMINATE_EXISTING_CUSTOMER = "bss.response-params.nominateExistingCustomerTM";
	public static final String BSS_RESPONSE_REGISTER_DEVICE_EXISTING = "bss.response-params.registerDeviceExistingCustomer";
	public static final String BSS_RESPONSE_NOMINATE_NEW_CUSTOMER = "bss.response-params.nominateNewCustomerTM";
	public static final String BSS_RESPONSE_REGISTER_DEVICE_NEW = "bss.response-params.registerDeviceNewCustomer";

	//CODE
	public static final String CODE_SUCCESSFUL = "code.successful";
	public static final String CODE_ERROR = "code.error";
	public static final String CODE_MANDATORY = "code.mandatory";
	public static final String CODE_FORMAT = "code.format";
	public static final String CODE_SERVICE = "code.service";
	public static final String CODE_DB = "code.db";
	public static final String CODE_EXECUTION = "code.execution";
	public static final String CODE_SOCKET = "code.socket";

	//MSJ
	public static final String MSJ_SUCCESSFUL = "msj.successful";
	public static final String MSJ_ERROR = "msj.error";
	public static final String MSJ_MANDATORY = "msj.mandatory";
	public static final String MSJ_FORMAT = "msj.format";
	public static final String MSJ_SERVICE = "msj.service";
	public static final String MSJ_DB = "msj.db";
	public static final String MSJ_EXECUTION = "msj.execution";
	public static final String MSJ_SOCKET = "msj.socket";

	public static final String STATUS_OK = "status.ok";
	public static final String STATUS_ERROR = "status.error";
	public static final String STATUS_NO_RESULT = 	"status.no.result";

	//autenticacion
	public static final String PARAMETER_DOCUMENTO = "documento";
	public static final String BSS_AUTH_USER = 	"tbb.qvantel.cba.user";
	public static final String BSS_AUTH_PASS = 	"tbb.qvantel.cba.password";
	
	//request consumer
	public static final String PARAMETER_IDTRANSACTION = "idTransaction";
	public static final String PARAMETER_SERVICE = "service";
	public static final String PARAMETER_TIPODOCUMENTO = "tipoDocumento";
	public static final String PARAMETER_NRODOCUMENTO = "nroDocumento";
	
	//response consumer
	public static final String OPERATOR_AXS = "AXS";
	public static final String PARAMETER_ESTADO = "estado";
	
	public static final String OPERATOR_VIVA = "VIVA";
	public static final String PARAMETER_ESTADO_DEUDA = "estadoDeuda";
	
	public static final String OPERATOR_ENTEL = "ENTEL";
	
	public static final String OPERATOR_COTAS = "COTAS";
	public static final String PARAMETER_ESTADO_DEUDA_COTAS = "EstadoDeuda";
	
	//estado null si llega a fallar el consumo del servicio
	public static final String ESTADO = null;

}
