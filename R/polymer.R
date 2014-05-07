scale <- function(source)
{
	data = readData(source);
	sizes = subset(data, !duplicated(data$PolymerSize))$PolymerSize;
	dataPoints = data.frame();
	for (i in sizes)
	{
		dataPoints = rbind(dataPoints, getScalingDataPoint(data, i));
	}
	
	x = log(dataPoints[,1]);
	y = log(dataPoints[,2]);
	lin = lm(y ~ x);
	pdf('scale.pdf');
	plot(x, y, col=2, xlab='ln(N)', ylab='ln(r)');
	abline(lin);
	slope=paste('m = ', signif(lin$coefficients[2], 4));
	legend("bottomright", slope, col=1,lty=1);
	dev.off();
}

movesVsAngle <- function(source)
{
	polyData = readData(source);
	scaleRange = getScaleRange(polyData$PolymerSize);
	fileName = paste('movesAngle.pdf');
	pdf(fileName);
	par(mfrow=c(2,1));
	for (i in 1:length(scaleRange))
	{
		scaleData = getMoveVsAngle(polyData[polyData$PolymerSize == scaleRange[i], ]);
		plotTitle = paste('PolymerSize', paste('=', scaleRange[i]));
		plot(scaleData$Move, scaleData$Correlation, type='l', col=2, xlab='Move', ylab='Correlation', main=plotTitle);
		if (i %% 2 == 0)
		{
			par(mfrow=c(2, 1));			
		}
	}
	dev.off();
}

getMoveVsAngle <- function(polyData)
{
	movesCorr = data.frame();
	totalMoves = 0;
	for(i in 1:length(polyData[,1]))
	{
		dataRow = polyData[i, ];
		totalMoves = totalMoves + dataRow$ValidMoves;
		movesCorr = rbind(movesCorr, c(totalMoves, dataRow$AverageMonomerBondAngle));
	}
	colnames(movesCorr) <- c("Move", "Correlation");
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

equilibration <- function(source)
{
	data = read.table(source, header=TRUE, sep='\t');
	postscript('equilibration.eps', horizontal=FALSE);
	plot(data$PolymerRadius, col=2, type='l');
}

timeEvolution <- function(source)
{
	polyData = readData(source);
	pdf('timeEvolution.pdf');
	hist(polyData$PolymerRadius, col='darkred', xlab='Polymer Radius');
	dev.off();
}

readData <- function(source)
{
	return(read.table(source, header=TRUE, sep='\t'));
}

getByPolymerSize <- function(data, polymerSize)
{
	return(data[data$PolymerSize == polymerSize,]);
}

getScalingDataPoint <- function(data, polymerSize)
{
	subData = getByPolymerSize(data, polymerSize);
	return(cbind(polymerSize, mean(subData$PolymerRadius), sd(subData$PolymerSize)));
}
