<%@ page contentType="text/html; charset=utf-8"%>
<%@ page import="org.unidal.expense.biz.home.*"%>
<%@ taglib prefix="a" uri="/WEB-INF/app.tld"%>
<jsp:useBean id="ctx" type="Context" scope="request" />
<jsp:useBean id="payload" type="Payload" scope="request" />
<jsp:useBean id="model" type="Model" scope="request" />

<a:body>

Welcome, <b>${ctx.signinMember.name}</b>.

</a:body>