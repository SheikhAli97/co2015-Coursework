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
    <link href="/css/storyDetails.css" rel="stylesheet">

    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.2.1/css/bootstrap.min.css">
    <link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.7.1/css/all.css">
    <link rel="stylesheet" href="https://fonts.googleapis.com/css?family=Lato|Kaushan+Script">
    <link href="https://fonts.googleapis.com/icon?family=Material+Icons"
          rel="stylesheet">

    <style>
        .error {
            color: #ff0000;
            font-size: 18px;
            font-weight: 700;
        }
    </style>

</head>

<body>

<%-- There is 3 scenarios to consider:
    - A user can only contribute if the story is open and they have not contributed before
--%>
<c:choose>
    <%--Show form only if story is open and can contribute.--%>
    <c:when test="${story.isOpen() && canContribute == true }">
        <%--Display the user form--%>
        <c:set var="displayForm" value="true"/>
        <%--Set the values in the form--%>
        <c:set var="buttonText" value="Contribute"/>
        <c:set var="buttonColor" value="btn-success"/>
        <%--Set the stats icon and colors--%>
        <c:set var="statusIcon" value="fa-unlock"/>
        <c:set var="statusText" value="Open"/>
        <c:set var="statusColor" value="#82c91e"/>
    </c:when>
    <%--If the story is open but can't contribute disable form for the user--%>
    <c:when test="${story.isOpen() && canContribute == false}">
        <%--Disabled the form--%>
        <c:set var="displayForm" value="false"/>
        <%--Set the status icon, colors and text--%>
        <c:set var="statusIcon" value="fa-unlock"/>
        <c:set var="statusText" value="Open"/>
        <c:set var="statusColor" value="#82c91e"/>
    </c:when>
    <%--Otherwise story is not open--%>
    <c:otherwise>
        <%--Disable the form--%>
        <c:set var="displayForm" value="false"/>
        <%--Set the icons--%>
        <c:set var="statusIcon" value="fa-lock"/>
        <c:set var="statusText" value="Closed"/>
        <c:set var="statusColor" value="#aa2536"/>
    </c:otherwise>
</c:choose>

<!-- Navigation Bar Fragment -->
<div class="container-fluid">
    <%@include file="./fragments/navigation.jspf" %>
    <div class="row">
        <%--<div class="bck-image"></div>--%>
        <%--<div class="content2">--%>
        <!-- Image container + branches link?-->
        <div class="px-0 text-right col-lg-4" id="side-container">
            <!-- background for this side of the container -->
            <c:choose>
                <c:when test="${story.getImageUrl() != null}">
                    <c:set var="backgroundURL" value="${story.getImageUrl()}"/>
                </c:when>
                <c:otherwise><c:set var="backgroundURL" value="../images/default.png"/> </c:otherwise>
            </c:choose>
            <div class="background-image" style="background-image: url(${backgroundURL})"></div>
            <div class="content">
                <%-- image container --%>
                <div class="image-container text-right">
                    <!-- style and resize image appropriately -->
                    <img src="${backgroundURL}" alt="${story.getTitle()}-cover"/>
                </div>

                <%--Icons below the image--%>
                <div class="text-right col-lg-12 relative-content">
                    <%--@Link to save this to the users favourites so you view on dashboard--%>
                    <c:if test="${isAuthor == false}">
                        <c:choose>
                            <c:when test="${likedAlready == false}">
                                <c:set var="action" value="like"/>
                                <c:set var="likeIcon" value="fas fa-heart"/>
                            </c:when>
                            <c:otherwise>
                                <c:set var="action" value="dislike"/>
                                <c:set var="likeIcon" value="fas fa-heart-broken"/>
                            </c:otherwise>
                        </c:choose>
                        <a class="fav-button" href="/story/like/?id=${story.getId()}&action=${action}"><i
                                class="${likeIcon}"></i></a>
                    </c:if>
                    <%--@Link to scroll to the input text for logged in user to contribute, enable only if current max is not reach--%>
                    <a class="contribute-button" href="javascript:void(0)"><i
                            class="fas fa-pencil-alt"></i></a>
                    <%--@Link to branches of the story in view, if null disable this button and add hover button--%>
                    <a class="branch-button" href="#"><i class="fas fa-code-branch"></i></a>
                </div>

                <%--Section that is displayed upon the branch button being clicked.--%>
                <div class="branch-group text-right">
                    <!-- Clicking this will result in a page where this story is the parent -->
                    <a class="btn btn-success btn-block" href="/viewBranches/?id=${story.getId()}"
                       role="button" id="displayChildrenLink">View
                        Branches</a>
                    <!-- Clicking this will result in branch icons appearing next to each extract. -->
                    <a class="btn btn-success btn-block" href="javascript:void(0)" role="button" id="brancher">Branch
                        This
                        story</a>
                </div>
            </div>
        </div>

        <!-- The story container: fix classes -->
        <div class="story-container col-lg-5 px-4 mx-0 px-0" style="padding-top: 120px !important;">
            <!-- Populate story details here -->
            <div class="story-details py-3 mb-0">
                <p class="story-title">${story.getTitle()}</p>
                <div class="ml-1 story-categories row" style="margin-bottom: 20px;">
                    <div class="category-item mr-1"><a href="/">${story.getCategory().toString()}</a></div>
                </div>

                <div class="story-stats row">
                    <div class="ml-2 text-left col-sm read-only-stats" style="vertical-align: middle;">
                        <span title="Contributors"><i class="fas fa-users "
                                                      style="color: #ffc107;"></i> ${story.getContributorSize()}</span>
                        <span title="Favourites"><i class="fas fa-heart"
                                                    style="color: #ff5a5f;"></i> ${story.getLikeCount()}</span>
                        <%--if the user has already contributed show this visual indicator.--%>
                        <c:if test="${canContribute == false}">
                            <span title="contribution-status"><i class="fa fa-check-double" style="color: #82c91e"></i> Contributed</span>
                        </c:if>
                    </div>

                    <!-- status of the story -->
                    <div class="ml-2 text-right col-sm status-container">
                        <span title="story-status"><i class="fa ${statusIcon}"
                                                      style="color: ${statusColor};"></i> ${statusText}</span>
                    </div>
                </div>
                <!-- Extracts that belong to this story in this div -->
                <div class="story-segments mt-0">
                    <h4 class="segment-details">Started by ${story.getAuthor().getUsername()}</h4>
                    <div class="segment-contents">${story.getIntroduction()}</div>
                    <%--Populate this stories segments in here. Add a branch link after the first contribution up to n-1 where n = size of the list.--%>
                    <c:choose>
                        <c:when test="${story.getStoryExtracts().size() == 0}">
                            <c:set var="_label" value="<h3 class='text-success'>Be The First to Contribute</h3>"/>
                            <c:set var="_placeholder" value="Write the Next Segment"/>
                        </c:when>
                        <c:otherwise>
                            <%--If the size of the story extract is 1 then also display the link--%>
                            <%--Otherwise if n> 1 only display the link from n -> n--%>
                            <c:forEach var="StoryExtract" items="${story.getStoryExtracts()}" varStatus="loop">
                                <div class="row">
                                    <div class="col-md text-left">
                                        <h4 class="segment-details ">Segment ${loop.count}
                                            by ${StoryExtract.getExtractAuthor().getUsername()}</h4>
                                    </div>
                                        <%-- Clicking will launch the modal jspf to fill the extract to add after the branch.--%>
                                    <span class="col-sm branch-link mr-3">
                                        <a data-target="#extractModal" data-toggle="modal" data-id="${story.getId()}"
                                           data-branchpoint="${loop.index}" href="#extractModal" class="text-secondary"><i
                                                class="fas fa-external-link-alt"></i></a>
                                    </span>
                                </div>
                                <div class="segment-contents">
                                        ${StoryExtract.getContent()}
                                </div>
                            </c:forEach>
                            <c:set var="_placeholder" value="Continue the story.."/>
                        </c:otherwise>
                    </c:choose>
                    <c:if test="${displayForm == true}">
                        <form:form action="/story/addContribution/?id=${story.getId()}" method="POST"
                                   modelAttribute="StoryExtract">
                            <div class="form-group mt-4">
                                    ${_label}
                                <input name="content" class="form-control mt-1" id="contribute-textarea"
                                       pattern="^[^\s]+(\s+[^\s]+)*$" title="Spaces Only Allowed between alpha-numeric content"
                                       placeholder="${_placeholder}" maxlength="75" value="${existingContent}"
                                       required/>
                                <p class="text-right text-muted mb-0"><span id="chars">75</span> characters remaining
                                </p>
                                <div class="error mt-1">${errorMessage}</div>
                            </div>
                            <div class="form-group text-right">
                                <button class="btn btn-lg btn-success text-white" type="submit"
                                        id="submit-button">
                                    Add Segment
                                </button>
                            </div>
                        </form:form>
                    </c:if>
                </div>
            </div>
        </div>
    </div>
</div>

<%@include file="./fragments/extractModel.jspf" %>

<script src="https://code.jquery.com/jquery-3.3.1.min.js"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.2.1/js/bootstrap.min.js"></script>
<script src="/js/storyDetails.js"></script>
</body>

</html>