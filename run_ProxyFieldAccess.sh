#!/bin/bash

JAVA_PATH="/Users/dcsl/wf/graal-master/graal/sdk/latest_graalvm_home/bin/"
EXPERIMENTATION_JAR_PATH="/Users/dcsl/wf/GraalJSExperiments/experimentation/target/experimentation-1.0-SNAPSHOT.jar"

#-Djdk.graal.MethodFilter=*for_multiply_add*
IGV_DEBUG="-Djdk.graal.Dump=:3 -Djdk.graal.MethodFilter=*for_multiply* -Djdk.graal.PrintGraph=Network"
JVM_DEBUG="-XX:CompileCommand=print,*::*profiledPERoot*"
JAVA_DEBUG="-agentlib:jdwp=transport=dt_socket,server=y,address=8000,suspend=y"

# Some clean up
rm -rf *.log

# Build the package
pushd experimentation/
#mvn clean package
popd

# Run it
${JAVA_PATH}/java                                                                       \
    -XX:+UnlockExperimentalVMOptions                                                    \
    -XX:+UnlockDiagnosticVMOptions                                                      \
    -server                                                                             \
    -ea -esa \
    -XX:+EnableJVMCI                                                                    \
    -XX:-TraceDeoptimization                                                                    \
    ${JVM_DEBUG_}                                                                       \
    ${JAVA_DEBUG_}                                                                      \
    ${IGV_DEBUG}                                                                        \
    --add-exports=java.base/jdk.internal.misc=jdk.graal.compiler                        \
    -Dgraalvm.locatorDisabled=true                                                      \
    @locally-built-graal-jars.txt                                                                    \
    -Xms8g -Xmx8g -XX:ReservedCodeCacheSize=256m                                        \
    --enable-native-access=org.graalvm.truffle                                          \
    --sun-misc-unsafe-memory-access=allow                                               \
    -cp ${EXPERIMENTATION_JAR_PATH}                                                     \
    com.jtortugo.proxies.ProxyFieldAccess

