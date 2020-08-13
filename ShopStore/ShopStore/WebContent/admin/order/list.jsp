<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<HTML>
	<HEAD>
		<meta http-equiv="Content-Language" content="zh-cn">
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<link href="${pageContext.request.contextPath}/css/Style1.css" rel="stylesheet" type="text/css" />
		<script language="javascript" src="${pageContext.request.contextPath}/js/public.js"></script>
		
		<!-- 弹出层插件 -->
		<link href="${pageContext.request.contextPath}/css/popup_layer.css" type="text/css" rel="stylesheet"/>
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-1.8.3.min.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/popup_layer.js"></script>		
		<!-- 调用插件弹出层的方法 -->
		<script type="text/javascript">
			$(function(){
				
				<c:forEach var="order" items="${orderList }">
				//弹出层插件调用
				new PopupLayer({
					trigger:".clickedElement_${order.oid }",
					popupBlk:"#showDiv_${order.oid }",
					closeBtn:"#closeBtn_${order.oid }",
					useOverlay:true
				});
				</c:forEach>
				
			});
			
			
			
		</script>
		
	</HEAD>
	<body>
	
		<form id="Form1" name="Form1" action="${pageContext.request.contextPath}/user/list.jsp" method="post">
			<table cellSpacing="1" cellPadding="0" width="100%" align="center" bgColor="#f5fafe" border="0">
				<TBODY>
					<tr>
						<td class="ta_01" align="center" bgColor="#afd1f3">
							<strong>订单列表</strong>
						</TD>
					</tr>
					
					<tr>
						<td class="ta_01" align="center" bgColor="#f5fafe">
							<table cellspacing="0" cellpadding="1" rules="all"
								bordercolor="gray" border="1" id="DataGrid1"
								style="BORDER-RIGHT: gray 1px solid; BORDER-TOP: gray 1px solid; BORDER-LEFT: gray 1px solid; WIDTH: 100%; WORD-BREAK: break-all; BORDER-BOTTOM: gray 1px solid; BORDER-COLLAPSE: collapse; BACKGROUND-COLOR: #f5fafe; WORD-WRAP: break-word">
								<tr
									style="FONT-WEIGHT: bold; FONT-SIZE: 12pt; HEIGHT: 25px; BACKGROUND-COLOR: #afd1f3">

									<td align="center" width="10%">
										序号
									</td>
									<td align="center" width="10%">
										订单编号
									</td>
									<td align="center" width="10%">
										订单金额
									</td>
									<td align="center" width="10%">
										收货人
									</td>
									<td align="center" width="10%">
										订单状态
									</td>
									<td align="center" width="50%">
										订单详情
									</td>
								</tr>
								 <c:forEach var="order" items="${orderList }" varStatus="status">
								<tr onmouseover="this.style.backgroundColor = 'white'"
									onmouseout="this.style.backgroundColor = '#F5FAFE';">
									<td style="CURSOR: hand; HEIGHT: 22px" align="center"
										width="18%">
										${status.index+1 }
									</td>
									<td style="CURSOR: hand; HEIGHT: 22px" align="center"
										width="17%">
										${order.oid }
									</td>
									<td style="CURSOR: hand; HEIGHT: 22px" align="center"
										width="17%">
										${order.total }
									</td>
									<td style="CURSOR: hand; HEIGHT: 22px" align="center"
										width="17%">
										${order.name }
									</td>
									<td style="CURSOR: hand; HEIGHT: 22px" align="center"
										width="17%">
									${order.state eq 1 ?"已结清":"未付款" }
									</td>
									<td align="center" style="HEIGHT: 22px">
										<input type="button" value="订单详情" class="clickedElement_${order.oid }" />
									</td>
					
								</tr>
								</c:forEach>
								
							</table>
						</td>
					</tr>
					
				</TBODY>
			</table>
		</form>
		
		<!-- 弹出层 HaoHao added -->
		 <c:forEach var="order" items="${orderList }">
		
        <div id="showDiv_${order.oid }" class="blk" style="display:none;">
            <div class="main">
                <h2>订单编号：<span id="shodDivOid" style="font-size: 13px;color: #999">${order.oid }</span></h2>
                <a href="javascript:void(0);" id="closeBtn_${order.oid }" class="closeBtn">关闭</a>
              
				<div style="padding:20px;">
					<table id="showDivTab" style="width:100%">
						<tr id='showTableTitle'>
							<th width='20%'>图片</th>
							<th width='25%'>商品</th>
							<th width='20%'>价格</th>
							<th width='15%'>数量</th>
							<th width='20%'>小计</th>
						</tr>
						 <c:forEach var="orderItem" items="${order.items }" >
						<tr style='text-align: center;'>
							<td>
								<img src='${pageContext.request.contextPath }/${orderItem.product.pimage}' width='70' height='60'>
							</td>
							<td><a target='_blank'>${orderItem.product.pname}</a></td>
							<td>￥${orderItem.product.shop_price}</td>
							<td>${orderItem.count}</td>
							<td><span class='subtotal'>￥${orderItem.subtotal}</span></td>
						</tr>
					  </c:forEach>
						
						
					</table>
				</div>
            </div>
            
        </div>
      <div align="center">
		</c:forEach>
		      <c:if test="${currentPage eq 1 }">
			     上一页
			</c:if>
			<c:if test="${currentPage ne 1 }">
				<a href="${pageContext.request.contextPath }/findOrder?method=findOrder&currentPage=${currentPage-1}">
				上一页
				</a>
			</c:if>
		
			<c:forEach var="p"  begin="1" end="${totalPage }">
		
			  <c:if test="${p eq currentPage }">
			  	<a href="javascript:void(0);" style="color: red">${p}</a>
			  </c:if>
			    <c:if test="${p ne currentPage }">
		     <a href="${pageContext.request.contextPath }/findOrder?method=findOrder&currentPage=${p}">${p }</a>
				</c:if>
			</c:forEach>
			
			<c:if test="${currentPage eq totalPage }">
		            下一页
			
			</c:if>
			<c:if test="${currentPage ne totalPage }">
		<a href="${pageContext.request.contextPath }/findOrder?method=findOrder&&currentPage=${currentPage+1}">
			下一页</a>
			</c:if>
		</div>
	</body>
</HTML>

