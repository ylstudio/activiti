<%@ page language="java" pageEncoding="UTF-8" %>
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<%@page import="org.springframework.context.ApplicationContext"%>
<%@page import="org.springframework.jdbc.core.JdbcTemplate"%>
<%@page import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<%
    ApplicationContext ctx = null;
    ctx = WebApplicationContextUtils.getWebApplicationContext(application);
	JdbcTemplate jdbcTemplate = (JdbcTemplate) ctx.getBean("jdbcTemplate");
	List<Map<String, Object>> users = jdbcTemplate.queryForList("select * from ACT_ID_USER");
%>
    <s:actionmessage id="m-success-message" cssStyle="display:none;"/>

    <!-- start of header bar -->
    <div class="navbar navbar-inverse">
      <div class="navbar-inner">
        <div class="container">
          <a data-target=".navbar-responsive-collapse" data-toggle="collapse" class="btn btn-navbar">
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
          </a>
          <a href="${scopePrefix}/" class="brand">Mossle</a>
          <div class="nav-collapse collapse navbar-responsive-collapse">
            <ul class="nav">
              <li class="divider-vertical"></li>
              <li class="${empty HEADER_MODEL or HEADER_MODEL eq 'home' ? 'active' : ''}">
			    <a href="${scopePrefix}/bpm/workspace!listProcessDefinitions.do">首页</a>
			  </li>
              <li class="${HEADER_MODEL eq 'bpm-admin' ? 'active' : ''}">
			    <a href="${scopePrefix}/bpm/console!listProcessDefinitions.do">系统管理</a>
			  </li>
              <li class="divider-vertical"></li>
            </ul>
            <ul class="nav pull-right">
              <li class="dropdown">
                <a data-toggle="dropdown" class="dropdown-toggle" href="#">
                  <%=com.mossle.core.util.SpringSecurityUtils.username%>
                  <b class="caret"></b>
                </a>
                <ul class="dropdown-menu">
<%
	for (Map<String, Object> item : users) {
		pageContext.setAttribute("item", item);
%>
                  <li><a href="${scopePrefix}/index.jsp?username=${item.id_}">切换用户${item.id_}</a></li>
<%
	}
	pageContext.removeAttribute("item");
%>
                </ul>
              </li>
            </ul>
          </div><!-- /.nav-collapse -->
        </div>
      </div><!-- /navbar-inner -->
    </div>
    <!-- end of header bar -->
