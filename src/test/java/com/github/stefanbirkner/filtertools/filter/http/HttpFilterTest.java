package com.github.stefanbirkner.filtertools.filter.http;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.rules.ExpectedException.none;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyZeroInteractions;

public class HttpFilterTest {
    private final ServletRequest request = mock(HttpServletRequest.class);
    private final ServletResponse response = mock(HttpServletResponse.class);
    private final FilterChain filterChain = mock(FilterChain.class);
    private final TestHttpFilter filter = new TestHttpFilter();

    @Rule
    public final ExpectedException thrown = none();

    @Test
    public void callsDoFilterWithProvidedRequest() throws Exception {
        filter.doFilter(request, response, filterChain);
        assertThat(filter.request, is(sameInstance(request)));
    }

    @Test
    public void callsDoFilterWithProvidedResponse() throws Exception {
        filter.doFilter(request, response, filterChain);
        assertThat(filter.response, is(sameInstance(response)));
    }

    @Test
    public void callsDoFilterWithProvidedFilterChain() throws Exception {
        filter.doFilter(request, response, filterChain);
        assertThat(filter.filterChain, is(sameInstance(filterChain)));
    }

    @Test
    public void onlyHandsOverParamters() throws Exception {
        filter.doFilter(request, response, filterChain);
        verifyZeroInteractions(request, response, filterChain);
    }

    @Test
    public void cannotBeCalledWithAnArbitraryServletRequest() throws Exception {
        NotAnHttpServletRequest inappropriateRequest = new NotAnHttpServletRequest();
        thrown.expect(NotAnHttpServletRequestException.class);
        thrown.expect(hasProperty("request", sameInstance(inappropriateRequest)));
        filter.doFilter(inappropriateRequest, response, filterChain);
    }

    @Test
    public void cannotBeCalledWithAnArbitraryServletResponse() throws Exception {
        NotAnHttpServletResponse inappropriateResponse = new NotAnHttpServletResponse();
        thrown.expect(NotAnHttpServletResponseException.class);
        thrown.expect(hasProperty("response", sameInstance(inappropriateResponse)));
        filter.doFilter(request, inappropriateResponse, filterChain);
    }

    private static class TestHttpFilter extends HttpFilter {
        private HttpServletRequest request;
        private HttpServletResponse response;
        private FilterChain filterChain;

        @Override
        protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
                throws IOException, ServletException {

            this.request = request;
            this.response = response;
            this.filterChain = filterChain;
        }
    }

    private static class NotAnHttpServletRequest implements ServletRequest {

        @Override
        public Object getAttribute(String name) {
            return null;
        }

        @Override
        public Enumeration getAttributeNames() {
            return null;
        }

        @Override
        public String getCharacterEncoding() {
            return null;
        }

        @Override
        public void setCharacterEncoding(String env) throws UnsupportedEncodingException {

        }

        @Override
        public int getContentLength() {
            return 0;
        }

        @Override
        public String getContentType() {
            return null;
        }

        @Override
        public ServletInputStream getInputStream() throws IOException {
            return null;
        }

        @Override
        public String getParameter(String name) {
            return null;
        }

        @Override
        public Enumeration getParameterNames() {
            return null;
        }

        @Override
        public String[] getParameterValues(String name) {
            return new String[0];
        }

        @Override
        public Map getParameterMap() {
            return null;
        }

        @Override
        public String getProtocol() {
            return null;
        }

        @Override
        public String getScheme() {
            return null;
        }

        @Override
        public String getServerName() {
            return null;
        }

        @Override
        public int getServerPort() {
            return 0;
        }

        @Override
        public BufferedReader getReader() throws IOException {
            return null;
        }

        @Override
        public String getRemoteAddr() {
            return null;
        }

        @Override
        public String getRemoteHost() {
            return null;
        }

        @Override
        public void setAttribute(String name, Object o) {

        }

        @Override
        public void removeAttribute(String name) {

        }

        @Override
        public Locale getLocale() {
            return null;
        }

        @Override
        public Enumeration getLocales() {
            return null;
        }

        @Override
        public boolean isSecure() {
            return false;
        }

        @Override
        public RequestDispatcher getRequestDispatcher(String path) {
            return null;
        }

        @Override
        public String getRealPath(String path) {
            return null;
        }

        @Override
        public int getRemotePort() {
            return 0;
        }

        @Override
        public String getLocalName() {
            return null;
        }

        @Override
        public String getLocalAddr() {
            return null;
        }

        @Override
        public int getLocalPort() {
            return 0;
        }
    }

    private static class NotAnHttpServletResponse implements ServletResponse {

        @Override
        public String getCharacterEncoding() {
            return null;
        }

        @Override
        public String getContentType() {
            return null;
        }

        @Override
        public ServletOutputStream getOutputStream() throws IOException {
            return null;
        }

        @Override
        public PrintWriter getWriter() throws IOException {
            return null;
        }

        @Override
        public void setCharacterEncoding(String charset) {

        }

        @Override
        public void setContentLength(int len) {

        }

        @Override
        public void setContentType(String type) {

        }

        @Override
        public void setBufferSize(int size) {

        }

        @Override
        public int getBufferSize() {
            return 0;
        }

        @Override
        public void flushBuffer() throws IOException {

        }

        @Override
        public void resetBuffer() {

        }

        @Override
        public boolean isCommitted() {
            return false;
        }

        @Override
        public void reset() {

        }

        @Override
        public void setLocale(Locale loc) {

        }

        @Override
        public Locale getLocale() {
            return null;
        }
    }
}
