<%@include file="header.jsp"%>
<h3>Component details</h3>
<table class="table">
<tr><td>ID</td><td><c:out value="${id}" /></td></tr>
<tr><td>Name</td><td><c:out value="${name}" /></td></tr>
<tr><td> Description </td><td><c:out value="${description}"/></td></tr>
</table>
<hr />
<%@include file="footer.jsp"%>