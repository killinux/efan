<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/common/meta.jsp"%>
<%@ include file="/common/taglibs.jsp"%>

<form id="pagerForm" method="post" onsubmit="return navTabSearch(this);" action="channel/search">
    <input type="hidden" name="orderField" value="${pagerForm.orderField}" />
    <input type="hidden" name="orderDirection" value="${pagerForm.orderDirection}" />
    <input type="hidden" name="pageNum" value="${pagerForm.pageNum}" />
    <input type="hidden" name="numPerPage" value="${pagerForm.numPerPage}" />    
</form>

<div class="pageHeader">
     <form rel="pagerForm" onsubmit="return navTabSearch(this);" action="channel/search" method="post">
        <div class="unit">
            <label>频道名称：</label>
            <input type="text" name="name" value="${searchName}"/>
            <button type="submit" name="searchBtn" id="searchId">查询</button>
        </div>
    </form> 
</div>

<div class="pageContent">
    <div class="panelBar">
        <ul class="toolBar">
            <li><a class="add" href="channel/add" target="dialog" mask="true" rel="addAuthDevice" height="300"><span>新增频道</span></a></li>
        </ul>
    </div>
    <table class="table" width="100%" layoutH="110">
        <thead>
            <th width="5%" orderField="id" <c:if test="${pagerForm.orderField == 'id'}"> class="${pagerForm.orderDirection}" </c:if> >id</th>
            <th width="20%" orderField="name" <c:if test="${pagerForm.orderField == 'name'}"> class="${pagerForm.orderDirection}" </c:if> >频道名称</th>
            <th width="20%" orderField="weight" <c:if test="${pagerForm.orderField == 'weight'}"> class="${pagerForm.orderDirection}" </c:if> >频道排序权重</th>
            <th width="10%">频道状态</th>
            <!-- <th width="10%">是否可用</th> -->
 			<!-- <th width="10%">频道创建用户</th> -->
 			<th width="10%">频道修改用户</th>
            <th width="10%" orderField="update_time" <c:if test="${pagerForm.orderField == 'update_time'}"> class="${pagerForm.orderDirection}" </c:if> >频道修改时间</th>
            <th width="10%">操作</th>
        </thead>
        <tbody>
            <c:forEach var="item" items="${list}" >
            <tr>
                <td><c:out value="${item.id}" /></td>
                <td><c:out value="${item.name}" /></td>
                <td><c:out value="${item.weight}" /></td>
                <td>
                <%-- <c:out value="${item.status}" /> --%>
                 	<c:choose>
                        <c:when test="${item.status == '0' }">未激活</c:when>
                        <c:when test="${item.status == '1' }">激活</c:when>
                        <c:otherwise><c:out value="${item.status}" /></c:otherwise>
                    </c:choose> 
                </td>
                 <%-- <td>
                   <c:choose>
                        <c:when test="${item.authStatus == 'S' }">鉴权成功</c:when>
                        <c:when test="${item.authStatus == 'F' }">鉴权失败</c:when>
                        <c:otherwise><c:out value="${item.authStatus}" /></c:otherwise>
                    </c:choose> 
                </td>--%>
                <%-- <td>
                   <c:choose>
                        <c:when test="${item.isDeleted == '0' }">可用</c:when>
                        <c:when test="${item.isDeleted == '1' }">禁用</c:when>
                        <c:otherwise><c:out value="${item.isDeleted}" /></c:otherwise>
                    </c:choose> 
                </td> --%>
               <%--  <td><c:out value="${item.createUserName}" /></td> --%>
                <td><c:out value="${item.updateUserName}" /></td>
                <td><fmt:formatDate value="${item.updateTime}" pattern='yyyy-MM-dd HH:mm:ss'/></td>
                <td>
                		<!-- <a href="channel/list" target="navTab" mask="true" rel="channelList" title="频道管理">频道管理</a> -->
                    <a class="btnEdit" href="channel/update/${item.id}" target="dialog" mask="true" rel="updateChannel" width="530" height="300" title="修改频道信息">修改</a>
                    <%-- <a class="btnEdit" href="channel/manager/${item.id}" target="dialog" mask="true" rel="managerChannel" width="600" height="500" title="管理数据">管理数据</a> --%>
                     <a class="btnAssign" href="channel/manager/${item.id}" target="navTab" mask="true" rel="managerChannel" width="600" height="500" title="管理“${item.name}”数据">管理数据</a>
                     
                    <c:if test="${item.isDeleted == '0'}">
                    <a class="btnDel" href="channel/remove/${item.id}" target="ajaxtodo" title="删除频道">删除频道</a>
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