$(document).ready(function () {

    $('.nav-tabs a').on('shown.bs.tab', function (e) {
        window.location.hash = e.target.hash;
    });

    let hashArr = getHashArray();
    if (hashArr != null) {
        if (hashArr.includes('option')) {
            $('#lioption').trigger('click');
        }
    }
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

function getHrefForJump(group) {
    let hrefaddr = window.location.href;
    /*console.log("getHrefForJump");
    console.log(hrefaddr);
    let hashArr = getHashArray();
    if (hashArr != null){
        hrefaddr = window.location.href.split("#")[0];
        console.log(hashArr.length);
        if (hashArr.length>1) {
            hrefaddr = hrefaddr + "#" + hashArr[0] + "&";
        } else {
            hrefaddr = hrefaddr + "#";
        }
    } else {
        hrefaddr = hrefaddr + "#";
    }
    hrefaddr = hrefaddr +"spanGroup"+group;
    console.log(hrefaddr);*/
    return hrefaddr;
}

function getSpanMlfb() {
    let mlfbText = "";
    for (let i = 1; i <= 16; i++) {
        mlfbText = mlfbText + $("#lm" + i).text().trim();
    }
    return mlfbText;
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
            mlfbText = mlfbText + "{" + $("#lc" + i).text().trim() + "}";
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
    console.log(group);
    console.log(name);
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
            name = "1";
        }
    }


    console.log("spanmlfbb = "+spanmlfbb);

    console.log("------------");
    $("#mlfbForm").append('<input type="hidden" name="mlfb" value= "' + mlfb + '"/> ');
    $("#mlfbForm").append('<input type="hidden" name="mlfbB" value= "' + spanmlfbb + '"/> ');
    $("#mlfbForm").append('<input type="hidden" name="mlfbC" value= "' + spanmlfbc + '"/> ');
    $("#mlfbForm").append('<input type="hidden" name="mlfbCText" value= "' + spanmlfbсText + '"/> ');
    $("#mlfbForm").append('<input type="hidden" name="group" value= "' + group + '"/> ');
    $("#mlfbForm").append('<input type="hidden" name="option" value= "' + name + '"/> ');
    $("#mlfbForm").attr('action', getHrefForJump(group));
    $("#mlfbForm").submit();
}

function isNumeric(value) {
    return /^-?\d+$/.test(value);
}

function insertMlfbB(spanmlfbb, option) {
    if (spanmlfbb != "") {
        if (!spanmlfbb.split("+").includes(option)) {
            spanmlfbb = spanmlfbb + "+" + option;
        }
    } else {
        spanmlfbb = option;
    }
    return spanmlfbb;
}

function insertMlfbC(spanmlfbс, option, text) {
    spanmlfbс = spanmlfbс + " {" + option + ": " + text + "}";
    return spanmlfbс;
}

function jumpNextGroup(option, group, isIncrease) {
    let isSubmit = "";
    let groupInt;
    let spanmlfbb = getSpanMlfbB();
    let spanmlfbс = getSpanMlfbC();
    let spanmlfbсText = "";
    let arrY = ["Y01", "Y02", "Y21", "Y22", "Y15", "Y16"];
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

            let index = arrY.indexOf(option);
            if (index !== -1) {
                if (option == "Y15" || option == "Y16") {
                    spanmlfbсText = $("#" + option + "input").val();
                    if (spanmlfbсText) {
                        spanmlfbсText = option + ": " + spanmlfbсText;
                        //spanmlfbс = insertMlfbC(spanmlfbс, option, text);
                        //spanmlfbb = insertMlfbB(spanmlfbb, option);
                    } else {
                        isSubmit = "Введите дополнительные данные для опции " + option;
                        $("input[name='radio" + option + "']").prop('checked', false);
                    }
                } else if (option == "Y21") {
                    spanmlfbсText = option + ": " + $("#" + option + "select option:selected").text();
                    //spanmlfbс = insertMlfbC(spanmlfbс, option, text);
                    //spanmlfbb = insertMlfbB(spanmlfbb, option);
                } else if (option == "Y01" || option == "Y02" || option == "Y22") {
                    let t1 = $("#" + option + "number1").val();
                    let t2 = $("#" + option + "number2").val();
                    if (t1 & t2) {
                        spanmlfbсText = option + ": " + t1 + "..." + t2 + " " + $("#" + option + "select option:selected").text();
                        //spanmlfbс = insertMlfbC(spanmlfbс, option, t1 + "..." + t2 + " " + $("#" + option + "select option:selected").text());
                        //spanmlfbb = insertMlfbB(spanmlfbb, option);
                    } else {
                        isSubmit = "Введите дополнительные данные для опции " + option;
                        $("input[name='radio" + option + "']").prop('checked', false);
                    }
                }
            } else {
                //spanmlfbb = insertMlfbB(spanmlfbb, option);
            }
        }

        groupInt = group;
    }

    if (isSubmit == "") {
        $("#mlfbForm").append('<input type="hidden" name="mlfb" value= "' + getSpanMlfb() + '"/> ');
        $("#mlfbForm").append('<input type="hidden" name="mlfbB" value= "' + spanmlfbb + '"/> ');
        $("#mlfbForm").append('<input type="hidden" name="mlfbC" value= "' + spanmlfbс + '"/> ');
        $("#mlfbForm").append('<input type="hidden" name="mlfbCText" value= "' + spanmlfbсText + '"/> ');
        $("#mlfbForm").append('<input type="hidden" name="group" value= "' + groupInt + '"/> ');
        $("#mlfbForm").append('<input type="hidden" name="option" value= "' + option + '"/> ');
        $("#mlfbForm").attr('action', getHrefForJump(groupInt));
        $("#mlfbForm").submit();
    } else {
        loginfo(isSubmit, 4);
    }
}
