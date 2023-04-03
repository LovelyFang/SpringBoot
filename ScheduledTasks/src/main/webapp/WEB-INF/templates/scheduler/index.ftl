<!DOCTYPE html>
<html lang="zh-CN">
  <head>
	<title>任务调度管理平台</title>
	<link rel="stylesheet" href="http://cdn.bootcss.com/bootstrap/3.2.0/css/bootstrap.min.css">
	<script src="http://cdn.bootcss.com/jquery/1.11.1/jquery.min.js"></script>
	<script src="http://cdn.bootcss.com/bootstrap/3.2.0/js/bootstrap.min.js"></script>
  </head>
  <body>
  <div class="panel panel-default">
	  <div class="panel-heading">
	  	${serverIp}:${port}
		<#if start>
			<span class="label label-success">启动</span>
		<#else>
			<span class="label label-warning">停止</span>
		</#if>
		<#if start>
	  		<button type="button" onclick="stopAll()" class="btn btn-default">停止所有</button>
	  	<#else>
	  		<button type="button" onclick="startAll()" class="btn btn-default">启动所有</button>
	  	</#if>
	  </div>
      <table class="table">
        <thead>
          <tr>
            <th>NO</th>
            <th>Group</th>
            <th>Name</th>
            <th>state</th>
            <th>cronExpression</th>
            <th>startTime</th>
            <th>endTime</th>
            <th>nextFireTime</th>
            <th>previousFireTime</th>
            <th>操作</th>
          </tr>
        </thead>
        <tbody>
        <#list triggers as item>
          <tr>
            <td>${item_index}</td>
            <td>${item.triggerGroup!""}</td>
            <td>${item.triggerName!""}</td>
            <td>
            <#if item.state=='NORMAL'>
            <span class="label label-success"> ${item.stateName!""}</span>
            <#else>
            <span class="label label-warning">${item.stateName!""}</span>
            </#if>
            </td>
            
            <td>${item.cronExpression!""}</td>
            <td><#if item.startTime??>${item.startTime?string("yyyy-MM-dd HH:mm:ss")}</#if></td>
            <td><#if item.endTime??>${item.endTime?string("yyyy-MM-dd HH:mm:ss")}</#if></td>
            <td><#if item.nextFireTime??>${item.nextFireTime?string("yyyy-MM-dd HH:mm:ss")}</#if></td>
            <td><#if item.previousFireTime??>${item.previousFireTime?string("yyyy-MM-dd HH:mm:ss")}</#if></td>
            <td>
            	<div class="btn-group btn-group-xs">
        	<#if start&&item.state=='NORMAL'>
		        <button type="button" onclick="pause('${item.triggerName!""}','${item.triggerGroup!""}')" class="btn btn-default">暂停</button>
	        <#elseif start&&item.state=='PAUSED'>
		        <button type="button" onclick="resume('${item.triggerName!""}','${item.triggerGroup!""}')" class="btn btn-default">启动</button>
	        </#if>
	        <#if start&&item.state!='BLOCKED'>
		        <button type="button" onclick="run('${item.triggerName!""}','${item.triggerGroup!""}')" class="btn btn-default">立即执行</button>
	        </#if>
		      </div>
		      </td>
          </tr>
        </#list>  
        </tbody>
      </table>
	</div>
	<script type="text/javascript">
		function startAll(){
			if(!confirm("确定启动所有?")){
				return ;
			}
			$.ajax({
				url : 'start',
				dataType : "json",
				type : "post",
				async : false,
				success : function(data) {
					if (data.status==true) {
						alert("启动成功");
						window.location.href=window.location.href;
					} else {
						alert("启动失败"+data.message);
					}
				}
			});
		}
		function stopAll(){
			if(!confirm("确定停止所有?")){
				return ;
			}
			$.ajax({
				url : 'stop',
				dataType : "json",
				type : "post",
				async : false,
				success : function(data) {
					if (data.status==true) {
						alert("停止成功");
						window.location.href=window.location.href;
					} else {
						alert("停止失败"+data.message);
					}
				}
			});
		}
		function resume(triggerName,group){
			$.ajax({
				url : 'resume',
				data : {
					triggerName : triggerName,
					group:group
				},
				dataType : "json",
				type : "post",
				async : false,
				success : function(data) {
					if (data.status==true) {
						alert("启动成功");
						window.location.href=window.location.href;
					} else {
						alert("启动失败"+data.message);
					}
				}
			});
		}
		function pause(triggerName,group){
			$.ajax({
				url : 'pause',
				data : {
					triggerName : triggerName,
					group:group
				},
				dataType : "json",
				type : "post",
				async : false,
				success : function(data) {
					if (data.status==true) {
						alert("停止成功");
						window.location.href=window.location.href;
					} else {
						alert("停止失败"+data.message);
					}
				}
			});
		}
		function run(triggerName,group){
			$.ajax({
				url : 'run',
				data : {
					triggerName : triggerName,
					group:group
				},
				dataType : "json",
				type : "post",
				async : false,
				success : function(data) {
					if (data.status==true) {
						alert("执行成功");
						window.location.href=window.location.href;
					} else {
						alert("执行失败"+data.message);
					}
				}
			});
		}
	</script>
	
  </body>
 </html>
