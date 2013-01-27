<%@include file="header.jsp"%>
<h3>${id}</h3>
<table class="table">

	<c:forEach var="e" items="${keys}">
		<tr>
			<td>${e}</td>
			<td>${event[e]}</td>
		</tr>
	</c:forEach>

</table>
<hr />
<%@include file="footer.jsp"%>