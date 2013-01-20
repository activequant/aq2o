<%@include file="header.jsp"%>
<h3>${iid}</h3>


<table class="table">

	<c:forEach var="e" items="${keys}">
		<tr>
			<td>${e}</td>
			<td>${instrument[e]}</td>
		</tr>
	</c:forEach>

</table>

<h5>Related MDIs</h5>

<table>
	<c:forEach var="e" items="${mdis}">
		<tr>
			<td><a href="mdi?mdiid=${e.id}">${e.id}</a></td>
		</tr>
	</c:forEach>
</table>


<h5>Related TDIs</h5>

<table>
	<c:forEach var="e" items="${tdis}">
		<tr>
			<td><a href="tdi?tdiid=${e.id}">${e.id}</a></td>
		</tr>
	</c:forEach>
</table>


<hr />
<%@include file="footer.jsp"%>