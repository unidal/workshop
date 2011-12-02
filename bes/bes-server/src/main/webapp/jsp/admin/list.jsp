<%@ page contentType="text/html; charset=utf-8" %>
<%@ page import="java.util.List,com.site.web.util.PageKit" %>
<%@ page import="com.site.bes.server.admin.*" %>
<%@ page import="com.site.bes.server.admin.type.*" %>
<%
AdminRequest req = (AdminRequest)request.getAttribute("REQUEST");
AdminResponse res = (AdminResponse)request.getAttribute("RESPONSE");
AdminSortedByEnum sortedBy = req.getSortedBy();
%>

<%@ include file="header.jsp" %>

<%
if (sortedBy==AdminSortedByEnum.EVENT) {
   List events = res.getEventList();
%>
<table border="0" cellpadding="4" cellspacing="1" bgcolor="#cccccc">
   <tr>
      <td class="title" rowspan="2">Event Type</td>
      <td class="title" colspan="4" align="center">Consumed By</td>
   </tr>
   <tr>
      <td class="title">Consumer Type</td>
      <td class="title">Status</td>
      <td class="title">Check Interval</td>
      <td class="title">Action</td>
   </tr>
   <%
   for (int i=0;i<events.size();i++) {
      EventType event = (EventType)events.get(i);
      List consumers = event.getConsumerList();
      int size = consumers.size();

      for (int j=0;j<size;j++) {
         ConsumerType consumer = (ConsumerType)consumers.get(j);
   %>
   <tr class="<%=(i%2==1?"odd":"even")%>">
      <%if (j==0) {%>
      <td rowspan="<%=size%>"><%=event.getEventType()%></td>
      <%}%>
      <td><%=consumer.getConsumerType()%></td>
      <td align="center"><font color="<%="Enabled".equals(consumer.getStatus())?"green":"red"%>"><%=consumer.getStatus()%></font></td>
      <td align="center"><%=PageKit.toTime(consumer.getCheckInterval())%></td>
      <td align="center"><a href="?op=dashboard&eventType=<%=event.getEventType()%>&consumerType=<%=consumer.getConsumerType()%>">View</a></td>
   </tr>
   <%
      }
   }
   %>
</table>
<%
} else if (sortedBy==AdminSortedByEnum.CONSUMER) {
   List consumers = res.getConsumerList();
%>
<table border="0" cellpadding="4" cellspacing="1" bgcolor="#cccccc">
   <tr>
      <td rowspan="2" class="title">Consumer Type</td>
      <td rowspan="2" class="title">Status</td>
      <td rowspan="2" class="title">Check Interval</td>
      <td colspan="2" class="title" align="center">Listen On</td>
   </tr>
   <tr>
      <td class="title">Event Type</td>
      <td class="title">Action</td>
   </tr>
   <%
   for (int i=0;i<consumers.size();i++) {
      ConsumerType consumer = (ConsumerType)consumers.get(i);
      List events = consumer.getEventList();
      int size = events.size();

      for (int j=0;j<size;j++) {
         EventType event = (EventType)events.get(j);
   %>
   <tr class="<%=(i%2==1?"odd":"even")%>">
      <%if (j==0) {%>
      <td rowspan="<%=size%>"><%=consumer.getConsumerType()%></td>
      <td rowspan="<%=size%>" align="center"><font color="<%="Enabled".equals(consumer.getStatus())?"green":"red"%>"><%=consumer.getStatus()%></font></td>
      <td rowspan="<%=size%>" align="center"><%=PageKit.toTime(consumer.getCheckInterval())%></td>
      <%}%>
      <td><%=event.getEventType()%></td>
      <td align="center"><a href="?op=dashboard&eventType=<%=event.getEventType()%>&consumerType=<%=consumer.getConsumerType()%>">View</a></td>
   </tr>
   <%
      }
   }
   %>
</table>
<%
}
%>

</body>
</html>
