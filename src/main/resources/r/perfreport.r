# R script. 
# has one main function 

cat("SOURCING PERFREPORT SCRIPT\n")

require(xts)
require(quantmod)
require(fBasics)
require(PerformanceAnalytics) 

p <- function(fileName, folder="./", prefix="", cw = 800, ch = 600){
	png(paste(folder, prefix, fileName, ".png", sep=""), width=cw, height=ch)
}


# target resolution must be any of: 
# RAW, 1m, 1h, 1d, 1w, 1M 
analysis <- function(seriesCsvFile="/home/ustaudinger/work/activequant/trunk/reports2/pnl.csv", fileType="PNL", targetResolution="EOD", targetFolder="./", chartWidth=800, chartHeight=600){
	browser()
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
	
	# read transaction numbers
	transactionCount = read.csv(paste(targetFolder, "transactionCount.properties", sep=""), sep="=")
	cat("Loaded transaction count\n")
	#
	config = read.csv(paste(targetFolder, "/report.config", sep=""), sep="=")
	rownames(config) = config[,1]
	cat("Loaded config\n")
	#
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
		if(targetResolution=="MINUTES_1"){
			rsPnl = to.minutes(pnlData[,i], 1)
			formerPeriod = as.POSIXlt(index(rsPnl)[1], origin="1970-01-01")
			formerPeriod$min = formerPeriod$min - 1
		} else if(targetResolution=="HOURS_1"){
			rsPnl = to.hourly(pnlData[,i])
			formerPeriod = as.POSIXlt(index(rsPnl)[1], origin="1970-01-01")
			formerPeriod$hour = formerPeriod$hour - 1			
		} else if(targetResolution=="EOD"){
			rsPnl = to.daily(pnlData[,i], drop.time=TRUE)
			formerPeriod = as.POSIXlt(index(rsPnl)[1], origin="1970-01-01")
			formerPeriod$yday = formerPeriod$yday - 1
		} 
		colnames(rsPnl) <- c("Open","High","Low", "Close")
		#if(formerPeriod!=-1){
			## append it. 
		#	newRow = xts(cbind(formerValue,formerValue,formerValue,formerValue), formerPeriod, tzone="")
		#	colnames(newRow) <- c("Open","High","Low", "Close")
		#	rsPnl = rbind(newRow, rsPnl)
		#}
				
		# merge the rs close value into the outer rs values object. 
		rsCloses = merge(rsCloses, rsPnl[,4])
		colnames(rsCloses)[length(rsCloses[1,])] = columnName
		 			
		# generate candle chart
		p(columnName, folder=targetFolder, prefix=paste(fileType,"_CANDLE_", sep=""), cw=chartWidth, ch=chartHeight)
		#browser()
		candleChart(rsPnl, name=columnName, theme="white", TA="addEMA()")
		dev.off()
		# generate a plain line chart. 
		p(columnName, folder=targetFolder, prefix=paste(fileType, "_LINE_", sep=""), cw=chartWidth, ch=chartHeight)
		lineChart(rsPnl[,4], name=columnName, theme="white", TA="addEMA()")
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
		finPnl = as.double(last(rsPnl)[,4]) 
		characteristics["finalValue", columnName] = finPnl 
		characteristics["transactions", columnName] = transactionCount[columnName,1]
		characteristics["pnl per transaction", columnName] = transactionCount[columnName,1]/finPnl
		characteristics["maxValue", columnName] = max(rsPnl)
		characteristics["minValue", columnName] = min(rsPnl)
		characteristics["meanAbsRetPerPeriod", columnName] = mean(absReturns)
	
		if(!is.na(config[paste(columnName, ".START", sep=""),2])){
			startCap = config[paste(columnName, ".STARTCAP", sep=""),2]
			startCap = as.double(as.matrix(startCap))			
			characteristics["startCap", columnName] = startCap
			# calculate the performance
			absGainOverStartCap = diff(pnlData[,columnName])/startCap
			monthlyGains = table.CalendarReturns(absGainOverStartCap)
			write.csv(monthlyGains, paste(targetFolder, "/", fileType, "_", columnName, "_TABULARRETS.csv", sep=""))
												
		}
		
		
		#browser()
	}	
	# generate an aggregated chart with all rs closes on it. 	
	p("AGGREGATION", folder=targetFolder, prefix=paste(fileType, "_", sep=""), cw=chartWidth, ch = chartHeight) 
	for(i in 1:nCols){
		columnName = colnames(rsCloses)[i]
		cat("Processing rs close for ", columnName, "\n")		
		if(i==1){
			plot(rsCloses[,i], type="l", main="Comparison of value series", ylim=c(min(rsCloses),max(rsCloses)))
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


currentwd = getwd()
setwd(args[3])


analysis(seriesCsvFile=args[1], targetFolder=args[3],targetResolution=args[4], fileType="PNL")
analysis(seriesCsvFile=args[2], targetFolder=args[3],targetResolution=args[4], fileType="CASH");




pnl = read.csv("pnl.csv")
fees = read.csv("fees_resampled_to_timeframe.csv")
interest = read.csv("interest.csv")
refrates = read.csv("refrates.csv")

nCols1 = length(pnl[1,])
nCols2 = length(fees[1,])
nCols3 = length(interest[1,])
nCols4 = length(refrates[1,])

xpnl = xts(pnl[,3:nCols1],as.POSIXct(pnl[,1] / 1000000000, origin="1970-01-01"))
xfees = xts(fees[,3:nCols2],as.POSIXct(fees[,1] / 1000000000, origin="1970-01-01"))
colnames(xfees)=colnames(fees)[3:ncol(fees)]

xinterest = xts(interest[,3:nCols3],as.POSIXct(interest[,1] / 1000000000, origin="1970-01-01"))
colnames(xinterest)=colnames(interest)[3:ncol(interest)]

xrefrates = xts(refrates[,3:nCols4],as.POSIXct(refrates[,1] / 1000000000, origin="1970-01-01"))
colnames(xrefrates)=colnames(refrates)[3:ncol(refrates)]

pnlCols = colnames(pnl)
refratesCols= colnames(xrefrates)
processPnlCol <- function (i) {
  inverted1 = FALSE
  inverted2 = FALSE
  columnToBeUsed = -1
  # 
  colname = substring(pnlCols[i], 4)
  # have to find the target ref rate to convert to USD
  quotee = substring(colname, 4)  
  if (quotee == "USD") {
    conversionRate = 1.0;
    columnToBeUsed = -2
    cat("Not converting a USD rate. \n")        
  }
  else {
    # check all the xrefrate columns if we can use one. 
    for(j in 1:ncol(xrefrates)){
      refratecol = refratesCols[j]
      refratecol = substring(refratecol, 4)
      cat("- Checking ", refratecol, "\n")
      base_ = substring(refratecol, 1,3)
      quotee_ = substring(refratecol, 4)
      if (base_== "USD") {
        # ok, possible target ...
        if (quotee_ == quotee) {
          inverted1 = TRUE
          columnToBeUsed = j		      
          cat("Have to invert first, ", columnToBeUsed, "\n")
          break
        }
      } else if (quotee_ == "USD") {
        # ok, possible target ...
        if (base_ == quotee) {
          columnToBeUsed = j		    
          cat("Have to invert none, ", columnToBeUsed, "\n")
          break
        }
      }
    } 
  }  
  return(c(columnToBeUsed, inverted1, inverted2))
}




nCols5 = ncol(xpnl)
pnlCols = colnames(xpnl)

convertedPnl = xts()
for(i in 1:nCols5){
  columnToBeUsed = -1
  inverted1 = FALSE
  inverted2 = FALSE
  columnName = pnlCols[i]
  cat("I: ", i, "--> " , columnName, "\n")
  if(pnlCols[i]=="TOTAL"){
    cat("Not converting a total. \n")
  }
  else{
    x1 = processPnlCol(i)    
    columnToBeUsed = x1[1]
    inverted1 = x1[2]
    inverted2 = x1[3]
    pnlColumn = xpnl[,i]
    print(x1)
    if(columnToBeUsed==-1){
      cat("Can't convert.\n")
    }
    else if(columnToBeUsed==-2){
      convertedPnlInUsd = pnlColumn
      colnames(convertedPnlInUsd) = c(columnName)
      print(colnames(convertedPnlInUsd))
      convertedPnl = merge(convertedPnl, convertedPnlInUsd)
      #print(pnlColumn)
    }
    else {      
      pnlColumn = xpnl[,i]
      refratecol = xrefrates[,columnToBeUsed]
      if(inverted1==1)
        refratecol = 1/refratecol      
      convertedPnlInUsd = pnlColumn * refratecol
      colnames(convertedPnlInUsd) = c(columnName)
      print(colnames(convertedPnlInUsd))
      # browser()
      convertedPnl = merge(convertedPnl, convertedPnlInUsd)
      #print(convertedPnl)
    }
  }    
}

#
x1 = rowsum(convertedPnl,group=index(convertedPnl))
print(x1)
convertedPnl = cbind(x1, convertedPnl)

#


# add all fees, etc. 
write.csv(convertedPnl, "pnl_in_usd.csv")

png("pnl_in_usd.png", width=800, height=600)
plot(convertedPnl[,1], ylim=c(min(convertedPnl), max(convertedPnl)), main="PNL IN USD")
par(xpd=TRUE)
legend("topleft", legend=colnames(rsCloses), fill = colors)
colors = rainbow(ncol(convertedPnl))
for(i in 1:ncol(convertedPnl)){
  lines(convertedPnl[,i],col=colors[i])
}

dev.off()



setwd(currentwd) 