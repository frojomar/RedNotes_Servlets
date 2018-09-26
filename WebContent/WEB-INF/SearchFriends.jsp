<%@ page language="java" contentType="text/html; charset=UTF-8"     pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta http-equiv="content-type" content="text/html; charset=utf-8">
    <title> RedNotes - Search Friends</title>
    <link rel="stylesheet" type="text/css" href="css/master.css">
    <link rel="stylesheet" type="text/css" href="css/friends.css">
    <link rel="icon" type="image/png" href="images/myfavicon.png">
  </head>
  <body>

    <!-- TOP BAR -->
    <header>
      <!--MENU OF NAVEGATION-->
      <nav>
        <ul>
          <li class="logoname">
            <img class="logo" src="images/myfavicon.png" alt="">
            <h1 class="name">RedNotes</h1>
          </li>
          <li>
            <h2><a href="NotesServlet">Notes</a></h2>
          </li>
          <li>
            <h2><a href="RemindersServlet">Reminders</a></h2>
          </li>
          <li id="actualSelected">
              <h2><a href="FriendsServlet">Friends</a></h2>
          </li>
          <li>
              <h2><a href="FriendshipPeticionsServlet">Petitions</a></h2>
          </li>
        </ul>
      </nav>
      <!-- MENU OF PERFIL SETTINGS -->
      <ul id="navPerfil">
        <li><a href="PerfilServlet"><img class="userico" src="${userlogin.image}" alt=""><span>    ${userlogin.username} </span></a>
          <ul>
          	<li><a href="PerfilServlet">View Perfil</a></li>
            <li><a href="EditPerfilServlet">EditPerfil</a></li>
            <li><a href="LoginServlet?disconnect=true">Log out</a></li>
          </ul>
        </li>
      </ul>
      <!-- SEARCH BAR -->
      <div class="searchbarDiv">
        <form action="SearchFriendServlet" method="get">
          <input id="searchbar" name="search" type="search" placeholder="Search friends...">
          <button>Search</button>
        </form>
      </div>
    </header>

    <!-- BODY CONTENT-->
    <div id="body">
      <div id="tableRow">
        <!-- FRIENDS LIST-->
        <div id="LeftColumn">
          <h3>Users List </h3>

          <ul>
            <c:forEach var="friendtolist" items="${friendslist}">
	            <li><a href="AddNewFriendServlet?username=${friendtolist.username}"><img class="userico" src="${friendtolist.image}" alt="">
	              <h4>${friendtolist.name} (@${friendtolist.username})</h4></a>
	            </li>
            </c:forEach>
          </ul>
        </div>
        <div id="RightColumn">
          <!-- ADVANCED SEARCH-->
          <div class="box" id="AdvancedSearch">
          	<h3>Advanced Search</h3>
            <form class="" action="AddNewFriendServlet" method="post">
	        	<p><span>Name: </span><input type="text" name="name" placeholder="Name to search"></p>
	        	<p><span>Username: </span><input type="text" name="username" placeholder="Username to search"></p>
	  			<p><span>Country: </span><input type="text" name="country" placeholder="Country to search"></p>
	  			<p><span>City: </span><input type="text" name="city" placeholder="City to search"></p>
              <br>
              <input type="submit" name="Search">
  			</form>
          </div>
          <div class="box" id="infoBox">
            <img class="userico2" src="${user.image}" alt="">
           	<p><span>Name: </span>${user.name}</p>
           	<p><span>Username: </span>${user.username}</p>
	        <p><span>Country: </span>${user.country}</p>
	        <p><span>City: </span>${user.city}</p>

            <form action="FriendshipPeticionsServlet" method="post">
            	<input type="hidden" name="action" value="newPetition">
            	<input type="hidden" name="idu" value="${user.idu}">
            	<input type="submit" name="actbutton" value="Add Friend">
          </form>
          </div>
        </div>
      </div>
    </div>
    <!--  LOWER BAR -->
<!--    <footer>
      <p> Information about us</p>
    </footer> -->
  </body>
</html>
