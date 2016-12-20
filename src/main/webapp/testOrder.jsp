<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
	<table width=100% align="center" style="line-height:2.0px" border = "0px">
		<tr>
			<td align='center'>
				<form action="zfbCallBack" method="post" >
					<table cellpadding="5px" cellspacing="0"  border="1px;" style="border-style: solid;">
						<tr>
							<td>商户订单号</td>
							<td><input name="out_trade_no" value="120160701164304000002"/></td>
						</tr>
						<tr>
							<td>支付宝交易号</td>
							<td><input name="trade_no" value="2016070921001004220227712082"/></td>
						</tr>
						<tr>
							<td>交易状态</td>
							<td><input name="trade_status"  value="TRADE_SUCCESS"/></td>
						</tr>
						<tr>
							<td>成功标识</td>
							<td><input name="is_success" value="T" /></td>
						</tr>
						<tr>
							<td>签名方式</td>
							<td><input name="sign_type" value="RSA" /></td>
						</tr>
						<tr>
							<td>签名</td>
							<td><input name="sign"  value="lQK7uGNfWIccKQPYB%2FGpSar1NRta1c1oc%2Fiq6p5gXAPZn%2BvIOz8CMRGEV4WF6yjcKabksJfngzGAVWtqGbdpJjwWh5N0yXIdylrNCj5BEQ2bUgTsRDdOn540LJNaGnzGHajBiaf6HmOR7c2B4AmNKEAjjdcIDuV0%2BNz1eQNaO%2BA%3D"/></td>
						</tr>
						<tr>
							<td>商品名称号</td>
							<td><input name="subject" value="test%E5%95%86%E5%93%81123" /></td>
						</tr>
						<tr>
							<td>支付类型</td>
							<td><input name="payment_type" value="1" /></td>
						</tr>
						<tr>
							<td>通知校验ID</td>
							<td><input name="notify_id" value="RqPnCoPT3K9%252Fvwbh3InXQYHExiIDcvub5dfk6lyaM3saBXOy6lZ2WNZ8h8igAOTQAWOM"/></td>
						</tr>
						<tr>
							<td>通知时间</td>
							<td><input name="notify_time" value="2016-07-09+17%3A17%3A57"/></td>
						</tr>
						<tr>
							<td>通知类型</td>
							<td><input name="notify_type" value="trade_status_sync" /></td>
						</tr>
						<tr>
							<td>卖家支付宝账号</td>
							<td><input name="seller_email" value="seentao%40ufida.com.cn" /></td>
						</tr>
						<tr>
							<td>买家支付宝账号</td>
							<td><input name="buyer_email" value="what866%40163.com" /></td>
						</tr>
						<tr>
							<td>卖家支付宝账号对应的支付宝唯一用户号<span style="color:red;">*</span></td>
							<td><input name="seller_id" value="2088701275916749" /></td>
						</tr>
						<tr>
							<td>买家支付宝账号对应的支付宝唯一用户号</td>
							<td><input name="buyer_id" value="2088902387037221"  /></td>
						</tr>
						<tr>
							<td>交易金额<span style="color:red;">*</span></td>
							<td><input name="total_fee" value="0.01" /></td>
						</tr>
						<tr>
							<td>商品描述</td>
							<td><input name="body" value="%E5%8D%B3%E6%97%B6%E5%88%B0%E8%B4%A6%E6%B5%8B%E8%AF%95" /></td>
						</tr>
						<tr>
							<td colspan=2 align="center"><input type="submit" value="提交" /> </td>
						</tr>
					
					</table>
				</form>
			</td>
		</tr>
	</table>
</body>
</html>