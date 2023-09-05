<%--
  Created by IntelliJ IDEA.
  User: 程序员Artist
  Date: 16/3/24
  Time: 20:55
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<c:set var="root" value="${pageContext.request.contextPath}" />
<script src="${root}/public/js/jquery-1.12.2.min.js" ></script>
<html>
  <head>
      <title>traceName2Did</title>
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
    <a href="${root}/admin/systemindex">回到首页</a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
    <a href="${root}/trace/index">刷新</a><br/><br/><br/><br/>
    <font color="red">链路名称-Did列表</font> <br/><br/>

    <c:forEach items="${tName2Did}" var="t2Ids">

        <table class="stripe_tb" width="50%" border="0" cellpadding="0" cellspacing="0">
            <tr>
                <th>链路名称=${t2Ids.key}</th>
            </tr>

            <c:forEach items="${t2Ids.value}" var="did">
            <tr>
                <td><a href="${root}/trace/tidsByDid?traceName=${t2Ids.key}&did=${did}" target="_blank">${did}</a></td>
            </tr>
            </c:forEach>

        </table>
        <br/><br/><br/>

    </c:forEach>

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
