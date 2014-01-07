package com.github.stefanbirkner.filtertools.filter;

import com.github.stefanbirkner.filtertools.filterchain.FilterWithFilterChain;

import javax.servlet.*;
import java.io.IOException;

/**
 * Encases multiple filters into a single filter.
 *
 * <p>This allows you to include multiple filters into your web.xml by including a single filter.
 * You can provide recommended filter combinations as a single filter, too.
 */
public class EncasedFilters implements Filter {
    private static final Filter NO_OP_FILTER = new NoOpFilter();
    private final Filter firstFilter;
    private final Filter secondFilter;

    public EncasedFilters(Filter... filters) {
        this.firstFilter = (filters.length == 0) ? NO_OP_FILTER : filters[0];
        this.secondFilter = (filters.length > 1) ? new EncasedFilters(rest(filters)) : NO_OP_FILTER;
    }

    private Filter[] rest(Filter[] filters) {
        Filter[] rest = new Filter[filters.length - 1];
        for (int i = 0; i < rest.length; ++i)
            rest[i] = filters[i + 1];
        return rest;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        firstFilter.init(filterConfig);
        secondFilter.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         final FilterChain filterChain)
            throws IOException, ServletException {
        FilterChain secondFilterWithFilterChain = new FilterWithFilterChain(
                secondFilter, filterChain);
        firstFilter.doFilter(request, response, secondFilterWithFilterChain);
    }

    @Override
    public void destroy() {
        try {
            firstFilter.destroy();
        } finally {
            secondFilter.destroy();
        }
    }
}
