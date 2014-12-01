package com.github.stefanbirkner.filtertools.filter.http;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * An implementation of the {@link javax.servlet.Filter}
 * interface that works with {@link javax.servlet.http.HttpServletRequest} and
 * {@link javax.servlet.http.HttpServletResponse} instead of {@link javax.servlet.ServletRequest} and
 * {@link javax.servlet.ServletResponse}. You have to implement the template method
 * {@link #doFilter(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, javax.servlet.FilterChain)}
 * instead of
 * {@link Filter#doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse, javax.servlet.FilterChain)}.
 *
 * @since 1.1.0
 */
public abstract class HttpFilter implements Filter {
    /**
     * Does nothing. Can be overridden.
     *
     * @param filterConfig A filter configuration object used by a servlet container
     *                     to pass information to a filter during initialization.
     */
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    /**
     * Delegates to {@link #doFilter(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, javax.servlet.FilterChain)}.
     * Fails if the request is not an {@link javax.servlet.http.HttpServletRequest} or the response is not an
     * {@link javax.servlet.http.HttpServletResponse}
     *
     * @param request     an {@link javax.servlet.http.HttpServletRequest}
     * @param response    an {@link javax.servlet.http.HttpServletResponse}
     * @param filterChain used to invoke the next filter in the chain, or if the calling filter
     *                    is the last filter in the chain, to invoke the resource at the end of the chain.
     * @throws IOException      if an input or output exception occurs
     * @throws ServletException if an exception has occurred that interferes with the filter's normal operation
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
        throws IOException, ServletException {
        if (!(request instanceof HttpServletRequest))
            throw new NotAnHttpServletRequestException(request);
        if (!(response instanceof HttpServletResponse))
            throw new NotAnHttpServletResponseException(response);
        doFilter((HttpServletRequest) request, (HttpServletResponse) response, filterChain);
    }

    /**
     * A replacement for {@link Filter#doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse, javax.servlet.FilterChain)}
     * that uses {@link javax.servlet.http.HttpServletRequest} and {@link javax.servlet.http.HttpServletResponse}.
     *
     * @param request  an {@link javax.servlet.http.HttpServletRequest}
     * @param response an {@link javax.servlet.http.HttpServletResponse}
     * @param filterChain used to invoke the next filter in the chain, or if the calling filter
     *                    is the last filter in the chain, to invoke the resource at the end of the chain.
     * @throws IOException      if an input or output exception occurs
     * @throws ServletException if an exception has occurred that interferes with the filter's normal operation
     */
    protected abstract void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
        throws IOException, ServletException;

    /**
     * Does nothing. Can be overridden.
     */
    @Override
    public void destroy() {
    }
}
