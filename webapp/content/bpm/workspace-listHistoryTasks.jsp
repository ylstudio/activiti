<%@page contentType="text/html;charset=UTF-8"%>
<%@include file="/taglibs.jsp"%>
<%pageContext.setAttribute("currentMenu", "bpm");%>
<!doctype html>
<html lang="en">

  <head>
    <%@include file="/common/meta.jsp"%>
    <title>流程列表</title>
    <%@include file="/common/s.jsp"%>
  </head>

  <body>
    <%@include file="/header.jsp"%>

    <div class="row-fluid">
	<%@include file="/menu-bpm.jsp"%>

	<!-- start of main -->
    <section id="m-main" class="span10" style="float:right">

      <article class="m-widget">
        <header class="header">
		  <h4 class="title">列表</h4>
		</header>
		<div class="content">

  <table id="demoGrid" class="m-table table table-hover">
    <thead>
      <tr>
        <th class="sorting" name="id">编号</th>
        <th class="sorting" name="name">名称</th>
        <th class="sorting" name="startTime">开始时间</th>
        <th class="sorting" name="endTime">结束时间</th>
        <th class="sorting" name="assignee">负责人</th>
        <th class="sorting" name="deleteReason">处理结果</th>
        <th width="170">&nbsp;</th>
      </tr>
    </thead>

    <tbody>
      <s:iterator value="historicTasks" var="item">
      <tr>
	    <td>${item.id}</td>
	    <td>${item.name}</td>
	    <td>${item.startTime}</td>
	    <td>${item.endTime}</td>
	    <td>${item.assignee}</td>
	    <td>${item.deleteReason}</td>
        <td>
          <a href="workspace!withdraw.do?taskId=${item.id}">撤销</a>
          <a href="workspace!viewHistory.do?processInstanceId=${item.processInstanceId}">历史</a>
        </td>
      </tr>
      </s:iterator>
    </tbody>
  </table>
        </div>
      </article>

    </section>
	<!-- end of main -->
	</div>

  </body>

</html>
