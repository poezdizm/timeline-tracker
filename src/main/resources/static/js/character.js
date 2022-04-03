let network = null;
let chosenChar = null;
let chosenRel = null;

$(document).ready(function() {
    getNetwork();
    $('.network-choose').on('click', function(e) {
        if (!$(this).hasClass('disabled')) {
            $('#network_buttons').removeClass('shown');
            if ($(this).hasClass('character')) {
                chooseCharacter();
            } else {
                chooseRelation();
            }
        }
    });
    $('.network-back').on('click', function(e) {
        if (!$(this).hasClass('disabled')) {
            reInitNetwork();
        }
    });

    $('.network-edit').on('click', function(e) {
        if (network.getSelectedNodes().length === 0) {
            $('#rel_modal').modal('show');
            setInputsForRelation(network.body.edges[network.getSelectedEdges()[0]]);
        } else {
            $('#char_modal').modal('show');
            setInputsForCharacter(network.body.nodes[network.getSelectedNodes()[0]]);
        }
    });

    $('#character_save').on('click', function(e) {
        saveCharacter();
        $('#char_modal').modal('hide');
    });
    $('#modal_close').on('click', function(e) {
        $('#char_modal').modal('hide');
    });

    $('#rel_save').on('click', function(e) {
        saveRelation();
        $('#rel_modal').modal('hide');
    });
    $('#rel_modal_close').on('click', function(e) {
        $('#rel_modal').modal('hide');
    });

    $('.network-delete').on('click', function(e) {
        if (network.getSelectedNodes().length === 0) {
            $('#delete_char_modal_label')
                .text('Удалить персонажа ' + network.body.nodes[network.getSelectedNodes()[0]].options.label);
            $('#delete_char_modal').modal('show');
        } else {
            $('#delete_rel_modal').modal('show');
        }
    });

    $('#delete_character').on('click', function(e) {
        deleteCharacter(network.getSelectedNodes()[0]);
        $('#delete_char_modal').modal('hide');
    });
    $('#delete_modal_close').on('click', function(e) {
        $('#delete_char_modal').modal('hide');
    });

    $('#delete_rel').on('click', function(e) {
        deleteRelation(network.getSelectedEdges()[0]);
        $('#delete_char_modal').modal('hide');
    });
    $('#delete_rel_close').on('click', function(e) {
        $('#delete_rel_modal').modal('hide');
    });
});

function reInitNetwork() {
    $('#network_buttons').removeClass('shown');
    chosenRel = null;
    chosenChar = null;
    network.destroy();
    network = null;
    getNetwork();
}

function chooseCharacter() {
    if (network !== null) {
        let nodes = network.getSelectedNodes();
        if (nodes.length !== 0) {
            chosenRel = null;
            chosenChar = nodes[0];
            getNetwork(nodes[0]);
        }
    }
}

function chooseRelation() {
    if (network !== null) {
        let edges = network.getSelectedEdges();
        if (edges.length !== 0) {
            chosenChar = null;
            chosenRel = edges[0];
            getRelation(edges[0]);
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

function getRelation(id) {
    if (id === undefined) {
        return null;
    }
    let url = "/characters/network/relation?id=" + id;
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
        data.characterModels.forEach(c => {
            c.color =  {border: '#000000'};
            c.shadow = {
                enabled: true,
                color: 'rgba(0,0,0,0.5)',
                size:5,
                x:3,
                y:5
            };
            if (c.image === null || c.image === "" || c.image === undefined) {
                c.shape = "icon";
                c.icon = {
                    face: "'FontAwesome'",
                    code: "\uf007",
                    size: 50,
                    color: "#333333"
                };
                delete c.image;
            } else {
                c.shape = "circularImage"
            }
        });
        data.relationModels.forEach(c => {
            c.font = {align: "top"};
            c.length = 200;
        });

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
                selectButtons.removeClass('network-button');
                selectButtons.addClass('network-button-active');
                selectButtons.removeClass('disabled');
                changeChosen(e, buttonContainer, charLabels, relLabels, chooseButtons);
            });
        }
    }
}

function changeChosen(event, buttonContainer, charLabels, relLabels, chooseButtons) {
    if (event.nodes.length === 0 && event.edges.length === 0) {
        buttonContainer.removeClass('shown');
    } else {
        if (event.nodes.length !== 0) {
            chooseButtons.removeClass('relation');
            chooseButtons.addClass('character');
            relLabels.addClass('hidden');
            charLabels.removeClass('hidden');
            tweakButtons(event, chooseButtons, charLabels, relLabels, event.nodes[0], chosenChar);
        } else {
            chooseButtons.removeClass('character');
            chooseButtons.addClass('relation');
            charLabels.addClass('hidden');
            relLabels.removeClass('hidden');
            tweakButtons(event, chooseButtons, charLabels, relLabels, event.edges[0], chosenRel);
        }
        buttonContainer.addClass('shown');
    }
}

function tweakButtons(event, chooseButtons, charLabels, relLabels, elem, chosen) {
    if (chosen !== null) {
        if (elem === chosen) {
            chooseButtons.attr('disabled', true);
            chooseButtons.removeClass('network-button-active');
            chooseButtons.addClass('network-button');
        } else {
            chooseButtons.attr('disabled', false);
            chooseButtons.addClass('network-button-active');
            chooseButtons.removeClass('network-button');
        }
    } else {
        chooseButtons.attr('disabled', false);
        chooseButtons.addClass('network-button-active');
        chooseButtons.removeClass('network-button');
    }
}

function setInputsForCharacter(node) {
    let name = node.options.label;
    let dead = false;
    if (name.includes('\uD83D\uDC80')) {
        name = name.replace('\uD83D\uDC80', '');
        dead = true;
    }
    $('#char_id_input').val(node.id);
    $('#name_input').val(name);
    $('#image_input').val(node.options.image);
    $('#main_check').attr('checked', node.baseSize === 35);
    $('#dead_check').attr('checked', dead);
}

function setInputsForRelation(edge) {
    $('#rel_id_input').val(edge.id);

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
    $('#from_select').empty();
    $('#to_select').empty();
    $('#type_select').empty().append($('<option>', {
        value: '',
        text: 'Не указано',
        selected: true
    }));
    characters.forEach(c => {
        $('#from_select').append($('<option>', {
            value: c.id,
            text: c.label,
            selected: edge.fromId === c.id
        }));
        $('#to_select').append($('<option>', {
            value: c.id,
            text: c.label,
            selected: edge.toId === c.id
        }));
    });

    let types = [];
    $.ajax({
        method: "GET",
        url: "/relations/types",
        async: false,
        dataType: "json",
        success : function(data, textStatus, jqXHR) {
            types = data;
        }
    });
    types.forEach(c => {
        $('#type_select').append($('<option>', {
            value: c.id,
            text: c.label,
            selected: edge.options.label === c.label
        }));
    });
}

function saveCharacter() {
    let character = {};
    character.id = $('#char_id_input').val();
    character.name = $('#name_input').val();
    character.image = $('#image_input').val();
    character.isMain = $('#main_check').attr('checked') === 'checked';
    character.isDead = $('#dead_check').attr('checked') === 'checked';
    $.ajax({
        method: "POST",
        url: "/characters/character",
        dataType: "json",
        data: JSON.stringify(character),
        contentType: "application/json; charset=utf-8",
        success : function(data, textStatus, jqXHR) {
            reInitNetwork();
        }
    });
}

function saveRelation() {
    let relation = {};
    relation.id = $('#rel_id_input').val();
    relation.from = $( "#from_select option:selected" ).val();
    relation.to = $( "#to_select option:selected" ).val();
    relation.type = $( "#type_select option:selected" ).val();
    $.ajax({
        method: "POST",
        url: "/relations",
        dataType: "json",
        data: JSON.stringify(relation),
        contentType: "application/json; charset=utf-8",
        success : function(data, textStatus, jqXHR) {
            reInitNetwork();
        }
    });
}

function deleteCharacter(id) {
    let url = "/characters/character/delete?id=" + id;
    $.ajax({
        method: "POST",
        url: url,
        dataType: "json",
        success : function(data, textStatus, jqXHR) {
            reInitNetwork();
        }
    });
}

function deleteRelation(id) {
    let url = "/relations/delete?id=" + id;
    $.ajax({
        method: "POST",
        url: url,
        dataType: "json",
        success : function(data, textStatus, jqXHR) {
            reInitNetwork();
        }
    });
}