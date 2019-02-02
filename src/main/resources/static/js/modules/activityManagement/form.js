
$("#activityManagementForm").steps({
	bodyTag: "fieldset",
	onStepChanging: function (event, currentIndex, newIndex) {
		var formObj = $('#activityManagementForm').serializeObject(),
			params = {};
		if(newIndex == 1) {
			if(formObj.name =="") {
				layer.msg('请填写活动名称后再进行下一步', {icon: 5});
				return false;
			}
			if(formObj.description =="" || formObj.description.length < 9) {
				layer.msg('活动说明长度不能少于10字符', {icon: 5});
				return false;
			}
		}
		if(newIndex == 2) {
			if(formObj.mrListName =="") {
				layer.msg('请选择收货单后再进行下一步', {icon: 5});
				return false;
			}
			if (formObj.setDateBegin == "") {
				layer.msg('请设置活动开始时间后再进行下一步', {icon: 5});
				return false;
			}
			if (formObj.setDateEnd == "") {
				layer.msg('请设置活动结束时间后再进行下一步', {icon: 5});
				return false;
			}
			if((new Date(formObj.setDateBegin)).getTime() > (new Date(formObj.setDateEnd)).getTime()) {
				layer.msg('活动开始时间晚于活动结束时间，请重新设置', {icon: 5});
				return false;
			}
		}
		return true;
	},
	onFinished: function (event, currentIndex) {
		submitActivity()
	},
	onCanceled:function(){
		goPage('index');
	},
	labels: {
	    finish: "保存配置"
	}
});

function submitActivity(){
	//设置奖品信息
	if (awardList.length == 0) {
		layer.msg('请完善奖品信息后再进行下一步', {icon: 5});
		return false;
	}else{
		var formObj = $('#activityManagementForm').serializeObject(),
		params = {};
		params = {
				name: formObj.name,
				description: formObj.description,
				awardList: awardList,
				mrList: mrList,
				id: formObj.id,
				creatorEcmId: globalEcmId,
				ecmId:formObj.ecmId,
				creator: JSON.parse(localStorage.wxbasePerson),
				startTime:(new Date(formObj.setDateBegin + ' 00:00:00')).getTime(),
				endTime: (new Date(formObj.setDateEnd + ' 23:59:59')).getTime()
		}
		
		var totalProbability = 0;
		for(var j = 0;j < params.awardList.length; j++){
			totalProbability += params.awardList[j].probability*1;
		}
		
		if(params.startTime < (new Date()).getTime()){
			layer.msg('活动开始时间不应早于当前时间，请重新设置！', {icon: 5});
		} else if(totalProbability > 100){
			layer.msg('活动的中奖率不能大于100%，请重新设置！', {icon: 5});
		}else {
			saveActivity({
				jsonStr: JSON.stringify(params)
			}, 'activityManagement')
		}
		
			
	}
}
//展示已经选择的收货单的详情
function chosenMrList() {
	var option = {
			url: basePath +'activityManagement/findRGBySn',
			type:'GET'
		},
		activityReceiveList = [];
	for(var i = 0; i < mrList.length; i++) {
		option.data={
				sn: mrList[i].receiveSN
		};
		ajaxRequest(option, function(result){
			if(result.success){
				activityReceiveList.push(result.result);
				if(activityReceiveList.length == mrList.length){
					showMrlist(activityReceiveList,'showMrlist');
				}
			} else {
				layer.msg('获取收货单数据失败', {icon: 5});
			}
		})
	}
	
}
function showAddPanel(){
	$('#awardEditForm').removeClass('hide')
	$('#awardEditForm input[name="awardItem"]').val('');
	$('#awardEditForm input[name="awardName"]').val('');
	$('#awardEditForm input[name="totalCount"]').val('');
	$('#awardEditForm input[name="probability"]').val('');
	$('#awardEditForm input[name="awardItem"]').attr("data-index",'');
	$('.awardEditForm-title').html('新增')
}
//添加奖品
function addAward(){
	var submitObj = {},
		params = {},
		isEdit = false,
		isSingle = true,
		temp = -1,
		index = $('#awardEditForm input[name="awardItem"]').attr("data-index")?$('#awardEditForm input[name="awardItem"]').attr("data-index"):'';
	
	submitObj.awardItem = $('#awardEditForm input[name="awardItem"]').val();
	submitObj.totalCount = $('#awardEditForm input[name="totalCount"]').val();
	submitObj.probability = $('#awardEditForm input[name="probability"]').val();
	if(submitObj.awardItem == undefined || submitObj.awardItem == '') {
		layer.msg('请选择奖品或优惠券', {icon: 5});
	} else if (submitObj.totalCount == undefined || (submitObj.totalCount == 0 || submitObj.totalCount == '')) {
		layer.msg('请填写奖品数量', {icon: 5});
	} else if(submitObj.probability == undefined || submitObj.probability == '') {
		layer.msg('请重新填写奖品数量', {icon: 5});
	
	} else {
		var previousList = [];
		params.totalCount = submitObj.totalCount;
		params.probability = submitObj.probability.substring(0,submitObj.probability.length-1);
		params.awardInfo = JSON.parse(submitObj.awardItem);
		params.awardType = params.awardInfo.awardType;
		params.awardId = params.awardInfo.awardId;
		
		for(var i = 0; i < awardList.length; i++) {
			if(params.awardId == awardList[i].awardId && params.awardType == awardList[i].awardType) {
				isEdit = true;
				isSingle = false;
				temp = i;
			}
			previousList.push(awardList[i]);
		}
		
		if (isSingle) {
			if(index == ''){//新增
				var tempList = awardList;
				
				awardList.push(params);	
			} else {//编辑
				awardList[index] = params
			}
			
			judge(previousList);
			/*for(var j = 0; j < awardList.length; j++) {
				awardTotalProbability += awardList[j].probability*1;
			}
			console.log('awardTotalProbability',awardTotalProbability)
			if(awardTotalProbability > 100) {
				layer.msg('活动的中奖率不能大于100%', {icon: 5});
				awardList = previousList;
			} else {
				renderAwardList(awardList);
				showAddPanel();
			}*/
			
			
		} else {
			if(index.length > 0 ){
				if(temp == index){
					awardList[index] = params;
					judge(previousList);
					/*renderAwardList(awardList);
					showAddPanel();*/
				} else {
					layer.msg('奖品已经存在', {icon: 5});
				}
			} else {
				awardList[temp] = params;
				judge(previousList)
				/*renderAwardList(awardList);
				showAddPanel();*/
			}
			
		}
		function judge(list){
			var awardTotalProbability = 0;
			for(var j = 0; j <awardList.length; j++) {
				awardTotalProbability += awardList[j].probability*1;
			}
			if(awardTotalProbability > 100) {
				layer.msg('活动的中奖率不能大于100%', {icon: 5});
				awardList = previousList;
			} else {
				renderAwardList(awardList);
				showAddPanel();
			}
		}
	}
}

$('input[name="totalCount"]').on('change',function(){
	var value = $('input[name="totalCount"]').val();
	calProbability(value);
})
//计算中奖率
function calProbability(value){
	var totalDotNumber = 0;
	for(var i = 0; i < mrList.length; i++ ){
		totalDotNumber += Math.round(mrList[i].productCount);	
	}
	if(value > totalDotNumber){
		layer.msg('该奖品数量不应大于产品数量（' + totalDotNumber + '）', {icon: 5});
		$('input[name="probability"]').val('');
	} else if(value == 0) {
		layer.msg('奖品数量不能为0', {icon: 5});
	} else {
		var res = Math.round((value/totalDotNumber)*10000)/100;
		$('input[name="probability"]').val(res+'%');
	}
}
//渲染奖品列表
function renderAwardList(data) {
	var tpl = awardListDatas.innerHTML,
		view = $('#activityManagementForm #awardList-view');
	if(data.length > 0) {
		laytpl(tpl).render(data, function(html){
			view.html(html);
		});
	} else {
		view.html('<tr><td>暂无</td></tr>');
	}
	$('.awardList-table').removeClass('footable-loaded');
	$('.awardList-table').footable();
}

//删除奖品
function removeAward(id){
	for(var i = 0; i < awardList.length; i++) {
		if(id == awardList[i].awardId) {
			awardList.splice(i,1);
		}
	}
	renderAwardList(awardList);
}

//编辑奖品
function editAward(id,index){
	var item = awardList[index];
	$('#awardEditForm').removeClass('hide')
	$('.awardEditForm-title').html('编辑')
	$('#awardEditForm input[name="awardItem"]').val(JSON.stringify(item.awardInfo));
	$('#awardEditForm input[name="awardItem"]').attr("data-index",index);
	$('#awardEditForm input[name="awardName"]').val(item.awardInfo.name);
	$('#awardEditForm input[name="totalCount"]').val(item.totalCount);
	$('#awardEditForm input[name="probability"]').val(item.probability+"%");
}

//只能输入数字
function onlyNum (){
	var e = event || window.event,
 		keynum=e.keyCode;
	console.log(keynum)
	 if(keynum == 187 || keynum == 190 || keynum == 189 || keynum == 109 || keynum == 107 || keynum == 46 || keynum == 110|| keynum == 229){
	 	e.returnValue = false
	 }
}
//保存活动
function saveActivity(params, moduleName) {
	jsonResponse(
			{
				moduleName : moduleName,
				oper : 'save',
				params : params
			},
			function() {
				var pid = params.id ? params.id : null, 
					jsonStr = params.jsonStr ? params.jsonStr: null;

				if (jsonStr != null) {
					pid = JSON.parse(jsonStr).id;
				}

				if (!pid) {
					goPage('index', {
						limit : 10,
						createEcmId: JSON.parse(jsonStr).ecmId
					});
				} else {
					goPage('index');
				}
			});
}



