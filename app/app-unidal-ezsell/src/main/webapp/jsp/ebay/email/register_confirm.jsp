<%@ page contentType="text/xml; charset=utf-8"%>
<%@ page import="org.unidal.ezsell.*"%>
<%@ taglib prefix="w" uri="/web/core"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<jsp:useBean id="payload" class="EbayPayload" scope="request" />
<jsp:useBean id="model" class="EbayModel" scope="request" />

<c:set var="subject">Thank you for your registration.</c:set>
<c:set var="textContent">Please visit ${model.pageUri}?token=${model.token} to confirm your email.</c:set>
<c:set var="htmlContent">Please click here: <a href="${model.pageUri}?token=${model.token}">Confirm</a> your email.</c:set>

<jsp:useBean id="email" scope="request" class="org.unidal.expense.view.Email">
	<jsp:setProperty name="email" property="subject" value="${subject}"/>
	<jsp:setProperty name="email" property="textContent" value="${textContent}"/>
	<jsp:setProperty name="email" property="htmlContent" value="${htmlContent}"/>
</jsp:useBean>
