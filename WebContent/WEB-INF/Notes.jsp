<%@ page language="java" contentType="text/html; charset=UTF-8"     pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta http-equiv="content-type" content="text/html; charset=utf-8">
    <title> RedNotes</title>
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
          <li id="actualSelected">
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
      <div id="tableRow">
        <!-- NOTES LIST-->
        <div id="LeftColumn">
          <h3>List of Archived Notes</h3>
          <ul>
          	<c:forEach var="note" items="${archivednotes}">
            	<li class="${note.color}-archived "><div class="linksArchived">
	              <a href="FriendsServlet?username=${note.owner}"><img class="userico" title="${note.owner}" src="${note.imageOwner}" alt=""></a>
	              <a class="noteLink" href="NoteServlet?id=${note.id}">
	              <h4>${note.title}</h4></a>
	              </div>
				  <a href="ShareNoteServlet?id=${note.id}">
                    <img title="Share Note" src="images/share-icon.png" alt="Button for share a note">
                  </a>
	              <ul class="optionsMenu">
	                <li><img src="images/optionsNotes.png" alt="">
	                  <ul>
	                  	<li><a href="FriendsServlet?username=${note.owner}">Property of: ${note.owner}</a></li>
	                    <li><form action="PinnedArchivedServlet" method="post">
		                    	<input type="hidden" name="id" value="${note.id}">
		                    	<input type="hidden" name="archived" value="false">
		                    	<input type="submit" name="action" value="Unarchive Note">		                    	
		                </form></li>
	                    <li><a href="EditNoteServlet?id=${note.id}">Edit Note</a></li>
	                    <li><a href="DeleteNoteServlet?id=${note.id}">Delete Note</a></li>
	                  </ul>
	                </li>
	              </ul>
	           	</c:forEach>
            </li>
          </ul>
        </div>
        <!-- NOTES -->
        <div id="CenterColumn">
          <h3> Notes </h3>
          	<c:forEach var="message" items="${messages}">
          		<p>${message}</p>
          	</c:forEach>

          	<c:forEach var="note" items="${anchorednotes}">
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
	              <a href="ShareNoteServlet?id=${note.id}">
                    <img title="Share Note" src="images/share-icon.png" alt="Button for share a note">
                  </a>
	              <ul class="optionsMenu">
	                <li><img src="images/optionsNotes.png" alt="">
	                  <ul>
	                  	<li><a href="FriendsServlet?username=${note.owner}">Property of: ${note.owner}</a></li>
	                    	                    <li><form action="PinnedArchivedServlet" method="post">
		                    	<input type="hidden" name="id" value="${note.id}">
		                    	<input type="hidden" name="archived" value="true">
		                    	<input type="submit" name="action" value="Archive Note">		                    	
		                </form></li>
	                    <li><a href="EditNoteServlet?id=${note.id}">Edit Note</a></li>
	                    <li><a href="DeleteNoteServlet?id=${note.id}">Delete Note</a></li>
	                  </ul>
	                </li>
	              </ul>
	            </div>
           	</c:forEach>

            <c:forEach var="note" items="${notanchorednotes}">
	            <div class="note ${note.color}">
	              <a href="NoteServlet?id=${note.id}">
	                <h4>
	                <c:if test="${note.type==1}"><img src="images/list-ico.png"></c:if> ${note.title}  </h4>
	                <p>
	                	<c:if test="${note.type==0}">${note.content}</c:if>
	                	<c:if test="${note.type==1}">(Enter to see the contents of the list)</c:if>
	                </p>
	              </a>
	              <a href="ShareNoteServlet?id=${note.id}">
                    <img title="Share Note" src="images/share-icon.png" alt="Button for share a note">
                  </a>
	              <ul class="optionsMenu">
	                <li><img src="images/optionsNotes.png" alt="">
	                  <ul>
	                  	<li><a 	href="FriendsServlet?username=${note.owner}">Property of: ${note.owner}</a></li>
	                    <li><form action="PinnedArchivedServlet" method="post">
		                    	<input type="hidden" name="id" value="${note.id}">
		                    	<input type="hidden" name="archived" value="true">
		                    	<input type="submit" name="action" value="Archive Note">		                    	
		                </form></li>
	                    <li><a href="EditNoteServlet?id=${note.id}">Edit Note</a></li>
	                    <li><a href="DeleteNoteServlet?id=${note.id}">Delete Note</a></li>
	                  </ul>
	                </li>
	              </ul>
	            </div>
            </c:forEach>

        </div>
        <!-- OPTIONS -->
        <div id="RightColumn">
          <div class="box">
            <form action="NotesServlet" method="post">
	            <p>Order notes by: </p>
	              <input type="radio" name="orderby" value="creation" <c:if test="${orderby == 'creation'}"> checked </c:if>>Creation date <br>
	              <input type="radio" name="orderby" value="modification" <c:if test="${orderby == 'modification'}">checked</c:if>>Modification date <br>
	              <input type="radio" name="orderby" value="name" <c:if test="${orderby == 'name'}">checked</c:if>>Name <br>
	            <p>Show</p>
	              <input type="radio" name="show1" value="notesandlists" <c:if test="${show1 == 'notesandlists'}">checked</c:if>>Notes and lists<br>
	              <input type="radio" name="show1" value="notes" <c:if test="${show1 == 'notes'}">checked</c:if>>Only notes<br>
	              <input type="radio" name="show1" value="lists" <c:if test="${show1 == 'lists'}">checked</c:if>>Only Lists<br>
	              <br>
	              <input type="radio" name="show2" value="all" <c:if test="${show2 == 'all'}">checked</c:if>>All notes<br>
	              <input type="radio" name="show2" value="my" <c:if test="${show2 == 'my'}">checked</c:if>>My notes<br>
	              <input type="radio" name="show2" value="friends" <c:if test="${show2 == 'friends'}">checked</c:if>>Friends notes<br>
	              <br>
	              <input type="radio" name="colorfilter" value="allcolors" <c:if test="${colorfilter == 'allcolors'}">checked</c:if>>All colors<br>
	              <input type="radio" name="colorfilter" value="yellow" <c:if test="${colorfilter == 'yellow'}">checked</c:if>>Yellow<br>
	              <input type="radio" name="colorfilter" value="blue" <c:if test="${colorfilter == 'blue'}">checked</c:if>>Blue<br>
	              <input type="radio" name="colorfilter" value="green" <c:if test="${colorfilter == 'green'}">checked</c:if>>Green<br>
	              <input type="radio" name="colorfilter" value="red" <c:if test="${colorfilter == 'red'}">checked</c:if>>Red<br>
	              <input type="radio" name="colorfilter" value="orange" <c:if test="${colorfilter == 'orange'}">checked</c:if>>Orange<br>	              
	              <br>
	              <input type="submit" name="OK">
            </form>
          </div>
          <a href="EditNoteServlet?id=9999">
            <div class="box" id="addNote"> <img src="images/add.svg" alt="">
              <p>Add Note</p>
            </div>
          </a>
          <a href="AddItemListServlet?id=9999">
            <div class="box" id="addList"> <img src="images/add.svg" alt="">
              <p>Add List</p>
            </div>
          </a>
        </div>
      </div>
    </div>
    <!--  LOWER BAR -->
<!--    <footer>
      <p><a href="Information.html">Information about us</a></p>
    </footer> -->
  </body>
</html>
