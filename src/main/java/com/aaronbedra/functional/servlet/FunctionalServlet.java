package com.aaronbedra.functional.servlet;

import com.jnape.palatable.lambda.adt.Either;
import com.jnape.palatable.lambda.adt.Unit;
import com.jnape.palatable.lambda.functions.Fn2;
import com.jnape.palatable.lambda.io.IO;
import com.jnape.palatable.lambda.monad.MonadRec;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.jnape.palatable.lambda.adt.Either.left;
import static com.jnape.palatable.lambda.io.IO.io;
import static javax.servlet.http.HttpServletResponse.SC_METHOD_NOT_ALLOWED;
import static javax.servlet.http.HttpServletResponse.SC_OK;

public final class FunctionalServlet<M extends MonadRec<?, M>, F, A> implements ServletMethods<M, F, A> {
    private final Fn2<HttpServletRequest, HttpServletResponse, MonadRec<Either<F, A>, M>> headFn;
    private final Fn2<HttpServletRequest, HttpServletResponse, MonadRec<Either<F, A>, M>> getFn;
    private final Fn2<HttpServletRequest, HttpServletResponse, MonadRec<Either<F, A>, M>> postFn;
    private final Fn2<HttpServletRequest, HttpServletResponse, MonadRec<Either<F, A>, M>> putFn;
    private final Fn2<HttpServletRequest, HttpServletResponse, MonadRec<Either<F, A>, M>> deleteFn;
    private final Fn2<HttpServletRequest, HttpServletResponse, MonadRec<Either<F, A>, M>> optionsFn;
    private final Fn2<HttpServletRequest, HttpServletResponse, MonadRec<Either<F, A>, M>> traceFn;
    private final Fn2<HttpServletResponse, A, MonadRec<Unit, M>> successFn;
    private final Fn2<HttpServletResponse, F, MonadRec<Unit, M>> failureFn;

    private FunctionalServlet(
            Fn2<HttpServletRequest, HttpServletResponse, MonadRec<Either<F, A>, M>> headFn,
            Fn2<HttpServletRequest, HttpServletResponse, MonadRec<Either<F, A>, M>> getFn,
            Fn2<HttpServletRequest, HttpServletResponse, MonadRec<Either<F, A>, M>> postFn,
            Fn2<HttpServletRequest, HttpServletResponse, MonadRec<Either<F, A>, M>> putFn,
            Fn2<HttpServletRequest, HttpServletResponse, MonadRec<Either<F, A>, M>> deleteFn,
            Fn2<HttpServletRequest, HttpServletResponse, MonadRec<Either<F, A>, M>> optionsFn,
            Fn2<HttpServletRequest, HttpServletResponse, MonadRec<Either<F, A>, M>> traceFn,
            Fn2<HttpServletResponse, A, MonadRec<Unit, M>> successFn,
            Fn2<HttpServletResponse, F, MonadRec<Unit, M>> failureFn) {

        this.headFn = headFn;
        this.getFn = getFn;
        this.postFn = postFn;
        this.putFn = putFn;
        this.deleteFn = deleteFn;
        this.optionsFn = optionsFn;
        this.traceFn = traceFn;
        this.successFn = successFn;
        this.failureFn = failureFn;
    }

    public static <M extends MonadRec<?, M>, F, A> FunctionalServlet<M, F, A> functionalServlet(
            Fn2<HttpServletRequest, HttpServletResponse, MonadRec<Either<F, A>, M>> headFn,
            Fn2<HttpServletRequest, HttpServletResponse, MonadRec<Either<F, A>, M>> getFn,
            Fn2<HttpServletRequest, HttpServletResponse, MonadRec<Either<F, A>, M>> postFn,
            Fn2<HttpServletRequest, HttpServletResponse, MonadRec<Either<F, A>, M>> putFn,
            Fn2<HttpServletRequest, HttpServletResponse, MonadRec<Either<F, A>, M>> deleteFn,
            Fn2<HttpServletRequest, HttpServletResponse, MonadRec<Either<F, A>, M>> optionsFn,
            Fn2<HttpServletRequest, HttpServletResponse, MonadRec<Either<F, A>, M>> traceFn,
            Fn2<HttpServletResponse, A, MonadRec<Unit, M>> successFn,
            Fn2<HttpServletResponse, F, MonadRec<Unit, M>> failureFn) {

        return new FunctionalServlet<>(headFn, getFn, postFn, putFn, deleteFn, optionsFn, traceFn, successFn, failureFn);
    }

    @Override
    public MonadRec<Either<F, A>, M> head(HttpServletRequest request, HttpServletResponse response) {
        return headFn.apply(request, response);
    }

    @Override
    public MonadRec<Either<F, A>, M> get(HttpServletRequest request, HttpServletResponse response) {
        return getFn.apply(request, response);
    }

    @Override
    public MonadRec<Either<F, A>, M> post(HttpServletRequest request, HttpServletResponse response) {
        return postFn.apply(request, response);
    }

    @Override
    public MonadRec<Either<F, A>, M> put(HttpServletRequest request, HttpServletResponse response) {
        return putFn.apply(request, response);
    }

    @Override
    public MonadRec<Either<F, A>, M> delete(HttpServletRequest request, HttpServletResponse response) {
        return deleteFn.apply(request, response);
    }

    @Override
    public MonadRec<Either<F, A>, M> options(HttpServletRequest request, HttpServletResponse response) {
        return optionsFn.apply(request, response);
    }

    @Override
    public MonadRec<Either<F, A>, M> trace(HttpServletRequest request, HttpServletResponse response) {
        return traceFn.apply(request, response);
    }

    @Override
    public MonadRec<Unit, M> succeed(HttpServletResponse response, A payload) {
        return successFn.apply(response, payload);
    }

    @Override
    public MonadRec<Unit, M> fail(HttpServletResponse response, F failure) {
        return failureFn.apply(response, failure);
    }

    public FunctionalServlet<M, F, A> head(Fn2<HttpServletRequest, HttpServletResponse, MonadRec<Either<F, A>, M>> headFn) {
        return functionalServlet(headFn, getFn, postFn, putFn, deleteFn, optionsFn, traceFn, successFn, failureFn);
    }

    public FunctionalServlet<M, F, A> get(Fn2<HttpServletRequest, HttpServletResponse, MonadRec<Either<F, A>, M>> getFn) {
        return functionalServlet(headFn, getFn, postFn, putFn, deleteFn, optionsFn, traceFn, successFn, failureFn);
    }

    public FunctionalServlet<M, F, A> post(Fn2<HttpServletRequest, HttpServletResponse, MonadRec<Either<F, A>, M>> postFn) {
        return functionalServlet(headFn, getFn, postFn, putFn, deleteFn, optionsFn, traceFn, successFn, failureFn);
    }

    public FunctionalServlet<M, F, A> put(Fn2<HttpServletRequest, HttpServletResponse, MonadRec<Either<F, A>, M>> putFn) {
        return functionalServlet(headFn, getFn, postFn, putFn, deleteFn, optionsFn, traceFn, successFn, failureFn);
    }

    public FunctionalServlet<M, F, A> delete(Fn2<HttpServletRequest, HttpServletResponse, MonadRec<Either<F, A>, M>> deleteFn) {
        return functionalServlet(headFn, getFn, postFn, putFn, deleteFn, optionsFn, traceFn, successFn, failureFn);
    }

    public FunctionalServlet<M, F, A> options(Fn2<HttpServletRequest, HttpServletResponse, MonadRec<Either<F, A>, M>> optionsFn) {
        return functionalServlet(headFn, getFn, postFn, putFn, deleteFn, optionsFn, traceFn, successFn, failureFn);
    }

    public FunctionalServlet<M, F, A> trace(Fn2<HttpServletRequest, HttpServletResponse, MonadRec<Either<F, A>, M>> traceFn) {
        return functionalServlet(headFn, getFn, postFn, putFn, deleteFn, optionsFn, traceFn, successFn, failureFn);
    }

    public FunctionalServlet<M, F, A> succeed(Fn2<HttpServletResponse, A, MonadRec<Unit, M>> successFn) {
        return functionalServlet(headFn, getFn, postFn, putFn, deleteFn, optionsFn, traceFn, successFn, failureFn);
    }

    public FunctionalServlet<M, F, A> fail(Fn2<HttpServletResponse, F, MonadRec<Unit, M>> failureFn) {
        return functionalServlet(headFn, getFn, postFn, putFn, deleteFn, optionsFn, traceFn, successFn, failureFn);
    }

    public static <M extends MonadRec<?, M>, F, A> FunctionalServlet<M, F, A> defaultFunctionalServlet(
            Fn2<HttpServletRequest, HttpServletResponse, MonadRec<Either<F, A>, M>> defaultResponse,
            Fn2<HttpServletResponse, A, MonadRec<Unit, M>> successFn,
            Fn2<HttpServletResponse, F, MonadRec<Unit, M>> failureFn) {

        return functionalServlet(
                defaultResponse,
                defaultResponse,
                defaultResponse,
                defaultResponse,
                defaultResponse,
                defaultResponse,
                defaultResponse,
                successFn,
                failureFn);
    }

    public static <A> FunctionalServlet<IO<?>, Integer, A> defaultFunctionalIOServlet() {
        Fn2<HttpServletRequest, HttpServletResponse, MonadRec<Either<Integer, A>, IO<?>>> defaultResponse =
                (req, res) -> io(left(SC_METHOD_NOT_ALLOWED));
        Fn2<HttpServletResponse, Integer, MonadRec<Unit, IO<?>>> failureFn = (res, failure) -> io(() -> res.setStatus(failure));
        Fn2<HttpServletResponse, A, MonadRec<Unit, IO<?>>> successFn = (res, success) -> io(() -> {
            res.setStatus(SC_OK);
            res.setContentType("text/plain");
            res.getWriter().println(success.toString());
        });

        return functionalServlet(
                defaultResponse,
                defaultResponse,
                defaultResponse,
                defaultResponse,
                defaultResponse,
                defaultResponse,
                defaultResponse,
                successFn,
                failureFn);
    }
}
