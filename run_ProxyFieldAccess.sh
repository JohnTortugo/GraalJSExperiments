#!/bin/bash

JAVAHOME="/Users/dcsl/wf/graal-master/graal/sdk/latest_graalvm_home/"
EXPERIMENTATION_JAR_PATH="/Users/dcsl/wf/GraalJSExperiments/experimentation/target/experimentation-1.0-SNAPSHOT.jar"

[[ $2 == *I* ]] && IGV="-Djdk.graal.Dump=:3 -Djdk.graal.MethodFilter=for_multiply_add -Djdk.graal.PrintGraph=Network"
[[ $2 == *D* ]] && DEBUG="-agentlib:jdwp=transport=dt_socket,server=y,address=8000,suspend=y"
[[ $2 == *P* ]] && PROF="-agentpath:/Users/dcsl/wf/tools/async-profiler-4.1-macos/lib/libasyncProfiler.dylib=start,event=cpu,cstack=vm,ann,file=profile.html"

# Some clean up
rm -rf *.log

# Build the package
pushd experimentation/
mvn clean package
popd

${JAVAHOME}/bin/java                                                                        \
    -server                                                                                 \
    -XX:+UnlockExperimentalVMOptions                                                        \
    -XX:+UnlockDiagnosticVMOptions -XX:+DebugNonSafepoints                                  \
    -XX:+PreserveFramePointer                                                               \
    -XX:+EnableJVMCI                                                                        \
    ${DEBUG}                                                                               \
    ${IGV}                                                                                 \
    ${PROF}                                                                                \
    @locally-built-graal-jars.txt                                                           \
    --add-modules org.graalvm.shadowed.icu4j                                                \
    --enable-native-access=org.graalvm.truffle                                              \
    -Xms2g -Xmx2g -Xss24m                                                                   \
    --sun-misc-unsafe-memory-access=allow                                                   \
    -cp ~/wf/GraalJSExperiments/experimentation/target/experimentation-1.0-SNAPSHOT.jar     \
    com.jtortugo.proxies.ProxyFieldAccess $1
