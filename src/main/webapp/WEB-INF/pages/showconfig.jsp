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
    <title>showconfig</title>
  </head>
  <body>
    <a href="${root}/admin/systemindex">回到首页</a><br/><br/>
    config list <br/><br/>

    <table>
        <tr>
          <td>${traceConfig.traceName}</td>
          <td>${traceConfig.reportNum}</td>
          <td>${traceConfig.stop}</td>
        </tr>
    </table>

  </body>
</html>
