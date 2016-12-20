package com.seentao.stpedu.filter;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;

public class CharacterEncodingFilter implements Filter {

	private FilterConfig filterConfig;
	private String defaultCharset = "UTF-8";

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		this.filterConfig = filterConfig;
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse res,
			FilterChain chain) throws IOException, ServletException {

		MyHttpServletRequestWrapper request = new MyHttpServletRequestWrapper(
				(HttpServletRequest) req);
		HttpServletResponse response = (HttpServletResponse) res;

		String charset = filterConfig.getInitParameter("charset");
		if (charset == null) {
			charset = defaultCharset;
		}
		request.setCharacterEncoding(charset);
		response.setCharacterEncoding(charset);
		response.setContentType("text/html;charset=" + charset);
		chain.doFilter(request, response);
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

}

class MyHttpServletRequestWrapper extends HttpServletRequestWrapper {
	private HttpServletRequest request;

	public MyHttpServletRequestWrapper(HttpServletRequest request) {
		super(request);
		this.request = request;
	}

	@Override
	public String getParameter(String name) {
		try {
			String value = this.request.getParameter(name);
			if (value == null) {
				return null;
			}

			if (this.request.getMethod().equalsIgnoreCase("post")) {
				return value;
			}

			value = new String(value.getBytes("ISO8859-1"),
					this.request.getCharacterEncoding());
			return value;
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}

}
