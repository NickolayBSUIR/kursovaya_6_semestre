$(document).ready(function () {
    google.charts.load('current', {
        packages: ['corechart', 'bar']
    });
    today = new Date();
    before = new Date(today.getTime() - 24 * 60 * 60000 * 29);
    today = today.getFullYear() + "-" + (today.getMonth() + 1) + "-" + today.getDate();
    before = before.getFullYear() + "-" + (before.getMonth() + 1) + "-" + before.getDate();
    url = 'https://data.messari.io/api/v1/assets/' + (new URLSearchParams(window.location.search)).get('curr') + '/metrics/price/time-series?start=' + before + '&end=' + today + '&interval=1d';
    getJSON(url, function (err, data) {
        if (err != null) {
            console.error(err);
        } else {
            google.charts.setOnLoadCallback(drawGraphic(data));
        }
    });
});

function drawGraphic(json) {
    let data = new google.visualization.DataTable();
    let values = json.data.values;
    let predict = [];
    let average = [];

    for (let i = 0; i < values.length - 2; i++) { // выведет 0, затем 1, затем 2
        average.push(values[i+1][1] / values[i][1]);
    }

    // for (let i = 0; i < (new Date()).getDate(); i++) {
    //     predict.push(average[i%28]);
    // }

    predict = average.concat(average.slice(0,(new Date()).getDate() % 28));
    predict = (predict.reduce((a, b) => a * b, 1) - 1) * 100;
    average = ((average.reduce((a, b) => a + b, 0) / average.length) - 1) * 100;

    data.addColumn('date', 'X');
    data.addColumn('number');

    data.addRows(values.map((el => [new Date(el[0]), el[1]])).slice(1,values.length));

    let options = {
        title: "График курса валюты за предыдущий месяц",
        hAxis: {
            title: 'День',
            format: 'MMM d'
        },
        vAxis: {
            title: 'Курс валюты (в $)',
            format: '###.## $'
        },
        colors: ['#a52714'],
        crosshair: {
            color: '#000'
        }
    };

    let chart = new google.visualization.LineChart(document.getElementById('chart_div'));

    document.getElementById('avg').innerHTML = average.toFixed(3) + " %";
    if (average >= 0) {
        document.getElementById('avg').color = "green";
    }
    else {
        document.getElementById('avg').color = "red";
    }

    document.getElementById('pred').innerHTML = predict.toFixed(3) + " %";
    if (average >= 0) {
        document.getElementById('pred').color = "green";
    }
    else {
        document.getElementById('pred').color = "red";
    }

    chart.draw(data, options);
};

function getJSON(url, callback) {

    let xmlhttprequest = new XMLHttpRequest();
    xmlhttprequest.open('GET', url, true);
    xmlhttprequest.responseType = 'json';

    xmlhttprequest.onload = function () {

        let status = xmlhttprequest.status;

        if (status == 200) {
            callback(null, xmlhttprequest.response);
        } else {
            callback(status, xmlhttprequest.response);
        }
    };

    xmlhttprequest.send();
}