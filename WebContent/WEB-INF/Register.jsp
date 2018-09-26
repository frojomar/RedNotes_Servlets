<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">

  <head>
    <meta charset="utf-8">
    <title> RedNotes-Register Page </title>
    <link rel="stylesheet" href="css/master.css">
    <link rel="stylesheet" href="css/reglogin.css">
    <link rel="icon" type="image/png" href="images/myfavicon.png">
  </head>

  <body>
    <header>
      <div class="logoname">
        <a href="LoginServlet">
          <img class="logo" src="images/myfavicon.png" alt="">
          <h1 class="name">RedNotes</h1>
        </a>
      </div>
    </header>

    <div class="content">
      <div class="box">
        <h2> Register now in our web </h2>
        <c:forEach var="error" items="${messages}">
        	<p>${error}</p>
        </c:forEach>
        <form action="RegisterServlet" method="post">
          <p>
            Enter your username: <br>
            <input type="text" name="username" placeholder="e.g. JavierR97" required>
          </p>
          <p>
            Enter your e-mail: <br>
            <input type="email" name="email" placeholder="12345@example.com" required>
          </p>
          <p>
            Enter your password (Between 6 and 10 characters): <br>
            <input type="password" name="password" placeholder="******" required>
          </p>
          <p>
            Repeat password: <br>
            <input type="password" name="password2" placeholder="******" required>
          </p>
          <div id="OKbutton">
            <input type="submit" value="Registrarse">
          </div>
        </form>

        <a href="LoginServlet"> &lt; &lt; Return to Login page </a>

      </div>
    </div>

  <!--
    <footer>
      <p> Information about us</p>
    </footer> -->

  </body>


</html>
