<%@include file="header.jsp" %>

<h3>Reference data</h3>
<p>
Reference data is one of, if not even the most important data type. 
Automated trading strategies need to monitor expiry details, 
they need to monitor first and last trading dates, settlement cycles, 
but surely tick sizes and tick values - 
or how do you  construct a cash neutral position between two futures?   
</p>
<p>Luckily, we have the solution. Your nose points at it!</p>

<h3>How does it work?</h3>
<p>
Data feeders, for example AQMS&trade; broker connectors, keep the AQMS data up-to-date. Usually, at regular intervals, connectors check for new ones and update existing instruments with the correct data. Some connectors also maintain chain information, so that it's possible to move within the security chain, for example from one expiry or from one strike to another.   
</p>
<p>
Some connectors are available for free, others are available as commercial solutions. 
</p>


<hr/>
<%@include file="footer.jsp" %>
