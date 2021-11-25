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
        let el = $("#boxcount" + i);
        if (el.length == 0) {
            break;
        } else {
            $("#boxcount" + i).inputFilter(function (value) {
                return /^\d*$/.test(value);
            });
            $("#boxcoef" + i).inputFilter(function (value) {
                return /^-?\d*[.]?\d{0,2}$/.test(value);
            });
            $("#boxcount" + i).change(function () {
                calcPrice("box", i);
            });
            $("#boxcoef" + i).change(function () {
                calcPrice("box", i);
            });
            calcPrice("box", i);
        }
    }
});

function calcPrice(elbeginname, rownumber) {
    let count = $("#" + elbeginname + "count" + rownumber).val();
    let coef = $("#" + elbeginname + "coef" + rownumber).val();
    let price = $("#" + elbeginname + "price" + rownumber).text();
    let pricecount = price * count;
    let pricecoef = price * coef;
    let pricetotal = pricecoef * count;
    $("#" + elbeginname + "pricecount" + rownumber).text(pricecount.toFixed(2));
    $("#" + elbeginname + "pricecoef" + rownumber).text(pricecoef.toFixed(2));
    $("#" + elbeginname + "pricetotal" + rownumber).text(pricetotal.toFixed(2));
    calcTotalPrice();
}

function calcTotalPrice() {
    for (let i = 1; i <= 100; i++) {
        let sumPriceCoef = 0;
        let sumPriceTotal = 0;
        let el = $("#boxtotalpricecoef" + i);
        if (el.length == 0) {
            break;
        } else {
            let temp = $("#boxpricecoef" + i).text();
            sumPriceCoef = sumPriceCoef + Number(temp);
            temp = $("#boxpricetotal" + i).text();
            sumPriceTotal = sumPriceTotal + Number(temp);
            for (let j = 1; j <= 100; j++) {
                let el2 = $("#completepricecoef" + i + j);
                if (el2.length == 0) {
                    break;
                } else {
                    temp = $("#completepricecoef" + i + j).text();
                    sumPriceCoef = sumPriceCoef + Number(temp);
                    temp = $("#completepricetotal" + i + j).text();
                    sumPriceTotal = sumPriceTotal + Number(temp);
                }
            }
            $("#boxtotalpricecoef" + i).text(sumPriceCoef.toFixed(2));
            $("#boxtotalpricetotal" + i).text(sumPriceTotal.toFixed(2));
        }
    }
}

function addComplete(rownumber) {
    let oldelname;
    for (let i = 1; i <= 100; i++) {
        let el = $("#rowсomplete" + rownumber + i);
        if (el.length == 0) {
            if (i == 1) {
                oldelname = "rowсompletename" + rownumber;
            } else {
                oldelname = "rowсomplete" + rownumber + (i - 1);
            }
            let newname = "rowсomplete" + rownumber + i;
            let btnDel = '<button type="button" class="btn btn-outline-dark btn-sm" onclick="removeComplete(\'' + newname + '\')">Удалить</button>';
            let select = getSelectComplete(rownumber, i);
            $("#" + oldelname).after('<tr id="' + newname + '">' +
                '<td>' + btnDel + '</td>' +
                '<td>' + select + '</td>' +
                '<td id="completedescr' + rownumber + i + '"></td>' +
                '<td id="completeprice' + rownumber + i + '"></td>' +
                '<td><input id="completecount' + rownumber + i + '" class="basketinput" onchange="calcPrice(\'complete\',' + rownumber + i + ')"/></td>' +
                '<td id="completepricecount' + rownumber + i + '"></td>' +
                '<td><input id="completecoef' + rownumber + i + '" class="basketinput" onchange="calcPrice(\'complete\',' + rownumber + i + ')"/></td>' +
                '<td id="completepricecoef' + rownumber + i + '"></td>' +
                '<td id="completepricetotal' + rownumber + i + '"></td>' +
                '</tr>');
            $(".selectpicker").selectpicker("refresh");
            break;
        }
    }
}

function getSelectComplete(rownumber, i) {
    let s = $("#completeSelect").clone();
    return "<select class='selectpicker' title='Выберите компл...' data-width='200px' id='selectComplete" + rownumber + i + "' detail='" + rownumber + i + "' onchange='changeComplete(" + rownumber + i + ")'>" + s.html() + "</select>";
}

function removeComplete(name) {
    $("#" + name).remove();
}

function changeComplete(detail) {
    let el = $("#selectComplete" + detail);
    let selected = $(el).find("option:selected");
    let price = selected.attr("price");
    let coef = selected.attr("coef");
    $("#completedescr" + detail).text(selected.text());
    $("#completeprice" + detail).text(price);
    $("#completecoef" + detail).val(coef);
    $("#completecount" + detail).val(1);
    calcPrice("complete", detail);
}

function basketExport(type) {
    console.log("basketExport = "+type);
}