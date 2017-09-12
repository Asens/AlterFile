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
</style>
[#list list as file]
<div class="newList" >
    <span>${file.fileName}</span>

    <i class="ui icon download"></i>

    <i class="ui icon refresh"></i>

    <i class="ui icon send"></i>
</div>
[/#list]