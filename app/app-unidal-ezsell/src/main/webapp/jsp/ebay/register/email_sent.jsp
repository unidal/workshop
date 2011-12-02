<%@ page contentType="text/html; charset=utf-8"%>
<%@ page import="org.unidal.ezsell.*"%>
<%@ taglib prefix="a" uri="/WEB-INF/app.tld"%>
<jsp:useBean id="payload" class="EbayPayload" scope="request" />
<jsp:useBean id="model" type="EbayModel" scope="request" />

<a:register>

<!--
An email has sent to: ${model.user.email}. Please check your email box for confirmation.<br/><br/>
-->
Click <a href="${model.pageUri}?token=${model.token}">here</a> to confirm directly.

</a:register>
