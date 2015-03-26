#!/bin/bash

parameters=$1
expDate=$(date | tr ' ' '.')
newDir="/Users/jwilley44/Documents/workspace/dev/results/Polymer/${expDate}"
mkdir $newDir
rm /Users/jwilley44/Documents/workspace/dev/results/Polymer/latest
ln -s $newDir /Users/jwilley44/Documents/workspace/dev/results/Polymer/latest
results=${newDir}/results.tsv
error=${newDir}/err.log
cp $parameters ${newDir}/
time java -Xmx2048m willey.app.physics.PolymerExperimentApp $parameters > $results 2> $error
