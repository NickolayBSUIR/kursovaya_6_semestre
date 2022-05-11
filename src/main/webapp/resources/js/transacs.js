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
            { orderable: false }
        ]
    });
})