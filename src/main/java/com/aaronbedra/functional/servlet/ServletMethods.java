package com.aaronbedra.functional.servlet;

import com.jnape.palatable.lambda.adt.Either;
import com.jnape.palatable.lambda.adt.Unit;
import com.jnape.palatable.lambda.monad.MonadRec;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface ServletMethods<M extends MonadRec<?, M>, F, A> {
    MonadRec<Either<F, A>, M> head(HttpServletRequest request, HttpServletResponse response);

    MonadRec<Either<F, A>, M> get(HttpServletRequest request, HttpServletResponse response);

    MonadRec<Either<F, A>, M> post(HttpServletRequest request, HttpServletResponse response);

    MonadRec<Either<F, A>, M> put(HttpServletRequest request, HttpServletResponse response);

    MonadRec<Either<F, A>, M> delete(HttpServletRequest request, HttpServletResponse response);

    MonadRec<Either<F, A>, M> options(HttpServletRequest request, HttpServletResponse response);

    MonadRec<Either<F, A>, M> trace(HttpServletRequest request, HttpServletResponse response);

    MonadRec<Unit, M> succeed(HttpServletResponse response, A payload);

    MonadRec<Unit, M> fail(HttpServletResponse response, F failure);
}
