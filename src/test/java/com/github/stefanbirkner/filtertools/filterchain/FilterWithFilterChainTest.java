package com.github.stefanbirkner.filtertools.filterchain;


import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import static org.junit.rules.ExpectedException.none;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class FilterWithFilterChainTest {
    private final Filter filter = mock(Filter.class);
    private final ServletRequest request = mock(ServletRequest.class);
    private final ServletResponse response = mock(ServletResponse.class);
    private final FilterChain baseFilterChain = mock(FilterChain.class);
    private final FilterWithFilterChain filterWithFilterChain = new FilterWithFilterChain(
            filter, baseFilterChain);

    @Rule
    public final ExpectedException thrown = none();

    @Test
    public void callsFilter() throws Exception {
        filterWithFilterChain.doFilter(request, response);
        verify(filter).doFilter(request, response, baseFilterChain);
    }

    @Test
    public void cannotBeCreatedWithoutFilter() {
        thrown.expect(NullPointerException.class);
        thrown.expectMessage("The filter is missing.");
        new FilterWithFilterChain(null, baseFilterChain);
    }

    @Test
    public void cannotBeCreatedWithoutBaseFilterChain() {
        thrown.expect(NullPointerException.class);
        thrown.expectMessage("The base filter chain is missing.");
        new FilterWithFilterChain(filter, null);
    }
}
