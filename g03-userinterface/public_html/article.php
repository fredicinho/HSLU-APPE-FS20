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

    <script>
        function uuidv4() {
            return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function(c) {
            var r = Math.random() * 16 | 0, v = c == 'x' ? r : (r & 0x3 | 0x8);
            return v.toString(16);
            });
        }
    </script>
  </head>
  <body>
    <?php
        include("menu.php");
    ?>
    <div id = "getArticles">
        <h3>Get Articles</h3>
        <input type="text" id="id" name="id">
        <button type="button" onclick="getRequestArticle()">Request data</button>
    </div>

    <div id = "postArticles">
        <h3>add new Article</h3>
        <form id = "insertArticle" enctype='application/json'>
            <input type="text" id="id" name="id" hidden ><br>
            <label for="name">Name</label>
            <input type="text" id="name" name="name" value="pizza">
            <br><label for="numberInStock">Number</label>
            <input type="text" id="numberInStock" name="numberInStock" value='23'><br>
            <label for="price">Price</label>
            <input type="text" id="price" name="price" value = '33'><br>
            <label for="category">Category</label>
            <select id="category">
                <option name='category' value="IT">IT</option>
                <option name='category[1][name]'value="Software">Software</option>
                <option name='category[2][name]'value="Food">Food</option>
                <option name='category[3][name]'value="Car">car</option>
            </select>   
            <button type="button" onclick="postDataArticle()">Post data</button>
        </form>
    </div>
  </body>
</html>