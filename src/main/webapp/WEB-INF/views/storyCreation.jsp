<!Doctype HTML>
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
    <script src="https://code.jquery.com/jquery-3.3.1.min.js"></script>

    <style>
        .error {
            color: #ff0000;
        }
    </style>

</head>
<body style="background-color: #272727">

<%@include file="./fragments/navigation.jspf" %>


<div class="container py-5 mt-5 px-5">
    <div class="card text-left mx-auto col-lg-9 my-5 rounded-0">
        <div class="card-header text-left col-lg-12 mx-0 bg-transparent pb-0 text-center"><h2>Start a Story</h2></div>
        <div class="card-body">
            <form:form name="story-creation-form" method="POST" action="/story/create/" modelAttribute="Story" enctype="multipart/form-data">
                <div class="form-group mb-1">
                    <label class="col-form-label" for="story-title">Title</label>
                        <%-- No WhiteSpace at the start or end allowed --%>
                    <input class="form-control" type="text" id="story-title" name="title" pattern="^[^\s]+(\s+[^\s]+)*$" required/>
                    <form:errors path="title" cssClass="error"/>
                </div>
                <div class="form-group mb-0">
                    <label class="col-form-label" for="story-intro">Start of your Story</label>
                        <%-- No WhiteSpace at the start or end allowed --%>
                    <input class="form-control" type="text" id="story-intro" name="introduction" maxlength="55" pattern="^[^\s]+(\s+[^\s]+)*$" required/>
                    <form:errors path="introduction" cssClass="error"/>
                    <p class="text-right text-muted mb-0"><span id="chars">55</span> characters remaining</p>
                </div>
                <div class="form-group mb-4">
                    <label class="col-form-label" for="story-category">Category</label>
                    <select class="form-control" id="story-category" name="category" required>
                        <option selected disabled>Choose a Category</option>
                        <c:forEach var="Category" items="${categories}">
                            <option value="${Category.toString()}">${Category.toString()}</option>
                        </c:forEach>
                    </select>
                </div>
                <div class="input-group">
                    <label class="input-group-btn">
                    <span class="btn btn-info">
                        Story Cover<input name="file" id="uploadFile" type="file" accept=".png,.jpg,.jpeg" style="display: none;">
                    </span>
                    </label>
                    <input type="text" class="form-control" readonly placeholder="only .JPEG,.JPG,.PNG accepted">
                </div>
                <div class="error mt-2" id="fileUploadSpan">${uploadError}</div>
                <button id="submitButton"  type="submit" class="btn btn-success btn-lg btn-block mt-4"><i class="fa fa-pen"> Create</i>
                </button>
            </form:form>
        </div>
    </div>

</div>
<script src="/js/storyCreation.js"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.2.1/js/bootstrap.min.js"></script>
</body>
</html>
