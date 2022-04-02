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
    var nodes = new vis.DataSet(data.characterModels.map(m => ({id: m.id, label: m.name, shape: "circularImage", image: m.imageUrl})));
    var edges = new vis.DataSet(data.relationModels);

    var container = document.getElementById('char_network');

    var data = {
        nodes: nodes,
        edges: edges
    };
    var options = {};

    var network = new vis.Network(container, data, options);
}