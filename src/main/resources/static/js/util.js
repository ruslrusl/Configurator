function loginfo(text, type = 0) {
    if (type != 0) {
        //alert(text);
        console.log(text);
        if (type == 1)
            toastr.success(text, 'Успешно');
        else if (type == 2)
            toastr.info(text, 'Информация');
        else if (type == 3)
            toastr.warning(text, 'Предупреждение');
        else if (type == 4)
            toastr.error(text, 'Ошибка');
    } else {
        console.log(text);
    }
}