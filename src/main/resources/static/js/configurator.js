$(document).ready(function () {

    $('.nav-tabs a').on('shown.bs.tab', function (e) {
        let hashArr = getHashArray();
        if (hashArr != null) {
            let curHash = e.target.hash.substring(1);
            if (!hashArr.includes(curHash)) {
                //меняем хэш в зависимости от клика
                let tabName = 'main';
                if (curHash==tabName) {
                    tabName = 'option';
                }
                let index = hashArr.indexOf(tabName);
                if (index !== -1) {
                    hashArr[index] = curHash;
                }
                let hashStr = hashArr.join("&");
                window.location.hash = '#'+hashStr;
            }
        } else {
            window.location.hash = e.target.hash;
        }
    });
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
        hashArr.forEach(function(element, i, arr) {
            if(element.startsWith("spanGroup")) {
                setTimeout(function () {
                    $("#"+element).get(0).scrollIntoView();
                }, 1000);
                return;
            }
        });

    } else {
        window.location.hash = 'main';
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
    let tabName = 'main';
    if (!isNumeric(group)) {
        tabName = 'option';
    }
    let hrefaddr = tabName+"&spanGroup"+group;
    return "#"+hrefaddr;
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
            console.log($("#lc" + i).text());
            console.log($("#lc" + i).text().trim());
            //mlfbText = mlfbText + "{" + $("#lc" + i).text().trim() + "}";
            if (mlfbText!="") {
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
    console.log("group = "+group);
    console.log("name = "+name);
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

            console.log(spanmlfbc);
            let arrspanmlfbc = spanmlfbc.split("}");
            console.log(arrspanmlfbc);

            arrspanmlfbc = arrspanmlfbc.filter(function (item) {
                return item.indexOf("{"+name) !== 0;
            });

            console.log(arrspanmlfbc);
            //
            // var index = arrspanmlfbc.indexOf("{"+name);
            // console.log(index);
            // if (index !== -1) {
            //     arrspanmlfbc.splice(index, 1);
            // }
            spanmlfbc = arrspanmlfbc.filter(i => i!="")
                .map(i => i + '}').join("");
            console.log(spanmlfbc);
            name = "0";
        }
    }

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

            if (element!==null && element.type=='checkbox' && element.checked==false) {
                console.log("here checkbox element.checked==false");
                console.log("removeGroupOption");
                removeGroupOption(group, option)
            } else {
                let index = arrY.indexOf(option);
                console.log("here index = " + index);
                if (index !== -1) {
                    if (option == "Y15" || option == "Y16" || option == "Y17" || option == "Y25"|| option == "Y32" || option == "Y99") {
                        spanmlfbсText = $("#" + option + "input").val();
                    } else if (option == "Y21" || option == "Y30") {
                        if ($("#" + option + "select option:selected").text()) {
                            spanmlfbсText = $("#" + option + "select option:selected").text();
                        } else if ($("#" + option + "input").val()){
                            spanmlfbсText = $("#" + option + "input").val();
                        }
                    } else if (option == "Y01" ) {
                        let t1 = $("#" + option + "number1").val();
                        let t2 = $("#" + option + "number2").val();
                        if (t1!==null && t2!==null && t1 && t2 && $("#" + option + "select option:selected").text())  {
                            spanmlfbсText = t1 + " ... " + t2 + " " + $("#" + option + "select option:selected").text();
                        }
                    } else if (option == "Y02" ) {
                        let t1 = $("#" + option + "number1").val();
                        let t2 = $("#" + option + "number2").val();
                        if (t1!==null && t2!==null && t1 && t2 && $("#" + option + "select option:selected").text())  {
                            spanmlfbсText = t1 + " ... " + t2 + " " + $("#" + option + "select option:selected").text();
                        } else if ($("#" + option + "select option:selected").text()) {
                            spanmlfbсText = $("#" + option + "select option:selected").text();
                        }
                    } else if (option == "Y22" || option == "Y23") {
                        let t1 = $("#" + option + "number1").val();
                        let t2 = $("#" + option + "number2").val();

                        if (t1!==null && t2!==null && t1 && t2 && $("#" + option + "input").val())  {
                            spanmlfbсText = t1 + " ... " + t2 + " " + $("#" + option + "input").val();
                        } else if (t1!==null && t2!==null && t1 && t2 && $("#" + option + "select option:selected").text())  {
                            spanmlfbсText = t1 + " ... " + t2 + " " + $("#" + option + "select option:selected").text();
                        }
                    } else if (option == "Y26" || option == "Y31"|| option == "Y38") {
                        if ($("#" + option + "select option:selected")) {
                            spanmlfbсText = $("#" + option + "select option:selected").text();
                        }
                    }
                    console.log("spanmlfbсText = "+spanmlfbсText);
                    if (spanmlfbсText!==null && spanmlfbсText) {
                        spanmlfbсText = option + ": " + spanmlfbсText;
                    } else {
                        isSubmit = "Введите дополнительные данные для опции " + option;
                        $("input[name='radio" + option + "']").prop('checked', false);
                    }

                } else {
                    //spanmlfbb = insertMlfbB(spanmlfbb, option);
                }
            }
        }

        groupInt = group;
    }
    console.log("here!!!!!!!");
    console.log("groupInt = "+groupInt);
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
