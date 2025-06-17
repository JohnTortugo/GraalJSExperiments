# GraalJS Experiments

###### Building the Application

I had a problem where executing `mvn clean package` was not copying the dependencies to `target/modules` as described in the documentation. The fix for that was removing the `<pluginManagement>...</pluginManagement>` scope surrounding the plugins configuration.

Now I'm able to run the program using this command: `java --module-path target/modules --add-modules=org.graalvm.polyglot -cp target/experimentation-1.0-SNAPSHOT.jar com.jtortugo.App yeah` . However, I do get a bunch of warnings:

```bash
Hello Cesar from Java
WARNING: A restricted method in java.lang.System has been called
WARNING: java.lang.System::load has been called by com.oracle.truffle.runtime.ModulesSupport in module org.graalvm.truffle.runtime (file:/Users/dcsl/wf/GraalJSExperiments/experimentation/target/modules/truffle-runtime-24.1.0.jar)
WARNING: Use --enable-native-access=org.graalvm.truffle.runtime to avoid a warning for callers in this module
WARNING: Restricted methods will be blocked in a future release unless native access is enabled

WARNING: A terminally deprecated method in sun.misc.Unsafe has been called
WARNING: sun.misc.Unsafe::objectFieldOffset has been called by com.oracle.truffle.api.dsl.InlineSupport$UnsafeField (file:/Users/dcsl/wf/GraalJSExperiments/experimentation/target/modules/truffle-api-24.1.0.jar)
WARNING: Please consider reporting this to the maintainers of class com.oracle.truffle.api.dsl.InlineSupport$UnsafeField
WARNING: sun.misc.Unsafe::objectFieldOffset will be removed in a future release
[To redirect Truffle log output to a file use one of the following options:
* '--log.file=<path>' if the option is passed using a guest language launcher.
* '-Dpolyglot.log.file=<path>' if the option is passed using the host Java launcher.
* Configure logging using the polyglot embedding API.]
[engine] WARNING: The polyglot engine uses a fallback runtime that does not support runtime compilation to native code.
Execution without runtime compilation will negatively impact the guest application performance.
The following cause was found: JVMCI is not enabled for this JVM. Enable JVMCI using -XX:+EnableJVMCI.
For more information see: https://www.graalvm.org/latest/reference-manual/embed-languages/#runtime-optimization-support.
To disable this warning use the '--engine.WarnInterpreterOnly=false' option or the '-Dpolyglot.engine.WarnInterpreterOnly=false' system property.
Hello Cesar from JS
```

The Java that I'm using in this case is `OpenJDK 64-Bit Server VM Temurin-25+19-202504172033 (build 25-beta+19-ea, mixed mode, sharing)`. If I change the invocation by adding `-XX:+UnlockExperimentalVMOptions -XX:+EnableJVMCI` I instead get a VM crash:

```
Exception in thread "main" java.lang.NoSuchFieldError: Class jdk.vm.ci.services.Services does not have member field 'boolean IS_BUILDING_NATIVE_IMAGE'
	at org.graalvm.truffle.runtime/com.oracle.truffle.runtime.hotspot.libgraal.LibGraal.<clinit>(LibGraal.java:222)
	at org.graalvm.truffle.runtime/com.oracle.truffle.runtime.hotspot.HotSpotTruffleRuntimeAccess.createRuntime(HotSpotTruffleRuntimeAccess.java:147)
	at org.graalvm.truffle.runtime/com.oracle.truffle.runtime.hotspot.HotSpotTruffleRuntimeAccess.getRuntime(HotSpotTruffleRuntimeAccess.java:75)
	at org.graalvm.truffle/com.oracle.truffle.api.Truffle.createRuntime(Truffle.java:145)
	at org.graalvm.truffle/com.oracle.truffle.api.Truffle$1.run(Truffle.java:176)
	at org.graalvm.truffle/com.oracle.truffle.api.Truffle$1.run(Truffle.java:174)
	at java.base/java.security.AccessController.doPrivileged(AccessController.java:74)
	at org.graalvm.truffle/com.oracle.truffle.api.Truffle.initRuntime(Truffle.java:174)
	at org.graalvm.truffle/com.oracle.truffle.api.Truffle.<clinit>(Truffle.java:63)
	at com.oracle.truffle.enterprise/com.oracle.truffle.runtime.enterprise.EnterpriseTruffle.supportsEnterpriseExtensions(stripped:22)
	at com.oracle.truffle.enterprise/com.oracle.truffle.polyglot.enterprise.EnterprisePolyglotImpl.getPriority(stripped:551)
	at java.base/java.util.Comparator.lambda$comparing$77a9974f$1(Comparator.java:473)
	at java.base/java.util.TimSort.countRunAndMakeAscending(TimSort.java:355)
	at java.base/java.util.TimSort.sort(TimSort.java:220)
	at java.base/java.util.Arrays.sort(Arrays.java:1304)
	at java.base/java.util.ArrayList.sortRange(ArrayList.java:1817)
	at java.base/java.util.ArrayList.sort(ArrayList.java:1810)
	at java.base/java.util.Collections.sort(Collections.java:178)
	at org.graalvm.polyglot/org.graalvm.polyglot.Engine.loadAndValidateProviders(Engine.java:1641)
	at org.graalvm.polyglot/org.graalvm.polyglot.Engine$1.run(Engine.java:1717)
	at org.graalvm.polyglot/org.graalvm.polyglot.Engine$1.run(Engine.java:1712)
	at java.base/java.security.AccessController.doPrivileged(AccessController.java:74)
	at org.graalvm.polyglot/org.graalvm.polyglot.Engine.initEngineImpl(Engine.java:1712)
	at org.graalvm.polyglot/org.graalvm.polyglot.Engine$ImplHolder.<clinit>(Engine.java:170)
	at org.graalvm.polyglot/org.graalvm.polyglot.Engine.getImpl(Engine.java:422)
	at org.graalvm.polyglot/org.graalvm.polyglot.Engine$Builder.build(Engine.java:724)
	at org.graalvm.polyglot/org.graalvm.polyglot.Context$Builder.build(Context.java:1925)
	at org.graalvm.polyglot/org.graalvm.polyglot.Context.create(Context.java:979)
	at com.jtortugo.App.main(App.java:13)
```

If I instead use this VM `~/wf/tools/labsjdk-ce-25-jvmci-b01/Contents/Home/bin/java` I don't get a crash. 

###### Running GraalJS

As far as I could see by investigating the source code there is no way to use mx to execute a `.jar` with an embedded JS script. I.e., something like this is not possible: `mx js my-app.jar` . To workaround that I ran `mx -v --dy /compiler js -version` to get the configuration being used to run the JSLauncher application and then I replaced the launcher with my own application. Below is the full command line that works for running the application or that I can use for setting up in a debugger, which is actually my main goal.

```
/Users/dcsl/wf/GraalJSExperiments/graal-ws/graal/sdk/mxbuild/darwin-aarch64/GRAALJDK_CE_1602C36D2E_JAVA25/graaljdk-ce-1602c36d2e-java25-25.0.0-dev/Contents/Home/bin/java -server -XX:+BackgroundCompilation -XX:+UnlockExperimentalVMOptions -XX:+EnableJVMCI --add-exports=java.base/jdk.internal.misc=jdk.graal.compiler -Djdk.graal.CompilationFailureAction=Diagnose -Djdk.graal.DumpOnError=true -Djdk.graal.ShowDumpFiles=true -Djdk.graal.PrintGraph=Network -Djdk.graal.ObjdumpExecutables=objdump,gobjdump -Dgraalvm.locatorDisabled=true --module-path=/Users/dcsl/wf/GraalJSExperiments/graal-ws/graaljs/graal-js/mxbuild/jdk25/dists/jdk17/graaljs-launcher.jar:/Users/dcsl/wf/GraalJSExperiments/graal-ws/graal/sdk/mxbuild/jdk25/dists/jdk17/collections.jar:/Users/dcsl/wf/GraalJSExperiments/graal-ws/graal/sdk/mxbuild/jdk25/dists/jdk11/word.jar:/Users/dcsl/wf/GraalJSExperiments/graal-ws/graal/sdk/mxbuild/jdk25/dists/jdk11/nativeimage.jar:/Users/dcsl/wf/GraalJSExperiments/graal-ws/graal/sdk/mxbuild/jdk25/dists/jdk17/polyglot.jar:/Users/dcsl/wf/GraalJSExperiments/graal-ws/graal/sdk/mxbuild/jdk25/dists/jdk17/jline3.jar:/Users/dcsl/wf/GraalJSExperiments/graal-ws/graal/sdk/mxbuild/jdk25/dists/jdk17/launcher-common.jar:/Users/dcsl/wf/GraalJSExperiments/graal-ws/graal/truffle/mxbuild/jdk25/dists/jdk21/truffle-api.jar:/Users/dcsl/wf/GraalJSExperiments/graal-ws/graaljs/graal-js/mxbuild/jdk25/dists/jdk17/graaljs.jar:/Users/dcsl/wf/GraalJSExperiments/graal-ws/graal/truffle/mxbuild/jdk25/dists/jdk17/truffle-xz.jar:/Users/dcsl/wf/GraalJSExperiments/graal-ws/graal/truffle/mxbuild/jdk25/dists/jdk17/truffle-icu4j.jar:/Users/dcsl/wf/GraalJSExperiments/graal-ws/graal/regex/mxbuild/jdk25/dists/jdk17/tregex.jar:/Users/dcsl/wf/GraalJSExperiments/graal-ws/graal/truffle/mxbuild/jdk25/dists/jdk17/truffle-runtime.jar:/Users/dcsl/wf/GraalJSExperiments/graal-ws/graal/sdk/mxbuild/jdk25/dists/jdk17/jniutils.jar:/Users/dcsl/wf/GraalJSExperiments/graal-ws/graal/truffle/mxbuild/jdk25/dists/jdk17/truffle-compiler.jar -Xms2g -Xmx2g -Xss24m --enable-native-access=org.graalvm.truffle --sun-misc-unsafe-memory-access=allow -cp ~/wf/GraalJSExperiments/experimentation/target/experimentation-1.0-SNAPSHOT.jar com.jtortugo.App Cesar

```

###### Debugging GraalJS on Eclipse

First step was to run `mx eclipseinit` in the graal-js folder, then I configured eclipse by adding my current JAVA_HOME as a installed JRE and also a JDK22 as an additional JRE (it was suggested by a message printed by `mx eclipseinit`). Next step was improting all project in Eclipse as described by output of `eclipseinit`.

Adding the `-d` option to mx makes it launch the VM in debug mode, essentially it will include these arguments in the options to launch the VM `-agentlib:jdwp=transport=dt_socket,server=y,address=8000,suspend=y`. I modified the command line from the previous section by adding this option.

If I run the code below the script will be parsed but not executed, however I can already start inspecting the AST.

```java
try (Context context = Context.create()) {
    context.eval("js", JS_CODE);

    Value jsBindings = context.getBindings("js");
    Value myFunc = jsBindings.getMember("myFunc");

    //assert myFunc.canExecute();
    //myFunc.execute();
}
```

The first place where the execution stops is here:

```
Thread [main] (Suspended)	
	GraalJSEvaluator.parseScript(JSContext, Source, String, String, boolean, List<String>) line: 254	
	JavaScriptLanguage.parseScript(JSContext, Source, String, String, boolean, List<String>) line: 319	
	JavaScriptLanguage.parse(TruffleLanguage$ParsingRequest) line: 221	
	TruffleLanguage$ParsingRequest.parse(TruffleLanguage<?>) line: 1204	
	JavaScriptLanguage(TruffleLanguage<C>).parse(Source, OptionValues, String...) line: 1618	
	LanguageAccessor$LanguageImpl.parse(TruffleLanguage$Env, Source, OptionValues, Node, String...) line: 298	
	PolyglotSourceCache.parseImpl(PolyglotSourceCache$ParseOrigin, PolyglotLanguageContext, String[], Source) line: 118	
	PolyglotSourceCache$WeakCache.lookup(PolyglotSourceCache$ParseOrigin, PolyglotLanguageContext, Source, String[], boolean) line: 367	
	PolyglotSourceCache.parseCached(PolyglotSourceCache$ParseOrigin, PolyglotLanguageContext, Source, String[]) line: 93	
	PolyglotLanguageContext.parseCached(PolyglotSourceCache$ParseOrigin, PolyglotLanguage, Source, String[]) line: 375	
	PolyglotContextImpl.eval(String, Object) line: 1894	
	PolyglotContextDispatch.eval(Object, String, Object) line: 62	
	Context.eval(Source) line: 418	
	Context.eval(String, CharSequence) line: 448	
	App.main(String[]) line: 10	

```

Next interesting stop is shown in the stack trace below. The method returns a `/com.oracle.js.parser/src/com/oracle/js/parser/ir/FunctionNode.java` which regarless being under the "IR" package is actually a node in the "AST" as we can confirm by checking it's top-most base class.

```
Thread [main] (Suspended)	
	GraalJSParserHelper$1(Parser).parse(TruffleString, int, int, int, Scope, List<String>) line: 542	
	GraalJSParserHelper$1(Parser).parse() line: 453	
	GraalJSParserHelper.parseSource(JSContext, Source, JSParserOptions, boolean, boolean, boolean, Scope, String, String, List<String>) line: 131	
	GraalJSParserHelper.parseScript(JSContext, Source, JSParserOptions, boolean, boolean, Scope, String, String, List<String>) line: 90	
	JavaScriptTranslator.translateScript(NodeFactory, JSContext, Environment, Source, boolean, boolean, boolean, DirectEvalContext, String, String, List<String>, ScriptOrModule) line: 96	
	JavaScriptTranslator.translateScript(NodeFactory, JSContext, Source, boolean, String, String, List<String>) line: 78	
	GraalJSEvaluator.parseScript(JSContext, Source, String, String, boolean, List<String>) line: 254	
	JavaScriptLanguage.parseScript(JSContext, Source, String, String, boolean, List<String>) line: 319	
	JavaScriptLanguage.parse(TruffleLanguage$ParsingRequest) line: 221	
	TruffleLanguage$ParsingRequest.parse(TruffleLanguage<?>) line: 1204	
	JavaScriptLanguage(TruffleLanguage<C>).parse(Source, OptionValues, String...) line: 1618	
	LanguageAccessor$LanguageImpl.parse(TruffleLanguage$Env, Source, OptionValues, Node, String...) line: 298	
	PolyglotSourceCache.parseImpl(PolyglotSourceCache$ParseOrigin, PolyglotLanguageContext, String[], Source) line: 118	
	PolyglotSourceCache$WeakCache.lookup(PolyglotSourceCache$ParseOrigin, PolyglotLanguageContext, Source, String[], boolean) line: 367	
	PolyglotSourceCache.parseCached(PolyglotSourceCache$ParseOrigin, PolyglotLanguageContext, Source, String[]) line: 93	
	PolyglotLanguageContext.parseCached(PolyglotSourceCache$ParseOrigin, PolyglotLanguage, Source, String[]) line: 375	
	PolyglotContextImpl.eval(String, Object) line: 1894	
	PolyglotContextDispatch.eval(Object, String, Object) line: 62	
	Context.eval(Source) line: 418	
	Context.eval(String, CharSequence) line: 448	
	App.main(String[]) line: 10	


```





#### Phases Hierarchy

TruffleTier
    AgnosticInliningPhase
    InstrumentationSuite
        InstrumentBranchesPhase
        InstrumentTruffleBoundariesPhase
    ReportPerformanceWarningsPhase
    VerifyFrameDoesNotEscapePhase
    NeverPartOfCompilationPhase
    SetIdentityForValueTypesPhase
    ReplaceanyExtendNodePhase
    InliningAcrossTruffleBoundaryPhase

PostPartialEvaluationSuite
	ConvertDeoptimizeToGuardPhase
	InlineReplacementsPhase
	CanonicalizerPhase
	ConditionalEliminationPhase
	FrameAccessVerificationPhase
	PartialEscapePhase
	PhiTransformPhase

| Community High Tier Phases              | Economy High Tier Phases |
| --------------------------------------- | ------------------------ |
| CanonicalizerPhase                      | CanonicalizerPhase       |
| InliningPhase                           |                          |
| DeadCodeEliminationPhase                |                          |
| DisableOverflownCountedLoopsPhase       |                          |
| ConvertDeoptimizeToGuardPhase           |                          |
| IterativeConditionalEliminationPhase    |                          |
| DominatorBasedGlobalValueNumberingPhase |                          |
| LoopFullUnrollPhase                     |                          |
| LoopPeelingPhase                        |                          |
| LoopUnswitchingPhase                    |                          |
| BoxNodeIdentityPhase                    |                          |
| FinalPartialEscapePhase                 |                          |
| ReadEliminationPhase                    |                          |
| BoxNodeOptimizationPhase                |                          |
| HighTierLoweringPhase                   | HighTierLoweringPhase    |



| Community Mid Tier Phases            | Economy Mid Tier Phases     |
| ------------------------------------ | --------------------------- |
| LockEliminationPhase                 |                             |
| FloatingReadPhase                    |                             |
| IterativeConditionalEliminationPhase |                             |
| LoopPredicationPhase                 |                             |
| LoopSafepointEliminationPhase        |                             |
| SpeculativeGuardMovementPhase        |                             |
| GuardLoweringPhase                   | GuardLoweringPhase          |
| InsertGuardFencesPhase               |                             |
| VerifyHeapAtReturnPhase              |                             |
| LoopFullUnrollPhase                  |                             |
| RemoveValueProxyPhase                | RemoveValueProxyPhase       |
| LoopSafepointInsertionPhase          | LoopSafepointInsertionPhase |
| MidTierLoweringPhase                 | MidTierLoweringPhase        |
| IterativeConditionalEliminationPhase |                             |
| OptimizeDivPhase                     |                             |
| FrameStateAssignmentPhase            | FrameStateAssignmentPhase   |
| LoopPartialUnrollPhase               |                             |
| ReassociationPhase                   |                             |
| DeoptimizationGroupingPhase          |                             |
| CanonicalizerPhase                   | CanonicalizerPhase          |
| WriteBarrierAdditionPhase            | WriteBarrierAdditionPhase   |



| Community Low Tier Phases           | Economy Low Tier Phases     |
| ----------------------------------- | --------------------------- |
| ProfileCompiledMethodsPhase         |                             |
| InitMemoryVerificationPhase         | InitMemoryVerificationPhase |
| CanonicalizerWithGVNPhase           |                             |
|                                     | CanonicalizerPhase          |
| LowTierLoweringPhase                | LowTierLoweringPhase        |
|                                     | CanonicalizerPhase          |
| ExpandLogicPhase                    | ExpandLogicPhase            |
|                                     | BarrierSetVerificationPhase |
| OptimizeOffsetAddressPhase          |                             |
| SchedulePhase                       |                             |
| FixReadsPhase                       |                             |
| CanonicalizerWithoutGVNPhase        |                             |
| AddressLoweringPhase                | AddressLoweringPhase        |
| CanonicalizerWithoutGVN             |                             |
| FinalCanonicalizerPhase             |                             |
| DeadCodeEliminationPhase            |                             |
| PropagateDeoptimizeProbabilityPhase |                             |
| OptimizeExtendsPhase                |                             |
| RemoveOpaqueValuePhase              | RemoveOpaqueValuePhase      |
| FinalSchedulePhase                  | FinalSchedulePhase          |
| TransplantGraphsPhase               | TransplantGraphsPhase       |



### The GraalJS / Truffle Abstract Syntax Tree - Empty Function

I'm going to run the experimentation program with an empty JavaScript method, like so:

```javascript
function myFunc() { }
```

The `com.oracle.truffle.runtime.OptimizedCallTarget::callBoundary` is the boundary method, it is annotated with @TruffleBoundary, limiting the scope of the Partial Evaluator optimizations. In other words, and as commented in that method: "Note this method compiles without any inlining or other optimizations." That method basically has two main paths (1) it calls `doInvoke(args)` if `interpreterCall()` returns true or it calls `profiledPERoot(args)` if `interpreterCall()` returns false. In my debugging sessions I only see the path executing `profiledPERoot` being exercised. The `profiledPERoot` method seems to be, for all effects, the top-most method seens by the Partial Evaluator and the AST/IR graphs seen in IGV reflects that. The code of the method is this:

```java
// Note: {@code PartialEvaluator} looks up this method by name and signature.
protected final Object profiledPERoot(Object[] originalArguments) {
    Object[] args = originalArguments;
    if (!CompilerDirectives.inInterpreter() && CompilerDirectives.hasNextTier()) {
        firstTierCall();
    }
    args = injectArgumentsProfile(originalArguments);
    Object result = executeRootNode(createFrame(getRootNode().getFrameDescriptor(), args), getTier());
    profileReturnValue(result);
    return result;
}
```

In IGV we see that the method calls in this method are being inlined and they are part of the compilation unit for the JavaScript method being compiled:

![GraalJS-CallTrace](/Users/dcsl/wf/GraalJSExperiments/GraalJS-CallTrace.png)

In IGV the color of the edges represent they kind. Blue is for "Data" edges, Red is for "Control" edges and "Black" is for metadata or "State". The graph below is a simplified version of the one obtained in the "After PE Tier" phase when running GraalJS to execute an empty JavaScript method. You can see that the graph depicts the code in the `ProfiledPERoot` method - in this case most of it is from the `firstTierCall` method - and the method calls that follow from it. Note the `lastTierCompile` method invocation at the bottom, that method is annotated with `@TruffleBoundary` .  I imagine that the code following the last `merge` in the graph would be the AST for implementing the JS function, however, since in this case the function is empty then there is nothing interesting to look at here.

![GraalJS-EmptyFunc-Top](/Users/dcsl/wf/GraalJSExperiments/GraalJS-EmptyFunc-Top.png)

![GraalJS-EmptyFunc-Bottom](/Users/dcsl/wf/GraalJSExperiments/GraalJS-EmptyFunc-Bottom.png)

### The GraalJS / Truffle Abstract Syntax Tree - Classic For Loop

Now I'm doing experiments using a JavaScript function, shown below, that has a simple counted loop that increments a local variable by one in each iteration. The value of the local variable is later returned by the function.

```javascript
function myFunc() { 
  var res = 0;
  for (var i=0; i<100; i++) {
    res++; 
  }
  return res; 
}
```

To the best of my knowledge the image below is the AST representation of the code above. The tree, as expected, is a pretty direct representation of the code.

![Screenshot 2025-05-25 at 10.00.47 PM](/Users/dcsl/wf/GraalJSExperiments/Screenshot 2025-05-25 at 10.00.47 PM.png)







### Interesting Node Types



NarrowNode -> Converts an integer with N bits (e.g., 64) to a narrower integer with M bits (e.g., 32).
ConditionalNode -> Seems to be a node similar to an IF or an CMov, in the sense that it will return one of two values based on a condition.





#### Notes

- ":program" is the name for the entrypoint script global scope. See Parser.java::PROGRAM_NAME

- ":=>" function name for arrow functions.

- jdk.graal.compiler.phases.PhaseSuite: A compiler phase that can apply an ordered collection of phases to a graph.

- jdk.graal.compiler.phases.BasePhase: Base class for all compiler phases. 

- jdk.graal.compiler.core.phases.CEOptimization : This class enumerates the most important platform-independent optimizations in the GraalVM CE compiler. It contains summaries of the optimizations with links to their sources and the options to enable them. The linked sources typically contain detailed examples and motivation for the optimizations.
  - Canonicalization, Inlining,  DeadCodeElimination, DeoptimizeToGuard, ConditionalElimination, InstructionScheduling, FloatingReads, ReadElimination, PartialEscapeAnalysis, LockElimination, SafepointElimination, ExpressionReassociation, DeoptimizationGrouping, TrappingNullChecks, FullLoopUnrolling, SpeculativeGuardMovement, LoopPredication, LoopPeeling, LoopUnswitching, PartialLoopUnrolling, BoxNodeOptimization

- jdk.graal.compiler.core.phases.CommunityCompilerConfiguration: The default configuration for the community edition of Graal.

- jdk.graal.compiler.core.phases.EconomyCompilerConfiguration: A compiler configuration that performs fewer Graal IR optimizations while using the same backend as the CommunityCompilerConfiguration.

- Decision of which CompilerConfigurationFactory to use is implemented in `jdk.graal.compiler.hotspot.CompilerConfigurationFactory.selectFactory(...)`.

- Truffle Tier1 executes the economy configuration of Graal and Truffle Tier2 executes the default tier (i.e., CE in GraalCE).

  

