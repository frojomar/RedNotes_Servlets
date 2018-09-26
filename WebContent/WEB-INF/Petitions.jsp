<%@ page language="java" contentType="text/html; charset=UTF-8"     pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta http-equiv="content-type" content="text/html; charset=utf-8">
    <title> RedNotes - Petitions of friendship</title>
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
          <li id="actualSelected">
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
      <c:forEach var="message" items="${messages}">
        <p>${message}</p>
      </c:forEach>
      <div id="tableRow">
        <!-- LIST OF PETITIONS SENT-->
        <div id="LeftColumn" class="petitionList">
          <h3>Petitions sent</h3>

          <ul>
            <c:forEach var="userpetition" items="${userspetitionssent}">
	            <li class="petition"><img class="userico" src="${userpetition.image}" alt="">
	              <h4>${userpetition.name} (@${userpetition.username})</h4>
                <form action="FriendshipPeticionsServlet" method="post">
                  <input type="hidden" name="action" value="revision">
                  <input type="hidden" name="idu" value="${userpetition.idu}">
                  <input type="submit" name="actbutton" value="Delete">
                </form>
	            </li>
            </c:forEach>
          </ul>
        </div>
        <!-- LIST OF PETITIONS RECEIVED-->
        <div id="LeftColumn" class="petitionList">
          <h3>Petitions received</h3>

          <ul>
            <c:forEach var="userpetition" items="${userspetitionsreceived}">
              <li class="petition"><img class="userico" src="${userpetition.image}" alt="">
	              <h4>${userpetition.name} (@${userpetition.username})</h4>
                <form action="FriendshipPeticionsServlet" method="post">
                  <input type="hidden" name="action" value="confirmation">
                  <input type="hidden" name="idu" value="${userpetition.idu}">
                  <input type="submit" name="actbutton" value="Confirm">
                  <input type="submit" name="actbutton" value="Delete">
                </form>
	            </li>
            </c:forEach>
          </ul>
        </div>
      </div>
    </div>
    <!--  LOWER BAR -->
<!--    <footer>
      <p> Information about us</p>
    </footer> -->
  </body>
</html>
