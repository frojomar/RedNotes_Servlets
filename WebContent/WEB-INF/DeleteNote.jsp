<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">

  <head>
    <meta charset="utf-8">
    <title> RedNotes - Delete Note </title>
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
          <h1> Are you sure you want to delete this note? </h1>

          <div class="note ${note.color}">
              <h4>${note.title} </h4>
              <p> ${note.content}</p>
          </div>

          <form action="DeleteNoteServlet?id=${note.id}" method="post">
            <input type="submit" name="confirm" value="Yes, I am sure">
            <input type="submit" name="confirm" value="No, I don't want to delete it.">
          </form>

          <h3><a href="NotesServlet"> Go back </a></h3>
      </div>
    </div>
  </body>


</html>
