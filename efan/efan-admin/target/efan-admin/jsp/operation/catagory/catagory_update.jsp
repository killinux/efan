<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/meta.jsp"%>
<%@ include file="/common/taglibs.jsp"%>

<div class="pageContent">
	<form method="post" action="catagory/update" class="pageForm required-validate"
			onsubmit="return validateCallback(this, dialogAjaxDone);" >
		<div class="pageFormContent" layoutH="50">
			<div class="unit">
				<label>分类名称:</label>
				<input type="hidden" name="id" value="${entity.id}"/>
				<label><c:out value="${entity.name}"/></label>
			</div>
			<div class="unit">
				<label>状态:</label>
				<select name="status">
					<option value="1" <c:if test="${ entity.status == '1' }"> selected </c:if> >激活</option>
					<option value="0" <c:if test="${ entity.status == '0' }"> selected </c:if>  >未激活</option>
				</select>	
			</div>			
		</div>
		<div class="panelBar">
			<button type="submit">确定</button>
		</div>
	</form> 
</div>