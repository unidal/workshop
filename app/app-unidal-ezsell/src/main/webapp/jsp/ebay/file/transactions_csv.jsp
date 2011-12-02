<%@ page contentType="text/xml; charset=utf-8"%>
<%@ page import="org.unidal.ezsell.*"%>
<%@ taglib prefix="w" uri="/web/core"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<jsp:useBean id="model" class="EbayModel" scope="request" />

Lables, Title, Quantity Purchased, Amount, Final Value Fee, PayPal Fee, Account
<c:forEach var="trx" items="${model.transactions}"
>${trx.labels},"${w:csvEscape(trx.itemTitle)}",${trx.quantityPurchased},${w:format(trx.amountPaid,'#.00')},${w:format(trx.finalValueFee,'#.00')},${w:format(trx.paymentFee,'#.00')},${w:format(trx.buyerAccount,'#.00')}
</c:forEach>
