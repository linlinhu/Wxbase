layui.use(['laypage'], function(){
	laypage = layui.laypage;
});
function initTable(p) {
	var i = 0, 
		paramsEls = null,
		modelName = p.modelName,
		curr = p.curr ? parseInt(p.curr) : 0,
		totalPage = p.totalPage ? parseInt(p.totalPage) : 0,
		totalCount = p.totalCount ? parseInt(p.totalCount) : 0,
		limit = p.limit ? parseInt(p.limit) : 10,
		pageInput = $('#' + modelName + 'SearchForm input[name="page"]');
	
	if (!modelName) {
		return false;
	}
	pageInput.val(curr);
	// 初始化列表
	$('#' + modelName + 'Table').footable();

	if (totalPage > 0 && curr <= totalPage) {
		pageList({
			modelName: modelName,
			totalCount: totalCount,
			limit: limit,
			curr: pageInput.val()
		}, function(obj) {
			pageInput.val(obj.curr);
			pageSearch(modelName, obj.limit);
		})
	}

	if (curr > totalPage && totalPage != 0) {
		layer.msg('当前页已无可操作数据，即将跳转至查询首页数据...');
		setTimeout(function() {
			goPage('index', {
				limit : limit
			}, 2000);
		})
	}

	$('#' + modelName + 'SearchForm button[role="submit"]').attr('onclick',
			"pageSearch('" + modelName + "', '" + limit + "')");
	$('#' + modelName + 'SearchForm button[role="reset"]').attr('onclick',
			"resetForm('" + modelName + "', '" + limit + "')");

	paramsEls = $('#' + modelName + 'SearchForm input[role="user-params"]');
	for (i = 0; i < paramsEls.length; i++) {
		$(paramsEls[i]).attr('onclick', "resetPage('" + modelName + "')");
	}

	paramsEls = $('#' + modelName + 'SearchForm select[role="user-params"]');
	for (i = 0; i < paramsEls.length; i++) {
		$(paramsEls[i]).attr('onclick', "resetPage('" + modelName + "')");
	}

	$('#' + modelName + 'SearchForm').keydown(function(e) {
		var theEvent = e || window.event;
		var code = theEvent.keyCode || theEvent.which || theEvent.charCode;
		if (code == 13) {
			pageSearch(modelName, limit);
			return false;
		}
		return true;
	});


}

function pageList(p, callback) {
	laypage.render({
		elem: p.modelName + 'Page', //注意，这里的 test1 是 ID，不用加 # 号
		count: p.totalCount, //数据总数 //从服务端得到
		limit: p.limit,
		curr: p.curr,
		theme: '#0069b6',
	  	layout: ['count', 'prev', 'page', 'next', 'limit', 'skip'],
		jump : function(obj, first) {
			if(!first) {
				callback(obj);
			}
		}
	});
}

/**
 * 搜索查询
 */
function pageSearch(modelName, limit) {
	var searchParams = $('#' + modelName + 'SearchForm').serializeObject();

	searchParams.limit = limit ? limit : 10;

	goPage('index', searchParams);
}

/**
 * 重置表单
 */
function resetForm(modelName, limit) {
	var ups = $('#' + modelName + 'SearchForm input[role="user-params"]');
	for (i = 0; i < ups.length; i++) {
		$(ups[i]).val('');
	}
	ups = $('#' + modelName + 'SearchForm select[role="user-params"]');
	for (i = 0; i < ups.length; i++) {
		$(ups[i]).val('');
	}

	pageSearch(modelName, limit);
}

/**
 * 重置页码
 */
function resetPage(modelName) {
	$('#' + modelName + 'SearchForm input[name="page"]').val(1);
}

/*******************************************************************************
 * 删除资源
 * 
 * @param ids
 *            资源编号，多个用逗号分隔
 * @param moduleName
 *            模块名称
 */
function remove(ids, moduleName, confirmMsg) {
	jsonResponse({
		moduleName : moduleName,
		oper : 'delete',
		params : {
			ids : ids
		},
		confirmMsg : confirmMsg
	});
}

/*******************************************************************************
 * 保存资源
 * 
 * @param params
 *            传递参数
 * @param moduleName
 *            模块名称
 */
function save(params, moduleName) {
	jsonResponse(
			{
				moduleName : moduleName,
				oper : 'save',
				params : params
			},
			function() {
				var pid = params.id ? params.id : null, jsonStr = params.jsonStr ? params.jsonStr
						: null;

				if (jsonStr != null) {
					pid = JSON.parse(jsonStr).id;
				}

				if (!pid) {
					goPage('index', {
						limit : 10
					});
				} else {
					goPage('index');
				}
			});
}

/**
 * 同步数据
 * 
 * @param params
 *            传递参数
 * @param moduleName
 *            模块名称
 */
function synData(params, moduleName) {
	jsonResponse({
		moduleName : moduleName,
		oper : 'syn',
		params : params
	});
}

function setStatus(moduleName, ids, status) {
	jsonResponse({
		moduleName : moduleName,
		oper : (status == 1 ? 'disable' : 'enable'),
		params : {
			ids : ids
		}
	});
}

function firstUpper(str) {
	return str.substring(0, 1).toUpperCase() + str.substring(1);
}
/**
 * Ajax请求返回json数据结果统一封装
 * 
 * @param p
 *            参数项
 * @param callback
 *            回调函数
 */
function jsonResponse(p, callback) {
	var moduleName = p.moduleName ? p.moduleName : '', // 模块名称
	oper = p.oper ? p.oper : '', // 操作
	params = p.params ? p.params : {}, // ajax参数
	operChinese = '', moduleNameChinese = '', confirmMsg = ''; // 操作对应中文提示

	if (!moduleName || !oper) {
		console.log('ERR: 数据请求必要参数缺失')
		return false;
	}
	$.ajaxSettings.async = false;
	$.getJSON('js/emin/moduleName_en_cn.json', function(res) {
		for (var i = 0; i < res.length; i++) {
			if (res[i].key.toLowerCase() == moduleName.toLowerCase()) {
				moduleNameChinese = res[i].value;
			}
		}
	});
	$.getJSON('js/emin/oper_en_cn.json', function(res) {
		for (var i = 0; i < res.length; i++) {
			if (res[i].key.toLowerCase() == oper.toLowerCase()) {
				operChinese = res[i].value;
			}
		}
	});
	$.ajaxSettings.async = true;

	confirmMsg = p.confirmMsg ? p.confirmMsg
			: ('是否确认' + operChinese + moduleNameChinese);

	layer.confirm(confirmMsg, {
		icon : 3,
		btn : [ '确认', '取消' ]
	// 按钮
	}, function() {
		loading = layer.load();
		ajaxRequest({
			url : moduleName + '/' + oper + firstUpper(moduleName),
			data : params,
			type : "post",
		}, function(data) {
			if (typeof data == 'string') {
				data = JSON.parse(data);
			}
			if (!data.success) {
				layer.msg(moduleNameChinese + operChinese + "失败！"
						+ (data.message ? "错误提示：" + data.message : ""), {
					icon : 5
				});
			} else {
				layer.msg(moduleNameChinese + operChinese + "成功！", {
					icon : 6
				});
				if (callback) {
					callback();
				} else {
					goPage('index');
				}
			}
		})
	});
}
//仅上传1张时，显示上传图片
function showUploadImg(data,el) {
	$(el + ' div.upload-img').removeClass('hide');
	$(el + ' .upload-input').addClass('hide');
	$(el + ' .upload-input input').val(JSON.stringify(data));
	$(el + ' .upload-img img').attr('src',data.storage[2].fileStorageUrl);
}
//仅上传1张时，删除上传图片
function removeUploadImg(el) {
	$(el + ' div.upload-img').addClass('hide');
	$(el + ' .upload-input').removeClass('hide');
	$(el + ' .upload-input input').val('');
}
