
[#list list as file]
<div class="newList" >
    <span>${file.fileName}</span>

    <i class="ui icon download" onclick="location.href='/download/${file.id}'"></i>

    <i class="ui icon refresh"></i>

    <i class="ui icon send" onclick="pushToServer('${file.id}')"></i>

    <p >${file.absolutePath!}</p>
</div>
[/#list]