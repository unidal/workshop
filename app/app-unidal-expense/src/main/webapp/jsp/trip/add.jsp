<%@ page contentType="text/html; charset=utf-8"%>
<%@ page import="org.unidal.expense.biz.trip.*"%>
<%@ taglib prefix="a" uri="/WEB-INF/app.tld"%>
<jsp:useBean id="ctx" type="Context" scope="request" />
<jsp:useBean id="payload" type="Payload" scope="request" />
<jsp:useBean id="model" type="Model" scope="request" />

<a:body>

<form method="post" action="${model.pageUri}">
	<input type="hidden" name="op" value="${model.action.name}" />
	<table border="0">
		<tr>
			<td><label id="title">Trip:</label></td>
			<td><input type="text" name="title" value="${payload.title}" /></td>
		</tr>
		<tr>
			<td>&nbsp;</td>
			<td><input name="save" type="submit" value="Next >>" /></td>
		</tr>
	</table>
</form>

</a:body>