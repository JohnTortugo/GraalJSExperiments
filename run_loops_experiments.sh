#!/bin/bash

JAVAHOME="/Users/dcsl/wf/graal-property-strings/graal/sdk/mxbuild/darwin-aarch64/GRAALVM_LIBGRAAL_JAVA25/graalvm-libgraal-openjdk-25+37.1/Contents/Home"

[[ $2 == *I* ]] && IGV="-Djdk.graal.Dump=:3 -Djdk.graal.MethodFilter=forof -Djdk.graal.PrintGraph=Network"
[[ $2 == *D* ]] && DEBUG="-agentlib:jdwp=transport=dt_socket,server=y,address=8000,suspend=y"
[[ $2 == *P* ]] && PROF="-agentpath:/Users/dcsl/wf/tools/async-profiler-4.1-macos/lib/libasyncProfiler.dylib=start,event=cpu,cstack=vm,ann,file=profile.html"


${JAVAHOME}/bin/java                                                                        \
    -server                                                                                 \
    -XX:+UnlockExperimentalVMOptions                                                        \
    -XX:+UnlockDiagnosticVMOptions -XX:+DebugNonSafepoints                                  \
    -XX:+PreserveFramePointer                                                               \
    -XX:+EnableJVMCI                                                                        \
    ${DEBUG}                                                                               \
    ${IGV}                                                                                 \
    ${PROF}                                                                                \
    --add-exports=java.base/jdk.internal.misc=jdk.graal.compiler                            \
    @locally-built-graal-jars.txt                                                           \
    --add-modules org.graalvm.shadowed.icu4j                                                \
    --enable-native-access=org.graalvm.truffle                                              \
    -Xms2g -Xmx2g -Xss24m                                                                   \
    --sun-misc-unsafe-memory-access=allow                                                   \
    -cp ~/wf/GraalJSExperiments/experimentation/target/experimentation-1.0-SNAPSHOT.jar     \
    com.jtortugo.graaljs.BenchIt $1
