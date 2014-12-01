package com.github.stefanbirkner.filtertools.filter.http;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.IOException;

/**
 * Disables the methods
 * {@link javax.servlet.http.HttpServletResponse#encodeURL(String)} and
 * {@link javax.servlet.http.HttpServletResponse#encodeRedirectURL(String)}
 * (as well as the deprecated methods
 * {@link javax.servlet.http.HttpServletResponse#encodeUrl(String)} and
 * {@link javax.servlet.http.HttpServletResponse#encodeRedirectUrl(String)}.
 * These methods will return the URL unmodified if the
 * {@code DisableEncodeUrlFilter} is active.
 * <p><em>Caution!</em> This filter disables session support for clients
 * that don't accept cookies, because the session id is no longer
 * added to URLs.
 */
public class DisableEncodeUrlFilter extends HttpFilter {
    @Override
    protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
        throws IOException, ServletException {
        DisableEncodeUrlResponse disableEncodeUrlResponse = new DisableEncodeUrlResponse(response);
        filterChain.doFilter(request, disableEncodeUrlResponse);
    }

    private static class DisableEncodeUrlResponse extends HttpServletResponseWrapper {
        public DisableEncodeUrlResponse(HttpServletResponse response) {
            super(response);
        }

        @Override
        public String encodeURL(String url) {
            return url;
        }

        @Override
        public String encodeRedirectURL(String url) {
            return url;
        }

        @Override
        public String encodeUrl(String url) {
            return url;
        }

        @Override
        public String encodeRedirectUrl(String url) {
            return url;
        }
    }
}
