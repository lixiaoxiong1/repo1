<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
    
<!DOCTYPE html>
<script src="js/jquery-1.11.3.min.js" type="text/javascript"></script>
<script type="text/javascript">
  //使用ajax异步请求后台加载商品类型
  $(document).ready(function(){
	 
	  $.post(
		   "${pageContext.request.contextPath}/categoryList",
		   function(data){
			     var content="";
			      for(var i=0;i<data.length;i++){
			    	  content+="<li class='active'><a href='${pageContext.request.contextPath}/product?method=findProductByCategory&cid="+data[i].cid+"'>"+data[i].cname+"</a></li>";
			      }
			     $("#categoryContent").html(content);
		   },
	      "json"
	  );
  });
</script>
<!-- 登录 注册 购物车... -->
<div class="container-fluid">
	<div class="col-md-4">
		<img src="${pageContext.request.contextPath }/img/logo2.png"  />
	</div>

	<div class="col-md-5">
		<img src="${pageContext.request.contextPath }/img/header.png" />
	</div>
	<div class="col-md-3" style="padding-top:20px">
		<ol class="list-inline">
		     <c:choose>
		        <c:when test="${empty user }">
		        <li><a href="login.jsp">登录</a></li>
			   <li><a href="register.jsp">注册</a></li>
		     	<li><a href="cart.jsp">购物车</a></li>
		
		        </c:when>
		        <c:otherwise>
		             你好,${user.username },<a href="${pageContext.request.contextPath }/user?method=loginOut">退出</a>
		        	<li><a href="${pageContext.request.contextPath }/privilege/order?method=myOrder">我的订单</a></li>
		        </c:otherwise>
		     </c:choose>
			
		</ol>
	</div>
</div>

<!-- 导航条 -->
<div class="container-fluid">
	<nav class="navbar navbar-inverse">
		<div class="container-fluid">
			<!-- Brand and toggle get grouped for better mobile display -->
			<div class="navbar-header">
				<button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1" aria-expanded="false">
					<span class="sr-only">Toggle navigation</span>
					<span class="icon-bar"></span>
					<span class="icon-bar"></span>
					<span class="icon-bar"></span>
				</button>
				<a class="navbar-brand" href="${pageContext.request.contextPath }/index">首页</a>
			</div>

			<div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
				<ul class="nav navbar-nav" id="categoryContent">
				    
				</ul>
				<form class="navbar-form navbar-right" role="search">
					<div class="form-group">
						<input type="text" class="form-control" placeholder="Search">
					</div>
					<button type="submit" class="btn btn-default">Submit</button>
				</form>
			</div>
		</div>
	</nav>
</div>