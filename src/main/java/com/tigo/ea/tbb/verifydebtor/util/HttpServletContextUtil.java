package com.tigo.ea.tbb.verifydebtor.util;

import javax.servlet.http.HttpServletRequest;

public class HttpServletContextUtil {
	
	private static ThreadLocal<HttpServletRequest> threadLocal = new ThreadLocal<HttpServletRequest>(){
		@Override
		protected HttpServletRequest initialValue() {
			return null;
		}
	};
	
	private HttpServletContextUtil() {
	}

	public static HttpServletRequest getHttpServletRequestContext() {
		return threadLocal.get();
	}

	public static void setHttpServletRequestContext(HttpServletRequest context) {
		threadLocal.set(context);
	}

	public static void removeHttpServletRequestContext() {
		threadLocal.remove();
	}
}
