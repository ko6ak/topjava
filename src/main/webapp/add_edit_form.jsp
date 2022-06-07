<%@ page contentType="text/html;charset=UTF-8" %>
<html lang="ru">
<head>
    <title>Meals</title>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<hr>
<h2>Add or Edit meal</h2>
<form action="meals" method="POST">
    <table>
        <tr>
            <td style="width: 150px">DateTime: </td>
            <td><input name="dateTime" type="datetime-local" style="width: 200px" value="${meal.dateTime}"/></td>
        </tr>
        <tr>
            <td>Description: </td>
            <td><input type="text" name="description" style="width: 300px" value="${meal.description}"/></td>
        </tr>
        <tr>
            <td>Calories: </td>
            <td><input type="text" name="calories" style="width: 200px" value="${meal.calories}"/></td>
        </tr>
    </table>
    <input type="hidden" name="id" value="${meal.id}">
    <input type="submit" value="Save" />
    <input type="button" onclick="window.location.replace('meals')" value="Cancel"/>
</form>
</body>
</html>
