package com.github.stefanbirkner.filtertools;

import org.junit.Test;

import javax.servlet.FilterChain;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.sameInstance;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class NoOpFilterTest {
    final ServletRequest request = mock(ServletRequest.class);
    final ServletResponse response = mock(ServletResponse.class);
    final FilterChain filterChain = mock(FilterChain.class);
    final NoOpFilter filter = new NoOpFilter();

    @Test
    public void callsTheFilterChainWithProvidedRequestAndResponse() throws Exception {
        filter.doFilter(request, response, filterChain);
        verify(filterChain).doFilter(
                argThat(is(sameInstance(request))),
                argThat(is(sameInstance(response))));
    }
}
