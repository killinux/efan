<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/common/meta.jsp" %>
<%@ include file="/common/taglibs.jsp" %>

<form id="pagerForm" method="post" onsubmit="return navTabSearch(this);" action="app/search">
	<input type="hidden" name="catagory" value="${pagerForm.catagory}" />
	<input type="hidden" name="channel" value="${pagerForm.channel}" />
	<input type="hidden" name="pubStatus" value="${pagerForm.pubStatus}" />
	<input type="hidden" name="name" value="${pagerForm.name}" />
	<input type="hidden" name="orderField" value="${pagerForm.orderField}" />
	<input type="hidden" name="orderDirection" value="${pagerForm.orderDirection}" />
	<input type="hidden" name="pageNum" value="${pagerForm.pageNum}" />
    <input type="hidden" name="numPerPage" value="${pagerForm.numPerPage}" />

</form>

<div class="pageHeader">
	<form rel="pagerForm" method="post" onsubmit="return navTabSearch(this);" action="app/search" >

			<div class="unit">
				<label>应用名称：</label>
				<input type="text" name="name" maxlength="128" value="<c:out value="${pagerForm.name}"/>"/>

				<label>应用分类：</label>
				<select class="select" name="catagory" style="width:133px">
					<option value="0">选择分类</option>
					<c:forEach var="item" items="${catagories}">
						<option value="${item.id}" <c:if test="${pagerForm.catagory == item.id}"> selected </c:if> ><c:out value="${item.name}"/></option>
					</c:forEach>
				</select>

				<label>应用频道：</label>
				<select class="select" name="channel" style="width:133px">
					<option value="0">选择分类</option>
					<c:forEach var="item" items="${channels}">
						<option value="${item.id}" <c:if test="${pagerForm.channel == item.id}"> selected </c:if> ><c:out value="${item.name}"/></option>
					</c:forEach>
				</select>

				<label>应用状态：</label>
				<select class="select" name="pubStatus" style="width:133px">
					<option value="" <c:if test="${pagerForm.pubStatus == ''}"> selected </c:if> >所有</option>
					<option value="W" <c:if test="${pagerForm.pubStatus == 'W'}"> selected </c:if> >处理中</option>
					<option value="T" <c:if test="${pagerForm.pubStatus == 'T'}"> selected </c:if> >待发布</option>
					<option value="P" <c:if test="${pagerForm.pubStatus == 'P'}"> selected </c:if> >已发布</option>
					<option value="N" <c:if test="${pagerForm.pubStatus == 'N'}"> selected </c:if> >已下架</option>
				</select>
				<button type="submit" name="searchBtn" id="searchId">查询</button>
			</div>

	</form>
</div>

<div class="pageContent">
	<div class="panelBar">
		<ul class="toolBar">
			<li><a class="add" href="app/add" target="navTab" mask="true" rel="addApp"><span>新增应用</span></a></li>
		</ul>
	</div>
	<table class="table" width="100%" layoutH="110" nowrapTD="false" >
		<thead>
			<tr>
				<th width="20" orderField="id" <c:if test="${pagerForm.orderField == 'id'}"> class="${pagerForm.orderDirection}" </c:if> >ID</th>
				<th width="80" >名称</th>
				<th width="50" >版本名称</th>
				<th width="40" orderField="pkg_size" <c:if test="${pagerForm.orderField == 'pkg_size'}"> class="${pagerForm.orderDirection}" </c:if> >包的大小</th>
				<th width="80" orderField="pub_time" <c:if test="${pagerForm.orderField == 'pub_time'}"> class="${pagerForm.orderDirection}" </c:if> >发布时间</th>
				<th width="40" >发布状态</th>
				<th width="80" >应用简介</th>
				<th width="50" >应用分类</th>
				<th width="50" >应用频道</th>
				<th width="50" >应用开发商</th>
				<th width="50" >修改人</th>
				<th width="80" >修改时间</th>
				<th width="150" >操作</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach var="item" items="${list}" >
				<tr>
					<td><c:out value="${item.id}"/></td>
					<td><c:out value="${item.name}" /></td>
					<td><c:out value="${item.verName}" /></td>
					<td><c:out value="${item.pkgSize}" />M</td>
					<td><fmt:formatDate value="${item.pubTime}" pattern='yyyy-MM-dd HH:mm:ss'/></td>
					<td>
						<c:choose>
							<c:when test="${item.pubStatus == 'W' }">处理中</c:when>
							<c:when test="${item.pubStatus == 'T' }">待发布</c:when>
							<c:when test="${item.pubStatus == 'P' }">已发布</c:when>
							<c:when test="${item.pubStatus == 'N' }">已下架</c:when>
						</c:choose>
					</td>
					<td><div style="text-overflow:true;" title="${item.brief}"><c:out value="${item.brief}"/></div></td>
					<td>
						<c:out value="${item.catagories}" />
					</td>
					<td>
						<c:out value="${item.channels}" />
					</td>
					<td>
						<c:out value="${item.developer}"/>
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
						<c:choose>
							<c:when test="${item.pubStatus == 'P' }">
								<a class="btnEdit" href="app/update/${item.id}" target="navtab" title="编辑">编辑</a>
								<a class="btnDel" href="app/unpublish/${item.id}" target="ajaxTodo" title="下架应用">下架</a>
							</c:when>
							<c:when test="${item.pubStatus == 'T' }">
								<a class="btnEdit" href="app/update/${item.id}" target="navtab" title="编辑">编辑</a>
								<a class="btnSelect" href="app/publish/${item.id}" target="ajaxTodo" title="发布应用">发布</a>
								<a class="btnDel" href="app/unpublish/${item.id}" target="ajaxTodo" title="下架应用">下架</a>
							</c:when>
						</c:choose>
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
