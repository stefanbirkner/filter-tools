package com.github.stefanbirkner.filtertools.filter.http;

import org.junit.Test;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DisableEncodeUrlFilterTest {
    private static final HttpServletRequest request = mock(HttpServletRequest.class);
    private static final HttpServletResponse response = mock(HttpServletResponse.class);
    private final Filter filter = new DisableEncodeUrlFilter();

    @Test
    @SuppressWarnings("deprecation")
    public void disablesEncodeUrl() throws Exception {
        when(response.encodeUrl(anyString())).thenReturn("encoded url");
        assertThatUrlIsNotEncoded(new ResponseAction() {
            @Override
            public String callMethodOfResponse(HttpServletResponse response) {
                return response.encodeUrl("not encoded url");
            }
        });
    }

    @Test
    public void disablesEncodeURL() throws Exception {
        when(response.encodeURL(anyString())).thenReturn("encoded url");
        assertThatUrlIsNotEncoded(new ResponseAction() {
            @Override
            public String callMethodOfResponse(HttpServletResponse response) {
                return response.encodeURL("not encoded url");
            }
        });
    }

    @Test
    @SuppressWarnings("deprecation")
    public void disablesEncodeRedirectUrl() throws Exception {
        when(response.encodeRedirectUrl(anyString())).thenReturn("encoded url");
        assertThatUrlIsNotEncoded(new ResponseAction() {
            @Override
            public String callMethodOfResponse(HttpServletResponse response) {
                return response.encodeRedirectUrl("not encoded url");
            }
        });
    }

    @Test
    @SuppressWarnings("deprecation")
    public void disablesEncodeRedirectURL() throws Exception {
        when(response.encodeRedirectURL(anyString())).thenReturn("encoded url");
        assertThatUrlIsNotEncoded(new ResponseAction() {
            @Override
            public String callMethodOfResponse(HttpServletResponse response) {
                return response.encodeRedirectURL("not encoded url");
            }
        });
    }

    private void assertThatUrlIsNotEncoded(final ResponseAction action)
        throws IOException, ServletException {
        filter.doFilter(request, response, new FilterChain() {
            @Override
            public void doFilter(ServletRequest request, ServletResponse response)
                throws IOException, ServletException {
                String url = action.callMethodOfResponse((HttpServletResponse) response);
                assertThat(url, is(equalTo("not encoded url")));
            }
        });
    }

    private static interface ResponseAction {
        String callMethodOfResponse(HttpServletResponse response);
    }
}
