<%@ page language="java" contentType="text/html; charset=UTF-8"     pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta http-equiv="content-type" content="text/html; charset=utf-8">
    <title> RedNotes - Reminders</title>
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
          <li id="actualSelected">
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
        <form action="SearchNoteServlet" method="get">
          <input id="searchbar" name="search" type="search" placeholder="Search notes...">
          <button>Search</button>
        </form>
      </div>
    </header>

    <!-- BODY CONTENT-->
    <div id="body">
      <c:forEach var="message" items="${messages}">
        <p>${message}</p>
      </c:forEach>
      <div id="tableRow">
        <!-- LIST OF REMINDERS-->
        <div id="LeftColumn" class="friendsList">
          <h3>List of reminders</h3>
          <a href="AddNewReminderServlet">
            <div class="box" id="addFriend"> <img src="images/add.svg" alt="">
              <p>Add Reminder</p>
            </div>
            <c:forEach var="message" items="${messages}">
          		<p>${message}</p>
          	</c:forEach>
          </a>
          <ul>
            <c:forEach var="reminder" items="${remindersList}">
	            <li class="petition"><img class="userico" src="${reminder.n.imageOwner}" alt="">
	              <h4>${reminder.n.title}</h4>
                <h5>Description: ${reminder.description}</h5>
                <h5>Date: ${reminder.date}</h5>
                <form action="DeleteReminderServlet" method="post">
                  <input type="hidden" name="idu" value="${userlogin.idu}">
                  <input type="hidden" name="idn" value="${reminder.idn}">
                  <input type="hidden" name="date" value="${reminder.dateString}">
                  <input type="submit" name="actbutton" value="Delete">
                </form>
	            </li>
            </c:forEach>
          </ul>
        </div>
      </div>
    </div>
  </body>
</html>
