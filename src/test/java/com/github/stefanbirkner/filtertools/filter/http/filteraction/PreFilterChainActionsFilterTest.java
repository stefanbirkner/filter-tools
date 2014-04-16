package com.github.stefanbirkner.filtertools.filter.http.filteraction;

import org.junit.Test;
import org.mockito.InOrder;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;

public class PreFilterChainActionsFilterTest {
    private static final HttpServletRequest DUMMY_REQUEST = mock(HttpServletRequest.class);
    private static final HttpServletResponse DUMMY_RESPONSE = mock(HttpServletResponse.class);
    private final FilterAction firstAction = mock(FilterAction.class);
    private final FilterAction secondAction = mock(FilterAction.class);
    private final FilterChain filterChain = mock(FilterChain.class);
    private final Filter filter = new PreFilterChainActionsFilter(firstAction, secondAction);

    /* You must copy the tests from ActionsFilterTest if PreFilterChainActionsFilter doesn't extend ActionsFilter
     * anymore .
     */

    @Test
    public void executesActionsBeforeInvokingFilterChain() throws Exception {
        filter.doFilter(DUMMY_REQUEST, DUMMY_RESPONSE, filterChain);
        InOrder inOrder = inOrder(firstAction, secondAction, filterChain);
        inOrder.verify(firstAction).execute(DUMMY_REQUEST, DUMMY_RESPONSE);
        inOrder.verify(filterChain).doFilter(DUMMY_REQUEST, DUMMY_RESPONSE);
    }

    @Test
    public void executesActionsInOrder() throws Exception {
        filter.doFilter(DUMMY_REQUEST, DUMMY_RESPONSE, filterChain);
        InOrder inOrder = inOrder(firstAction, secondAction);
        inOrder.verify(firstAction).execute(DUMMY_REQUEST, DUMMY_RESPONSE);
        inOrder.verify(secondAction).execute(DUMMY_REQUEST, DUMMY_RESPONSE);
    }
}
