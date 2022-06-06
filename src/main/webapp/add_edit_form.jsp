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
            <td style="width: 100px">DateTime: </td>
            <td><input name="dateTime" type="datetime-local" style="width: 200px" /></td>
        </tr>
        <tr>
            <td>Description: </td>
            <td><input name="description" style="width: 200px" /></td>
        </tr>
        <tr>
            <td>Calories: </td>
            <td><input name="calories" style="width: 200px" /></td>
        </tr>
    </table>

<%--    Name: <input name="name" /><br><br>--%>
<%--    Age: <input name="age" /><br><br>--%>
<%--    Gender: <input type="radio" name="gender" value="female"--%>
<%--                   checked />Female--%>
<%--    <input type="radio" name="gender" value="male" />Male<br><br>--%>
<%--    Country: <select name="country"><option>France</option>--%>
<%--    <option>Germany</option></select><br><br>--%>
<%--    Courses:--%>
<%--    <input type="checkbox" name="courses" value="JavaSE"--%>
<%--           checked />Java SE--%>
<%--    <input type="checkbox" name="courses" value="JavaEE" />Java EE--%>
<%--    <br><br>--%>
    <input type="submit" value="Submit" />
    <input type="reset" value="Reset" />
</form>
</body>
</html>
