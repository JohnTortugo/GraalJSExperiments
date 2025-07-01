#!/bin/bash

JAVA_PATH="/Users/dcsl/wf/GraalJSExperiments/graal-ws/graal/sdk/mxbuild/darwin-aarch64/GRAALJDK_CE_1602C36D2E_JAVA25/graaljdk-ce-1602c36d2e-java25-25.0.0-dev/Contents/Home/bin/"
EXPERIMENTATION_JAR_PATH="/Users/dcsl/wf/GraalJSExperiments/experimentation/target/experimentation-1.0-SNAPSHOT.jar"

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
    --add-exports=java.base/jdk.internal.misc=jdk.graal.compiler                        \
    -XX:CompileCommand='exclude,*ProxyFieldAccess::singleFieldProxyAccess'              \
    -XX:CompileCommand='exclude,*ProxyFieldAccess::run_it'                              \
    -XX:CompileCommand='dontinline,*ProxyFieldAccess::run_it'                           \
    -Dgraalvm.locatorDisabled=true                                                      \
    @module-path.txt                                                                    \
    -Xms8g -Xmx8g -XX:ReservedCodeCacheSize=1g                                          \
    --enable-native-access=org.graalvm.truffle                                          \
    --sun-misc-unsafe-memory-access=allow                                               \
    -cp ${EXPERIMENTATION_JAR_PATH}                                                     \
    com.jtortugo.proxies.ProxyFieldAccess

