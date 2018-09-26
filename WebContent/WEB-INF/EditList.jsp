<%@ page language="java" contentType="text/html; charset=UTF-8"     pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta http-equiv="content-type" content="text/html; charset=utf-8">
    <title> RedNotes - Edit List</title>
    <link rel="stylesheet" type="text/css" href="css/master.css">
    <link rel="stylesheet" type="text/css" href="css/note.css">
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
          <input id="searchbar"  name="search" type="search" placeholder="Search notes...">
          <button>Search</button>
        </form>
      </div>
    </header>

		<!-- BODY CONTENT-->
    <div id="body" >
        <!-- NOTES LIST-->
        <div id="LeftColumn">
          <h3>Versions Control</h3>
          <ul>
            <c:forEach var="n" items="${listversions}">
	            <li <c:if test="${n.modificationDate==note.modificationDate}">class="${note.color}"</c:if>
	            <c:if test="${n.modificationDate!=note.modificationDate}">class="${note.color}-archived"</c:if>><div class="linksArchived">
	              <img class="userico" src="${n.imageOwner}" title="${n.owner}" alt="">
	              <a class="noteLink" href="VersionServlet?id=${n.id}&date=${n.dateVersion}"><h4>[${n.modificationDate}] ${n.title}</h4></a>
	              </div>
	              <ul class="optionsMenu">
	                <li><img src="images/optionsNotes.png" alt="">
	                  <ul>
	                    <li><a href="VersionServlet?id=${n.id}&date=${n.dateVersion}">View version</a></li>
	                    <li><a href="ChangeVersionServlet?id=${n.id}&date=${n.dateVersion}">Set version as definitive</a></li>
	                    <li><a href="DeleteVersionServlet?id=${n.id}&date=${n.dateVersion}">Delete Version</a></li>
	                  </ul>
	                </li>
	              </ul>
	            </li>
          	</c:forEach>
          </ul>
        </div>
        <!-- NOTES -->
        <div id="RightColumn">
          <h3> ${note.title} [${note.modificationDate}]</h3>
            <div class="note ${note.color}">
            	<form action="EditListServlet?id=${note.id}" method="post">
            	<div id="form">
                  <h4>Name of note:<input name="name" required value="${note.title}"></h4>
                  <span> Mark the check boxes of the items you want to delete</span><br><br>
	              <ul>
	              <c:forEach var="element" items="${listelements}">
					<li>
					<c:if test="${element.value==1}">(COMPLETE)</c:if>
					<c:if test="${element.value==0}">(NOT COMPLETE)</c:if>
					<span>${element.text} <input type="checkbox" name="deleteBox" value="${element.text}"></span><br>
	              	</li>
	              </c:forEach>
	              </ul>
                 </div>
              <ul id="options">
								<!--principalOptions-->
								<div class="optionsGroup">
                  <li>
	                  	<button type="submit">
	          			<img title="Save changes" src="images/save-ico.png" alt="Button for save changes">
	          			<span> Save changes</span>
	          			</button>
	          		</form>
          		  </li>
                  <li><a href="ListServlet?id=${note.id}">
                    <img title="Cancel" src="images/delversion-ico.png" alt="Button for cancel the action">
                  </a></li>
								</div>
              </ul>
            </div>
        </div>
      </div>

	</body>
</html>
