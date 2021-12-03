function changeSensorType() {
    let sensorsTypeId = $("#sensorsType option:selected").val();
    display("btnNext", false);
    clear("modelvalue");
    clear("signalvalue");
    display("modelspan", false);
    display("signalspan", false);

    $("#loading").show();
    $.getJSON("/getmodel/" + sensorsTypeId)
        .done(function (data) {
            let elSelect = $('<select  class="selectpicker" title="Выберите из списка..." id="sensorsModel" onchange="changeSensorModel()"/>');
            $.each(data, function (i, item) {
                let id = item['id'];
                let name = item['name'];
                elSelect.append($('<option id="' + id + '" value="' + id + '">' + name + '</option>>'));
            });
            display("modelspan", true);
            $("#modelvalue").append(elSelect[0]);
            elSelect.selectpicker('render');
            $("#loading").hide();
        })
        .fail(function (data) {
            display("btnNext", false);
            $("#loading").hide();
            loginfo("Ошибка при выборе моделей. Код ошибки: " + data.status, 4);
        });
}

function clear(elid) {
    $("#" + elid).empty();
}

function display(elid, isvisible) {
    let disp = 'none';
    if (isvisible) {
        disp = 'block';
    }
    $('#' + elid).css('display', disp);
}

function changeSensorModel() {
    let sensorsTypeId = $("#sensorsType option:selected").val();
    let sensorsModelId = $("#sensorsModel option:selected").val();
    clear("signalvalue");
    $("#loading").show();
    $.getJSON("/getsignal/" + sensorsTypeId + "/" + sensorsModelId)
        .done(function (data) {
            if (sensorsTypeId == 1) {//КМ35-М
                let elSelect = $('<select id="sensorsSignal"/>');
                $.each(data, function (i, item) {
                    let id = item['id'];
                    let name = item['name'];
                    let idSensor = item['idSensor'];
                    elSelect.append($('<option id="' + id + '" value="' + idSensor + '">' + name + '</option>>'));
                });
                $("#signalvalue").append(elSelect[0]);

                display("signalspan", false);
                display("signalvalue", false);
                display("btnNext", true);
            } else {
                let elSelect = $('<select id="sensorsSignal" class="selectpicker" title="Выберите из списка..." onchange="changeSensorSignal()"/>');
                $.each(data, function (i, item) {
                    let id = item['id'];
                    let name = item['name'];
                    let idSensor = item['idSensor'];
                    elSelect.append($('<option id="' + id + '" value="' + idSensor + '">' + name + '</option>>'));
                });
                display("signalspan", true);
                display("signalvalue", true);
                display("btnNext", false);
                $("#signalvalue").append(elSelect[0]);
                elSelect.selectpicker('render');
            }
            $("#loading").hide();
        })
        .fail(function (data) {
            display("btnNext", false);
            $("#loading").hide();
            loginfo("Ошибка при выборе типа связи. Код ошибки: " + data.status, 4);
        });
}

function changeSensorSignal() {
    display("btnNext", true);
}

function nextBtn() {
    let sensorId = $("#sensorsSignal option:selected").val();
    window.location = "/configurator/" + sensorId;
}