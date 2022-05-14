$(() => {
    let tb = $("#tab").DataTable({
        language: {
            "emptyTable": "В таблице нет данных",
            "info": "Показано от _START_ до _END_ записей из _TOTAL_",
            "infoEmpty": "Нет записей",
            "infoFiltered": "(отфильтровано _MAX_ total записей)",
            "lengthMenu": "Показать _MENU_ строк",
            "loadingRecords": "Загрузка...",
            "processing": "Идёт обработка...",
            "search": "Поиск:",
            "zeroRecords": "Записи не найдены",
            "paginate": {
                "first": "Первый",
                "last": "Посл",
                "next": "След",
                "previous": "Пред"
            }
        },
        columnDefs: [
            { orderable: false, targets: ($("th").length - 1) }
        ]
    });
    
    $(document).on('click', '.trade', function(e) {
        var el = $(e.target);
        var del = $('button.close');
        if (del.val() !== undefined) {
            del.parent().parent().parent().children().eq(del.parent().parent().index()+1).remove();
            del.removeClass('close').addClass('trade');
            // del.text("Торговать!");
        }
        var text = '<td colspan="5"><form action="/transac" method="POST" id="post_form"><input type="hidden" name="crypto" value="' + el.val() + '" /><div class="input-group align-items-center"><input type="number" name="currencyCount" style="color: green;" class="form-control float-end text-end crypto" step="0.00001" min="0.00001" placeholder="' + el.val() + '" required><span class="input-group-text" id="basic-addon3"><-></span><input type="number" name="usdCount" style="color: red;" class="form-control money" step="0.00001" min="0.00001" placeholder="$" required></div><br><div class="text-center"><input type="radio" class="btn-check buy" name="buying" id="option2" autocomplete="off" value="true" checked><label class="btn btn-success" for="option2">Купить</label><a type="button" href="/prediction?curr=' + el.val() + '" target="_blank" class="btn btn-outline-primary">Прогноз</a><input type="radio" class="btn-check sell" name="buying" value="false" id="option1" autocomplete="off"><label class="btn btn-danger" for="option1">Продать</label><br><br><button type="button" class="btn btn-warning exec-modal">Совершить!</button></div></form></td>';
        $(text).insertAfter(el.parent().parent());
        el.removeClass('trade').addClass('close');
        // el.text("Закрыть");
    });

    $(document).on('click', '.close', function(e) {
        var el = $(e.target);
        el.parent().parent().parent().children().eq(el.parent().parent().index()+1).remove();
        el.removeClass('close').addClass('trade');
        // el.text("Торговать!");
    });

    $(document).on('click', '.buy', function(e) {
        var el = $(e.target).parent().parent().children('.input-group');
        el.children('.crypto').css('color','green');
        el.children('.money').css('color','red');
    });

    $(document).on('click', '.sell', function(e) {
        var el = $(e.target).parent().parent().children('.input-group');
        el.children('.crypto').css('color','red');
        el.children('.money').css('color','green');
    });

    $(document).on('change', '.crypto', function(e) {
        var el = $(e.target);
        var rate = parseFloat(el.parent().parent().parent().parent().children().eq(el.parent().parent().parent().index()-1).children().eq(1).text().replace(",", "."));
        var crypto = parseFloat(el.val());
        var money = crypto * rate;
        el.parent().children('.money').val(money.toFixed(5));
    });

    $(document).on('change', '.money', function(e) {
        var el = $(e.target);
        var rate = parseFloat(el.parent().parent().parent().parent().children().eq(el.parent().parent().parent().index()-1).children().eq(1).text().replace(",", "."));
        var money = el.val();
        var crypto = money / rate;
        el.parent().children('.crypto').val(crypto.toFixed(5));
    });

    $(document).on('click', '.exec-modal', function(e) {
        var text = "";
        var crypto = $("input[type='hidden']").val();
        if ($("input[name='usdCount']").val() == "" && $("input[name='currencyCount']").val() == "") {
            alert("Пожалуйста, введите значения.");
            return;
        }
        if ($("input[name='buying']:checked").val() === 'true') {
            text = '<div class="input-group align-items-center"><input type="text" style="color: green;" class="form-control float-end text-end" value="' + $("input[name='currencyCount']").val() + ' ' + crypto * 0.998 + '" disabled><span class="input-group-text" id="basic-addon3"><-></span><input type="text" style="color: red;" class="form-control" value="' + $("input[name='usdCount']").val() + ' $" disabled></div><br>Тип операции: <a style="color: green;">ПОКУПКА</a><br>За транзакцию в качестве оплаты были взяты <a style="color: green;">0.2%</a> криптовалюты.';
        }
        else {
            text = '<div class="input-group align-items-center"><input type="text" style="color: red;" class="form-control float-end text-end" value="' + $("input[name='currencyCount']").val() + ' ' + crypto + '" disabled><span class="input-group-text" id="basic-addon3"><-></span><input type="text" style="color: green;" class="form-control" value="' + ($("input[name='usdCount']").val() * 0.97 * 0.998).toFixed(5) + ' $" disabled></div><br>Тип операции: <a style="color: red;">ПРОДАЖА</a><br>Из получаемый суммы была отнята <a style="color: red;">3%</a> комиссии, также <a style="color: green;">0.2%</a> криптовалюты была оплачена транзакция.';
        }
        text += '<br><br><a class="float-end" style="color: white; text-decoration: inherit;">Подтвердить операцию?</a>';
        $(".modal-body").empty();
        $(".modal-body").append(text);
        $(".modal").modal({ backdrop: 'static', keyboard: false });
        $(".modal").modal("show"); 
    });
})