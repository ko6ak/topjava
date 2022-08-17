const mealAjaxUrl = "profile/meals/";

let form1 = $('#filter')

// https://stackoverflow.com/a/5064235/548473
const ctx = {
    ajaxUrl: mealAjaxUrl,
    updateTable: function () {

        let temp = $('#startDate1').val();
        if (temp !== "") {
            temp = $('#startDate1').val().split(".")
            $('#startDate').val(temp[2] + "-" + temp[1] + "-" + temp[0])
        }

        temp = $('#endDate1').val()
        if (temp !== "") {
            temp = $('#endDate1').val().split(".")
            $('#endDate').val(temp[2] + "-" + temp[1] + "-" + temp[0])
        }

        $.ajax({
            type: "GET",
            url: mealAjaxUrl + "filter",
            data: form1.find(':not(input[name=startDate1], input[name=endDate1])').serialize()
        }).done(updateTableByData);
    }
}

function clearFilter() {
    $("#filter")[0].reset();
    $.get(mealAjaxUrl, updateTableByData);
}

$(function () {
    makeEditable(
        $("#datatable").DataTable({
            "ajax": {
                "url": mealAjaxUrl,
                "dataSrc": ""
            },
            "paging": false,
            "info": true,
            "columns": [
                {
                    "data": "dateTime",
                    "render": function (date, type, row) {
                        if (type === "display") {
                            return date.substring(0, 10) + " " + date.substring(11, 16);
                        }
                        return date;
                    }
                },
                {
                    "data": "description"
                },
                {
                    "data": "calories"
                },
                {
                    "orderable": false,
                    "defaultContent": "",
                    "render": renderEditBtn
                },
                {
                    "orderable": false,
                    "defaultContent": "",
                    "render": renderDeleteBtn
                }
            ],
            "order": [
                [
                    0,
                    "desc"
                ]
            ],
            "createdRow": function (row, data, dataIndex) {
                data.excess ? $(row).attr("data-meal-excess", true) : $(row).attr("data-meal-excess", false);
            }
        })
    );
});