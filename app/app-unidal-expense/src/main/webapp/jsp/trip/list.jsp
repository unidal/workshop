<%@ page contentType="text/html; charset=utf-8"%>
<%@ page import="org.unidal.expense.biz.trip.*"%>
<%@ taglib prefix="a" uri="/WEB-INF/app.tld"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<jsp:useBean id="ctx" type="Context" scope="request" />
<jsp:useBean id="payload" type="Payload" scope="request" />
<jsp:useBean id="model" type="Model" scope="request" />

<a:body>

	<a href="${a:uri2(model, null, 'op=add')}">New Trip</a>

	<form method="post" action="${model.pageUri}">
	<table border="1" cellspadding="2" cellspacing="0">
		<tr>
			<th>Trip</th>
		</tr>
		<c:forEach var="trip" items="${model.trips}">
			<tr>
				<td><a href="${a:uri(model, trip.id)}">${trip.title}</a></td>
			</tr>
		</c:forEach>
	</table>
	</form>

</a:body>