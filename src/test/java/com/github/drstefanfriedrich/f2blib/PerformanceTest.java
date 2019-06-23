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
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.*;

import static org.junit.Assert.fail;

/**
 * Do a lot of function evaluations concurrently to show how good the library
 * performs.
 */
public class PerformanceTest extends AbstractPerformanceTest {

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

    private final FunctionEvaluationKernel kernel = new FunctionEvaluationFactory().get().create();

    private final ExecutorService executorService = Executors.newFixedThreadPool(NUMBER_CORES + 1);

    private final List<Future<?>> workerFutures = new ArrayList<>();

    @Test
    public void performance() throws ExecutionException, InterruptedException, TimeoutException {
        TestUtil.assumePerformanceTest();
        TestUtil.assumeNotRunFromGradle();

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
            waitForResultConsumer.get(100, TimeUnit.SECONDS);

            // Stop the workers
            workerFutures.forEach(f -> f.cancel(true));

            long end = System.currentTimeMillis();

            fail("Performance should always be better. That's why we fail the unit test.\n\n" +
                    "Number of objects: " + NUMBER_OBJECTS + "\n" +
                    "Number of cores: " + NUMBER_CORES + "\n" +
                    "Total duration (ms): " + (end - start) + "\n" +
                    "Objects per second: " + ((double) (NUMBER_OBJECTS)) / (end - start) * 1000);


        } catch (TimeoutException e) {
            for (Future<?> future : workerFutures) {
                future.get(10, TimeUnit.SECONDS);
            }
        } finally {
            executorService.shutdown();
        }
    }

}
