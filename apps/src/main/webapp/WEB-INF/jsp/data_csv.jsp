<%@include file="header.jsp"%>

<p>
	<i>Start date and end date must be in format YYYYMMDD. Typical
		fields are BID, ASK, OPEN, etc.</i>
</p>

<div class="input">
	<form>
		<table>
			<tr>
				<td>MDI ID</td>
				<td><input type="text" name="mdiid" value="${mdiid}" /></td>
			</tr>
			<tr>
				<td>Field</td>
				<td><input type="text" name="field" value="${field}" /></td>
			</tr>
			<tr>
				<td>Start date</td>
				<td><input type="text" name="startdate" value="${startdate}" /></td>
			</tr>

			<tr>
				<td>End date</td>
				<td><input type="text" name="enddate" value="${enddate}" /></td>
			</tr>
			<tr>
				<td></td>
				<td align="right"><button type="submit" class="btn btn-primary">Fetch!</button></td>
			</tr>
		</table>
	</form>
</div>

<hr />
<%@include file="footer.jsp"%>
