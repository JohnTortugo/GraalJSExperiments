package com.jtortugo.proxies;

import com.jtortugo.ExperimentConfig;
import com.oracle.truffle.api.interop.UnknownIdentifierException;
import com.oracle.truffle.api.interop.UnsupportedMessageException;
import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Engine;
import org.graalvm.polyglot.HostAccess;
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

	public static Value benchIt(String expName, String jsSource, String proxyConfigName, Function<Integer, Object> gen) {
		Value[] results = new Value[BENCH_ITERATIONS];

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
				results[i] = function.execute(order, 100_000);
			}
		}

		return results[BENCH_ITERATIONS-1];
	}
	  
    public static void main(String[] args) throws Exception {
		Engine engine = Engine.newBuilder("js")
				.allowExperimentalOptions(true)
				.option("engine.WarnInterpreterOnly", "false")
				.build();

		String jsSource = "function foobar(input) {  return input; }";
		var source = Source.newBuilder("js", jsSource, "foobar.js").buildLiteral();

		HostAccess access = HostAccess.newBuilder(HostAccess.ALL).targetTypeMapping(Value.class, Object.class,
				(v -> v.hasMembers()), (v -> v)).build();

		try (var context = Context.newBuilder("js").engine(engine).allowHostAccess(HostAccess.ALL).build()) {
			context.eval(source);

			Value jsBindings = context.getBindings("js");
			var jsFunction = jsBindings.getMember("foobar");

			//Object tsObj = new ProtonBooleanProxy();
			Object tsObj = new MapAccess_OneField_ProxyObject(2025);
			Value result = jsFunction.execute(tsObj);


			System.out.println("result.isHostObject(): " + result.isHostObject());
			System.out.println("result.isProxyObject(): " + result.isProxyObject());
			System.out.println("result.isMetaObject(): " + result.isMetaObject());

			//ProtonBooleanProxy res2 = result.as(ProtonBooleanProxy.class);
			MapAccess_OneField_ProxyObject res2 = result.asProxyObject();
			System.out.println("This is res2: " + res2);
			System.out.println("This is field1: " + res2.getMember("field1"));
			System.out.println("Context: " + context.getClass().getSimpleName());
		}
    }
}



