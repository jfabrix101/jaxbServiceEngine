<%@page import="it.jaxbservice.JaxbServiceConfigSingleton"%>
<%@page import="java.util.Map.Entry"%>
<!DOCTYPE html>
  <html lang="en">
  <head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- The above 3 meta tags *must* come first in the head; any other head content must come *after* these tags -->
    <title>Test Service</title>

    <!-- Bootstrap -->
    <link href="css/bootstrap.min.css" rel="stylesheet">

    <!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
    <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
    <!--[if lt IE 9]>
      <script src="https://oss.maxcdn.com/html5shiv/3.7.2/html5shiv.min.js"></script>
      <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
    <![endif]-->
  </head>
  <body>

<div class="container">

	
	
	<h2> TEST FORM </h2>
	
	<p> Your current IP : <%=request.getRemoteAddr()%>
	
	<form class="form" method="post" action="<%=request.getContextPath()%>/service">
	
	<p> ServiceName: <select name="serviceName">
		<option value=""></option>
		<%
			for (String key : JaxbServiceConfigSingleton.serviceMap.keySet()) {
		%>
		<option value="<%= key %>"> <%= key  %> </option>
		<% } %>
	</select> 
	&nbsp; &nbsp; <input type="checkbox" name="traceMode" value="true" /> TraceMode
	
	<p> (Param1) <input type="text" name="param1" value="">
	<p> (Param2) <input type="text" name="param2" value="">
	
	<p> <textarea rows="10" cols="40" name="payload"></textarea>
	
	<p> <input type="submit" value="Invia" />
	</form>
</div>
<!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.2/jquery.min.js"></script>
<!-- Include all compiled plugins (below), or include individual files as needed -->
<script src="js/bootstrap.min.js"></script>
</body>
</html>
</html>