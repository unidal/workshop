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
			<table border="0">
				<tr>
					<td colspan="2">
						Please fill in package name for corresponding namespace:
					</td>
				</tr>
				<tr>
					<td>
						Namespace
					</td>
					<td>
						Java Package Name
					</td>
				</tr>
				<c:forEach var="namespace" items="${model.namespaces}" varStatus="status">
				<tr>
					<td>
						<input type="hidden" name="namespace" value="${namespace}"/>
						${empty namespace ? "&lt;default-namespace&gt;" : ""+namespace}
					</td>
					<td>
						<input type="text" name="package" value="${model.packages[status.count-1]}" size="30"/>
					</td>
				</tr>
				</c:forEach>
				<tr>
					<td>
						<input type="submit" name="previous" value="&lt;&lt; Previous" />
					</td>
					<td>
						<input type="submit" name="next" value="Next &gt;&gt;" />
					</td>
				</tr>
			</table>
		</form>
	</c:if>
</a:body>
