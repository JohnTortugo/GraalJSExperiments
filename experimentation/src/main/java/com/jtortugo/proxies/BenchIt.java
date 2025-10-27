package com.jtortugo.proxies;

import java.util.function.Function;

import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Engine;
import org.graalvm.polyglot.Source;
import org.graalvm.polyglot.Value;
import com.oracle.truffle.api.interop.TruffleObject;

public class BenchIt {
    public static void computeStats(String label, long[] data, int sampleSize) {
        if (data == null || data.length == 0 || sampleSize <= 1 || sampleSize > data.length) {
            throw new IllegalArgumentException("Invalid input or N too large/small");
        }

        int startIndex = data.length - sampleSize;
        double sum = 0.0;
        for (int i = startIndex; i < data.length; i++) {
            sum += data[i];
        }
        double average = sum / sampleSize;

        double varianceSum = 0.0;
        for (int i = startIndex; i < data.length; i++) {
            double diff = data[i] - average;
            varianceSum += diff * diff;
        }

        double standardDeviation = Math.sqrt(varianceSum / (sampleSize - 1)); // sample stdev
        double standardError = standardDeviation / Math.sqrt(sampleSize);

        double zScore = 1.96; // for 95% confidence
        double confidenceLow = average - zScore * standardError;
        double confidenceHigh = average + zScore * standardError;

        System.out.printf("\t%s %n", label);
        System.out.printf("\t\tAverage...........................: %.2f us%n", average);
        System.out.printf("\t\tStandard Deviation................: %.2f us%n", standardDeviation);
        System.out.printf("\t\t95%% Confidence Interval...........: [%.2f, %.2f] us%n", confidenceLow, confidenceHigh);
    }
    
	public static double benchIt(String expName, String jsSource, String proxyConfigName, Function<Integer, Object> gen) {
        final int BENCH_ITERATIONS = 10000;
    	long[] times = new long[BENCH_ITERATIONS];
    	double[] results = new double[BENCH_ITERATIONS];

    	final Engine engine = Engine.newBuilder("js")
                .allowExperimentalOptions(true)
                .option("engine.DynamicCompilationThresholds", "false")
                .option("engine.BackgroundCompilation",        "false")
                .option("engine.OSR", 						   "false")
                .option("engine.TraceCompilationDetails",      "true")
                .option("engine.TraceTransferToInterpreter",      "true")
                .option("engine.TraceAssumptions",      "true")
                .build();

        var source = Source.newBuilder("js", jsSource, expName + "_" + proxyConfigName + ".js").buildLiteral();

        try (var context = Context.newBuilder("js").engine(engine).allowAllAccess(true).build()) {
            var function = context.eval(source);

            for (int i = 0; i < BENCH_ITERATIONS; i++) {
                var order = gen.apply(i);
                var start = System.nanoTime();
                Value vlw = function.execute(order, 10_000);
				times[i] = (System.nanoTime() - start) / 1_000;
				results[i] = vlw.asDouble();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        computeStats(proxyConfigName, times, BENCH_ITERATIONS > 1000 ? 1000 : BENCH_ITERATIONS);
        return results[BENCH_ITERATIONS-1];
    }
}
