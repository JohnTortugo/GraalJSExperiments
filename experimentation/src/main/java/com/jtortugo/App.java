package com.jtortugo;

import org.graalvm.polyglot.*;
import org.graalvm.polyglot.proxy.*;

public class App {

    static String JS_CODE = "(function myFun(param){console.log('Hello ' + param + ' from JS');})";

    public static void main(String[] args) {
        String who = args.length == 0 ? "World" : args[0];
        System.out.println("Hello " + who + " from Java");
        try (Context context = Context.create()) {
            Value value = context.eval("js", JS_CODE);
            value.execute(who);
        }
    }
}

