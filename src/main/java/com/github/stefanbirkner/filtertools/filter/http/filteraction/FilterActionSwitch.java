package com.github.stefanbirkner.filtertools.filter.http.filteraction;

import com.github.stefanbirkner.filtertools.filter.Predicate;

import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Creates a {@link FilterAction} that executes one of two specified actions based on the outcome of a
 * {@link com.github.stefanbirkner.filtertools.filter.Predicate}. The switch is created by an Fluent API.
 * <pre>FilterAction actionSwitch = FilterActionSwitch.execute(new DoSomething())
 *    .when(new APredicate())
 *    .otherwiseExecute(new DoSomethingElse());</pre>
 *
 * @since 1.3.0
 */
public class FilterActionSwitch implements FilterAction {
    /**
     * Start to build a {@code FilterActionSwitch}.
     *
     * @param action the {@link com.github.stefanbirkner.filtertools.filter.http.filteraction.FilterAction} that is
     *               executed if the predicate matches.
     */
    public static FirstActionSpecified execute(FilterAction action) {
        return new FirstActionSpecified(action);
    }

    public static class FirstActionSpecified {
        private FilterAction actionForSuccessfulTest;

        private FirstActionSpecified(FilterAction action) {
            this.actionForSuccessfulTest = notNull(action, "action");
        }

        /**
         * Specify the {@link com.github.stefanbirkner.filtertools.filter.Predicate} of the {@code FilterActionSwitch}.
         *
         * @param predicate the {@link com.github.stefanbirkner.filtertools.filter.Predicate} that is used to decide
         *                  which {@link com.github.stefanbirkner.filtertools.filter.http.filteraction.FilterAction}
         *                  is executed.
         */
        public PredicateSpecified when(Predicate<HttpServletRequest> predicate) {
            return new PredicateSpecified(actionForSuccessfulTest, predicate);
        }
    }

    public static class PredicateSpecified {
        private FilterAction actionForSuccessfulTest;
        private Predicate<HttpServletRequest> predicate;

        private PredicateSpecified(FilterAction actionForSuccessfulTest, Predicate<HttpServletRequest> predicate) {
            this.actionForSuccessfulTest = actionForSuccessfulTest;
            this.predicate = notNull(predicate, "predicate");
        }

        /**
         * Creates the switch.
         *
         * @param action the {@link com.github.stefanbirkner.filtertools.filter.http.filteraction.FilterAction} that is
         *               executed if the predicate does not match.
         * @return the switch.
         */
        public FilterAction otherwiseExecute(FilterAction action) {
            notNull(action, "action");
            return new FilterActionSwitch(actionForSuccessfulTest, action, predicate);
        }
    }

    private static <T> T notNull(T object, String name) {
        if (object == null)
            throw new NullPointerException("The " + name + " is missing");
        else
            return object;
    }

    private final FilterAction actionForSuccessfulTest;
    private final FilterAction actionForFailingTest;
    private final Predicate<HttpServletRequest> predicate;

    private FilterActionSwitch(FilterAction actionForSuccessfulTest, FilterAction actionForFailingTest,
                               Predicate<HttpServletRequest> predicate) {
        this.actionForSuccessfulTest = actionForSuccessfulTest;
        this.actionForFailingTest = actionForFailingTest;
        this.predicate = predicate;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        actionForSuccessfulTest.init(filterConfig);
        actionForFailingTest.init(filterConfig);
    }

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        FilterAction action = predicate.test(request) ? actionForSuccessfulTest : actionForFailingTest;
        action.execute(request, response);
    }

    @Override
    public void destroy() {
        try {
            actionForSuccessfulTest.destroy();
        } finally {
            actionForFailingTest.destroy();
        }
    }
}
