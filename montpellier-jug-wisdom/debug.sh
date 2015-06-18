#!/usr/bin/env bash

if [ $# -eq 0 ]; then
   echo "Token must be provided check your email ;)"
   exit 1
fi

if type -p java; then
    _java=java
elif [[ -n "$JAVA_HOME" ]] && [[ -x "$JAVA_HOME/bin/java" ]];  then
    _java="$JAVA_HOME/bin/java"
else
    echo "no java"
fi

if [[ "$_java" ]]; then
    version=$("$_java" -version 2>&1 | awk -F '"' '/version/ {print $2}')
    if [[ "$version" < "1.8" ]]; then
       echo Java 8 needed :P read the manual.
       exit
    fi
    mvn clean wisdom:run -DskipTests -DskipITs -Ddebug=5005 -Dtoken=$@
fi
