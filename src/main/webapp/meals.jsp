<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<% DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    request.setAttribute("formatter", formatter);%>
<html lang="ru">
<head>
    <title>Meals</title>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<hr>
<h2>Meals</h2>
<table border="1">
    <tr>
        <th>Date</th>
        <th>Description</th>
        <th>Calories</th>
    </tr>
    <c:forEach var="mealTo" items="${mealsTo}">
<%--        <c:url var="updateButton" value="/updateInfo"><c:param name="empId" value="${emp.id}"/></c:url>--%>
<%--        <c:url var="deleteButton" value="/deleteEmployee"><c:param name="empId" value="${emp.id}"/></c:url>--%>
        <tr style="${mealTo.excess ? "color: red" : "color : green"}">
            <td><c:out value="${mealTo.dateTime.format(formatter)}"/></td>
            <td><c:out value="${mealTo.description}"/></td>
            <td><c:out value="${mealTo.calories}"/></td>
<%--            <td><a href="index.html" onclick="window.location.href='${updateButton}'">Update</a></td>--%>
            <td><a href="meals?id=${mealTo.id}">Delete</a></td>
        </tr>
    </c:forEach>
</table>
</body>
</html>