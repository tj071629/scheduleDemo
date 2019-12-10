var selectId;
var rolepriMap;

var Rolepri = {
	//加载页面时所要执行的函数
	loadPage:function(obj){

		rolepriMap = obj;
		
		Rolepri.buildGridTree();
	},
	//列表
	buildGridTree:function(){
		
		jQuery("#grid-table-rolepri").jqGrid({
			url:"admin/listForTree.json",
			datatype: 'json',     
			rowNum:10,
			rowList:[10,20,30],
			pager : "#grid-pager-rolepri",
			mtype: "POST",
			colNames: ["<center>操作</center>","权限名称","权限层级","权限URL","上级菜单"],
			colModel: [
			    /*{name: "id",width:200,key:true,hidden:true},*/
			    
			    {name: "id", index: 'id', width:15, edittype: 'checkbox', editoptions: { value: "true:false" },
				    editable: false, formatter: rolepriboxFormatter},
				    
			    {name: "text", width:100},
			    {name: "level",width:50},
			    {name: "uri",width:100},
			    {name: "parent",width:1,hidden:true}
			],
			treeGrid: true,
			autoScroll: true,
			treeGridModel: "adjacency",
			ExpandColumn: "text",
			treeIcons: {
				plus:'icon-plus',
				minus:'icon-minus'
			},
			autowidth: true,
			ExpandColClick: true,
			jsonReader: {
			    repeatitems: false
			},
			onSelectRow: function(id){
				selectId = id;
			},
			gridComplete : function(){
				/**
				 * 窗口缩放时，经动态变化宽度
				 */
				$(window).resize(function(){
					var winwidth=$('.page-content').width(); 	//当前页面的宽度
					$("#grid-table-rolepri").setGridWidth(winwidth);
				});
				
				/**
				 * 点击菜单边框收缩菜单时，动态变化表格宽度
				 */
				$("#bjui-sidebar").find("#lockNav").click(function(){
					var winwidth=$('#bjui-container').width(); 	//当前窗口中，一行的宽度
					$("#grid-table-rolepri").setGridWidth(winwidth);
				});
			}
		});
		
		var newHeight = $(window).height() - 245;
		$(".ui-jqgrid .ui-jqgrid-bdiv").css("cssText","height: "+newHeight+"px!important;");
		
	}
	
	
}

function rolepriboxFormatter(cellvalue, options, rowObject) {
	if(cellvalue != 0){
		return '<center><input type="checkbox"' + ((rolepriMap.get(cellvalue) && rolepriMap.get(cellvalue) == 'true') ? ' checked="checked"' : '') +
	       'onclick="rolepriclickevent(this.checked, ' + options.rowId + ')" /></center>';
	}else{
		return '';
	}
}  

function rolepriclickevent(checked, rowId) {
    if(checked){
		addPri(rowId);
    }else{
		removePri(rowId);
    }
}  

function addPri(priid){
	//alert(rolepriMap.get("roleid"));
	var url='admin/addrolepri';
	var params={roleid:rolepriMap.get("roleid"),priid:priid};
	var callback=function(rtndata){
		var success=rtndata.success;
		if(success){
		}
		
	}
	querydata(url,params,callback);
}

function removePri(priid){
	//alert(rolepriMap.get("roleid"));
	var url='admin/delrolepri';
	var params={roleid:rolepriMap.get("roleid"),priid:priid};
	var callback=function(rtndata){
		var success=rtndata.success;
		if(success){
		}
		
	}
	querydata(url,params,callback);
	
}