<%@ page language="java" contentType="text/html; charset=gb2312"
    pageEncoding="gb2312"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=1">
<meta content="yes" name="apple-mobile-web-app-capable">
<meta name="apple-mobile-web-app-status-bar-style" content="black">
<meta http-equiv="x-ua-compatible" content="IE=edge">
<title>${title}</title>
<link rel="stylesheet" href="css/weui-0.4.3.css">
<link rel="stylesheet" href="css/jquery-weui-0.8.3.css">
<link rel="stylesheet" type="text/css" href="css/list.css?v=12" />
<link rel="stylesheet" type="text/css" href="css/mobiscroll.2.13.2.css?v=12" />
<script src="js/jquery.js?v=1"></script>
<script src="js/wxpublic/jquery-weui-0.8.3.js"></script>
<script src="js/wxpublic/fastclick.js"></script>
<script src="js/wxpublic/mobiscroll.2.13.2.js"></script>
<script src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js" type="text/javascript"></script>
<style type="text/css">
#scroller .li1 {
    padding:0 10px;
    height:50px;
    line-height:50px;
    border-bottom:1px solid #EBEBEB;
    border-top:1px solid #EBEBEB;
    background-color:white;
    margin-top:20px;
}

.li2 {
    padding:0 10px;
    height:120px;
    border-bottom:1px solid #EBEBEB;
    border-top:1px solid #EBEBEB;
    background-color:white;
    font-size:16px;
    margin-top:20px;
}

a{
	text-decoration:none;
	color:#6D6D6D;
	font-size:16px;
	
	position: relative;
	top:-35px;
	left:30px;
}

.img1{
	width:20px;
	height:20px;
	margin-top:15px;
}
</style>

<style type="text/css">
.carnumber {
	margin-left: 10px;
	border-width: 0;
	font-size: 16px;
}

.wx_pay {
	border-radius: 3px;
	width: 98%;
	margin-left: 1%;
	height: 40px;
	margin-top: 20px;
	font-size: 15px;
	background-color: #38B074;
	color: white;
	border: 1px solid #F0F0F0;
}

.hide {
	display: none;
}

.error {
	color: red;
	font-size: 15px;
}

.company_name {
	margin-left: 10px;
	text-decoration: none;
	color: #04be02;
	font-size: 16px;
}

input::-webkit-outer-spin-button,input::-webkit-inner-spin-button {
	-webkit-appearance: none;
}

.wxpay-logo {
	content: "";
	background: url(images/wxpublic/wxpay_logo.png) no-repeat;
	width: 71px;
	height: 19px;
	display: inline-block;
	-webkit-background-size: 71px 19px;
	background-size: 71px 19px;
	-moz-transform: translateX(-50%);
	-webkit-transform: translateX(-50%);
	-ms-transform: translateX(-50%);
	transform: translateX(-50%);
	margin: 20px 0 20px 50%;
}

.jine{
	width: 60%;
	height: 25px;
	padding-top: 0px;
	border: 0px solid red;
	font-size: 16px;
	-webkit-appearance: none;
	float:right;
	text-align:right;
	margin-top: 12px;
	direction:rtl;
	color:#04be02;
	font-weight:bold;
	background:white;
}
</style>
</head>
<body style="background-color:#EEEEEE;">
<div class="container">  
    <input id="date" />  
</div>
<div id="wrapper" style="margin-top:-45px;">
<form method="post" role="form" action="wxpaccount.do?action=topayprod&showwxpaytitle=1" id="payform">
	<div id="scroller">
		<ul id="thelist">
			<li id="li2" class="li2">
				<div style="margin-top:15px;color:#6D6D6D;font-weight:bold;margin-left: 10px;">车场名称：${cname}</div>
				<div id="pname" style="margin-top:15px;color:#6D6D6D;font-weight:bold;margin-left: 10px;">包月产品：${pname}</div>
				<div id="exptime" style="margin-top:15px;color:#6D6D6D;font-weight:bold;margin-left: 10px;">套餐有效期至：${exptime}</div>
			</li>
			<li class="li1">
				<div class="company_name">
					<span>起始日期</span>
					<input id="starttime" name="starttime" class="jine" value="${btime}" />
				</div>
			</li>
			<li class="li1">
				<div class="company_name"><span>包月时长</span>
					<span style="float:right;color:#C3C3C3;">个月</span>
					<div style="float:right;margin-top:3px;">
					<select id="months" name="months" class="weui_select" style="font-weight:bold;color:#04be02;">
						<option selected="selected">1</option>
						<option>2</option>
						<option>3</option>
						<option>4</option>
						<option>5</option>
						<option>6</option>
						<option>7</option>
						<option>8</option>
						<option>9</option>
						<option>10</option>
						<option>11</option>
						<option>12</option>
					</select>
					</div>
				</div>
			</li>
			<input type="text" name="openid" class="hide" value="${openid}">
			<input type="text" name="prodid" class="hide" value="${prodid}">
			<input type="text" name="cardid" class="hide" value="${cardid}">
			<div>
				<div style="text-align:center;margin-top:20px;">
					<span style="font-size:25px;font-weight:bold;color:#04be02;">￥</span>
					<input id="thirdprice" style="display:none" name="thirdprice" value=""><span id="moneyafter" style="font-size:60px;font-weight:bold;color:#04be02;">${money}</span>
					<input id="trade_no" style="display:none" name="trade_no" value="">
					<input id="comid" style="display:none" name="comid" value="">
				</div>
			</div>
			<input type="button" id="paysubmit" class="weui_btn weui_btn_primary" style="width:95%" onclick='check();' value="续费月卡" />
			<div class="wxpay-logo"></div>
			<div style="text-align:center;" id="error" class="error"></div>
		</ul>
	</div>
</form>
</div>

<div id="footer"></div>
<script type="text/javascript">
	var pname = ${pname}
	var exptime = ${exptime}
	if(pname==-1||exptime==-1){
		document.getElementById("exptime").style.display='none'
		document.getElementById("pname").style.display='none'
		document.getElementById("li2").style.height=55+'px'
	}
	
	
	function getprice(flag) {
		var starttime = document.getElementById("starttime").value;
		var months = document.getElementById("months").value;
		jQuery.ajax({
					type : "post",
					url : "wxpaccount.do",
					data : {
						'action' : 'getlocalprice',
						'prodid' : '${prodid}',
						'cardid' : '${cardid}',
						'starttime' : starttime,
						'months' : months,
						'openid' : '${openid}',
						'comid' : '${comid}',
						'r' : Math.random()
					},
					async : false,
					success : function(result) {
						if(result == "-1"){
							document.getElementById("error").innerHTML = "出错了";
						}else if(result == "-2"){
							document.getElementById("error").innerHTML = "起始日期要晚于今天";
						}else if(result == "-3"){
							document.getElementById("error").innerHTML = "超出产品有效期，请重新选择";
						}else {
							if(flag == "1"){
								$("#payform")[0].submit();
							}else{
								document.getElementById("error").innerHTML = "";
								var jsonData = eval("(" + result + ")");
								document.getElementById("moneyafter").innerHTML = jsonData.total;
								document.getElementById("thirdprice").value = jsonData.total;
								document.getElementById("trade_no").value = jsonData.trade_no;
								document.getElementById("comid").value = jsonData.comid;
							}
						}
					}
				});
	}
	getprice();
	
	$("#starttime").bind("change", function() {
		getprice();
	});
	$("#months").bind("change", function() {
		getprice();
	});

	function check() {
		/* var starttime = document.getElementById("starttime").value;
		var t1 = new Date((starttime+" 00:00:00").replace(/-/g,"/")).getTime();
		var t2 = new Date().getTime();
        if(t1 < t2 - 24*60*60*1000){
        	document.getElementById("error").innerHTML = "起始日期要晚于今天";
        	return;
        } */
       $("#payform")[0].submit();
       //getprice("1");
	}
</script>
<script type="text/javascript">
$(function () {  
    $("#starttime").mobiscroll().date({  
        theme: "android-ics light",  
        lang: "zh",  
		minDate: new Date(${minyear}, ${minmonth}, ${minday}),
        maxDate: new Date(${maxyear}, ${maxmonth}, ${maxday}),
		//invalid: { daysOfMonth: ['5/1', '12/24', '12/25'] },
        cancelText: null,  
        dateFormat: 'yy-mm-dd', //返回结果格式化为年月格式  
        // wheels:[], 设置此属性可以只显示年月，此处演示，就用下面的onBeforeShow方法,另外也可以用treelist去实现  
        onBeforeShow: function (inst) { 
			
		}, //弹掉“日”滚轮  
        headerText: function (valueText) { //自定义弹出框头部格式  
            array = valueText.split('-');  
            var time = array[0] + "-" + array[1] + "-" + array[2];
            return time;  
        }  
    });  
}) 
</script>
</body>
</html>
