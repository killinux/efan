<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/meta.jsp"%>
<%@ include file="/common/taglibs.jsp"%>

<div class="pageContent">
	<form method="post" action="user/update" class="pageForm required-validate"
			onsubmit="return validateCallback(this, dialogAjaxDone);">
		<div class="pageFormContent" layoutH="50">
			<div class="unit">
				<input type="hidden" name="id" value="${entity.id}"/>
				<label>用户名:</label>
				<input type="text" name="displayName" class="required" maxlength="64" value="<c:out value="${entity.displayName}" />"/>
			</div>
			<div class="unit">
				<label>登录名:</label>
				<input type="text" name="loginName" class="required" maxlength="64" value="<c:out value="${entity.loginName}" />"/>
			</div>
			<div class="unit">
				<label>邮箱:</label>
				<input type="text" name="email" class="required email" maxlength="128" value="<c:out value="${entity.email}" />"/>
			</div>
			<div class="unit">
				<label>账号权限:</label>
				<select name="role">
					<option value="1" <c:if test="${ entity.role == 1 }"> selected </c:if>  >管理员</option>
					<option value="2" <c:if test="${ entity.role == 2 }"> selected </c:if> >普通用户</option>
				</select>
			</div>				
			<div class="unit">
				<label>账号状态:</label>
				<select name="isDeleted">
					<option value="true" <c:if test="${ entity.isDeleted == true }"> selected </c:if>  >禁用</option>
					<option value="false" <c:if test="${ entity.isDeleted == false }"> selected </c:if> >激活</option>
				</select>
			</div>
		</div>
		<div class="panelBar">
			<button type="submit">确定</button>
		</div>
	</form>
</div>