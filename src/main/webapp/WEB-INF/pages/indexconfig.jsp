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
<script src="${root}/public/js/jquery-1.12.2.min.js" ></script>
<html>
  <head>
      <title>configList</title>
      <style type="text/css">
          th {
              background-color: #f58220;
              color: #fff;
              line-height: 20px;
              height: 30px;
          }
          td {
              padding: 6px 11px;
              border-bottom: 1px solid #95bce2;
              vertical-align: top;
              text-align: center;
          }
          td * {
              padding: 6px 11px;
          }

          tr.alt td {
              background: #ecf6fc;
          }

          tr.over td {
              background: #bcd4ec;
          }

      </style>
  </head>
  <body>
    <a href="${root}/admin/systemindex">回到首页</a><br/><br/><br/><br/>
    <font color="red">config配置列表</font><br/><br/>

    <c:if test="${null != sorry}"><font color="red">${sorry}</font><br/><br/></c:if>

    <table class="stripe_tb" width="50%" border="0" cellpadding="0" cellspacing="0">
        <tr>
            <th>链路名称</th>
            <th>上报数上限</th>
            <th>是否关闭上报</th>
            <th>查询操作</th>
            <th>更新操作</th>
        </tr>

        <c:forEach items="${traceConfigs}" var="config">
        <tr>
            <td>${config.traceName}</td>
            <td>${config.reportNum}</td>
            <td>${config.stop}</td>
            <td><a href="${root}/admin/showDid?traceName=${config.traceName}">did列表</a></td>
            <td><a href="${root}/admin/preUConfig?traceName=${config.traceName}">修改</a></td>
        </tr>

      </c:forEach>
    </table>


    <c:if test="${null != selfConfig}">
        <br/><br/><br/><font color="red">修改config</font><br/><br/>

        <form action="${root}/admin/uConfig" method="post">
            链路名称:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="text" name="traceName" value="${selfConfig.traceName}" readonly="true"/><br/><br/>
            上报数上限:&nbsp;&nbsp;&nbsp;&nbsp;<input type="text" name="reportNum" value="${selfConfig.reportNum}"/><br/><br/>
            是否关闭上报:&nbsp;&nbsp;&nbsp;<input type="text" name="stop" value="${selfConfig.stop}"/><br/><br/>
            <input type="submit" value="提交"/>
        </form>
    </c:if>
  </body>

  <script type="text/javascript">
      //隔行变色
      $(document).ready(function(){

          $(".stripe_tb tr").mouseover(function(){ //如果鼠标移到class为stripe_tb的表格的tr上时，执行函数
              $(this).addClass("over");}).mouseout(function(){ //给这行添加class值为over，并且当鼠标一出该行时执行函数
              $(this).removeClass("over");}) //移除该行的class

          $(".stripe_tb tr:even").addClass("alt"); //给class为stripe_tb的表格的偶数行添加class值为alt

      });
  </script>

</html>
