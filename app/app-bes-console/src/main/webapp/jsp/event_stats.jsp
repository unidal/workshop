<%@ page contentType="text/html; charset=utf-8"
	import="com.site.app.bes.console.*"%>
<%@ taglib prefix="a" uri="/WEB-INF/app.tld"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<jsp:useBean id="payload" class="Payload" scope="request" />
<jsp:useBean id="model" class="Model" scope="request" />

<a:body>
	<form name="search" method="get">
	<table border="0" cellpadding="4" cellspacing="1">
		<tr>
			<td>From Date:</td>
			<td><input type="text" name="dateFrom"
				value="${a:format(payload.dateFrom,'yyyy-MM-dd')}" size="10"
				maxlength="10" /></td>
		</tr>
		<tr>
			<td>To Date:</td>
			<td><input type="text" name="dateTo"
				value="${a:format(payload.dateTo,'yyyy-MM-dd')}" size="10"
				maxlength="10" /></td>
		</tr>
		<tr>
			<td>Group By:</td>
			<td><c:forEach var="groupBy"
				items="${payload.statsGroupBy.values()}">
				<input type="radio" name="groupBy" id="${groupBy.name}"
					value="${groupBy.name}" ${payload.statsGroupBy==groupBy?'checked' : ''} >
				<label for="${groupBy.name}"> <c:out
					value="${groupBy.description}" /> </label>
			</c:forEach></td>
		</tr>
		<tr>
			<td>&nbsp;</td>
			<td><input type="submit" value="Search" /></td>
		</tr>
	</table>
	</form>

	<hr />

	<c:if test="${!empty model.eventList}">
		<table border="0">
			<tr>
				<c:set var="max" value="${a:max(model.eventList,'events')}" />
				<c:forEach var="e" items="${model.eventList}">
					<c:set var="height" value="${e.events * 200 / (max==0?1:max)}" />
					<c:set var="title">
			         Events: ${e.events}
						<c:choose>
							<c:when test="${payload.statsGroupBy.name == '15m'}">${e.creationDateIn15m}</c:when>
							<c:when test="${payload.statsGroupBy.name == '1h'}">${e.creationDateIn1h}</c:when>
							<c:when test="${payload.statsGroupBy.name == '1d'}">${e.creationDateIn1d}</c:when>
						</c:choose>
					</c:set>
					<td valign="bottom"><img
						src="${pageContext.servletContext.servletContextName}/img/blue_dot.gif"
						border="0" width="8" height="${height}" title="${title}" /></td>
				</c:forEach>
			</tr>
		</table>

		<hr />

		<table border="0" cellpadding="4" cellspacing="1" class="grid">
			<tr class="head">
				<th>${payload.statsGroupBy.description}</th>
				<th>Total</th>
				<th>Percentage(%)</th>
				<th>Successful<br />First Time</th>
				<th>Successful<br />After Retried</th>
				<th>Rescheduled</th>
				<th>Retrying</th>
				<th>Failed</th>
			</tr>
			<c:set var="sum" value="${a:sum(model.eventList,'events')}" />
			<c:forEach var="e" items="${model.eventList}">
				<c:set var="even" value="${!even}" />
				<tr align="center" class="${even?'even':'odd'}">
					<td align="left">
						<c:choose>
							<c:when test="${payload.statsGroupBy.name == '15m'}">${e.creationDateIn15m}</c:when>
							<c:when test="${payload.statsGroupBy.name == '1h'}">${e.creationDateIn1h}</c:when>
							<c:when test="${payload.statsGroupBy.name == '1d'}">${e.creationDateIn1d}</c:when>
						</c:choose>
					</td>
					<td>${e.events}</td>
					<td>${a:percentage(e.events, sum, 2)}</td>
					<td><font color="green">${e.events - model.dataSelector.getEvents(e)}</font></td>
					<td><font color="996633">${model.dataSelector.getEvents(e,1)}</font></td>
					<td>${model.dataSelector.getEvents(e,-1)}</td>
					<td>${model.dataSelector.getEvents(e,-2)}</td>
					<td><font color="red">${model.dataSelector.getEvents(e,-3)}</font>
					</td>
				</tr>
			</c:forEach>
			<tr>
				<th>Total Events</th>
				<th>${sum}</th>
				<th>${a:percentage(sum, sum, 2)}</th>
				<th><font color="green">${sum - model.dataSelector.getEvents()} (${a:percentage(sum - model.dataSelector.getEvents(), sum, 2)})</font></th>
				<th><font color="996633">${model.dataSelector.getEventsByState(1)} (${a:percentage(model.dataSelector.getEventsByState(1), sum, 2)})</font></th>
				<th>${model.dataSelector.getEventsByState(-1)} (${a:percentage(model.dataSelector.getEventsByState(-1), sum, 2)})</th>
				<th>${model.dataSelector.getEventsByState(-2)} (${a:percentage(model.dataSelector.getEventsByState(-2), sum, 2)})</th>
				<th><font color="red">${model.dataSelector.getEventsByState(-3)} (${a:percentage(model.dataSelector.getEventsByState(-3), sum, 2)})</font></th>
			</tr>
		</table>
	</c:if>
</a:body>
