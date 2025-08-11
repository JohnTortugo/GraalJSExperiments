package com.jtortugo.proxies;

import com.oracle.truffle.api.interop.UnknownIdentifierException;
import com.oracle.truffle.api.interop.UnsupportedMessageException;
import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Engine;
import org.graalvm.polyglot.Source;
import org.graalvm.polyglot.Value;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class TruffleObjectProxied {
	private static final int BENCH_ITERATIONS = 100_000;


	private static final ArrayList<ExperimentConfig> experimetConfigs = new ArrayList<>(List.of(
				new ExperimentConfig("return_unchanged_input", """
                    (
                        function read_after_read(input) {
                            return input;
                        }
                    )
                    """)
			.addProxyConfig("MapAccess_SingleField_CustomProxy", null )
	));

	public static Object gen_value(int i) {
		if (i % 2 == 0) {
			return i;
		} else {
			return new MapAccess_OneField_CustomProxy(i);
		}
	}

	public static Double benchIt(String expName, String jsSource, String proxyConfigName, Function<Integer, Object> gen) {
		Double[] results = new Double[BENCH_ITERATIONS];

		final Engine engine = Engine.newBuilder("js")
				.allowExperimentalOptions(true)
				.option("engine.DynamicCompilationThresholds", "false")
				.option("engine.BackgroundCompilation",        "false")
				.option("engine.OSR", 						   "false")
				.build();

		var source = Source.newBuilder("js", jsSource, expName + "_" + proxyConfigName + ".js").buildLiteral();

		try (var context = Context.newBuilder("js").engine(engine).allowAllAccess(true).build()) {
			var function = context.eval(source);

			for (int i = 0; i < BENCH_ITERATIONS; i++) {
				Object order = gen_value(i);
				Value val = function.execute(order, 100_000);

				if (val.isNumber()) {
					results[i] = function.execute(order, 100_000).asDouble();
					System.out.println("isNumber: true, number is: " + results[i]);
				} else {
					System.out.println("Something else: true");

					try {
						MapAccess_OneField_CustomProxy proxy = val.as(MapAccess_OneField_CustomProxy.class);
						System.out.println("Terms: " + proxy.readMember("term"));

						System.out.println("Annotations_AsField: ");
						for (String a : (String[]) (proxy.readMember("annotations"))) {
							System.out.println("\t" + a);
						}

						System.out.println("Annotations_AsMethod: ");
						for (String a : proxy.getAnnotations()) {
							System.out.println("\t" + a);
						}
					} catch (ClassCastException | UnknownIdentifierException | UnsupportedMessageException e) {
						e.printStackTrace();
					}
                }
			}
		}

		return results[BENCH_ITERATIONS-1];
	}
	  
    public static void main(String[] args) throws Exception {
    	for (var expConfig : experimetConfigs) {
    		System.out.println("Source: " + expConfig.name);
    		for (var proxyConfig : expConfig.proxyConfigs) {    			
    			benchIt(expConfig.name, expConfig.source, proxyConfig.name, proxyConfig.gen);
    		}
    	}
    }
}



