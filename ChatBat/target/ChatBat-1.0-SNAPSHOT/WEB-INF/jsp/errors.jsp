<%@page contentType="text/html" pageEncoding="UTF-8" isErrorPage="true" trimDirectiveWhitespaces="true"%>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Ow!</title>
    </head>
    <body>
        <div class="container">
            <h1>${exception}</h1>
            ${errorMessage}
        </div>
    </body>
</html>