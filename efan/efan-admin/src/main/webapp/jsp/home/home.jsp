<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/common/meta.jsp"%>
<%@ include file="/common/taglibs.jsp"%>

<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>Meedo-OTT应用市场管理后台</title>

<link href="themes/default/style.css" rel="stylesheet"	type="text/css" media="screen" />
<link href="themes/css/core.css" rel="stylesheet" type="text/css"	media="screen" />
<link href="themes/css/print.css" rel="stylesheet" type="text/css"	media="print" />
<link href="uploadify/css/uploadify.css" rel="stylesheet" type="text/css" media="screen" />
<!--[if IE]>
<link href="themes/css/ieHack.css" rel="stylesheet" type="text/css" media="screen"/>
<![endif]-->

<!--  若需要navMenu生效，需要将该样式打开
<style type="text/css">
	#header{height:85px}
	#leftside, #container, #splitBar, #splitBarProxy{top:90px}
</style>
 -->

<!--[if lte IE 9]>
<script src="js/speedup.js" type="text/javascript"></script>
<![endif]-->
<script src="js/jquery-1.7.2.min.js" type="text/javascript"></script>
<script src="js/jquery.cookie.js" type="text/javascript"></script>
<script src="js/jquery.validate.js" type="text/javascript"></script>
<script src="js/jquery.bgiframe.js" type="text/javascript"></script>
<script src="xheditor/xheditor-1.2.1.min.js" type="text/javascript"></script>
<script src="xheditor/xheditor_lang/zh-cn.js" type="text/javascript"></script>
<script src="uploadify/scripts/jquery.uploadify.min.js" type="text/javascript"></script>

<script src="bin/dwz.min.js" type="text/javascript"></script>
<script src="js/dwz.regional.zh.js" type="text/javascript"></script>

<script type="text/javascript">
	$(function() {
		DWZ.init("themes/dwz.frag.xml", {
			loginUrl : "login_dialog",
			loginTitle : "登录", // 弹出登录对话框
			statusCode : {
				ok : 200,
				error : 300,
				timeout : 301
			}, 
			pageInfo : {
				pageNum : "pageNum",
				numPerPage : "numPerPage",
				orderField : "orderField",
				orderDirection : "orderDirection"
			}, 
			debug : false, // 调试模式 【true|false】
			callback : function() {
				initEnv();
				$("#themeList").theme({
					themeBase : "themes"
				});
			}
		});
	});
</script>
</head>

<body scroll="no">
	<div id="layout">
		<div id="header">
			<div class="headerNav">
				<a class="logo" href="#">标志</a>
				<ul class="nav">
					<li>
						&nbsp;&nbsp;&nbsp;&nbsp;<a href="javascript:void(0)">${displayName}，您好</a>&nbsp;&nbsp;&nbsp;&nbsp;
					</li>
					<li><a href="logout">退出</a></li>
				</ul>
				<ul class="themeList" id="themeList">
					<li theme="default"><div class="selected">蓝色</div></li>
					<li theme="green"><div>绿色</div></li>
					<li theme="purple"><div>紫色</div></li>
					<li theme="silver"><div>银色</div></li>
					<li theme="azure"><div>天蓝</div></li>
				</ul>
			</div>
		</div>

		<div id="leftside">
			<div id="sidebar_s">
				<div class="collapse">
					<div class="toggleCollapse">
						<div></div>
					</div>
				</div>
			</div>
			<div id="sidebar">
				<div class="toggleCollapse">
					<h2>主菜单</h2>
					<div>收缩</div>
				</div>
				<div class="accordion" fillSpace="sidebar">
					<div class="accordionHeader"><h2><span>Folder</span>运营管理</h2></div>
					<div class="accordionContent">
						<ul class="tree">
							<li><a href="channel/list" target="navTab" mask="true" rel="channelList" title="频道管理">频道管理</a></li>
							<li><a href="catagory/list" target="navTab" mask="true" rel="catagoryList" title="分类管理">分类管理</a></li>
							<li><a href="app/list" target="navTab" mask="true" rel="appList" title="应用管理">应用管理</a></li>
						</ul>
					</div>
					<c:if test="${auth == 1}">
						<div class="accordionHeader"><h2><span>Folder</span>权限管理</h2></div>
						<div class="accordionContent">
							<ul class="tree">
								<li><a href="user/list" target="navTab" rel="userList">账号管理</a></li>
							</ul>
						</div>
					</c:if>
				</div>
			</div>
		</div>

		<div id="container">
			<div id="navTab" class="tabsPage">
				<div class="tabsPageHeader">
					<div class="tabsPageHeaderContent">
						<!-- 显示左右控制时添加 class="tabsPageHeaderMargin" -->
						<ul class="navTab-tab">
							<li tabid="main" class="main"><a href="javascript:;"><span><span class="home_icon">我的主页</span></span></a></li>
						</ul>
					</div>
					<div class="tabsLeft">left</div><!-- 禁用只需要添加一个样式 class="tabsLeft tabsLeftDisabled" -->
					<div class="tabsRight">right</div><!-- 禁用只需要添加一个样式 class="tabsRight tabsRightDisabled" -->
					<div class="tabsMore">more</div>
				</div>
				<ul class="tabsMoreList">
					<li><a href="javascript:;">我的主页</a></li>
				</ul>
				<div class="navTab-panel tabsPageContent layoutBox">
					<div class="page unitBox">
						<div class="accountInfo">
							<div class="center">
								<p>
									<span style="color: red">meedo - OTT Launcher 应用市场管理后台</span>
								</p>
							</div>
						</div>
						<div class="pageFormContent" layoutH="80" style="margin-right: 230px">
							<p>
								<span>开发ing</span>
							</p>
							<div class="divider"></div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</body>
</html>
