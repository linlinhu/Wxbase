var woaId = -1,
	wxMenus = [];

/**
 * 显示或隐藏公众号列表
 */
function toggleWxoaUlsDisplay(self) {
	var el = $('#wxMenuManage-woalist');
	if (el.hasClass('hide')) {
		el.removeClass('hide');
		$(self).find('i.fa').removeClass('fa-angle-down').addClass('fa-angle-up');
	} else {
		el.addClass('hide');
		$(self).find('i.fa').removeClass('fa-angle-up').addClass('fa-angle-down');
	}
}


/**
 * 设置全局变量wxMenus数据内容
 */
function setWxMenus() {
	wxMenus = [];
	$.ajaxSettings.async = false;
	loadWxMenus(0, function(res) {//加载第一级
		for (i = 0; i < res.length; i++) {
			wxMenus.push(res[i]);
			wxMenus[i]._index = i;
			wxMenus[i].subMenus = [];
			loadWxMenus(res[i].id, function(subRes) { //加载第二级
				for (j = 0; j < subRes.length; j++) {
					wxMenus[i].subMenus.push(subRes[j]);
					wxMenus[i].subMenus[j].pindex = i;
					wxMenus[i].subMenus[j]._index = j;
				}
			});
		}
	});
	renderWxMenus(wxMenus); // 渲染数据
	$.ajaxSettings.async = true;
	
}
/**
 * 设置选中微信公众号
 * @param self
 */
function setWxoaSelected(self) {
	woaId = $(self).attr('data-id'); //公众号id
	$('#wxMenu-woaName').html($(self).html());
	$(self).siblings('li').removeClass('selected');
	$(self).addClass('selected');
	toggleWxoaUlsDisplay($(self).parent());
	setWxMenus(); //设置微信自定义菜单数据
	renderWxMenuForm({empty: true});
}

/**
 * 添加，编辑微信自定义菜单数据
 * @param pindex 父级下标
 */
function addWxMenu(pindex) {
	var wxMenus_index = typeof pindex != 'undefined' ? pindex : -1, // 一级下标
		wxSubMenus_index = -1; // 二级下标
	
	if (wxMenus_index >= 0) { // 第二级的数据逻辑操作：将【当前操作数据对象】根据{父级下标}放进【父级菜单对象】的【子级数据集合】中
		for (i = 0; i < wxMenus.length; i++) {
			if (wxMenus[i]._index == wxMenus_index) {
				wxSubMenus_index = wxMenus[i].subMenus.length; // 二级下标赋值
				wxMenus[i].subMenus.push({
					pindex: wxMenus_index,
					_index: wxSubMenus_index,
					name: "子菜单名称", // 二级菜单默认名字
				})
			}
		}
		renderWxMenus(wxMenus); // 渲染菜单集合数据
		
		editWxMenu(pindex, wxSubMenus_index); // 编辑选中当前添加对象 
		
	} else {
		wxMenus_index = wxMenus.length; // 一级下标赋值
		wxMenus.push({
			_index: wxMenus_index,
			name: "菜单名称", // 一级菜单默认名称
			subMenus: [] // 数据对象包含子级菜单元素
		})
		
		renderWxMenus(wxMenus); // 渲染菜单集合数据
		
		editWxMenu(wxMenus_index); // 编辑选中当前添加对象 
	}
}

/**
 * 自动将表单中的数据对象保存到数据集合
 */
function autoSaveWxMenuForm() {
	var saveObj = $("#wxMenuForm").serializeObject(); // 获得表单的序列化对象
	
	if (saveObj.pindex.length > 0) { // 存在父级下标，表示当前对象是二级菜单
		if (!saveObj.name) { // 菜单名称为空判断
			saveObj.name = '子菜单名称';
		}
		// 根据父级下标和对象下标找到对应数据集合对象，进行覆盖操作
		wxMenus[saveObj.pindex].subMenus[saveObj._index] = saveObj;
	} else { // 父级下标为空，表示当前对象是一级菜单
		delete saveObj['pindex']; // 删除父级下标元素
		saveObj.subMenus = wxMenus[saveObj._index].subMenus; // 父级菜单继承子级菜单
		if (!saveObj.name) { // 菜单名称为空判断
			saveObj.name = '菜单名称';
		}
		// 如果子级菜单个数大于0，删除当前菜单的菜单内容
		if (saveObj.subMenus.length > 0) {
			delete saveObj['activetype'];
			delete saveObj['activekey'];
		}
		// 根据下标找到对应的数据集合对象，进行覆盖
		wxMenus[saveObj._index] = saveObj;
	}
}
/**
 * 编辑选中菜单对象
 * @param _index 一级下标
 * @param sub_index 二级下标
 */
function editWxMenu(_index, sub_index) {
	var wxMenu = null; // 定义当前编辑菜单对象
	
	if ($('#wxMenuForm').length == 1) { // 表单存在，将表单数据保存到wxMenus对象中去
		autoSaveWxMenuForm();
	}
	//清除选中
	$('.selected').removeClass('selected');
	$('.current').removeClass('current');
	
	if (_index >= 0) {
		if (typeof sub_index != 'undefined' && sub_index >= 0) { // 根据父级下标和本级下标找到对应的菜单对象
			wxMenu = wxMenus[_index].subMenus[sub_index];
			$('#menu_' + _index).addClass('selected'); // 选中父级
			$('#subMenu_' + _index + '' + sub_index).addClass('current'); // 设置当前选中编辑样式
		} else {
			wxMenu = wxMenus[_index];
			$('#menu_' + _index).addClass('current selected'); // 选中自己，弹出子级，并显示当前菜单编辑中的样式
		}
	}
	// 渲染菜单编辑表单
	renderWxMenuForm(wxMenu);
}
/**
 * 从数据集合中移除自定义菜单对象
 * @param idStr
 * '一级菜单下标' 
 * '一级菜单下标-二级菜单下标'
 */
function removeWxMenu(idStr) {
	var index = -1,
		sub_index = -1,
		removeId = -1;
	
	if (idStr.indexOf('-') >= 0) { // 移除二级菜单
		index = parseInt(idStr.split('-')[0]);
		sub_index = parseInt(idStr.split('-')[1]);
		// 删除确认
		layer.confirm('删除后,“' + wxMenus[index].subMenus[sub_index].name + '”菜单下设置的内容将被删除。',function(confirmIndex){
			layer.close(confirmIndex);
			// 如果存在id, 需要进行数据库交互操作，将id暂存全局变量数组中
			removeId = wxMenus[index].subMenus[sub_index].id;
			if (removeId) {
				$.ajaxSettings.async = false;
				deleteWxMenu(removeId, function(success){
					if (!success) {
						return false; // 不成功停止向下执行代码
					}
				})
				layer.msg('删除操作成功！', {icon: 6});
				$.ajaxSettings.async = true;
			}
			renderWxMenuForm({empty: true});
			// 执行数据集合删除操作
			wxMenus[index].subMenus.splice(sub_index, 1);
			// 刷新数据集合
			refreshWxMenus();
			// 编辑选中父级菜单
			editWxMenu(index);
		});
	} else { // 移除一级菜单
		index = parseInt(idStr);
		// 删除确认
		layer.confirm('删除后,“' + wxMenus[index].name + '”菜单下设置的内容' + (wxMenus[index].subMenus.length > 0 ? '以及子级菜单' : '') + '将被删除。',function(confirmIndex){
			layer.close(confirmIndex);
			removeId = wxMenus[index].id;
			// 如果存在id, 需要进行数据库交互操作，将id暂存全局变量数组中
			if (removeId) {
				$.ajaxSettings.async = false;
				for (i = 0; i < wxMenus[index].subMenus.length; i++) {
					deleteWxMenu(wxMenus[index].subMenus[i].id, function(success){
						if (!success) {
							return false; // 不成功停止向下执行代码
						}
					})
				}
				
				deleteWxMenu(removeId, function(success){
					if (!success) {
						return false; // 不成功停止向下执行代码
					}
				})
				layer.msg('删除操作成功！', {icon: 6});
				$.ajaxSettings.async = true;
			}
			renderWxMenuForm({empty: true});
			// 执行数据集合删除操作
			wxMenus.splice(index, 1);
			// 刷新数据集合
			refreshWxMenus();
			// 如果还存在其它菜单数据，编辑选中第一个父级菜单
			if (wxMenus.length > 0) {
				editWxMenu(0);
			}
		});
	}
}
/**
 * 刷新自定义菜单数据集合
 */
function refreshWxMenus() {
	// 重置下标
	for (i = 0; i < wxMenus.length; i++) {
		wxMenus[i]._index = i;
		for (j = 0; j < wxMenus[i].subMenus.length; j++) {
			wxMenus[i].subMenus[j].pindex = i;
			wxMenus[i].subMenus[j]._index = j;
		}
	}
	// 渲染数据
	renderWxMenus(wxMenus);
}
/**
 * 渲染自定义菜单界面
 */
function renderWxMenus(menus) {
	var tpl = wxMenuListDatas.innerHTML,//数据模板
		view = $('#wxMenuList');
	
	laytpl(tpl).render(menus, function(html){
		view.html(html);
	});
}

/**
 * 渲染form表单
 * @param menu
 */
function renderWxMenuForm(menu) {
	var tpl = wxMenuEditBoxData.innerHTML,//数据模板
		view = $('#wxMenuEditBox');
	
	laytpl(tpl).render(menu, function(html){
		view.html(html);
		if (!menu.empty) {
			// ichecks 初始化
			$('.i-checks').iCheck({
			    checkboxClass: 'icheckbox_square-green',
			    radioClass: 'iradio_square-green',
			});
			// 菜单名称绑定失焦事件
			$('#wxMenuForm input[name="name"]').bind('blur', function() {
				refreshWxMenuName(this);
			});
			
			// 添加表单验证
	        var icon = "<i class='fa fa-times-circle'></i> ";
			$("#wxMenuForm").validate({
		        rules: {
		            name: {
		                required: true,
		                stringMinLength: 8,
		                isContainsSpecialChar: true
		            },
		        },
		        messages: {
		        	name: {
		                required: icon + "菜单名称不能为空",
		                stringMinLength: icon + "请最多输入8个汉字或16个英文字符"
		            }
		        }
		    });
		}
	});
}
/**
 * 同步菜单名称
 * @param el
 */
function refreshWxMenuName(el) {
	setTimeout(function() {
		var menuIndex = $(el).attr('menu-index'),
			val = $(el).val(),
			pindex = -1,
			_index = -1;

		if ($(el).attr('aria-invalid')) {
			// 菜单名称不能超过8个中文字符,16个英文字符，如果违规，自动截取
			val = val ? substrByCharLen(val, 16) : '';
		}
		
		if (menuIndex.indexOf('-') > 0) {
			pindex = menuIndex.split('-')[0];
			_index = menuIndex.split('-')[1];
			// 更新数据集合对应对象的菜单名称
			wxMenus[pindex].subMenus[_index].name = val ? val : '子菜单名称';
			// 更新菜单界面的菜单名称
			$('#subMenu_' + pindex + _index).find('span.js_l2Title').html(val ? val : '子菜单名称');
		} else {
			_index = menuIndex;
			// 更新数据集合对应对象的菜单名称
			wxMenus[_index].name = val ? val : '菜单名称';
			// 更新菜单界面的菜单名称
			$('#menu_' + _index).find('span.js_l1Title').html(val ? val : '菜单名称');
		}
		if (val) {
			// 更新表单的菜单名称
			$(el).val(val);
			// 清除错误样式
			$($(el).parent().parent('.form-group')).removeClass('has-error').addClass('has-success');
			$(el).siblings('span').hide();
		}
	}, 500);
}

/**
 * 保存或发布
 */
function validSubmit() {
	// 数据验证
	if ($('#wxMenuForm').length == 1) { 
		// 当前表单的菜单内容为空提醒，不为空自动保存
    	var saveObj = $("#wxMenuForm").serializeObject();
    	if (saveObj.activekey && saveObj.activekey.length == 0) {
			layer.msg('请完善菜单内容！');
			return false;
    	} else {
    		autoSaveWxMenuForm();
    	}
	}
	var menuIslegal = true,
		menuKey = '',
		menuKeyReg = new RegExp(/http(s)?:\/\/([\w-]+\.)+[\w-]+(\/[\w- .\/?%&=]*)?/);
	// 循环数据集合对象，若菜单对象的菜单内容为空，编辑选中后提醒
	for (i = 0; i < wxMenus.length; i++) {
		menuIslegal = true;
		menuKey = wxMenus[i].activekey;
		if ((!wxMenus[i].subMenus || wxMenus[i].subMenus.length == 0) && (!menuKey || menuKey.length == 0)) {
			menuIslegal = false;
			layer.msg('请完善菜单内容！');
		}
		
		if (menuKey && wxMenus[i].activetype != 'click' && !menuKeyReg.test(menuKey)) {
			menuIslegal = false;
			layer.msg('菜单内容不合法（必须是一个网址链接）！');
		}
		if (!menuIslegal) {
			editWxMenu(i);
			return false;
		}
		for (j = 0; j < wxMenus[i].subMenus.length; j++) {
			menuKey = wxMenus[i].subMenus[j].activekey;
			if (!menuKey || menuKey.length == 0) {
				menuIslegal = false;
				layer.msg('请完善子菜单内容！');
			}
			
			if (menuKey && wxMenus[i].subMenus[j].activetype != 'click' && !menuKeyReg.test(menuKey)) {
				menuIslegal = false;
				layer.msg('子菜单内容不合法（必须是一个网址链接）！');
			}
			
			if (!menuIslegal) {
				editWxMenu(i, j);
				return false;
			}
		}
	}
	// 通过验证，开始进行数据库交互
	saveAndPulishWxMenus(wxMenus);
}
/**
 * 保存发布数据库交互
 */
function saveAndPulishWxMenus() {
	var wxMenu = {},
		isSuccess = true;
	$.ajaxSettings.async = false;
	for (i = 0 ; i < wxMenus.length; i++) { // 循环保存一级菜单
		wxMenu = {
			id: wxMenus[i].id,
			pid: '', // 父级id为空
			name: wxMenus[i].name,
			sort: wxMenus[i]._index,
			activetype: typeof wxMenus[i].activetype == 'undefined' ? 'view' : wxMenus[i].activetype,
			activekey: typeof wxMenus[i].activekey == 'undefined' ? 'http://www.none.com' : wxMenus[i].activekey,
			woaId: woaId
		};
		saveWxMenu(wxMenu, function(success, pid) {
			if (success) { // 父级菜单保存成功后，将返回父级id，以便进行二级菜单关联保存
				for (j = 0; j < wxMenus[i].subMenus.length; j++) {
					wxMenu = {
						id: wxMenus[i].subMenus[j].id,
						pid: pid, // 父级菜单保存成功后的id
						name: wxMenus[i].subMenus[j].name,
						sort: wxMenus[i].subMenus[j]._index,
						activetype: wxMenus[i].subMenus[j].activetype,
						activekey: wxMenus[i].subMenus[j].activekey,
						woaId: woaId
					};
					saveWxMenu(wxMenu, function(success) {
						if (!success) {
							isSuccess = false;
							return false;
						}
					});
				}
			} else {
				isSuccess = false;
				return false;
			}
		});
	}
	if (isSuccess) {
		// 发布菜单
		publishWxMenus(function(success){
			if (!success) {
				isSuccess = false;
			}
		});
		
	}
	
	if (isSuccess) {
		// 所有交互完美执行，提示操作成功
		layer.msg('操作成功！', {icon: 6});
		$.ajaxSettings.async = true;
		setWxMenus();
		renderWxMenuForm({empty: true});
	}
	
}

/**
 * 加载微信自定义菜单集合
 * @param pid 父级id，第一级传0
 * @param callback 回调函数
 */
function loadWxMenus(pid, callback) {
	ajaxRequest({
		url: base + 'wxMenu/load',
		data: {
			woaId: woaId,
			pid: pid ? pid : 0
		}
	}, function(res) {
		if (!res.success) {
			layer.msg("加载菜单失败！" + (res.message ? "错误提示：" + res.message : ""), {icon: 5});
		} else {
			callback(res.result);
		}
	})
}

/**
 * 保存微信自定义菜单
 * @param wxMenu 单个自定义菜单对象
 * @param callback 回调函数
 */
function saveWxMenu(wxMenu, callback) {
	ajaxRequest({
		url: base + 'wxMenu/saveWxMenu',
		data: {
			jsonStr: JSON.stringify(wxMenu)
		}
	}, function(res) {
		if (!res.success) {
			layer.msg("保存" + (wxMenu.pid != '' ? '子' : '') + "菜单【" + wxMenu.name + "】失败！" + (res.message ? "错误提示：" + res.message : ""), {icon: 5});
			callback(false, null);
		} else {
			callback(true, res.result ? res.result : '');
		}
	})
}

/**
 * 发布微信自定义菜单
 * @param callback
 */
function publishWxMenus(callback) {
	ajaxRequest({
		url: base + 'wxMenu/generate',
		data: {
			woaId: woaId
		}
	}, function(res) {
		if (!res.success) {
			layer.msg("发布菜单失败！" + (res.message ? "错误提示：" + res.message : ""), {icon: 5});
			callback(false);
		} else {
			callback(true);
		}
	})
}
/**
 * 删除微信自定义菜单
 * @param id 菜单id
 * @param callback 回调函数
 */
function deleteWxMenu(id, callback) {
	ajaxRequest({
		url: base + 'wxMenu/deleteWxMenu',
		data: {
			id: id
		}
	}, function(res) {
		if (!res.success) {
			layer.msg("删除菜单失败！" + (res.message ? "错误提示：" + res.message : ""), {icon: 5});
		} else {
			callback();
		}
	})
}