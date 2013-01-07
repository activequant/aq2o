<%@include file="header.jsp"%>

<p>This page will provides some very basic charting possibilities.
	It can be used to peek into the server.</p>
<div id="lineChart"></div>

<div id="chart">
	<svg></svg>
</div>


<style type="text/css">
#chart svg {
	height: 400px;
}
/* tell the SVG path to be a thin blue line without any area fill */
path {
	stroke: steelblue;
	stroke-width: 1;
	fill: none;
}

.chart div {
	font: 10px sans-serif;
	background-color: steelblue;
	text-align: right;
	padding: 3px;
	margin: 1px;
	color: white;
}
</style>

<!-- reference d3.  -->
<script src='<c:url value="resources/js/d3.v3.js"/>' charset="utf-8"></script>
<script src='<c:url value="resources/js/nv.d3.js"/>' charset="utf-8"></script>
<script src='<c:url value="resources/js/lineWithFocusChart.js"/>' charset="utf-8"></script>
<style src='<c:url value="resources/css/nv.d3.css"/>' type="text/css"></style>

<script type="text/javascript">
	var width = 800;
	var height = 300;

	d3.csv('<c:url value="csv/sampledata"/>', function(data) {
		console.log("Processing data.");

		// for nv we have to transform it. 
		/*		var tdata = [ {"key":"RAND", "values" : [ [10000,1], [20000,2],[30000,3],[40000,4],[50000,5]]} ].map(function(series) {
		 series.values = series.values.map(function(d) { return {x: d[0], y: d[1] } });
		 return series;
		 });
		 */
		var tdata = [ {
			"key" : "RAND",
			"values" : data
		} ].map(function(series) {
			series.values = series.values.map(function(d) {
				return {
					x : d["TimeStampNanos"],
					y : d["RAND"] / 1000
				}
			});
			return series;
		});

		nv.addGraph(function() {
			console.log("Adding graph.");
			var chart = nv.models.lineWithFocusChart();
			// 
			chart.xAxis.tickFormat(function(d) {
				return d3.time.format('%Y-%m-%d')(new Date(d))
			});
			chart.x2Axis.tickFormat(function(d) {
				return d3.time.format('%Y-%m-%d')(new Date(d))
			});

			// chart.useVoronoi(false);
			chart.yAxis.tickFormat(d3.format(',.2f'));
			chart.y2Axis.tickFormat(d3.format(',.2f'));

			// 			
			d3.select('#chart svg').datum(tdata).call(chart);
			nv.utils.windowResize(chart.update);
			return chart;
		});

	});
</script>

<hr />
<%@include file="footer.jsp"%>
