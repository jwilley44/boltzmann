polymerRadiusHist <- function(source)
{
	polyData = readData(source);
	pdf('polymerRadiusHist.pdf');
	aveRad = mean(polyData$PolymerRadius);
	dens = density(polyData$PolymerRadius / aveRad);
	plot(dens);
	hist(polyData$PolymerRadius, col='darkred', xlab='PolymerRadius');
	dev.off();
}

identity <- function(value)
{
	return(value);
}

log2 <- function(value)
{
	return(log(value, 2));
}

inverseMeanSqr <- function(simData, variable)
{
	ave = sqrt(mean(simData[, variable]**2));
	return(simData[, variable]/ave);
}

histScale <- function(source, histVariable, scaleVariable, variableFunc)
{
	simData = readData(source);
	filename = paste(histVariable, 'Hist', scaleVariable, 'pdf', sep='.');
	pdf(filename);
	scaleRange = getScaleRange(simData, scaleVariable);
	par(mfrow=c(2,2));
	function1=match.fun(variableFunc);
	for (i in 1:length(scaleRange))
	{
		subData = getSubsetByVariable(simData, scaleVariable, scaleRange[i]);
		plotTitle = paste(scaleVariable, scaleRange[i], sep=' = ');
		hist(function1(subData, histVariable), col='darkred', xlab=histVariable, main=plotTitle);
		if (i%%4 == 0)
		{
			par(mfrow=c(2,2));
		}
	}
	dev.off()
}

meanVsScale <- function(source, meanVariable, scaleVariable, meanFunc, scaleFunc)
{
	simData = readData(source);
	filename = paste(scaleVariable, meanVariable, 'pdf', sep='.');
	pdf(filename);
	scaleRange = getScaleRange(simData, scaleVariable);
	means=c();
	function1 = match.fun(scaleFunc);
	function2 = match.fun(meanFunc);
	for (i in 1:length(scaleRange))
	{
		subData = getSubsetByVariable(simData, scaleVariable, scaleRange[i]);
		means[i] = mean(subData[, meanVariable]);
	}
	plot(function1(scaleRange), function2(means), type='l', col=4, xlab=scaleVariable, ylab=meanVariable);
	dev.off();
}

timeEvolAveRadius <- function(source)
{
	polyData = readData(source);
	pdf('radiusVsT.pdf');
	count = length(polyData$PolymerRadius) - 1;
	ave = 0;
	aveVsT = c();
	size = polyData$PolymerSize[1];
	for (i in 1:count)
	{
		ave = (ave*(i-1) + polyData$PolymerRadius[i])/i;
		aveVsT[i] = ave;
	}
	plot(1:count, log(aveVsT)/log(size), type='l', col=2);
	dev.off();
}

scaleScatterPlot <- function(source, scaleVariable, xVariable, yVariable)
{
	simData = readData(source);
	filename = paste(yVariable, 'Vs', xVariable, 'pdf', sep='.');
	pdf(filename);
	scaleRange = getScaleRange(simData, scaleVariable);
	par(mfrow=c(2,2));
	for (i in 1:length(scaleRange))
	{
		subData = getSubsetByVariable(simData, scaleVariable, scaleRange[i]);
		plotTitle = paste(scaleVariable, scaleRange[i], sep=' = ');
		plot(subData[, xVariable], subData[, yVariable], col=4, xlab=xVariable, ylab=yVariable, main=plotTitle);
		if (i%%4 == 0)
		{
			par(mfrow=c(2,2));
		}
	}
	dev.off()
}

scatterPlot <- function(source, xVariable, yVariable)
{
	simData = readData(source);
	filename = paste(yVariable, 'Vs', xVariable, 'pdf', sep='.');
	pdf(filename);
	plot(simData[, xVariable], simData[, yVariable], col=4, xlab=xVariable, ylab=yVariable);
	dev.off()
}

singlePolymerRadius <- function(source)
{
	polyData = readData(source);
	pdf('radius.pdf');
	scaleRange = getScaleRange(polyData, 'PolymerSize');
	sizeRadiiData = data.frame();
	for (i in 1:length(scaleRange))
	{
		sizeData = getSubsetByVariable(polyData, "PolymerSize", scaleRange[i]);
		aveRadius = sqrt(mean(sizeData$PolymerRadius**2));
		stdErr = sd(sizeData$PolymerRadius);
		sizeRadiiData = rbind(sizeRadiiData, cbind(log(scaleRange[i]), log(aveRadius), stdErr / aveRadius));
	}
	colnames(sizeRadiiData) <- c("lnSize", "lnRadius", "Err");
	lin = lm(sizeRadiiData$lnRadius ~ sizeRadiiData$lnSize);
	plot(sizeRadiiData$lnSize, sizeRadiiData$lnRadius, col=2, xlab='ln(N)', ylab='ln(r)', pch=20);
	yErrorBar(sizeRadiiData$lnSize, sizeRadiiData$lnRadius, sizeRadiiData$Err);
	abline(lin);
	slope=paste('m = ', signif(lin$coefficients[2], 4));
	legend("bottomright", slope, col=1,lty=1);
	dev.off();
}

scaleRod <- function(source, variableName)
{
	rodData = readData(source);
	scaleRange = getScaleRange(rodData, variableName);
	pdf('scale.pdf');
	par(mfrow=c(2,1));
	for (i in 1:length(scaleRange))
	{
		scaleData = cumulateMoves(getSubsetByVariable(rodData, variableName, scaleRange[i]));
		plotTitle = paste(variableName, paste('=', scaleRange[i]));
		plot(scaleData$Move, scaleData$Correlation, type='l', col=2, xlab='Move', ylab='Correlation', main=plotTitle);
		if (i %% 2 == 0)
		{
			par(mfrow=c(2, 1));			
		}
	}
	dev.off();
}

radiusHistAndDens <- function(source)
{
	polyData = readData(source);
	pdf('scaleHist.pdf');
	scaleRange = getScaleRange(polyData, 'PolymerSize');
	par(mfrow=c(2,2));
	for (i in 1:length(scaleRange))
	{
		subData = getSubsetByVariable(polyData, 'PolymerSize', scaleRange[i]);
		Rf = sqrt(mean(subData$PolymerRadius));
		radii = subData$PolymerRadius/Rf;
		plotTitle = paste('PolymerSize', paste('=', scaleRange[i]));
		hist(radii, col='darkred', main=plotTitle);
		dens = density(radii);
		plot(dens)
		if (i %% 2 == 0)
		{
			par(mfrow=c(2,2));			
		}
	}
	dev.off();
}

timeEvolution <- function(source, evolutionVariable, scalingVariable)
{
	simData = readData(source);
	scaleRange = sort(getScaleRange(simData, scalingVariable));
	fileName = paste('evolution', evolutionVariable, 'pdf', sep='.');
	pdf(fileName);
	par(mfrow=c(2,1));
	for (i in 1:length(scaleRange))
	{
		scaleData = cumulateMoves(getSubsetByVariable(simData, scalingVariable, scaleRange[i]));
		plotTitle = paste(scalingVariable, paste('=', scaleRange[i]));
		plot(scaleData$Move, scaleData[, evolutionVariable], type='l', col=2, xlab='Move', ylab=evolutionVariable, main=plotTitle);
		if (i %% 2 == 0)
		{
			par(mfrow=c(2, 1));			
		}
	}
	dev.off();
}

readData <- function(source)
{
	return(read.table(source, header=TRUE, sep='\t'));
}

cumulateMoves <- function(simData)
{
	returnData = simData;
	moves = c();
	totalMoves = 0;
	for(i in 1:length(simData[,1]))
	{
		dataRow = simData[i, ];
		totalMoves = totalMoves + dataRow$ValidMoves;
		moves[i] = totalMoves;
	}
	returnData$Move <- moves;
	return(returnData);
}

getSubsetByVariable <- function(simData, variable, value)
{
	return(simData[simData[, variable] == value, ]);
}

getScaleRange <- function(simData, scaleVariable)
{
	subData = subset(simData, !duplicated(simData[, scaleVariable]));
	return(subData[, scaleVariable]);
}

cdfDataFrame <- function(dataColumn, xLabel)
{
	x = sort(dataColumn);
	y = (0:length(x))/length(x);
	cdf = data.frame(cbind(xValue = x, Probabily = y));
	colnames(cdf)[1] <- xLabel;
	return(cdf);
}

yErrorBar <- function(xValues, yValues, yError)
{
	arrows(xValues, yValues + yError, xValues, yValues - yError, length=0.05, lwd=0.5, angle=90, code=3);
}

