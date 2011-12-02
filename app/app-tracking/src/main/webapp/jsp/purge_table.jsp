<%@ page contentType="text/html; charset=utf-8"
	import="com.site.app.tracking.analysis.*"%>
<%@ taglib prefix="a" uri="/WEB-INF/app.tld"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<jsp:useBean id="payload" class="Payload" scope="request" />
<jsp:useBean id="model" class="Model" scope="request" />

<a:body>
	<form name="search" method="get">
		<table border="0">
			<tr>
				<td>
					Table:
				</td>
				<td>
					<c:forEach var="purgeTable" items="${payload.purgeTable.values()}">
						<input type="radio" name="purgeTable" id="${purgeTable.name}"
							value="${purgeTable.name}" ${payload.purgeTable==purgeTable?'checked' : ''} >
						<label for="${purgeTable.name}">
							<c:out value="${purgeTable.description}" />
						</label>
					</c:forEach>
				</td>
			</tr>
			<tr>
				<td>
					Rows Created Before:
				</td>
				<td>
					<input type="text" name="dateTo"
						value="${a:format(payload.dateTo,'yyyy-MM-dd')}" size="10"
						maxlength="10" />
				</td>
			</tr>
			<tr>
				<td>
					&nbsp;
				</td>
				<td>
					<c:forEach var="action" items="${payload.purgeTableAction.values()}">
						<input type="submit" name="action" value="${action.name}">
					</c:forEach>
				</td>
			</tr>
		</table>
	</form>

	<hr />

	<c:if test="${payload.purgeTableAction.name == 'Check'}">
	   ${model.purgeTableCount} rows meet the criteria.
	</c:if>
	<c:if test="${payload.purgeTableAction.name == 'Submit'}">
	   ${model.purgeTableCount} rows have been purged.
	</c:if>
</a:body>
