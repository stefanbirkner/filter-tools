package com.github.stefanbirkner.filtertools.filter;

import org.hamcrest.Matcher;

import javax.servlet.*;
import java.io.IOException;

/**
 * Combine a filter and a {@link Predicate} or Hamcrest {@link org.hamcrest.Matcher}. The filter is
 * executed if the predicate or the matchers matches and skipped otherwise.
 *
 * <h3>Example</h3>
 * <p>Define a predicate and wrap the base filter.</p>
 * <pre>
 * public class FrenchSomethingFilter extends OptionalFilter {
 *   public FrenchSomethingFilter() {
 *     super(new HasLocale(Locale.FRENCH), new SomethingFilter());
 *   }
 * }
 * </pre>
 * <p>Add the filter to your web.xml.</p>
 * <pre>
 * &lt;filter&gt;
 *   &lt;filter-name&gt;FrenchSomethingFilter&lt;/filter-name&gt;
 *   &lt;filter-class&gt;your.package.FrenchSomethingFilter&lt;/filter-class&gt;
 * &lt;/filter&gt;
 * &lt;filter-mapping&gt;
 *   &lt;filter-name&gt;FrenchSomethingFilter&lt;/filter-name&gt;
 *   &lt;url-pattern&gt;/*&lt;/url-pattern&gt;
 * &lt;/filter-mapping&gt;
 * </pre>
 * <p>You can provide init-params that will be dispatched to the base filter.</p>
 *
 * <h3>Lambda Support</h3>
 * <p>You can use Lambda expressions as predicates if you're using Java 8.</p>
 * <pre>
 * public class FrenchSomethingFilter extends OptionalFilter {
 *   public FrenchSomethingFilter() {
 *     super(
 *       (request) -&gt; request.getLocale().equals(Locale.FRENCH),
 *       new SomethingFilter());
 *   }
 * }
 * </pre>
 *
 * <h3>Hamcrest Support</h3>
 * <p>You can use Hamcrest {@link org.hamcrest.Matcher}s as predicates.</p>
 * <pre>
 * public class FrenchSomethingFilter extends OptionalFilter {
 *   public FrenchSomethingFilter() {
 *     super(
 *       hasProperty("locale", equalTo(Locale.FRENCH)),
 *       new SomethingFilter());
 *   }
 * }
 * </pre>
 */
public class OptionalFilter implements Filter {
    private final Predicate<? extends ServletRequest> predicate;
    private final Filter baseFilter;

    public OptionalFilter(Predicate<? extends ServletRequest> predicate, Filter baseFilter) {
        if (predicate == null)
            throw new NullPointerException("The predicate is missing.");
        this.predicate = predicate;
        if (baseFilter == null)
            throw new NullPointerException("The base filter is missing.");
        this.baseFilter = baseFilter;
    }

    public OptionalFilter(Matcher<? extends ServletRequest> matcher, Filter baseFilter) {
        this(createPredicateForMatcher(matcher), baseFilter);
    }

    private static Predicate<ServletRequest> createPredicateForMatcher(
        final Matcher<? extends ServletRequest> matcher) {
        if (matcher == null)
            throw new NullPointerException("The matcher is missing.");
        return new Predicate<ServletRequest>() {
            @Override
            public boolean test(ServletRequest request) {
                return matcher.matches(request);
            }
        };
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        baseFilter.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
        throws IOException, ServletException {
        if (predicate.test(request))
            baseFilter.doFilter(request, response, filterChain);
        else
            filterChain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        baseFilter.destroy();
    }
}
