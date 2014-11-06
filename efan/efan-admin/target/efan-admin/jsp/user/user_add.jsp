<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/meta.jsp"%>
<%@ include file="/common/taglibs.jsp"%>

<script type="text/javascript">
	$(function() {
		$("#loginName").val('');
		$("#password").val('');
	});
</script>

<div class="pageContent">
	<form method="post" action="user/add" class="pageForm required-validate"
			onsubmit="return validateCallback(this, dialogAjaxDone);" >
		<div class="pageFormContent" layoutH="50">
			<div class="unit">
				<label>真实姓名:</label>
				<input type="text" name="displayName" class="required" maxlength="64"/>
			</div>
			<div class="unit">
				<label>账号:</label>
				<input type="text" name="loginName" class="required" maxlength="64" value="" autocomplete="off"/>
			</div>
			<div class="unit">
				<label>密码:</label>
				<input type="password" name="password" class="required" maxlength="15" value="" autocomplete="off"/>
			</div>
			<div class="unit">
				<label>账号权限:</label>
				<select name="role" class="required">
					<option value="1">管理员</option>
					<option value="2">普通用户</option>
				</select>
			</div>			
			<div class="unit">
				<label>邮箱:</label>
				<input type="text" name="email" class="required email" maxlength="128"/>
			</div>
		</div>
		<div class="panelBar">
			<button type="submit">确定</button>
		</div>
	</form>
</div>