package com.github.stefanbirkner.filtertools.filterchain;

import javax.servlet.*;
import java.io.IOException;

/**
 * Merges a {@link Filter} and a {@link FilterChain} into a new  {@code FilterChain}.
 *
 * <p>Calling {@link #doFilter(ServletRequest, ServletResponse)} on the
 * {@code FilterWithFilterChain} is like calling
 * {@link Filter#doFilter(ServletRequest, ServletResponse, FilterChain)} on the filter with the
 * {@code basisFilterChain}.
 */
public class FilterWithFilterChain implements FilterChain {
    private final Filter filter;
    private final FilterChain basisFilterChain;

    public FilterWithFilterChain(Filter filter, FilterChain basisFilterChain) {
        if (filter == null)
            throw new NullPointerException("The filter is missing.");
        this.filter = filter;
        if (basisFilterChain == null)
            throw new NullPointerException("The base filter chain is missing.");
        this.basisFilterChain = basisFilterChain;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response) throws IOException, ServletException {
        filter.doFilter(request, response, basisFilterChain);
    }
}
