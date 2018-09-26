<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta http-equiv="content-type" content="text/html; charset=utf-8">
    <title> RedNotes - Edit Perfil</title>
    <link rel="stylesheet" type="text/css" href="css/master.css">
    <link rel="stylesheet" type="text/css" href="css/perfil.css">
    <link rel="icon" type="image/png" href="images/myfavicon.png">
  </head>
  <body>

    <header>
      <div class="logoname">
          <img class="logo" src="images/myfavicon.png" alt="">
          <h1 class="name">RedNotes</h1>
      </div>
    </header>


    <!-- BODY CONTENT-->
		<div id="body" >
			<img class="userico2" src="${userlogin.image}" alt="">
			<h3>Personal information</h3>
			<c:forEach var="error" items="${messages}">
        		<p>${error}</p>
        	</c:forEach>
      <form class="" action="EditPerfilServlet" method="post">
        <p><span>Name: </span><input type="text" name="name" placeholder="Your name" value="${userlogin.name}"></p>
  			<p><span>Country: </span><input type="text" name="country" placeholder="Your country" value="${userlogin.country}"></p>
  			<p><span>City: </span><input type="text" name="city" placeholder="Your city" value="${userlogin.city}"></p>
  			<p><span>Age: </span><input type="number" name="age" placeholder="Your age" value="${userlogin.age}"> </p>
  			<p><span>Telephone: </span><input type="tel" name="telephone" placeholder="Your telephone" value="${userlogin.telephone}"></p>
	  		<p><span>Email: </span><input type="email" name="email" placeholder="Your email" value="${userlogin.email}" required></p>
	

  			<p>If you want to change your password... </p>
  			<p><span>Actual Password: </span><input type="password" name="actualpas"></p>
  			<p><span>New Password: </span><input type="password" name="newpas"></p>
  			<p><span>Repeat New Password: </span><input type="password" name="newpas2"></p>

        <button type="submit">
          <img title="Save changes" src="images/save-ico.png" alt="Button for save changes">
          <span> Save changes</span>
          </button>
      </form>
      <form method="get" action="PerfilServlet">
        <button type="submit">
          <img src="images/delversion-ico.png" title="Cancel" alt="Button for cancel">
  				<span> Cancel</span>
          </button>
      </form>
		</div>
		<!--  LOWER BAR -->
<!--    <footer>
      <p> Information about us</p>
    </footer> -->
  </body>
</html>
