let form;

function makeEditable(datatableApi) {
    ctx.datatableApi = datatableApi;
    form = $('#detailsForm');
    $(".delete").click(function () {
        if (confirm('Are you sure?')) {
            deleteRow($(this).closest('tr').attr("id"));
        }
    });

    $('input:checkbox').click(function(){
        let chk = $(this).is(':checked');
        let id = $(this).closest('tr').attr("id");
        // $.post(ctx.ajaxUrl + $(this).closest('tr').attr("id"), $(this).is(':checked').serialize())

        $.ajax({
            type: "POST",
            url: ctx.ajaxUrl + "/enable",
            data: {"checkboxvalue":chk, "id":id}
        }).done(function () {
            // alert($(this).is(':checked').serialize());
            $("#editRow").modal("hide");
            updateTable();
            successNoty("Done");
        });

        if ($(this).is(':checked')) {
            $(this).parent().css('color', 'green');
        } else {
            $(this).parent().css('color', '#000000');
        }
    });

    $(document).ajaxError(function (event, jqXHR, options, jsExc) {
        failNoty(jqXHR);
    });

    // solve problem with cache in IE: https://stackoverflow.com/a/4303862/548473
    $.ajaxSetup({cache: false});
}

function add() {
    form.find(":input").val("");
    $("#editRow").modal();
}

function deleteRow(id) {
    $.ajax({
        url: ctx.ajaxUrl + id,
        type: "DELETE"
    }).done(function () {
        updateTable();
        successNoty("Deleted");
    });
}

function updateTable() {
    $.get(ctx.ajaxUrl, function (data) {
        ctx.datatableApi.clear().rows.add(data).draw();
    });
}

function save() {
    $.ajax({
        type: "POST",
        url: ctx.ajaxUrl,
        data: form.serialize()
    }).done(function () {
        $("#editRow").modal("hide");
        updateTable();
        successNoty("Saved");
    });
}

let failedNote;

function closeNoty() {
    if (failedNote) {
        failedNote.close();
        failedNote = undefined;
    }
}

function successNoty(text) {
    closeNoty();
    new Noty({
        text: "<span class='fa fa-lg fa-check'></span> &nbsp;" + text,
        type: 'success',
        layout: "bottomRight",
        timeout: 1000
    }).show();
}

function failNoty(jqXHR) {
    closeNoty();
    failedNote = new Noty({
        text: "<span class='fa fa-lg fa-exclamation-circle'></span> &nbsp;Error status: " + jqXHR.status,
        type: "error",
        layout: "bottomRight"
    });
    failedNote.show()
}