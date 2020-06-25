<!DOCTYPE html>
<html lang="de">
  <head>
    <meta charset="utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
    <script src="js/crud.js"></script>

    <link rel="stylesheet" type="text/css" href="css/style.css">
    <link rel="stylesheet" type="text/css" href="css/styleArticle.css">
    <title>Titel</title>
  </head>
  <body>
    <?php
        include("menu.php");
    ?>
    <div id = "getArticles">
        <h3>Get Customer</h3>
        <input type="text" id="id" name="id">
        <button type="button" onclick="fredsGetRequestCustomer()">Request data</button>
        <div id="responseGet"></div>
    </div>

    <div id = "postCustomer">
        <h3>add new Customer</h3>
        <form id = "insertCustomer" enctype='application/json'>
            <label for="first_name">first Name</label>
            <input type="text" id="first_name" name="first_name" value = "peter"><br>
            <label for="last_name">Last Name</label>
            <input type="text" id="last_name" name="last_name" value="pizza">
            <br><label for="street">Street</label>
            <input type="text" id="street" name="street" value='23'><br>
            <label for="number">Number</label>
            <input type="text" id="number" name="number" value = '33'><br>
            <label for="city">City</label> 
            <input type="text" id="city" name="city" value = '33'><br>
            <label for="zip">ZIP</label> 
            <input type="text" id="zip" name="zip" value = '2222'><br>
            <label for="email">Email</label> 
            <input type="text" id="email" name="email" value = '33sadf@sadlfj'><br>
            <label for="phone">Phone</label> 
            <input type="text" id="phone" name="phone" value = '33'><br>
            <button type="button" onclick="postDataCustomer()">Post data</button>
        </form>
        <div id="responsePost"></div>
    </div>

  </body>
</html>