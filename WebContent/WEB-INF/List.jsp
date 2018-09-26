<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta http-equiv="content-type" content="text/html; charset=utf-8">
    <title> RedNotes - Note(List)</title>
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
          <input id="searchbar" name="search" type="search" placeholder="Search notes...">
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
          <h3>${note.title} [${note.modificationDate}]</h3>
            <div class="note ${note.color}">
            	<a href="FriendsServlet?username=${note.owner}"><img class="userico" title="${note.owner}" src="${note.imageOwner}" alt=""></a>
                <h4>${note.title}</h4>
				<form id="list" action="ListServlet?id=${note.id}" method="post">
	              <c:forEach var="element" items="${listelements}">
	              	<input type="checkbox" name="elementBox" value="${element.text}" <c:if test="${element.value == 1}"> checked </c:if>>${element.text} <br>
	              </c:forEach>
	              <br>
	              <input type="submit" name="OK" value="Save changes checkbox">
	             <a href="EditListServlet?id=${note.id}">
            		<div class="box" id="deleteItem"> <img src="images/minus.png" alt="">
              			<p>Delete Item </p>
        		    </div>
		          </a>
	              <a href="AddItemListServlet?id=${note.id}">
            		<div class="box" id="addItem"> <img src="images/add.svg" alt="">
              			<p>Add Item </p>
        		    </div>
		          </a>
	            </form>
              <ul id="options">
                <li><a href="DeleteNoteServlet?id=${note.id}">
                  <img title="Delete Note" src="images/del-ico.png" alt="Button for delete the note">
                </a></li>

				<!--principalOptions-->
				<div class="optionsGroup">
				  <li><a href="ShareNoteServlet?id=${note.id}">
                    <img title="Share Note" src="images/share-icon.png" alt="Button for share a note">
                  </a></li>
                  <li><form action="PinnedArchivedServlet" method="post">
                    	<input type="hidden" name="id" value="${note.id}">
                    	<input type="hidden" name="archived" value="true">
                    	<button type="submit">
				         	<img title="Archive Note" src="images/archive-ico.png" alt="Button for archive note">
				        </button>	                    	
		          </form></li>
		          <li><form action="PinnedArchivedServlet" method="post">
                    	<input type="hidden" name="id" value="${note.id}">
                    	<input type="hidden" name="archived" value="false">
                    	<button type="submit">
				         	<img title="Unarchive Note" src="images/archive2-ico.png" alt="Button for unarchive note">
				        </button>	                    	
		          </form></li>
		          <li><form action="PinnedArchivedServlet" method="post">
                    	<input type="hidden" name="id" value="${note.id}">
                    	<input type="hidden" name="anchored" value="true">
                    	<button type="submit">
				         	<img title="Pinned Note" src="images/fij-ico.png" alt="Button for pinned a note">
				        </button>	                    	
		          </form></li>
		          <li><form action="PinnedArchivedServlet" method="post">
                    	<input type="hidden" name="id" value="${note.id}">
                    	<input type="hidden" name="anchored" value="false">
                    	<button type="submit">
				         	<img title="Unpinned Note" src="images/fij2-ico.png" alt="Button for unpinned a note">
				        </button>	                    	
		          </form></li>

								</div>
								<!--optionsEdit-->
								<div class="optionsGroup">
									<li>
                    <ul class="optionsMenu">
                      <li><img title="Choose color" src="images/colorpicker-ico.png" alt="Button for select color of note">
                        <ul>
                          <form id="colorSelector" action="EditColorServlet?id=${note.id}" method="post">
                            <li><input type="submit" name="color" value="red"></li>
                            <li><input type="submit" name="color" value="blue"></li>
                            <li><input type="submit" name="color" value="green"></li>
                            <li><input type="submit" name="color" value="yellow"></li>
                            <li><input type="submit" name="color" value="orange"></li>
                          </form>
                        </ul>
                      </li>
                    </ul>
                  </li>
				</div>
				<!--controlVersionOptions-->
				<div class="optionsGroup">
                  <li><a href="NoteServlet?id=${note.id}">
                    <img title="Go to actual version" src="images/actualversion-ico.png" alt="Button for go to the actual version of the note">
                  </a></li>
				</div>
              </ul>
            </div>
        </div>
      </div>

	</body>
</html>
