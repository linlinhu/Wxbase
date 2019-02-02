function getLogs(id){
	var option = {
		url: basePath +'audit/auditLog',
		type:'POST',
		data:{
			id:id
		}
		},
		tpl = LogDatas.innerHTML,
		view = null;
	layer.open({
		type : 1,
		title : '审核追踪',
		skin : 'layui-layer-rim', //加上边框
		area : [ '60%', '490px' ], //宽高
		content : '<div class="wrapper-content" id="logDetail">' + $('#logDatas-Panel').html() + '</div>'
	});
	ajaxRequest(option, function(result){
		if(result.success){
			view = $('#logDetail #log-view');
			laytpl(tpl).render(result.result, function(html){
				view.html(html);
			});
		} else {
			layer.msg(result.message?result.message:'查询详情失败', {icon: 5});
		}
	})
}