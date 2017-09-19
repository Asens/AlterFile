<!DOCTYPE html>
<html>

<head>

    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <meta http-equiv="x-ua-compatible" content="ie=edge">

    <title>Asens</title>

    <script type="text/javascript" src="/jquery/jquery-2.2.3.min.js"></script>


    <link rel="stylesheet" type="text/css" href="/semantic/semantic.min.css">
    <script src="/semantic/semantic.min.js"></script>
    <script src="/layer/layer.js"></script>

    <style>
        .newList{
            color: #0a7700;
            width: 100%;
            padding: 5px;
            font-size: 16px
        }

        .newList i{
            margin-left: 10px;
            cursor: pointer;
            color: #2185D0;
        }

        .newList i:hover{
            color: #0d71bb;
        }

        .newList p{
            color: #888;
            padding: 10px 0;
        }

        .changeList{
            color: #2185D0;
            width: 100%;
            padding: 5px;
            font-size: 16px
        }

        .changeList i{
            margin-left: 10px;
            cursor: pointer;
            color: #2185D0;
        }

        .changeList i:hover{
            color: #0d71bb;
        }

        .changeList p{
            color: #888;
            padding: 10px 0;
        }
    </style>

</head>

<body>

    <div class="ui container" style="top:0;">
        <div class="row ui grid" style="padding-top: 40px;" >
            <div class="sixteen wide column "  >

                <h2 class="ui header">${project.name}</h2>

                <p style="color: #888"> 项目路径 : ${project.basePath}</p>

                [#if project.excludePath??]<p style="color: #888"> 排除路径 : ${project.excludePath}</p>[/#if]

                [#if project.serverUploadPath??]<p style="color: #888"> 上传路径 : ${project.serverUploadPath}</p>[/#if]

                [#if project.remotePath??]<p style="color: #888"> 服务器文件路径 : ${project.remotePath}</p>[/#if]

            [#if project.tomcatUsername??]<p style="color: #888"> 服务器管理员用户名 : ${project.tomcatUsername}</p>[/#if]

            [#if project.tomcatPassword??]<p style="color: #888"> 服务器管理员密码 : ${project.tomcatPassword}</p>[/#if]

            [#if project.reloadPath??]<p style="color: #888"> 服务器重启路径 : ${project.reloadPath}</p>[/#if]

                [#if project.initialized==0]
                <button class="ui primary button" onclick="initProject('${project.id}')">
                    初始化
                </button>
                [/#if]

            </div>
        </div>

        <div class="row ui grid" style="padding-top: 40px;">
            <div class="sixteen wide column ">
                <button class="ui primary button" onclick="refreshList()">
                    更新列表
                </button>
                <button class="ui red button" onclick="cancelAll()">
                    全部还原
                </button>
                <button class="ui teal button" onclick="location.href='/project/${project.id}/edit'">
                    配置
                </button>
                <button class="ui orange button" onclick="">
                    生成更新文档
                </button>
                <button class="ui yellow button" onclick="pushAll()">
                    全部推送
                </button>
                <button class="ui purple button" onclick="reloadTomcat()">
                    重启服务器
                </button>
                <h4 class="ui header">新增列表</h4>
                <div id="newList" style="margin-top: 30px;"></div>

                <h4 class="ui header">修改列表</h4>
                <div id="changeList" style="margin-top: 30px;"></div>
            </div>
        </div>


    </div>
    <input type="hidden" id="projectId" value="${project.id}">

<script>

    function pushToServer(id){
        var load;
        $.ajax({
            url:"/push/"+id,
            data:{
                id:id
            },
            beforeSend:function(){
                load=layer.load(2);
            },
            complete:function(){
                layer.close(load);
            },
            success:function(data){
                if(data==='success'){
                    layer.msg("推送成功");
                    $("#file_"+id).css("color","#00aaaa");
                }else{
                    layer.msg("推送失败");
                }
            }
        })
    }

    function reloadTomcat(){
        var load;
        var id=$("#projectId").val();
        $.ajax({
            url:"/project/"+id+"/reload",
            beforeSend:function(){
                load=layer.load(2);
            },complete:function(){
                layer.close(load);
            },
            success:function(data){
                if(data==="success"){
                    layer.msg("命令发送成功,服务器重启中");
                }else if(data==="fail"){
                    layer.msg("网络错误,服务器配置错误或服务器未启动")
                }else{
                    layer.msg("请先配置tomcat")
                }
            }
        })
    }

    function pushAll(){
        var id=$("#projectId").val();
        var load;
        $.ajax({
            url:"/pushAll/"+id,
            dataType:"json",
            beforeSend:function(){
                load=layer.load(2);
            },complete:function(){
                layer.close(load);
            },success:function(data){
                var arr=data.files;
                for(var i=0;i<arr.length;i++){
                    $("#file_"+arr[i]).css("color","#00aaaa");
                }
                if(data.total===data.sent){
                    layer.msg("全部推送成功");
                }else{
                    layer.msg("共"+data.total+"个文件,已传送"+data.sent+"个");
                }

            }
        })
    }

    function cancelFile(fileId){
        $.ajax({
            url:"/cancelFile/"+fileId,
            success:function(data){
                layer.msg("还原成功");
                refreshList();
            }
        })
    }

    function refreshList(){
        getChangeList();
        getNewList();
    }

    function cancelAll(){
        var id=$("#projectId").val();
        $.ajax({
            url:"/project/"+id+"/cancelAll",
            success:function(data){
                getChangeList();
                getNewList();
            }
        })
    }

    function initProject(id){
        var load;
        $.ajax({
            url:"/project/"+id+"/init",
            beforeSend:function(){
                load=layer.load(2);
            },
            complete:function(){
                layer.close(load);
            },
            success:function(data){
                if(data==="success"){
                    layer.alert("初始化成功")
                }else{
                    layer.alert("初始化失败")
                }
            }
        })
    }

    function getChangeList(){
        var load;
        var id=$("#projectId").val();
        $.ajax({
            url:"/project/"+id+"/changeList",
            beforeSend:function(){
                load=layer.load(2);
            },complete:function(){
                layer.close(load);
            },
            success:function(data){
                $("#changeList").html(data);
            }
        })
    }

    function getNewList(){
        var load;
        var id=$("#projectId").val();
        $.ajax({
            url:"/project/"+id+"/newList",
            beforeSend:function(){
                load=layer.load(2);
            },complete:function(){
                layer.close(load);
            },
            success:function(data){
                $("#newList").html(data);
            }
        })
    }

    $(document).ready(function(){
        refreshList();
//        setInterval(function(){
//            refreshList();
//        },30000);
    })
</script>
</body>



</html>



