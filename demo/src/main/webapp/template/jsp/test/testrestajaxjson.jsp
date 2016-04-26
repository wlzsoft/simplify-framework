<%@ page language="java" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
 <head>
  <title>测试列表数据1</title>
 </head>

 <body>
    <c:forEach items="${result}" var="test">
  		${test.fid} | ${test.name} | ${test.createTime }|${test.createName }<br>
 	</c:forEach>
 </body>
</html>