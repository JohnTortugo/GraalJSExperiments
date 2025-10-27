package com.jtortugo.graaljs;

import java.io.File;
import java.util.ArrayList;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.util.Scanner;

import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.HostAccess;
import org.graalvm.polyglot.Engine;
import org.graalvm.polyglot.Value;

public class BenchIt {
	private static String codeFilePath;
	private static final DecimalFormat formatter = new DecimalFormat("#,###0.000");
	
	public static void main(String[] args) throws IOException {
		codeFilePath = args[0];
		
		System.out.println("codeFileName " + codeFilePath);
		
		for (int i=1; i<=5; i++) {
			test(i);
		}
	}

	public static void test(int execId) throws IOException {
		final Engine engine = Engine.newBuilder("js")
				.allowExperimentalOptions(true)
				.option("engine.DynamicCompilationThresholds", "false")
				.option("engine.BackgroundCompilation", "false")
				.option("engine.PropagateLoopCountToLexicalSingleCaller", "false")
				.option("engine.PropagateLoopCountToLexicalSingleCallerMaxDepth", "0")
				.option("engine.TraceCompilationDetails", "false")
				.option("engine.TraceTransferToInterpreter", "false")
				.option("compiler.TraceInliningDetails", "false")
				.option("compiler.TraceMethodExpansion", "false")
				.option("compiler.TraceNodeExpansion", "false")
				.option("engine.OSR", "false")
				.build();
		
		try (Context context = Context.newBuilder()
				.allowHostAccess(HostAccess.newBuilder(HostAccess.ALL).build())
                .allowHostClassLookup(className -> true)
                .engine(engine)
                .build()) {
			context.eval("js", readCode(codeFilePath));
			Value jsBindings = context.getBindings("js");
			Value test       = jsBindings.getMember("main");
			long widx 	     = 0;

			// Warming up, compiling, etc.
			for (widx = 0; widx < 15000; widx++) {
				test.execute();
			}

			// For real now
			System.out.println("Execution " + execId);
			ArrayList<Double> measurements = new ArrayList<>();
			String label = "Statistics for iterations " + widx + " to " + (15000+1000) + ": ";
			while (widx++ < 15000+1000) {
				long start = System.nanoTime();
				test.execute();
				long end = System.nanoTime();

				if (widx < 15000+5) {
					System.out.println("\tIteration " + widx + " took " + formatter.format((end - start) / 1_000.0) + " us");	
				}
				measurements.add((end-start) / 1_000.0);
			}
			computeStats(label, measurements);
		}
	}

	private static String readCode(String codeFileName) {
		try {
			InputStream inputStream = new FileInputStream(new File(codeFileName));

			try (Scanner scanner = new Scanner(inputStream, StandardCharsets.UTF_8.name())) {
				scanner.useDelimiter("\\A"); // Match the beginning of the input
				return scanner.hasNext() ? scanner.next() : "";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

    public static void computeStats(String label, ArrayList<Double> measurements) {
        if (measurements == null || measurements.size() == 0) {
            throw new IllegalArgumentException("Invalid input or N too large/small");
        }

        double sum = 0.0;
        for (int i = 0; i < measurements.size(); i++) {
            sum += measurements.get(i);
        }
        double average = sum / measurements.size();

        double varianceSum = 0.0;
        for (int i = 0; i < measurements.size(); i++) {
            double diff = measurements.get(i) - average;
            varianceSum += diff * diff;
        }

        double standardDeviation = Math.sqrt(varianceSum / (measurements.size() - 1)); // sample stdev
        double standardError = standardDeviation / Math.sqrt(measurements.size());

        double zScore = 1.96; // for 95% confidence
        double confidenceLow = average - zScore * standardError;
        double confidenceHigh = average + zScore * standardError;

        System.out.printf("\t%s %n", label);
        System.out.printf("\t\tAverage...........................: %.2f us%n", average);
        System.out.printf("\t\tStandard Deviation................: %.2f us%n", standardDeviation);
        System.out.printf("\t\t95%% Confidence Interval...........: [%.2f, %.2f] us%n", confidenceLow, confidenceHigh);
    }
}
