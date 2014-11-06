<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/meta.jsp"%>
<%@ include file="/common/taglibs.jsp"%>

<div class="pageContent">
	<form method="post" action="channel/add" class="pageForm required-validate"
			onsubmit="return validateCallback(this, dialogAjaxDone);">
		<div class="pageFormContent" layoutH="50">
			<div class="unit">
				<label>频道名称:</label>
				<input type="text" name="name" class="required" maxlength="64"/>
				<input type="hidden" name="isDeleted" class="" maxlength="15" value="0"/>
			</div>
			<div class="unit">
				<label>频道权重:</label>
				<input type="text" name="weight" class="required" maxlength="15"/>
			</div>
			<div class="unit">
				<label>频道状态:</label>
				<select name="status">
					<option value="0">未激活</option>
					<option value="1" >激活</option>
				</select>
			</div>			
		</div>
		<div class="panelBar">
			<button type="submit">确定</button>
		</div>
	</form>
</div>