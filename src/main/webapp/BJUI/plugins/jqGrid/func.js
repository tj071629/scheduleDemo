var selectId;

var Func = {
	//加载页面时所要执行的函数
	loadPage:function(){
		Func.buildGridTree();
		
		//新增按钮
		$('#newfunc').click(function(){
			
			if (selectId == null){
				altmsg("请选择一条除根目录以外的记录！","提示",function(){});
			}else{
				$('#func-input').removeData("bs.modal");
				if (!selectId){
					selectId = "";
				}
				
				$('#func-input').modal({
					remote:'price/inputNew?timestamp=' + new Date().getTime() + '&selectId=' + selectId,
					backdrop:'static'
				});
				$('#newfunc').button('loading');
			}
		});
		//修改按钮
		$('#editfunc').click(function(){
			
			if (selectId == null || selectId == 0){
				altmsg("请选择一条除根目录以外的记录！","提示",function(){});
			}else{
				$('#func-input').removeData("bs.modal");
				
				$('#func-input').modal({
					remote:'price/inputEdit?funcId=' + selectId + '&timestamp=' + new Date().getTime(),
					backdrop:'static'
				});
				
				$('#editfunc').button('loading');
			}
		});
		//删除按钮
		$('#deletefunc').click(function(){
			
			if (selectId == null || selectId == 0){
				altmsg("请选择一条除根目录以外的记录！","提示",function(){});
				
			}else{
		    	
		    	$("body").alertmsg('confirm', '系统将会级联删除当前节点及其下所属所有功能项，确定删除吗？', {
		    		okCall : function() {
		    			
						var url='price/deletefunc';
				    	var params = {delIds:selectId};
				    	
				    	var callback=function(rtndata){
				    		
				    		var success=rtndata.success;
				    		if(success){
				    			
				    			altmsg("删除成功！","删除成功",function(){
				    			});
				    			
				    			$('#bjui-navtab').navtab('reloadFlag', 'funcindex');
				    			
				    		}else{
				    			
				    			$("body").alertmsg('error', '删除失败，请联系管理员',{displayMode:'slide', displayPosition:'middlecenter', okName:'Yes', cancelName:'no', title:'错误'})
				    			
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
		$('#newfunc').button('reset');
		$('#editfunc').button('reset');
		
		if ($("#funcEditId").val() != 0){
			$("#input-title").html("编辑功能");
		}else{
			$("#input-title").html("添加功能");
		}
		
		//校验表单
	    $('#func_fmt').validator({
	    	rules: {
	            //自定义一个规则，用来代替remote（注意：要把$.ajax()返回出来）
	            myRemote: function(element){
	                return $.ajax({
	                    url: 'price/isexistfunccode',
	                    type: 'post',
	                    data: 'funcCode='+$("#funcCode").val()+'&id='+$("#funcEditId").val(),
	                    dataType: 'text',
	                    success: function(d){
	                    }
	                });
	            }
	        },
	        fields: {
	            'funcName': 'required; ',
	            'funcCode': 'required; myRemote;',
	        },
	        valid: function(form){
	        	//触发提交后台操作
	        	savefunc();
	        }
	    }).on("click", "#savefunc", function(e){
	        $(e.delegateTarget).trigger("validate");
	    }).on("click", "#closefunc", function(e){
	    	//关闭modal蒙版
	    	$('#func-input').modal('hide');
	    });


	     function savefunc(){
	    	
	    	var url='price/savefunc';
	    	var params= $('#func_fmt').serialize();
	    	
	    	var callback=function(rtndata){
	    		
	    		var success=rtndata.success;
	    		
	    		if(success){
	    			
	    			altmsg("保存成功！","保存成功",function(){
	    			});
	    			
	    			$('#bjui-navtab').navtab('reloadFlag', 'funcindex');
	    			//setTimeout(function() { $('#bjui-navtab').navtab('reloadFlag', 'funcindex') }, 100)
	    			
	    			//关闭modal蒙版
	    			$('#func-input').modal('hide');
	    			
	    		}
	    	}
	    	
	    	querydata(url,params,callback); 

	    }
	    
	},
	//获取数字格式
	getNumber:function(obj){
	   if (obj==null) return 0;
	   if (!Func.checkFloat(obj)||obj+""==""){
	    return 0;
	   }else{
	        return parseFloat(""+obj);
	   }
	},
	//检查输入参数是否为浮点数
	checkFloat:function(str){
	    var rc=true;
	    oneDecimal=false;
	    if (str+"" == "undefined" || str == null || str==''){
	    rc=false;
	} else{
	    for(i=0;i<str.length;i++){
	        ch=str.charAt(i);
	        if(i==0 && ch=='-'){
	            continue;
	        }
	        if(ch=="." && !oneDecimal){
	        oneDecimal=true;
	            continue;
	        }
	        if(ch==","){
	            continue;
	        }
	        if ((ch< "0") || (ch >'9')){
	                rc=false;
	                break;
	            }
	        }
	    }
	    return rc;
	},
	//列表
	buildGridTree:function(){
		
		jQuery("#grid-table-func").jqGrid({
			url:"price/listForTree.json",
			datatype: 'json',     
			rowNum:10,
			rowList:[10,20,30],
			pager : "#grid-pager-func",
			mtype: "POST",
			colNames: ["","功能名称","功能顺序","功能编码","功能URL","上级菜单"],
			multiselect: true,
			colModel: [
				/*{name: 'text', index: 'text', edittype: 'checkbox', editoptions: { value: "True:False" },
				    editable: true, formatter: cboxFormatter, unformat: cboxUnFormatter},*/
				    
			    {name: "id", index: 'id', hidden:true},
			    
			    {name: 'text', index: 'text', width:150},
			    {name: "funcOrder",width:50},
			    {name: "funcCode",width:80},
			    {name: "funcPath",width:250},
			    {name: "parent",width:1,hidden:true}
			],
			treeGrid: true,
			autoScroll: false,
			treeGridModel: "adjacency",
			ExpandColumn: "text",
			treeIcons: {
				plus:'icon-plus',
				minus:'icon-minus'
			},
			caption: "功能列表",
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
					$("#grid-table-func").setGridWidth(winwidth);
				});
				
				/**
				 * 点击功能边框收缩功能时，动态变化表格宽度
				 */
				$("#bjui-sidebar").find("#lockNav").click(function(){
					var winwidth=$('#bjui-container').width(); 	//当前窗口中，一行的宽度
					$("#grid-table-func").setGridWidth(winwidth);
					$('.tooltip-inner').hide();
				});
			}
		});
		
		var newHeight = $(window).height() - 240;
		$(".ui-jqgrid .ui-jqgrid-bdiv").css("cssText","height: "+newHeight+"px!important;");
	}
}
