
/***
 * 设置单选
 * @param self 本体
 */
function singleChosen(self){
	var idPrefix = $(self).parent().parent().attr('id-prefix');
	
	$('#' + idPrefix + 'Chosen .iradio_square-green.checked').removeClass('checked');
	$(self).find('.iradio_square-green').addClass('checked');
}

/***
 * 设置一个主体
 * @param self 本体
 */
function choseAEcm(self, idPrefix, formId){
	var dataId = $(self).attr('data-id'),
		formId = formId ? formId : $(self).parent().parent().parent().attr('id'),
		ecmTpl = ecmDatas.innerHTML,//数据模板
		ecmView;
	/*loading = layer.load();*/
	layer.open({
		type : 1,
		title : '选择一个主体',
		skin : 'layui-layer-rim', //加上边框
		area : [ '60%', '525px' ], //宽高
		content : '<div class="wrapper-content" id="' + idPrefix + 'Chosen">' + $('#ecmChosenTpl').html() + '</div>',
		btn : [ '已选择', '取消' ],
		yes : function(lindex, layero) {
			var ecm = $('#' + idPrefix + 'Chosen .iradio_square-green.checked').find('input[name="chosenEcm"]').val(),
				ecmObj = (ecm && ecm.length > 0) ? JSON.parse(ecm) : null;
	
			if (ecmObj) {
				var temp = ecmObj.name +'  '+ ecmObj.sn;
				$('#' + formId + ' input[name="ecmId"]').val(ecmObj.id);
				$('#' + formId + ' input[name="ecmName"]').val(temp);
				$('#' + formId + ' input[name="ecm"]').attr('data-id', ecmObj.id);
				layer.close(lindex);
			} else {
				layer.alert('请选择一个主体', {icon: 5});
			}
			return false;
		}
	});
	
	$('.PCSearchForm button[role="submit"]').on('click', function(){
		var data = $('#' + idPrefix +'Chosen .PCSearchForm input').val();
		if(data.trim().length > 0){
			searchEcm (data)
			
		} else {
			layer.alert('请输入关键字！', {icon: 5});	
		}
	})
	
	function searchEcm (keyword) {
		$.ajax({
			url: basePath + 'ecm/getEcms',
			type: 'get',
			data:{
				keyword:keyword
			},
			
			success:function(result){
				/*layer.close(loading);*/
				if (result.success) {
					ecmView = $('#' + idPrefix + 'Chosen #ecm-view');
					if(result.result && result.result.length > 0) {
						
						laytpl(ecmTpl).render(result.result, function(html){
							ecmView.html(html);
							//form的id只能实时生成，单页面应用需要保证id的唯一性。 
							
							$('#' + idPrefix + 'Chosen .i-checks').iCheck({
							    checkboxClass: 'icheckbox_square-green',
							    radioClass: 'iradio_square-green',
							});
							if(dataId) {
								$('#' + idPrefix + 'Chosen .iradio_square-green.checked').removeClass('checked');
								$('#' + idPrefix + 'Chosen input[data-id="' + dataId + '"]').parent().addClass('checked');
							}
							$('#' + idPrefix + 'Chosen .footable').removeClass('footable-loaded');
							$('#' + idPrefix + 'Chosen .footable').footable();
							$('#' + idPrefix + 'Chosen #ecm-view').attr('id-prefix', idPrefix);
						});
					} else {
						ecmView.html('<tr><td></td><td>暂无数据</td></tr>');
						
					}
					
				} else {
					layer.msg('获取数据失败！', {icon: 5});
				}
			}
		});
	}
}

/***
 * 选择一个收货单
 * @param self 本体
 */
function choseARec(self, idPrefix, formId){
	var dataId = $(self).attr('data-id'),
		formId = formId ? formId : $(self).parent().parent().parent().attr('id'),
		recTpl = ActivityManagementRecDatas.innerHTML,//数据模板
		option = {
			url: basePath +'activityManagement/findAllReceivedRecord',
			type:'GET',
			data:{
				deliverEcmId:$('#activityManagementForm input[name="ecmId"]').val()
			}
		},
		recView;
	
	layer.open({
		type : 1,
		title : '选择收货单',
		skin : 'layui-layer-rim', //加上边框
		area : [ '60%', '525px' ], //宽高
		content : '<div class="wrapper-content" id="' + idPrefix + 'Chosen">' + $('#ActivityManagementRec-Panel').html() + '</div>',
		btn : [ '已选择', '取消' ],
		yes : function(lindex, layero) {
			var rec = null,
				recObj = null,
				temp = '',
				msg = null,
				checkedInput = $('#' + idPrefix + 'Chosen input:checked');
			
			if (checkedInput.length > 0) {
				mrList = [];
				$('#' + idPrefix + 'Chosen input:checked').each(function(){ 
					rec = $(this).val();
					msg = $(this).attr('data-id');
					recObj = (rec && rec.length > 0) ? JSON.parse(rec):null;
					temp += (recObj.receiveSN + '  ');
					
					recObj.statisticData = JSON.parse(msg);
					recObj.receiver = {
						receiverPersonName: recObj.receiverPersonName,
						receiverPersonPhone:recObj.receiverPersonPhone
					};
					recObj.receiverECM = {
							ecmId:recObj.receiverEcmId,
							name:recObj.receiverName
					};
					recObj.productCount = recObj.statisticData.totalDot;
					mrList.push(recObj);
				});
				
				$('#' + formId + ' input[name="mrList"]').val(JSON.stringify(mrList));
				$('#' + formId + ' input[name="mrListName"]').val(temp);
				$('#' + formId + ' .chosenMr').removeClass('hide');
				
				var totalDotNumber = 0;
				for(var i = 0; i < mrList.length; i++ ){
					totalDotNumber += Math.round(mrList[i].productCount);//统计收货单的产品数量
				}
				for(var j = 0; j<awardList.length; j++) {
					awardList[j].probability = Math.round((awardList[j].totalCount/totalDotNumber)*10000)/100;//重新计算中奖率
				}
				renderAwardList(awardList);
				layer.close(lindex);
			} else {
				layer.alert('请选择收货单', {icon: 5});
			}
			return false;
		}
	});
	ajaxRequest(option, function(result){
		if(result.success) {
			recView = $('#' + idPrefix + 'Chosen #ActivityManagementRec-view');
			
			if(result.result && result.result.length > 0) {
				for(var i = 0; i < result.result.length; i++){
					var statisticData = {
					        list:[],
					        count:0,// 总箱数
					        totalDot:0// 总个数
					    },
					    goods = result.result[i].goods;
					getDetailsGoods(goods,statisticData);
					result.result[i].statisticData = statisticData;
					result.result[i].msg = JSON.stringify(statisticData);
				}
				result.mrList = mrList;
				
				console.log(result)
				laytpl(recTpl).render(result, function(html){
					recView.html(html);
					//form的id只能实时生成，单页面应用需要保证id的唯一性。 
					$('#' + idPrefix + 'Chosen .i-checks').iCheck({
					    checkboxClass: 'icheckbox_square-green',
					    radioClass: 'iradio_square-green',
					});
					$('#' + idPrefix + 'Chosen .filter').attr('id', idPrefix + 'Filter');
					$('#' + idPrefix + 'Chosen .footable').attr('data-filter', '#' + idPrefix + 'Filter');
					$('#' + idPrefix + 'Chosen .footable').footable();
					$('#' + idPrefix + 'Chosen #ActivityManagementRec-view').attr('id-prefix', idPrefix);
					
					$('#' + idPrefix + 'Chosen input:checked').parent().removeClass("disabled");
					$('#' + idPrefix + 'Chosen input:checked').removeAttr("disabled");
					
				});
			} else {
				recView.html('<tr><td></td><td>暂无数据</td></tr>');
				
			}
		} else {
			layer.msg('获取收货单数据失败', {icon: 5});
		}
	})
}

/***
 * 选择一个奖品或优惠券
 * @param self 本体
 */
function choseAnAward(self, idPrefix, formId){
	var dataId = $(self).attr('data-id'),
		formId = formId ? formId : $(self).parent().parent().parent().attr('id'),
		option = {
			url: basePath +'activityManagement/getAwardCoupon',
			type:'GET'
		},
		awardType = 1,
		awdTpl = ActivityManagementAwardDatas.innerHTML,//奖品数据模板
		couTpl = ActivityManagementCouponDatas.innerHTML,//优惠券数据模板
		awdView,
		tpl,
		data;
		
	layer.open({
		type : 1,
		title : '选择奖品或优惠券',
		skin : 'layui-layer-rim', //加上边框
		area : [ '60%', '525px' ], //宽高
		content : '<div class="wrapper-content" id="' + idPrefix + 'Chosen">' + $('#ActivityManagementAward-Panel').html() + '</div>',
		btn : [ '已选择', '取消' ],
		yes : function(lindex, layero) {
			var rec = $('#' + idPrefix + 'Chosen .iradio_square-green.checked').find('input[name="chosenActivityManagementRec"]').val(),
				recObj = (rec && rec.length > 0) ? JSON.parse(rec) : null,
				awardType = $('#' + idPrefix + 'Chosen .iradio_square-green.checked').find('input[name="chosenActivityManagementRec"]').attr('data-type');
				
			if (recObj) {
				var temp = recObj.name;
				recObj.awardType = awardType;
				recObj.awardId = recObj.id;
				console.log('recObj',recObj);
				$('#' + formId + ' input[name="awardItem"]').val(JSON.stringify(recObj));
				$('#' + formId + ' input[name="awardName"]').val(temp);
				$('#' + formId + ' input[name="awardItem"]').attr('data-id', recObj.id);
				layer.close(lindex);
			} else {
				layer.alert('请设置奖品或优惠券', {icon: 5});
			}
			return false;
		}
	});
	ajax(awardType);
	$('#'+ idPrefix +'Chosen #awardTypeSearchForm input.award').attr('checked','');
	$('input[name="awardType"]').on('ifChecked', function(event){
		var awardType = event.currentTarget.value;
		if(awardType == 1) {
			$('#' + idPrefix + 'Chosen .award-panel').removeClass('hide');
			$('#' + idPrefix + 'Chosen .coupon-panel').addClass('hide')
		} else if(awardType == 2) {
			$('#' + idPrefix + 'Chosen .award-panel').addClass('hide');
			$('#' + idPrefix + 'Chosen .coupon-panel').removeClass('hide')
		}
		$('#' + idPrefix + 'Chosen input.filter').val('');
		ajax(awardType)
	})
	
	$('#'+ idPrefix +'Chosen .i-checks').iCheck({
	    checkboxClass: 'icheckbox_square-green',
	    radioClass: 'iradio_square-green',
	});
	/*$('#'+ idPrefix +'Chosen #awardTypeSearchForm .btn').on('click',function(){
		var formObj = $('#'+ idPrefix +'Chosen #awardTypeSearchForm').serializeObject();
			awardType = formObj.awardType;
		if(awardType == 1) {
			$('#' + idPrefix + 'Chosen .award-panel').removeClass('hide');
			$('#' + idPrefix + 'Chosen .coupon-panel').addClass('hide')
		} else if(awardType == 2) {
			$('#' + idPrefix + 'Chosen .award-panel').addClass('hide');
			$('#' + idPrefix + 'Chosen .coupon-panel').removeClass('hide')
		}
		$('#' + idPrefix + 'Chosen input.filter').val('');
		ajax(awardType)
	})*/
	
	function ajax(type){
		ajaxRequest(option, function(result){
			if(result.coupons && result.awards) {
				if(type == 1) {
					awdView = $('#' + idPrefix + 'Chosen #ActivityManagementAward-view');
					tpl = awdTpl;
					data = result.awards
				} else if(type == 2) {
					awdView = $('#' + idPrefix + 'Chosen #ActivityManagementCoupon-view');
					tpl = couTpl;
					data = result.coupons
				}
				
				if(data.length > 0) {
					laytpl(tpl).render(data, function(html){
						awdView.html(html);
						//form的id只能实时生成，单页面应用需要保证id的唯一性。 
						$('#' + idPrefix + 'Chosen table .i-checks').iCheck({
						    checkboxClass: 'icheckbox_square-green',
						    radioClass: 'iradio_square-green',
						});
						if(dataId) {
							$('#' + idPrefix + 'Chosen .iradio_square-green.checked').removeClass('checked');
							$('#' + idPrefix + 'Chosen input[data-id="' + dataId + '"]').parent().addClass('checked');
						}
						if(type == 1) {
							$('#' + idPrefix + 'Chosen .awardFilter').attr('id', idPrefix + 'AwardFilter');
							$('#' + idPrefix + 'Chosen .award-footable').attr('data-filter', '#' + idPrefix + 'AwardFilter');
							$('#' + idPrefix + 'Chosen .award-footable').footable();
						} else if(type == 2) {
							$('#' + idPrefix + 'Chosen .couponFilter').attr('id', idPrefix + 'CouponFilter');
							$('#' + idPrefix + 'Chosen .coupon-footable').attr('data-filter', '#' + idPrefix + 'CouponFilter');
							$('#' + idPrefix + 'Chosen .coupon-footable').footable();
						}
						
						$('#' + idPrefix + 'Chosen #ActivityManagementRec-view').attr('id-prefix', idPrefix);
					});
				} else {
					awdView.html('<tr><td></td><td>暂无数据</td></tr>');	
				}
				if(result.coupons.length == 0 && result.awards.length == 0){
					layer.msg('没有可用的奖品和奖券', {icon: 5});
				}
			} else {
				layer.msg('获取数据失败', {icon: 5});
			}
		})
	}
	
}



