<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">

  <head>
    <meta charset="utf-8">
    <title> RedNotes-Delete Account </title>
    <link rel="stylesheet" href="css/master.css">
    <link rel="stylesheet" href="css/reglogin.css">
    <link rel="stylesheet" href="css/delete.css">
    <link rel="icon" type="image/png" href="images/myfavicon.png">

  </head>

  <body>
    <header>
        <div class="logoname">
          <img class="logo" src="images/myfavicon.png" alt="">
          <h1 class="name">RedNotes</h1>
        </div>
    </header>

    <div class="content">
      <div class="box" id="regcompleted">
          <h1> Are you sure you want to delete your account? <br>
          "@${userlogin.username}"</h1>
          <img class="userico2" src="images/persona.jpg" alt="">
          <p><c:choose><c:when test="${not empty userlogin.name}"> "${userlogin.name}(@${userlogin.username})"</c:when>
    					<c:otherwise>
    					"@${userlogin.username}"
    					</c:otherwise>
						</c:choose></p>

          <form action="DeleteAccountServlet" method="post">
            <input type="submit" name="confirm" value="Yes, I am sure">
            <input type="submit" name="confirm" value="No, I don't want to delete it.">
          </form>

          <h3><a href="PerfilServlet"> Go back </a></h3>
      </div>
    </div>
  </body>


</html>
