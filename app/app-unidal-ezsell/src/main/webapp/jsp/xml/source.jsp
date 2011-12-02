<%@ page contentType="text/html; charset=utf-8"%>
<%@ page import="org.unidal.xml.*"%>
<%@ taglib prefix="a" uri="/WEB-INF/app.tld"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<jsp:useBean id="payload" class="XmlPayload" scope="request" />
<jsp:useBean id="model" class="XmlModel" scope="request" />

<a:body>
	<c:if test="${!empty payload.page}">
		<form action="${model.pageUri}" method="post">
			<c:forEach var="namespace" items="${model.namespaces}">
				<input type="hidden" name="namespace" value="${namespace}"/>
			</c:forEach>
			<c:forEach var="package" items="${model.packages}">
				<input type="hidden" name="package" value="${package}"/>
			</c:forEach>
			<table border="0">
				<tr>
					<td>
						Please input your XML source below, which will be used to generate Java code to parse any XML source with samiliar structure.
					</td>
				</tr>
				<tr>
					<td>
						<textarea name="content" rows="20" cols="100">${model.content}</textarea>
					</td>
				</tr>
				<tr>
					<td>
						<input type="submit" name="next" value="Next &gt;&gt;" />
					</td>
				</tr>
			</table>
		</form>
	</c:if>
</a:body>
