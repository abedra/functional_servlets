package com.aaronbedra.functional.servlet;

import com.jnape.palatable.lambda.adt.Either;
import com.jnape.palatable.lambda.adt.Unit;
import com.jnape.palatable.lambda.adt.hlist.Tuple2;
import com.jnape.palatable.lambda.adt.hmap.HMap;
import com.jnape.palatable.lambda.adt.hmap.TypeSafeKey;
import com.jnape.palatable.lambda.functions.Fn2;
import com.jnape.palatable.lambda.functor.builtin.Writer;
import com.jnape.palatable.lambda.monad.MonadRec;
import com.jnape.palatable.lambda.monoid.Monoid;
import com.jnape.palatable.shoki.impl.StrictQueue;
import org.junit.Before;
import org.junit.Test;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.aaronbedra.functional.servlet.FunctionalServlet.functionalServlet;
import static com.jnape.palatable.lambda.adt.Either.left;
import static com.jnape.palatable.lambda.adt.Either.right;
import static com.jnape.palatable.lambda.adt.Unit.UNIT;
import static com.jnape.palatable.lambda.adt.hmap.HMap.singletonHMap;
import static com.jnape.palatable.lambda.adt.hmap.TypeSafeKey.typeSafeKey;
import static com.jnape.palatable.lambda.functions.builtin.fn1.Constantly.constantly;
import static com.jnape.palatable.lambda.functor.builtin.Writer.tell;
import static com.jnape.palatable.lambda.monoid.builtin.MergeHMaps.mergeHMaps;
import static com.jnape.palatable.shoki.impl.StrictQueue.strictQueue;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static testsupport.matchers.EitherMatcher.isLeftThat;
import static testsupport.matchers.EitherMatcher.isRightThat;

public class FunctionalServletTest {
    private static final TypeSafeKey.Simple<Either<StrictQueue<String>, String>> headInteraction = typeSafeKey();
    private static final TypeSafeKey.Simple<Either<StrictQueue<String>, String>> getInteraction = typeSafeKey();
    private static final TypeSafeKey.Simple<Either<StrictQueue<String>, String>> postInteraction = typeSafeKey();
    private static final TypeSafeKey.Simple<Either<StrictQueue<String>, String>> putInteraction = typeSafeKey();
    private static final TypeSafeKey.Simple<Either<StrictQueue<String>, String>> deleteInteraction = typeSafeKey();
    private static final TypeSafeKey.Simple<Either<StrictQueue<String>, String>> optionsInteraction = typeSafeKey();
    private static final TypeSafeKey.Simple<Either<StrictQueue<String>, String>> traceInteraction = typeSafeKey();
    private static final TypeSafeKey.Simple<String> successInteraction = typeSafeKey();
    private static final TypeSafeKey.Simple<StrictQueue<String>> failureInteraction = typeSafeKey();

    private FunctionalServlet<Writer<HMap, ?>, StrictQueue<String>, String> succeedingServlet;
    private FunctionalServlet<Writer<HMap, ?>, StrictQueue<String>, String> failingServlet;
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
        Writer<HMap, Either<StrictQueue<String>, String>> result = succeedingServlet.head(request, response).coerce();
        Tuple2<Either<StrictQueue<String>, String>, HMap> actual = result.runWriter(MERGE_RESULTS);

        assertThat(actual._1(), isRightThat(equalTo("head")));
        assertThat(actual._2(), equalTo(singletonHMap(headInteraction, right("head"))));
    }

    @Test
    public void headFailure() {
        Writer<HMap, Either<StrictQueue<String>, String>> result = failingServlet.head(request, response).coerce();
        Tuple2<Either<StrictQueue<String>, String>, HMap> actual = result.runWriter(MERGE_RESULTS);

        assertThat(actual._1(), isLeftThat(equalTo(strictQueue("head failed"))));
        assertThat(actual._2(), equalTo(singletonHMap(headInteraction, left(strictQueue("head failed")))));
    }

    @Test
    public void getSuccess() {
        Writer<HMap, Either<StrictQueue<String>, String>> result = succeedingServlet.get(request, response).coerce();
        Tuple2<Either<StrictQueue<String>, String>, HMap> actual = result.runWriter(MERGE_RESULTS);

        assertThat(actual._1(), isRightThat(equalTo("get")));
        assertThat(actual._2(), equalTo(singletonHMap(getInteraction, right("get"))));
    }

    @Test
    public void getFailure() {
        Writer<HMap, Either<StrictQueue<String>, String>> result = failingServlet.get(request, response).coerce();
        Tuple2<Either<StrictQueue<String>, String>, HMap> actual = result.runWriter(MERGE_RESULTS);

        assertThat(actual._1(), isLeftThat(equalTo(strictQueue("get failed"))));
        assertThat(actual._2(), equalTo(singletonHMap(getInteraction, left(strictQueue("get failed")))));
    }

    @Test
    public void postSuccess() {
        Writer<HMap, Either<StrictQueue<String>, String>> result = succeedingServlet.post(request, response).coerce();
        Tuple2<Either<StrictQueue<String>, String>, HMap> actual = result.runWriter(MERGE_RESULTS);

        assertThat(actual._1(), isRightThat(equalTo("post")));
        assertThat(actual._2(), equalTo(singletonHMap(postInteraction, right("post"))));
    }

    @Test
    public void postFailure() {
        Writer<HMap, Either<StrictQueue<String>, String>> result = failingServlet.post(request, response).coerce();
        Tuple2<Either<StrictQueue<String>, String>, HMap> actual = result.runWriter(MERGE_RESULTS);

        assertThat(actual._1(), isLeftThat(equalTo(strictQueue("post failed"))));
        assertThat(actual._2(), equalTo(singletonHMap(postInteraction, left(strictQueue("post failed")))));
    }

    @Test
    public void putSuccess() {
        Writer<HMap, Either<StrictQueue<String>, String>> result = succeedingServlet.put(request, response).coerce();
        Tuple2<Either<StrictQueue<String>, String>, HMap> actual = result.runWriter(MERGE_RESULTS);

        assertThat(actual._1(), isRightThat(equalTo("put")));
        assertThat(actual._2(), equalTo(singletonHMap(putInteraction, right("put"))));
    }

    @Test
    public void putFailure() {
        Writer<HMap, Either<StrictQueue<String>, String>> result = failingServlet.put(request, response).coerce();
        Tuple2<Either<StrictQueue<String>, String>, HMap> actual = result.runWriter(MERGE_RESULTS);

        assertThat(actual._1(), isLeftThat(equalTo(strictQueue("put failed"))));
        assertThat(actual._2(), equalTo(singletonHMap(putInteraction, left(strictQueue("put failed")))));
    }

    @Test
    public void deleteSuccess() {
        Writer<HMap, Either<StrictQueue<String>, String>> result = succeedingServlet.delete(request, response).coerce();
        Tuple2<Either<StrictQueue<String>, String>, HMap> actual = result.runWriter(MERGE_RESULTS);

        assertThat(actual._1(), isRightThat(equalTo("delete")));
        assertThat(actual._2(), equalTo(singletonHMap(deleteInteraction, right("delete"))));
    }

    @Test
    public void deleteFailure() {
        Writer<HMap, Either<StrictQueue<String>, String>> result = failingServlet.delete(request, response).coerce();
        Tuple2<Either<StrictQueue<String>, String>, HMap> actual = result.runWriter(MERGE_RESULTS);

        assertThat(actual._1(), isLeftThat(equalTo(strictQueue("delete failed"))));
        assertThat(actual._2(), equalTo(singletonHMap(deleteInteraction, left(strictQueue("delete failed")))));
    }

    @Test
    public void optionsSuccess() {
        Writer<HMap, Either<StrictQueue<String>, String>> result = succeedingServlet.options(request, response).coerce();
        Tuple2<Either<StrictQueue<String>, String>, HMap> actual = result.runWriter(MERGE_RESULTS);

        assertThat(actual._1(), isRightThat(equalTo("options")));
        assertThat(actual._2(), equalTo(singletonHMap(optionsInteraction, right("options"))));
    }

    @Test
    public void optionsFailure() {
        Writer<HMap, Either<StrictQueue<String>, String>> result = failingServlet.options(request, response).coerce();
        Tuple2<Either<StrictQueue<String>, String>, HMap> actual = result.runWriter(MERGE_RESULTS);

        assertThat(actual._1(), isLeftThat(equalTo(strictQueue("options failed"))));
        assertThat(actual._2(), equalTo(singletonHMap(optionsInteraction, left(strictQueue("options failed")))));
    }

    @Test
    public void traceSuccess() {
        Writer<HMap, Either<StrictQueue<String>, String>> result = succeedingServlet.trace(request, response).coerce();
        Tuple2<Either<StrictQueue<String>, String>, HMap> actual = result.runWriter(MERGE_RESULTS);

        assertThat(actual._1(), isRightThat(equalTo("trace")));
        assertThat(actual._2(), equalTo(singletonHMap(traceInteraction, right("trace"))));
    }

    @Test
    public void traceFailure() {
        Writer<HMap, Either<StrictQueue<String>, String>> result = failingServlet.trace(request, response).coerce();
        Tuple2<Either<StrictQueue<String>, String>, HMap> actual = result.runWriter(MERGE_RESULTS);

        assertThat(actual._1(), isLeftThat(equalTo(strictQueue("trace failed"))));
        assertThat(actual._2(), equalTo(singletonHMap(traceInteraction, left(strictQueue("trace failed")))));
    }

    @Test
    public void succeed() {
        Writer<HMap, Unit> result = succeedingServlet.succeed(response, "success").coerce();
        Tuple2<Unit, HMap> actual = result.runWriter(MERGE_RESULTS);

        assertThat(actual._1(), equalTo(UNIT));
        assertThat(actual._2(), equalTo(singletonHMap(successInteraction, "success")));
    }

    @Test
    public void fail() {
        Writer<HMap, Unit> result = succeedingServlet.fail(response, strictQueue("failure")).coerce();
        Tuple2<Unit, HMap> actual = result.runWriter(MERGE_RESULTS);

        assertThat(actual._1(), equalTo(UNIT));
        assertThat(actual._2(), equalTo(singletonHMap(failureInteraction, strictQueue("failure"))));
    }

    private FunctionalServlet<Writer<HMap, ?>, StrictQueue<String>, String> defaultSucceedingTestServlet() {
        Fn2<HttpServletRequest, HttpServletResponse, MonadRec<Either<StrictQueue<String>, String>, Writer<HMap, ?>>> headFn =
                (req, res) -> tell(singletonHMap(headInteraction, right("head")))
                        .fmap(constantly(Either.<StrictQueue<String>, String>right("head")));
        Fn2<HttpServletRequest, HttpServletResponse, MonadRec<Either<StrictQueue<String>, String>, Writer<HMap, ?>>> getFn =
                (req, res) -> tell(singletonHMap(getInteraction, right("get")))
                        .fmap(constantly(Either.<StrictQueue<String>, String>right("get")));
        Fn2<HttpServletRequest, HttpServletResponse, MonadRec<Either<StrictQueue<String>, String>, Writer<HMap, ?>>> postFn =
                (req, res) -> tell(singletonHMap(postInteraction, right("post")))
                        .fmap(constantly(Either.<StrictQueue<String>, String>right("post")));
        Fn2<HttpServletRequest, HttpServletResponse, MonadRec<Either<StrictQueue<String>, String>, Writer<HMap, ?>>> putFn =
                (req, res) -> tell(singletonHMap(putInteraction, right("put")))
                        .fmap(constantly(Either.<StrictQueue<String>, String>right("put")));
        Fn2<HttpServletRequest, HttpServletResponse, MonadRec<Either<StrictQueue<String>, String>, Writer<HMap, ?>>> deleteFn =
                (req, res) -> tell(singletonHMap(deleteInteraction, right("delete")))
                        .fmap(constantly(Either.<StrictQueue<String>, String>right("delete")));
        Fn2<HttpServletRequest, HttpServletResponse, MonadRec<Either<StrictQueue<String>, String>, Writer<HMap, ?>>> optionsFn =
                (req, res) -> tell(singletonHMap(optionsInteraction, right("options")))
                        .fmap(constantly(Either.<StrictQueue<String>, String>right("options")));
        Fn2<HttpServletRequest, HttpServletResponse, MonadRec<Either<StrictQueue<String>, String>, Writer<HMap, ?>>> traceFn =
                (req, res) -> tell(singletonHMap(traceInteraction, right("trace")))
                        .fmap(constantly(Either.<StrictQueue<String>, String>right("trace")));
        Fn2<HttpServletResponse, String, MonadRec<Unit, Writer<HMap, ?>>> successFn =
                (res, success) -> tell(singletonHMap(successInteraction, success));
        Fn2<HttpServletResponse, StrictQueue<String>, MonadRec<Unit, Writer<HMap, ?>>> failureFn =
                (res, failure) -> tell(singletonHMap(failureInteraction, (failure)));

        return functionalServlet(headFn, getFn, postFn, putFn, deleteFn, optionsFn, traceFn, successFn, failureFn);
    }

    private FunctionalServlet<Writer<HMap, ?>, StrictQueue<String>, String> defaultFailingTestServlet() {
        Fn2<HttpServletRequest, HttpServletResponse, MonadRec<Either<StrictQueue<String>, String>, Writer<HMap, ?>>> headFn =
                (req, res) -> tell(singletonHMap(headInteraction, left(strictQueue("head failed"))))
                        .fmap(constantly(left(strictQueue("head failed"))));
        Fn2<HttpServletRequest, HttpServletResponse, MonadRec<Either<StrictQueue<String>, String>, Writer<HMap, ?>>> getFn =
                (req, res) -> tell(singletonHMap(getInteraction, left(strictQueue("get failed"))))
                        .fmap(constantly(left(strictQueue("get failed"))));
        Fn2<HttpServletRequest, HttpServletResponse, MonadRec<Either<StrictQueue<String>, String>, Writer<HMap, ?>>> postFn =
                (req, res) -> tell(singletonHMap(postInteraction, left(strictQueue("post failed"))))
                        .fmap(constantly(left(strictQueue("post failed"))));
        Fn2<HttpServletRequest, HttpServletResponse, MonadRec<Either<StrictQueue<String>, String>, Writer<HMap, ?>>> putFn =
                (req, res) -> tell(singletonHMap(putInteraction, left(strictQueue("put failed"))))
                        .fmap(constantly(left(strictQueue("put failed"))));
        Fn2<HttpServletRequest, HttpServletResponse, MonadRec<Either<StrictQueue<String>, String>, Writer<HMap, ?>>> deleteFn =
                (req, res) -> tell(singletonHMap(deleteInteraction, left(strictQueue("delete failed"))))
                        .fmap(constantly(left(strictQueue("delete failed"))));
        Fn2<HttpServletRequest, HttpServletResponse, MonadRec<Either<StrictQueue<String>, String>, Writer<HMap, ?>>> optionsFn =
                (req, res) -> tell(singletonHMap(optionsInteraction, left(strictQueue("options failed"))))
                        .fmap(constantly(left(strictQueue("options failed"))));
        Fn2<HttpServletRequest, HttpServletResponse, MonadRec<Either<StrictQueue<String>, String>, Writer<HMap, ?>>> traceFn =
                (req, res) -> tell(singletonHMap(traceInteraction, left(strictQueue("trace failed"))))
                        .fmap(constantly(left(strictQueue("trace failed"))));
        Fn2<HttpServletResponse, String, MonadRec<Unit, Writer<HMap, ?>>> successFn =
                (res, success) -> tell(singletonHMap(successInteraction, success));
        Fn2<HttpServletResponse, StrictQueue<String>, MonadRec<Unit, Writer<HMap, ?>>> failureFn =
                (res, failure) -> tell(singletonHMap(failureInteraction, (failure)));

        return functionalServlet(headFn, getFn, postFn, putFn, deleteFn, optionsFn, traceFn, successFn, failureFn);
    }

    private static final Monoid<HMap> MERGE_RESULTS = mergeHMaps()
            .key(headInteraction, (x, y) -> x)
            .key(getInteraction, (x, y) -> x)
            .key(postInteraction, (x, y) -> x)
            .key(putInteraction, (x, y) -> x)
            .key(deleteInteraction, (x, y) -> x)
            .key(optionsInteraction, (x, y) -> x)
            .key(traceInteraction, (x, y) -> x)
            .key(successInteraction, (x, y) -> x)
            .key(failureInteraction, (x, y) -> x);
}
