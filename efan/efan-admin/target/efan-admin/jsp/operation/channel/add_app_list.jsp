<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/common/meta.jsp"%>
<%@ include file="/common/taglibs.jsp"%>

<form id="pagerForm" method="post" onsubmit="return dialogSearch(this);" action="channel/searchapp/${channel_id}">  
    <input type="hidden" name="pageNum" value="${pagerForm.pageNum}" />
    <input type="hidden" name="numPerPage" value="${pagerForm.numPerPage}" />    
</form>

<div class="pageHeader">
	<form rel="pagerForm" onsubmit="return dialogSearch(this);"  action="channel/searchapp/${channel_id}" method="post"> 
        <div class="unit">
            <label>应用名：</label>
            <input type="text" name="name" value="${pagerForm.name}"/>
            <label>按分类筛选：</label>
            <select name="catagory" >
            	 <option value="0">--选择分类--</option>
	             <c:forEach var="item" items="${catagorylist}" varStatus="status" >
	            	<option value="${item.id}" <c:if test="${pagerForm.catagory == item.id}"> selected </c:if>>${item.name}</option>
	             </c:forEach>
            </select>
            <button type="submit" name="searchBtn" id="searchId">搜索</button>
        </div>
    </form> 
</div>

<div class="pageContent">
	<form method="post" action="channel/batchAddAppToChannel" class="pageForm required-validate" onsubmit="return validateCallback(this, dialogAjaxDone)">
    <div class="panelBar">
        <ul class="toolBar">
            <li><label style="float:left"><input id="checkall_app" type="checkbox" class="checkboxCtrl" group="app_add_id" />全选</label></li>
            <!-- <li><div class="button"><div class="buttonContent"><button type="button" class="checkboxCtrl" group="app_add_id" selectType="invert">反选</button></div></div></li> -->
            <li><div class="buttonActive"><div class="buttonContent"><button type="submit">添加</button></div></div></li>
            <input type="hidden" name ="channel_id"  value="${channel_id}"/>
            <!-- <li><a class="add" href="chan/add" target="dialog" mask="true" rel="addAuthDevice" height="300"><span>添加</span></a></li> -->
        </ul>
    </div>
    <table class="table" width="100%" layoutH="110">
        <thead>
        	<!-- <th width="5%">序号</th> -->
        	<th width="20%">选择</th>
            <th width="20%">ID</th>
            <th width="20%">应用名称</th>
            <th width="10%">分类</th>
        </thead>
        <tbody>
            <c:forEach var="item" items="${list}" varStatus="status" >
            <tr>
            	<%-- <td><c:out value="${status.index+1}" /></td> --%>
            	<td><input type="checkbox" name="app_add_id" value="${item.id}"  onclick='javascript:$("#checkall_app").attr("checked",false)'/></td>
                <td><c:out value="${item.id	}" /></td>
                <td><c:out value="${item.name}" /></td>
                <td> <c:out value="${item.catagories}" /> </td>
            </tr>
            </c:forEach>
        </tbody>
    </table>
</div>
</form>
<div class="panelBar">
    <div class="pages">
        <span>显示</span> 
            <select class="select" name="numPerPage"
                onchange="dialogPageBreak({numPerPage:this.value})">
                <option value="20" <c:if test="${pagerForm.numPerPage == 20}"> selected </c:if> >20</option>
                <option value="50" <c:if test="${pagerForm.numPerPage == 50}"> selected </c:if> >50</option>
                <option value="100" <c:if test="${pagerForm.numPerPage == 100}"> selected </c:if> >100</option>
                <option value="500" <c:if test="${pagerForm.numPerPage == 500}"> selected </c:if> >500</option>
            </select> 
        <span>条，共 ${pagerForm.totalCount} 条，共 ${pagerForm.totalPageCount} 页</span>
    </div>

    <div class="pagination" targetType="dialog"
        totalCount="${pagerForm.totalCount}"
        numPerPage="${pagerForm.numPerPage}"
        pageNumShown="${pagerForm.pageNumShown}"
        currentPage="${pagerForm.pageNum}">
    </div>
</div>