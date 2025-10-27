package com.jtortugo.graaljs.loops;

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

public class ForLoops {
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
				.option("engine.PropagateLoopCountToLexicalSingleCaller", "false")
				.option("engine.PropagateLoopCountToLexicalSingleCallerMaxDepth", "0")
				.option("engine.TraceCompilationDetails", "false")
				.option("engine.TraceTransferToInterpreter", "false")
				.option("compiler.TraceInliningDetails", "false")
				.option("compiler.TraceMethodExpansion", "false")
				.option("compiler.TraceNodeExpansion", "false")
				.option("engine.OSR", "false")
//				.option("cpusampler",  "flamegraph")
//				.option("cpusampler.SampleInternal",  "true")
//				.option("cpusampler.Output", "json")
				.build();
		
		try (Context context = Context.newBuilder() .allowHostAccess(HostAccess.newBuilder(HostAccess.ALL).build())
                .allowHostClassLookup(className -> true).engine(engine).build()) {
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
			exec(execId, test, 10009, 11009);

			System.out.println();
		}
	}

	
	private static void exec(long execId, Value test, long from, long to) {
		exec(execId, test, from, to, true);
	}
	
	private static void exec(long execId, Value test, long from, long to, boolean print) {
		long start = 0, end = 0, acc = 0;
		long length = (to - from) + 1; // +1 because both ends are inclusive
		int numOrders = 500;
		ArrayList<Order> left = new ArrayList<Order>();
		ArrayList<Order> right = new ArrayList<Order>();
		ArrayList<Order> noneof = new ArrayList<Order>();
	
		for (int i=0; i<numOrders; i++) {
			left.add(new Order(i));
			right.add(new Order(numOrders + i));
			noneof.add(new Order(2*numOrders + i));
		}
		if (print) {
			System.out.print("Execution " + execId + ") Iterations [" + String.format("%5d", from) + "," + String.format("%5d", to) + "] average duration: ");	
		}
		
		for (int i=0; i<length; i++) {
			start = System.nanoTime();
			test.execute(left, right, noneof);
			end = System.nanoTime();
			acc = acc + (end-start);
		}

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
	
	public static class Order {
		public int id;
		
		public Order(int id) {
			this.id = id;
		}
	}
}
