let network;

$(document).ready(function() {
    getNetwork();
});

function getNetwork(id) {
    let url = "/characters/network";
    if (id !== undefined) {
        url += "?id=" + id;
    }
    $.ajax({
        method: "GET",
        url: url,
        dataType: "json",
        success : function(data, textStatus, jqXHR) {
            initNetwork(data);
        }
    });
}

function initNetwork(data) {
    data.characterModels.forEach(c => c.shape = "circularImage");
    data.relationModels.forEach(c => c.font = {align: "top"});

    var nodes = new vis.DataSet(data.characterModels);
    var edges = new vis.DataSet(data.relationModels);

    var container = document.getElementById('char_network');

    var data = {
        nodes: nodes,
        edges: edges
    };
    var options = {};

    network = new vis.Network(container, data, options);

    let buttonContainer = $('#network_buttons');
    let charButtons = $('.character-label');
    let relButtons = $('.relation-label');
    network.on("selectNode", function(e) {
        changeChosen(e, buttonContainer, charButtons, relButtons);
    });
    network.on("selectEdge", function(e) {
        changeChosen(e, buttonContainer, charButtons, relButtons);
    });
    network.on("deselectNode", function(e) {
        changeChosen(e, buttonContainer, charButtons, relButtons);
    });
    network.on("deselectEdge", function(e) {
        changeChosen(e, buttonContainer, charButtons, relButtons);
    });

    let selectButtons = $('.network-select');
    network.on("dragStart", function(e) {
        selectButtons.removeClass('network-button-active');
        selectButtons.addClass('network-button');
    });
    network.on("dragEnd", function(e) {
        selectButtons.removeClass('network-button');
        selectButtons.addClass('network-button-active');
        buttonContainer.addClass('shown');
    });
}

function changeChosen(event, buttonContainer, charButtons, relButtons) {
    if (event.nodes.length === 0 && event.edges.length === 0) {
        buttonContainer.removeClass('shown');
    } else {
        if (event.nodes.length !== 0) {
            relButtons.addClass('hidden');
            charButtons.removeClass('hidden');
        } else {
            charButtons.addClass('hidden');
            relButtons.removeClass('hidden');
        }
        buttonContainer.addClass('shown');
    }
}