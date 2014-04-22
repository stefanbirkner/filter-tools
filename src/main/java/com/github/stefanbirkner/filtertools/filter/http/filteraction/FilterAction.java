package com.github.stefanbirkner.filtertools.filter.http.filteraction;

import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Does some action based on the request and/or the response. E.g.
 * <ul>
 * <li>Set request attributes.</li>
 * <li>Set a cookie.</li>
 * <li>Do some action that needs information from the request or response.</li>
 * </ul>
 * {@code FilterAction}s are designed to be used in a filter before or after the {@link javax.servlet.FilterChain} is
 * invoked. Filter tools itself provides the
 * {@link com.github.stefanbirkner.filtertools.filter.http.filteraction.PreFilterChainActionsFilter}
 * and the {@link com.github.stefanbirkner.filtertools.filter.http.filteraction.PostFilterChainActionsFilter}.
 *
 * @since 1.2.0
 */
public interface FilterAction {
    /**
     * Called to indicate to an action that it is being placed into service. The init method is called exactly once The
     * init method must complete successfully before the action executed.
     *
     * @param filterConfig A filter configuration object used by a servlet container
     *                     to pass information to an action during initialization.
     * @throws ServletException if an exception has occurred that interferes with the action's normal operation
     */
    void init(FilterConfig filterConfig) throws ServletException;

    /**
     * The {@code execute} method of the action is called by a filter before or after the
     * {@link javax.servlet.FilterChain} is invoked dependeing on the filter itself.
     *
     * @param request  an {@link javax.servlet.http.HttpServletRequest}
     * @param response an {@link javax.servlet.http.HttpServletResponse}
     * @throws IOException      if an input or output exception occurs
     * @throws ServletException if an exception has occurred that interferes with the action's normal operation
     */
    void execute(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException;

    /**
     * Called to indicate to an action that it is being taken out of service. This
     * method is only called once. After this method is called, the
     * execute method is not called again on this instance of the filter.
     * <p>This method gives the action an opportunity to clean up any resources that are being held (for
     * example, memory, file handles, threads).
     */
    void destroy();
}
