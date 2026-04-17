function myAjax(method,url,formData){
    var reOut;

    $.ajax({
        type:method,
        url:url,
        dataType:"json",//后端返回给前端的数据格式
        data:formData,
        async:false,//不异步，即要求前后台同步，等待后台返回数据再向下执行
        success:function(re){

            reOut=re;
        },
        error:function(){alert("出错了33啊啊啊啊啊1");}

    });
    return reOut;
}

//专门写vue
function demo(re,div)
{
    var vm=new Vue({
        el:"#"+div,
        data:re
    });
}
