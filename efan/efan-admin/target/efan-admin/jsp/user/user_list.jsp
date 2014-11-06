<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/common/meta.jsp" %>
<%@ include file="/common/taglibs.jsp" %>

<form id="pagerForm" method="post" onsubmit="return navTabSearch(this);" action="user/search">
	<input type="hidden" name="userListSearchIsDeleted" value="${pagerForm.userListSearchIsDeleted}"/>
	<input type="hidden" name="pageNum" value="${pagerForm.pageNum}" />
    <input type="hidden" name="numPerPage" value="${pagerForm.numPerPage}" />    
</form>

<div class="pageHeader">
	<form rel="pagerForm" method="post" onsubmit="return navTabSearch(this);" action="user/search" >
		<div class="searchBar">
			<label>账号状态：</label>
			<select class="select" name="userListSearchIsDeleted" style="width:133px">
				<option value="-1" >所有</option>
				<option value="1" <c:if test="${pagerForm.userListSearchIsDeleted == 1}"> selected </c:if> >禁用</option>
				<option value="0" <c:if test="${pagerForm.userListSearchIsDeleted == 0}"> selected </c:if> >激活</option>
			</select>
			<button type="submit" name="searchBtn" id="searchId">查询</button>
		</div>
	</form>
</div>

<div class="pageContent">
	<div class="panelBar">
		<ul class="toolBar">
			<li><a class="add" href="user/add" target="dialog" mask="true" rel="addUser" width="530" height="300" ><span>新增用户</span></a></li>
		</ul>
	</div>
	<table class="table" width="100%" layoutH="110" nowrapTD="false" >
		<thead>
			<tr>
				<th width="50" >ID</th>
				<th width="80" >账号</th>
				<th width="80" >真实姓名</th>
				<th width="80" >账号状态</th>
				<th width="80" >角色</th>
				<th width="150" >创建时间</th>
				<th width="150" >修改时间</th>
				<th width="100" >操作</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach var="item" items="${list}" >
			<tr>
				<td><c:out value="${item.id}" /></td>
				<td><c:out value="${item.loginName}" /></td>
				<td><c:out value="${item.displayName}" /></td>
				<td>
					<c:choose>
						<c:when test="${item.isDeleted == true }">禁用</c:when>
						<c:when test="${item.isDeleted == false }">激活</c:when>
						<c:otherwise><c:out value="${item.isDeleted}" /></c:otherwise>
					</c:choose>
				</td>
				<td>
					<c:choose>
						<c:when test="${item.role == 1 }">管理员</c:when>
						<c:when test="${item.role == 2 }">普通用户</c:when>
						<c:otherwise><c:out value="${item.role}" /></c:otherwise>
					</c:choose>
				</td>				
				<td><fmt:formatDate value="${item.createTime}" pattern='yyyy-MM-dd HH:mm:ss'/></td>
				<td><fmt:formatDate value="${item.updateTime}" pattern='yyyy-MM-dd HH:mm:ss'/></td>
				<td>
					<a class="btnEdit" href="user/update/${item.id}" target="dialog" mask="true" rel="updateUser" width="530" height="300" title="修改账号信息">修改</a>
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
