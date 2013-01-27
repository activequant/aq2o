<%@include file="header.jsp"%>
<h3>Order Events</h3>
<p>
	<i>Specify a date in yyyyMMdd format to list all events of that date.</i>
</p>
<div class="input-append">
	<form>
		<input
			class="span2" style="width: 200px;" id="appendedInputButton"
			name="date" type="text" value="${date}" />
		<button type="submit" class="btn btn-primary">Search!</button>
	</form>
</div>
<table>
<c:forEach var="e" items="${eventIds}">
	<tr>
		<td><a href="orderevent?id=${e}">${e}</a></td>
	</tr>
</c:forEach>
</table>



<hr />
<%@include file="footer.jsp"%>