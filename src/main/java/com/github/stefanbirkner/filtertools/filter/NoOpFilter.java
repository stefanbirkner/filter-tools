package com.github.stefanbirkner.filtertools.filter;

import javax.servlet.*;
import java.io.IOException;

/**
 * A null object for the {@link javax.servlet.Filter} interface. This filter does nothing but
 * calling the filterchain with the provided request and response.
 */
public class NoOpFilter implements Filter {
    /**
     * Does nothing.
     */
    @Override
    public void init(FilterConfig filterConfig) {
    }

    /**
     * Does nothing but calling the filterchain with the provided request and response.
     *
     * @throws java.io.IOException            if the filter chain throws an {@code IOException}.
     * @throws javax.servlet.ServletException if the filter chain throws an {@code ServletException}.
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
        throws IOException, ServletException {
        filterChain.doFilter(request, response);
    }

    /**
     * Does nothing.
     */
    @Override
    public void destroy() {
    }
}
