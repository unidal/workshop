<%@ page contentType="text/html; charset=utf-8"%>
<%@ page import="org.unidal.ezsell.*"%>
<%@ taglib prefix="a" uri="/WEB-INF/app.tld"%>
<%@ taglib prefix="w" uri="/web/core"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<jsp:useBean id="payload" class="EbayPayload" scope="request" />
<jsp:useBean id="model" class="EbayModel" scope="request" />

<a:body>

<style>
.cr{background:#fff9c7}
.ucr{background:white}
</style>

<form method="post" action="${model.pageUri}" id="trx">
	<table border="0">
		<tr>
			<td>Title:</td>
			<td><input type="text" name="keyword" value="${w:htmlEncode(payload.keyword)}" size="30"/></td>
			<td>( <input type="text" name="nonKeyword" value="${w:htmlEncode(payload.nonKeyword)}" size="30"/> )</td>
		</tr>
		<tr>
			<td>Labels:</td>
			<td><input type="text" name="label" value="${w:htmlEncode(payload.label)}" size="30"/></td>
			<td>( <input type="text" name="nonLabel" value="${w:htmlEncode(payload.nonLabel)}" size="30"/> )</td>
		</tr>
		<tr>
			<td>Buyer Account:</td>
			<td colspan="2"><input type="text" name="buyerAccount" value="${w:htmlEncode(payload.buyerAccount)}" size="30"/></td>
		</tr>
		<tr>
			<td>Shipping Tracking No:</td>
			<td colspan="2"><input type="text" name="shippingTrackingId" value="${w:htmlEncode(payload.shippingTrackingId)}" size="30"/></td>
		</tr>
		<tr>
			<td>Transaction Date:</td>
			<td>
				<input type="text" name="dateFrom" value="${w:format(payload.dateFrom,'yyyy-MM-dd')}" size="10" maxlength="10"/>
				 - 
				<input type="text" name="dateTo" value="${w:format(payload.dateTo,'yyyy-MM-dd')}" size="10" maxlength="10"/>
			</td>
			<td>&nbsp;</td>
		</tr>
		<tr>
			<td>Shipping Date:</td>
			<td>
				<input type="text" name="shipDateFrom" value="${w:format(payload.shipDateFrom,'yyyy-MM-dd')}" size="10" maxlength="10"/>
				 - 
				<input type="text" name="shipDateTo" value="${w:format(payload.shipDateTo,'yyyy-MM-dd')}" size="10" maxlength="10"/>
			</td>
			<td>&nbsp;</td>
		</tr>
		<tr>
			<td>Transaction Status:</td>
			<td colspan="2">
				${w:showCheckboxes('status', payload.status.values, payload.statuses, 'name', 'description')}
			</td>
		</tr>
		<tr>
			<td>Feedback Status:</td>
			<td colspan="2">
				${w:showRadios('feedbackStatus', payload.feedbackStatuses, payload.feedbackStatus, 'value', 'description')}
			</td>
		</tr>
		<tr>
			<td>Order By:</td>
			<td colspan="2">
				${w:showRadios('orderBy', payload.orderBy.values, payload.orderBy, 'name', 'description')}
			</td>
		</tr>
		<tr>
			<td>&nbsp;</td>
			<td><input type="submit" name="search" value="Search" />&nbsp;&nbsp;<input type="reset" value="Reset" /></td>
			<td><input type="submit" name="ptr" value="Print Transactions Report"/>&nbsp;&nbsp;<input type="submit" name="dcf" value="Download CSV File"/></td>
			<td><input type="submit" name="lt" value="Load Transactions from eBay"/>&nbsp;&nbsp;<input type="submit" name="lf" value="Load Feedbacks from eBay"/></td>
		</tr>
	</table>

	<br>
	<table border="0" width="100%" cellspadding="2" cellspacing="0">
		<tr>
			<td>
				<input type="text" name="labels" size="20"/>
				<input type="submit" name="al" value="Apply Labels"/>
				<input type="submit" name="rl" value="Remove Labels"/>
			</td>
			<td align="right"><b>${w:count(model.transactions)}</b> transactions found!</td>
		</tr>
	</table>
	
	<table border="1" width="100%" cellspadding="2" cellspacing="0">
		<tr>
			<th><input type="checkbox" onclick="checkItems('sti', this.checked)"/></th>
			<th>Labels</th>
			<th>Title</th>
			<th>Transaction Date / Tracking No.</th>
			<th>Transaction Price / Quantity</th>
			<th>Amount / FVF / PayPal / Shipping</th>
			<th>Status</th>
			<th>Buyer ID</th>
		</tr>
		<c:forEach var="trx" items="${model.transactions}" varStatus="status">
			<tr align="center">
				<td><input type="checkbox" name="sti" value="${trx.id}" onclick="clickItem(this)"/></td>
				<td>${trx.labels}&nbsp;</td>
				<td align="left">
					<c:choose>
						<c:when test="${trx.id > 0 and trx.orderId > 0}"><a href="${model.moduleUri}/transaction?id=${trx.id}"><b title="order id: ${trx.orderId}">${trx.itemTitle}</b></a></c:when>
						<c:when test="${trx.id > 0}"><a href="${model.moduleUri}/transaction?id=${trx.id}">${trx.itemTitle}</a></c:when>
						<c:when test="${trx.orderId > 0}"><b title="order id: ${trx.orderId}">${trx.itemTitle}</b></c:when>
						<c:otherwise>${trx.itemTitle}</c:otherwise>
					</c:choose>
					<c:if test="${not empty trx.checkoutMessage}">
						<br><b>Buyer Message</b>: <font size="-1">${trx.checkoutMessage}</font>
					</c:if>
				</td>
				<td nowrap="true">
					${w:format(trx.transactionCreationDate,'yyyy-MM-dd HH:mm:ss')}
					<br><font size="-1">${trx.shippingTrackingId}</font>
				</td>
				<td>${w:format(trx.transactionPrice,'#.00')} / ${trx.quantityPurchased}</td>
				<td>${w:format(trx.amountPaid,'#.00')} / ${w:format(trx.finalValueFee,'#.00')} / ${w:format(trx.paymentFee,'#.00')} / ${w:format(trx.shippingFee,'#.00')}</td>
				<td><a:trxStatus transaction="${trx}"/></td>
				<td nowrap="true"><label title="${trx.buyerName}

${trx.shippingAddress}">${w:shorten(trx.buyerName,19)}<br><font size="-1">(${trx.buyerAccount})</font></label></td>
			</tr>
		</c:forEach>
		<tr>
			<th colspan="4" align="right">Sub Total:</th>
			<th>\$${w:format(w:sum(model.transactions, 'transactionPrice'), '#,##0.00')} / ${w:format(w:sum(model.transactions, 'quantityPurchased'), '0')}</th>
			<th>\$${w:format(w:sum(model.transactions, 'amountPaid'), '#,##0.00')} / \$${w:format(w:sum(model.transactions, 'finalValueFee'), '#,##0.00')} / \$${w:format(w:sum(model.transactions, 'paymentFee'), '#,##0.00')} / RMB ${w:format(w:sum(model.transactions, 'shippingFee'), '#,##0.00')}</th>
			<th colspan="2">&nbsp;</th>
		</tr>
	</table>
	</font>
</form>

<script>
function checkItems(name, checked) {
	var form = document.getElementById("trx");
	var len = form.elements.length;
	
	for (var i=0;i<len;i++) {
		var e = form.elements[i];
		
		if (e.type=='checkbox' && e.name==name) { 
			checkItem(e, checked);
		}
	}
}

function clickItem(element) {
	if (element.checked) {
	   element.parentNode.parentNode.className="cr";
	} else {
	   element.parentNode.parentNode.className="ucr";
	}
}

function checkItem(element, checked) {
	element.checked = checked;
	clickItem(element)
}
</script>

</a:body>