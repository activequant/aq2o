<%@include file="header.jsp"%>
<h3>${mdiid}</h3>


<table class="table">

	<c:forEach var="e" items="${keys}">
		<tr>
			<td>${e}</td>
			<td>${mdi[e]}</td>
		</tr>
	</c:forEach>

</table>
<p>
<a href="data_inspector?mdiid=${mdiid}">Proceed to data inspector</a>.
</p>
<hr />
<%@include file="footer.jsp"%>