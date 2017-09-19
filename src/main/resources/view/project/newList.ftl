
[#list list as file]
<div class="newList" >
    <span>${file.fileName}</span>

    <i class="ui icon download" onclick="location.href='/download/${file.id}'"></i>

    <i class="ui icon refresh" onclick="cancelFile('${file.id}')"></i>

    <i class="ui icon send" id="file_${file.id}" onclick="pushToServer('${file.id}')"></i>

    <p >${file.absolutePath!}</p>
</div>
[/#list]