<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@page pageEncoding="UTF-8"%>
<%@page isELIgnored="false"%>

<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="utf-8">

<meta name='viewport' content='width=device-width, initial-scale=1.0'>
<link href='<c:url value="resources/css/bootstrap.css"/>'
	rel='stylesheet'>
<style type="text/css">
body {
	padding-top: 60px;
	padding-bottom: 40px;
}
</style>
<link href='<c:url value="resources/css/bootstrap-responsive.css"/>'
	rel='stylesheet'>



<!-- HTML5 shim, for IE6-8 support of HTML5 elements -->
<!--[if lt IE 9]>
      <script src="http://html5shim.googlecode.com/svn/trunk/html5.js"></script>
    <![endif]-->
<!-- Fav and touch icons -->
<link rel="apple-touch-icon-precomposed" sizes="144x144"
	href='<c:url value="resources/img/icon-144.png"/>'>
<link rel="apple-touch-icon-precomposed" sizes="114x114"
	href='<c:url value="resources/img/icon-114.png"/>'>
<link rel="apple-touch-icon-precomposed" sizes="72x72"
	href='<c:url value="resources/img/icon-72.png"/>'>
<link rel="apple-touch-icon-precomposed"
	href='<c:url value="resources/img/icon-57.png"/>'>
<link rel="shortcut icon"
	href='<c:url value="resources/img/icon-57.png"/>'>





</head>
<body>
	<div class="navbar navbar-inverse navbar-fixed-top">
		<div class="navbar-inner">
			<div class="container">
				<a class="btn btn-navbar" data-toggle="collapse"
					data-target=".nav-collapse"> <span class="icon-bar"></span> <span
					class="icon-bar"></span> <span class="icon-bar"></span>
				</a> <a class="brand" href="/">AQ Master Server</a>
				<div class="nav-collapse collapse">
					<ul class="nav">
						<li><a href="/">Home</a></li>
						<li><a href="charting">Charting</a></li>
						<li class="dropdown"><a href="#" class="dropdown-toggle"
							data-toggle="dropdown">Administration <b class="caret"></b></a>
							<ul class="dropdown-menu">
								<li><a href="server">Server</a></li>
								<li><a href="components">Components</a></li>
							</ul></li>


						<li class="dropdown"><a href="#" class="dropdown-toggle"
							data-toggle="dropdown">Instruments <b class="caret"></b></a>
							<ul class="dropdown-menu">

								<li><a href="instruments">Instruments</a></li>
								<li><a href="mdis">Market data instruments</a></li>
								<li><a href="tdis">Tradeable instruments</a></li>
							</ul></li>


						<li class="dropdown"><a href="#" class="dropdown-toggle"
							data-toggle="dropdown">Help <b class="caret"></b></a>
							<ul class="dropdown-menu">

								<li><a href="documentation">Documentation</a></li>
								<li class="divider"></li>
								<li><a href="about">About</a></li>
								<li><a href="license">License</a></li>
								<li><a href="http://server.activequant.com/download.html">Download
										page</a></li>
								<li class="divider"></li>
								<li><a href="http://activequant.com/contact">Contact
										&amp; Consulting</a></li>

							</ul></li>
					</ul>

				</div>
				<!--/.nav-collapse -->
			</div>
		</div>
	</div>

	<div class="container">