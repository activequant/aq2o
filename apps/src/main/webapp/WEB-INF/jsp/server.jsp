<%@include file="header.jsp"%>
<h3>Server configuration</h3>
<h4>Framework properties</h4>


<table class="table table-striped table-hover">
	<thead>
		<tr>
			<th>Key</th>
			<th>Value</th>			
		</tr>
	</thead>
	<tbody>
		<c:forEach var="e" items="${framework}">
			<tr>
				<td>${e.key}</td>
				<td>${e.value}</td>
			</tr>
		</c:forEach>
	</tbody>
</table>


<h4>AQ2Server properties</h4>

<table class="table table-striped table-hover">
	<thead>
		<tr>
			<th>Key</th>
			<th>Value</th>			
		</tr>
	</thead>
	<tbody>
		<c:forEach var="e" items="${aq2server}">
			<tr>
				<td>${e.key}</td>
				<td>${e.value}</td>
			</tr>
		</c:forEach>
	</tbody>
</table>

<hr />
<%@include file="footer.jsp"%>
