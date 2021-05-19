package it.objectmethod.tatami.controller.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(2)
public class RandomFilter implements Filter {

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
		throws IOException, ServletException {
		//		HttpServletRequest req = (HttpServletRequest) request;
		//
		//		System.out.println("RANDOM Hai chiamato la url: " + req.getRequestURI());
		chain.doFilter(request, response);
	}
}