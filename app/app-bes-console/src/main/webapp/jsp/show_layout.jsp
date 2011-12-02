<%@ page contentType="text/html; charset=utf-8"
	import="com.site.app.bes.console.*,com.site.app.bes.dal.*"%>
<%@ taglib prefix="a" uri="/WEB-INF/app.tld"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<jsp:useBean id="payload" class="Payload" scope="request" />
<jsp:useBean id="model" class="Model" scope="request" />

<a:body>
	<c:if test="${empty payload.user}">
		<form name="login" method="post">
			<input type="hidden" name="lastUrl" value="${payload.lastUrl}" />
			<table border="0">
				<tr>
					<td>
						User Name:
					</td>
					<td>
						<input type="text" name="username" value="${payload.username}" />
					</td>
				</tr>
				<tr>
					<td>
						Password:
					</td>
					<td>
						<input type="password" name="password" />
					</td>
				</tr>
				<tr>
					<td>
						&nbsp;
					</td>
					<td>
						<input type="submit" value="Login" />
					</td>
				</tr>
			</table>
		</form>

		<hr />
	</c:if>

	<c:if test="${!empty payload.user}">
	This is main page.
	</c:if>
</a:body>
