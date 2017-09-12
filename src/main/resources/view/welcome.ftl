<html>
<script type="text/javascript" src="/jquery/jquery-2.2.3.min.js"></script>
<script type="text/javascript" src="/layer/layer.js"></script>

<title>Welcome</title>

<style>
    html,body{
        background-color: #f4f4f4;
        margin: 0;
    }

    .container{
        width:45%;
        margin: 0 auto;
        padding: 20px 20px 20px 20px;
    }

    .row{
        width: 100%;
        text-align: center;
        padding: 10px 0;
    }

    .row.left{
        text-align: left;
    }
</style>
<body>
<div class="container">
    <div class="row">
        <h2>Welcome to the Spring-Boot SEED</h2>
    </div>

    <div class="row left">
        <p>Hello, ${(user.name)!} your freeMarker config is
        [#if freeMarker??]<span style="color: green">right</span>
        [#else]<span style="color: red">wrong</span>
        [/#if], </p>
        <p>if there is wrong ,please check your Internet</p>

    </div>

    <div class="row left">
        <h3>FreeMarker</h3>
        <p>FreeMarker is enabled</p>
        <br>
        <h3>Log4j2</h3>
        <p>I use lo4j2 by default , it's easy to config</p>
        <br>
        <h3>Active profile</h3>
        <p>set spring.profiles.active to use witch properties ,one for develop and
            the other is for production</p>
        <br>
        <h3>Exception handler</h3>
        <p>use ErrorAdvice to enable catch global exceptions</p>
        <br>
        <h3>Dao</h3>
        <p>Hibernate,using spring-boot-starter-data-jpa </p>
        <p>use interface by default , and make Custom*Dao interface and *DaoImpl to extend the function not apply</p>
    </div>
    <script>layer.msg("Welcome");</script>
</div>
</body>
</html>