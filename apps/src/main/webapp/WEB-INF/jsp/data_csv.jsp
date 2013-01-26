<%@include file="header.jsp"%>

<h3>Time series inspector</h3>
<p>Use this form to parametrize a request to this server's CSV interface. </p>
<p>
	<i>Start date and end date must be in format YYYYMMDD. Typical
		fields are BID, ASK, OPEN, etc. Typical frequencies are RAW, EOD, HOURS_1, MINUTES_1. </i>
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
