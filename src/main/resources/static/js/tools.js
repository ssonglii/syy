
//查看是否已点赞
function isLiked(aid){
    const uid = sessionStorage.getItem('uid');
    //const uid="1";
    //alert(uid);
    //alert(aid);
    var data0={};
    data0.aid=aid;
    data0.uid=uid;
    var re0=null;
    $.ajax({
        url:"/findActivityLikes",
        type:"post",
        dataType:"json",
        async:false,
        contentType:"application/json;charset=UTF-8",
        data:JSON.stringify(data0),
        success:function(re){
            // 判断是否存在点赞记录
            const hasLiked = re && re.length > 0; // 假设返回数组，有元素表示已点赞
            // 更新图片
            var keyword="like-img";
            //alert(keyword);
            const likeImg = document.getElementById(keyword);
            const likeContainer=document.getElementById("like-container");
            if (likeImg) {
                likeImg.src = hasLiked
                    ? "../static/image/img/已点赞-copy.png"  // 已点赞状态
                    : "../static/image/img/点赞 (2).png";    // 未点赞状态
            }
            if(likeContainer){
                likeContainer.setAttribute('data-tooltip', hasLiked ? '取消点赞' : '点赞');
            }
        },error:function (re2){
            alert("失败");
        }
    })
}

//查看是否已点赞
function isLiked1(aid){
    const uid = sessionStorage.getItem('uid');
    var data0={};
    data0.aid=aid;
    data0.uid=uid;
    var re0=null;
    $.ajax({
        url:"/findActivityLikes",
        type:"post",
        dataType:"json",
        async:false,
        contentType:"application/json;charset=UTF-8",
        data:JSON.stringify(data0),
        success:function(re){
            // 判断是否存在点赞记录
            const hasLiked = re && re.length > 0; // 假设返回数组，有元素表示已点赞
            // 更新图片
            var keyword=`like-img-${aid}`;
            //alert(keyword);
            var keyword0=`like-container-${aid}`;
            const likeImg = document.getElementById(keyword);
            const likeContainer=document.getElementById(keyword0);
            if (likeImg) {
                likeImg.src = hasLiked
                    ? "../static/image/img/已点赞-copy.png"  // 已点赞状态
                    : "../static/image/img/点赞 (2).png";    // 未点赞状态
            }
            if(likeContainer){
                likeContainer.setAttribute('data-tooltip', hasLiked ? '取消点赞' : '点赞');
            }
        },error:function (re2){
            alert("失败");
        }
    })
}

//查看评论是否已点赞
function isLiked0(fbid,likeImgId,likeContainerId){
    const uid = sessionStorage.getItem('uid');
    var data0={};
    data0.fbid=fbid;
    data0.uid=uid;
    var re0=null;
    $.ajax({
        url:"/findCommentsLikes",
        type:"post",
        dataType:"json",
        async:false,
        contentType:"application/json;charset=UTF-8",
        data:JSON.stringify(data0),
        success:function(re){
            // 判断是否存在点赞记录
            const hasLiked = re && re.length > 0; // 假设返回数组，有元素表示已点赞
            // 更新图片
            const likeImg = document.getElementById(likeImgId);
            const likeContainer=document.getElementById(likeContainerId);
            if (likeImg) {
                likeImg.src = hasLiked
                    ? "../static/image/img/已点赞-copy.png"  // 已点赞状态
                    : "../static/image/img/点赞 (2).png";    // 未点赞状态
            }
            if(likeContainer){
                likeContainer.setAttribute('data-tooltip', hasLiked ? '取消点赞' : '点赞');
            }
        },error:function (re2){
            alert("失败");
        }
    })
}

//查看是否已收藏
function isCollected(aid){
    const uid = sessionStorage.getItem('uid');
    var data0={};
    data0.aid=aid;
    data0.uid=uid;
    var re0=null;
    $.ajax({
        url:"/findActivityCollects",
        type:"post",
        dataType:"json",
        async:false,
        contentType:"application/json;charset=UTF-8",
        data:JSON.stringify(data0),
        success:function(re){
            // 判断是否存在收藏记录
            const hasCollected = re && re.length > 0; // 假设返回数组，有元素表示已收藏

            // 更新图片
            const favoriteImg = document.getElementById("favorite-img");
            const favoriteContainer = document.getElementById("favorite-container");
            if (favoriteImg) {
                favoriteImg.src = hasCollected
                    ? "../static/image/img/收藏-已收藏.png"  // 已收藏状态
                    : "../static/image/img/收藏.png";        // 未收藏状态
            }
            if (favoriteContainer) {
                // 根据收藏状态更新提示文本
                favoriteContainer.setAttribute('data-tooltip', hasCollected ? '取消收藏' : '收藏');
            }
        },error:function (re2){
            alert("失败");
        }
    })
}

//查看是否已收藏
function isCollected1(aid){
    const uid = sessionStorage.getItem('uid');
    var data0={};
    data0.aid=aid;
    data0.uid=uid;
    var re0=null;
    $.ajax({
        url:"/findActivityCollects",
        type:"post",
        dataType:"json",
        async:false,
        contentType:"application/json;charset=UTF-8",
        data:JSON.stringify(data0),
        success:function(re){
            // 判断是否存在收藏记录
            const hasCollected = re && re.length > 0; // 假设返回数组，有元素表示已收藏

            // 更新图片
            const favoriteImg = document.getElementById(`favorite-img-${aid}`);
            const favoriteContainer = document.getElementById(`favorite-container-${aid}`);
            if (favoriteImg) {
                favoriteImg.src = hasCollected
                    ? "../static/image/img/收藏-已收藏.png"  // 已收藏状态
                    : "../static/image/img/收藏.png";        // 未收藏状态
            }
            if (favoriteContainer) {
                // 根据收藏状态更新提示文本
                favoriteContainer.setAttribute('data-tooltip', hasCollected ? '取消收藏' : '收藏');
            }
        },error:function (re2){
            alert("失败");
        }
    })
}

//活动点赞提交
function likeActivity(aid){
    const uid = sessionStorage.getItem('uid');
    var data0={};
    data0.aid=aid;
    data0.uid=uid;
    var re0=null;
    $.ajax({
        url:"/addActivityLikes",
        type:"post",
        dataType:"json",
        async:false,
        contentType:"application/json;charset=UTF-8",
        data:JSON.stringify(data0),
        success:function(re){
            re0=re;
            document.getElementById("like-img").src="../static/image/img/已点赞-copy.png";
            document.getElementById("like-container").setAttribute('data-tooltip', '取消点赞');
        },error:function (re2){
            delActivityLikes(aid);
            //alert("失败");
        }
    })
}

//活动点赞提交
function likeActivity0(aid){
    const uid = sessionStorage.getItem('uid');

    var data0={};
    data0.aid=aid;
    data0.uid=uid;
    var re0=null;
    $.ajax({
        url:"/addActivityLikes",
        type:"post",
        dataType:"json",
        async:false,
        contentType:"application/json;charset=UTF-8",
        data:JSON.stringify(data0),
        success:function(re){
            re0=re;
            var keyword=`like-img-${aid}`;
            //alert(keyword);
            var keyword0=`like-container-${aid}`;
            document.getElementById(keyword).src="../static/image/img/已点赞-copy.png";
            document.getElementById(keyword0).setAttribute('data-tooltip', '取消点赞');
            updateCounts(aid);
        },error:function (re2){

            delActivityLikes0(aid);
            updateCounts(aid);
        }
    })
}

function updateCounts(aid){
    //const uid = sessionStorage.getItem('uid');
    //const uid="1";
    var data0={};
    data0.aid=aid;
    //data0.uid=uid;
    $.ajax({
        url:"/findCounts",
        type:"post",
        dataType:"json",
        async:false,
        contentType:"application/json;charset=UTF-8",
        data:JSON.stringify(data0),
        success:function(re){
            re.forEach(re0=>{
                const container = document.querySelector(`.icon-tooltip[data-aid="${aid}"][data-action="like"]`);
                const countEl = container.querySelector('.like-count');
                countEl.textContent=re0.likes;
                const container1 = document.querySelector(`.icon-tooltip[data-aid="${aid}"][data-action="favorite"]`);
                const countEl1 = container1.querySelector('.favorite-count');
                countEl1.textContent=re0.collections;
            })
        },error:function (re2){
            alert("失败");
        }
    })
}
//评论点赞提交
function likeComments(fbid){
    const uid = sessionStorage.getItem('uid');
    //alert(fbid);
    var data0={};
    data0.fbid=fbid;
    data0.uid=uid;
    var re0=null;
    $.ajax({
        url:"/addCommentsLikes",
        type:"post",
        dataType:"json",
        async:false,
        contentType:"application/json;charset=UTF-8",
        data:JSON.stringify(data0),
        success:function(re){
            //re0=re;
            // alert(re0);
            document.getElementById(`like-img-${fbid}`).src="../static/image/img/已点赞-copy.png";
            document.getElementById(`like-container-${fbid}`).setAttribute('data-tooltip', '取消点赞');
        },error:function (re2){
            delCommentsLikes(fbid);
            //alert("失败");
        }
    })
}

//取消活动点赞
function delActivityLikes(aid){
    const uid = sessionStorage.getItem('uid');
    var data0={};
    data0.aid=aid;
    data0.uid=uid;
    var re0=null;
    $.ajax({
        url:"/delActivityLikes",
        type:"post",
        dataType:"json",
        async:false,
        contentType:"application/json;charset=UTF-8",
        data:JSON.stringify(data0),
        success:function(re){
            re0=re;
            document.getElementById("like-img").src="../static/image/img/点赞 (2).png";
            document.getElementById("like-container").setAttribute('data-tooltip', '点赞');
        },error:function (re2){
            alert("失败");
        }
    })
}

//取消活动点赞
function delActivityLikes0(aid){
    const uid = sessionStorage.getItem('uid');

    var data0={};
    data0.aid=aid;
    data0.uid=uid;
    var re0=null;
    $.ajax({
        url:"/delActivityLikes",
        type:"post",
        dataType:"json",
        async:false,
        contentType:"application/json;charset=UTF-8",
        data:JSON.stringify(data0),
        success:function(re){
            re0=re;
            var keyword=`like-img-${aid}`;
            //alert(keyword);
            var keyword0=`like-container-${aid}`;
            document.getElementById(keyword).src="../static/image/img/点赞 (2).png";
            document.getElementById(keyword0).setAttribute('data-tooltip', '点赞');
        },error:function (re2){
            alert("失败");
        }
    })
}

//取消评论点赞
function delCommentsLikes(fbid){
    const uid = sessionStorage.getItem('uid');
    //alert("取消评论点赞");
    var data0={};
    data0.fbid=fbid;
    data0.uid=uid;
    var re0=null;
    $.ajax({
        url:"/delCommentsLikes",
        type:"post",
        dataType:"json",
        async:false,
        contentType:"application/json;charset=UTF-8",
        data:JSON.stringify(data0),
        success:function(re){
            re0=re;
            //alert(re0);
            document.getElementById(`like-img-${fbid}`).src="../static/image/img/点赞 (2).png";
            document.getElementById(`like-container-${fbid}`).setAttribute('data-tooltip', '点赞');
        },error:function (re2){
            alert("失败");
        }
    })
}

//活动收藏提交
function favoriteActivity(aid){
    const uid = sessionStorage.getItem('uid');
    var data0={};
    data0.aid=aid;
    data0.uid=uid;
    var re0=null;
    $.ajax({
        url:"/addActivityCollects",
        type:"post",
        dataType:"json",
        async:false,
        contentType:"application/json;charset=UTF-8",
        data:JSON.stringify(data0),
        success:function(re){
            re0=re;
            document.getElementById("favorite-img").src="../static/image/img/收藏-已收藏.png";
            document.getElementById("like-favorite").setAttribute('data-tooltip', '取消收藏');
        },error:function (re2){
            delActivityCollects(aid);
            //alert("失败");
        }
    })
}

//活动收藏提交
function favoriteActivity0(aid){
    const uid = sessionStorage.getItem('uid');

    var data0={};
    data0.aid=aid;
    data0.uid=uid;
    var re0=null;
    $.ajax({
        url:"/addActivityCollects",
        type:"post",
        dataType:"json",
        async:false,
        contentType:"application/json;charset=UTF-8",
        data:JSON.stringify(data0),
        success:function(re){
            re0=re;
            var keyword=`favorite-img-${aid}`;
            //alert(keyword);
            var keyword0=`favorite-container-${aid}`;
            document.getElementById(keyword).src="../static/image/img/收藏-已收藏.png";
            document.getElementById(keyword0).setAttribute('data-tooltip', '取消收藏');
            updateCounts(aid);
        },error:function (re2){
            delActivityCollects0(aid);
            updateCounts(aid);
            //alert("失败");
        }
    })
}

//取消活动收藏
function delActivityCollects(aid){
    const uid = sessionStorage.getItem('uid');
    var data0={};
    data0.aid=aid;
    data0.uid=uid;
    var re0=null;
    $.ajax({
        url:"/delActivityCollects",
        type:"post",
        dataType:"json",
        async:false,
        contentType:"application/json;charset=UTF-8",
        data:JSON.stringify(data0),
        success:function(re){
            re0=re;
            document.getElementById("favorite-img").src="../static/image/img/收藏.png";
            document.getElementById("favorite-container").setAttribute('data-tooltip', '收藏');
        },error:function (re2){
            alert("失败");
        }
    })
}

//取消活动收藏
function delActivityCollects0(aid){
    const uid = sessionStorage.getItem('uid');
    var data0={};
    data0.aid=aid;
    data0.uid=uid;
    var re0=null;
    $.ajax({
        url:"/delActivityCollects",
        type:"post",
        dataType:"json",
        async:false,
        contentType:"application/json;charset=UTF-8",
        data:JSON.stringify(data0),
        success:function(re){
            re0=re;
            var keyword=`favorite-img-${aid}`;
            //alert(keyword);
            var keyword0=`favorite-container-${aid}`;
            document.getElementById(keyword).src="../static/image/img/收藏.png";
            document.getElementById(keyword0).setAttribute('data-tooltip', '收藏');
        },error:function (re2){
            alert("失败");
        }
    })
}