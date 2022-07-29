const mealsAjaxUrl = "profile/meals/";

// https://stackoverflow.com/a/5064235/548473
const ctx = {
    ajaxUrl: mealsAjaxUrl
};

// $(document).ready(function () {
$(function () {
    makeEditable(
        $("#mealstable").DataTable({
            "paging": false,
            "info": true,
            "columns": [
                {
                    "data": "dateTime"
                },
                {
                    "data": "description"
                },
                {
                    "data": "calories"
                },
                {
                    "defaultContent": "Edit",
                    "orderable": false
                },
                {
                    "defaultContent": "Delete",
                    "orderable": false
                }
            ],
            "order": [
                [
                    0,
                    "desc"
                ]
            ]
        })
    );
});

let filterform = $('#filter');

function clearFilter(){
    filterform.find(":input").val("");
}

function filterTable() {
    $.get(ctx.ajaxUrl + "filter", filterform.serialize()).done(function (data) {
        ctx.datatableApi.clear().rows.add(data).draw();
    });
}