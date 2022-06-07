<%@ page contentType="text/html;charset=UTF-8" %>
<html lang="ru">
<head>
    <title>Meals</title>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<hr><br>
<form action="meals" method="POST">
    <table>
        <tr>
            <td style="width: 150px">DateTime: </td>
            <td><input name="dateTime" type="datetime-local" style="width: 200px" value="${meal.dateTime}"/></td>
        </tr>
        <tr>
            <td>Description: </td>
            <td><input name="description" style="width: 300px" value="${meal.description}"/></td>
        </tr>
        <tr>
            <td>Calories: </td>
            <td><input name="calories" style="width: 200px" value="${meal.calories}"/></td>
        </tr>
    </table>
    <input type="hidden" name="id" value="${meal.id}">
    <input type="submit" value="Submit" />
    <input type="button" onclick="window.location.replace('meals')" value="Cancel"/>
</form>
</body>
</html>
