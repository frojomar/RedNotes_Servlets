<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">

  <head>
    <meta charset="utf-8">
    <title> RedNotes-Register Completed </title>
    <link rel="stylesheet" href="css/master.css">
    <link rel="stylesheet" href="css/reglogin.css">
    <link rel="icon" type="image/png" href="images/myfavicon.png">

  </head>

  <body>
    <header>
      <div class="logoname">
        <a href="LoginServlet">
          <img class="logo" src="images/myfavicon.png" alt="">
          <h1 class="name">RedNotes</h1>
        </a>
      </div>
    </header>

    <div class="content">
      <div class="box" id="regcompleted">
          <h1> Welcome @${username} !! </h1>
          <h2> Now you are a new Member </h2>
          <h3><a href="LoginServlet"> Go to log in </a></h3>
      </div>
    </div>
  </body>


</html>