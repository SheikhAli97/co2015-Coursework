// scroll animation when the pen icon is clicked.
$(".contribute-button").click(function () {
    $([document.documentElement, document.body]).animate({
        scrollTop: $("#contribute-textarea").offset().top
    }, 2000);
    // focus on the contribute text area when scroll animation is done.
    $("#contribute-textarea").focus();
});


var maxLength = 75;
$('#contribute-textarea').bind({
    keyup: function () {
        var length = $(this).val().length;
        var remaningLength = maxLength - length;
        $('#chars').text(remaningLength);
    }});


// fade in effect when the branch icon under the picture is clicked
$('.branch-button').click(function () {
    $('.branch-group').fadeToggle('slow', 'swing');
});

// fade effect when branch this story button is clicked.
$('#brancher').click(function () {
    $('.branch-link i').fadeToggle('slow', 'swing');
});


// dynamically set form action based on where was clicked.
$('#extractModal').on('show.bs.modal', function (event) {
    var link = $(event.relatedTarget) // Button that triggered the modal
    // The story id to branch from
    var idd = link.data('id');
    // the point to sublist to (0 -> branchpoint(exclusive))
    var branchPoint = link.data('branchpoint'); // Extract info from data-* attributes

    console.log(branchPoint);
    // Add the dynamic action attribute to the form.
    $('#modalForm').attr("action", "/story/branch/?id=" + idd + "&branchPoint=" + branchPoint);
});
