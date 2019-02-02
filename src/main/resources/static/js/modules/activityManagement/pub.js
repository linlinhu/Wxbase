/*收货单商品数据整理
 * data 商品list
 * list 共计结果{
		        list:[],
		        count:0,// 总箱数
		        totalDot:0// 总个数
		    }
 */
function getDetailsGoods(data,list){
    for (var i = 0; i < data.length; i++){
        var type1 = '',
            type2 = '',
            msg = '',
            goodsItem = {};
        msg = data[i].good.name + '&nbsp;'
        for(var n = 0; n < data[i].good.prdFeatures.length; n++) {
            var prdFeature = data[i].good.prdFeatures[n];
            if(prdFeature.type == 1){
               type1 += (prdFeature.value + '&nbsp');
            } else {
            	var itemValue = prdFeature.value;
            	
            	type2 += ('×'+prdFeature.value);
              
            }    
        }
        msg = msg + type1 + type2;
        goodsItem = {
            code:data[i].code,
            good:{
                sn:data[i].good.sn,
                msg: msg, 
                value: itemValue
            }
        };
        list = statistic(goodsItem,list)
    }
}

/*数据统计
 * item 被统计的每一条数据
 * list 共计结果 {coun：0，list:[]}
 */
function statistic(item,list){
   var temp = -1;
   
   list.count += 1;
   list.totalDot += parseInt(item.good.value);
   for(var i =0; i < list.list.length; i++){
      if(list.list[i].good.sn == item.good.sn) {
         list.list[i].count +=1; 
         temp = i;
      } 
   }
   if(temp == -1) {
       item.count = 1;
       list.list.push(item);
   }
   return list;
}

//参与活动的收货单详情
function showMrlist(data,idPrefix){
	var mrlistDetailTpl = ActivityManagementMrListDatas.innerHTML,
	 	mrlistDetailView,
		activityReceiveListStatistic = [],
		statisticItem = {
	        list:[],
	        count:0,// 收货单的总箱数
	        totalDot:0// 总个数
	    },
	    totalNumber = 0, //多个收货单的总箱数
	    totalDotNumber = 0;//多个收货单的总个数
	layer.open({
		type : 1,
		title : '参与活动产品',
		skin : 'layui-layer-rim', //加上边框
		area : [ '40%', '400px' ], //宽高
		content : '<div class="wrapper-content" id="' + idPrefix + 'Detail">' + $('#activityManagementMrList-Panel').html() + '</div>'
	});
	for(var i = 0; i < data.length; i++) {
		var goods = data[i].goods;
		
		statisticItem = {
	        list:[],
	        count:0,// 总箱数
	        totalDot:0// 总个数
	    };
		getDetailsGoods(goods,statisticItem);
		statisticItem.receiveSN = data[i].receiveSn;
		totalNumber += parseInt(statisticItem.count);
		totalDotNumber += parseInt(statisticItem.totalDot);
		activityReceiveListStatistic.push(statisticItem);
	}
	var renderData = {
			activityReceiveListStatistic:activityReceiveListStatistic,
			totalNumber:totalNumber,
			totalDotNumber:totalDotNumber	
	}
	renderMrList(renderData)
	function renderMrList(list){
		 mrlistDetailView = $('#'+ idPrefix + 'Detail #activityManagementMrList-view');
		laytpl( mrlistDetailTpl).render(list, function(html){
			 mrlistDetailView.html(html);
		});
	}
}
