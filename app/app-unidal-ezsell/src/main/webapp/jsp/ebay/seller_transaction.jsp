<%@ page contentType="text/html; charset=utf-8"%>
<%@ page import="org.unidal.ezsell.*"%>
<%@ taglib prefix="a" uri="/WEB-INF/app.tld"%>
<%@ taglib prefix="w" uri="/web/core"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<jsp:useBean id="payload" class="EbayPayload" scope="request" />
<jsp:useBean id="model" class="EbayModel" scope="request" />

<a:body>

<form method="post" action="${model.pageUri}">
	<input type="hidden" name="id" value="${model.transaction.id}"/>
	<input type="hidden" name="lastUrl" value="${payload.lastUrl}"/>
	<table border="0">
		<tr>
			<td>Shipping Tracking Number</td>
			<td>
				<input type="text" name="trackingNumber" value="${payload.trackingNumber}" size="40" maxlength="20">
				<input type="submit" name="mas" value="Mark As Shipped"/>
			</td>
		</tr>
		<tr>
			<td>Labels</td>
			<td>
				<input type="text" name="labels" value="${payload.labels}" size="40">
				<input type="submit" name="ul" value="Update Labels"/>
			</td>
		</tr>
		<tr>
			<td>&nbsp;</td>
			<td>
				<input type="submit" name="pcd" value="Print Customs Declaration"/>&nbsp;&nbsp;
				<input type="submit" name="psl" value="Print Shipping Label"/>&nbsp;&nbsp;
				<a href="${payload.lastUrl}"/>Back</a>
			</td>
		</tr>
	</table>
</form>

<table border="1" width="100%" cellspadding="2" cellspacing="0">
	<tr>
		<td>Item ID</td>
		<td width="30%">
			<a href="http://cgi.ebay.com/ws/eBayISAPI.dll?ViewItem&item=${model.transaction.itemId}" target="_blank">${model.transaction.itemId}</a>
			&nbsp;&nbsp;<a:trxStatus transaction="${model.transaction}"/>
		</td>
		<td>Transaction ID</td>
		<td width="30%"><a href="http://payments.ebay.com/ws/eBayISAPI.dll?ViewPaymentStatus&transId=${model.transaction.transactionId}&ssPageName=STRK:MESOX:VPS&itemid=${model.transaction.itemId}" target="_blank">${model.transaction.transactionId}</a></td>
	</tr>
	<tr>
		<td>Item Title</td>
		<td><a href="http://cgi.ebay.com/ws/eBayISAPI.dll?ViewItem&item=${model.transaction.itemId}" target="_blank">${model.transaction.itemTitle}</a></td>
		<td>Transaction Site</td>
		<td>${model.transaction.transactionSiteId}</td>
	</tr>
	<tr>
		<td>Quantity Purchased</td>
		<td>${model.transaction.quantityPurchased}</td>
		<td>Transaction Price</td>
		<td>${w:format(model.transaction.transactionPrice,'#.00')}</td>
	</tr>
	<tr>
		<td>Transaction Time</td>
		<td>${w:format(model.transaction.transactionCreationDate,'yyyy-MM-dd HH:mm:ss')}</td>
		<td>Transaction Amount</td>
		<td>${w:format(model.transaction.amountPaid,'#.00')}</td>
	</tr>
	<tr>
		<td>Payment Id</td>
		<td>${model.transaction.paymentId}&nbsp;</td>
		<td>PayPal Fee / Shipping Fee</td>
		<td>${w:format(model.transaction.paymentFee,'#.00')} / ${w:format(model.transaction.shippingFee,'#.00')}</td>
	</tr>
	<tr>
		<td>Paid Time</td>
		<td>${w:format(model.transaction.paidTime,'yyyy-MM-dd HH:mm:ss')}&nbsp;</td>
		<td>Shipped Time</td>
		<td>${w:format(model.transaction.shippedTime,'yyyy-MM-dd HH:mm:ss')}&nbsp;</td>
	</tr>
	<tr>
		<td>Creation Date</td>
		<td>${model.transaction.creationDate}</td>
		<td>Last Modified Date</td>
		<td>${model.transaction.lastModifiedDate}</td>
	</tr>
	<tr>
		<td>Buyer Name</td>
		<td>${model.transaction.buyerName} (${model.transaction.buyerAccount})</td>
		<td>Shipping Address</td>
		<td><pre>${model.transaction.shippingAddress}</pre></td>
	</tr>
	<tr>
		<td height="5" colspan="4"></td>
	</tr>
	<tr>
		<td>Shipping Tracking Number</td>
		<td>${model.transaction.shippingTrackingId}&nbsp;</td>
		<td>Labels</td>
		<td>${model.transaction.labels}&nbsp;</td>
	</tr>
</table>

</a:body>