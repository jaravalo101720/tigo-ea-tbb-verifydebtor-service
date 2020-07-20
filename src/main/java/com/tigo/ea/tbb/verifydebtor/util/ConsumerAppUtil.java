package com.tigo.ea.tbb.verifydebtor.util;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import com.htc.ea.util.log.EventData;
import com.htc.ea.util.log.EventLevel;
import com.htc.ea.util.log.LoggingServiceFacade;
import com.htc.ea.util.parsing.ParsingUtil;
import com.htc.ea.util.util.TransactionIdUtil;

@Component
public class ConsumerAppUtil {

	@Autowired
	private Environment env;

	@Autowired
	private LoggingServiceFacade logger;

	@SuppressWarnings("unused")
	@Autowired
	private ParsingUtil parsingUtil;

	public void info(String category,Object data, Class<?> clazz, String serviceName, String msg, String detail, String msisdn, long duration){
		try{
			if (logging()){
				String dataType = data!=null ? "json" : null;
				String endUserLocation = getConsumerLocation();
				String msisdn2 = msisdn == null ? "" : msisdn;
				//				endUserLocation = this.getIpRequest();
				String serverLocation = InetAddress.getLocalHost().getHostAddress();
				String refId = TransactionIdUtil.getId();
				String sequence = TransactionIdUtil.nextSequenceId();
				msisdn2 = TransactionIdUtil.getMsisdn() != null && !TransactionIdUtil.getMsisdn().isEmpty()
			            ? TransactionIdUtil.getMsisdn() : msisdn2;
				logger.log(EventData.builder()
						.category(category)
						.level(EventLevel.INFO.toString())
						.endUserLocation(endUserLocation)
						.serverLocation(serverLocation)
						.endUser(msisdn2)
						.source(clazz)
						.name(serviceName)
						.message(msg)
						.detail(detail)
						.originReferenceId(refId)
						.referenceId(refId)
						.duration(duration)
						.automatic(false)
						.successful(true)
						.sequence(sequence)
						.data(data)
						.dataType(dataType)
						.build());
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void debug(String category,Object data, Class<?> clazz, String serviceName, String msg, String detail, String msisdn, Long duration){
		try{
			if (logging()){
				String dataType = data!=null ? "json" : null;
				String endUserLocation = getConsumerLocation();
				String msisdn2 = msisdn == null ? "" : msisdn;
				//				endUserLocation = this.getIpRequest();
				String serverLocation = InetAddress.getLocalHost().getHostAddress();
				String refId = TransactionIdUtil.getId();
				String sequence = TransactionIdUtil.nextSequenceId();
				msisdn2 = TransactionIdUtil.getMsisdn() != null && !TransactionIdUtil.getMsisdn().isEmpty()
			            ? TransactionIdUtil.getMsisdn() : msisdn2;
				logger.log(EventData.builder()
						.category(category)
						.level(EventLevel.DEBUG.toString())
						.endUserLocation(endUserLocation)
						.serverLocation(serverLocation)
						.endUser(msisdn2)
						.source(clazz)
						.name(serviceName)
						.message(msg )
						.detail(detail)
						.originReferenceId(refId)
						.referenceId(refId)
						.duration(duration)
						.automatic(false)
						.successful(true)
						.sequence(sequence)
						.data(data)
						.dataType(dataType)
						.build());
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void error(String category, Class<?> clazz, String serviceName, String msg, String detail, String msisdn, long duration, String responseCode, Throwable exception) {
		try{
			if (logging()){
				String endUserLocation = getConsumerLocation();
				String serverLocation = InetAddress.getLocalHost().getHostAddress();
				String msisdn2 = msisdn == null ? "" : msisdn;
				String refId = TransactionIdUtil.getId();
				String sequence = TransactionIdUtil.nextSequenceId();
				msisdn2 = TransactionIdUtil.getMsisdn() != null && !TransactionIdUtil.getMsisdn().isEmpty()
			            ? TransactionIdUtil.getMsisdn() : msisdn2;
				logger.log(EventData.builder()
						.category(category)
						.level(EventLevel.ERROR.toString())
						.endUserLocation(endUserLocation)
						.serverLocation(serverLocation)
						.endUser(msisdn2)
						.source(clazz)
						.name(serviceName)
						.message(msg)
						.detail(detail)
						.responseCode(responseCode)
						.originReferenceId(refId)
						.referenceId(refId)
						.duration(duration)
						.automatic(false)
						.successful(true)
						.exception(exception)
						.sequence(sequence)
						.build());
			}
		}catch (Exception e) {
			e.printStackTrace();
		}

	}


	public static String getConsumerLocation(){
		String endUserLocation = "unknownHost";
		HttpServletRequest context = HttpServletContextUtil.getHttpServletRequestContext();
		if (context != null) {
			String ip = context.getHeader("Remote_Addr");
			if(ip != null){
				endUserLocation = ip;
			}
		}
		//Si no hay balanceador
		if (endUserLocation.equals("unknownHost")){
			endUserLocation = getServerLocation();
		}

		return endUserLocation;
	}


	public static String getServerLocation(){
		try {
			return InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		return "unknownHost";
	}

	public String getParamFromHeaders(HttpHeaders httpHeaders,String nameParam){
		String param ="";
		Map<String, String> tempMap = httpHeaders.toSingleValueMap();
		param = tempMap.get(nameParam);

		return param;
	}

	public boolean logging(){
		return Boolean.parseBoolean(env.getProperty("logging.enabled"));
	}

	public static String getMethodName() {
		return Thread.currentThread().getStackTrace()[2].getMethodName();
	}
	
	public XMLGregorianCalendar convertDateToXmlGregorianCalendar(Date fecha) throws DatatypeConfigurationException {
		GregorianCalendar c = new GregorianCalendar();
		c.setTime(fecha);
		XMLGregorianCalendar cal = DatatypeFactory.newInstance().newXMLGregorianCalendar(c);
		return cal;
	}
}
