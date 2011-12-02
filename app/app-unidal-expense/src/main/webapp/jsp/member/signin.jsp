<%@ page contentType="text/html; charset=utf-8"%>
<%@ page import="org.unidal.expense.biz.member.signin.*"%>
<%@ taglib prefix="a" uri="/WEB-INF/app.tld"%>
<jsp:useBean id="ctx" type="Context" scope="request" />
<jsp:useBean id="payload" type="Payload" scope="request" />
<jsp:useBean id="model" type="Model" scope="request" />

<a:body>

<form name="login" method="post" action="${model.pageUri}">
	<input type="hidden" name="rtnUrl" value="${payload.rtnUrl}" />
	<table border="0">
		<tr>
			<td>Account:</td>
			<td><input type="text" name="account" value="${payload.account}" /></td>
		</tr>
		<tr>
			<td>Password:</td>
			<td><input type="password" name="password" /></td>
		</tr>
		<tr>
			<td>&nbsp;</td>
			<td><input type="submit" name="login" value="Login" />&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Not a member yet, register <a href="${model.moduleUri}/register">here</a>!</td>
		</tr>
	</table>
</form>

</a:body>
