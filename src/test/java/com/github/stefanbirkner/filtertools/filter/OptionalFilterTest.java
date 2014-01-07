package com.github.stefanbirkner.filtertools.filter;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import javax.servlet.*;

import static org.junit.rules.ExpectedException.none;
import static org.mockito.Mockito.*;

public class OptionalFilterTest {
    private final ServletRequest request = mock(ServletRequest.class);
    private final ServletResponse response = mock(ServletResponse.class);
    private final FilterChain filterChain = mock(FilterChain.class);
    private final Predicate<ServletRequest> predicate = mock(Predicate.class);
    private final Filter baseFilter = mock(Filter.class);
    private final OptionalFilter optionalFilter = new OptionalFilter(predicate, baseFilter);

    @Rule
    public final ExpectedException thrown = none();

    @Test
    public void callsFilterIfPredicateMatches() throws Exception {
        when(predicate.test(request)).thenReturn(true);
        optionalFilter.doFilter(request, response, filterChain);
        verify(baseFilter).doFilter(request, response, filterChain);
    }

    @Test
    public void skipsFilterIfPredicateDoesNotMatch() throws Exception {
        when(predicate.test(request)).thenReturn(false);
        optionalFilter.doFilter(request, response, filterChain);
        verifyZeroInteractions(baseFilter);
    }

    @Test
    public void callsFilterChainIfPredicateDoesNotMatch() throws Exception {
        when(predicate.test(request)).thenReturn(false);
        optionalFilter.doFilter(request, response, filterChain);
        verify(filterChain).doFilter(request, response);
    }

    @Test
    public void initializesBaseFilter() throws Exception {
        FilterConfig config = mock(FilterConfig.class);
        optionalFilter.init(config);
        verify(baseFilter).init(config);
    }

    @Test
    public void destroysBaseFilter() {
        optionalFilter.destroy();
        verify(baseFilter).destroy();
    }

    @Test
    public void cannotBeCreatedWithoutPredicate() {
        thrown.expect(NullPointerException.class);
        thrown.expectMessage("The predicate is missing.");
        new OptionalFilter(null, baseFilter);
    }

    @Test
    public void cannotBeCreatedWithoutBaseFilter() {
        thrown.expect(NullPointerException.class);
        thrown.expectMessage("The base filter is missing.");
        new OptionalFilter(predicate, null);
    }
}
