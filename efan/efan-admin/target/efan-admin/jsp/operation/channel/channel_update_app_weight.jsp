<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/meta.jsp"%>
<%@ include file="/common/taglibs.jsp"%>

<div class="pageContent">
	<form method="post" action="channel/managerUpdateAppWeight" class="pageForm required-validate"
			onsubmit="return validateCallback(this, dialogAjaxDone);">
		<div class="pageFormContent" layoutH="50">
			<div class="unit">
				<label>应用名称:</label>
				<input type="hidden" name="id" value="${entity.id}"/>
				${entity.name}
				<input type="hidden" name="name" class="required" maxlength="64" value="<c:out value="${channel_id}" />"/>
				<!-- 这里为了传值，用了不是名字的字段传给名字，是有点奇怪，看代码的时候不要骂人 -->
			</div>
			<div class="unit">
				<label>排序权重:</label>
				<input type="text" name="weight" class="required" maxlength="15" value="<c:out value="${app_weight}" />"/>
			</div>
				
		</div>
		<div class="panelBar">
			<button type="submit">确定</button>
		</div>
	</form>
</div>