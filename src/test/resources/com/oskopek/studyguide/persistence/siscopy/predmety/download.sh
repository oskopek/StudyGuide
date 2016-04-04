#!/bin/bash

for id in `cat predmety.txt`; do
    echo "Downloading $id"
    curl -sS 'https://is.cuni.cz/studium/predmety/index.php?do=predmet&kod='"$id" -o 'index.php?do=predmet&kod='"$id" &
done
sleep 1s
count=`ps aux | grep curl | grep -v grep | wc -l`
while [ $count -ne 0 ]; do
    echo "Waiting to finish downloading... $count files"
    count=`ps aux | grep curl | grep -v grep | wc -l`
    sleep 1s
done