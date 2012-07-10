# R script. 
# has one main function 

cat("SOURCING PERFREPORT SCRIPT\n")

require(xts)
require(quantmod)
require(fBasics)

p <- function(fileName, folder="./", prefix="", cw = 800, ch = 600){
	png(paste(folder, prefix, fileName, ".png", sep=""), width=cw, height=ch)
}


# target resolution must be any of: 
# RAW, 1m, 1h, 1d, 1w, 1M 
analysis <- function(seriesCsvFile="/home/ustaudinger/work/activequant/trunk/reports2/pnl.csv", fileType="PNL", targetResolution="1d", targetFolder="./", chartWidth=800, chartHeight=600){
	
	pnlData = read.csv(seriesCsvFile)
	# convert pnl data to xts 	
	if(length(pnlData)==0){
		cat("No PNL data \n");
		return;	
	}	
	nCols = length(pnlData[1,])
	pnlData = xts(pnlData[,3:nCols],as.POSIXct(pnlData[,1] / 1000000000, origin="1970-01-01"))
	
	# as we have made an XTS object out of it, we have now one column less. 
	nCols = length(pnlData[1,])
	# create a colour palette	
	colors = rainbow(nCols)
	
	characteristics = data.frame();
	
	# rs closes .. 
	rsCloses = xts()
	
	# resample pnlData to the specified resolution
	for(i in 1:nCols){
		columnName = colnames(pnlData)[i]
		cat("Processing ", columnName, "\n")
		
		# raw requires no action		
		rsPnl = pnlData[,i]
		formerPeriod = -1;
		formerValue = pnlData[1,i]; 
		# 
		if(targetResolution=="EOM"){
			rsPnl = to.monthly(pnlData[,i], dropTime=TRUE)
			formerPeriod = as.POSIXlt(index(rsPnl)[1])
			formerPeriod$yday = formerPeriod$yday - 31 
		} else if(targetResolution=="MINUTES_1"){
			rsPnl = to.minutes(pnlData[,i], 1)
			formerPeriod = as.POSIXlt(index(rsPnl)[1])
			formerPeriod$min = formerPeriod$min - 1
		} else if(targetResolution=="HOURS_1"){
			rsPnl = to.hourly(pnlData[,i])
			formerPeriod = as.POSIXlt(index(rsPnl)[1])
			formerPeriod$hour = formerPeriod$hour - 1			
		} else if(targetResolution=="EOD"){
			rsPnl = to.daily(pnlData[,i], drop.time=TRUE)
			formerPeriod = as.POSIXlt(index(rsPnl)[1])
			formerPeriod$yday = formerPeriod$yday - 1
		} else if(targetResolution=="EOW"){
			rsPnl = to.weekly(pnlData[,i], drop.time=TRUE)
			formerPeriod = as.POSIXlt(index(rsPnl)[1])
			formerPeriod$yday = formerPeriod$yday - 7		
		}
		colnames(rsPnl) <- c("Open","High","Low", "Close")
		if(formerPeriod!=-1){
			## append it. 
			newRow = xts(cbind(formerValue,formerValue,formerValue,formerValue), formerPeriod, tzone="")
			colnames(newRow) <- c("Open","High","Low", "Close")
			rsPnl = rbind(newRow, rsPnl)
		}
				
		# merge the rs close value into the outer rs values object. 
		rsCloses = merge(rsCloses, rsPnl[,4])
		colnames(rsCloses)[length(rsCloses[1,])] = columnName
		 			
		# generate candle chart
		p(columnName, folder=targetFolder, prefix=paste(fileType,"_CANDLE_", sep=""), cw=chartWidth, ch=chartHeight)
		#browser()
		candleChart(rsPnl, title=columnName, theme="white", TA="addEMA()")
		dev.off()
		# generate a plain line chart. 
		p(columnName, folder=targetFolder, prefix=paste(fileType, "_LINE_", sep=""), cw=chartWidth, ch=chartHeight)
		lineChart(rsPnl[,4], main=columnName, theme="white", TA="addEMA()")
		dev.off()
		
		# generate return statistics
			
		absReturns = diff(rsPnl[,4])
		# replace NAs. 
		absReturns[is.na(absReturns)] = 0
		
		p(columnName, folder=targetFolder, prefix=paste(fileType, "_HIST_", sep=""), cw=chartWidth, ch=chartHeight)		
		hist(absReturns, col="gray", main=paste("Histogram of ",fileType," (", columnName, ")", sep=""))
		dev.off();
		
		p(columnName, folder=targetFolder, prefix=paste(fileType, "_QQ_", sep=""), cw=chartWidth, ch=chartHeight)
		z.norm <- (absReturns - mean(absReturns))/sd(absReturns)
		qqnorm(z.norm, main=paste("Normal QQ Plot of ", fileType, " (", columnName, ")", sep=""))
		abline(0,1)
		dev.off()
		
		# calculate some curve specific parameters
		characteristics["skewness", columnName] = skewness(absReturns)
		characteristics["kurtosis", columnName] = kurtosis(absReturns)
		
		characteristics["startCash", columnName] = as.double(first(rsPnl)[,1])
		characteristics["finalValue", columnName] = as.double(last(rsPnl)[,4])
		characteristics["maxValue", columnName] = max(rsPnl)
		characteristics["minValue", columnName] = min(rsPnl)
		characteristics["meanAbsRetPerPeriod", columnName] = mean(absReturns)
		
		#browser()
	}	
	# generate an aggregated chart with all rs closes on it. 	
	p("AGGREGATION", folder=targetFolder, prefix=paste(fileType, "_", sep=""), cw=chartWidth, ch = chartHeight) 
	for(i in 1:nCols){
		columnName = colnames(rsCloses)[i]
		cat("Processing rs close for ", columnName, "\n")		
		if(i==1){
			plot(rsCloses[,i], type="l", main="Comparison of value series")
			par(xpd=TRUE)
			legend("topleft", legend=colnames(rsCloses), fill = colors)
			
		}
		else{
			lines(rsCloses[,i],col=colors[i])
		}
	}
	dev.off()	
	# write the characteristics
	write.csv(characteristics, paste(targetFolder, "/", fileType, "_characteristics.csv", sep=""))	
}

# see http://stackoverflow.com/questions/2151212/how-can-i-read-command-line-parameters-from-an-r-script
args <- commandArgs(trailingOnly = TRUE)
print(args)

analysis(seriesCsvFile=args[1], targetFolder=args[3],targetResolution=args[4], fileType="PNL")
analysis(seriesCsvFile=args[2], targetFolder=args[3],targetResolution=args[4], fileType="CASH"); 