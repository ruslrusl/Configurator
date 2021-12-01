(function ($) {
    $.fn.inputFilter = function (inputFilter) {
        return this.on("input keydown keyup mousedown mouseup select contextmenu drop", function () {
            if (inputFilter(this.value)) {
                this.oldValue = this.value;
                this.oldSelectionStart = this.selectionStart;
                this.oldSelectionEnd = this.selectionEnd;
            } else if (this.hasOwnProperty("oldValue")) {
                this.value = this.oldValue;
                this.setSelectionRange(this.oldSelectionStart, this.oldSelectionEnd);
            } else {
                this.value = "";
            }
        });
    };
}(jQuery));

$(document).ready(function () {
    for (let i = 1; i <= 100; i++) {
        let el = $("#compordernumb" + i);
        if (el.length == 0) {
            break;
        } else {
            $("#compordernumb" + i).inputFilter(function (value) {
                return /^\d*$/.test(value);
            });
            $("#compprice" + i).inputFilter(function (value) {
                return /^-?\d*[.]?\d{0,2}$/.test(value);
            });
            $("#compcoef" + i).inputFilter(function (value) {
                return /^-?\d*[.]?\d{0,2}$/.test(value);
            });
        }
    }
});

function compadd() {
    let i = getNextIndexComp();

    $("#compTable > tbody").append("<tr id='comprow" + i + "'>" +
        "<td id='compid" + i + "' style='display: none;'>" + i + "</td>" +
        "<td><button type='button' class='btn btn-dark btn-sm' onclick='compdelete(" + i + ")'>Удалить</button></td>" +
        "<td><input id='compname" + i + "'></td>" +
        "<td><textarea id='compdescr" + i + "'></textarea></td>" +
        "<td><input id='compprice" + i + "' class='numberinput' value='0'></td>" +
        "<td><input id='compcoef" + i + "' class='numberinput' value='1.0'></td>" +
        "<td><textarea id='compprovider" + i + "'></textarea></td>" +
        "<td><input id='compunit" + i + "' class='numberinput'></td>" +
        "<td><input id='compordernumb" + i + "' class='nninput' value='" + i + "'></td>" +
        "</tr>");

    $("#compordernumb" + i).inputFilter(function (value) {
        return /^\d*$/.test(value);
    });
    $("#compprice" + i).inputFilter(function (value) {
        return /^-?\d*[.]?\d{0,2}$/.test(value);
    });
    $("#compcoef" + i).inputFilter(function (value) {
        return /^-?\d*[.]?\d{0,2}$/.test(value);
    });
}

function compdelete(i) {
    $("#comprow" + i).remove();
}

function getNextIndexComp() {
    let i;
    for (i = 1; i <= 100; i++) {
        let el = $("#compordernumb" + i);
        if (el.length == 0) {
            break;
        }
    }
    return i;
}

function compunsave() {
    window.location = "/settings";
}

function compsave() {
    $("#loading").show();
    let arr = getObj();
    var json = JSON.stringify(arr);
    var xhttp = new XMLHttpRequest();

    xhttp.onreadystatechange = function () {
        if (this.readyState == 4) {
            $("#loading").hide();
            if (this.status == 200) {
                loginfo("Изменения сохранены.", 1);
            } else {
                loginfo("Ошибка при сохранении. Код ошибки: " + this.status, 4);
            }
        }
    };
    xhttp.open("POST", "/savecomplete", true);
    xhttp.setRequestHeader("Content-Type", "application/json");
    xhttp.responseType = "blob";
    xhttp.send(json);
    $("#openSendEmail").modal('hide');
}

function getObj() {
    let arr = [];
    $('#compTable tr').each(function () {
        let i = $(this)[0].id.replace("comprow", "");
        if (i) {
            console.log(i);
            let id = $("#compid" + i).text();
            let name = $("#compname" + i).val();
            let descr = $("#compdescr" + i).text();
            let price = $("#compprice" + i).val();
            let coef = $("#compcoef" + i).val();
            let provider = $("#compprovider" + i).text();
            let unit = $("#compunit" + i).val();
            let ordernumb = $("#compordernumb" + i).val();

            let obj = {
                id: id,
                name: name,
                descr: descr,
                price: price,
                coef: coef,
                provider: provider,
                unit: unit,
                ordernumb: ordernumb,
                isused: 1
            };
            arr.push(obj);
        }
    });
    return arr;
}