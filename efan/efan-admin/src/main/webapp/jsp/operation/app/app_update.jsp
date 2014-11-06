<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/meta.jsp"%>
<%@ include file="/common/taglibs.jsp"%>

<script type="text/javascript">
	function uploadApkCallback(json) {
		DWZ.ajaxDone(json);
		if(json.statusCode == DWZ.statusCode.ok) {
			var apkInfo = JSON.parse(json.data);
			$(".updateApp #apkUrl").val(apkInfo.apkUrl);
			$(".updateApp #pkgName").val(apkInfo.pkgName);
			$(".updateApp #pkgSize").val(apkInfo.pkgSize);
			$(".updateApp #verName").val(apkInfo.verName);
			$(".updateApp #verCode").val(apkInfo.verCode);
			$(".updateApp #displayName").val(apkInfo.displayName);
		} else {
			$(".updateApp #apkFile").val("");
		}
	}

	function setPosterUrl() {
		var posterUrl = $(".updateApp #posterImg").attr("src");
		$(".updateApp #posterUrl").val(posterUrl);
	}

	function uploadPosterCallback(json) {
		DWZ.ajaxDone(json);
		if(json.statusCode == DWZ.statusCode.ok) {
			var posterUrl = json.data;
			$(".updateApp #posterUrl").val(posterUrl);
			$(".updateApp #posterImg").attr("src", posterUrl);
		} else {
			$(".updateApp #poster").val("");
		}
	}

	function setSnapshotsUrls(){
		var allSnapshotsUrls = "";
		for(var i = 0; i < 5; i ++) {
			var snapshotUrlVal = $(".updateApp #snapshotsImg_" + i).attr("src");
			if(snapshotUrlVal) {
				if(i > 0) {
					allSnapshotsUrls += ",";
				}
				allSnapshotsUrls += snapshotUrlVal;
			} 
		}

		$(".updateApp #snapshotsUrls").val(allSnapshotsUrls);
	}

	function uploadSnapshotsCallback(json) {
		DWZ.ajaxDone(json);
		if(json.statusCode == DWZ.statusCode.ok) {
			var snapshotsUrls = json.data;
			var snapshotsUrlArray = snapshotsUrls.split(",");
			var allSnapshotsUrls = "";
			for(var i = 0; i < snapshotsUrlArray.length; i ++) {
				$(".updateApp #snapshotsImg_" + i).attr("src", snapshotsUrlArray[i]);
				allSnapshotsUrls += snapshotsUrlArray[i];
				if(i != snapshotsUrlArray.length - 1) {
					allSnapshotsUrls += ",";
				}
			}

			$(".updateApp #snapshotsUrls").val(allSnapshotsUrls);
		} else {
			$(".updateApp #snapshots").val("");
		}
	}

	function validateAppBaseInfo() {
		var apkUrlInfo = $(".updateApp #apkUrl").val();
		if(apkUrlInfo == "" || apkUrlInfo == null) {
			alertMsg.error("请上传APK!");
			return false;
		}
		return true;
	}

	function validateAppPicInfo() {
		var postUrlVal = $(".updateApp #posterImg").attr("src");
		if(!postUrlVal) {
			alertMsg.error("请上传海报！");
			return false;
		}

		var totalSnapshotsCnt = 0;
		for(var i = 0; i < 5; i ++) {
			var snapshotUrlVal = $(".updateApp #snapshotsImg_" + i).attr("src");
			if(snapshotUrlVal) {
				totalSnapshotsCnt ++;
			}
		}
		if(totalSnapshotsCnt < 3 || totalSnapshotsCnt > 5) {
			alertMsg.error("截图最少上传3张，最多上传5张！");
			return false;
		}


		return true;
	}

	function validateAppAttrInfo() {
		var catagories = $(".updateApp input[name=catagory]:checked");
		if(catagories.size() < 1) {
			alertMsg.error("请选择商品分类！");
			return false;
		}

		var channels = $(".updateApp input[name=channel]:checked");
		if(channels.size() < 1) {
			alertMsg.error("请选择商品用户！");
			return false;
		}

		var supportControllers = $(".updateApp input[name=supportController]:checked");
		if(supportControllers.size() < 1) {
			alertMsg.error("请选择支持的遥控器！");
			return false;
		}

		var developerName = $(".updateApp #developerName").val();
		if(developerName == null || developerName == "") {
			alertMsg.error("请选择商品开发者！");
			return false;
		}	

		return true;	
	}

	$(function() {
		$(".updateApp #poster").on('change', function() {
			var uploadFileName = $(".updateApp #poster").val();
			if(uploadFileName) {
				$(".updateApp #posterForm").submit();
			} else {
				$(".updateApp #posterImg").removeAttr("src");
				$(".updateApp #postUrl").val("");		
			}
		});

		$(".updateApp #snapshots").on('change', function() {
			var uploadFileName = $(".updateApp #snapshots").val();
			for(var i = 0; i < 5; i ++) {
				$(".updateApp #snapshotsImg_" + i).removeAttr("src");
			}
			if(uploadFileName) {
				$(".updateApp #snapshotsForm").submit();
			} else {
				$(".updateApp #snapshotsUrls").val("");
			}
		});

		$(".updateApp #submit").on('click', function() {
			if(!validateAppBaseInfo()) {
				return;
			}

			if(!validateAppPicInfo()) {
				return;
			}

			if(!validateAppAttrInfo()) {
				return;
			}

			setPosterUrl();
			/*setIconUrl();*/
			setSnapshotsUrls();

			var catagories = $(".updateApp input[name=catagory]:checked");
			var allCatagories = "";
			for(var i = 0; i < catagories.size(); i ++) {
				allCatagories += catagories[i].value;
				if(i != catagories.size() - 1) {
					allCatagories += ",";
				}
			}
			$(".updateApp #catagories").val(allCatagories);

			var channels = $(".updateApp input[name=channel]:checked");
			var allChannels = "";
			for(var i = 0; i < channels.size(); i ++) {
				allChannels += channels[i].value;
				if(i != channels.size() - 1) {
					allChannels += ",";
				}
			}
			$(".updateApp #channels").val(allChannels);

			var supportController = $(".updateApp input[name=supportController]:checked");
			var supportControllers = "";
			for(var i = 0; i < supportController.size(); i ++) {
				supportControllers += supportController[i].value;
				if(i != supportController.size() - 1) {
					supportControllers += ",";
				}
			}
			$(".updateApp #supportControllers").val(supportControllers);

			var developerName = $(".updateApp #developerName").val();
			$(".updateApp #developer").val(developerName);

			$(".updateApp #appForm").submit();
		});
	});
</script>

<div class="pageContent updateApp">
	<div class="pageFormContent" layoutH="45">

			<!-- 基本信息 -->
		<div style="float:left">
 			<form id="appForm" method="post" action="app/update" class="required-validate"
				onsubmit="return validateCallback(this, navTabAjaxDone);" autocomplete="off">
				<fieldset style="width:420px;margin:4px;height:380px">
					<legend  align="left">基本信息</legend>
					<!-- 基本信息 -->
					<input type="hidden" id="apkUrl" name="apkUrl" value="${entity.apkUrl}"/>
 					<div class="unit">
						<label>商品ID：</label>
						<input type="text" size="25" name="id" class="required" value="<c:out value="${entity.id}"/>"/>
					</div> 
					<div class="unit">
						<label>商品名称：</label>
						<input id="pkgName" type="text" name="pkgName" size="25" class="required" value="<c:out value="${entity.pkgName}"/>"/>
					</div>
					<div class="unit">
						<label>商品大小：</label>
						<input id="pkgSize" type="text" size="25" name="pkgSize" class="required" value="<c:out value="${entity.pkgSize}"/>"/>
						<span class="info">M</span>
					</div>
					<div class="unit">
						<label>当前版本名称：</label>
						<input id="verName" type="text" name="verName" size="25" class="required" value="<c:out value="${entity.verName}"/>"/>
					</div>
					<div class="unit">
						<label>当前版本编码：</label>
						<input id="verCode" type="text" name="verCode" size="25" class="required" value="<c:out value="${entity.verCode}"/>"/>
					</div>
					<div class="unit">
						<label>前端显示名：</label>
						<input id="displayName" type="text" name="name" size="25" class="required" value="<c:out value="${entity.name}"/>"/>
					</div>
					<div class="unit">
						<label>简介：</label>
						<textarea name="brief" cols="30" rows="5" class="required" maxlength="200" value="${entity.brief}"><c:out value="${entity.brief}"/>
						</textarea>
					</div>
					<div class="unit">
						<label>升级内容：</label>
						<textarea name="updateContent" cols="30" rows="5" maxlength="200" value="${entity.updateContent}"><c:out value="${entity.updateContent}"/>
						</textarea>
					</div>
					<!-- 图片信息 -->
					<input type="hidden" id="posterUrl" name="posterUrl"/>
					<!-- <input type="hidden" id="iconUrl" name="iconUrl"/> -->
					<input type="hidden" id="snapshotsUrls" name="snapshotsUrls"/>

					<!-- 属性 -->
					<input type="hidden" id="catagories" name="catagories"/>
					<input type="hidden" id="channels" name="channels"/>
					<input type="hidden" id="supportControllers" name="supportControllers"/>
					<input type="hidden" id="developer" name="developer"/>
				</fieldset>
			</form>
		</div>
		<!-- 图片 -->
		<div style="float:left">
			<fieldset style="width:420px;margin:4px;height:380px">
				<legend  align="left">图片</legend>
				<div class="unit">
					<label>海报：</label>
					<form id="posterForm" action="app/upload-poster" method="post" enctype="multipart/form-data" 
						onsubmit="return iframeCallback(this, uploadPosterCallback);">
						<input id="poster" type="file" name="poster" size="25"/>
					</form>
					<img id="posterImg" width="120px" height="120px" title="海报-(1:1)" src="${entity.posterUrl}"/>
				</div>
				<div class="divider"/>
				<div class="unit">
					<label>截图：</label>
						<form id="snapshotsForm" action="app/upload-snapshots" method="post" enctype="multipart/form-data" 
							onsubmit="return iframeCallback(this, uploadSnapshotsCallback);">
							<input id="snapshots" type="file" name="snapshots" size="25" multiple="multiple"/>
						</form>
					<c:forEach var="item" items="${appSnapshotsUrls}" varStatus="status">
						<img id="snapshotsImg_${status.index}" width="128px" height="72px" title="截图-(16:9)" src="${item}"/>
					</c:forEach>	
				</div>
			</fieldset>	
		</div>
		
		<!-- 基本信息 -->
		<div style="float:left">
			<fieldset style="width:420px;margin:4px;height:380px">
				<legend  align="left">属性</legend>
				<div class="unit">
					<fieldset style="width:420px;margin:4px">
						<legend>所属分类</legend>
						<c:forEach var="item" items="${catagories}">
							<input type="checkbox" name="catagory" value="${item.id}" <c:if test="${fn:contains(appCatagories, item.name)}">checked</c:if>  >${item.name}</checkbox>
						</c:forEach>
					</fieldset>
				</div>
				<div class="unit">
					<fieldset style="width:420px;margin:4px">
						<legend>所属用户</legend>
						<c:forEach var="item" items="${channels}" >
							<input type="checkbox" name="channel" value="${item.id}" <c:if test="${fn:contains(appChannels, item.name)}">checked</c:if>  >${item.name}</checkbox>
						</c:forEach>
					</fieldset>
				</div>
				<div class="unit">
					<fieldset style="width:420px;margin:4px">
						<legend>遥控器</legend>
						<input type="checkbox" name="supportController" value="标准遥控器" 
							<c:if test="${fn:contains(appSupportControllers, '标准遥控器')}">checked</c:if> >  标准遥控器</checkbox>
						<input type="checkbox" name="supportController" value="空中飞鼠" 
							<c:if test="${fn:contains(appSupportControllers, '空中飞鼠')}">checked</c:if>  >空中飞鼠</checkbox>
						<input type="checkbox" name="supportController" value="游戏手柄" 
							<c:if test="${fn:contains(appSupportControllers, '游戏手柄')}">checked</c:if>  >游戏手柄</checkbox>
					</fieldset>
				</div>
				<div class="unit">
					<label>商品开发商：</label>
					<input type="text" class="required" id="developerName" size="40" maxlength="15" value="<c:out value="${entity.developer}"/>"/>
				</div>
			</fieldset>	
		</div>	
	</div>
	<div class="panelBar">
		<div style="float:right">
			<button id="submit">保存修改</button>
		</div>
	</div>
</div>
