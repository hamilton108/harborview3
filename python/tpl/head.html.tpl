<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">

<head th:fragment="head(title)">
    <meta charset="UTF-8">
    <title th:text="<%text>${title}</%text>">Title</title>

    <!-- Bootstrap -->
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.1.0/css/bootstrap.min.css"
        integrity="sha384-9gVQ4dYFwwWSjIDZnLEWnxCjeSWFphJiwGPXr1jddIhOegiu1FwO5qRGvFXOdJZ4" crossorigin="anonymous">
    <!-- Bootstrap 5
        <script src="https://cdn.jsdelivr.net/npm/bootstrap.native@3.0.15/dist/bootstrap-native.min.js"></script>
    -->

    <!-- Font Awesome -->
    <script src="https://kit.fontawesome.com/54e36e60f6.js" crossorigin="anonymous"></script>
    <!--

    <link rel="stylesheet" href="https://use.fontawesome.com/releases/v6.2.0/css/all.css"
        integrity="sha384-DNOHZ68U8hZfKXOrtjWvjxusGo9WQnrNx2sqG0tfsghAvtVlRW3tvkXWZh58N9jp" crossorigin="anonymous">
    <link rel="stylesheet"
        href="https://cdnjs.cloudflare.com/ajax/libs/awesome-bootstrap-checkbox/1.0.1/awesome-bootstrap-checkbox.min.css">
    -->


    <!-- Local stuff -->
    <!-- <link rel="stylesheet" href="/css/${psname}"> -->

    <link rel="stylesheet" href="/css/sortable.min.css">
</head>

<body>
    <div th:fragment="scripts">
        <script type="text/javascript"
            src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap.native/2.0.15/bootstrap-native-v4.min.js"></script>
        <!-- Bootstrap 5
            <script src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap.native/3.0.15/bootstrap-native.min.js"></script>
        -->
    </div>
</body>

</html>