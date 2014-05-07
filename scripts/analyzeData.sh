#!/bin/bash
expType=$1
rawData=$2
dataFolder=$(echo $rawData | rev | cut -d '/' -f2- | rev)
dataFolder="${dataFolder}/"
oldData="${dataFolder}data.tsv"
java -Xmx1024m willey.app.physics.polymer.AnalyzeRawDataApp $expType $rawData > newData.tsv
cut -f 1,2 $oldData | paste - <(cut -f3- newData.tsv) > data.tsv
rm newData.tsv rawData.dat
mv data.tsv $dataFolder