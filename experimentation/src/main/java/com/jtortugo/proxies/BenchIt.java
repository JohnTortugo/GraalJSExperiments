package com.jtortugo.proxies;

import java.util.function.Function;

import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Engine;
import org.graalvm.polyglot.Source;
import org.graalvm.polyglot.Value;
import com.oracle.truffle.api.interop.TruffleObject;
import com.oracle.truffle.host.DatapathProxy;

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

        System.out.printf("\t%25s \t\tAvgerage: %4.2f us \t\tStDev: %4.2f us  \t\t95%% ConfInt: [%4.2f, %4.2f] us %n", 
        		label, average, standardDeviation, confidenceLow, confidenceHigh);
    }
    
	public static double benchIt(String expName, String jsSource, String proxyConfigName, Function<Integer, Object> gen) {
        final int BENCH_ITERATIONS = 100000;
    	long[] times = new long[BENCH_ITERATIONS];
    	double[] results = new double[BENCH_ITERATIONS];

    	final Engine engine = Engine.newBuilder("js")
                .allowExperimentalOptions(true)
                .option("engine.DynamicCompilationThresholds", "false")
                .option("engine.BackgroundCompilation",        "false")
                .option("engine.OSR", 						   "false")
                .option("engine.TraceCompilationDetails",      "false")
                .option("engine.TraceTransferToInterpreter",   "false")
                .option("engine.TraceAssumptions",      	   "false")
                .build();

        var source = Source.newBuilder("js", jsSource, expName + "_" + proxyConfigName + ".js").buildLiteral();

        try (var context = Context.newBuilder("js").engine(engine).allowAllAccess(true).build()) {
            var function = context.eval(source);

            for (int i = 0; i < BENCH_ITERATIONS; i++) {
                var order = gen.apply(i);
                var start = System.nanoTime();
                Value vlw = function.execute(order, 10_000);
				times[i] = (System.nanoTime() - start) / 1_000;
				if (vlw.isDatapathProxyObject()) {
					DatapathProxy tfl = vlw.asDatapathProxyObject();
					//System.out.println("InteropLibrary!! " + tfl.read("field1"));
				}
				//results[i] = vlw.asDouble();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        computeStats(proxyConfigName, times, BENCH_ITERATIONS > 1000 ? 1000 : BENCH_ITERATIONS);
        return results[BENCH_ITERATIONS-1];
    }
}
