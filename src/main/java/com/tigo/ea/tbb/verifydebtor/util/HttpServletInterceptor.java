package com.tigo.ea.tbb.verifydebtor.util;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.springframework.web.filter.GenericFilterBean;


public class HttpServletInterceptor extends GenericFilterBean   {

	@Override
	public void doFilter(ServletRequest arg0, ServletResponse arg1,FilterChain arg2) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) arg0;
		HttpServletContextUtil.setHttpServletRequestContext(req);
        arg2.doFilter(arg0, arg1);
	}
}
