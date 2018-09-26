<%@ page language="java" contentType="text/html; charset=UTF-8"     pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta http-equiv="content-type" content="text/html; charset=utf-8">
    <title> RedNotes - Friends</title>
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
          <h3>Friends List </h3>

          <ul>
            <li>
              <div id="options">
                <p>Order friends by: </p>
                <form action="FriendsServlet" method="post">
                  <input type="radio" name="friendsorderby" value="name" <c:if test="${friendsorderby == 'name'}"> checked </c:if>>Username<br>
                  <input type="radio" name="friendsorderby" value="date" <c:if test="${friendsorderby == 'date'}"> checked </c:if>>Date of friendship<br>
                  <input type="submit" name="OK">
                </form>
              </div>
            </li>
            <c:forEach var="friendtolist" items="${friendslist}">
	            <li><a href="FriendsServlet?username=${friendtolist.username}"><img class="userico" src="${friendtolist.image}" alt="">
	              <h4>${friendtolist.name} (@${friendtolist.username})</h4></a>
	              <ul class="optionsMenu">
	                <li><img src="images/optionsNotes.png" alt="">
	                  <ul>
	                    <li><a href="FriendsServlet?username=${friendtolist.username}">View Friend</a></li>
	                    <li><a href="DeleteFriendsServlet?id=${friendtolist.idu}">Delete Friend</a></li>
	                  </ul>
	                </li>
	              </ul>
	            </li>
            </c:forEach>
          </ul>
        </div>
        <div id="RightColumn">
          <!-- INFO PERSON -->
          <a href="AddNewFriendServlet">
            <div class="box" id="addFriend"> <img src="images/add.svg" alt="">
              <p>Add Friend</p>
            </div>
          </a>
          <c:if test="${friend!='null'}">
	          <div class="box" id="infoBox">
	            <img class="userico2" src="${friend.image}" alt="">
	            <h3>Personal information</h3>
	            <p><span>Name: </span>${friend.name}</p>
	            <p><span>Country: </span>${friend.country}</p>
	            <p><span>City: </span>${friend.city}</p>
	            <p><span>Age: </span>${friend.age} </p>
	            <p><span>Telephone: </span>${friend.telephone}</p>
	            <br>
	            <p><span>Email: </span>${friend.email}</p>
	            <p><span>Username: </span>@${friend.username}</p>
	            <p><span>Date of friendship: </span>${friend.date} </p>

	            <h3>Shared notes</h3>

	          	<c:forEach var="note" items="${notes}">
		            <div class="note ${note.color}">
		              <a href="NoteServlet?id=${note.id}">
		                <h4>
		                 <img src="images/fij-ico.png" alt="">
		                 <c:if test="${note.type==1}"><img src="images/list-ico.png"></c:if>
		                  ${note.title} </h4>
		                <p>
		                	<c:if test="${note.type==0}">${note.content}</c:if>
		                	<c:if test="${note.type==1}">(Enter to see the contents of the list)</c:if>
		                </p>
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

	            <a class="deleteButton"  href="DeleteFriendsServlet?id=${friend.idu}">
	              <img src="images/del-ico.png" alt="Delete Friend Button">
	            </a>
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
