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
					<c:forEach var="groupBy" items="${payload.statsGroupBy.values()}">
						<input type="radio" name="groupBy" id="${groupBy.name}"
							value="${groupBy.name}" ${payload.statsGroupBy==groupBy?'checked' : ''} >
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
			<tr>
				<c:set var="max" value="${a:max(model.pageVisitLogs,'totalVisits')}"/>
			   <c:forEach var="e" items="${model.pageVisitLogs}">
			      <c:set var="height">${e.totalVisits * 200 / (max==0?1:max)}</c:set>
			      <c:set var="title">
			         Visits: ${e.totalVisits}
						<c:choose>
							<c:when test="${payload.statsGroupBy.name == '15m'}">${e.creationDateIn15m}</c:when>
							<c:when test="${payload.statsGroupBy.name == '30m'}">${e.creationDateIn30m}</c:when>
							<c:when test="${payload.statsGroupBy.name == '1h'}">${e.creationDateIn1h}</c:when>
							<c:when test="${payload.statsGroupBy.name == '1d'}">${e.creationDateIn1d}</c:when>
						</c:choose>
			      </c:set>
				   <td valign="bottom">
				      <img src="${pageContext.servletContext.servletContextName}/img/blue_dot.gif" border="0" width="8" height="${height}" title="${title}"/>
				   </td>
				</c:forEach>
			</tr>
		</table>		

		<hr />

		<table border="0">
			<tr class="head">
				<th>
					${payload.statsGroupBy.description}
				</th>
				<th>
					Visits
				</th>
				<th>
					Percentage(%)
				</th>
			</tr>
			<c:set var="sum" value="${a:sum(model.pageVisitLogs,'totalVisits')}"/>
			<c:forEach var="e" items="${model.pageVisitLogs}">
				<c:set var="even" value="${!even}" />
				<tr ${even ? "" : "class='strip'"}>
					<td>
						<c:choose>
							<c:when test="${payload.statsGroupBy.name == '15m'}">${e.creationDateIn15m}</c:when>
							<c:when test="${payload.statsGroupBy.name == '30m'}">${e.creationDateIn30m}</c:when>
							<c:when test="${payload.statsGroupBy.name == '1h'}">${e.creationDateIn1h}</c:when>
							<c:when test="${payload.statsGroupBy.name == '1d'}">${e.creationDateIn1d}</c:when>
						</c:choose>
					</td>
					<td align="center">
						${e.totalVisits}
					</td>
					<td align="center">
						${a:percentage(e.totalVisits, model.pageVisit.totalVisits, 2)}
					</td>
				</tr>
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
