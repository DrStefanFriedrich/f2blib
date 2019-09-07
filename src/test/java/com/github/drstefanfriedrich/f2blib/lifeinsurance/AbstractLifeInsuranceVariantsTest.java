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

package com.github.drstefanfriedrich.f2blib.lifeinsurance;

import com.github.drstefanfriedrich.f2blib.util.TestUtil;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.number.OrderingComparison.greaterThan;
import static org.junit.Assert.*;

/**
 * A performance test that calculates different variants of life insurances.
 */
public abstract class AbstractLifeInsuranceVariantsTest {

    protected abstract LifeInsuranceCalculator getLifeInsuranceCalculator();

    private static final int REPETITIONS = 1;
    private static final int THREADS = 8;

    private final ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    private final List<Future<?>> workerFutures = new ArrayList<>();

    @Test
    public void performance() {
        TestUtil.assumePerformanceTest();
        LifeInsuranceCalculator calculator = getLifeInsuranceCalculator();

        long start = System.currentTimeMillis();

        for (int i = 0; i < THREADS; i++) {
            workerFutures.add(executorService.submit(new Worker(calculator)));
        }

        workerFutures.forEach(f -> {
            try {
                f.get();
            } catch (InterruptedException | ExecutionException e) {
                throw new IllegalStateException(e);
            }
        });

        long end = System.currentTimeMillis();

        executorService.shutdown();

        fail("Performance should always be better. That's why we fail the unit test. " +
                "Total duration (ms): " + (end - start));
    }

    private static final class Worker implements Runnable {

        private final LifeInsuranceCalculator calculator;

        public Worker(LifeInsuranceCalculator calculator) {
            this.calculator = calculator;
        }

        @Override
        public void run() {
            for (int i = 0; i < REPETITIONS; i++) {
                executeCalculations();
            }
        }

        private void executeCalculations() {
            for (int ageAtContractBeginning = 20; ageAtContractBeginning <= 40; ageAtContractBeginning += 2) {
                for (int durationOfContract = 20; durationOfContract <= 65 - ageAtContractBeginning; durationOfContract += 2) {
                    for (int fee = 5000; fee <= 15000; fee += 5000) {
                        for (int deathPremium = 5000; deathPremium <= 15000; deathPremium += 5000) {
                            for (int interestRate = 1; interestRate <= 2; interestRate++) {
                                for (int expenseFactor = 5; expenseFactor <= 6; expenseFactor++) {

                                    double male = calculator.calculate(ageAtContractBeginning, durationOfContract, fee,
                                            deathPremium, true, ((double) interestRate) / 100,
                                            ((double) expenseFactor) / 100);
                                    double female = calculator.calculate(ageAtContractBeginning, durationOfContract, fee,
                                            deathPremium, false, ((double) interestRate) / 100,
                                            ((double) expenseFactor) / 100);

                                    assertThat(male, greaterThan(female));
                                }
                            }
                        }
                    }
                }
            }
        }

    }

}
