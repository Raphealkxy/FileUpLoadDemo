<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<script type="text/javascript">
	function addAttach(){
		// 在div中添加 文件上传框
		var attachments = document.getElementById("attachments");
		attachments.innerHTML += "<div><input type='file' /><input type='button' value='删除' onclick='delAttach(this);' /></div>";
	}

	function delAttach(btn){
		// 传入参数 当前点击按钮
		//alert(btn.nodeName);

		// 先获得删除 div
		var wantDelDiv = btn.parentNode;

		// 用要删除div找到父亲，杀死
		wantDelDiv.parentNode.removeChild(wantDelDiv);
		
	}
</script>
</head>
<body>
<!-- JS 编写动态文件上传框 -->
<input type="button" value="添加附件"  onclick="addAttach();"/>
<div id="attachments"></div>
</body>
</html>