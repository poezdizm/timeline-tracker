$(document).ready(function() {
    $.ajax({
        method: "GET",
        url: "/characters/all",
        dataType: "json",
        success : function(data, textStatus, jqXHR) {
            $("#char_network").text(data[0].name);
        }
    });
});