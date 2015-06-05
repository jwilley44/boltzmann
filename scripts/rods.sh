#!/bin/bash

parameters=$1
expDate=$(date | tr ' ' '.')
newDir="/Users/jwilley44/Documents/workspace/dev/results/Rods/${expDate}"
mkdir $newDir
rm /Users/jwilley44/Documents/workspace/dev/results/Rods/latest
ln -s $newDir /Users/jwilley44/Documents/workspace/dev/results/Rods/latest
results=${newDir}/results.tsv
error=${newDir}/err.log
cp $parameters ${newDir}/
time java -Xmx2048m  willey.app.physics.RodsExperimentApp $parameters > $results 2> $error
