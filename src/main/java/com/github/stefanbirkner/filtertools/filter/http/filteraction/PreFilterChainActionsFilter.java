package com.github.stefanbirkner.filtertools.filter.http.filteraction;


import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Executes {@link com.github.stefanbirkner.filtertools.filter.http.filteraction.FilterAction}s before invoking the
 * {@link javax.servlet.FilterChain}.
 *
 * @since 1.2.0
 */
public class PreFilterChainActionsFilter extends ActionsFilter {
    /**
     * Create a new filter.
     * {@link #doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse, javax.servlet.FilterChain)}
     * executes the actions in the same order as you provide it.
     *
     * @param actions a list of {@link com.github.stefanbirkner.filtertools.filter.http.filteraction.FilterAction}s
     */
    public PreFilterChainActionsFilter(FilterAction... actions) {
        super(actions);
    }

    @Override
    protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
        throws IOException, ServletException {
        executeAllFilterActions(request, response);
        filterChain.doFilter(request, response);
    }
}
