<%--
  Created by IntelliJ IDEA.
  User: 程序员Artist
  Date: 16/3/24
  Time: 20:55
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>

<c:set var="root" value="${pageContext.request.contextPath}" />
<script src="${root}/public/js/jquery-1.12.2.min.js" ></script>
<html>
    <head>
        <title>dids</title>
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
        <a href="${root}/admin/showDid?traceName=${traceName}">刷新</a><br/><br/><br/><br/>
        <font color="red">did列表</font><br/><br/>

        <c:if test="${null != sorry}"><font color="red">${sorry}</font><br/><br/></c:if>

        <table class="stripe_tb" width="80%" border="0" cellpadding="0" cellspacing="0">
                <tr>
                    <th>Did</th>
                    <th>Did别名</th>
                    <th>查询操作</th>
                    <th>更新操作</th>
                </tr>
            <c:forEach items="${didToAlias}" var="did">
                <tr>
                    <td>${did.key}</td>
                    <td>${did.value}</td>
                    <td><a href="${root}/trace/tidsByDid?traceName=${traceName}&did=${did.key}" target="_blank">链路列表</a></td>
                    <td><a href="${root}/admin/removeDid?traceName=${traceName}&did=${did.key}">删除</a></td>
                </tr>
            </c:forEach>
        </table>

        <br/><br/><br/><br/>

        <font color="red">新增did</font><br/><br/>

        <form action="${root}/admin/addDid" method="post">
            链路名称:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;${traceName}<br/><br/>
            did:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
            <input type="text" name="did" value=""/><br/><br/>
            did的别名:&nbsp;&nbsp;&nbsp;<input type="text" name="didAlias" value=""/><br/><br/>
            <input type="hidden" name="traceName" value="${traceName}"/>
            <input type="submit" value="新增"/>
        </form>
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
