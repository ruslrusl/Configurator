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
    setFilter();
    $("#basketnumb").inputFilter(function (value) {
        return /^\d*$/.test(value);
    });
});

function setFilter() {
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
}

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
        let el = $("#row??omplete" + rownumber + i);
        if (el.length == 0) {
            if (i == 1) {
                oldelname = "row??ompletename" + rownumber;
            } else {
                oldelname = "row??omplete" + rownumber + (i - 1);
            }
            let newname = "row??omplete" + rownumber + i;
            let btnDel = '<button type="button" class="btn btn-outline-dark btn-sm" onclick="removeComplete(\'' + newname + '\')">??????????????</button>';
            let select = getSelectComplete(rownumber, i);
            $("#" + oldelname).after('<tr id="' + newname + '">' +
                '<td></td>' +
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

            $("#completecount" + rownumber + i).inputFilter(function (value) {
                return /^\d*$/.test(value);
            });
            $("#completecoef" + rownumber + i).inputFilter(function (value) {
                return /^-?\d*[.]?\d{0,2}$/.test(value);
            });

            break;
        }
    }
}

function getSelectComplete(rownumber, i) {
    let s = $("#completeSelect").clone();
    return "<select class='selectpicker' title='???????????????? ??????????...' data-width='200px' id='selectComplete" + rownumber + i + "' detail='" + rownumber + i + "' onchange='changeComplete(" + rownumber + i + ")'>" + s.html() + "</select>";
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
    let bsktnumb = $("#basketnumb").val();
    if (bsktnumb === "") {
        loginfo("?????????????? ?????????? ??????????????", 3);
        return;
    }

    //json ???????????? ?? ???????? ??????????????
    let arr = getObj();
    let typearr = [];
    typearr.push(type);
    let jsonObj = {
        "type": typearr,
        "sensors": arr,
        "number": $("#basketnumb").val()
    }
    let json = JSON.stringify(jsonObj);
    let xhttp = new XMLHttpRequest();

    $("#loading").show();
    xhttp.onreadystatechange = function () {
        if (this.readyState == 4) {
            if (this.status == 200) {
                $("#loading").hide();
                let disposition = this.getResponseHeader('content-disposition');
                let filename = "";

                if (disposition && disposition.indexOf('attachment') !== -1) {
                    var filenameRegex = /filename[^;=]*=((['"]).*?2|[^;]*)/;
                    var matches = filenameRegex.exec(disposition);
                    if (matches != null && matches[1]) {
                        filename = matches[1].replace(/['"]/g, '');
                        filename = decodeURI(filename);
                    }
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
            } else {
                $("#loading").hide();
                loginfo("???????????? ?????? ????????????????. ?????? ????????????: " + this.status);
            }
        }
    };
    xhttp.open("POST", "/getfile", true);
    xhttp.setRequestHeader("Content-Type", "application/json");
    xhttp.responseType = "blob";
    xhttp.send(json);
}

function removeSensor(id) {
    let mlfbrus = $("#boxmlfbrus" + id).text();
    let json = JSON.stringify(mlfbrus);

    let xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function () {
        if (this.readyState == 4 && this.status == 200) {
            location.reload();
        }
    };
    xhttp.open("POST", "/removefrombasket", true);
    xhttp.setRequestHeader("Content-Type", "application/json");
    xhttp.send(json);
}

function openSendEmail() {
    let bsktnumb = $("#basketnumb").val();
    if (bsktnumb === "") {
        loginfo("?????????????? ?????????? ??????????????", 3);
        return;
    }
    $("#openSendEmail").modal('show')
}

function sendEmail() {
    //json ???????????? ?? ???????? ??????????????
    let sendto = $("#sendto").val();
    let arrType = [];
    if (sendto) {
        if (validateEmail(sendto)) {
            for (let i = 1; i <= 5; i++) {
                if ($("#checkboxExport" + i).is(':checked')) {
                    arrType.push(i);
                }
            }
            if ($("#checkboxExport10").is(':checked')) {
                arrType.push(10);
            }
            if (arrType.length == 0) {
                loginfo("???? ?????????????? ?????????? ?????? ????????????????", 4);
            } else {
                $("#loading").show();
                let arr = getObj();
                let jsonObj = {
                    "type": arrType,
                    "sensors": arr,
                    "number": $("#basketnumb").val(),
                    "sendto": sendto,
                    "sendmsg": $("#emailmsg").val(),
                }
                var json = JSON.stringify(jsonObj);
                var xhttp = new XMLHttpRequest();

                xhttp.onreadystatechange = function () {
                    if (this.readyState == 4) {
                        $("#loading").hide();
                        if (this.status == 200) {
                            loginfo("?????????????????? ????????????????????.", 1);
                        } else {
                            loginfo("???????????? ?????? ????????????????. ?????? ????????????: " + this.status, 4);
                        }
                    }
                };
                xhttp.open("POST", "/sendmail", true);
                xhttp.setRequestHeader("Content-Type", "application/json");
                xhttp.responseType = "blob";
                xhttp.send(json);
                $("#openSendEmail").modal('hide');
            }
        } else {
            loginfo("???? ?????????????????? ???????????? email", 3);
        }
    } else {
        loginfo("???? ???????????????? ??????????????", 4);
    }
}

function validateEmail(email) {
    var re = /\S+@\S+\.\S+/;
    return re.test(email);
}

function downloadFile() {
    $("#openDownloadFile").modal('hide');
    $("#loading").show();
    let formData = new FormData(document.getElementById("downloadForm"));
    let xhr = new XMLHttpRequest();
    xhr.open('POST', '/downloadtobasket');
    xhr.onreadystatechange = function () {
        if (this.readyState == 4) {
            if (this.status == 200) {
                $("#loading").hide();
                fillFromObject(this.response);
            } else {
                $("#loading").hide();
                loginfo("???????????? ?????? ???????????????? ??????????. ?????? ????????????: " + this.status, 4);
            }
        }
    };
    xhr.send(formData);
}

function fillFromObject(json) {
    let arr = JSON.parse(json);
    let number = arr["number"];
    if (number) {
        $("#basketnumb").val(number)
    }
    let sensors = arr["sensors"];
    if (sensors) {
        $("#baskettable > tbody").empty();
        for (let i = 0; i < sensors.length; i++) {
            let j = i + 1;
            let obj = sensors[i];
            $("#baskettable > tbody").append(
                "<tr id='rowsensor" + j + "'>" +
                '<td><button class="btn btn-default" onclick="removeSensor('+j+')"><img src="/css/images/bi-trash.svg"></button></td>' +
                "<th scope='row' id='boxnumber"+j+"'>"+j+"</th>" +
                "<td id='boxmlfbrus"+j+"'>"+obj["mlfbrus"]+"</td>" +
                "<td style='display: none;' id='boxmlfb"+j+"'>"+obj["mlfb"]+"</td>"+
                "<td id='boxdescr"+j+"'>"+obj["descr"]+"</td>"+
                "<td id='boxprice"+j+"'>"+obj["price"]+"</td>" +
                "<td><input id='boxcount"+j+"' class='basketinput' value='"+obj["count"]+"'></td>" +
                "<td id='boxpricecount"+j+"'></td>"+
                "<td><input id='boxcoef"+j+"' class='basketinput' value='"+obj["coef"]+"'></td>" +
                "<td id='boxpricecoef"+j+"'></td>"+
                "<td id='boxpricetotal"+j+"'></td>"+
                "</tr>"
            );
            $("#baskettable > tbody").append("<tr id='row??ompletename" + j + "'>" +
                "<td></td>"+
                '<td><button type="button" class="btn btn-dark btn-sm" onclick="addComplete('+j+')">????????????????</button></td>'+
                "<td>?? ??????????????????:</td>"+
                "</tr>"
            );
            let completes = obj["complete"];
            for (let k = 0; k < completes.length; k++) {
                let complete = completes[k];
                console.log(complete);
                let l = k + 1;
                addComplete(j);
                let compName=complete["name"];
                $("#completeSelect option").each(function()
                {
                    // Add $(this).val() to your list
                    if ($(this).text().localeCompare(compName)==0) {
                        let sval = String($(this).val());
                        console.log("sval = "+sval);
                        $("#selectComplete"+j+l).selectpicker('val', sval);
                        changeComplete(j+""+l);
                        $("#completecoef" +j+l).val(complete["coef"]);
                        $("#completecount"+j+l).val(complete["count"]);
                    }
                });
            }
            $("#baskettable > tbody").append("<tr id='rowtotal" + j + "'>" +
                "<td id='boxtotalname"+j+"' colspan='8' class='text-right font-weight-bold'>???????????????? ?????????????????? ??????????????????</td>"+
                "<td id='boxtotalpricecoef"+j+"' class='font-weight-bold'></td>"+
                "<td id='boxtotalpricetotal"+j+"' class='font-weight-bold'></td>"+
                "</tr>"
            );
        }
        setFilter();
    }
}