<%@ page language="java" contentType="text/html; charset=UTF-8"     pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta http-equiv="content-type" content="text/html; charset=utf-8">
    <title> RedNotes - Add Reminder</title>
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
          <li>
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
            <c:forEach var="noteofList" items="${noteslist}">
	            <li class="${noteofList.color}-archived">
	              <a href="AddNewReminderServlet?idn=${noteofList.id}"><img class="userico" src="${noteofList.imageOwner}" alt="">
	              <h4>${noteofList.title}</h4></a>
	            </li>
            </c:forEach>
          </ul>
        </div>
        <div id="RightColumn">
          <!-- ADVANCED SEARCH-->
	      <form method="get" action="RemindersServlet">
	        <button type="submit">
	            <img class="goBack" src="images/goback-icon.jpg" alt="Button for go back to Reminder">
	            <span> Go Back (Cancel)</span>
	          </button>
	      </form>
	      <c:if test="${note!='null'}">
	          <div class="box ${note.color}" id="infoBox">
	            <img class="userico2" src="${note.imageOwner}" alt="">
	           	<p><span>Title Note: </span>${note.title}</p>

	            <form action="AddNewReminderServlet" method="post">
	            	<input type="hidden" name="idn" value="${note.id}">
	            	<input type="hidden" name="idu" value="${userlogin.idu}">
	            	<input type="datetime-local" name="date" required>
	            	<input type="text" name="description">
	            	<input type="submit" name="confirm" value="Add Reminder">
	          </form>
	          </div>
	       </c:if>
	    </div>
      </div>
    </div>
    <!--  LOWER BAR -->
<!--    <footer>
      <p> Information about us</p>
    </footer> -->
  </body>
</html>
