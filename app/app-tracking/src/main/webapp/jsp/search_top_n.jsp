<%@ page contentType="text/html; charset=utf-8"
	import="com.site.app.tracking.analysis.*"%>
<%@ taglib prefix="a" uri="/WEB-INF/app.tld"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<jsp:useBean id="payload" class="Payload" scope="request" />
<jsp:useBean id="model" class="Model" scope="request" />

<a:body>
	<form name="search" method="get">
		<input type="hidden" name="maxNum" value="${payload.maxNum}" />
		<table border="0">
			<tr>
				<td>
					From Date:
				</td>
				<td>
					<input type="text" name="dateFrom"
						value="${a:format(payload.dateFrom,'yyyy-MM-dd')}" size="10"
						maxlength="10" />
				</td>
			</tr>
			<tr>
				<td>
					To Date:
				</td>
				<td>
					<input type="text" name="dateTo"
						value="${a:format(payload.dateTo,'yyyy-MM-dd')}" size="10"
						maxlength="10" />
				</td>
			</tr>
			<tr>
				<td>
					Group By:
				</td>
				<td>
					<c:forEach var="groupBy" items="${payload.topnGroupBy.values()}">
						<input type="radio" name="groupBy" id="${groupBy.name}"
							value="${groupBy.name}" ${payload.topnGroupBy==groupBy?'checked' : ''} >
						<label for="${groupBy.name}">
							<c:out value="${groupBy.description}" />
						</label>
					</c:forEach>
				</td>
			</tr>
			<tr>
				<td>
					&nbsp;
				</td>
				<td>
					<input type="submit" value="Search" />
				</td>
			</tr>
		</table>
	</form>

	<hr />

	<c:if test="${!empty model.pageVisitLogs}">
		<table border="0">
			<tr class="head">
				<th>
					${payload.topnGroupBy.description}
				</th>
				<th>
					Visits
				</th>
				<th>
					Percentage(%)
				</th>
			</tr>
			<c:forEach var="e" items="${model.pageVisitLogs}">
				<c:set var="even" value="${!even}" />
				<tr class="${even?'':'strip'}">
					<td>
						<c:choose>
							<c:when test="${payload.topnGroupBy.name == 'page'}">
								<a href="detail?url=${e.pageVisit.pageUrl}">${e.pageVisit.pageUrl}</a>
							</c:when>
							<c:when test="${payload.topnGroupBy.name == 'source'}">${e.fromPage}</c:when>
							<c:when test="${payload.topnGroupBy.name == 'category1'}">${e.category1}</c:when>
							<c:when test="${payload.topnGroupBy.name == 'category2'}">${e.category2}</c:when>
							<c:when test="${payload.topnGroupBy.name == 'onTop'}">${e.onTop ? 'Yes' : 'No'}</c:when>
						</c:choose>
					</td>
					<td align="center">
						${e.totalVisits}
					</td>
					<td align="center">
						${a:percentage(e.totalVisits, model.pageVisit.totalVisits, 2)}
					</td>
				</tr>
				<c:set var="sum" value="${sum + e.totalVisits}" />
			</c:forEach>
			<tr>
				<th>
					Total Visits
				</th>
				<th>
					${sum}
				</th>
				<th>
					${a:percentage(sum, model.pageVisit.totalVisits, 2)}
				</th>
			</tr>
		</table>
	</c:if>
</a:body>
