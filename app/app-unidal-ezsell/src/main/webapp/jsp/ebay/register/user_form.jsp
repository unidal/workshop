<%@ page contentType="text/html; charset=utf-8"%>
<%@ page import="org.unidal.ezsell.*"%>
<%@ taglib prefix="a" uri="/WEB-INF/app.tld"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<jsp:useBean id="ctx" class="EbayContext" scope="request" />
<jsp:useBean id="payload" class="EbayPayload" scope="request" />
<jsp:useBean id="model" type="EbayModel" scope="request" />

<a:register>

Please register with email address and password.

<a:errors>
	<title>Following errors occured, please correct it.</title>
	<error id="email.required" field="email"><![CDATA[Email is required and must be in the format of <b>mailbox@domain.com</b>]]></error>
	<error id="password.required" field="password">Password is required</error>
</a:errors>

<form method="post" action="${model.pageUri}">
	<table border="0">
		<tr>
			<td><label id="email">Email:</label></td>
			<td><input type="text" name="email" value="${payload.email}" /></td>
		</tr>
		<tr>
			<td>Password:</td>
			<td><input type="password" name="password" /></td>
		</tr>
		<tr>
			<td>&nbsp;</td>
			<td><input type="submit" name="submit" value="Continue" /></td>
		</tr>
	</table>
</form>

</a:register>