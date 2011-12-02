<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<html>
	<head>
		<title>Expense - ${model.page.description}</title>
		<link rel='stylesheet' href='${model.webapp}/default.css' type='text/css' />
	</head>
	<body>
		<h1>
			${model.page.description}
		</h1>
		<ul class="tabs">
			<c:forEach var="page" items="${model.page.values}">
				<c:if test="${page.realPage}">
					<li ${model.page == page ? 'class="selected"' : ''}><a href="${model.moduleUri}/${page.name}">${page.description}</a></li>
				</c:if>
			</c:forEach>
		</ul>

		<jsp:doBody />
	</body>
</html>
