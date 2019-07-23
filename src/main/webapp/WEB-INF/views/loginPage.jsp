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
        .error{
            color: #ff0000;
        }
    </style>

</head>
<body class="login-page">
<!-- Navigation Bar Fragment -->
<%@include file="./fragments/navigation.jspf" %>
<div style="margin-bottom: 150px"></div>

<c:url value="/login" var="formAction"/>
<section class="mt-5 text-center">
    <div class="container">
        <div class="card-wrapper mx-auto">
            <div class="card border-0">
                <h2 class="card-title">Login</h2>
                <div class="card-body text-left">
                    <form:form modelAttribute="User" method="POST" action="${formAction}">
                        <%-- UserName section --%>
                        <div class="form-group">
                            <label class="col-form-label ml-2" for="username">Username</label>
                            <input class="form-control border-bottom rounded-0" type="text" id="username" name="username" placeholder="Type in your username" required/>
                        </div>
                        <%-- Password Section --%>
                        <div class="form-group">
                            <label class="col-form-label ml-2" for="password">Password</label>
                            <input class="form-control border-bottom rounded-0" type="password" id="password" name="password" placeholder="Type in your password" style="background: transparent !important;" required/>
                            <div class="text-right mt-1"><a href="#" class="text-white">Forgot password?</a></div>
                        </div>
                        <div class="error text-center">${errorMessage}</div>
                        <div class="form-group mt-4">
                                <%-- Login Button --%>
                            <button type="submit" class="btn btn-success btn-block">LOG IN</button>
                            <div class="text-center mt-3">
                                <p class="text-white mb-2">Don't have an account?</p>
                                    <%-- Sign Up Button --%>
                                <a href="${pageContext.request.contextPath}/user-registerPage" class="text-success">SIGN UP</a>
                            </div>
                        </div>
                    </form:form>
                    <c:if test="${logout == true}">
                        <div class="alert text-center text-warning" role="alert" id="logout-alert">
                            <strong> You have successfully been logged out. </strong>
                        </div>
                    </c:if>
                </div>

            </div>
        </div>
    </div>
</section>
<script src="https://code.jquery.com/jquery-3.3.1.min.js"></script>
<script src="/js/custom.js"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.2.1/js/bootstrap.min.js"></script>
</body>

</html>
