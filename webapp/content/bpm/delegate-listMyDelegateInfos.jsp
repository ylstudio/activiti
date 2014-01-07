<%@page contentType="text/html;charset=UTF-8"%>
<%@include file="/taglibs.jsp"%>
<%pageContext.setAttribute("currentMenu", "delegate");%>
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
        <th width="10" class="m-table-check"><input type="checkbox" name="checkAll" onchange="toggleSelectedItems(this.checked)"></th>
        <th class="sorting" name="id">编号</th>
        <th class="sorting" name="key">委托人</th>
        <th class="sorting" name="name">被委托人</th>
        <th class="sorting" name="category">开始时间</th>
        <th class="sorting" name="version">结束时间</th>
        <th class="sorting" name="description">流程定义</th>
        <th class="sorting" name="suspended">状态</th>
        <th width="150">&nbsp;</th>
      </tr>
    </thead>

    <tbody>
      <s:iterator value="delegateInfos" var="item">
      <tr>
        <td><input type="checkbox" class="selectedItem" name="selectedItem" value="${item.id}"></td>
	    <td>${item.id}</td>
	    <td>${item.assignee}</td>
	    <td>${item.attorney}</td>
	    <td>${item.start_time}</td>
	    <td>${item.end_time}</td>
	    <td>${item.process_definition_id}</td>
	    <td>${item.status}</td>
	    <td><a href="delegate!removeDelegateInfo.do?id=${item.id}">删除</a></td>
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
