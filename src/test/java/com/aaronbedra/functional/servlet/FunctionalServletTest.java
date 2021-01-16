package com.aaronbedra.functional.servlet;

import com.jnape.palatable.lambda.adt.Either;
import com.jnape.palatable.lambda.adt.Unit;
import com.jnape.palatable.lambda.functions.Fn2;
import com.jnape.palatable.lambda.functor.builtin.Identity;
import com.jnape.palatable.lambda.monad.MonadRec;
import org.junit.Before;
import org.junit.Test;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.aaronbedra.functional.servlet.FunctionalServlet.functionalServlet;
import static com.jnape.palatable.lambda.adt.Either.left;
import static com.jnape.palatable.lambda.adt.Either.right;
import static com.jnape.palatable.lambda.adt.Unit.UNIT;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static testsupport.matchers.EitherMatcher.isLeftThat;
import static testsupport.matchers.EitherMatcher.isRightThat;

public class FunctionalServletTest {
    private FunctionalServlet<Identity<?>, String, String> succeedingServlet;
    private FunctionalServlet<Identity<?>, String, String> failingServlet;
    private HttpServletRequest request;
    private HttpServletResponse response;

    @Before
    public void before() {
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        succeedingServlet = defaultSucceedingTestServlet();
        failingServlet = defaultFailingTestServlet();
    }

    @Test
    public void headSuccess() {
        Identity<Either<String, String>> head = succeedingServlet.head(request, response).coerce();

        assertThat(head.runIdentity(), isRightThat(equalTo("head")));
    }

    @Test
    public void headFailure() {
        Identity<Either<String, String>> head = failingServlet.head(request, response).coerce();

        assertThat(head.runIdentity(), isLeftThat(equalTo("head failed")));
    }

    @Test
    public void getSuccess() {
        Identity<Either<String, String>> get = succeedingServlet.get(request, response).coerce();

        assertThat(get.runIdentity(), isRightThat(equalTo("get")));
    }

    @Test
    public void getFailure() {
        Identity<Either<String, String>> get = failingServlet.get(request, response).coerce();

        assertThat(get.runIdentity(), isLeftThat(equalTo("get failed")));
    }

    @Test
    public void postSuccess() {
        Identity<Either<String, String>> post = succeedingServlet.post(request, response).coerce();

        assertThat(post.runIdentity(), isRightThat(equalTo("post")));
    }

    @Test
    public void postFailure() {
        Identity<Either<String, String>> post = failingServlet.post(request, response).coerce();

        assertThat(post.runIdentity(), isLeftThat(equalTo("post failed")));
    }

    @Test
    public void putSuccess() {
        Identity<Either<String, String>> put = succeedingServlet.put(request, response).coerce();

        assertThat(put.runIdentity(), isRightThat(equalTo("put")));
    }

    @Test
    public void putFailure() {
        Identity<Either<String, String>> put = failingServlet.put(request, response).coerce();

        assertThat(put.runIdentity(), isLeftThat(equalTo("put failed")));
    }

    @Test
    public void deleteSuccess() {
        Identity<Either<String, String>> delete = succeedingServlet.delete(request, response).coerce();

        assertThat(delete.runIdentity(), isRightThat(equalTo("delete")));
    }

    @Test
    public void deleteFailure() {
        Identity<Either<String, String>> delete = failingServlet.delete(request, response).coerce();

        assertThat(delete.runIdentity(), isLeftThat(equalTo("delete failed")));
    }

    @Test
    public void optionsSuccess() {
        Identity<Either<String, String>> options = succeedingServlet.options(request, response).coerce();

        assertThat(options.runIdentity(), isRightThat(equalTo("options")));
    }

    @Test
    public void optionsFailure() {
        Identity<Either<String, String>> options = failingServlet.options(request, response).coerce();

        assertThat(options.runIdentity(), isLeftThat(equalTo("options failed")));
    }

    @Test
    public void traceSuccess() {
        Identity<Either<String, String>> trace = succeedingServlet.trace(request, response).coerce();

        assertThat(trace.runIdentity(), isRightThat(equalTo("trace")));
    }

    @Test
    public void traceFailure() {
        Identity<Either<String, String>> trace = failingServlet.trace(request, response).coerce();

        assertThat(trace.runIdentity(), isLeftThat(equalTo("trace failed")));
    }

    @Test
    public void succeed() {
        Identity<Unit> success = succeedingServlet.succeed(response, "success").coerce();

        assertThat(success.runIdentity(), equalTo(UNIT));
    }

    @Test
    public void fail() {
        Identity<Unit> failure = succeedingServlet.fail(response, "failure").coerce();

        assertThat(failure.runIdentity(), equalTo(UNIT));
    }

    private FunctionalServlet<Identity<?>, String, String> defaultSucceedingTestServlet() {
        Fn2<HttpServletRequest, HttpServletResponse, MonadRec<Either<String, String>, Identity<?>>> headFn =
                (req, res) -> new Identity<>(right("head"));
        Fn2<HttpServletRequest, HttpServletResponse, MonadRec<Either<String, String>, Identity<?>>> getFn =
                (req, res) -> new Identity<>(right("get"));
        Fn2<HttpServletRequest, HttpServletResponse, MonadRec<Either<String, String>, Identity<?>>> postFn =
                (req, res) -> new Identity<>(right("post"));
        Fn2<HttpServletRequest, HttpServletResponse, MonadRec<Either<String, String>, Identity<?>>> putFn =
                (req, res) -> new Identity<>(right("put"));
        Fn2<HttpServletRequest, HttpServletResponse, MonadRec<Either<String, String>, Identity<?>>> deleteFn =
                (req, res) -> new Identity<>(right("delete"));
        Fn2<HttpServletRequest, HttpServletResponse, MonadRec<Either<String, String>, Identity<?>>> optionsFn =
                (req, res) -> new Identity<>(right("options"));
        Fn2<HttpServletRequest, HttpServletResponse, MonadRec<Either<String, String>, Identity<?>>> traceFn =
                (req, res) -> new Identity<>(right("trace"));

        Fn2<HttpServletResponse, String, MonadRec<Unit, Identity<?>>> successFn =
                (res, success) -> new Identity<>(UNIT);
        Fn2<HttpServletResponse, String, MonadRec<Unit, Identity<?>>> failureFn =
                (res, failure) -> new Identity<>(UNIT);

        return functionalServlet(headFn, getFn, postFn, putFn, deleteFn, optionsFn, traceFn, successFn, failureFn);
    }

    private FunctionalServlet<Identity<?>, String, String> defaultFailingTestServlet() {
        Fn2<HttpServletRequest, HttpServletResponse, MonadRec<Either<String, String>, Identity<?>>> headFn =
                (req, res) -> new Identity<>(left("head failed"));
        Fn2<HttpServletRequest, HttpServletResponse, MonadRec<Either<String, String>, Identity<?>>> getFn =
                (req, res) -> new Identity<>(left("get failed"));
        Fn2<HttpServletRequest, HttpServletResponse, MonadRec<Either<String, String>, Identity<?>>> postFn =
                (req, res) -> new Identity<>(left("post failed"));
        Fn2<HttpServletRequest, HttpServletResponse, MonadRec<Either<String, String>, Identity<?>>> putFn =
                (req, res) -> new Identity<>(left("put failed"));
        Fn2<HttpServletRequest, HttpServletResponse, MonadRec<Either<String, String>, Identity<?>>> deleteFn =
                (req, res) -> new Identity<>(left("delete failed"));
        Fn2<HttpServletRequest, HttpServletResponse, MonadRec<Either<String, String>, Identity<?>>> optionsFn =
                (req, res) -> new Identity<>(left("options failed"));
        Fn2<HttpServletRequest, HttpServletResponse, MonadRec<Either<String, String>, Identity<?>>> traceFn =
                (req, res) -> new Identity<>(left("trace failed"));

        Fn2<HttpServletResponse, String, MonadRec<Unit, Identity<?>>> successFn =
                (res, success) -> new Identity<>(UNIT);
        Fn2<HttpServletResponse, String, MonadRec<Unit, Identity<?>>> failureFn =
                (res, failure) -> new Identity<>(UNIT);

        return functionalServlet(headFn, getFn, postFn, putFn, deleteFn, optionsFn, traceFn, successFn, failureFn);
    }
}
