#!/bin/bash

if [ "$#" -ne 2 ]; then
    echo "Usage: $0 inputFileName outputFileName"
    exit 1
fi

inputFileName=$1
outputFileName=$2

javac -cp .:gson-2.10.jar Main.java

if [ $? -ne 0 ]; then
    echo "Compilation failed"
    exit 1
fi

java -cp .:gson-2.10.jar Main $inputFileName $outputFileName
