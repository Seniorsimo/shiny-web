<%-- 
    Document   : index
    Created on : 26-nov-2013, 15.09.03
    Author     : Simone
--%>

<%@page contentType="text/html" pageEncoding="UTF-8" session="true"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Pizzeria da Ugo</title>
        <link href="stile.css" type="text/css" rel="stylesheet">
    </head>
    <body>
        <div id="wrap">
        <%@include file = "header.html"%>
        <div id="content">
        <%@include file = "nav.html"%>
        <article>
            Immagine pizza
        </article>
        <%@include file = "aside.jsp"%>
        </div>
        <%@include file = "footer.html"%>


        </div>

    </body>
</html>
