function afterLoadPageForm(formId,tableName,context,formUrl){
	$(".submitButton").bind("click",function(){
		//提交之前设置一些验证，根据输入的条件动态判断哪些控件需要做什么限制
		addValidateBefore();
		//防止表单重复提交，点击的时候就让所有提交按钮禁用
		$(".submitButton").attr("disabled","disabled");
		setFormSelect();
		$("#submitFlag").val($(this).attr("id"));
		var validate = $("#"+formId).form('validate'); 
	    if (!validate) {  
	        $.iMessager.alert({title:'提示',msg:'请正确填写表单！',icon: 'warm',
                fn:function(){
                	$("#"+formId).find(".validatebox-invalid:first").focus();   
                }
            });
	        return false ; 
	    }  
		if(!otherValidate())
		{
			return;
		}
		$.iMessager.progress({
            text: "正在操作...",top:$(document).scrollTop()+200
        });
	 	removeDisable();
	    $('#'+formId).ajaxSubmit(function(data){
		    $.iMessager.progress("close");
		    if(data)
		  	{
		  		if(typeof data == "string")
		  		{
		  			data = $.parseJSON(data.replace(/<.*?>/ig,""));
		  		}
		  		if(data.status)
		  		{
		  			$.iMessager.alert({title:'提示',msg:data.message,icon: 'info',top:150,
                        fn:function(){
                    		var refresh_tab = parent.$('#index_tabs').iTabs('getSelected');
                            var refresh_iframe = refresh_tab.find('iframe')[0];
                            refresh_iframe.src=formUrl+"id="+data.entityId;
                    		window.location.href=formUrl+"id="+data.entityId;
                        }
                    });
		  		}
		  		else
		  		{
		  			$.iMessager.alert({title: data.title, msg: data.message,top:150});
		  			showDisable();
		  		}
		  	}else
			{
				$.iMessager.alert({title: "提示", msg: "操作失败！",top:150});
			}
	 	});
	}); 
	
	$(document).click(function(e){                         
	    $(".thisDivNeedToHide").hide();  
	    var allDiv = $(".thisDivNeedToHide");
	    if(allDiv && allDiv.length>0)
	    {
	    	for(var i=0;i<allDiv.length;i++)
	    	{
	    		var name = $(allDiv[i]).attr("title");
	    		var idValue = $("#"+name+"Ids").val();
	    		if(!idValue || ""==idValue)
	    		{
	    			$("#"+name+"Names").val("");
	    		}
	    		var idValueS = $("#"+name+"Id").val();
	    		if(!idValueS || ""==idValueS)
	    		{
	    			$("#"+name+"Name").val("");
	    		}
	    	}
	    }
	});
	intiAutoHeight();
	initCompany("officeName","officeId",context);
}