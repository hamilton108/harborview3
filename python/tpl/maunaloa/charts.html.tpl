<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">

<head th:replace="~{head.html :: head('Maunaloa')}">
    <title>Maunaloa</title>
</head>

<body>
    <div th:replace="~{navbar.html :: navbar}"></div>
    <div class="logo"></div>

    <div class="bd-example" role="tabpanel" style="margin-bottom: 1rem">
        <ul class="nav nav-tabs" id="myTab" role="tablist" style="margin-bottom: 1rem">
            <li class="nav-item">
                <a class="nav-link active" id="chart1-tab" data-toggle="tab" data-height="true" href="#chart1"
                    role="tab" aria-controls="chart1" aria-expanded="true">Days</a>
            </li>
            <li class="nav-item">
                <a class="nav-link" id="chart2-tab" data-toggle="tab" data-height="true" href="#chart2" role="tab"
                    aria-controls="chart2" aria-expanded="false">Weeks</a>
            </li>
            <li class="nav-item">
                <a class="nav-link" id="chart3-tab" data-toggle="tab" data-height="true" href="#chart3" role="tab"
                    aria-controls="chart3" aria-expanded="false">Month</a>
            </li>
        </ul>
        <div class="tab-content">
            <div role="tabpanel" class="tab-pane active show" id="chart1" aria-labelledby="chart1-tab"
                aria-expanded="true">
                <div th:replace="~{maunaloa/menubar.html :: menubar(1)}"></div>
                <div th:replace="~{maunaloa/chart.html :: chart(1)}"></div>
            </div>
            <div role="tabpanel" class="tab-pane" id="chart2" aria-labelledby="chart2-tab" aria-expanded="false">
                <div th:replace="~{maunaloa/menubar.html :: menubar(2)}"></div>
                <div th:replace="~{maunaloa/chart.html :: chart(2)}"></div>
            </div>
            <div role="tabpanel" class="tab-pane" id="chart3" aria-labelledby="chart3-tab" aria-expanded="false">
                <div th:replace="~{maunaloa/menubar.html :: menubar(3)}"></div>
                <div th:replace="~{maunaloa/chart.html :: chart(3)}"></div>
            </div>
        </div>
    </div>
    <div th:replace="~{head.html :: scripts}"></div>
    <script type="text/javascript" src="/js/maunaloa/${psname}"></script>
    <link rel="stylesheet" href="/css/maunaloa/${cssname}">
    <script type="module" src="/js/maunaloa/charts.js"></script>
    <script type="module" src="/js/maunaloa/svg/draggable.js"></script>
</body>

</html>