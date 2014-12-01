package com.github.stefanbirkner.filtertools.filter;

import com.github.stefanbirkner.filtertools.filterchain.FilterWithFilterChain;

import javax.servlet.*;
import java.io.IOException;

import static java.util.Arrays.copyOfRange;

/**
 * Encase multiple filters into a single filter.
 *
 * <p>This allows you to include multiple filters into your web.xml by including a single filter.
 * You can provide recommended filter combinations as a single filter, too.
 *
 * <h3>Example</h3>
 * <p>The filter</p>
 * <pre>
 * public class MyEncasedFilters extends EncasedFilters {
 *   public MyEncasedFilters() {
 *     super(MyFirstFilter(), MySecondFilter());
 *   }
 * }
 * </pre>
 * <pre>
 * &lt;filter&gt;
 *   &lt;filter-name&gt;MyEncasedFilters&lt;/filter-name&gt;
 *   &lt;filter-class&gt;my.package.MyEncasedFilters&lt;/filter-class&gt;
 * &lt;/filter&gt;
 * &lt;filter-mapping&gt;
 *   &lt;filter-name&gt;MyEncasedFilters&lt;/filter-name&gt;
 *   &lt;url-pattern&gt;/*&lt;/url-pattern&gt;
 * &lt;/filter-mapping&gt;
 * </pre>
 *
 * <p>is a replacement for</p>
 * <pre>
 * &lt;filter&gt;
 *   &lt;filter-name&gt;MyFirstFilter&lt;/filter-name&gt;
 *   &lt;filter-class&gt;my.package.MyFirstFilter&lt;/filter-class&gt;
 * &lt;/filter&gt;
 * &lt;filter&gt;
 *   &lt;filter-name&gt;MySecondFilter&lt;/filter-name&gt;
 *   &lt;filter-class&gt;my.package.MySecondFilter&lt;/filter-class&gt;
 * &lt;/filter&gt;
 * &lt;filter-mapping&gt;
 *   &lt;filter-name&gt;MyFirstFilter&lt;/filter-name&gt;
 *   &lt;url-pattern&gt;/*&lt;/url-pattern&gt;
 * &lt;/filter-mapping&gt;
 * &lt;filter-mapping&gt;
 *   &lt;filter-name&gt;MySecondFilter&lt;/filter-name&gt;
 *   &lt;url-pattern&gt;/*&lt;/url-pattern&gt;
 * &lt;/filter-mapping&gt;
 * </pre>
 */
public class EncasedFilters implements Filter {
    private static final Filter NO_OP_FILTER = new NoOpFilter();
    private final Filter firstFilter;
    private final Filter secondFilter;

    public EncasedFilters(Filter... filters) {
        this.firstFilter = (filters.length == 0) ? NO_OP_FILTER : filters[0];
        this.secondFilter = (filters.length > 1) ? new EncasedFilters(rest(filters)) : NO_OP_FILTER;
    }

    private Filter[] rest(Filter[] filters) {
        return copyOfRange(filters, 1, filters.length);
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        firstFilter.init(filterConfig);
        secondFilter.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         final FilterChain filterChain)
        throws IOException, ServletException {
        FilterChain secondFilterWithFilterChain = new FilterWithFilterChain(
            secondFilter, filterChain);
        firstFilter.doFilter(request, response, secondFilterWithFilterChain);
    }

    @Override
    public void destroy() {
        try {
            firstFilter.destroy();
        } finally {
            secondFilter.destroy();
        }
    }
}
