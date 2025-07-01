package com.jtortugo.proxies;

import java.util.function.Function;

import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Engine;
import org.graalvm.polyglot.Source;

class BenchIt {
    public static void computeStats(String label, long[] data, int N) {
        if (data == null || data.length == 0 || N <= 1 || N > data.length) {
            throw new IllegalArgumentException("Invalid input or N too large/small");
        }

        int startIndex = data.length - N;
        double sum = 0.0;
        for (int i = startIndex; i < data.length; i++) {
            sum += data[i];
        }
        double average = sum / N;

        double varianceSum = 0.0;
        for (int i = startIndex; i < data.length; i++) {
            double diff = data[i] - average;
            varianceSum += diff * diff;
        }

        double standardDeviation = Math.sqrt(varianceSum / (N - 1)); // sample stdev
        double standardError = standardDeviation / Math.sqrt(N);

        double zScore = 1.96; // for 95% confidence
        double confidenceLow = average - zScore * standardError;
        double confidenceHigh = average + zScore * standardError;

        System.out.printf("\t%s %n", label);
        System.out.printf("\t\tAverage...........................: %.2f us%n", average);
        System.out.printf("\t\tStandard Deviation................: %.2f us%n", standardDeviation);
        System.out.printf("\t\t95%% Confidence Interval...........: [%.2f, %.2f] us%n", confidenceLow, confidenceHigh);
    }
    
	public static Double benchIt(String jsSource, String proxyConfigName, Function<Integer, Object> gen) {
        final int BENCH_ITERATIONS = 20100;
    	long[] times = new long[BENCH_ITERATIONS];
    	Double[] results = new Double[BENCH_ITERATIONS];

    	final Engine engine = Engine.newBuilder("js")
                .allowExperimentalOptions(true)
                .option("engine.DynamicCompilationThresholds", "false")
                .option("engine.BackgroundCompilation",        "false")
                .option("engine.OSR", 						   "false")
                .build();

        var source = Source.newBuilder("js", jsSource, "field_access.js").buildLiteral();

        try (var context = Context.newBuilder("js").engine(engine).allowAllAccess(true).build()) {
            var function = context.eval(source);

            for (int i = 0; i < BENCH_ITERATIONS; i++) {
                var order = gen.apply(i);
                var start = System.nanoTime();
                results[i] = function.execute(order, 100_000).asDouble();
				times[i] = (System.nanoTime() - start) / 1_000;
                
            }
        }

        computeStats(proxyConfigName, times, 10_000);
        return results[BENCH_ITERATIONS-1];
    }
}