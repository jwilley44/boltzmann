plotResults <- function(source)
{
	timingData = readData(source);
	createPdf();
	plotTimings(timingData);
	plotCDF(timingData);
	plotTotals(timingData);
	plotAverages(timingData);
}

readData <- function(source)
{
	return(read.table(source, header=TRUE, sep='\t'));
}

createPdf <- function()
{
	pdf('timingTests.pdf');
}

plotTimings <- function(timingData)
{
	columnNames = colnames(timingData);
	ymax = max(timingData);
	plot(timingData[,1], type='l', col=1, ylim=c(0,ymax), ylab='Run Time (s)');
	for (i in 2:length(columnNames))
	{
		lines(timingData[, i], type='l', col=i);
	}
	legend("bottomleft", columnNames, col=1:length(timingData[1,]), lty=1);
}

plotCDF <- function(timingData)
{
	columnNames = colnames(timingData);
	xmax = max(timingData);
	cdf = dataFrameCDF(timingData);
	plot(cdf[,2], cdf$probability, col=1, type='l', xlim=c(0, xmax), ylab='Probability', xlab='Run Time (s)');
	for (i in 2:length(columnNames))
	{
		lines(cdf[,(i+1)], cdf$probability, type='l', col=i);
	}
	legend("bottomright", columnNames, col=1:length(timingData[1,]), lty=1);
}

plotTotals <- function(timingData)
{
	sums = sapply(timingData, sum);
	barplot(sums, names=colnames(timingData), ylab='Total time (s)', col='darkred');
}

plotAverages <- function(timingData)
{
	means = sapply(timingData, mean);
	barplot(means, names=colnames(timingData), ylab='Average time (s)', col='darkred');
}

dataFrameCDF <- function(dataFrame)
{
	return(data.frame(probability=(1:length(dataFrame[,1])/length(dataFrame[,1])), sapply(dataFrame, sort)));
}

