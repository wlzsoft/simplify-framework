<%@ page language="java" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
 <head>
  <title>测试列表数据</title>
 </head>

 <body>
    <c:forEach items="${testList}" var="test">
  		${test.id} | ${test.name} | ${test.createTime }<br>
 	</c:forEach>
 </body>
</html>