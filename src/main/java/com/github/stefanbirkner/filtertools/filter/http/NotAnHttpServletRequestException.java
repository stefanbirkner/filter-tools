package com.github.stefanbirkner.filtertools.filter.http;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;

/**
 * An {@code NotAnHttpServletRequestException} exception is thrown by a method that handles
 * {@link javax.servlet.http.HttpServletRequest} only, but has been called with another type of
 * {@link javax.servlet.ServletException}.
 *
 * @since 1.1.0
 */
public class NotAnHttpServletRequestException extends ServletException {
    private final ServletRequest request;

    /**
     * Constructs a new {@code NotAnHttpServletRequestException} with an inappropriate request.
     *
     * @param request an inappropriate request.
     */
    public NotAnHttpServletRequestException(ServletRequest request) {
        super("The request " + request + " is not a javax.servlet.http.HttpServletRequest, but a "
            + request.getClass().getName() + ".");
        this.request = request;
    }

    /**
     * Returns the inappropriate request.
     *
     * @return the inappropriate request.
     */
    public ServletRequest getRequest() {
        return request;
    }
}
