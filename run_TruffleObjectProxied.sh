#!/bin/bash

JAVA_PATH="/Users/dcsl/wf/GraalJSExperiments/graal-ws/graal/sdk/mxbuild/darwin-aarch64/GRAALJDK_CE_1602C36D2E_JAVA25/graaljdk-ce-1602c36d2e-java25-25.0.0-dev/Contents/Home/bin/"
EXPERIMENTATION_JAR_PATH="/Users/dcsl/wf/GraalJSExperiments/experimentation/target/experimentation-1.0-SNAPSHOT.jar"

IGV_DUMP="-Djdk.graal.Dump=:3 -Djdk.graal.MethodFilter=*read_after_read* -Djdk.graal.PrintGraph=Network"
JVM_DEBUG="-XX:CompileCommand=print,com.oracle.truffle.runtime.OptimizedCallTarget::profiledPERoot -Xcomp -XX:CompileCommand='compileonly,*ProxyFieldAccess::singleFieldProxyAccess*' -XX:CompileCommand='print,*ProxyFieldAccess::singleFieldProxyAccess*'"
JAVA_DEBUG="-agentlib:jdwp=transport=dt_socket,server=y,address=8000,suspend=y"

# Some clean up
rm -rf *.log

# Build the package
pushd experimentation/
mvn clean package
popd

# Run it
${JAVA_PATH}/java                                                                       \
    -XX:+UnlockExperimentalVMOptions                                                    \
    -XX:+UnlockDiagnosticVMOptions                                                      \
    -server                                                                             \
    -XX:+EnableJVMCI                                                                    \
    ${JVM_DEBUG_}                                                                       \
    ${JAVA_DEBUG_}                                                                      \
    ${IGV_DUMP_}                                                                        \
    --add-exports=java.base/jdk.internal.misc=jdk.graal.compiler                        \
    -Dgraalvm.locatorDisabled=true                                                      \
    @module-path.txt                                                                    \
    -Xms8g -Xmx8g -XX:ReservedCodeCacheSize=256m                                        \
    --enable-native-access=org.graalvm.truffle                                          \
    --sun-misc-unsafe-memory-access=allow                                               \
    -cp ${EXPERIMENTATION_JAR_PATH}                                                     \
    com.jtortugo.proxies.TruffleObjectProxied

