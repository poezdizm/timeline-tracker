let timeline;

$(document).ready(function() {

    getTimeline();

    $('#event_start_input').datepicker();
    $('#event_end_input').datepicker();

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

    $('#new_event').on('click', function(e) {
        clearInputsForEvent();
        $('#event_modal').modal('show');
    });
    $('#event_save').on('click', function(e) {
        saveEvent();
        $('#event_modal').modal('hide');
    });
    $('#modal_event_close').on('click', function(e) {
        $('#event_modal').modal('hide');
    });

    $('#new_chapter').on('click', function(e) {
        clearInputsForChapter();
        $('#chapter_modal').modal('show');
    });
    $('#chapter_save').on('click', function(e) {
        saveChapter();
        $('#chapter_modal').modal('hide');
    });
    $('#modal_chapter_close').on('click', function(e) {
        $('#chapter_modal').modal('hide');
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

    data.events.forEach(e => {
        e.className = "white";
    });
    let items = new vis.DataSet(data.events);

    let options = {
        zoomMin: 1000 * 60 * 60 * 24 * 6,
        zoomMax: 1000 * 60 * 60 * 24 * 31 * 12,
    };

    timeline = new vis.Timeline(container, items, options);
}

function clearInputsForCharacter() {
    $('#char_id_input').val("");
    $('#name_input').val("");
    $('#image_input').val("");
    $('#main_check').prop('checked', true);
    $('#dead_check').prop('checked', false);
}

function clearInputsForEvent() {
    $('#event_id_input').val("");
    $('#event_title_input').val("");
    $('#event_image_input').val("");
    $('#event_start_input').val('');
    $('#event_end_input').val('');

    $('#event_characters').empty();
    $('#event_chapters').empty();

    let characters = [];
    $.ajax({
        method: "GET",
        url: "/characters/all",
        async: false,
        dataType: "json",
        success : function(data, textStatus, jqXHR) {
            characters = data;
        }
    });
    characters.forEach(c => {
        $('#event_characters').append($('<option>', {
            value: c.id,
            text: c.label
        }));
    });

    let chapters = [];
    $.ajax({
        method: "GET",
        url: "/chapter",
        async: false,
        dataType: "json",
        success : function(data, textStatus, jqXHR) {
            chapters = data;
        }
    });
    chapters.forEach(с => {
        $('#event_chapters').append($('<option>', {
            value: с.id,
            text: с.title
        }));
    });
}

function clearInputsForChapter() {
    $('#chapter_id_input').val("");
    $('#chapter_title_input').val("");
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

function saveEvent() {
    let event = {};
    event.id = $('#event_id_input').val();
    event.title = $('#event_title_input').val();
    event.image = $('#event_image_input').val();
    event.start = $('#event_start_input').val();
    event.end = $('#event_end_input').val();
    event.characters = $('#event_characters').val();
    event.chapters = $('#event_chapters').val();
    $.ajax({
        method: "POST",
        url: "/event",
        dataType: "json",
        data: JSON.stringify(event),
        contentType: "application/json; charset=utf-8",
        success : function(data, textStatus, jqXHR) {
            //reInitNetwork();
        }
    });
}

function saveChapter() {
    let chapter = {};
    chapter.id = $('#chapter_id_input').val();
    chapter.title = $('#chapter_title_input').val();
    $.ajax({
        method: "POST",
        url: "/chapter",
        dataType: "json",
        data: JSON.stringify(chapter),
        contentType: "application/json; charset=utf-8",
        success : function(data, textStatus, jqXHR) {
            //reInitNetwork();
        }
    });
}