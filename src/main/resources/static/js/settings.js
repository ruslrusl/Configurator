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
    $('.nav-tabs a').on('shown.bs.tab', function (e) {
        let hashArr = getHashArray();
        if (hashArr != null) {
            let curHash = e.target.hash.substring(1);
            if (!hashArr.includes(curHash)) {
                //меняем хэш в зависимости от клика
                let tabName = 'complete';
                if (curHash == tabName) {
                    tabName = 'price';
                }
                let index = hashArr.indexOf(tabName);
                if (index !== -1) {
                    hashArr[index] = curHash;
                }
                let hashStr = hashArr.join("&");
                window.location.hash = '#' + hashStr;
            }
        } else {
            window.location.hash = e.target.hash;
        }
    });

    let hashArr = getHashArray();
    if (hashArr != null) {
        if (hashArr.includes('price')) {
            $('#liprice').trigger('click');
        } else {
            let index = hashArr.indexOf('complete');
            if (index !== -1) {

            } else {
                window.location.hash = 'complete';
            }
        }
    } else {
        window.location.hash = 'complete';
    }
    changeSensor();
});

function getHashArray() {
    if (window.location.hash) {
        strhashes = window.location.href.split("#")[1];
        strhash = strhashes.split("&");
        return strhash;
    } else {
        return null;
    }
}

function changeSensor() {
    let mlfbid = $("#sensors option:selected").val();
    $("#sensorTable > tbody").empty();
    $("#loading").show();
    $.getJSON("/getprice/" + mlfbid)
        .done(function (data) {
            $.each(data, function (i, item) {
                let name = item['name'] != null ? item['name'] : '';
                let position = item['position'];
                let option = item['option'];
                let price = item['price'];
                $("#sensorTable > tbody").append("<tr>" +
                    "<td style='display: none;' id='pid" + i + "'>" + item['id'] + "</td>" +
                    "<td id='pname" + i + "'>" + name + "</td>" +
                    "<td>" + (position == 'Z' ? option : '') + "</td>" +
                    "<td>" + (position == '6' ? option : '') + "</td>" +
                    "<td>" + (position == '7' ? option : '') + "</td>" +
                    "<td>" + (position == '8' ? option : '') + "</td>" +
                    "<td>" + (position == '9' ? option : '') + "</td>" +
                    "<td>" + (position == '10' ? option : '') + "</td>" +
                    "<td>" + (position == '11' ? option : '') + "</td>" +
                    "<td>" + (position == '12' ? option : '') + "</td>" +
                    "<td>" + (position == '13' ? option : '') + "</td>" +
                    "<td>" + (position == '14' ? option : '') + "</td>" +
                    "<td>" + (position == '15' ? option : '') + "</td>" +
                    "<td>" + (position == '16' ? option : '') + "</td>" +
                    "<td><input id='pprice" + i + "' value='" + price + "' class='numberinput'></td>" +
                    "</tr>");
            });
            for (let i = 1; i <= 500; i++) {
                let el = $("#pprice" + i);
                if (el.length == 0) {
                    break;
                } else {
                    $("#pprice" + i).inputFilter(function (value) {
                        return /^-?\d*[.]?\d{0,2}$/.test(value);
                    });
                }
            }
            $("#loading").hide();
        })
        .fail(function (data) {
            loginfo("Ошибка при выборе цен. Код ошибки: " + data.status, 4);
            $("#loading").hide();
        });
}

function compadd() {
    let i = getNextIndexComp();

    $("#compTable > tbody").append("<tr id='comprow" + i + "'>" +
        "<td id='compid" + i + "' style='display: none;'>" + i + "</td>" +
        "<td><button type='button' class='btn btn-dark btn-sm' onclick='compdelete(" + i + ")'>Удалить</button></td>" +
        "<td><input id='compname" + i + "'></td>" +
        "<td><textarea id='compdescr" + i + "'></textarea></td>" +
        "<td><input id='compprice" + i + "' class='numberinput' value='0.00'></td>" +
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
    window.location.reload();
}

function priceunsave() {
    window.location.reload();
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
                window.location = "/settings";
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

function pricesave() {
    $("#loading").show();
    let arr = getPrice();
    var json = JSON.stringify(arr);
    var xhttp = new XMLHttpRequest();

    xhttp.onreadystatechange = function () {
        if (this.readyState == 4) {
            $("#loading").hide();
            if (this.status == 200) {
                loginfo("Изменения сохранены.", 1);
                window.location = "/settings#price";
            } else {
                loginfo("Ошибка при сохранении. Код ошибки: " + this.status, 4);
            }
        }
    };
    xhttp.open("POST", "/saveprice", true);
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

function getPrice() {
    let arr = [];
    for (let i = 0; i <= 500; i++) {
        let el = $("#pprice" + i);
        if (el.length == 0) {
            break;
        } else {
            let obj = {
                id: $("#pid" + i).text(),
                price: $("#pprice" + i).val()
            };
            arr.push(obj);
        }
    }
    return arr;
}