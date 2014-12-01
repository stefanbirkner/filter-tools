package com.github.stefanbirkner.filtertools.filter;

import org.junit.Test;

import javax.servlet.*;

import static org.hamcrest.Matchers.any;
import static org.hamcrest.Matchers.*;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.*;

public class EncasedFiltersTest {
    private final Filter firstFilter = mock(Filter.class);
    private final Filter secondFilter = mock(Filter.class);
    private final FilterConfig config = mock(FilterConfig.class);
    private final ServletRequest request = mock(ServletRequest.class);
    private final ServletResponse response = mock(ServletResponse.class);
    private final FilterChain filterChain = mock(FilterChain.class);

    @Test
    public void callsFirstFilterWithProvidedRequestAndResponse() throws Exception {
        EncasedFilters encasedFilters = new EncasedFilters(firstFilter);
        encasedFilters.doFilter(request, response, filterChain);
        verify(firstFilter).doFilter(
            argThat(is(sameInstance(request))),
            argThat(is(sameInstance(response))),
            argThat(is(any(FilterChain.class))));
    }

    @Test
    public void callsFilterChainAtTheEnd() throws Exception {
        EncasedFilters encasedFilters = new EncasedFilters(new NoOpFilter(), new NoOpFilter());
        encasedFilters.doFilter(request, response, filterChain);
        verify(filterChain).doFilter(request, response);
    }

    @Test
    public void callsFilterChainEvenWithoutFilters() throws Exception {
        EncasedFilters encasedFilters = new EncasedFilters();
        encasedFilters.doFilter(request, response, filterChain);
        verify(filterChain).doFilter(request, response);
    }

    @Test
    public void initializesEveryFilter() throws Exception {
        EncasedFilters encasedFilters = new EncasedFilters(firstFilter, secondFilter);
        encasedFilters.init(config);
        verify(firstFilter).init(config);
        verify(secondFilter).init(config);
    }

    @Test
    public void destroysEveryFilter() throws Exception {
        EncasedFilters encasedFilters = new EncasedFilters(firstFilter, secondFilter);
        encasedFilters.destroy();
        verify(firstFilter).destroy();
        verify(secondFilter).destroy();
    }

    @Test
    public void destroySecondFilterEvenIfItFailsToDestroyFirstFilter() {
        doThrow(new RuntimeException()).when(firstFilter).destroy();
        EncasedFilters encasedFilters = new EncasedFilters(firstFilter, secondFilter);
        try {
            encasedFilters.destroy();
        } catch (RuntimeException e) {
            verify(secondFilter).destroy();
        }
    }
}
