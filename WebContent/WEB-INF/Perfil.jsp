<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta http-equiv="content-type" content="text/html; charset=utf-8">
    <title> RedNotes-Perfil</title>
    <link rel="stylesheet" type="text/css" href="css/master.css">
    <link rel="stylesheet" type="text/css" href="css/perfil.css">
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
			<a href="EditPerfilServlet?selectImage=true"><img class="userico2" src="${userlogin.image}" alt=""></a>
			<h3>Personal information</h3>
			<p><span>Name: </span>${userlogin.name}</p>
			<p><span>Country: </span>${userlogin.country}</p>
			<p><span>City: </span>${userlogin.city}</p>
			<p><span>Age: </span>${userlogin.age} </p>
			<p><span>Telephone: </span>${userlogin.telephone}</p>
			<br>
			<h3>Other information</h3>
			<p><span>Email: </span>${userlogin.email}</p>
			<p><span>Username: </span>@${userlogin.username}</p>
			<p><span>Date of creation of account: </span>${userlogin.date} </p>

      <form method="get" action="DeleteAccountServlet">
        <button type="submit">
          <img class="deleteButton" title="Delete account" src="images/del-ico.png" alt="Button for delet account">
  				<span> Delete account</span>
          </button>
      </form>

      <form method="get" action="EditPerfilServlet">
        <button type="submit">
          <img src="images/edit-ico.png" title="Edit" alt="Button for edit perfil">
  				<span> Edit perfil </span>
          </button>
      </form>
		</div>
		<!--  LOWER BAR -->
<!--    <footer>
      <p> Information about us</p>
    </footer> -->
  </body>
</html>
