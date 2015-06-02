<%@page import="org.apache.commons.lang3.StringUtils"%>
<%@page import="org.apache.commons.lang3.StringEscapeUtils"%>
<%@page import="it.jaxbservice.util.*"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="java.util.Map.Entry"%>
<%@page import="it.jaxbservice.*"%>
<!DOCTYPE html>
<%
	String serviceName = request.getParameter("serviceName");
	JaxbServiceBeanInfo serviceBeanInfo = JaxbServiceConfigSingleton.serviceMap.get(serviceName);
%>
	<html lang="en">
  <head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- The above 3 meta tags *must* come first in the head; any other head content must come *after* these tags -->
    <title>Service spec: <%= serviceName %></title>

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
<%
	if (serviceBeanInfo == null) {
%>
	<h1 align="center"> Service not Found</h1>
<%  } else { 

	String inputClass = serviceBeanInfo.getInputClassName();
	String outputClass = serviceBeanInfo.getOutputClassName();
	
	String xsdInput = "(Empty)", xsdOutput = "(Empty)";
	if (StringUtils.isNotEmpty(inputClass)) xsdInput = StringEscapeUtils.escapeXml(JaxbHelper.toXSD(inputClass));
	if (StringUtils.isNotEmpty(outputClass)) xsdOutput = StringEscapeUtils.escapeXml(JaxbHelper.toXSD(outputClass));
%>	

	<h1 class="text-center"> Service: <b><i> <%= serviceName %></i></b></h1>

	<h3 class="text-center"><%= serviceBeanInfo.getDescription() %></h3>


<div class="row">
	<div class="col-sm-6">
		<h2> Input schema </h2>
		<p> Class: <b><%= serviceBeanInfo.getInputClassName() %></b>
		<p> <pre> <%= xsdInput %> </pre>
	</div>

	<div class="col-sm-6">
		<h2> Output schema</h2>
		<p> Class: <b><%= serviceBeanInfo.getOutputClassName() %></b>
		<p> <pre><code> <%= xsdOutput %> </code></pre>
	</div>

</div>

<% } %>
</body>
</html>