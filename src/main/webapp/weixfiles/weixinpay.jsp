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
<link rel="stylesheet" href="css/weui.css">
<script src="js/jquery.js" type="text/javascript"></script>
<title>微信支付</title>
<script type="text/javascript">
	window.onerror=reportError;
	function reportError(sMsg,sUrl,sLine){
		var oErrorLog="";
		oErrorLog="<b>An error was thrown and caught.</b><p>";
		oErrorLog +="Error: " + sMsg + "<br>";
		oErrorLog +="Line: " + sLine + "<br>";
		oErrorLog +="URL: " + sUrl + "<br>";
		sendError(oErrorLog);
	}
</script>
</head>
<body >
<div class="weui-loadmore" style='margin-top :200px' id ='message'>
      <i class="weui-loading" ></i>
      <span class="weui-loadmore__tips">正在加载</span>
</div>
</body>
<script type="text/javascript">
	var appid='${appid}';
	var timestamp='${timestamp}';
	var nonceStr='${nonceStr}';
	var packagevalue='${packagevalue}';
	var signType='${signType}';
	var paySign='${paySign}';
	//alert(appid+","+timestamp+","+nonceStr+","+packagevalue+","+signType+","+paySign);
	function sendError(error){
		//alert(error);
		jQuery.ajax({
			type : "post",
			url : "wxpfast.do",
			data : {
				'orderid' : '${trade_no}'+error,
				'action' : 'printlogs'
			  },
			  async : true,
			  success : function(result) {
			  }
			}
		);
	}
	function isRepay(){
		//alert(error);
		jQuery.ajax({
			type : "post",
			url : "thirdpay.do",
			data : {
				'tradeno' : '${trade_no}',
				'action' : 'isrepay'
			  },
			  async : true,
			  success : function(result) {
				  if(result=='1'){
					  document.getElementById('message').innerHTML ="订单支付成功...";
					  window.location.href ='${callback_url}';
				  }else if(result =='0'){
					    try{
							callpay();
						}catch(e){
							sendError(e.message);
					    }
				  }else if(result =='-1'){
					  document.getElementById('message').innerHTML ="订单支付失败...";
				  }
			  }
			});
	};
	function callpay(){//调起微信支付
		if (typeof WeixinJSBridge == "undefined"){
			   if( document.addEventListener ){
			       document.addEventListener('WeixinJSBridgeReady', onBridgeReady, false);
			   }else if (document.attachEvent){
			       document.attachEvent('WeixinJSBridgeReady',onBridgeReady); 
			       document.attachEvent('onWeixinJSBridgeReady', onBridgeReady);
			   }
			}else{
			   onBridgeReady();
		}
	    
	}

	
	function onBridgeReady(){
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
	 				if('${callback_url}'!='null'){
	 					window.location.href ='${callback_url}';
	 				}else{
		                window.location.href = "wxpublic.do?action=balancepayinfo&openid=${openid}&money=${money}&notice_type=${notice_type}&leaving_time=${delaytime}&paytype=${paytype}&orderid=${orderid}";
	 				}
	          	  }else{
	          		if('${cancel_url}'!='null'){
	 					window.location.href ='${cancel_url}';
	 				}else{
		                window.location.href = "https://beta.bolink.club/unionapi/thirdfailcallback?trade_no=${trade_no}";
	 				}
	          	  }
			});
	}
	
	/******请求支付***/
	isRepay();
	/******请求支付***/
	
	(function(XBack) {
        XBack.STATE = 'x - back';
        XBack.element;

        XBack.onPopState = function(event) {
            event.state === XBack.STATE && XBack.fire();
            XBack.record(XBack.STATE); //初始化事件时，push一下
        };

        XBack.record = function(state) {
            history.pushState(state, null, location.href);
        };

        XBack.fire = function() {
            var event = document.createEvent('Events');
            event.initEvent(XBack.STATE, false, false);
            XBack.element.dispatchEvent(event);
        };

        XBack.listen = function(listener) {
            XBack.element.addEventListener(XBack.STATE, listener, false);
        };

        XBack.init = function() {
            XBack.element = document.createElement('span');
            window.addEventListener('popstate', XBack.onPopState);
            XBack.record(XBack.STATE);
        };

    })(XBack); // 引入这段js文件

    XBack.init();
    XBack.listen(function() {});
</script>
</html>
