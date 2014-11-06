<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/common/meta.jsp"%>
<%@ include file="/common/taglibs.jsp"%>

<form id="pagerForm" method="post" onsubmit="return navTabSearch(this);" action="channel/manager/${channel_id}">
	<input type="hidden" name="orderField" value="${pagerForm.orderField}" />
    <input type="hidden" name="orderDirection" value="${pagerForm.orderDirection}" />
    <input type="hidden" name="pageNum" value="${pagerForm.pageNum}" />
    <input type="hidden" name="numPerPage" value="${pagerForm.numPerPage}" />    
</form>

<div class="pageHeader">
用户ID：${channel_id}  -------- 用户名称: ${channel_name}
</div>

<div class="pageContent">
    <div class="panelBar">
        <ul class="toolBar">
            <li><a class="add" href="channel/listallapp/${channel_id}" target="dialog" mask="true" rel="listAllAppForChannel" width="800" height="600"><span>添加商品</span></a></li>
        </ul>
    </div>
    <table class="table" width="100%" layoutH="110">
        <thead>
            <th width="5%" >序号</th>
            <th width="20%" orderField="id" <c:if test="${pagerForm.orderField == 'id'}"> class="${pagerForm.orderDirection}" </c:if> >商品ID</th>
            <th width="20%">商品名称</th>
            <th width="10%">分类</th>
            <th width="10%" orderField="ott_channel_app_rel.app_weight" <c:if test="${pagerForm.orderField == 'ott_channel_app_rel.app_weight'}"> class="${pagerForm.orderDirection}" </c:if> >排序权重</th>
            <th width="10%">状态</th>
            <th width="10%">操作</th>
        </thead>
        <tbody>
            <c:forEach var="item" items="${list}" varStatus="status" >
            <tr>
            	<td><c:out value="${status.index+1}" /></td>
                <td><c:out value="${item.id}" /></td>
                <td><c:out value="${item.name}" /></td>
                <td><c:out value="${item.catagories}" /></td>
                <td>
                 	<c:out value="${item.weight}" />
                </td>
                <td>
                	<c:if test="${item.verCode != item.lastestVer}">不</c:if>是最新的商品
                </td>
                <td>
                    <a class="btnEdit" href="channel/managerUpdateAppWeight/${channel_id}/${item.id}/${item.weight}" target="dialog" mask="true" rel="managerAppWeight" width="530" height="300" title="编辑App在“${channel_name}”的权重">编辑权重</a>
                    <a class="btnDel" href="channel/removeAppFromChannel/${channel_id}/${item.id}" target="ajaxtodo" title="在用户中移除App">在用户中移除App</a>
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