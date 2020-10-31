package com.aaronbedra.functional.servlet.example;

import com.aaronbedra.functional.servlet.FunctionalServlet;
import com.jnape.palatable.lambda.adt.Either;
import com.jnape.palatable.lambda.functions.Fn2;
import com.jnape.palatable.lambda.io.IO;
import com.jnape.palatable.lambda.monad.MonadRec;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.aaronbedra.functional.servlet.FunctionalServlet.defaultFunctionalIOServlet;
import static com.jnape.palatable.lambda.adt.Maybe.maybe;
import static com.jnape.palatable.lambda.io.IO.io;
import static java.lang.String.format;
import static javax.servlet.http.HttpServletResponse.SC_BAD_REQUEST;

public class GreetingServlet {
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
