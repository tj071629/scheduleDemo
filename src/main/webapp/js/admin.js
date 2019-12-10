function querydata(url,params,callback){
	$.ajax({
		type : "POST",
		url : url,
		data : params,
		contentType: "application/x-www-form-urlencoded; charset=utf-8", 
		async:true,
		dataType : "json",
		success : function(rtndata) {
			if(callback){
				callback(rtndata);
			}
		}
	});
}

function altmsg(msg,title,callback){
	var cfg={displayMode:'slide', displayPosition:'middlecenter', okName:'确定', cancelName:'no', title:title};
	if(callback){
		cfg.okCall=callback;
	}
	$("body").alertmsg('info',msg, cfg);
}