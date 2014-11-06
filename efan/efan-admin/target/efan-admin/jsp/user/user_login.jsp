<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/meta.jsp"%>
<%@ include file="/common/taglibs.jsp"%>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>Meedo-OTT应用市场管理后台</title>

<link href="themes/css/login.css" rel="stylesheet" type="text/css" />
<script src="js/jquery-1.7.2.min.js" type="text/javascript"></script>

</head>
<body>
	<p>Meedo-OTT应用市场管理后台</p>
	<div id="login">
		<div id="login_content">
			<div class="loginForm">
				<form action="login" method="post" >
					<p>
						<label>用户名：</label>
						<input type="text" name="login_username" size="20"/>
					</p>
					<p>
						<label>密码：</label>
						<input type="password" name="login_password" size="20"/>
					</p>
					<div class="login_bar">
						<input class="sub" type="submit" value=" " />
						<s:if test='errMsg != null'>
							<p><font color='red'>${errMsg}</font></p>
						</s:if>
					</div>
				</form>
			</div>
		</div>
	</div>
</body>
