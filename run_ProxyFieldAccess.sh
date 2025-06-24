#!/bin/bash

DUMP="-Djdk.graal.Dump=:3 -Djdk.graal.MethodFilter=*field* -Djdk.graal.PrintGraph=Network"
#JVM_DEBUG="-XX:CompileCommand=print,com.oracle.truffle.runtime.OptimizedCallTarget::profiledPERoot -Xcomp -XX:CompileCommand='compileonly,*ProxyFieldAccess::singleFieldProxyAccess*' -XX:CompileCommand='print,*ProxyFieldAccess::singleFieldProxyAccess*'"
DEBUG="-agentlib:jdwp=transport=dt_socket,server=y,address=8000,suspend=y"


pushd experimentation/
mvn clean package
popd


echo "Graal CE"
/Users/dcsl/wf/GraalJSExperiments/graal-ws/graal/sdk/mxbuild/darwin-aarch64/GRAALJDK_CE_1602C36D2E_JAVA25/graaljdk-ce-1602c36d2e-java25-25.0.0-dev/Contents/Home/bin/java \
    -server \
    -XX:+UnlockExperimentalVMOptions \
    -XX:+EnableJVMCI \
    --add-exports=java.base/jdk.internal.misc=jdk.graal.compiler \
    -XX:CompileCommand='exclude,*ProxyFieldAccess::singleFieldProxyAccess' \
    ${DEBUG}               \
    ${DUMPa}               \
    -Dgraalvm.locatorDisabled=true    \
    --module-path=/Users/dcsl/wf/GraalJSExperiments/graal-ws/graaljs/graal-js/mxbuild/jdk25/dists/jdk17/graaljs-launcher.jar:/Users/dcsl/wf/GraalJSExperiments/graal-ws/graal/sdk/mxbuild/jdk25/dists/jdk17/collections.jar:/Users/dcsl/wf/GraalJSExperiments/graal-ws/graal/sdk/mxbuild/jdk25/dists/jdk11/word.jar:/Users/dcsl/wf/GraalJSExperiments/graal-ws/graal/sdk/mxbuild/jdk25/dists/jdk11/nativeimage.jar:/Users/dcsl/wf/GraalJSExperiments/graal-ws/graal/sdk/mxbuild/jdk25/dists/jdk17/polyglot.jar:/Users/dcsl/wf/GraalJSExperiments/graal-ws/graal/sdk/mxbuild/jdk25/dists/jdk17/jline3.jar:/Users/dcsl/wf/GraalJSExperiments/graal-ws/graal/sdk/mxbuild/jdk25/dists/jdk17/launcher-common.jar:/Users/dcsl/wf/GraalJSExperiments/graal-ws/graal/truffle/mxbuild/jdk25/dists/jdk21/truffle-api.jar:/Users/dcsl/wf/GraalJSExperiments/graal-ws/graaljs/graal-js/mxbuild/jdk25/dists/jdk17/graaljs.jar:/Users/dcsl/wf/GraalJSExperiments/graal-ws/graal/truffle/mxbuild/jdk25/dists/jdk17/truffle-xz.jar:/Users/dcsl/wf/GraalJSExperiments/graal-ws/graal/truffle/mxbuild/jdk25/dists/jdk17/truffle-icu4j.jar:/Users/dcsl/wf/GraalJSExperiments/graal-ws/graal/regex/mxbuild/jdk25/dists/jdk17/tregex.jar:/Users/dcsl/wf/GraalJSExperiments/graal-ws/graal/truffle/mxbuild/jdk25/dists/jdk17/truffle-runtime.jar:/Users/dcsl/wf/GraalJSExperiments/graal-ws/graal/sdk/mxbuild/jdk25/dists/jdk17/jniutils.jar:/Users/dcsl/wf/GraalJSExperiments/graal-ws/graal/truffle/mxbuild/jdk25/dists/jdk17/truffle-compiler.jar \
    -Xms20g -Xmx20g -Xss1g -XX:ReservedCodeCacheSize=1g \
    --enable-native-access=org.graalvm.truffle \
    --sun-misc-unsafe-memory-access=allow \
    -cp ~/wf/GraalJSExperiments/experimentation/target/experimentation-1.0-SNAPSHOT.jar \
    com.jtortugo.ProxyFieldAccess  | tee debug.txt



#### echo "Graal EE"
#### 
#### /Users/dcsl/Downloads/graalvm-jdk-24+36.1/Contents/Home/bin/java \
####     -server \
####     -XX:+UnlockExperimentalVMOptions \
####     -XX:+EnableJVMCI \
####     -XX:-UseCompressedOops \
####     ${DEBUG} \
####     --add-exports=java.base/jdk.internal.misc=jdk.graal.compiler \
####     ${DUMP}               \
####     -Dgraalvm.locatorDisabled=true    \
####     -Xms2g -Xmx2g -Xss24m \
####     --module-path=@/Users/dcsl/wf/GraalJSExperiments/tmp-polyglot-jars/classpath \
####     --enable-native-access=org.graalvm.truffle \
####     --sun-misc-unsafe-memory-access=allow \
####     -cp @/Users/dcsl/wf/GraalJSExperiments/tmp-polyglot-jars/classpath \
####     com.jtortugo.ProxyFieldAccess 
