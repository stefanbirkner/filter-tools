package com.github.stefanbirkner.filtertools.filter.http.filteraction;

import org.junit.Test;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;

public class ActionsFilterTest {
    private static final FilterConfig DUMMY_FILTER_CONFIG = mock(FilterConfig.class);
    private static final FilterChain DUMMY_FILTER_CHAIN = mock(FilterChain.class);
    private static final HttpServletRequest DUMMY_REQUEST = mock(HttpServletRequest.class);
    private static final HttpServletResponse DUMMY_RESPONSE = mock(HttpServletResponse.class);
    private final FilterAction firstAction = mock(FilterAction.class);
    private final FilterAction secondAction = mock(FilterAction.class);
    private final FilterAction thirdAction = mock(FilterAction.class);
    private final Filter filter = new ActionsFilter(firstAction, secondAction, thirdAction) {
        @Override
        protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
            executeAllFilterActions(request, response);
        }
    };

    @Test
    public void initializesAllActions() throws Exception {
        filter.init(DUMMY_FILTER_CONFIG);
        verify(firstAction).init(DUMMY_FILTER_CONFIG);
        verify(secondAction).init(DUMMY_FILTER_CONFIG);
        verify(thirdAction).init(DUMMY_FILTER_CONFIG);
    }

    @Test
    public void executesAllActions() throws Exception {
        filter.doFilter(DUMMY_REQUEST, DUMMY_RESPONSE, DUMMY_FILTER_CHAIN);
        verify(firstAction).execute(DUMMY_REQUEST, DUMMY_RESPONSE);
        verify(secondAction).execute(DUMMY_REQUEST, DUMMY_RESPONSE);
        verify(thirdAction).execute(DUMMY_REQUEST, DUMMY_RESPONSE);
    }

    @Test
    public void destroysAllActions() throws Exception {
        filter.destroy();
        verify(firstAction).destroy();
        verify(secondAction).destroy();
        verify(thirdAction).destroy();
    }

    @Test
    public void destroysAllActionssAlthoughDestroyingTheFirstFilterThrewAnException() {
        doThrow(new RuntimeException()).when(firstAction).destroy();
        try {
            filter.destroy();
            fail("Expected exception has not been thrown.");
        } catch (RuntimeException expected) {
            verify(firstAction).destroy();
            verify(secondAction).destroy();
            verify(thirdAction).destroy();
        }
    }

    @Test
    public void destroysInitializedActionsIfInitializationOfAnActionFails() throws Exception {
        doThrow(new RuntimeException()).when(thirdAction).init(DUMMY_FILTER_CONFIG);
        try {
            filter.init(DUMMY_FILTER_CONFIG);
            fail("Expected exception has not been thrown.");
        } catch (RuntimeException expected) {
            verify(firstAction).destroy();
            verify(secondAction).destroy();
        }
    }
}
