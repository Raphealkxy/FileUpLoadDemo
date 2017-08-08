<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page import="java.util.LinkedList"%>
<%@page import="java.io.File"%>
<%@page import="java.net.URLEncoder"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
<h1>文件下载列表</h1>
<!-- 将D:\TTPmusic 中所有音乐文件，显示列表，允许用户下载 -->
<%
	// 遍历指定目录 --- 非递归广度
	File root = new File("D:\\TTPmusic");
    LinkedList<File> list = new LinkedList<File>();// 存储
    list.add(root);// 集合中存在一个目录
    
    while(!list.isEmpty()){
    	// 集合不为空
    	File currentDir = list.removeFirst();// 返回目录对象
    	File[] files = currentDir.listFiles();// 获得目录下所有文件
    	for(File f : files){
    		if(f.isDirectory()){
    			// 将未遍历目录 加入集合
    			list.add(f);
    		}else{
    			// 是一个文件
    			// get 、post 提交中文，使用URL编码
    			String args = URLEncoder.encode(f.getCanonicalPath(),"utf-8");// 这个本来是浏览器默认动作，只是手动执行
    			out.println("<a href='/day20/downloadMusic?path="+args+"'>"+f.getName()+"</a><br/>");
    		}
    	}
    }
%>
</body>
</html>