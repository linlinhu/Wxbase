/**
 * 初始化分类树
 */
var eminZtree = function(params, callback) {
	var zTree = null,
		ztreeId = params.id ? params.id : null,
		sync = params.sync ? params.sync : {},
		async = params.async ? params.async : {},
		idKey = params.idKey ? params.idKey : 'id',
		pIdKey = params.pIdKey ? params.pIdKey : 'pId',
		diyDom = params.diyDom ? params.diyDom : null,
		checkList = params.checkList ? params.checkList : [],
		chkDisabled = false,
		searchNodeList = [],
		searchNode = function(val) {
			updateNodeHighlight(false);
			searchNodeList = zTree.getNodesByParamFuzzy('name', val, null);
			if (val != "") {
				updateNodeHighlight(true);
			}
		},
		updateNodeHighlight = function(isHighlight) {
			for( var i=0, l=searchNodeList.length; i<l; i++) {
				searchNodeList[i].highlight = isHighlight;
				zTree.updateNode(searchNodeList[i]);
			}
		},
		expandNodes = function(nodes) {
			for (var i = 0; i < nodes.length; i++) {
	            zTree.expandNode(nodes[i], true); 
	            if (nodes[i].isParent && nodes[i].zAsync) {//存在子级
	            	expandNodes(nodes[i].children);//递归
	            }
			} 
		},
		setting = {
			view: {
				fontCss: function(treeId, treeNode) {
					return (!!treeNode.highlight) ? {color:"#5a98de", "font-weight":"bold"} : {color:"#333", "font-weight":"normal"};
				},
				addDiyDom: diyDom
			},
			data: {
				key: {
					title: "name"
				},
				simpleData: {
					enable: true,
					idKey: idKey,
					pIdKey: pIdKey
				}
			},
			callback: {
				onClick: function (event, treeId, treeNode, clickFlag) {
					callback(zTree, treeNode);
				},
				onCheck: function(event, treeId, treeNode, clickFlag) {
					callback(zTree, treeNode);
				},
				onAsyncSuccess: function (event, treeId, treeNode, msg) {
					if (async && async.expandAll) {
						expandNodes(zTree.getNodes());
					}
				}
			}
		};  

	
	function filter(treeId, parentNode, childNodes) {
		if (!childNodes) return null;
		for (var i=0, l=childNodes.length; i<l; i++) {
			childNodes[i].name = childNodes[i].name.replace(/\.n/g, '.');
			if (childNodes[i].id == 1) {
				childNodes[i].chkDisabled = true;
			}
			for (var j = 0; j < checkList.length; j++) {
				if (childNodes[i].id == checkList[j].id) {
					childNodes[i].checked = true;
				}
			}
		}
		return childNodes;
	}

	
	if (params.check) {
		chkDisabled = params.check.chkDisabled;
		setting.check = params.check;
	}
	
	if (ztreeId) { 
		var ztreeObj = $('#' + ztreeId),
			keywordObj = $('#' + ztreeId + 'Key');
		if (sync && sync.zNodes) { // 同步所有数据
			$.fn.zTree.init(ztreeObj, setting, sync.zNodes);	
			zTree = $.fn.zTree.getZTreeObj(ztreeId);
			if (sync.expandAll) {
				zTree.expandAll(true);
			}
			
			if (checkList.length == 1 && checkList[0].id) {
				var node = zTree.getNodeByParam(idKey, checkList[0].id, null);
				if (!$('#' + node.tId + '_a').hasClass('curSelectedNode')) {
					$('#' + node.tId + '_a').addClass('curSelectedNode');
				}
			}
			
			for(i = 0; i < checkList.length; i++) {
				if (checkList[i].id) {
					var node = zTree.getNodeByParam(idKey, checkList[i].id, null);
					zTree.checkNode(node, true, true);
				}
			}
			
		}
		
		if (async && async.url) { // 异步加载数据
			setting.async = {
				enable: true,
				url: async.url,
				autoParam:["id=parentId"],
				dataFilter: filter	
			}
			
			$.fn.zTree.init(ztreeObj, setting);	
			zTree = $.fn.zTree.getZTreeObj(ztreeId);
		}
		
		if (keywordObj && keywordObj.length == 1) {
			keywordObj.on('input', function(){
				searchNode(keywordObj.val());
			}).on('propertychange', function(){
				searchNode(keywordObj.val());
			});
		}
	}
}


function setCurSelected (ztreeId, nodeId) {
	var selectedEls = $('#' + ztreeId + ' .curSelectedNode'),
		zTree = $.fn.zTree.getZTreeObj(ztreeId);
	
	for (i = 0; i < selectedEls.length; i++) {
		$(selectedEls[i]).removeClass('curSelectedNode');
	}
	$('#' + nodeId + '_a').addClass('curSelectedNode');
	
}
function concatNodeName(ztree, node, showTitle) {
	if (node.parentTId) {
		node = ztree.getNodeByTId(node.parentTId);
		showTitle = node.name + '&nbsp;&gt;&nbsp;' + showTitle;
		return concatNodeName(ztree, node, showTitle)
	} else {
		return showTitle;
	}
}

function treeDisplay (treeName) {
	var filter = treeName + 'Filter',
		wrap = treeName + 'Wrap',
		showTree = function() {
			var select = $('#' + filter);
			if (select.hasClass('user-defined')) {
				$('#' + wrap).slideDown("fast");
			} else {
				var selectOffset = select.position();
				$('#' + wrap).css({
					left: selectOffset.left + "px", 
					top: selectOffset.top + select.outerHeight() + "px",
					width: select.css('width')
				}).slideDown("fast");

			}
			$("body").bind("mousedown", onBodyDown);
		},
		hideTree = function() {
			$('#' + wrap).fadeOut("fast");
			$("body").unbind("mousedown", onBodyDown);
		},
		onBodyDown = function(event) {
			if (!(event.target.id == wrap  || event.target.id == filter || $(event.target).parents('#' + wrap).length>0)) {
				hideTree();
			}
		};
		
	$('#' + filter).bind('click', showTree);
}
