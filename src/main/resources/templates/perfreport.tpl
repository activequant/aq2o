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

<table>
<caption>Algo config</caption>
<!-- AC_ENTRY_START --><tr><td>{KEY}</td><td>{VALUE}</td></tr><!-- AC_ENTRY_END -->
</table>

<table>
<caption>PNL statistics</caption>
<#list PNL_CHARACTERISTICS as cRow>
	<tr>
		<#list cRow as cCell>
			<td>${cCell}</td>		
		</#list>
	</tr>
</#list>

</table>

<h2>Monthly returns</h2>
<#list MONTHLY_RETS as retTable>
<hr/>
<table border="1">
<#list retTable as cRow>
	<tr>
		<#list cRow as cCell>
			<td>${cCell}</td>		
		</#list>
	</tr>
</#list>

</table>
</#list>



</td>
<td class="pnlTD">
<h2>Pnl</h2>

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

<h2>Cash positions</h2>

<img src="CASH_AGGREGATION.png"/><br/>

<#list CASH_INSTS as instrument>
	<img src="CASH_CANDLE_${instrument}.png"/><br/>	
</#list>  

<#list CASH_INSTS as instrument>
	<img src="CASH_HIST_${instrument}.png"/><br/>	
</#list>  

<#list CASH_INSTS as instrument>
	<img src="CASH_QQ_${instrument}.png"/><br/>	
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