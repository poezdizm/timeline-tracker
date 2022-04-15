let timeline;

$(document).ready(function() {

    getTimeline();

    $('#new_character').on('click', function(e) {
        clearInputsForCharacter();
        $('#char_modal').modal('show');
    });
    $('#character_save').on('click', function(e) {
        saveCharacter();
        $('#char_modal').modal('hide');
    });
    $('#modal_close').on('click', function(e) {
        $('#char_modal').modal('hide');
    });
});

function getTimeline() {
    let url = "/timeline/data";
    $.ajax({
        method: "GET",
        url: url,
        dataType: "json",
        success : function(data, textStatus, jqXHR) {
            initTimeline(data);
        }
    });
}

function initTimeline(data) {
    let container = document.getElementById('timeline_vis');

    let items = new vis.DataSet(data.events);

    let options = {};

    timeline = new vis.Timeline(container, items, options);
}

function clearInputsForCharacter() {
    $('#char_id_input').val("");
    $('#name_input').val("");
    $('#image_input').val("");
    $('#main_check').prop('checked', true);
    $('#dead_check').prop('checked', false);
}

function saveCharacter() {
    let character = {};
    character.id = $('#char_id_input').val();
    character.name = $('#name_input').val();
    character.image = $('#image_input').val();
    character.isMain = $('#main_check').is(':checked');
    character.isDead = $('#dead_check').is(':checked');
    $.ajax({
        method: "POST",
        url: "/characters/character",
        dataType: "json",
        data: JSON.stringify(character),
        contentType: "application/json; charset=utf-8",
        success : function(data, textStatus, jqXHR) {
            //reInitNetwork();
        }
    });
}