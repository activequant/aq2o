<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>Backtest report from ${REPORTID}</title>
<link rel="stylesheet" href="report.css" type="text/css" />
</head>
<body>

<div class="header">
<div class="title">Report ${REPORTID}</div>
<div class="logo"><img src="http://aq2o.activequant.org/images/logo.png"/></div>
</div>


<div class="content">
<table>
<tr>
<td>

<table>
<caption>Generics</caption>
<tr><td>MDIs</td><td>${MDIS}</td></tr>
<tr><td>TDIs</td><td>${TDIS}</td></tr>
<tr><td>Start</td><td>${TIMESTAMPSTART}</td></tr>
<tr><td>End</td><td>${TIMESTAMPEND}</td></tr>
<tr><td>Report resolution</td><td>${RESOLUTION}</td></tr>
</table>

<!-- AC_MARKER_START -->
<table>
<caption>Algo config</caption>
<!-- AC_ENTRY_START --><tr><td>{KEY}</td><td>{VALUE}</td></tr><!-- AC_ENTRY_END -->
</table>
<!-- AC_MARKER_END -->

<!-- LEG_MARKER_START -->
<table>
<caption>Statistics for {INSTRUMENTID}</caption>
<tr><td>Final PNL</td><td>{FINALPNL}</td></tr>
<tr><td>Max P&L</td><td>{MAXPNL}</td></tr>
<!--  tr><td>Avg. P&L</td><td>{AVGSLOTPNL}</td></tr>
<tr><td>Std.Dev of P&L</td><td>{AVGSLOTPNL}</td></tr -->
<tr><td>P&L per trade</td><td>{PNLPERTRADE}</td></tr>
<tr><td># orders placed</td><td>{TOTALPLACED}</td></tr>
<tr><td># order fills</td><td>{TOTALFILLS}</td></tr>
<tr><td># order cancellations</td><td>{TOTALORDERCNCL}</td></tr>
</table>
<!-- LEG_MARKER_END -->

<!-- LEG_MARKER_PLACEMENT -->


</td>
<td class="pnlTD">
<img src="pnl.png"/><br/>
<img src="position.png"/><br/>
<img src="PNL_AGGREGATION.png"/><br/>

<#list instruments as instrument>
	<img src="PNL_CANDLE_${instrument}.png"/><br/>	
</#list>  

<#list instruments as instrument>
	<img src="PNL_HIST_${instrument}.png"/><br/>	
</#list>  

<#list instruments as instrument>
	<img src="PNL_QQ_${instrument}.png"/><br/>	
</#list>  
</td>
</tr>
</table>
</div>

<div class="footer">
<p class="footertext">
The results shown above are calculated automatically. The underlying equations have 
been thoroughly reviewed and are assumed to be correct. The creator of these reports
is not liable for any losses incurred by deducting investment decisions from these 
reports.  
</p>
</div>

</body>
</html>