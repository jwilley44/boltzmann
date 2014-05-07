#!/bin/bash

parameters=$1
expDate=$(date | tr ' ' '.')
newDir="/Users/jwilley44/Documents/workspace/dev/results/RodsAndPolymer/${expDate}"
mkdir $newDir
rm /Users/jwilley44/Documents/workspace/dev/results/RodsAndPolymer/latest
ln -s $newDir /Users/jwilley44/Documents/workspace/dev/results/RodsAndPolymer/latest
results=${newDir}/results.tsv
error=${newDir}/err.log
cp $parameters ${newDir}/
java -Xmx2048m willey.app.physics.PolymerAndRodsExperimentApp $parameters > $results 2> $error
