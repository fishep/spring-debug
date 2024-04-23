<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<html>
    <head>
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width,initial-scale=1.0">
        <link rel="icon" href="/favicon.ico">
        <title>grace</title>
    </head>
    <body>
        <noscript>
            <strong>
                We're sorry but browser doesn't work properly without JavaScript enabled.
                Please enable it to continue.
            </strong>
        </noscript>
        <div id="path">
            webapp/WEB-INF/index.jsp
        </div>
        <br>
        <div id="app">
            ${data}
        </div>
    </body>
</html>
