$(document).ready(function () {
    $('.nav-tabs a').on('shown.bs.tab', function (e) {
        let hashArr = getHashArray();
        if (hashArr != null) {
            let curHash = e.target.hash.substring(1);
            if (!hashArr.includes(curHash)) {
                //меняем хэш в зависимости от клика
                let tabName = 'main';
                if (curHash == tabName) {
                    tabName = 'option';
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

    $('#addMlfbManually').on('show.bs.modal', function (event) {
        $("#modalMlfb").val(getFullMlfb(1));
    })

    let hashArr = getHashArray();
    if (hashArr != null) {
        if (hashArr.includes('option')) {
            $('#lioption').trigger('click');
        } else {
            let index = hashArr.indexOf('main');
            if (index !== -1) {

            } else {
                window.location.hash = 'main';
            }
        }
        hashArr.forEach(function (element, i, arr) {
            if (element.startsWith("spanGroup")) {
                setTimeout(function () {
                    $("#" + element).get(0).scrollIntoView();
                }, 1000);
                return;
            }
        });

    } else {
        window.location.hash = 'main';
    }
});

function getFullMlfb(isRus) {
    let mlfb = getSpanMlfbRus();
    if (!isRus) {
        mlfb = getSpanMlfb();
    }
    let spanmlfbb = getSpanMlfbB();
    let spanmlfbc = getSpanMlfbC();
    let text = mlfb + " " + spanmlfbb + " " + spanmlfbc;
    return text;
}

function modalApply() {
    let mlfb = $("#modalMlfb").val().trim();
    if (mlfb.startsWith("7MF")) {
        submitForm(mlfb, "", "", "", "8", "", 1);
    } else {
        let j = 9;
        let russtandart = $("#russtandart").text();
        if (russtandart.startsWith("КМ35М")) {
            j = 7;
        }
        let tempMlfb = mlfb.replace("-", "");
        russtandart = russtandart.replace("-", "");
        if (tempMlfb.substring(0, j) != russtandart.substring(0, j)) {
            loginfo("Номер заказа должно начинаться c " + $("#russtandart").text().substring(0, j + 1), 4);
        } else {
            submitForm(mlfb, "", "", "", "8", "", 1);
        }
    }
}

function getHashArray() {
    if (window.location.hash) {
        strhashes = window.location.href.split("#")[1];
        strhash = strhashes.split("&");
        return strhash;
    } else {
        return null;
    }
}

function getHrefForJump(group) {
    let tabName = 'main';
    if (!isNumeric(group)) {
        tabName = 'option';
    }
    let hrefaddr = tabName + "&spanGroup" + group;
    return "#" + hrefaddr;
}

function getSpanMlfb() {
    let mlfbText = "";
    for (let i = 1; i <= 16; i++) {
        mlfbText = mlfbText + $("#lm" + i).text().trim();
    }
    return mlfbText;
}

function getSpanMlfbRus() {
    var all = $(".rusmlfbclass").map(function () {
        return this.innerHTML;
    }).get();
    return all.join("");
}

function getSpanMlfbB() {
    let mlfbText = "";
    for (let i = 1; i <= 40; i++) {
        if ($("#lb" + i).text()) {
            if (mlfbText != "") {
                mlfbText = mlfbText + "+";
            }
            mlfbText = mlfbText + $("#lb" + i).text().trim();
        } else {
            break;
        }
    }
    return mlfbText;
}

function getSpanMlfbC() {
    let mlfbText = "";
    for (let i = 1; i <= 40; i++) {
        if ($("#lc" + i).text()) {
            if (mlfbText != "") {
                mlfbText = mlfbText + " ";
            }
            mlfbText = mlfbText + $("#lc" + i).text().trim();

        } else {
            break;
        }
    }
    return mlfbText;
}

function removeGroupOption(group, name) {
    let mlfb = getSpanMlfb();
    let spanmlfbb = getSpanMlfbB();
    let spanmlfbc = getSpanMlfbC();
    let spanmlfbсText = "";
    console.log("group = " + group);
    console.log("name = " + name);
    if (isNumeric(group)) {
        let groupBeg = group - 1;
        mlfb = mlfb.slice(0, groupBeg) + "." + mlfb.slice(group);
    } else {//опции
        if (spanmlfbb != "") {
            let arrspanmlfbb = spanmlfbb.split("+");
            var index = arrspanmlfbb.indexOf(name);
            if (index !== -1) {
                arrspanmlfbb.splice(index, 1);
            }
            spanmlfbb = arrspanmlfbb.join("+");
            let arrspanmlfbc = spanmlfbc.split("}");
            arrspanmlfbc = arrspanmlfbc.filter(function (item) {
                return item.indexOf("{" + name) !== 0;
            });
            spanmlfbc = arrspanmlfbc.filter(i => i != "")
                .map(i => i + '}').join("");
            name = "0";
        }
    }
    submitForm(mlfb, spanmlfbb, spanmlfbc, spanmlfbсText, group, name, 0);
}

function isNumeric(value) {
    return /^-?\d+$/.test(value);
}

function jumpNextGroup(option, group, isIncrease, element) {
    let isSubmit = "";
    let groupInt;
    let spanmlfbb = getSpanMlfbB();
    let spanmlfbс = getSpanMlfbC();
    let spanmlfbсText = "";
    let arrY = ["Y01", "Y02", "Y15", "Y16", "Y17", "Y21", "Y22", "Y23", "Y25", "Y26", "Y30", "Y31", "Y32", "Y38", "Y99"];
    console.log("option = " + option);
    console.log("group = " + group);
    console.log("isIncrease = " + isIncrease);
    if (isNumeric(group)) {
        groupInt = parseInt(group, 0);
        if (isIncrease == 1) {
            $("#lm" + group).text(option);
            groupInt = groupInt + 1;
        }
        if (groupInt > 16) {
            groupInt = 16;
        }
    } else {//опции

        if (option != 0) {
            console.log("*******************");
            console.log(element);
            console.log(element.type);
            console.log(element.checked);
            if (element !== null && element.type == 'checkbox' && element.checked == false) {
                removeGroupOption(group, option)
            } else {
                let index = arrY.indexOf(option);
                if (index !== -1) {
                    if (option == "Y15" || option == "Y16" || option == "Y17" || option == "Y25" || option == "Y32" || option == "Y99") {
                        spanmlfbсText = $("#" + option + "input").val();
                    } else if (option == "Y21" || option == "Y30") {
                        if ($("#" + option + "select option:selected").text()) {
                            spanmlfbсText = $("#" + option + "select option:selected").text();
                        } else if ($("#" + option + "input").val()) {
                            spanmlfbсText = $("#" + option + "input").val();
                        }
                    } else if (option == "Y01") {
                        let t1 = $("#" + option + "number1").val();
                        let t2 = $("#" + option + "number2").val();
                        if (t1 !== null && t2 !== null && t1 && t2 && $("#" + option + "select option:selected").text()) {
                            spanmlfbсText = t1 + " ... " + t2 + " " + $("#" + option + "select option:selected").text();
                        }
                    } else if (option == "Y02") {
                        let t1 = $("#" + option + "number1").val();
                        let t2 = $("#" + option + "number2").val();
                        if (t1 !== null && t2 !== null && t1 && t2 && $("#" + option + "select option:selected").text()) {
                            spanmlfbсText = t1 + " ... " + t2 + " " + $("#" + option + "select option:selected").text();
                        } else if ($("#" + option + "select option:selected").text()) {
                            spanmlfbсText = $("#" + option + "select option:selected").text();
                        }
                    } else if (option == "Y22" || option == "Y23") {
                        let t1 = $("#" + option + "number1").val();
                        let t2 = $("#" + option + "number2").val();

                        if (t1 !== null && t2 !== null && t1 && t2 && $("#" + option + "input").val()) {
                            spanmlfbсText = t1 + " ... " + t2 + " " + $("#" + option + "input").val();
                        } else if (t1 !== null && t2 !== null && t1 && t2 && $("#" + option + "select option:selected").text()) {
                            spanmlfbсText = t1 + " ... " + t2 + " " + $("#" + option + "select option:selected").text();
                        }
                    } else if (option == "Y26" || option == "Y31" || option == "Y38") {
                        if ($("#" + option + "select option:selected")) {
                            spanmlfbсText = $("#" + option + "select option:selected").text();
                        }
                    }
                    if (spanmlfbсText !== null && spanmlfbсText) {
                        spanmlfbсText = option + ": " + spanmlfbсText;
                    } else {
                        isSubmit = "Введите дополнительные данные для опции " + option;
                        $("input[name='radio" + option + "']").prop('checked', false);
                    }
                }
            }
        }
        groupInt = group;
    }
    if (isSubmit == "") {
        submitForm(getSpanMlfb(), spanmlfbb, spanmlfbс, spanmlfbсText, groupInt, option, 0);
    } else {
        loginfo(isSubmit, 4);
    }
}

function submitForm(mlfb, mlfbb, mlfbс, mlfbсText, group, option, isformat) {

    console.log("mlfb = [" + mlfb + "], mlfbb= [" + mlfbb + "], mlfbс= [" + mlfbс + "], mlfbсText= [" + mlfbсText + "], group= [" + group + "], option= [" + option + "] ")

    $("#mlfbForm").append('<input type="hidden" name="mlfb" value= "' + mlfb + '"/> ');
    $("#mlfbForm").append('<input type="hidden" name="mlfbB" value= "' + mlfbb + '"/> ');
    $("#mlfbForm").append('<input type="hidden" name="mlfbC" value= "' + mlfbс + '"/> ');
    $("#mlfbForm").append('<input type="hidden" name="mlfbCText" value= "' + mlfbсText + '"/> ');
    $("#mlfbForm").append('<input type="hidden" name="group" value= "' + group + '"/> ');
    $("#mlfbForm").append('<input type="hidden" name="option" value= "' + option + '"/> ');
    $("#mlfbForm").append('<input type="hidden" name="isformat" value= "' + isformat + '"/> ');
    $("#mlfbForm").attr('action', getHrefForJump(group));
    $("#mlfbForm").submit();
}

function addToBasket() {
    let mlfb = getFullMlfb(1);

    let jsonObj = {
        "mlfb": mlfb
    };
    let json = JSON.stringify(mlfb);


    let xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function () {
        if (this.readyState == 4 && this.status == 200) {
            loginfo("Заказ " + mlfb + " добавлен в корзину", 1);
        }
    };
    xhttp.open("POST", "/addtobasket", true);
    xhttp.setRequestHeader("Content-Type", "application/json");
    xhttp.send(json);

}