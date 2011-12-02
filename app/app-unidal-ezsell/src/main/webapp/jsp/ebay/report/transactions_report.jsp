<%@ page contentType="text/xml; charset=utf-8"%>
<%@ page import="org.unidal.ezsell.*"%>
<%@ taglib prefix="w" uri="/web/core"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<jsp:useBean id="model" class="EbayModel" scope="request" />
<?xml version="1.0" encoding="utf-8"?>

<document author="Frankie Wu">
	<base-font alias="default" name="Helvetica" encoding="CP1252" embedded="true" />

	<page size="A4" rotate="true" margin-left="10" margin-top="50">
		<font name="default" size="14" style="0">

<table border="15" widths="10,20,60,20,20,20,20,20">
	<tr align="center">
		<td></td>
		<td>Lables</td>
		<td>Title</td>
		<td>Quantity Purchased</td>
		<td>Amount</td>
		<td>Final Value Fee</td>
		<td>PayPal Fee</td>
		<td>Account</td>
	</tr>
	<c:forEach var="trx" items="${model.transactions}" varStatus="status">
		<tr align="center">
			<td>${status.count}</td>
			<td align="left"><![CDATA[${trx.labels}]]></td>
			<td align="left"><![CDATA[${trx.itemTitle}]]></td>
			<td>${trx.quantityPurchased}</td>
			<td>${w:format(trx.amountPaid,'#.00')}</td>
			<td>${w:format(trx.finalValueFee,'#.00')}</td>
			<td>${w:format(trx.paymentFee,'#.00')}</td>
			<td>${w:format(trx.buyerAccount,'#.00')}</td>
		</tr>
	</c:forEach>
	<tr align="center">
		<td colspan="3" align="right">Sub Total:</td>
		<td>${w:format(w:sum(model.transactions, 'quantityPurchased'), '0')}</td>
		<td>\$${w:format(w:sum(model.transactions, 'amountPaid'), '#,###.00')}</td>
		<td>\$${w:format(w:sum(model.transactions, 'finalValueFee'), '#,###.00')}</td>
		<td>\$${w:format(w:sum(model.transactions, 'paymentFee'), '#,###.00')}</td>
		<td></td>
	</tr>
</table>

		</font>
	</page>
</document>