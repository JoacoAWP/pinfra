package com.filters;

import javax.inject.Inject;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.client.GestionUsuario;
import com.enums.TipoUsuario;

import java.io.IOException;

public class FiltroAnalista implements Filter {
	@Inject
	private GestionUsuario gestionUsuario;

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;

		if (TipoUsuario.ANALISTA.equals(gestionUsuario.getUsuarioLogeado().getTipoUsuario())) {
			chain.doFilter(request, response);
		} else {
			httpResponse.sendRedirect(httpRequest.getContextPath() + "/acceso_denegado.xhtml");
		}
	}
}
