package com.jtortugo;

import java.util.ArrayList;
import java.util.function.Function;

public class ExperimentConfig {
	public final String name;
	public final String source;
	public final ArrayList<ProxyConfig> proxyConfigs;

	public ExperimentConfig(String name, String source) {
		this.name = name;
		this.source = source;
		this.proxyConfigs = new ArrayList<ProxyConfig>();
	}
	
	public ExperimentConfig addProxyConfig(String name, Function<Integer, Object> gen) {
		this.proxyConfigs.add(new ProxyConfig(name, gen));
		return this;
	}
	
	public static class ProxyConfig {
		public final String name;
		public final Function<Integer, Object> gen;
		
		public ProxyConfig(String name, Function<Integer, Object> gen) {
			this.name = name;
			this.gen = gen;
		}
	}
}