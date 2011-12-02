<%@ page contentType="text/html; charset=utf-8" %>
<%@ page import="java.util.List,com.site.web.util.PageKit" %>
<%@ page import="com.site.bes.server.admin.*" %>
<%@ page import="com.site.bes.server.admin.type.*" %>
<%
AdminRequest req = (AdminRequest)request.getAttribute("REQUEST");
AdminResponse res = (AdminResponse)request.getAttribute("RESPONSE");
DashboardType d = res.getDashboard();
%>

<%@ include file="header.jsp" %>

<%
if (d!=null) {
%>
<table cellspacing="1" cellpadding="4" border="0" bgcolor="#cccccc">
   <tr>
      <td class="title">Event Type</td>
      <td><%=d.getEventType()%></td>
   </tr>
   <tr>
      <td class="title">Consumer Type</td>
      <td><%=d.getConsumerType()%></td>
   </tr>
   <tr>
      <td class="title">Status</td>
      <td>
         <%if (d.isRunning()) {%>
         <form method="post" action="<%=request.getAttribute("ACTION_URI")%>" style="margin-left:0px;margin-right:0px;margin-top:0px;margin-bottom:0px">
            <input type="hidden" name="op" value="dashboard">
            <input type="hidden" name="mode" value="stop">
            <input type="hidden" name="eventType" value="<%=d.getEventType()%>">
            <input type="hidden" name="consumerType" value="<%=d.getConsumerType()%>">
            <font color="green">RUNNING</font>&nbsp;&nbsp;<input type="submit" value="Stop Thread">
         </form>
         <%} else {%>
         <form method="post" action="<%=request.getAttribute("ACTION_URI")%>" style="margin-left:0px;margin-right:0px;margin-top:0px;margin-bottom:0px">
            <input type="hidden" name="op" value="dashboard">
            <input type="hidden" name="mode" value="start">
            <input type="hidden" name="eventType" value="<%=d.getEventType()%>">
            <input type="hidden" name="consumerType" value="<%=d.getConsumerType()%>">
            <font color="red">STOPPED</font>&nbsp;&nbsp;<input type="submit" value="Start Thread">
         </form>
         <%}%>
      </td>
   </tr>
   <tr>
      <td class="title">Last Fetched Id</td>
      <td><%=d.getLastFetchedId()%></td>
   </tr>
   <tr>
      <td class="title">Last Scheduled Date</td>
      <td><%=d.getLastScheduledDate()==null?"":PageKit.format(d.getLastScheduledDate(),PageKit.DATE_TIME)%></td>
   </tr>
   <tr>
      <td class="title">Batch Timeout</td>
      <td><%=d.getBatchTimeout()%></td>
   </tr>
   <tr>
      <td class="title">Creation Date</td>
      <td><%=PageKit.format(d.getCreationDate(),PageKit.DATE_TIME)%></td>
   </tr>
   <tr>
      <td class="title">Last Modified Date</td>
      <td><%=PageKit.format(d.getLastModifiedDate(),PageKit.DATE_TIME)%></td>
   </tr>
</table>

<form method="post" action="<%=request.getAttribute("ACTION_URI")%>">
   <input type="hidden" name="op" value="dashboard">
   <input type="hidden" name="mode" value="submit">
   <input type="hidden" name="eventType" value="<%=d.getEventType()%>">
   <input type="hidden" name="consumerType" value="<%=d.getConsumerType()%>">
   Batch Timeout(unit: seconds)<br>
   <input type="text" name="batchTimeout" value="<%=d.getBatchTimeout()%>">
   <input type="submit" value="Change Batch Timeout">
</form>
<%
} else {
%>
<table cellspacing="1" cellpadding="4" border="0">
   <tr>
      <td class="title">Event Type</td>
      <td><%=req.getEventType()%></td>
   </tr>
   <tr>
      <td class="title">Consumer Type</td>
      <td><%=req.getConsumerType()%></td>
   </tr>
</table>

<form method="post" action="<%=request.getAttribute("ACTION_URI")%>">
   <input type="hidden" name="op" value="dashboard">
   <input type="hidden" name="mode" value="submit">
   <input type="hidden" name="eventType" value="<%=req.getEventType()%>">
   <input type="hidden" name="consumerType" value="<%=req.getConsumerType()%>">
   <input type="hidden" name="batchTimeout" value="300">
   <input type="submit" value="Create Dashboard">
</form>
<%
}
%>

</body>
</html>
