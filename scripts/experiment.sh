#!/bin/bash
dir=$1
dataFile="data.tsv"
expFile="${dir}.exp"
errorFile="${dir}.err"
java -Xmx2048m willey.app.physics.polymer.ExperimentApp $@ > $dataFile 2> $errorFile
scaleVariable=$(cat $expFile | grep "Scale Variable" | cut -d ' ' -f 3)
expDate=$(date | tr ' ' '.')
newDir="${dir}/${expDate}"
mkdir ../results/$newDir
mv rawData.dat $dataFile $expFile $errorFile ../results/$newDir
cd ../results/$dir
rm latest
ln -s $expDate latest
cd latest
../results.sh $scaleVariable