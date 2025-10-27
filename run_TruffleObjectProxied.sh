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
#${JAVA_PATH}/

~/wf/tools/jdk-20.0.2+9/Contents/Home/bin/java                                                                       \
    -XX:+UnlockExperimentalVMOptions                                                    \
    -XX:+UnlockDiagnosticVMOptions                                                      \
    -ea                                                                                 \
    -esa                                                                                \
    -server                                                                             \
    -XX:+EnableJVMCI                                                                    \
    ${JVM_DEBUG_}                                                                       \
    ${JAVA_DEBUG}                                                                       \
    ${IGV_DUMP_}                                                                        \
    @module-path.txt                                                                    \
    --add-modules org.graalvm.polyglot                                                  \
    --enable-native-access=org.graalvm.truffle.runtime                                  \
    --enable-native-access=org.graalvm.truffle                                          \
    -cp ${EXPERIMENTATION_JAR_PATH}                                                     \
    com.jtortugo.proxies.TruffleObjectProxied

