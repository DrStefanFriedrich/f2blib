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

import com.google.common.base.Charsets;
import com.google.common.io.ByteSource;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.stream.IntStream;

abstract class AbstractPerformanceTest {

    static final int NUMBER_OBJECTS = 10000000;

    static final int NUMBER_CORES = Runtime.getRuntime().availableProcessors();

    private static final String FUNCTION_NAME = "org.f2blib.PerformanceTestFunction";

    static String readFunctionByFileName(String fileName) throws IOException {
        return new ByteSource() {
            @Override
            public InputStream openStream() {
                return getClass().getResourceAsStream("/org/f2blib/" + fileName);
            }
        }.asCharSource(Charsets.UTF_8).read();
    }

    boolean performanceTestsEnabled() {
        return Boolean.valueOf(System.getProperty("org.f2blib.PerformanceTest.enabled", Boolean.FALSE.toString()));
    }

    Set<RequestResponse> prepareTestObjects() {
        Set<RequestResponse> result = new HashSet<>();

        IntStream.range(0, NUMBER_OBJECTS).forEach(i -> result.add(new RequestResponse()));

        return result;
    }

    static class RequestResponse {

        private final double[] p = new double[2];

        private final double[] x = new double[2];

        private final double[] y = new double[2];

        double[] getP() {
            return p;
        }

        double[] getX() {
            return x;
        }

        double[] getY() {
            return y;
        }

    }

    /**
     * This thread takes in an infinite loop elements from the requestQueue, processes them
     * (i.e. evaluates the function), and puts the result back in the responseQueue until no
     * more elements are available in the requestQueue.
     */
    protected static class Evaluator implements Runnable {

        private final BlockingQueue<RequestResponse> requestQueue;

        private final BlockingQueue<RequestResponse> responseQueue;

        private final FunctionEvaluationKernel kernel;

        Evaluator(BlockingQueue<RequestResponse> requestQueue, BlockingQueue<RequestResponse> responseQueue, FunctionEvaluationKernel kernel) {
            this.requestQueue = requestQueue;
            this.responseQueue = responseQueue;
            this.kernel = kernel;
        }

        @Override
        public void run() {
            try {

                while (true) {

                    RequestResponse rr = requestQueue.take();
                    kernel.eval(FUNCTION_NAME, rr.getP(), rr.getX(), rr.getY());
                    responseQueue.put(rr);
                }

            } catch (InterruptedException e) {
                // In case of an interruption, end the thread
            }
        }

    }

    /**
     * This thread just consumes all elements from the responseQueue and does nothing with them.
     */
    protected static class ResultConsumer implements Runnable {

        private final BlockingQueue<RequestResponse> responseQueue;

        ResultConsumer(BlockingQueue<RequestResponse> responseQueue) {
            this.responseQueue = responseQueue;
        }

        @Override
        public void run() {
            try {

                int counter = 0;
                while (counter < NUMBER_OBJECTS) {
                    responseQueue.take();
                    counter++;
                }

            } catch (InterruptedException e) {
                // In case of an interruption, end the thread
            }
        }

    }

}
