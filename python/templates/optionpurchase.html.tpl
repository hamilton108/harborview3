<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">

<head th:replace="~{head.html :: head('Option Purchases')}">
    <title>Option Purchases</title>
</head>

<body>
    <div th:replace="~{navbar.html :: navbar}"></div>
    <div class="logo"></div>
    <div id="ps-main"></div>
    <div th:replace="~{head.html :: scripts}"></div>
    <div th:replace="~{footer.html :: footer}"></div>
    <script type="text/javascript" src="/js/maunaloa/${psname}"></script>

    <!--     <script type="text/javascript" src="/js/maunaloa/elm-optionpurchases.js"></script>
    <script type="text/javascript">
        const node = document.getElementById('my-app');
        const app = Elm.Maunaloa.OptionPurchases.init({ node: node, flags: 1 });
    </script> -->
</body>

</html>