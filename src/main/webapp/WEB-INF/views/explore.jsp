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
    <link rel="stylesheet" href="/css/index.css">
    <link rel="stylesheet" href="/css/explore.css">

    <script src="https://code.jquery.com/jquery-3.3.1.min.js"></script>
</head>

<body style="background-color: #272727">
<!-- Navigation Bar Fragment -->
<%@include file="./fragments/navigation.jspf" %>

<!-- Container for Latest Stories -->
<div class="bot-margin"></div>
<div class="container">
    <!-- Display of Stories Div Start -->
    <div class="jumbotron mb-0 pb-0 text-center custom-font" style="background: none; color:white;">
        <h2><strong>${title} Stories</strong></h2>
        <p class="lead">${leadText}</p>
    </div>
    <hr style="background-color:white;">
    <div>
        <!-- List of Stories Start -->
        <div class="card-columns mt-4">
            <c:forEach var="Story" items="${storyList}" varStatus="loop">
                <!-- 4 Simple Alternating Colors -->
                <c:choose>
                    <c:when test="${loop.index % 4== 1}"> <c:set var="colorAttribute" value="color-1"/></c:when>
                    <c:when test="${loop.index % 4== 2}"> <c:set var="colorAttribute" value="color-2"/></c:when>
                    <c:when test="${loop.index % 4== 3}"> <c:set var="colorAttribute" value="color-3"/></c:when>
                    <c:otherwise> <c:set var="colorAttribute" value="color-4"/></c:otherwise>
                </c:choose>
                <!-- Story Card fragment -->
                <%@include file="./fragments/story_card.jspf" %>
                <div class="bot-margin"></div>
            </c:forEach>
        </div> <!-- List of Stories End -->
    </div>
    <div class="text-center mb-4">
        <button class="btn btn-secondary">See More</button>
    </div>
</div>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.2.1/js/bootstrap.min.js"></script>
</body>

</html>