<%
com.site.web.page.PageContext ctx = (com.site.web.page.PageContext)request.getAttribute("CONTEXT");
String strSortedBy = ctx.getParameter("sortedBy","");
%>
<html>
<head>
<title>BES System Console</title>
<style stype="text/css">
body, table {font:normal 12px Arial, Helvetica, sans-serif;color:#000}
table tr.odd td {height:28px;background-color:#f8f8f8}
table tr.even td {height:28px;background-color:#ffffff}
table tr td.title {height:28px;background-color:#e6e6e6}
table tr td {height:28px;background-color:#ffffff}
</style>
</head>

<body>
<h2>BES System Console</h2>

<div>
   <b>List By: </b>
   <%if (strSortedBy.equals("event")) {%>
   <a href="?sortedBy=consumer">Consumer</a> | Event
   <%} else if (strSortedBy.equals("consumer")) {%>
   Consumer | <a href="?sortedBy=event">Event</a>
   <%} else {%>
   <a href="?sortedBy=consumer">Consumer</a> | <a href="?sortedBy=event">Event</a>
   <%}%>
</div>

<form method="post" action="<%=request.getAttribute("ACTION_URI")%>">
<input type="hidden" name="op" value="event">
Event Id: <input type="text" name="eventId" value="<%=ctx.getParameter("eventId","")%>">
&nbsp;<input type="submit" value="Get Event Info">
</form>
