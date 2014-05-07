polymerRadiusHist <- function(polyData)
{
	aveRad = mean(polyData$PolymerRadius);
	dens = density(polyData$PolymerRadius / aveRad);
	plot(dens);
	hist(polyData$PolymerRadius, col='darkred', xlab='PolymerRadius');
}

fractalization <- function(simData, scalingVariable, variableName)
{
	scaleRange = sort(getScaleRange(simData, scalingVariable));
	par(mfrow=c(2,2));
	for (i in 1:length(scaleRange))
	{
		rangeData = getSubsetByVariable(simData, scalingVariable, scaleRange[i]);
		fractPlot = getFractalization(rangeData, variableName);
		plotTitle = paste(paste(scalingVariable, scaleRange[i], sep=' = '), paste("(", variableName, ")", sep=""), sep=" ");
		plot(log(fractPlot$ArcLength), log(fractPlot$Distance), col=2, main=plotTitle, xlab="ln(arclength)", ylab="ln(distance)");
		lin = lm(log(fractPlot$Distance) ~ log(fractPlot$ArcLength) - 1);
		abline(lin);
		slope=paste('m = ', signif(lin$coefficients[1], 4));
		legend("bottomright", slope, col=1,lty=1);
	}
}

polymerScaleFactor <- function(simData, value)
{
	polymerSize = log(simData$PolymerSize[1]);
	return(log(value)/polymerSize)
	
}

getRatio <- function(simData)
{
	simData$PolymerRodRatio <- simData$RodLength / simData$PolymerSize;
	return(simData);
}

logPolSize <- function(simData, value)
{
	return(log(value, simData$PolymerSize[1]))
}

identity <- function(simData, value)
{
	return(value);
}

logn <- function(simData, value)
{
	return(log(value));
}

log2 <- function(simData, value)
{
	return(log(value, 2));
}

inverseMeanSqr <- function(simData, variable)
{
	ave = sqrt(mean(simData[, variable]**2));
	return(simData[, variable]/ave);
}

densityScale <- function(simData, histVariable, scaleVariable, variableFunc)
{
	scaleRange = getScaleRange(simData, scaleVariable);
	function1=match.fun(variableFunc);
	for (i in 1:length(scaleRange))
	{
		subData = getSubsetByVariable(simData, scaleVariable, scaleRange[i]);
		plotTitle = paste(scaleVariable, scaleRange[i], sep=' = ');
		dens = density(function1(subData, histVariable))
		plot(dens, col=4, main=plotTitle);
		if (i%%4 == 0)
		{
			par(mfrow=c(2,2));
		}
	}
}

histScale <- function(simData, histVariable, scaleVariable, variableFunc)
{
	scaleRange = getScaleRange(simData, scaleVariable);
	function1=match.fun(variableFunc);
	for (i in 1:length(scaleRange))
	{
		subData = getSubsetByVariable(simData, scaleVariable, scaleRange[i]);
		plotTitle = paste(scaleVariable, scaleRange[i], sep=' = ');
		hist(function1(subData, histVariable), col='darkred', xlab=getLabel(histVariable, variableFunc), main=plotTitle);
		if (i%%4 == 0)
		{
			par(mfrow=c(2,2));
		}
	}
}

getLabel <- function(variable, functionName)
{
	if (functionName == 'identity')
	{
		return(variable);
	}
	else
	{
		return(paste(functionName, '(', variable, ')', sep=''))
	}
}

multMeanVsScale <- function(simData, meanVariables, scaleVariable, meanFuncs, scaleFunc)
{
	for (i in 1:length(meanVariables))
	{
		meanVsScale(simData, meanVariables[i], scaleVariable, meanFuncs[i], scaleFunc);
	}
}

meanVsScale <- function(simData, meanVariable, scaleVariable, meanFunc, scaleFunc)
{
	scaleRange = getScaleRange(simData, scaleVariable);
	means=c();
	function1 = match.fun(scaleFunc);
	function2 = match.fun(meanFunc);
	for (i in 1:length(scaleRange))
	{
		subData = getSubsetByVariable(simData, scaleVariable, scaleRange[i]);
		means[i] = function2(subData, mean(subData[, meanVariable]));
	}
	xLabel = getLabel(scaleVariable, scaleFunc);
	yLabel = getLabel(meanVariable, meanFunc);
	plot(function1(simData, scaleRange), means, type='l', col=4, xlab=xLabel, ylab=yLabel);
}

timeEvolAveRadius <- function(polyData)
{
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
}

scaleScatterPlot <- function(simData, scaleVariable, xVariable, yVariable)
{
	scaleRange = getScaleRange(simData, scaleVariable);
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
}

scatterPlot <- function(simData, xVariable, yVariable)
{
	plot(simData[, xVariable], simData[, yVariable], col=4, xlab=xVariable, ylab=yVariable);
}

polymerScaling <- function(polyData)
{
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
	lin = lm(sizeRadiiData$lnRadius ~ sizeRadiiData$lnSize - 1);
	plot(sizeRadiiData$lnSize, sizeRadiiData$lnRadius, col=2, xlab='ln(N)', ylab='ln(r)', pch=20);
	yErrorBar(sizeRadiiData$lnSize, sizeRadiiData$lnRadius, sizeRadiiData$Err);
	abline(lin);
	slope=paste('m = ', signif(lin$coefficients[1], 4));
	legend("bottomright", slope, col=1,lty=1);
}

scaleRod <- function(rodData, variableName)
{
	scaleRange = getScaleRange(rodData, variableName);
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
}

getFractalization <- function(simData, variableName)
{
	fract = data.frame();
	fracCol = simData[, variableName];
	for (i in fracCol)
	{
		str = gsub("\\[|\\]", "", i);
		list = as.double(unlist(strsplit(str, split=", ")));
		fract = rbind(fract, list);
	}
	arcLengths = seq(1, length(fract))
	colnames(fract) <- arcLengths;
	means = c();
	for (i in 1:length(fract))
	{
		means[i] = mean(fract[, i]);
	}
	fractPlot = data.frame(arcLengths, means);
	colnames(fractPlot) <- c("ArcLength", "Distance");
	return(fractPlot);
}

list2Vector <- function(row)
{
	str = as.vector(row)[1];
	str = gsub("\\[|\\]", "", str);
	vect = as.double(unlist(strsplit(str, split=', ')));
	return(vect);
}

row2Vector <- function(row)
{
	str = as.vector(row)[1];
	str = gsub("\\(|\\)", "", str);
	vect = as.double(unlist(strsplit(str, split=', ')));
	return(vect);
}

vectorCorr2 <- function(simData, evolutionVariable)
{
	corr=c();
	corr[1] = 1.0;
	dir = row2Vector(evolutionVariable[1]);
	for (i in 2:(length(evolutionVariable)))
	{
		nextDir = row2Vector(evolutionVariable[i]);
		corr[i] = cosTheta(dir, nextDir)**2;
		dir = nextDir;
	}
	return(corr);
}

vectorCorr1 <- function(simData, evolutionVariable)
{
	corr=c();
	dir = row2Vector(evolutionVariable[1]);
	for (i in 1:(length(evolutionVariable)))
	{
		nextDir = row2Vector(evolutionVariable[i]);
		corr[i] = cosTheta(dir, nextDir)**2;
	}
	return(corr);
}

cosTheta <- function(v1 , v2)
{
	m1 = sqrt(v1%*%v1);
	m2 = sqrt(v2%*%v2);
	return(v1%*%v2/(m1*m2));
}

timeEvolution <- function(simData, evolutionVariable, scalingVariable, functionName)
{
	scaleRange = sort(getScaleRange(simData, scalingVariable));
	par(mfrow=c(2,1));
	evoFunction = match.fun(functionName);
	yLabel = getLabel(evolutionVariable, functionName);
	for (i in 1:length(scaleRange))
	{
		scaleData = cumulateMoves(getSubsetByVariable(simData, scalingVariable, scaleRange[i]));
		plotTitle = paste(scalingVariable, paste('=', scaleRange[i]));
		plot(scaleData$Move, evoFunction(scaleData, scaleData[, evolutionVariable]), type='l', col=2, xlab='Move', ylab=yLabel, main=plotTitle);
		if (i %% 2 == 0)
		{
			par(mfrow=c(2, 1));			
		}
	}
}

readData <- function(source)
{
	simData = read.table(source, header=TRUE, sep='\t');
	simData$FractionValidMoves = simData$ValidMoves/simData$EquilibrationTime;
	return(simData);
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
	return(sort(subData[, scaleVariable]));
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

