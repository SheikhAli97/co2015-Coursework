$(function () {

    // We can attach the `fileselect` event to all file inputs on the page
    $(document).on('change', ':file', function () {
        var input = $(this),
            numFiles = input.get(0).files ? input.get(0).files.length : 1,
            label = input.val().replace(/\\/g, '/').replace(/.*\//, '');
        input.trigger('fileselect', [numFiles, label]);
    });

    // We can watch for our custom `fileselect` event like this
    $(document).ready(function () {
        $(':file').on('fileselect', function (event, numFiles, label) {

            var input = $(this).parents('.input-group').find(':text'),
                log = numFiles > 1 ? numFiles + ' files selected' : label;

            if (input.length) {
                input.val(log);
            } else {
                if (log) alert(log);
            }

        });
    });

});


var maxLength = 55;

$('#story-intro').keyup(function () {
    var length = $(this).val().length;
    var remaningLength = maxLength - length;
    $('#chars').text(remaningLength);
});

var maxUploadSize = 20000000;
$('#uploadFile').bind('change', function () {
    if (this.files[0].size >= maxUploadSize) {
        $('#fileUploadSpan').html("Your cover is too big, please select an image smaller than 20MB" + "<br/>" +  "Uploaded File Size: " + this.files[0].size + " bytes");
        // disable teh submit button
        $('#submitButton').addClass('disabled');
    } else {
        // re-enable the submit button
        $('#submitButton').removeClass('disabled');
        $('#fileUploadSpan').fadeOut('fast');
    }
});
