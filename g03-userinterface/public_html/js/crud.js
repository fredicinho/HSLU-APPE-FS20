function postDataCustomer(){
    var xhttp = new XMLHttpRequest();
    var first_name = document.getElementById("first_name").value
    var last_name = document.getElementById("last_name").value
    var street = document.getElementById("street").value
    var number = document.getElementById("number").value
    var zip = document.getElementById("zip").value
    var city = document.getElementById("city").value
    var email = document.getElementById("email").value
    var phone = document.getElementById("phone").value
    //var message = '{"_id":"' + uuidv4() +'","name": "'+name+'","numberInStock":'+document.getElementById("numberInStock").value+',"category":{"ID":2,"name":"IT"}}")'

    var message ='{"first_name": "'+first_name+'","last_name": "'+last_name+'","street": "'+street+'","number": "'+number+'","zip": "'+zip+'","city": "'+city+'","email": "'+email+'","phone": "'+phone+'"}';
    xhttp.onreadystatechange = function() {
        document.getElementById("responsePost").innerHTML = xhttp.responseText;
    };
    xhttp.open("POST", "http://restinterface.appe-g03.el.eee.intern/api/v1/customers/", true);
    xhttp.setRequestHeader("Content-type", "application/json");
    xhttp.send(message);
}

    function getRequestArticle() {
        var id = document.getElementById("id").value
        var url = "http://restinterface.appe-g03.el.eee.intern/api/v1/storage?storageItemID="+id
        $.getJSON( url, function( data ) {
    var items = [];
    $.each( data, function( key, val ) {
        items.push( "<li id='" + key + "'>" + val + "</li>" );
    });
    
    $( "<ul/>", {
        "class": "my-new-list",
        html: items.join( "" )
    }).appendTo( document.getElementById("getArticles") );
    });
    }


    function fredsGetRequestCustomer() {
        var id = document.getElementById("id").value
        $.get("http://restinterface.appe-g03.el.eee.intern/api/v1/customers/"+id, function(data, status){
            document.getElementById("responseGet").innerHTML = JSON.parse(data);
          });
    }

    function postDataArticle(){
        var xhttp = new XMLHttpRequest();
        var uuid = uuidv4();

        var e = document.getElementById("category");
        var categoryName = e.options[e.selectedIndex].value;
        var categoryUUID= document.getElementById("category").selectedIndex
        var name = document.getElementById("name").value;
        var message = '{"_id":"' + uuidv4() +'","name": "'+name+'","numberInStock":'+document.getElementById("numberInStock").value+',"category":{"ID":'+categoryUUID+',"name":"'+categoryName+'"}}")'

        xhttp.onreadystatechange = function() {
            if (this.readyState == 4 && this.status == 200) {
              console.log(message);  
            }
        };
        xhttp.open("POST", "http://restinterface.appe-g03.el.eee.intern/api/v1/storage", true);
        xhttp.setRequestHeader("Content-type", "application/json");
        xhttp.send(message);
    }

    function getRequestCustomer() {
        var id = document.getElementById("id").value
        var url = "http://restinterface.appe-g03.el.eee.intern/api/v1/customers/"+id
        $.getJSON( url, function( data ) {
    var items = [];
    $.each( data, function( key, val ) {
        //var jsonData = JSON.parse(val);
        //document.write(val[0].first_name);
        var length = Object.keys(val).length
        for (var i = 0; i < length; i++) {
        var counter = val[i];
        console.log(counter.first_name);
        items.push( "<li id='" + key + "'>" + counter.first_name + " "+ counter.last_name + "</li>" );
        }
        
    });

    
    $( "<ul/>", {
        "class": "my-new-list",
        html: items.join( "" )
    }).appendTo( document.getElementById("getArticles") );
    });
    }



   



    
