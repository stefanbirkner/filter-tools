package com.github.stefanbirkner.filtertools;

import javax.servlet.*;
import java.io.IOException;

/**
 * This filter does nothing but calling the filter chain with the provided request and response.
 */
public class NoOpFilter implements Filter {
    /**
     * Does nothing.
     */
    @Override
    public void init(FilterConfig filterConfig) {
    }

    /**
     * Does nothing but calling the filter chain with the provided request and response.
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
