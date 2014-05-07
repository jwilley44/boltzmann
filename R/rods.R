timeEvolution <- function(source)
{
	data = readData(source);
	pdf('rodTimeEvolution.pdf');
	par(mfrow=c(2,2));
	plot(data$RodCorrelation, col=2, type='l', xlab='Time', ylab='Correlation');
	plot(data$AverageRodDistance, col=3, type='l', xlab='Time', ylab='Average Distance');
	movesVsCorr = cumulativeMoves(data);
	plot(movesVsCorr[,1], data$RodCorrelation, col=4, type='l', xlab='Total Moves', ylab='Correlation');
	dev.off();
}

scaleCount <- function(source)
{
	data = readData(source);
	
}

plotScale <- function(source, variableName)
{
	rodData = readData(source);
	scaleRange = getScaleRange(rodData[, variableName]);
	fileName = paste('scale.pdf');
	pdf(fileName);
	par(mfrow=c(2,1));
	for (i in 1:length(scaleRange))
	{
		scaleData = getMovesVsVariable(rodData, variableName, scaleRange[i]);
		plotTitle = paste(variableName, paste('=', scaleRange[i]));
		plot(scaleData$Move, scaleData$Correlation, type='l', col=2, xlab='Move', ylab='Correlation', main=plotTitle);
		if (i %% 2 == 0)
		{
			par(mfrow=c(2, 1));			
		}
	}
	dev.off();
}

getMovesVsVariable <- function(rodData, variableName, value)
{
	scaleRange = getByScale(rodData, value, variableName);
	return(getMoveVsRodCorr(scaleRange));
}

getMoveVsRodCorr <- function(rodData)
{
	movesCorr = data.frame();
	totalMoves = 0;
	for(i in 1:length(rodData[,1]))
	{
		dataRow = rodData[i, ];
		totalMoves = totalMoves + dataRow$ValidMoves;
		movesCorr = rbind(movesCorr, c(totalMoves, dataRow$RodCorrelation));
	}
	colnames(movesCorr)[1] <- "Move";
	colnames(movesCorr)[2] <- "Correlation";
	return(movesCorr);
}

cumulativeMoves <- function(data)
{
	moves = data.frame();
	x = 0;
	for (i in data$ValidMoves)
	{
		x = x + i;
		moves = rbind(moves, x);
	}
	colnames(moves)[1] <- "Move";
	return(moves);
}

readData <- function(source)
{
	return(read.table(source, header=TRUE, sep='\t'));
}

getByScale <- function(rodData, value, colName)
{
	return(rodData[rodData[, colName] == value, ]);
}

getScaleRange <- function(dataColumn)
{
	return(subset(dataColumn, !duplicated(dataColumn)))
}

