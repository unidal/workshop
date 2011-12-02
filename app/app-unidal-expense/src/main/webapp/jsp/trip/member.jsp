<%@ page contentType="text/html; charset=utf-8"%>
<%@ page import="org.unidal.expense.biz.trip.*"%>
<%@ taglib prefix="a" uri="/WEB-INF/app.tld"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="w" uri="/web/core"%>
<jsp:useBean id="ctx" type="Context" scope="request" />
<jsp:useBean id="payload" type="Payload" scope="request" />
<jsp:useBean id="model" type="Model" scope="request" />

<a:body>

	<form method="post" action="${a:action(model, model.trip.id)}">
	<input type="hidden" name="op" value="${model.action.name}" />
	<table border="0">
		<tr>
			<td>Trip: ${model.trip.title}</td>
		</tr>
		<tr>
			<td>Members</td>
		</tr>
		<c:forEach var="member" items="${model.members}">
			<tr>
				<td>${w:showCheckbox('mid', member, model.tripMemberIds, 'id', 'name')}</td>
			</tr>
		</c:forEach>
		<tr>
			<td><input type="submit" name="save" value="Next >>" /></td>
		</tr>
	</table>
	</form>

</a:body>