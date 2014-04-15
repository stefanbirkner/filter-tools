package com.github.stefanbirkner.filtertools.filter.http;

import javax.servlet.ServletException;
import javax.servlet.ServletResponse;

/**
 * An {@code NotAnHttpServletResponseException} exception is thrown by a method that handles
 * {@link javax.servlet.http.HttpServletResponse} only, but has been called with another type of
 * {@link javax.servlet.ServletResponse}.
 *
 * @since 1.1.0
 */
public class NotAnHttpServletResponseException extends ServletException {
    private final ServletResponse response;

    /**
     * Constructs a new {@code NotAnHttpServletResponseException} with an inappropriate response.
     *
     * @param response an inappropriate response.
     */
    public NotAnHttpServletResponseException(ServletResponse response) {
        super("The response " + response + " is not a javax.servlet.http.HttpServletResponse, but a "
                + response.getClass().getName() + ".");
        this.response = response;
    }

    /**
     * Returns the inappropriate response.
     *
     * @return the inappropriate response.
     */
    public ServletResponse getResponse() {
        return response;
    }
}
