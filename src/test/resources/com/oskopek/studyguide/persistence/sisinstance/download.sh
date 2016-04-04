#!/bin/bash

for id in `cat predmety.txt`; do
    wget 'https://is.cuni.cz/studium/predmety/index.php?do=predmet&kod='"$id"
done