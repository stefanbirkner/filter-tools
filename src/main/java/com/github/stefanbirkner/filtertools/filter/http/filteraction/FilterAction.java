package com.github.stefanbirkner.filtertools.filter.http.filteraction;

import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * {@code FilterAction}s are components of a frequently used type of {@link javax.servlet.Filter}s. Such filters do
 * a piece of work before or after calling the {@link javax.servlet.FilterChain}, but never wrap the request or
 * response. Examples are filters that set request attributes or cookies. This piece of works can be encapsulated by a
 * {@code FilterAction}.
 * <h2>Build a Filter Action</h2>
 * <p>{@link #execute(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)} is an action's
 * method that executes the desired piece of work. The action can be configured by
 * {@link #init(javax.servlet.FilterConfig)} which is the equivalent to
 * {@link javax.servlet.Filter#init(javax.servlet.FilterConfig)}. The {@link #destroy()} can be used to clean up
 * resources. A simple filter action may look like this.
 * <pre>
 * public class YourAction implements FilterAction {
 *   private YourService service;
 *
 *   public void init(FilterConfig config) throws ServletException {
 *       String param = config.getInitParameter("your parameter");
 *       service = new YourService(param);
 *   }
 *
 *   public void execute(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
 *       request.setAttribute("your attribute", service.getNextValue());
 *   }
 *
 *   public void destroy() {
 *       service.shutdown();
 *   }
 * }
 * </pre>
 * <h2>Build a Filter with Filter Actions</h2>
 * <p>Filter tools provides tow filter that are executing actions. The
 * {@link com.github.stefanbirkner.filtertools.filter.http.filteraction.PreFilterChainActionsFilter} executes actions
 * before it invokes the filter chain and
 * the {@link com.github.stefanbirkner.filtertools.filter.http.filteraction.PostFilterChainActionsFilter} executes it
 * afterwards. You use filter actions by creating your own filter that inherits one of the two filters. Look at this
 * example:</p>
 * <pre>
 * public class YourFilter implements PreFilterChainActionsFilter {
 *   public YourFilter() {
 *       super(new FirstAction(), new SecondAction());
 *   }
 * }
 * </pre>
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
     * {@link javax.servlet.FilterChain} is invoked depending on the filter itself.
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
