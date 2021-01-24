package com.aaronbedra.functional.servlet;

import com.jnape.palatable.lambda.adt.Either;
import com.jnape.palatable.lambda.io.IO;
import org.junit.Before;
import org.junit.Test;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.aaronbedra.functional.servlet.IOServletTest.GreetingServlet.greetingServlet;
import static com.jnape.palatable.lambda.adt.Maybe.maybe;
import static com.jnape.palatable.lambda.io.IO.io;
import static java.lang.String.format;
import static javax.servlet.http.HttpServletResponse.SC_BAD_REQUEST;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static testsupport.matchers.EitherMatcher.isLeftThat;
import static testsupport.matchers.EitherMatcher.isRightThat;
import static testsupport.matchers.IOMatcher.yieldsValue;

public class IOServletTest {
    private FunctionalServlet<IO<?>, Integer, String> greetingServlet;
    private HttpServletRequest request;
    private HttpServletResponse response;

    @Before
    public void before() {
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        greetingServlet = greetingServlet();
    }

    @Test
    public void success() {
        when(request.getParameter("name")).thenReturn("test");
        IO<Either<Integer, String>> result = greetingServlet.get(request, response).coerce();

        assertThat(result, yieldsValue(isRightThat(equalTo("Hello test"))));
    }

    @Test
    public void failure() {
        IO<Either<Integer, String>> result = greetingServlet.get(request, response).coerce();

        assertThat(result, yieldsValue(isLeftThat(equalTo(400))));
    }

    public static class GreetingServlet {
        public static FunctionalServlet<IO<?>, Integer, String> greetingServlet() {
            return FunctionalServlet.<String>defaultFunctionalIOServlet()
                    .get(GreetingServlet::handle);
        }

        private static IO<Either<Integer, String>> handle(HttpServletRequest request, HttpServletResponse response) {
            return io(() -> maybe(request.getParameter("name"))
                    .fmap(name -> format("Hello %s", name))
                    .toEither(() -> SC_BAD_REQUEST));
        }
    }
}
