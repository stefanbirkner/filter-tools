package com.github.stefanbirkner.filtertools.filter;

import org.hamcrest.Matcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import javax.servlet.*;

import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.not;
import static org.junit.rules.ExpectedException.none;
import static org.mockito.Mockito.*;

public class OptionalFilterTest {
    private final ServletRequest request = mock(ServletRequest.class);
    private final ServletResponse response = mock(ServletResponse.class);
    private final FilterChain filterChain = mock(FilterChain.class);
    private final Predicate<ServletRequest> predicate = mock(Predicate.class);
    private final Filter baseFilter = mock(Filter.class);
    private final OptionalFilter predicateFilter = new OptionalFilter(predicate, baseFilter);

    @Rule
    public final ExpectedException thrown = none();

    @Test
    public void callsFilterIfPredicateMatches() throws Exception {
        when(predicate.test(request)).thenReturn(true);
        predicateFilter.doFilter(request, response, filterChain);
        verify(baseFilter).doFilter(request, response, filterChain);
    }

    @Test
    public void skipsFilterIfPredicateDoesNotMatch() throws Exception {
        when(predicate.test(request)).thenReturn(false);
        predicateFilter.doFilter(request, response, filterChain);
        verifyZeroInteractions(baseFilter);
    }

    @Test
    public void callsFilterChainIfPredicateDoesNotMatch() throws Exception {
        when(predicate.test(request)).thenReturn(false);
        predicateFilter.doFilter(request, response, filterChain);
        verify(filterChain).doFilter(request, response);
    }

    @Test
    public void callsFilterIfMatcherMatches() throws Exception {
        OptionalFilter filter = new OptionalFilter(anything(), baseFilter);
        filter.doFilter(request, response, filterChain);
        verify(baseFilter).doFilter(request, response, filterChain);
    }

    @Test
    public void skipsFilterIfMatcherDoesNotMatch() throws Exception {
        OptionalFilter filter = new OptionalFilter(not(anything()), baseFilter);
        filter.doFilter(request, response, filterChain);
        verifyZeroInteractions(baseFilter);
    }

    @Test
    public void initializesBaseFilter() throws Exception {
        FilterConfig config = mock(FilterConfig.class);
        predicateFilter.init(config);
        verify(baseFilter).init(config);
    }

    @Test
    public void destroysBaseFilter() {
        predicateFilter.destroy();
        verify(baseFilter).destroy();
    }

    @Test
    public void cannotBeCreatedWithoutPredicate() {
        thrown.expect(NullPointerException.class);
        thrown.expectMessage("The predicate is missing.");
        new OptionalFilter((Predicate) null, baseFilter);
    }

    @Test
    public void cannotBeCreatedWithoutBaseFilter() {
        thrown.expect(NullPointerException.class);
        thrown.expectMessage("The base filter is missing.");
        new OptionalFilter(predicate, null);
    }

    @Test
    public void cannotBeCreatedWithoutMatcher() {
        thrown.expect(NullPointerException.class);
        thrown.expectMessage("The matcher is missing.");
        new OptionalFilter((Matcher) null, baseFilter);
    }
}
