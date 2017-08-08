<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
<h1>使用链接方式实现资源下载</h1>
<a href="/day20/download/1.jpg">1.jpg</a><br/>
<a href="/day20/download/2.xls">2.xls</a><br/>
<a href="/day20/download/3.rar">3.rar</a><br/>
<a href="/day20/download/4.txt">4.txt</a><br/>
<h1>通过Servlet完成资源下载</h1>
<a href="/day20/downloadFile?file=1.jpg">1.jpg</a><br/>
<a href="/day20/downloadFile?file=2.xls">2.xls</a><br/>
<a href="/day20/downloadFile?file=3.rar">3.rar</a><br/>
<a href="/day20/downloadFile?file=4.txt">4.txt</a><br/>
</body>
</html>