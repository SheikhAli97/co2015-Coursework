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

    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.2.1/css/bootstrap.min.css">
    <link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.7.1/css/all.css">
    <link rel="stylesheet" href="https://fonts.googleapis.com/css?family=Lato|Kaushan+Script">
    <link rel="stylesheet" href="/css/userPage.css">

    <style>
        .error {
            color: #ff0000;
        }
    </style>

</head>
<body class="login-page">
<!-- Navigation Bar Fragment -->
<%@include file="./fragments/navigation.jspf" %>
<div style="margin-bottom: 150px"></div>
<c:url value="/user-create" var="registerUser"/>
<section class="mt-5 text-center">
    <div class="container">
        <div class="card-wrapper mx-auto">
            <div class="card border-0">
                <h2 class="card-title">Sign Up</h2>
                <div class="card-body text-left mt-4">
                    <form:form modelAttribute="User" action="${registerUser}" method="POST">
                        <%-- UserName section --%>
                        <div class="form-group">
                            <label class="col-form-label ml-2" for="username">Username</label>
                            <input class="form-control border-bottom rounded-0" type="text" id="username" name="username" pattern="^[^\s]+(\s+[^\s]+)*$" placeholder="Type in a username" required/>
                            <form:errors path="username" cssClass="error ml-2"/>
                        </div>
                        <%-- Password Section --%>
                        <div class="form-group">
                            <label class="col-form-label ml-2" for="password">Password</label>
                            <input class="form-control border-bottom rounded-0" type="password" id="password" pattern="^[^\s]+(\s+[^\s]+)*$" name="password" placeholder="Type in a password" style="background: transparent !important;" required/>
                            <form:errors path="password" cssClass="error"/>
                        </div>
                        <div class="form-group mt-4">
                                <%-- Login Button --%>
                            <button type="submit" class="btn btn-success btn-block">Sign Up</button>
                            <div class="text-center mt-3">
                                <p class="text-white mb-2">Already have an account?</p>
                                    <%-- Sign Up Button --%>
                                <a href="${pageContext.request.contextPath}/user-login" class="text-success">LOG IN</a>
                            </div>
                        </div>
                    </form:form>
                </div>
            </div>
        </div>
    </div>

</section>
<script src="https://code.jquery.com/jquery-3.3.1.min.js"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.2.1/js/bootstrap.min.js"></script>
</body>

</html>
