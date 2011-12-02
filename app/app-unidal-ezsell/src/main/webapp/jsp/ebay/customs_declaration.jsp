<%@ page contentType="text/html; charset=utf-8"%>
<%@ page import="org.unidal.ezsell.*"%>
<%@ taglib prefix="a" uri="/WEB-INF/app.tld"%>
<%@ taglib prefix="w" uri="/web/core"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<jsp:useBean id="payload" class="EbayPayload" scope="request" />
<jsp:useBean id="model" class="EbayModel" scope="request" />

<a:body>

<form method="post" action="${model.pageUri}">
	<input type="hidden" name="id" value="${payload.id}"/>
	<input type="hidden" name="lastUrl" value="${payload.lastUrl}"/>
	<table border="0">
		<tr>
			<td colspan="2">CUSTOMS DECLARATION (CHINA POST).</td>
		</tr>
		<tr>
			<td>From Name:</td>
			<td><input type="text" name="fromName" value="${w:htmlEncode(model.seller.name)}" size="40" readonly disabled/></td>
		</tr>
		<tr>
			<td>From Address:</td>
			<td><textarea name="fromAddress" rows="4" cols="60" readonly disabled>${w:htmlEncode(model.seller.address)}</textarea></td>
		</tr>
		<tr>
			<td>Ship To Name:</td>
			<td><input type="text" name="toName" value="${w:htmlEncode(model.customsDeclaration.toName)}" size="40"/></td>
		</tr>
		<tr>
			<td>Ship To Address:</td>
			<td><textarea name="toAddress" rows="4" cols="60">${w:htmlEncode(model.customsDeclaration.toAddress)}</textarea></td>
		</tr>
		<tr>
			<td>Shipping Tracking No:</td>
			<td><input type="text" name="trackingNo" value="${w:htmlEncode(model.customsDeclaration.trackingNo)}"/></td>
		</tr>
		<tr>
			<td colspan="2">Including country of destination and Tel No.</td>
		</tr>
		<tr>
			<td>Item Title:</td>
			<td><input type="text" name="itemTitle" value="${w:htmlEncode(model.customsDeclaration.itemTitle)}" size="50"/></td>
		</tr>
		<tr>
			<td>Item Quantity:</td>
			<td><input type="text" name="itemQuantity" value="${model.customsDeclaration.itemQuantity}" size="5"/></td>
		</tr>
		<tr>
			<td>Net Weight:</td>
			<td><input type="text" name="weight" value="${model.customsDeclaration.weight}" size="5"/></td>
		</tr>
		<tr>
			<td>Item Value:</td>
			<td><input type="text" name="itemValue" value="${model.customsDeclaration.itemValue}" size="5"/></td>
		</tr>
		<tr>
			<td>Original Country:</td>
			<td><input type="text" name="origin" value="${model.customsDeclaration.origin}" size="5"/></td>
		</tr>
		<tr>
			<td colspan="2">
				<input type="submit" name="submit" value="Generate Customs Declaration PDF"/>
				<c:if test="${payload.id > 0}">
				   &nbsp;&nbsp;<a href="?id=${payload.id}&lastUrl=${w:urlEncode(payload.lastUrl)}">Back</a>
				</c:if>
			</td>
		</tr>
	</table>
</form>

</a:body>
