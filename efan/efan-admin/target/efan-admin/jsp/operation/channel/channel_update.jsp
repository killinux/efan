<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/meta.jsp"%>
<%@ include file="/common/taglibs.jsp"%>

<div class="pageContent">
	<form method="post" action="channel/update" class="pageForm required-validate"
			onsubmit="return validateCallback(this, dialogAjaxDone);">
		<div class="pageFormContent" layoutH="50">
			<div class="unit">
				<label>频道名称:</label>
				<input type="hidden" name="id" value="${entity.id}"/>
				${entity.name}
				<input type="hidden" name="name" class="required" maxlength="64" value="<c:out value="${entity.name}" />"/>
				<%-- <input type="text" name="name" class="required" maxlength="64" value="<c:out value="${entity.name}" />"/> --%>
			</div>
			<div class="unit">
				<label>频道权重:</label>
				<input type="text" name="weight" class="required" maxlength="15" value="<c:out value="${entity.weight}" />"/>
			</div>
			<div class="unit">
				<label>频道状态:</label>
				<select name="status">
					<!-- <option value="-1" selected>选择初始值</option> -->
					<option value="0" <c:if test="${entity.status == 0}"> selected </c:if> >未激活</option>
					<option value="1" <c:if test="${entity.status == 1}"> selected </c:if>>激活</option>
				</select>
				<%-- <input type="text" name="status" class="required" maxlength="30" value="<c:out value="${entity.status}" />"/> --%>
			</div>	
			 <%-- <div class="unit">
				<label>频道是否可用:</label>
				<select name="isDeleted">
					<option value="0" <c:if test="${ entity.isDeleted == '0' }"> selected </c:if> >可用</option>
					<option value="1" <c:if test="${ entity.isDeleted == '1' }"> selected </c:if> >禁用</option>
				</select>
			</div>	  --%>	
		</div>
		<div class="panelBar">
			<button type="submit">确定</button>
		</div>
	</form>
</div>