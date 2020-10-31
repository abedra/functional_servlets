package com.aaronbedra.functional.servlet;

import com.jnape.palatable.lambda.adt.Either;
import com.jnape.palatable.lambda.adt.Unit;
import com.jnape.palatable.lambda.monad.MonadRec;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public final class ServletAdapter<M extends MonadRec<?, M>, F, A> extends HttpServlet {
    private final ServletMethods<M, F, A> delegate;

    private ServletAdapter(ServletMethods<M, F, A> delegate) {
        this.delegate = delegate;
    }

    public static <M extends MonadRec<?, M>, F, A> ServletAdapter<M, F, A> servletAdapter(ServletMethods<M, F, A> delegate) {
        return new ServletAdapter<>(delegate);
    }

    @Override
    protected void doHead(HttpServletRequest req, HttpServletResponse resp) {
        delegate.head(req, resp).flatMap(eitherFailureSuccess -> respond(resp, eitherFailureSuccess));
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        delegate.get(req, resp).flatMap(eitherFailureSuccess -> respond(resp, eitherFailureSuccess));
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        delegate.post(req, resp).flatMap(eitherFailureSuccess -> respond(resp, eitherFailureSuccess));
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) {
        delegate.put(req, resp).flatMap(eitherFailureSuccess -> respond(resp, eitherFailureSuccess));
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) {
        delegate.delete(req, resp).flatMap(eitherFailureSuccess -> respond(resp, eitherFailureSuccess));
    }

    @Override
    protected void doOptions(HttpServletRequest req, HttpServletResponse resp) {
        delegate.options(req, resp).flatMap(eitherFailureSuccess -> respond(resp, eitherFailureSuccess));
    }

    @Override
    protected void doTrace(HttpServletRequest req, HttpServletResponse resp) {
        delegate.trace(req, resp).flatMap(eitherFailureSuccess -> respond(resp, eitherFailureSuccess));
    }

    private MonadRec<Unit, M> respond(HttpServletResponse response, Either<F, A> result) {
        return result
                .fmap(success -> delegate.succeed(response, success))
                .recover(failure -> delegate.fail(response, failure));
    }
}
