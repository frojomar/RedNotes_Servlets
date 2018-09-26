<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">

  <head>
    <meta charset="utf-8">
    <title> RedNotes - Delete Version </title>
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
          <h1> Are you sure you want to return to this version (and delete later versions)? </h1>

          <div class="note ${note.color}">
              <h4>[${note.modificationDate}]${note.title} </h4>
              <p> ${note.content}</p>
          </div>

          <form action="ChangeVersionServlet?id=${note.id}&date=${note.dateVersion}" method="post">
            <input type="submit" name="confirm" value="Yes, I am sure">
            <input type="submit" name="confirm" value="No, I don't want to delete it.">
          </form>

          <h3><a href="NoteServlet?id=${note.id}"> Go back </a></h3>
      </div>
    </div>
  </body>


</html>