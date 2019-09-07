/*****************************************************************************
 *
 * Copyright (c) 2019 Stefan Friedrich
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 ******************************************************************************/

package com.github.drstefanfriedrich.f2blib;

import com.github.drstefanfriedrich.f2blib.util.TestUtil;
import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.*;

import static java.lang.String.format;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Do a lot of function evaluations concurrently to show how good the library
 * performs.
 */
abstract class AbstractPerformanceTestDefinition extends AbstractPerformanceTest {

    private static final String FUNCTION_DEFINITION;

    static {
        try {
            FUNCTION_DEFINITION = readFunctionByFileName("PerformanceTestFunction.f");
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }

    private final BlockingQueue<RequestResponse> requestQueue = new ArrayBlockingQueue<>(NUMBER_OBJECTS + 1);
    private final BlockingQueue<RequestResponse> responseQueue = new ArrayBlockingQueue<>(NUMBER_OBJECTS + 1);
    private final FunctionEvaluationKernel kernel = instantiateKernel();
    private final ExecutorService executorService = Executors.newFixedThreadPool(NUMBER_CORES + 1);
    private final List<Future<?>> workerFutures = new ArrayList<>();

    protected abstract FunctionEvaluationKernel instantiateKernel();

    @Test
    public void performance() throws ExecutionException, InterruptedException, TimeoutException {
        TestUtil.assumePerformanceTest();

        try {

            kernel.load(FUNCTION_DEFINITION);

            // Start the workers
            for (int i = 0; i < NUMBER_CORES; i++) {
                workerFutures.add(executorService.submit(new Evaluator(requestQueue, responseQueue, kernel)));
            }

            // Start the response collector
            Future<?> waitForResultConsumer = executorService.
                    submit(new ResultConsumer(responseQueue));

            long start = System.currentTimeMillis();

            // Enqueue the requests
            Set<RequestResponse> testObjects = prepareTestObjects();
            for (RequestResponse rr : testObjects) {
                requestQueue.put(rr);
            }
            requestQueue.put(END_OF_QUEUE);

            // Wait until all responses arrived
            waitForResultConsumer.get( 60, TimeUnit.SECONDS);

            // Check workers for exceptions thrown
            workerFutures.forEach(f -> {
                try {
                    if (f.isDone()) {
                        f.get();
                    }
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } catch (ExecutionException e) {
                    throw new RuntimeException(e.getCause());
                }
            });

            // Stop the workers
            workerFutures.forEach(f -> f.cancel(true));

            long end = System.currentTimeMillis();

            fail("Performance should always be better. That's why we fail the unit test. " +
                    "Total duration (ms): " + (end - start));

        } catch (TimeoutException e) {
            throw new RuntimeException(format("requestQueue.size=%d, responseQueue.size=%d",
                    requestQueue.size(), responseQueue.size()), e);
        } finally {
            executorService.shutdown();
        }
    }

    @Test
    public void runOneEvaluation() throws ExecutionException, InterruptedException, TimeoutException {

        kernel.load(FUNCTION_DEFINITION);
        RequestResponse rr = new RequestResponse();
        kernel.eval(FUNCTION_NAME, rr.getP(), rr.getX(), rr.getY());

        assertTrue(true);
    }

}
