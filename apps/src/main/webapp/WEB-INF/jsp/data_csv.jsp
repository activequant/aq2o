<%@include file="header.jsp"%>

<p>
	<i>Start date and end date must be in format YYYYMMDD. Typical
		fields are BID, ASK, OPEN, etc.</i>
</p>

<div class="input">
	<form action="csv/">
		<table>
			<tr>
				<td>Series ID</td>
				<td><input type="text" name="SERIESID" value="${seriesid}" /></td>
			</tr>
			<tr>
				<td>Frequency</td>
				<td><input type="text" name="FREQ" value="${freq}" /></td>
			</tr>
			<tr>
				<td>Field</td>
				<td><input type="text" name="FIELD" value="${field}" /></td>
			</tr>
			<tr>
				<td>Start date</td>
				<td><input type="text" name="STARTDATE" value="${startdate}" /></td>
			</tr>

			<tr>
				<td>End date</td>
				<td><input type="text" name="ENDDATE" value="${enddate}" /></td>
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
