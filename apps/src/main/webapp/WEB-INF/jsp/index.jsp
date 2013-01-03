<%@include file="header.jsp"%>


<!-- Main hero unit for a primary marketing message or call to action -->
<div class="hero-unit">
	<!--  style="background-image: url(/<c:url value="resources/img/grass.jpg"/>);" -->
	<img style="float: right;"
		src='<c:url value="resources/img/weasel1.png"/>' />
	<h1>Yes, the weasel runs!</h1>
	<p>Congratulations! Your are viewing a running instance of the
		ActiveQuant Master Server. Downloading will give you a 110% replica.</p>
		<p>
		<i>The current date and time are: <c:out value="${serverTime}"/>.</i> 
		</p>
	<p>
		<a class="btn btn-primary btn-large"
			href="mailto:ustaudinger@activequant.com">Send us a message
			&raquo;</a>
	</p>
</div>

<!-- Example row of columns -->
<div class="row">

	<div class="span4">
		<h2>Reference data</h2>
		<p>Reference data is relevant data for automated trading. This
			includes tick size, tick values, expiry information, etc.</p>
		<p>
			<a class="btn" href="refdata">Tempting? &raquo;</a>
		</p>
	</div>
	<div class="span4">
		<h2>Market data</h2>
		<p>Quotes and ticks, combined with OHLC data, build the back bone
			of any good trading research company.</p>
		<p>
			<a class="btn" href="marketdata">What's there? &raquo;</a>
		</p>
	</div>
	<div class="span4">
		<h2>Trading</h2>
		<p>Direct market access. Current portfolio. Order fills and cleared trades. Do they
			match? Place some orders. </p>
		<p>
			<a class="btn" href="traddata">Curious? &raquo;</a>
		</p>
	</div>
</div>

<hr>
<div align="center">
<p>ActiveQuant Master Server is built with and proudly supports the following platforms and languages. </p>
<img src='<c:url value="resources/img/logos/java.jpg"/>'/>
<img src='<c:url value="resources/img/logos/spring.jpg"/>'/>
<img src='<c:url value="resources/img/logos/hbase.jpg"/>'/>
<img src='<c:url value="resources/img/logos/hadoop.jpg"/>'/>
<img src='<c:url value="resources/img/logos/activemq.jpg"/>'/>
<img src='<c:url value="resources/img/logos/hypersql_logo.jpg"/>'/>
<img src='<c:url value="resources/img/logos/r_logo.jpg"/>'/>
<img src='<c:url value="resources/img/logos/excel.jpg"/>'/>
<img src='<c:url value="resources/img/logos/python.jpg"/>'/>
<img src='<c:url value="resources/img/logos/scala.jpg"/>'/>
</div>
<hr>

<%@include file="footer.jsp"%>
