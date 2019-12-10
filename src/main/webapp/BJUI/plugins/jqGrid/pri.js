var selectId;

var Pri = {
	//加载页面时所要执行的函数
	loadPage:function(){
		Pri.buildGridTree();
		
		//新增按钮
		$('#newpri').click(function(){
			
			if (selectId == null){
				altmsg("请选择一条除根目录以外的记录！","提示",function(){});
			}else{
				$('#pri-input').removeData("bs.modal");
				if (!selectId){
					selectId = "";
				}
				
				$('#pri-input').modal({
					remote:'admin/inputNew?timestamp=' + new Date().getTime() + '&selectId=' + selectId,
					backdrop:'static'
				});
				$('#newpri').button('loading');
			}
		});
		//修改按钮
		$('#editpri').click(function(){
			
			if (selectId == null || selectId == 0){
				altmsg("请选择一条除根目录以外的记录！","提示",function(){});
			}else{
				$('#pri-input').removeData("bs.modal");
				
				$('#pri-input').modal({
					remote:'admin/inputEdit?priId=' + selectId + '&timestamp=' + new Date().getTime(),
					backdrop:'static'
				});
				
				$('#editpri').button('loading');
			}
		});
		
		//删除按钮
		$('#deletepri').click(function(){
			
			if (selectId == null || selectId == 0){
				altmsg("请选择一条除根目录以外的记录！","提示",function(){});
				
			}else{
		    	
		    	$("body").alertmsg('confirm', '系统将会级联删除当前节点及其下所属所有权限，确定删除吗？', {
		    		okCall : function() {
		    			
						var url='admin/deletepri';
				    	var params = {delIds:selectId};
				    	
				    	var callback=function(rtndata){
				    		
				    		var success=rtndata.success;
				    		if(success){
				    			
				    			altmsg("删除成功！","删除成功",function(){
				    			});
				    			
				    			$('#bjui-navtab').navtab('reloadFlag', 'prilist');
				    			
				    		}else{
				    			
				    			$("body").alertmsg('error', rtndata.msg, {displayMode:'slide', displayPosition:'middlecenter', okName:'Yes', cancelName:'no', title:'错误'})
				    			
				    		}
				    	}
				    	
				    	querydata(url,params,callback); 
		    			
		    		}
		    	})
				
			}
		});
	},
	
	//加载新增修改弹出窗口
	loadInputPage: function(){
		$('#newpri').button('reset');
		$('#editpri').button('reset');
		
		if ($("#editId").val() != 0){
			$("#input-title").html("编辑权限");
			
			//加载父节点编号显示，本身的编号放在文本框中
		}else{
			$("#input-title").html("添加权限");
		}
		
		//校验表单
	    $('#pri_fmt').validator({
	    	rules: {
	            //自定义一个规则，用来代替remote（注意：要把$.ajax()返回出来）
	            myRemote: function(element){
	                return $.ajax({
	                    url: 'admin/isexistpriname',
	                    type: 'post',
	                    data: 'priname='+$("#priname").val()+'&id='+$("#editId").val(),
	                    dataType: 'text',
	                    success: function(d){
	                    }
	                });
	            }
	        },
	        fields: {
	            'priname': 'required; myRemote;',
	            'priorder': 'required; integer; range[1~100];'
	        },
	        valid: function(form){
	        	//触发提交后台操作
	        	savepri();
	        }
	    }).on("click", "#savepri", function(e){
	        $(e.delegateTarget).trigger("validate");
	        
	    }).on("click", "#closepri", function(e){
	    	//关闭modal蒙版
			$('#pri-input').modal('hide');
	    });


	     function savepri(){
	    	
	    	var url='admin/savepri';
	    	var params= $('#pri_fmt').serialize();
	    	
	    	var callback=function(rtndata){
	    		
	    		var success=rtndata.success;
	    		if(success){
	    			
	    			altmsg("保存成功！","保存成功",function(){
	    			});
	    			
	    			$('#bjui-navtab').navtab('reloadFlag', 'prilist');
	    			//setTimeout(prition() { $('#bjui-navtab').navtab('reloadFlag', 'priindex') }, 100)
	    			
	    			//关闭modal蒙版
	    			$('#pri-input').modal('hide');
	    			
	    		}
	    	}
	    	
	    	querydata(url,params,callback); 

	    }
	    
	},
	//获取数字格式
	getNumber:function(obj){
	   if (obj==null) return 0;
	   if (!Pri.checkFloat(obj)||obj+""==""){
	    return 0;
	   }else{
	        return parseFloat(""+obj);
	   }
	},
	//列表
	buildGridTree:function(){
		
		jQuery("#grid-table-pri").jqGrid({
			url:"admin/listForTree.json",
			datatype: 'json',     
			rowNum:10,
			rowList:[10,20,30],
			pager : "#grid-pager-pri",
			mtype: "POST",
			colNames: ["","权限名称","权限层级","权限URL","上级菜单"],
			colModel: [
			    {name: "id",width:200,key:true,hidden:true},
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
			caption: "权限列表",
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
					$("#grid-table-pri").setGridWidth(winwidth);
					/*$('.ui-jqgrid-bdiv').css('width',winwidth+1);*/
				});
				
				/**
				 * 点击菜单边框收缩菜单时，动态变化表格宽度
				 */
				$("#bjui-sidebar").find("#lockNav").click(function(){
					var winwidth=$('#bjui-container').width(); 	//当前窗口中，一行的宽度
					$("#grid-table-pri").setGridWidth(winwidth);
					/*$('.ui-jqgrid-bdiv').css('width',winwidth+1);*/
					$('.tooltip-inner').hide();
				});
			}
		});
		
		var newHeight = $(window).height() - 240;
		$(".ui-jqgrid .ui-jqgrid-bdiv").css("cssText","height: "+newHeight+"px!important;");
	}
}
