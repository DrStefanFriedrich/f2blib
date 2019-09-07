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

import com.google.common.base.Charsets;
import com.google.common.io.ByteSource;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.stream.IntStream;

abstract class AbstractPerformanceTest {

    static final int NUMBER_OBJECTS = 10000;

    static final int NUMBER_CORES = Runtime.getRuntime().availableProcessors();

    protected static final String FUNCTION_NAME = "com.github.drstefanfriedrich.f2blib.performance.TestFunction";

    static String readFunctionByFileName(String fileName) throws IOException {
        return new ByteSource() {
            @Override
            public InputStream openStream() {
                return getClass().getResourceAsStream("/com/github/drstefanfriedrich/f2blib/" + fileName);
            }
        }.asCharSource(Charsets.UTF_8).read();
    }

    Set<RequestResponse> prepareTestObjects() {
        Set<RequestResponse> result = new HashSet<>();

        IntStream.range(0, NUMBER_OBJECTS).forEach(i -> result.add(new RequestResponse()));

        return result;
    }

    static class RequestResponse {

        private final double[] p = new double[2];
        private final double[] x = new double[2];
        private final double[] y = new double[4];

        RequestResponse() {
            x[0] = Math.random();
            x[1] = Math.random();
            p[0] = Math.random();
            p[1] = Math.random();
        }

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

    protected static final RequestResponse END_OF_QUEUE = new RequestResponse();

    /**
     * This thread takes in an infinite loop elements from the requestQueue, processes them
     * (i.e. evaluates the function), and puts the result back in the responseQueue until
     * the end of the queue is reached.
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

                    if (rr == END_OF_QUEUE) {
                        responseQueue.put(rr);
                        break;
                    }

                    kernel.eval(FUNCTION_NAME, rr.getP(), rr.getX(), rr.getY());
                    responseQueue.put(rr);
                }

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
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

                RequestResponse rr;

                do {

                    rr = responseQueue.take();

                } while (rr != END_OF_QUEUE);

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

    }

}
