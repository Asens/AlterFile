<a href="/project/add">添加项目</a>
[#if list??]
<ul>
[#list list as project]
    <li><a href="/project/${project.id}">${project.name}</a>---${project.basePath}</li>
[/#list]
</ul>
[/#if]