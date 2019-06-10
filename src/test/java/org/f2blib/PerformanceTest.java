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

package org.f2blib;

import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.*;

import static org.f2blib.util.TestUtil.assumePerformanceTest;
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

    private final BlockingQueue<RequestResponse> requestQueue = new ArrayBlockingQueue<>(NUMBER_OBJECTS + NUMBER_CORES);

    private final BlockingQueue<RequestResponse> responseQueue = new ArrayBlockingQueue<>(NUMBER_OBJECTS + NUMBER_CORES);

    private final FunctionEvaluationKernel kernel = new FunctionEvaluationFactory().get().create();

    private final ExecutorService worker = Executors.newFixedThreadPool(NUMBER_CORES);

    private final ExecutorService responseCollector = Executors.newSingleThreadExecutor();

    private final List<Future<?>> workerFutures = new ArrayList<>();

    @Test
    public void performance() throws ExecutionException, InterruptedException {
        assumePerformanceTest();

        kernel.load(FUNCTION_DEFINITION);

        // Start the workers
        for (int i = 0; i < NUMBER_CORES; i++) {
            workerFutures.add(worker.submit(new Evaluator(requestQueue, responseQueue, kernel)));
        }

        // Start the response collector
        Future<BlockingQueue<RequestResponse>> waitForResultConsumer = responseCollector.
                submit(new ResultConsumer(responseQueue), requestQueue);

        long start = System.currentTimeMillis();

        // Enqueue the requests
        Set<RequestResponse> testObjects = prepareTestObjects();
        for (RequestResponse rr : testObjects) {
            requestQueue.put(rr);
        }

        // Wait until all responses arrived
        waitForResultConsumer.get();

        // Stop the workers
        workerFutures.forEach(f -> f.cancel(true));

        long end = System.currentTimeMillis();

        worker.shutdown();
        responseCollector.shutdown();

        fail("Performance should always be better. That's why we fail the unit test.\n\n" +
                "Number of objects: " + NUMBER_OBJECTS + "\n" +
                "Number of cores: " + NUMBER_CORES + "\n" +
                "Total duration (ms): " + (end - start) + "\n" +
                "Objects per second: " + ((double) (NUMBER_OBJECTS)) / (end - start) * 1000);
    }

}
