<%@include file="header.jsp"%>
<h3>${tdiid}</h3>


<table class="table">

	<c:forEach var="e" items="${keys}">
		<tr>
			<td>${e}</td>
			<td>${tdi[e]}</td>
		</tr>
	</c:forEach>

</table>


<hr />
<%@include file="footer.jsp"%>