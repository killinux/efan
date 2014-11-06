<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/meta.jsp"%>
<%@ include file="/common/taglibs.jsp"%>

<div class="pageContent">
	<form method="post" action="catagory/add" class="pageForm required-validate"
			onsubmit="return validateCallback(this, dialogAjaxDone);" autocomplete="off">
		<div class="pageFormContent" layoutH="50">
			<div class="unit">
				<label>分类名称:</label>
				<input type="text" name="name" size="25" class="required" maxlength="128"/>
			</div>
			<div class="unit">
				<label>用户状态:</label>
				<select name="status">
					<option value="0" selected>未激活</option>
					<option value="1">激活</option>
				</select>
			</div>				
		</div>
		<div class="panelBar">
			<button type="submit">确定</button>
		</div>
	</form> 
</div>
