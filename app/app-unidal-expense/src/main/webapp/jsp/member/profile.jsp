<%@ page contentType="text/html; charset=utf-8"%>
<%@ page import="org.unidal.expense.biz.member.*"%>
<%@ taglib prefix="a" uri="/WEB-INF/app.tld"%>
<jsp:useBean id="ctx" type="Context" scope="request" />
<jsp:useBean id="payload" type="Payload" scope="request" />
<jsp:useBean id="model" type="Model" scope="request" />

<a:body>

Account: ${ctx.signinMember.account}<br/>
Name: ${ctx.signinMember.name}

</a:body>