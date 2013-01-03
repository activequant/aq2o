<%@include file="header.jsp"%>
<h3>Connected components</h3>



<table class="table table-striped table-hover">
	<thead>
		<tr>
			<th>Component ID</th>
			<th>Component name</th>
			<th>Last seen</th>
		</tr>
	</thead>
	<tbody>
		<c:forEach var="e" items="${entries}">
			<tr>
				<td><a href="/component?componentId=${e.id}">${e.id}</a></td>
				<td>${e.name}</td>
				<td>${e.lastSeen}</td>
			</tr>
		</c:forEach>
	</tbody>
</table>


<hr />
<%@include file="footer.jsp"%>
