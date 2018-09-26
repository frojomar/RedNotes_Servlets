<%@ page language="java" contentType="text/html; charset=UTF-8"     pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta http-equiv="content-type" content="text/html; charset=utf-8">
    <title> RedNotes - Search Note</title>
    <link rel="stylesheet" type="text/css" href="css/master.css">
    <link rel="stylesheet" type="text/css" href="css/notes.css">
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
        <form action="SearchNoteServlet" method="get">
          <input id="searchbar" name="search" type="search" placeholder="Search notes...">
          <button>Search</button>
        </form>
      </div>
    </header>


    <!-- BODY CONTENT-->
    <div id="body" >
      <h3> Notes </h3>

      	<c:forEach var="note" items="${notes}">
          <div class="note ${note.color}">
            <a href="NoteServlet?id=${note.id}">
              <h4> <img src="images/fij-ico.png" alt=""> ${note.title} </h4>
              <p> ${note.content}  </p>
            </a>
            <img class="sharedIcon" src="images/share-icon.png" alt="Icon to share note">
            <ul class="optionsMenu">
              <li><img src="images/optionsNotes.png" alt="">
                <ul>
                	<li><a href="FriendsServlet?username=${note.owner}">Property of: ${note.owner}</a></li>
                  <li><a href="NotesServlet?id=${note.id}&archived=true">Archive Note</a></li>
                  <li><a href="EditNoteServlet?id=${note.id}">Edit Note</a></li>
                  <li><a href="DeleteNoteServlet?id=${note.id}">Delete Note</a></li>
                </ul>
              </li>
            </ul>
          </div>
       	</c:forEach>
    </div>
    <!--  LOWER BAR -->
<!--    <footer>
      <p><a href="Information.html">Information about us</a></p>
    </footer> -->
  </body>
</html>
