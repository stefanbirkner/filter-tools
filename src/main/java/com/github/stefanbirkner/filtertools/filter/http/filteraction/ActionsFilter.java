package com.github.stefanbirkner.filtertools.filter.http.filteraction;

import com.github.stefanbirkner.filtertools.filter.http.HttpFilter;

import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static java.util.Arrays.asList;

/**
 * Base class for filters that support
 * {@link com.github.stefanbirkner.filtertools.filter.http.filteraction.FilterAction}s. It initialized and destroys the
 * actions. All actions are executed when the sub-class calls
 * {@link #executeAllFilterActions(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)}.
 *
 * @since 1.2.0
 */
public abstract class ActionsFilter extends HttpFilter {
    private final List<FilterAction> actions;

    public ActionsFilter(FilterAction... actions) {
        this.actions = asList(actions);
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        List<FilterAction> initializedActions = new ArrayList<FilterAction>();
        try {
            for (FilterAction action : actions) {
                action.init(filterConfig);
                initializedActions.add(action);
            }
        } catch (ServletException e) {
            destroyActions(initializedActions);
            throw e;
        } catch (RuntimeException e) {
            destroyActions(initializedActions);
            throw e;
        }
    }

    protected void executeAllFilterActions(HttpServletRequest request, HttpServletResponse response) throws IOException,
            ServletException {
        for (FilterAction action : actions)
            action.execute(request, response);
    }

    @Override
    public void destroy() {
        destroyActions(actions);
    }

    private void destroyActions(List<FilterAction> actionsToDestroy) {
        destroyRemainingActions(actionsToDestroy.iterator());
    }

    private void destroyRemainingActions(Iterator<FilterAction> it) {
        if (it.hasNext())
            try {
                it.next().destroy();
            } finally {
                destroyRemainingActions(it);
            }
    }
}
