<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta content="width=device-width,initial-scale=1.0,maximum-scale=1.0,user-scalable=no" name="viewport">
<meta content="yes" name="apple-mobile-web-app-capable">
<meta content="black" name="apple-mobile-web-app-status-bar-style">
<meta content="telephone=no" name="format-detection">
<meta content="email=no" name="format-detection">
<title>${title}</title>
<script src="js/jquery.js" type="text/javascript"></script>
<link rel="stylesheet" href="css/prepay.css?v=22">
<link rel="stylesheet" href="css/weui.css">
<link rel="stylesheet" href="css/jquery-weui-0.8.3.css">
<style type="text/css">
.error {
	color: red;
	font-size: 15px;
	margin-top:5%;
}


</style>
<script src="js/wxpublic/jquery-weui.min.js"></script>
<script type="text/javascript" src="js/tq.js"></script>
<script type="text/javascript">
	
	window.onerror=reportError;
	function reportError(sMsg,sUrl,sLine){
		var oErrorLog="";
		oErrorLog +="Error: " + sMsg + ",";
		oErrorLog +="Line: " + sLine + ",";
		oErrorLog +="URL: " + sUrl;
		sendError(oErrorLog);
	}
	//每次添加一个class
	function addClass(currNode, newClass){
        var oldClass;
        oldClass = currNode.getAttribute("class") || currNode.getAttribute("className");
        if(oldClass !== null) {
		   newClass = oldClass+" "+newClass;
		}
		currNode.className = newClass; //IE 和FF都支持
  		}

	//每次移除一个class
	function removeClass(currNode, curClass){
		var oldClass,newClass1 = "";
        oldClass = currNode.getAttribute("class") || currNode.getAttribute("className");
        if(oldClass !== null) {
		   oldClass = oldClass.split(" ");
		   for(var i=0;i<oldClass.length;i++){
			   if(oldClass[i] != curClass){
				   if(newClass1 == ""){
					   newClass1 += oldClass[i]
				   }else{
					   newClass1 += " " + oldClass[i];
				   }
			   }
		   }
		}
		currNode.className = newClass1; //IE 和FF都支持
	}

	//检测是否包含当前class
	function hasClass(currNode, curClass){
		var oldClass;
		oldClass = currNode.getAttribute("class") || currNode.getAttribute("className");
		if(oldClass !== null){
			oldClass = oldClass.split(" ");
			for(var i=0;i<oldClass.length;i++){
			   if(oldClass[i] == curClass){
				   return true;
			   }
		   }
		}
		return false;
	}
</script>
</head>
<body >
	<dl class="my-lpn">
		<dt class="title">我的车牌号码</dt>
		<dd class="lpn"><span>${carnumber}</Mspan><span id ="mycar"><a class="change-btn" href="wxpfast.do?action=thirdeditcar&openid=${openid}&comid=${comid}&ordeid=${orderid}">修改</a></span></dd>
	</dl>
	<section class="main" >
		<fieldset id = "maindiv">

		<div class="info-area">
			<dl class="totle" style="border-bottom:0px">
				<dt class="totle-title" id="pay_title">停车费</dt>
				<dd class="totle-num othermoney hide" style="text-decoration:line-through;font-size:20px;padding-top:10px;">￥${total}</dd>
				<dd class="totle-num" style="color:#04BE02;" id ="pay_money">￥<span id="money">${money}</span></dd>
				<div class="sweepcom hide" style="border-bottom: 1px solid #E0E0E0;"></div>
			</dl>
			<ul class="info-list" style="padding-top:1px;">
				<li class="list" id='prepayli'><span class="list-title">已付金额</span><span class="list-content" id="prepaymoney">${prepay}元</span></li>
				<li class="list" id='parknameli'><span class="list-title" >车场名称</span><span class="list-content">${parkname}</span></li>
				<li class="list"><span class="list-title">入场时间</span><span class="list-content">${starttime}</span></li>
				<li class="list" id="prepaidtime_title"><span class="list-title" id="prepaidtime" >已停时长</span><span class="list-content" id="parktime" >${parktime}</span></li>
				<li class="list" id="derate_money_title"><span class="list-title" id="derate_money" >减免金额</span><span class="list-content" id="derate_money_span" >${derate_money}元</span></li>
				<!-- <li class="list" id="derate_duration_title"><span class="list-title" id="derate_duration" >减免时长</span><span class="list-content" id="parktime" >${derate_duration}分钟</span></li> -->
				<li class="list"><span class="list-title">车牌号码</span><span class="list-content">${carnumber}</span></li>
				<li class="list" id="stop_position_title"><span class="list-title">停车位置</span><span class="list-content">${stop_position}</span></li>
			</ul>
			</ul>
		</div>
		<div style="height:15px"></div>
		<button id="wx_pay" onclick='payorder();' class="weui-btn weui-btn_primary weui_btn"  style="width:95%"><i id ='loading_img' class="weui-loading" style='display:none' ></i><span id='go_pay'>去支付</span></button>
		<div class="tips"></div>
		</fieldset>
		<div style="text-align:center;" id="error" class="error"></div>
		<div class="wxpay-logo hide"></div>
		<div style="text-align:center;" id="oErrorLog"></div>
	</section>
	
</body>
<script type="text/javascript">
	var getObj =function(id){return document.getElementById(id)};
	var carnumber= "${carnumber}";
	var car_number=encodeURI(carnumber);
	var orderid = "${orderid}";//订单编号
	var state = "${state}";
	var prepay = "${prepay}";//已付金额
	var parkname="${parkname}";
	var clientType = "${client_type}";
	var derate_money =parseFloat('${derate_money}');
	//var derate_duration ="${derate_duration}";
	var money = parseFloat('${money}');
	var is_paycomplete = "${is_paycomplete}";//结算待支付订单标记
	var is_modify_carno = "${is_modify_carno}";//待订单编号过来的不能修改车牌
	var pay_single = "${pay_single}";
	var out_trade_no = "${out_trade_no}";//预下单编号

	function pageInit(){
		if(derate_money>0){
			$(".othermoney").removeClass("hide");
		}else{
			getObj('derate_money_title').style.display='none';
		}
		<%--结算待支付不能修改车牌 --%>
		if(is_paycomplete == "1" || is_modify_carno == "0"){
			getObj("mycar").style.display='none';
		}
		<%--结算待支付不能显示多少时间免费离场 --%>
		if(is_paycomplete == "1" && state == "1"){
			$(".error").addClass("hide");
		}
		//alert(state);

		if(prepay==0){
			getObj("prepayli").style.display='none';
		}
		if(clientType=='ali'){
			getObj("mycar").style.display='none';
		}

		if(parkname==''){
			getObj("parknameli").style.display='none';
		}
		//state 0失败，1成功,2已预付，3网络不通
		if(state == "0"){
			getObj("maindiv").style.display='none';
			getObj("error").innerHTML = "当前无订单";
		}else if(state==2){
			$(".weui_btn").addClass("hide");
			getObj("pay_title").innerHTML="还需支付金额";
			getObj("prepaidtime").innerHTML="已预付时长";
			getObj("error").innerHTML = "您已预支付过，不能再次预支付";
		}else if(state==3){
			$(".weui_btn").addClass("hide");
			getObj("pay_money").innerHTML="";
			getObj("parktime").innerHTML="";
			//getObj("pay_title").innerHTML="金额未知";
			getObj("error").innerHTML = "查询金额失败，请稍候重试";
		}
		if(parseInt("${free_out_time}")>0){
			getObj("error").innerHTML = "请在支付后${free_out_time}分钟内离场,否则将产生超时费用！";
		}

		if("${parktime}"=="0"){
			getObj("prepaidtime_title").style.display='none';
		}
		if("${stop_position}"==""||"${stop_position}"=="null"){
			getObj("stop_position_title").style.display='none';
		}
		if(parseFloat('${money}')<=0){
			if(getObj('money'))
				getObj('money').innerHTML ="0.0";
			$(".weui_btn").addClass("hide");
		}

	}
	
	function sendError(error,fun){
		//alert(error);
		jQuery.ajax({
			type : "post",
			url : "wxpfast.do",
			data : {
				'orderid' : error,
				'error' : orderid,
				'openid' : '${openid}',
				'oid' : '${oid}',
				'money' : money,
				'derate_money' : '${derate_money}',
				'car_number' : car_number,
				'parkid' : '${comid}',
				'uin' : '${uin}',
				'test' : '0',//测试标志
				'is_paycomplete':is_paycomplete,
				'pay_single':pay_single,
				'out_trade_no':out_trade_no,
				'action' : 'printlogs'
			  },
			  async : true,
			  success : function(result) {
				 getObj('oErrorLog').innerHTML=result;
				 setTimeout(function(){
					 getObj('oErrorLog').innerHTML=''; 
				 },1000)
				 if(fun){
					 fun();
				 }
			  }
			}
		);
		
	}
	var isHit = false;
	function payorder(){
		//alert('star');
		//$(".asaa").addClass("weui-btn_loading");
		getObj('loading_img').style.display='';
		getObj('go_pay').innerHTML="正在支付...";
		if(isHit)
			return ;
		else
			isHit = true;
		/*if(clientType=='ali'){
			location="aliprepay.do?action=prepay&uin=${uin}&unionid=&parkid=${comid}&bid=${oid}&orderid="+orderid;
			return ;
		}*/
		
		//alert(isHit);
		//try{T.maskTip(1,'<img src='images/loading.gif' />正在发起支付,请稍后...');}catch(e){};
		getData();
	}
	
	function getData(){
		jQuery.ajax({
			type : "post",
			url : "wxpfast.do",
			data : {
				'orderid' : orderid,
				'openid' : '${openid}',
				'oid' : '${oid}',
				'money' : money,
				'derate_money' : '${derate_money}',
				'car_number' : car_number,
				'parkid' : '${comid}',
				'uin' : '${uin}',
				'test' : '0',//测试标志
				'is_paycomplete':is_paycomplete,
				'pay_single':pay_single,
				'out_trade_no':out_trade_no,
				'action' : 'thirdweixinorder'
			  },
			  //async : false,
		      error: function(XMLHttpRequest, textStatus, errorThrown){
		        sendError("payerror ajax >>>textStatus:"+textStatus+",errorThown:"+errorThrown);
		      },
			 success : function(result) {
				//try{T.maskTip(0);}catch(e){};
				
				//$(".wx_pay").removeClass("btn-loading-on");
             try{
				if(result){
					var rest = eval(''+result+'');
					var state = rest[0].state;
					if(state ==1){//正常发起支付
							var appid = rest[0].appid;
							var timestamp = rest[0].timestamp;
							var nonceStr = rest[0].nonceStr;
							var packagevalue = rest[0].packagevalue;
							var signType = rest[0].signType;
							var paySign = rest[0].paySign;
							callpay(appid,timestamp,nonceStr,packagevalue,signType,paySign);
							isHit=false;
							getObj('loading_img').style.display='none';
							getObj('go_pay').innerHTML='去支付';
					}else if(state==2){//订单金额有变化
						var nowmoney =rest[0].money;
						//$(".weui_btn").addClass("hide");
						//getObj("error").innerHTML = "订单金额有变化，原金额"+premoney+"，现在金额"+nowmoney;
						money = nowmoney;
						if(derate_money>0){
							money = money-derate_money;
							money = money<0?0:money;
							$(".othermoney").removeClass("hide");
							getObj("money").innerHTML=money;
						}else{
							getObj("money").innerHTML=nowmoney;
						}
						var duration = rest[0].duration;
						if(duration){
							getObj("prepaidtime_title").style.display='';
							getObj("parktime").innerHTML=duration;
						}
						//getObj("wx_pay").value="继续支付";
						getObj('go_pay').innerHTML='继续支付';
						if(money==0){
							$(".weui_btn").addClass("hide");
						}
						isHit=false;
						getObj('loading_img').style.display='none';
						
					}else if(state==3){//已预付
						var prepayid = rest[0].prepay;
						$(".weui_btn").addClass("hide");
						getObj("prepayli").style.display='';
						getObj("prepaymoney").innerHTML = prepayid+"元";
					}else if(state==4){//重复支付失败
						window.location.href = "wxpfast.do?action=thirdpayerror";
					}else if(state==5){
						$(".weui_btn").addClass("hide");
						getObj("pay_money").innerHTML="";
						getObj("parktime").innerHTML="";
						//getObj("pay_title").innerHTML="金额未知";
						getObj("error").innerHTML = "查询金额失败，请稍候重试";
					}else{//错误 ，下单错误
						$(".weui_btn").addClass("hide");
						getObj("error").innerHTML = "支付出错，请重新扫码";
					}
				}
		      }
		      catch(err){
		        sendError("payerror  >>>>:"+err.message);
		      }
			}
		});
	}
	
	function callpay(appid,timestamp,nonceStr,packagevalue,signType,paySign){//调起微信支付
		if (typeof WeixinJSBridge == "undefined"){
			   if( document.addEventListener ){
			       document.addEventListener('WeixinJSBridgeReady', function(){
			    	   onBridgeReady(appid,timestamp,nonceStr,packagevalue,signType,paySign);
			       }, false);
			   }else if (document.attachEvent){
			       document.attachEvent('WeixinJSBridgeReady', function(){
			    	   onBridgeReady(appid,timestamp,nonceStr,packagevalue,signType,paySign);
			       }); 
			       document.attachEvent('onWeixinJSBridgeReady', function(){
			    	   onBridgeReady(appid,timestamp,nonceStr,packagevalue,signType,paySign);
			       });
			   }
			}else{
			   onBridgeReady(appid,timestamp,nonceStr,packagevalue,signType,paySign);
		}
	    
	}

	
	function onBridgeReady(appid,timestamp,nonceStr,packagevalue,signType,paySign){
		 WeixinJSBridge.invoke(
	          'getBrandWCPayRequest',
	          {
	            "appId" : appid,
	                  "timeStamp":timestamp,
	                  "nonceStr" : nonceStr,
	                  "package" : packagevalue,
	                  "signType" :signType,
	                  "paySign" :paySign
	          },
	          function(res){
	              if(res.err_msg == "get_brand_wcpay_request:ok"){
	 //           		 alert("开始跳转成功页面。。。");
	                  window.location.href = "wxpublic.do?action=balancepayinfo&openid=${openid}&money=${money}&notice_type=${notice_type}&leaving_time=${delaytime}&paytype=${paytype}&orderid=${orderid}";
	              }else{
	            	  sendError("weixinpay error>>>>"+res.err_msg+","+JSON.stringify(res),function(){window.history.back();});
	            	  //window.location.href = "wxpublic.do?action=balancepayinfo";
	              }
		});
	}
	
	try{
		pageInit();
	}catch(e){
		//var oErrorLog= getObj('oErrorLog');
		sendError(e.message+",初始化失败");
	}
	

</script>
</html>
