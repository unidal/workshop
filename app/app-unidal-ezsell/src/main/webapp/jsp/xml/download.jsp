<%@ page contentType="text/html; charset=utf-8"%>
<%@ page import="org.unidal.xml.*"%>
<%@ taglib prefix="a" uri="/WEB-INF/app.tld"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<jsp:useBean id="payload" class="XmlPayload" scope="request" />
<jsp:useBean id="model" class="XmlModel" scope="request" />

<a:body>
	<c:if test="${!empty payload.page}">
		<form action="${model.pageUri}" method="post">
			<input type="hidden" name="content" value="${model.content}" />
			<c:forEach var="namespace" items="${model.namespaces}">
				<input type="hidden" name="namespace" value="${namespace}"/>
			</c:forEach>
			<c:forEach var="package" items="${model.packages}">
				<input type="hidden" name="package" value="${package}"/>
			</c:forEach>
			<table border="0">
				<tr>
					<td>
						Please download the generated Java source here: <input type="submit" name="download" value="Download Java Source" />
					</td>
				</tr>
				<tr>
					<td>
						<input type="submit" name="previous" value="&lt;&lt; Previous" />
					</td>
				</tr>
				<tr>
					<td>
						<br/><br/>You can download dependency jar from here: <a href="/dependency/runtime.jar">Download Dependency Jar</a>
					</td>
				</tr>
			</table>
		</form>
	</c:if>
</a:body>
