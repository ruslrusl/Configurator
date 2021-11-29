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
                '<td style="display: none" id="completename' + rownumber + i + '"></td>' +
                '<td style="display: none" id="completeprovider' + rownumber + i + '"></td>' +
                '<td style="display: none" id="completeunit' + rownumber + i + '"></td>' +
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
    let descr = selected.attr("descr");
    let unit = selected.attr("unit");
    let provider = selected.attr("provider");
    $("#completedescr" + detail).text(descr);
    $("#completeprice" + detail).text(price);
    $("#completecoef" + detail).val(coef);
    $("#completecount" + detail).val(1);
    $("#completename" + detail).text(selected.text());
    $("#completeunit" + detail).text(unit);
    $("#completeprovider" + detail).text(provider);
    calcPrice("complete", detail);
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

function getObj() {
    let arr = [];
    for (let i = 1; i <= 100; i++) {
        let el = $("#boxnumber" + i);
        if (el.length == 0) {
            break;
        } else {
            let number = $("#boxnumber" + i).text();
            let mlfbrus = $("#boxmlfbrus" + i).text();
            let mlfb = $("#boxmlfb" + i).text();
            let descr = $("#boxdescr" + i).html();
            let price = $("#boxprice" + i).text();
            let count = $("#boxcount" + i).val();
            let pricecount = $("#boxpricecount" + i).text();
            let coef = $("#boxcoef" + i).val();
            let pricecoef = $("#boxpricecoef" + i).text();
            let pricetotal = $("#boxpricetotal" + i).text();
            let totalpricecoef = $("#boxtotalpricecoef" + i).text();
            let totalpricetotal = $("#boxtotalpricetotal" + i).text();
            let completearr = [];
            for (let j = 1; j <= 100; j++) {
                let elcomp = $("#completedescr" + i + j);
                if (elcomp.length == 0) {
                    break;
                } else {
                    let completename = $("#completename" + i + j).text();
                    let completeunit = $("#completeunit" + i + j).text();
                    let completeprovider = $("#completeprovider" + i + j).text();
                    let completedescr = $("#completedescr" + i + j).text();
                    let completeprice = $("#completeprice" + i + j).text();
                    let completecount = $("#completecount" + i + j).val();
                    let completepricecount = $("#completepricecount" + i + j).text();
                    let completecoef = $("#completecoef" + i + j).val();
                    let completepricecoef = $("#completepricecoef" + i + j).text();
                    let completepricetotal = $("#completepricetotal" + i + j).text();
                    let complete = {
                        name: completename,
                        unit: completeunit,
                        provider: completeprovider,
                        descr: completedescr,
                        price: completeprice,
                        count: completecount,
                        pricecount: completepricecount,
                        coef: completecoef,
                        pricecoef: completepricecoef,
                        pricetotal: completepricetotal
                    };
                    completearr.push(complete);
                }
            }
            let obj = {
                number: number,
                mlfbrus: mlfbrus,
                mlfb: mlfb,
                descr: descr,
                price: price,
                count: count,
                pricecount: pricecount,
                coef: coef,
                pricecoef: pricecoef,
                pricetotal: pricetotal,
                totalpricecoef: totalpricecoef,
                totalpricetotal: totalpricetotal,
                complete: completearr
            };
            arr.push(obj);
        }
    }
    return arr;
}

function basketExport(type) {
    //json объект в виде массива
    let arr = getObj();
    let jsonObj = {
        "type": type,
        "sensors": arr
    }
    let json = JSON.stringify(jsonObj);

    var model = jsonObj;
    var modelJson = JSON.stringify(model);
    var xhttp = new XMLHttpRequest();

    xhttp.onreadystatechange = function () {
        if (this.readyState == 4 && this.status == 200) {

            let disposition = this.getResponseHeader('content-disposition');
            let filename = "";

            if (disposition && disposition.indexOf('attachment') !== -1) {
                var filenameRegex = /filename[^;=]*=((['"]).*?2|[^;]*)/;
                var matches = filenameRegex.exec(disposition);
                if (matches != null && matches[1]) filename = matches[1].replace(/['"]/g, '');
            } else {
                filename = 'mlfb.xlsx'
            }

            let downloadUrl = URL.createObjectURL(xhttp.response);
            let a = document.createElement("a");
            document.body.appendChild(a);
            a.style = "display: none";
            a.href = downloadUrl;
            a.download = filename;
            a.click();
        }
    };
    xhttp.open("POST", "/getfile", true);
    xhttp.setRequestHeader("Content-Type", "application/json");
    xhttp.responseType = "blob";
    xhttp.send(modelJson);

}