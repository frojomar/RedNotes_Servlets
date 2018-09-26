<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta http-equiv="content-type" content="text/html; charset=utf-8">
    <title> RedNotes - Share Note</title>
    <link rel="stylesheet" type="text/css" href="css/master.css">
    <link rel="stylesheet" type="text/css" href="css/friends.css">
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
    <div id="body">
      <c:forEach var="message" items="${messages}">
        <p>${message}</p>
      </c:forEach>
      <div id="tableRow">
        <!-- LIST OF FRIENDS-->
        <div id="LeftColumn" class="friendsList">
          <h3>Available friends to share the note</h3>
			<form method="get" action="NotesServlet">
        		<button type="submit">
          			<img src="images/goback-icon.jpg" title="Go back" alt="Go back">
  					<span> Go back </span>
      	    	</button>
     		 </form>
          <ul>
            <c:forEach var="friend" items="${friendslist}">
	            <li class="petition"><img class="userico" src="${friend.image}" alt="">
	              <h4>${friend.name} (@${friend.username})</h4>
                <form action="ShareNoteServlet" method="post">
                  <input type="hidden" name="idn" value="${idnote}">
                  <input type="hidden" name="idu" value="${friend.idu}">
                  <input type="submit" name="actbutton" value="Share">
  	    		</form>                
	            </li>
            </c:forEach>
          </ul>
        </div>
      </div>
    </div>
  </body>
</html>
