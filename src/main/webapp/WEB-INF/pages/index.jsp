<%--
  Created by IntelliJ IDEA.
  User: 程序员Artist
  Date: 16/3/24
  Time: 20:55
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<c:set var="root" value="${pageContext.request.contextPath}" />
<html>
  <head>
    <title>hello insecticide</title>
  </head>
  <body>
  <font color="red">hello, welcome to insecticide</font> <br/><br/>

    <a href="${root}/admin/index">config首页</a><br/><br/>
    <a href="${root}/trace/index">链路首页</a>
  </body>
</html>
