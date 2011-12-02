<%@ page contentType="text/html; charset=utf-8"%>
<%@ page import="org.unidal.weather.web.*"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="w" uri="/web/core"%>
<%@ taglib prefix="a" uri="/WEB-INF/app.tld"%>
<jsp:useBean id="payload" class="WebPayload" scope="request" />
<jsp:useBean id="model" class="WebModel" scope="request" />

<a:body>

<table border="1">
<tr>
<td colspan="4">${model.today.code}&#176;</td>
</tr>
<tr>
<td>${model.cityName}<br/>H:${model.today.high}&#176; L:${model.today.low}&#176;</td>
<td>&nbsp;</td>
<td colspan="2">${model.today.current}&#176;</td>
</tr>
<c:forEach var="weather" items="${model.nextDays}" varStatus="status">
<tr>
<td>${weather.dayInString}</td>
<td><img src="/img/${weather.code}.gif"/></td>
<td>${weather.high}&#176;</td>
<td>${weather.low}&#176;</td>
</tr>
</c:forEach>
<tr>
<td colspan="4">Updated: ${w:format(model.lastUpdatedTime,'yyyy-MM-dd HH:mm:ss')}</td>
</tr>
</table>

</a:body>