<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">

  <head>
    <meta charset="utf-8">
    <title> RedNotes - Delete Friend </title>
    <link rel="stylesheet" href="css/master.css">
    <link rel="stylesheet" href="css/reglogin.css">
    <link rel="stylesheet" href="css/delete.css">
    <link rel="icon" type="image/png" href="images/myfavicon.png">

  </head>

  <body>
    <header>
        <div class="logoname">
          <img class="logo" src="images/myfavicon.png" alt="">
          <h1 class="name">RedNotes</h1>
        </div>
    </header>
    <div class="content">
      <div class="box" id="regcompleted">
          <h1> Are you sure you want to delete "@${friend.username}" <br>
             from your friends list? </h1>
          <img class="userico2" src="${friend.image}" alt="">
          <p>"${friend.name} (@${friend.username})"</p>

          <form action="DeleteFriendsServlet?id=${friend.idu}" method="post">
            <input type="submit" name="confirm" value="Yes, I am sure">
            <input type="submit" name="confirm" value="No, I don't want to delete it.">
          </form>

          <h3><a href="FriendsServlet"> Go back </a></h3>
      </div>
    </div>
  </body>


</html>
