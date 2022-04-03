let network = null;
let chosenChar = null;

$(document).ready(function() {
    getNetwork();
    $('.network-choose').on('click', function(e) {
        if (!$(this).hasClass('disabled')) {
            chooseCharacter();
        }
    });
    $('.network-back').on('click', function(e) {
        if (!$(this).hasClass('disabled')) {
            chosenChar = null;
            network.destroy();
            network = null;
            getNetwork();
        }
    });
});

function chooseCharacter() {
    if (network !== null) {
        let nodes = network.getSelectedNodes();
        if (nodes.length !== 0) {
            chosenChar = nodes[0];
            getNetwork(nodes[0]);
        }
    }
}

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
    return null;
}

function initNetwork(data) {
    let selectButtons = $('.network-select');
    let optButtonsContainer = $('#opt_buttons');
    let buttonContainer = $('#network_buttons');
    let charLabels = $('.character-label');
    let relLabels = $('.relation-label');
    let chooseButtons = $('.network-choose');

    if (data.characterModels.length === 0) {
        $('.empty-label').removeClass('hidden');
    } else {
        data.characterModels.forEach(c => c.shape = "circularImage");
        data.relationModels.forEach(c => c.font = {align: "top"});

        var nodes = new vis.DataSet(data.characterModels);
        var edges = new vis.DataSet(data.relationModels);

        if (network !== null) {
            network.setData({nodes, edges});
            optButtonsContainer.addClass('shown');
        } else {
            optButtonsContainer.removeClass('shown');
            var container = document.getElementById('char_network');

            var data = {
                nodes: nodes,
                edges: edges
            };
            var options = {};

            network = new vis.Network(container, data, options);

            network.on("selectNode", function (e) {
                changeChosen(e, buttonContainer, charLabels, relLabels, chooseButtons);
            });
            network.on("selectEdge", function (e) {
                changeChosen(e, buttonContainer, charLabels, relLabels, chooseButtons);
            });
            network.on("deselectNode", function (e) {
                changeChosen(e, buttonContainer, charLabels, relLabels, chooseButtons);
            });
            network.on("deselectEdge", function (e) {
                changeChosen(e, buttonContainer, charLabels, relLabels, chooseButtons);
            });

            network.on("dragStart", function (e) {
                selectButtons.addClass('disabled');
                selectButtons.removeClass('network-button-active');
                selectButtons.addClass('network-button');
            });
            network.on("dragEnd", function (e) {
                changeChosen(e, buttonContainer, charLabels, relLabels, chooseButtons);
                selectButtons.removeClass('network-button');
                selectButtons.addClass('network-button-active');
                selectButtons.removeClass('disabled');
            });
        }
    }
}

function changeChosen(event, buttonContainer, charButtons, relButtons, chooseButtons) {
    if (event.nodes.length === 0 && event.edges.length === 0) {
        buttonContainer.removeClass('shown');
    } else {
        if (event.nodes.length !== 0) {
            relButtons.addClass('hidden');
            charButtons.removeClass('hidden');
            if (chosenChar !== null) {
                if (event.nodes[0] === chosenChar) {
                    chooseButtons.attr('disabled', true);
                    chooseButtons.removeClass('network-button-active');
                    chooseButtons.addClass('network-button');
                } else {
                    chooseButtons.attr('disabled', false);
                    chooseButtons.addClass('network-button-active');
                    chooseButtons.removeClass('network-button');
                }
            }
        } else {
            charButtons.addClass('hidden');
            relButtons.removeClass('hidden');
        }
        buttonContainer.addClass('shown');
    }
}