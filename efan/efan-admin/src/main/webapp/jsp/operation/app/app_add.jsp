<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/meta.jsp"%>
<%@ include file="/common/taglibs.jsp"%>

<script type="text/javascript">
	function upgradeCallback(json) {
		var appInfo = JSON.parse(json);
		if(appInfo.statusCode) {
			DWZ.ajaxDone(json);
			if(json.statusCode == DWZ.statusCode.fail) {
				var status = JSON.parse(json);
				alertMsg.error(status.message);
				return;
			}
		}

		var upgradeInfo = JSON.parse(appInfo.data);
		$(".addApp #id").val(upgradeInfo.id);
		$(".addApp #brief").val(upgradeInfo.brief);
		$(".addApp #updateContent").val(upgradeInfo.updateContent);

		$(".addApp #posterImg").attr("src", upgradeInfo.posterUrl);
		/*$("#iconImg").attr("src", upgradeInfo.iconUrl);*/

		var snapshotsUrlsItems = upgradeInfo.snapshotsUrls.split(",");
		for(var index = 0; index < snapshotsUrlsItems.length; index ++) {
			var snapshotsImgId = ".addApp #snapshotsImg_" + index;

			$(snapshotsImgId).attr("src", snapshotsUrlsItems[index]);
		}

		var allSupportControllers = $(".addApp input[name=supportController]");
		if(upgradeInfo.supportControllers) {
			for(var index = 0; index < allSupportControllers.length; index ++) {
				var controllerId = ".addApp #controller_" + index;
				var controllerName = $(controllerId).attr("controllerName");
				if(upgradeInfo.supportControllers.indexOf(controllerName) > -1) {
					$(controllerId).attr("checked", true);
				}
			}
		}

		var allCatagories = $(".addApp input[name=catagory]");
		if(upgradeInfo.catagories) {
			for(var index = 0; index < allCatagories.length; index ++) {
				var catagoryId = ".addApp #catagory_" + index;
				var catagoryName = $(catagoryId).attr("catagoryName");
				if(upgradeInfo.catagories.indexOf(catagoryName) > -1) {
					$(catagoryId).attr("checked", true);
				}
			}
		}

		var allChannels = $(".addApp input[name=channel]");
		if(upgradeInfo.channels) {
			for(var index = 0; index < allChannels.length; index ++) {
				var channelId = ".addApp #channel_" + index;
				var channelName = $(channelId).attr("channelName");
				if(upgradeInfo.channels.indexOf(channelName) > -1) {
					$(channelId).attr("checked", true);
				}
			}
		}

		$(".addApp #developerName").val(upgradeInfo.developer);
	}

	//清除AppInfo
	function resetAppInfo() {
		$(".addApp #id").val(0);
		$(".addApp #brief").val("");
		$(".addApp #updateContent").val();
		$(".addApp #posterImg").attr("src", "");
		$(".addApp #poster").val("");
		/*$("#iconImg").attr("src", "");*/

		$(".addApp #snapshots").val("");
		for(var i = 0; i < 5; i ++) {
			$(".addApp #snapshotsImg_" + i).attr("src", "");
		}

		$(".addApp input[name=catagory]").attr("checked", false);

		$(".addApp input[name=channel]").attr("checked", false);

		$(".addApp input[name=supportController]").attr("checked", false);

		$(".addApp #developerName").val("");
	}

	function uploadApkCallback(json) {
		DWZ.ajaxDone(json);
		if(json.statusCode == DWZ.statusCode.ok) {
			var apkInfo = JSON.parse(json.data);
			$(".addApp #apkUrl").val(apkInfo.apkUrl);
			$(".addApp #pkgName").val(apkInfo.pkgName);
			$(".addApp #pkgSize").val(apkInfo.pkgSize);
			$(".addApp #verName").val(apkInfo.verName);
			$(".addApp #verCode").val(apkInfo.verCode);
			$(".addApp #displayName").val(apkInfo.displayName);

			resetAppInfo();

			if(apkInfo.isUpdate) {
				$(".addApp #submit").css("display","none");
				$(".addApp #upgrade").css("display", "inline-block");

				$.post("app/get-upgrade-info",
					{'pkgName':apkInfo.pkgName, 'verCode':apkInfo.verCode},
					upgradeCallback);
			} else {
				$(".addApp #submit").css("display","inline-block");
				$(".addApp #upgrade").css("display", "none");
			}
		} else {
			$(".addApp #apkFile").val("");
		}
	}

	function uploadPosterCallback(json) {
		DWZ.ajaxDone(json);
		if(json.statusCode == DWZ.statusCode.ok) {
			var posterUrl = json.data;
			$(".addApp #postUrl").val(posterUrl);
			$(".addApp #posterImg").attr("src", posterUrl);
		} else {
			$(".addApp #poster").val("");
		}
	}

	function uploadSnapshotsCallback(json) {
		DWZ.ajaxDone(json);
		if(json.statusCode == DWZ.statusCode.ok) {
			var snapshotsUrls = json.data;
			var snapshotsUrlArray = snapshotsUrls.split(",");
			var allSnapshotsUrls = "";
			for(var i = 0; i < snapshotsUrlArray.length; i ++) {
				$(".addApp #snapshotsImg_" + i).attr("src", snapshotsUrlArray[i]);
				allSnapshotsUrls += snapshotsUrlArray[i];
				if(i != snapshotsUrlArray.length - 1) {
					allSnapshotsUrls += ",";
				}
			}

			$(".addApp #snapshotsUrls").val(allSnapshotsUrls);
		} else {
			$(".addApp #snapshots").val("");
		}
	}

	function validateAppBaseInfo() {
		var apkUrlInfo = $(".addApp #apkUrl").val();
		if(apkUrlInfo == "" || apkUrlInfo == null) {
			alertMsg.error("请上传APK!");
			return false;
		}
		return true;
		
	}

	function validateAppPicInfo() {
		var postUrlVal = $(".addApp #posterImg").attr("src");
		if(!postUrlVal) {
			alertMsg.error("请上传海报！");
			return false;
		}

		var totalSnapshotsCnt = 0;
		for(var i = 0; i < 5; i ++) {
			var snapshotUrlVal = $(".addApp #snapshotsImg_" + i).attr("src");
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
		var catagories = $(".addApp input[name=catagory]:checked");
		if(catagories.size() < 1) {
			alertMsg.error("请选择商品分类！");
			return false;
		}

		var channels = $(".addApp input[name=channel]:checked");
		if(channels.size() < 1) {
			alertMsg.error("请选择商品用户！");
			return false;
		}

		var supportControllers = $(".addApp input[name=supportController]:checked");
		if(supportControllers.size() < 1) {
			alertMsg.error("请选择支持的遥控器！");
			return false;
		}

		var developerName = $(".addApp #developerName").val();
		if(developerName == null || developerName == "") {
			alertMsg.error("请选择商品开发者！");
			return false;
		}	

		return true;	
	}

	function setPosterUrl() {
		var posterUrl = $(".addApp #posterImg").attr("src");
		$(".addApp #posterUrl").val(posterUrl);
	}

	function setSnapshotsUrls(){
		var allSnapshotsUrls = "";
		for(var i = 0; i < 5; i ++) {
			var snapshotUrlVal = $(".addApp #snapshotsImg_" + i).attr("src");
			if(snapshotUrlVal) {
				if(i > 0) {
					allSnapshotsUrls += ",";
				}
				allSnapshotsUrls += snapshotUrlVal;
			} 
		}

		$(".addApp #snapshotsUrls").val(allSnapshotsUrls);
	}

	$(function() {
		$(".addApp #apkFile").on('change', function() {
			val = uploadFileName = $("#apkFile").val();
			if(uploadFileName) {
				$(".addApp #apkForm").submit();
			} else {
				$(".addApp #apkUrl").val("");
			}
		});


		$(".addApp #poster").on('change', function() {
			var uploadFileName = $(".addApp #poster").val();
			if(uploadFileName) {
				$(".addApp #posterForm").submit();
			} else {
				$(".addApp #posterImg").removeAttr("src");
				$(".addApp #postUrl").val("");		
			}
		});

		$(".addApp #snapshots").on('change', function() {
			var uploadFileName = $(".addApp #snapshots").val();
			for(var i = 0; i < 5; i ++) {
				$(".addApp #snapshotsImg_" + i).removeAttr("src");
			}
			if(uploadFileName) {
				$(".addApp #snapshotsForm").submit();
			} else {
				$(".addApp #snapshotsUrls").val("");
			}
		});

		$(".addApp #upgrade").on('click', function() {
			$("#submit").click();
		});

		$(".addApp #submit").on('click', function() {
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

			setSnapshotsUrls();

			var catagories = $(".addApp input[name=catagory]:checked");
			var allCatagories = "";
			for(var i = 0; i < catagories.size(); i ++) {
				allCatagories += catagories[i].value;
				if(i != catagories.size() - 1) {
					allCatagories += ",";
				}
			}
			$(".addApp #catagories").val(allCatagories);

			var channels = $(".addApp input[name=channel]:checked");
			var allChannels = "";
			for(var i = 0; i < channels.size(); i ++) {
				allChannels += channels[i].value;
				if(i != channels.size() - 1) {
					allChannels += ",";
				}
			}
			$(".addApp #channels").val(allChannels);

			var supportController = $(".addApp input[name=supportController]:checked");
			var supportControllers = "";
			for(var i = 0; i < supportController.size(); i ++) {
				supportControllers += supportController[i].value;
				if(i != supportController.size() - 1) {
					supportControllers += ",";
				}
			}
			$(".addApp #supportControllers").val(supportControllers);

			var developerName = $(".addApp #developerName").val();
			$(".addApp #developer").val(developerName);

			$(".addApp #appForm").submit();
		});
	});
</script>

<div class="pageContent addApp">
	<div class="pageFormContent" layoutH="45">
		<!-- 基本信息 -->
		<div style="float:left">
			<form id="apkForm" action="app/upload-apk" method="post" enctype="multipart/form-data" 
						onsubmit="return iframeCallback(this, uploadApkCallback);">
				<div class="unit">
					<label>上传商品：</label>
					<input id="apkFile" type="file" name="apk" size="25">
				</div>
			</form>
 			<form id="appForm" method="post" action="app/add" class="required-validate"
				onsubmit="return validateCallback(this, navTabAjaxDone);" autocomplete="off">
				<fieldset style="width:420px;margin:4px;height:344px">
					<legend  align="left">基本信息</legend>
					<!-- 基本信息 -->
					<input type="hidden" id="apkUrl" name="apkUrl"/>
					<input type="hidden" id="id" name="id" value="0"/>
					<div class="unit">
						<label>商品包名：</label>
						<input type="text" id="pkgName" name="pkgName" size="25" class="required"/>
					</div>
					<div class="unit">
						<label>安装包大小：</label>
						<input type="text" id="pkgSize" name="pkgSize" size="25" class="required"/>
						<span class="info">M</span>
					</div>
					<div class="unit">
						<label>版本名称：</label>
						<input type="text" id="verName" name="verName" size="25" class="required"/>
					</div>
					<div class="unit">
						<label>版本编码：</label>
						<input type="text" id="verCode" name="verCode" size="25" class="required"/>
					</div>
					<div class="unit">
						<label>商品名：</label>
						<input type="text" id="displayName" name="name" size="25" class="required"/>
					</div>
					<div class="unit">
						<label>商品简介：</label>
						<textarea id="brief" name="brief" cols="25" rows="5" class="required" maxlength="200"/>
					</div>
					<div class="unit">
						<label>升级内容：</label>
						<textarea id="updateContent" name="updateContent" cols="25" rows="5" maxlength="200"/>
					</div>
					<!-- 图片信息 -->
					<input type="hidden" id="posterUrl" name="posterUrl"/>
					
					<!-- <input type="hidden" id="iconUrl" name="iconUrl"/> -->
					
					<input type="hidden" id="snapshotsUrls" name="snapshotsUrls" />

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
				<legend  align="left">图片(支持jpg、png、webp格式)</legend>
				<div class="unit">
					<label>海报：</label>
					<form id="posterForm" action="app/upload-poster" method="post" enctype="multipart/form-data" 
						onsubmit="return iframeCallback(this, uploadPosterCallback);">
						<input id="poster" type="file" name="poster" size="25"/>
					</form>
					<img id="posterImg" width="120px" height="120px" title="海报-(1:1)"/>
				</div>
				<div class="divider"/>
				<div class="unit">
					<label>截图：</label>
						<form id="snapshotsForm" action="app/upload-snapshots" method="post" enctype="multipart/form-data" 
							onsubmit="return iframeCallback(this, uploadSnapshotsCallback);">
							<input id="snapshots" type="file" name="snapshots" size="25" multiple="multiple"/>
						</form>
					<img id="snapshotsImg_0" width="128px" height="72px" title="截图-(16:9)"/>
					<img id="snapshotsImg_1" width="128px" height="72px" title="截图-(16:9)"/>
					<img id="snapshotsImg_2" width="128px" height="72px" title="截图-(16:9)"/>
					<img id="snapshotsImg_3" width="128px" height="72px" title="截图-(16:9)"/>
					<img id="snapshotsImg_4" width="128px" height="72px" title="截图-(16:9)"/>
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
						<c:forEach var="item" items="${catagories}" varStatus="status">
							<input type="checkbox" id="catagory_${status.index}" name="catagory" catagoryName="${item.name}" value="${item.id}">${item.name}</input>
						</c:forEach>
					</fieldset>
				</div>
				<div class="unit">
					<fieldset style="width:420px;margin:4px">
						<legend>所属用户</legend>
						<c:forEach var="item" items="${channels}" varStatus="status">
							<input type="checkbox" id="channel_${status.index}" name="channel" channelName="${item.name}" value="${item.id}">${item.name}</input>
						</c:forEach>
					</fieldset>
				</div>
				<div class="unit">
					<fieldset style="width:420px;margin:4px">
						<legend>遥控器</legend>
						<input type="checkbox" name="supportController" id="controller_0" controllerName="标准遥控器" value="标准遥控器">标准遥控器</input>
						<input type="checkbox" name="supportController" id="controller_1" controllerName="空中飞鼠" value="空中飞鼠">空中飞鼠</input>
						<input type="checkbox" name="supportController" id="controller_2" controllerName="游戏手柄" value="游戏手柄">游戏手柄</input>
					</fieldset>
				</div>
				<div class="unit">
					<label>商品开发商：</label>
					<input type="text" class="required" id="developerName" size="40" maxlength="15"/>
				</div>
			</fieldset>	
		</div>	
	</div>
	<div class="panelBar">
		<div style="float:right">
			<button id="upgrade" style="display:none;">升级</button>
			<button id="submit">确定</button>
		</div>
	</div>
</div>
