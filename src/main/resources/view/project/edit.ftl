<!DOCTYPE html>
<html>

<head>

    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <meta http-equiv="x-ua-compatible" content="ie=edge">

    <title>${project.name}--编辑</title>

    <script type="text/javascript" src="/jquery/jquery-2.2.3.min.js"></script>


    <link rel="stylesheet" type="text/css" href="/semantic/semantic.min.css">
    <script src="/semantic/semantic.min.js"></script>
    <script src="/layer/layer.js"></script>



</head>

<body>

    <div class="ui container" style="top:0;">
        <div class="row ui grid" style="padding-top: 40px;" >
            <div class="sixteen wide column "  >

                <h2 class="ui header">${project.name}</h2>

                <p style="color: #888"> 项目路径 : ${project.basePath}</p>



            </div>
        </div>

        <div class="row ui grid" style="padding-top: 40px;">
            <div class="sixteen wide column ">
                <form id="fr" action="update">
                    <h4 class="ui header">项目路径</h4>
                    <div class="ui input" style="width: 40%">
                        <input placeholder="排除路径" name="basePath" value="${project.basePath!}">
                    </div>

                    <h4 class="ui header">排除路径</h4>
                    <div class="ui input" style="width: 40%">
                        <input placeholder="排除路径" name="excludePath" value="${project.excludePath!}">
                    </div>

                    <h4 class="ui header">上传路径</h4>
                    <div class="ui input" style="width: 40%">
                        <input placeholder="上传路径" name="serverUploadPath" value="${project.serverUploadPath!}">
                    </div>

                    <h4 class="ui header">服务器文件路径</h4>
                    <div class="ui input" style="width: 40%">
                        <input placeholder="服务器文件路径" name="remotePath" value="${project.remotePath!}">
                    </div>

                    <h4 class="ui header">服务器管理员用户名</h4>
                    <div class="ui input" style="width: 40%">
                        <input placeholder="服务器管理员用户名" name="tomcatUsername" value="${project.tomcatUsername!}">
                    </div>

                    <h4 class="ui header">服务器管理员密码</h4>
                    <div class="ui input" style="width: 40%">
                        <input placeholder="服务器管理员密码" name="tomcatPassword" value="${project.tomcatPassword!}">
                    </div>

                    <h4 class="ui header">服务器重启路径</h4>
                    <div class="ui input" style="width: 40%">
                        <input placeholder="服务器重启路径" name="reloadPath" value="${project.reloadPath!}">
                    </div>
                </form>

                <button class="ui primary button" style="margin-top: 50px;" onclick="submitForm()">
                    保存
                </button>
                <button class="ui yellow button" style="margin-top: 50px;" onclick="location.href='/project/${project.id}'">
                    返回
                </button>
            </div>
        </div>


    </div>

<script>
    function submitForm(){
        $("#fr").submit();
    }
</script>


</body>



</html>



