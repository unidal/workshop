<%@ page contentType="text/html; charset=utf-8"%>
<%@ page import="org.unidal.ezsell.*"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="a" uri="/WEB-INF/app.tld"%>
<jsp:useBean id="payload" class="EbayPayload" scope="request" />
<jsp:useBean id="model" type="EbayModel" scope="request" />

<a:register>

Please fill in your eBay seller account to link:

<form name="register" method="post" action="${model.pageUri}">
	<input type="hidden" name="step" value="${payload.step.id}"/>
	<input type="hidden" name="userId" value="${payload.userId}"/>
	<table border="0">
		<c:choose>
		   <c:when test="${empty payload.ebayAccountLinkUrl}">
				<tr>
					<td>eBay Account:</td>
					<td><input type="text" name="ebayAccount" value="${payload.ebayAccount}" /></td>
				</tr>
		   </c:when>
		   <c:otherwise>
				<tr>
					<td>eBay Account:</td>
					<td><input type="text" name="ebayAccount" value="${payload.ebayAccount}" readonly/></td>
				</tr>
				<tr>
					<td>eBay Auth Token:</td>
					<td>
						Click <a href="${payload.ebayAccountLinkUrl}" target="_blank"><b>HERE</b></a> to link to your ebay account. <br/>
						<textarea name="ebayAuthToken" rows="10" cols="100"></textarea>
					</td>
				</tr>
		   </c:otherwise>
		</c:choose>
		<tr>
			<td>&nbsp;</td>
			<td><input type="submit" name="submit" value="Continue"/></td>
		</tr>
	</table>
</form>

</a:register>