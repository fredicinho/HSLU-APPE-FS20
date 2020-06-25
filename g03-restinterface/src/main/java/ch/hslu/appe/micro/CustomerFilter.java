package ch.hslu.appe.micro;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.MutableHttpResponse;
import io.micronaut.http.annotation.Filter;
import io.micronaut.http.filter.HttpServerFilter;
import io.micronaut.http.filter.ServerFilterChain;
import org.reactivestreams.Publisher;

@Filter("/*")
public class CustomerFilter implements HttpServerFilter {

    /**
     * A Filter.
     * @param request A request.
     * @param chain A chain.
     * @return
     */
    @Override
    public Publisher<MutableHttpResponse<?>> doFilter(final HttpRequest<?> request, final ServerFilterChain chain) {
        return chain.proceed(request);
    }
}
