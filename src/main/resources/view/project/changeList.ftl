

[#list list as file]
<div class="changeList" >
    <span>${file.fileName}</span>

    <i class="ui icon download" onclick="location.href='/download/${file.id}'"></i>

    <i class="ui icon refresh"></i>

    <i class="ui icon send"></i>

    <p >${file.absolutePath!}</p>
</div>
[/#list]