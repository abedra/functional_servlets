package com.aaronbedra.functional.servlet.example;

import com.aaronbedra.functional.servlet.FunctionalServlet;
import com.jnape.palatable.lambda.adt.Either;
import com.jnape.palatable.lambda.io.IO;
import org.junit.Before;
import org.junit.Test;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.aaronbedra.functional.servlet.example.GreetingServlet.greetingServlet;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static testsupport.matchers.EitherMatcher.isLeftThat;
import static testsupport.matchers.EitherMatcher.isRightThat;
import static testsupport.matchers.IOMatcher.yieldsValue;

public class GreetingServletTest {
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
}
