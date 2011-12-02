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
					Page URL:
				</td>
				<td>
					<input type="text" name="url" value="${payload.pageUrl}" size="30"
						maxlength="100" />
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

	<c:if test="${empty model.pageVisit and !empty payload.pageUrl}">
		No Record found for page ${payload.pageUrl}
	</c:if>
	<c:if test="${!empty model.pageVisit}">
		<h2>
			Page Detail Info
		</h2>
		<table border="0">
			<tr>
				<th>
					Page URL:
				</th>
				<td>
					<a target="_blank" href="http://www.xiaoxishu.com${model.pageVisit.pageUrl}">${model.pageVisit.pageUrl}</a>
				</td>
			</tr>
			<tr>
				<th>
					Total Visits:
				</th>
				<td>
					${model.pageVisit.totalVisits}
				</td>
			</tr>
			<tr>
				<th>
					First Visit Date:
				</th>
				<td>
					${model.pageVisit.creationDate}
				</td>
			</tr>
			<tr>
				<th>
					Last Visit Date:
				</th>
				<td>
					${model.pageVisit.lastModifiedDate}
				</td>
			</tr>
		</table>

		<h2>
			Group by On Top
		</h2>
		<table border="0">
			<tr class="head">
				<th>
					Is On Top?
				</th>
				<th>
					Total Visits
				</th>
			</tr>
			<c:forEach var="e" items="${model.dataSelector.onTopMap.entrySet()}">
				<tr>
					<th>
						${e.key}
					</th>
					<td>
						${e.value}
					</td>
				</tr>
			</c:forEach>
		</table>

		<h2>
			Group by Main Category
		</h2>
		<table border="0">
			<tr class="head">
				<th>
					Main Category
				</th>
				<th>
					Total Visits
				</th>
			</tr>
			<c:forEach var="e"
				items="${model.dataSelector.category1Map.entrySet()}">
				<tr>
					<th>
						${e.key}
					</th>
					<td>
						${e.value}
					</td>
				</tr>
			</c:forEach>
		</table>

		<h2>
			Group by Second Category
		</h2>
		<table border="0">
			<tr class="head">
				<th>
					Second Category
				</th>
				<th>
					Total Visits
				</th>
			</tr>
			<c:forEach var="e"
				items="${model.dataSelector.category2Map.entrySet()}">
				<tr>
					<th>
						${e.key}
					</th>
					<td>
						${e.value}
					</td>
				</tr>
			</c:forEach>
		</table>
	</c:if>
</a:body>
