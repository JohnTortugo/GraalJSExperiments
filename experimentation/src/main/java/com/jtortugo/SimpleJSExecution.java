package com.jtortugo;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.util.Scanner;

import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Engine;
import org.graalvm.polyglot.Value;

public class SimpleJSExecution {
	private static String codeFilePath;
	private static Boolean doWarmup;
	private static final DecimalFormat formatter = new DecimalFormat("#,###");
	
	public static void main(String[] args) throws IOException {
		codeFilePath = args[0];
		
		System.out.println("codeFileName " + codeFilePath + " doWarmup " + doWarmup);
		
		for (int i=1; i<=1; i++) {
			test(i);
		}
	}

	public static void test(int execId) throws IOException {
		final Engine engine = Engine.newBuilder("js")
				.allowExperimentalOptions(true)
				.option("engine.DynamicCompilationThresholds", "false")
				.option("engine.BackgroundCompilation", "false")
				.option("engine.TraceCompilationDetails", "true")
				.option("engine.PropagateLoopCountToLexicalSingleCaller", "false")
				.option("engine.PropagateLoopCountToLexicalSingleCallerMaxDepth", "0")
				.option("engine.OSR", "false")
				.build();
		
		try (Context context = Context.newBuilder().engine(engine).build()) {
			context.eval("js", readCode(codeFilePath));
			Value jsBindings = context.getBindings("js");
			Value test       = jsBindings.getMember("main");
	
			exec(execId, test,     1,     5);
			exec(execId, test,     6,   399);
			exec(execId, test,   400,   405, false /* no printing */);
			exec(execId, test,   406,  9999);
			exec(execId, test, 10000, 10005, false /* no printing */);
			exec(execId, test, 10006, 10006);
			exec(execId, test, 10007, 10007);
			exec(execId, test, 10008, 10008);

			System.out.println();
		}
	}

	
	private static void exec(long execId, Value test, long from, long to) {
		exec(execId, test, from, to, true);
	}
	
	private static void exec(long execId, Value test, long from, long to, boolean print) {
		long start = 0, end = 0, acc = 0;
		long length = (to - from) + 1; // +1 because both ends are inclusive
	
	    try {
            System.out.println("\n\nProceed with executions " + from + " to " + to + "?\n");
            System.in.read();
        } catch (Exception e) { } 

		if (print) {
			System.out.print("Execution " + execId + ") Iterations [" + String.format("%5d", from) + "," + String.format("%5d", to) + "] average duration: ");	
		}
		
		start = System.nanoTime();
		test.execute(length);
		end = System.nanoTime();
		acc = acc + (end-start);

		if (print) {
			System.out.println(formatter.format((acc / length) / 1_000) + " us");	
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
}
