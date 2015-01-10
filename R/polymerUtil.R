library(plyr)
library(ggplot2)

polymerScaling <- function(polymerData)
{
	ddply(polymerData, .(PolymerSize), function(x) data.frame(lnSize=log(x$PolymerSiz[1]), lnRadius=log(mean(x$PolymerRadius)), err=1/sqrt(sd(x$PolymerRadius**2))))
}

polymerScalingSlope <- function(polymerData)
{
	scalingData <- polymerScaling(polymerData);
	linFit <- with(scalingData, lm(lnRadius ~ lnSize))
	return(data.frame(slope=linFit$coefficients[2]))
}

read.polyerRods <- function(filename="latest")
{
	read.table(file.path("/Users/jwilley44/Documents/workspace/dev/results/RodsAndPolymer/", filename, "results.tsv"), header=T, sep='\t')
}
