#!/bin/bash
expression="source('/Users/jwilley44/Documents/workspace/dev/R/results.R'); simData=readData('data.tsv'); pdf('results.pdf'); par(mfrow=c(2,2)); ${1}";
/usr/bin/R --slave --vanilla -e "$expression"
