<%@ page contentType="text/html; charset=utf-8" %>
<%@ page import="java.util.List,com.site.web.util.PageKit" %>
<%@ page import="com.site.bes.server.admin.*" %>
<%@ page import="com.site.bes.server.admin.type.*" %>
<%
AdminRequest req = (AdminRequest)request.getAttribute("REQUEST");
AdminResponse res = (AdminResponse)request.getAttribute("RESPONSE");
EventInfoType e = res.getEventInfo();
List consumptionList = res.getConsumptionList();
%>

<%@ include file="header.jsp" %>

<%
if (e!=null) {
%>
<h3>Event Info</h3>
<table cellspacing="1" cellpadding="4" border="0" bgcolor="#cccccc">
   <tr>
      <td class="title">Event Id</td>
      <td><%=e.getEventId()%></td>
   </tr>
   <tr>
      <td class="title">Event Type</td>
      <td><%=e.getEventType()%></td>
   </tr>
   <tr>
      <td class="title">Producer Type</td>
      <td><%=e.getProducerType()%></td>
   </tr>
   <tr>
      <td class="title">Producer Id</td>
      <td><%=e.getProducerId()%></td>
   </tr>
   <tr>
      <td class="title">Max Retry Times</td>
      <td><%=e.getMaxRetryTimes()<=0?"N/A":""+e.getMaxRetryTimes()%></td>
   </tr>
   <tr>
      <td class="title">Creation Date</td>
      <td><%=PageKit.format(e.getCreationDate(),PageKit.DATE_TIME)%></td>
   </tr>
   <tr>
      <td class="title">Payload Format</td>
      <td><%=e.getPayloadFormat()%></td>
   </tr>
   <tr>
      <td class="title">Payload</td>
      <td><xmp><%=e.getPayload()%></xmp></td>
   </tr>
</table>

<h3>Consumption List</h3>
<table cellspacing="1" cellpadding="4" border="0" bgcolor="#cccccc">
   <tr>
      <td class="title">Consumer Type</td>
      <td class="title">Consumer Id</td>
      <td class="title">Event State</td>
      <td class="title">Retried Times</td>
      <td class="title">Next Schedule Date</td>
   </tr>
   <% 
   for (int i = 0; i < consumptionList.size(); i++) { 
      ConsumptionType c = (ConsumptionType) consumptionList.get(i);
   %>
   <tr>
      <td><%=c.getConsumerType()%></td>
      <td><%=c.getConsumerId()%></td>
      <td><%=c.getEventState().getName()%></td>
      <td><%=c.getRetriedTimes()%></td>
      <td><%=c.getNextScheduleDate()==null?"":PageKit.format(c.getNextScheduleDate(),PageKit.DATE_TIME)%></td>
   </tr>
   <%
   } 
   %>
</table>
<%
}
%>

</body>
</html>
