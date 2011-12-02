<%@ page contentType="text/plain; charset=utf-8" import="com.site.app.bes.console.*"%>
<%@ taglib prefix="a" uri="/WEB-INF/app.tld"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<jsp:useBean id="payload" class="Payload" scope="request" />
<jsp:useBean id="model" class="Model" scope="request" />

<% out.clear(); %>
Event Daily Report
<c:if test="${!empty model.eventList}">
<c:forEach var="e" items="${model.eventList}">
Date: ${a:format(payload.dateFrom, "yyyy-MM-dd")}
Total: ${e.events}
Success(1st time): ${e.events - model.dataSelector.getEvents(e)}
Success(retried): ${model.dataSelector.getEvents(e,1)}
Failure: ${model.dataSelector.getEvents(e,-3)}
</c:forEach>
</c:if>