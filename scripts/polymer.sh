#!/bin/bash

parameters=$1
expBucket=$(date | tr ' ' '.')
typeBucket="polymer"
expBucket=$(date | tr ' ' '.')
expBucket=${typeBucket}.${expBucket}
results=${expBucket}.results.tsv
log=${expBucket}.log
echo "Parameters" > $log
cat $parameters >> $log
echo "" >> $log
DIR=$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )
java -cp $DIR -Xmx2048m willey.app.physics.PolymerExperimentApp $parameters > $results 2>> $log
aws s3 cp $results s3://johnwilley
aws s3 cp $log s3://johnwilley
rm $results
rm $log
aws ec2 --stop-instances --instance-ids i-2fb32be9
