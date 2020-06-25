
<!DOCTYPE html>
<html lang="de">
  <head>
    <meta charset="utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
    <script src="js/crud.js"></script>
    <link rel="stylesheet" href="css/style.css">
    <title>Titel</title>
    <style>
    html {
        padding:5%;    
        font-family: Arial, Helvetica, sans-serif;
    }
    table {
    font-family: arial, sans-serif;
    border-collapse: collapse;
    width: 100%;
    margin-bottom:10%;
    }

    td, th {
    border: 1px solid #dddddd;
    text-align: left;
    padding: 8px;
    }

    tr:nth-child(even) {
    background-color: #dddddd;
    }

    #myInput{
        margin-bottom:5%;
    }
    #response{
        background-color:green;
    }
    </style>

    <script>
     function fillTableWithGet() {
        //var id = document.getElementById("id").value
        var url = "http://restinterface.appe-g03.el.eee.intern/api/v1/customers/"
        $.getJSON( url, function( data ) {
        var items = [];
        //items.push( "<table >" );

        items.push( "<thead><tr><th>add</th><th>ID</th><th>Firstname</th><th>Lastname</th></tr></thead>" );
        items.push('<tbody id="myTable">');
        $.each( data, function( key, val ) {
            //var jsonData = JSON.parse(val);
            //document.write(val[0].first_name);
            var length = Object.keys(val).length
            for (var i = 0; i < length; i++) {
            var counter = val[i];
            items.push( "<tr> <td>" + "+" + "</td><td>'" + counter.uuid + "'</td><td>" + counter.first_name + "</td><td> "+ counter.last_name + "</td></tr>" );
        }
            
        });
        
        $( "<table/>", {
            "class": "my-new-list",
            html: items.join( "" )
        }).appendTo( document.getElementById("customerTable") );
        });
        fillTableUser();
    }
    function fillTableUser() {
        //var id = document.getElementById("id").value
        var url = "http://restinterface.appe-g03.el.eee.intern/api/v1/storage?storageItemID="
        $.getJSON( url, function( data ) {
        var items = [];
        //items.push( "<table >" );

        items.push( "<thead><tr><th>add</th><th>ID</th><th>Name</th><th>Number in Stock</th><th>Number in Stock</th></tr></thead>" );
        items.push('<tbody id="myTable">');
        $.each( data, function( key, val ) {
            //var jsonData = JSON.parse(val);
            //document.write(val[0].first_name);
            var length = Object.keys(val).length
            var counter = JSON.parse(val)
            console.log(counter.price)

            items.push( "<tr> <td>" + "+" + "</td><td>'" + counter._id + "'</td><td>" + counter.name + "</td><td> "+ counter.numberInStock + "</td><td> "+ counter.price + "</td></tr>" );
        
            
        });
        
        $( "<table/>", {
            "class": "my-new-list",
            html: items.join( "" )
        }).appendTo( document.getElementById("articleTable") );
        });
    }
    </script>
  </head>
  <body onload = "fillTableWithGet()">
    <?php
    include("menu.php");
    ?>
    <h1>Create order</h1>
    <label for="myInput">Search</label>
    <input id="myInput" type="text" placeholder="Search..">
    <h2>Select Customer from list</h2>
    <div id="customerTable">
    </div>
    <h2>Select articles</h2>
    <div id="articleTable">
    </div>

    <div id = "order">
        <h1>Your order</h1>
        <p>Add the values from the list above</p>
        <form id = "order" enctype='application/json'>
            <br><label for="customer">Customer</label>
            <input id="customerId" type="text" placeholder="customer" required>
            <br><label for="articleId">Article 1</label>
            <input id="articleId1" type="text" placeholder="Article" required>
            <input id="numberOfArticle1" type="number" placeholder="Number">
        </form>
    </div>
    <div id="postOrder">
        <button type="button" onclick="sendOrder()">Place order</button>
    </div>
    <div id="response">
</div>
    <script>
        var articlesCounter = 1;
        $(document).ready(function(){
            $("#myInput").on("keyup", function() {
                var value = $(this).val().toLowerCase();
                $("#customerTable tr").filter(function() {
                $(this).toggle($(this).text().toLowerCase().indexOf(value) > -1)
                });
            });
            }); 
            var pickedup;
            $(document).on("click", "#customerTable tr", function(){

                if (pickedup != null) {
                    pickedup.css( "background-color", "#ffccff" );
                }
        
                $("#customerId").val($(this).find("td").eq(1).html());
                $( this ).css( "background-color", "red" );
        
                pickedup = $( this );
            });

            var pickedup2;
            $(document).on("click", "#articleTable tr", function(){
                if (pickedup2 != null) {
                    pickedup2.css( "background-color", "#ffccff" );
                }
                
                var txt = $('#articleId'+articlesCounter);  
    
                if (txt.val() != null && txt.val() != '') {  
                    articlesCounter=articlesCounter+1; 
			        $("#order").append('<div><label for="articleId"'+articlesCounter+'>Article '+articlesCounter+'</label><input type="text" id="articleId'+articlesCounter+'"/><input id="numberOfArticle'+articlesCounter+'" type="number" placeholder="Number"><a href="#" class="remove_field">Remove</a></div>'); //add input box
                    $( this ).css( "background-color", "red" );
                    //console.log("#articleId"+articlesCounter+"");
                    var article = "#articleId"+articlesCounter+"";
                    $("#articleId"+articlesCounter).val($(this).find("td").eq(1).html());
                } else {  
                    $("#articleId"+articlesCounter).val($(this).find("td").eq(1).html());
                    $( this ).css( "background-color", "red" ); 

                } 

                
        
                pickedup2 = $( this );
            });

            $("#order").on("click",".remove_field", function(e){ //user click on remove text
                e.preventDefault(); $(this).parent('div').remove();
                articlesCounter--;
	        })

        function sendOrder(){
        var xhttp = new XMLHttpRequest();

        //var message ='{"first_name": "'+first_name+'","last_name": "'+last_name+'","street": "'+street+'","number": "'+number+'","zipCode": "'+zip+'","city": "'+city+'","email": "'+email+'","phone": "'+phone+'"}';
        var customerId = document.getElementById("customerId").value;
        console.log(document.getElementById("numberOfArticle1").value);
        var articles = '{"articleID":'+document.getElementById("articleId1").value +',"count":'+document.getElementById("numberOfArticle1").value + '}';
        console.log(articles)
        //console.log(articles[0])
        //console.log(articlesCounter)

        for(var i = 2; i <= articlesCounter; i++){
            //console.log("articleId"+i)
            //console.log(document.getElementById("articleId"+i).value + document.getElementById("numberOfArticle"+i).value)
            articles += ',{"articleID":'+document.getElementById("articleId"+i).value + ',"count":'+document.getElementById("numberOfArticle"+i).value+'}';
            console.log(articles)
        }
        xhttp.onreadystatechange = function() {
                 document.getElementById("response").innerHTML = xhttp.responseText;
         };
         
        var message = '{"customerID":'+customerId+',"orderPositionList":['+articles+']}'
        
        var messageOk = message.replace(/'/g, '"');
        console.log(messageOk); 
        //console.log(message);
        xhttp.open("POST", "http://restinterface.appe-g03.el.eee.intern/api/v1/order/", true);
        xhttp.setRequestHeader("Content-type", "application/json");
        xhttp.send(messageOk);
    }

    </script>
  </body>
</html>