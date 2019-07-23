<!DOCTYPE html>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html lang="en">
<head>

    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <meta name="description" content="">
    <meta name="author" content="">

    <title>Storalate</title>
    <link href="/css/index.css" rel="stylesheet">


    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.2.1/css/bootstrap.min.css">
    <link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.7.1/css/all.css">
    <link rel="stylesheet" href="https://fonts.googleapis.com/css?family=Lato|Kaushan+Script">
    <script src="https://code.jquery.com/jquery-3.3.1.min.js"></script>
</head>

<body>

<!-- Navigation Bar Fragment -->
<%@include file="./fragments/navigation.jspf" %>

<!-- Image with Central Text -->
<header class="text-center text-white d-flex">
    <div class="container my-auto">
        <div class="row">
            <div class="col-lg-10 mx-auto">
                <h1 class="text-uppercase custom-font">
                    <strong>Create stories. <span class="text-warning">Together.</span> </strong>
                </h1>
                <hr>
            </div>
            <div class="col-lg-8 mx-auto 8 text-center">
                <a class="btn btn-success btn-lg" href="${pageContext.request.contextPath}/user-registerPage"><i class="fas fa-user-plus"></i> Register</a>
                <a class="btn btn-warning btn-lg" href="${pageContext.request.contextPath}/explore"><i
                        class="fa fa-book"></i> Library</a>
            </div>
        </div>
    </div>
</header>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.2.1/js/bootstrap.min.js"></script>
</body>

</html>