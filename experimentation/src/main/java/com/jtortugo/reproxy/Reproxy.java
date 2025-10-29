package com.jtortugo.reproxy;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;

public class Reproxy {

	public static void main(String[] args) throws StreamReadException, DatabindException, IOException {
		if (args.length < 1) {
			System.err.println("Please specify JSON file containing input objects.");
			System.exit(1);
		}

        File file = new File(args[0]);

        ObjectMapper mapper = JsonMapper.builder()
                .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                .build();

        // Parse as List of Map<String, Object>; Jackson handles nested Maps/Lists recursively.
        List<Map<String, Object>> rawList =
                mapper.readValue(file, new TypeReference<List<Map<String, Object>>>() {});

        // Wrap into InputObject
        //List<ProtonReadOnlyStructWrapper> inputObjects = new ArrayList<>(rawList.size());
        for (Map<String, Object> obj : rawList) {
        	printMap(obj, 4);
			System.out.println("\n");
            //inputObjects.add(new ProtonReadOnlyStructWrapper(new LinkedHashMap<>(obj)));
        }

        //// Demo print
        //for (ProtonReadOnlyStructWrapper io : inputObjects) {
        //    System.out.println(io);
        //}
	}
	
	
	public static void printMap(Object input, int indent) {
	    String pad = " ".repeat(indent);
	    if (input instanceof Map<?,?> entries) {
			for (Map.Entry<?, ?> entry : entries.entrySet()) {
				System.out.println(pad + entry.getKey() + ": ");
				printMap(entry.getValue(), indent + 2);
			}
	    } else if (input instanceof List entries) {
	    	for (var entry : entries) {
				printMap(entry, indent + 2);
			}
	    } else {
			System.out.println(pad + input);
	    }
	}
}
