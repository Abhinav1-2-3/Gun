#!/bin/sh
#
# Copyright 2015 the original author or authors.
#

DIRNAME="$(cd -- "$(dirname -- "$0")" && pwd -P)"
WRAPPER_JAR="$DIRNAME/gradle/wrapper/gradle-wrapper.jar"

if [ ! -f "$WRAPPER_JAR" ]; then
    echo "Downloading gradle-wrapper.jar..."
    mkdir -p "$DIRNAME/gradle/wrapper"
    curl -fsSL https://raw.githubusercontent.com/gradle/gradle/v8.11.1/gradle/wrapper/gradle-wrapper.jar -o "$WRAPPER_JAR"
fi

if [ -n "$JAVA_HOME" ]; then
    JAVA_EXE="$JAVA_HOME/bin/java"
else
    JAVA_EXE="java"
fi

exec "$JAVA_EXE" -classpath "$WRAPPER_JAR" org.gradle.wrapper.GradleWrapperMain "$@"
