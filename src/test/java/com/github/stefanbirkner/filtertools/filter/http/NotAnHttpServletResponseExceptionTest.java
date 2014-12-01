package com.github.stefanbirkner.filtertools.filter.http;

import org.junit.Test;

import javax.servlet.ServletOutputStream;
import javax.servlet.ServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Locale;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasProperty;

public class NotAnHttpServletResponseExceptionTest {
    @Test
    public void hasMeaningfulMessage() {
        NotAnHttpServletResponseException e = new NotAnHttpServletResponseException(new TestServletResponse());
        assertThat(e, hasProperty("message",
            equalTo("The response dummy TestServletResponse is not a javax.servlet.http.HttpServletResponse, but a com.github.stefanbirkner.filtertools.filter.http.NotAnHttpServletResponseExceptionTest$TestServletResponse.")));
    }

    private static class TestServletResponse implements ServletResponse {
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

        @Override
        public String toString() {
            return "dummy TestServletResponse";
        }
    }
}
