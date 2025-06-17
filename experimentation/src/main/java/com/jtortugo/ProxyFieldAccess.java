package com.jtortugo;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.Collection;


import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Engine;
import org.graalvm.polyglot.Source;
import org.graalvm.polyglot.Value;
import org.graalvm.polyglot.proxy.ProxyArray;
import org.graalvm.polyglot.proxy.ProxyExecutable;
import org.graalvm.polyglot.proxy.ProxyHashMap;
import org.graalvm.polyglot.proxy.ProxyObject;

public class ProxyFieldAccess {
    private static final int ORDER_SIZE = 1000;
    private static final int BENCH_ITERATIONS = 11100;

    public static class Order {
        public HashMap<String, Object> fields;

		public Order(double price, int quantity) {
            this.fields = new HashMap<String, Object>() {{
            	put("price", price);
            	put("quantity", quantity);
            }};
        }
    }

    private static Order[] directAccessInput(final long seed) {
        var random = new Random(seed);
        var orders = new Order[ORDER_SIZE];
        for (int i = 0; i < orders.length; i++) {
            orders[i] = new Order(random.nextInt(1000) + random.nextDouble(), random.nextInt(5000));
        }
        return orders;
    }

    // Creates an array of Order and passes it to the JavaScript function.
    // The JavaScript function iterates on the orders and access the entries in
    // the "fields" map as if they were real fields of the Map class.
    public static void directAccess() {
        final String content = """
            (function sum_direct(orders) {
                let sum = 0;
                for (var i=0; i<orders.length; i++) {
                  let order = orders[i];
                  sum += order.fields.price * order.fields.quantity;
                }
                return sum;
            })
            """;

        final Engine engine = Engine.newBuilder("js")
                .allowExperimentalOptions(true)
                .option("engine.DynamicCompilationThresholds", "false")
                .option("engine.BackgroundCompilation", "false")
                .option("engine.OSR", "false")
                .build();

        var source = Source.newBuilder("js", content, "sum_direct.js").buildLiteral();

        try (var context = Context.newBuilder("js").engine(engine).allowAllAccess(true).build()) {
            var function = context.eval(source);

            for (int i = 0; i < BENCH_ITERATIONS; i++) {
                var orders = directAccessInput(i);
                var start = System.nanoTime();
                var result = function.execute((Object) orders).asDouble();
                var end = System.nanoTime();
                if (i == (BENCH_ITERATIONS - 1))
					System.out.printf("DirectAccess [%d -> %f] took %d ns%n", i, result, (end - start));
            }
        }
    }

    
    
    
    
    
    
    public static class OrderProxy implements ProxyObject {
        public HashMap<String, Object> fields;
        public String[] keys;

        public OrderProxy(double price, int quantity) {
            this.fields = new HashMap<String, Object>() {{
            	put("price", price);
            	put("quantity", quantity);
            }};
            this.keys = fields.keySet().toArray(new String[0]);
        }

        @Override
        public Object getMember(final String key) {
            return fields.get(key);
        }

        @Override
        public Object getMemberKeys() {
            return keys;
        }

        @Override
        public boolean hasMember(final String key) {
            return fields.containsKey(key);
        }

        @Override
        public void putMember(final String key, final Value value) {
            throw new UnsupportedOperationException("OrderProxy is immutable");
        }
    }

    private static OrderProxy[] proxyAccessInput(final long seed) {
        var random = new Random(seed);
        var orders = new OrderProxy[ORDER_SIZE];
        for (int i = 0; i < orders.length; i++) {
            orders[i] = new OrderProxy(random.nextInt(1000) + random.nextDouble(), random.nextInt(5000));
        }
        return orders;
    }

    public static void proxyAccess() {
        final String content = """
            (function sum_proxy(orders) {
                let sum = 0;
                for (var i=0; i<orders.length; i++) {
                  let order = orders[i];
                  sum += order.price * order.quantity;
                }
                return sum;
            })
            """;

        final Engine engine = Engine.newBuilder("js")
                .allowExperimentalOptions(true)
                .option("engine.DynamicCompilationThresholds", "false")
                .option("engine.BackgroundCompilation", "false")
                .option("engine.OSR", "false")
                .build();

        var source = Source.newBuilder("js", content, "sum_proxy.js").buildLiteral();

        try (var context = Context.newBuilder("js").engine(engine).allowAllAccess(true).build()) {
            var function = context.eval(source);

            for (int i = 0; i < BENCH_ITERATIONS; i++) {
                var orders = proxyAccessInput(i);
                var start = System.nanoTime();
                var result = function.execute((Object) orders).asDouble();
                var end = System.nanoTime();
                if (i == (BENCH_ITERATIONS - 1))
					System.out.printf("ProxyAccess [%d -> %f] took %d ns%n", i, result, (end - start));
            }
        }
    }

    
    
    
    
    
	private static HashMap[] proxyMapAccessInput(final long seed) {
        var random = new Random(seed);
        HashMap[] orders = new HashMap[ORDER_SIZE];
        for (int i = 0; i < orders.length; i++) {
            orders[i] = new HashMap<String, Object>() {{
            	put("price", random.nextInt(1000) + random.nextDouble());
            	put("quantity", random.nextInt(5000));
            }};
        }
        return orders;
    }

    public static void proxyMapAccess() {
        final String content = """
            (function sum_proxy(orders) {
                let sum = 0;
                for (var i=0; i<orders.length; i++) {
                  let order = orders[i];
                  sum += order.price * order.quantity;
                }
                return sum;
            })
            """;

        final Engine engine = Engine.newBuilder("js")
                .allowExperimentalOptions(true)
                .option("engine.DynamicCompilationThresholds", "false")
                .option("engine.BackgroundCompilation", "false")
                .option("engine.OSR", "false")
                .build();

        var source = Source.newBuilder("js", content, "sum_proxy.js").buildLiteral();

        try (var context = Context.newBuilder("js").engine(engine).allowAllAccess(true).build()) {
            var function = context.eval(source);

            for (int i = 0; i < BENCH_ITERATIONS; i++) {
                var orders = proxyMapAccessInput(i);
                var start = System.nanoTime();
                var result = function.execute((Object) orders).asDouble();
                var end = System.nanoTime();
                if (i == (BENCH_ITERATIONS - 1))
					System.out.printf("HashMapAccess [%d -> %f] took %d ns%n", i, result, (end - start));
            }
        }
    }
    
    
    
    
    
    static class CustomProxyHashMap implements ProxyHashMap {
        private final HashMap<String, Object> hash;

        CustomProxyHashMap(HashMap<String, Object> backingMap) {
            this.hash = backingMap;
        }

        @Override
        public long getHashSize() {
            return hash.size();
        }

        @Override
        public boolean hasHashEntry(Value key) {
            return false;
        }

        @Override
        public Object getHashValue(Value key) {
            return 1;
        }

        @Override
        public Object getHashEntriesIterator() {
            return null;
        }

        @Override
        public void putHashEntry(Value key, Value value) {
            throw new UnsupportedOperationException("putHashEntry() not supported.");
        }
    } 
    
     private static CustomProxyHashMap[] proxyCustomMapAccessInput(final long seed) {
        var random = new Random(seed);
        var orders = new CustomProxyHashMap[ORDER_SIZE];
        for (int i = 0; i < orders.length; i++) {
        	double price = random.nextInt(1000) + random.nextDouble();
        	int quantity = random.nextInt(5000);
        	HashMap<String, Object> entry =  new HashMap<String, Object>() {{
            	put("price", price);
            	put("quantity", quantity);
            }};
            orders[i] = new CustomProxyHashMap(entry);
        }
        return orders;
    }

    public static void proxyCustomMapAccess() {
        final String content = """
            (function sum_extended_map_proxy(orders) {
                let sum = 0;
                for (var i=0; i<orders.length; i++) {
                  let order = orders[i];
                  sum += order.price * order.quantity;
                }
                return sum;
            })
            """;

        final Engine engine = Engine.newBuilder("js")
                .allowExperimentalOptions(true)
                .option("engine.DynamicCompilationThresholds", "false")
                .option("engine.BackgroundCompilation", "false")
                .option("engine.OSR", "false")
                .build();

        var source = Source.newBuilder("js", content, "sum_proxy.js").buildLiteral();

        try (var context = Context.newBuilder("js").engine(engine).allowAllAccess(true).build()) {
            var function = context.eval(source);

            for (int i = 0; i < BENCH_ITERATIONS; i++) {
                var orders = proxyCustomMapAccessInput(i);
                var start = System.nanoTime();
                var result = function.execute((Object) orders).asDouble();
                var end = System.nanoTime();
                if (i == (BENCH_ITERATIONS - 1))
					System.out.printf("ProxyCustomMapAccess [%d -> %f] took %d ns%n", i, result, (end - start));
            }
        }
    }   

    
    
    
    
    
    
    static class ExtendedHashMap<K, V> extends HashMap<K, V> {
        public ExtendedHashMap() {
            super();
        }

        public ExtendedHashMap(int initialCapacity) {
            super(initialCapacity);
        }

        public ExtendedHashMap(int initialCapacity, float loadFactor) {
            super(initialCapacity, loadFactor);
        }

        public ExtendedHashMap(Map<? extends K, ? extends V> m) {
            super(m);
        }

        @Override
        public V put(K key, V value) {
            return super.put(key, value);
        }

        @Override
        public V get(Object key) {
            return super.get(key);
        }

        @Override
        public V remove(Object key) {
            return super.remove(key);
        }

        @Override
        public void clear() {
            super.clear();
        }

        @Override
        public boolean containsKey(Object key) {
            return super.containsKey(key);
        }

        @Override
        public boolean containsValue(Object value) {
            return super.containsValue(value);
        }

        @Override
        public Set<K> keySet() {
            return super.keySet();
        }

        @Override
        public Collection<V> values() {
            return super.values();
        }

        @Override
        public Set<Entry<K, V>> entrySet() {
            return super.entrySet();
        }

        @Override
        public int size() {
            return super.size();
        }

        @Override
        public boolean isEmpty() {
            return super.isEmpty();
        }

        @Override
        public void putAll(Map<? extends K, ? extends V> m) {
            super.putAll(m);
        }
    }
    
     private static ExtendedHashMap[] extendedHashAccessInput(final long seed) {
        var random = new Random(seed);
        var orders = new ExtendedHashMap[ORDER_SIZE];
        for (int i = 0; i < orders.length; i++) {
        	double price = random.nextInt(1000) + random.nextDouble();
        	int quantity = random.nextInt(5000);
        	ExtendedHashMap<String, Object> entry =  new ExtendedHashMap<String, Object>() {{
            	put("price", price);
            	put("quantity", quantity);
            }};
            orders[i] = new ExtendedHashMap(entry);
        }
        return orders;
    }

    public static void extendedHashMapAccess() {
        final String content = """
            (function sum_hashmap_extended(orders) {
                let sum = 0, order = null;
                for (var i=0; i<orders.length; i++) {
                  order = orders[i];
                  sum += order.price * order.quantity;
                }
                return sum;
            })
            """;

        final Engine engine = Engine.newBuilder("js")
                .allowExperimentalOptions(true)
                .option("engine.DynamicCompilationThresholds", "false")
                .option("engine.BackgroundCompilation", "false")
                .option("engine.OSR", "false")
                .build();

        var source = Source.newBuilder("js", content, "sum_proxy.js").buildLiteral();

        try (var context = Context.newBuilder("js").engine(engine).allowAllAccess(true).build()) {
            var function = context.eval(source);

            for (int i = 0; i < BENCH_ITERATIONS; i++) {
                var orders = extendedHashAccessInput(i);
                var start = System.nanoTime();
                var result = function.execute((Object) orders).asDouble();
                var end = System.nanoTime();
                if (i == (BENCH_ITERATIONS - 1))
					System.out.printf("ExtendedHashMapAccess [%d -> %f] took %d ns%n", i, result, (end - start));
            }
        }
    }   
    
    
    
    
    

    public static void main(String[] args) throws IOException {
        proxyAccess();
        directAccess();
        proxyMapAccess();
        //proxyCustomMapAccess();
        extendedHashMapAccess();
    }
}