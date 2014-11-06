<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/common/meta.jsp" %>
<%@ include file="/common/taglibs.jsp" %>

<form id="pagerForm" method="post" onsubmit="return navTabSearch(this);" action="catagory/search">
	<input type="hidden" name="name" value="${pagerForm.name}" />
	<input type="hidden" name="orderField" value="${pagerForm.orderField}" />
	<input type="hidden" name="orderDirection" value="${pagerForm.orderDirection}" />
	<input type="hidden" name="pageNum" value="${pagerForm.pageNum}" />
    <input type="hidden" name="numPerPage" value="${pagerForm.numPerPage}" />    
</form>

<div class="pageHeader">
	<form rel="pagerForm" method="post" onsubmit="return navTabSearch(this);" action="catagory/search" >
		<div class="searchBar">
			<label>分类名称：</label>
			<input type="text" name="name" value="<c:out value="${pagerForm.name}"/>"/>
			<button type="submit" name="searchBtn" id="searchId">查询</button>
		</div>
	</form>
</div>

<div class="pageContent">
	<div class="panelBar">
		<ul class="toolBar">
			<li><a class="add" href="catagory/add" target="dialog" mask="true" rel="addCatagory" width="530" height="150" ><span>新增分类</span></a></li>
		</ul>
	</div>
	<table class="table" width="100%" layoutH="110" nowrapTD="false" >
		<thead>
			<tr>
				<th width="20" orderField="id" <c:if test="${pagerForm.orderField == 'id'}"> class="${pagerForm.orderDirection}" </c:if> >ID</th>
				<th width="80">名称</th>
				<th width="80" >状态</th>
				<th width="80" >修改人</th>
				<th width="80" orderField="update_time" <c:if test="${pagerForm.orderField == 'update_time'}"> class="${pagerForm.orderDirection}" </c:if> >修改时间</th>
				<th width="100" >操作</th>
			</tr>
		</thead>
		<tbody>
 			<c:forEach var="item" items="${list}" >
				<tr>
					<td><c:out value="${item.id}" /></td>
					<td><c:out value="${item.name}" /></td>
					<td>
						<c:choose>
							<c:when test="${item.status == '0' }">未激活</c:when>
							<c:when test="${item.status == '1' }">激活</c:when>
						</c:choose>
					</td>				
					<c:choose>
						<c:when test="${item.updateUserName != '' && item.updateUserName != null}">
							<td><c:out value="${item.updateUserName}" /></td>
						</c:when>
						<c:otherwise>
							<td><c:out value="${item.createUserName}" /></td>
						</c:otherwise>
					</c:choose>
					<c:choose>
						<c:when test="${item.updateTime != '' && item.updateTime != null}">
							<td><fmt:formatDate value="${item.updateTime}" pattern='yyyy-MM-dd HH:mm:ss'/></td>
						</c:when>
						<c:otherwise>
							<td><fmt:formatDate value="${item.createTime}" pattern='yyyy-MM-dd HH:mm:ss'/></td>
						</c:otherwise>
					</c:choose>
					<td>
						<a class="btnEdit" href="catagory/update/${item.id}" target="dialog" mask="true" rel="updateUser" width="320" height="200" title="修改分类信息">修改</a>
						<c:if test="${item.isDeleted == '0'}">
							<a class="btnDel" href="catagory/remove/${item.id}" target="ajaxTodo" title="删除">删除</a>
						</c:if>
					</td>
				</tr> 			
			</c:forEach>
		</tbody>
	</table>
</div>

<div class="panelBar">
    <div class="pages">
        <span>显示</span> 
            <select class="select" name="numPerPage"
                onchange="navTabPageBreak({numPerPage:this.value})">
                <option value="20" <c:if test="${pagerForm.numPerPage == 20}"> selected </c:if> >20</option>
                <option value="50" <c:if test="${pagerForm.numPerPage == 50}"> selected </c:if> >50</option>
                <option value="100" <c:if test="${pagerForm.numPerPage == 100}"> selected </c:if> >100</option>
                <option value="500" <c:if test="${pagerForm.numPerPage == 500}"> selected </c:if> >500</option>
            </select> 
        <span>条，共 ${pagerForm.totalCount} 条，共 ${pagerForm.totalPageCount} 页</span>
    </div>

    <div class="pagination" targetType="navTab"
        totalCount="${pagerForm.totalCount}"
        numPerPage="${pagerForm.numPerPage}"
        pageNumShown="${pagerForm.pageNumShown}"
        currentPage="${pagerForm.pageNum}">
    </div>
</div>
