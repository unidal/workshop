<%@ page contentType="text/html; charset=utf-8"%>
<%@ page import="org.unidal.ezsell.*"%>
<%@ taglib prefix="a" uri="/WEB-INF/app.tld"%>
<%@ taglib prefix="w" uri="/web/core"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<jsp:useBean id="payload" class="EbayPayload" scope="request" />
<jsp:useBean id="model" class="EbayModel" scope="request" />

<a:body>

<a:errors>
	<title>Following errors occured, please correct it.</title>
	<error id="login.failure" field="password"><![CDATA[Incorrect email or password.]]></error>
</a:errors>

<form name="login" method="post" action="${model.pageUri}">
	<input type="hidden" name="rtnUrl" value="${payload.rtnUrl}" />
	<table border="0">
		<tr>
			<td>Email:</td>
			<td><input type="text" name="email" value="${payload.email}" /></td>
		</tr>
		<tr>
			<td>Password:</td>
			<td><input type="password" name="password" /></td>
		</tr>
		<tr>
			<td>&nbsp;</td>
			<td><input type="submit" name="login" value="Login" />&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Not a user yet, register an account <a href="${model.moduleUri}/register">here</a></td>
		</tr>
	</table>
</form>


</a:body>
