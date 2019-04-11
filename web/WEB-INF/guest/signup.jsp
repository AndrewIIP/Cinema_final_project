<%--
  Created by IntelliJ IDEA.
  User: Andrew
  Date: 23.03.2019
  Time: 15:59
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page isELIgnored="false" %>

<fmt:setLocale value="${sessionScope.curLang}"/>
<fmt:setBundle basename="lang"/>

<html lang="${sessionScope.curLang}">
<head>
    <title>Title</title>
    <link  href="${pageContext.request.contextPath}/css/signup.css" type="text/css" rel="stylesheet">
    <link  href="${pageContext.request.contextPath}/css/bootstrap.css" rel="stylesheet" type="text/css"/>
    <script src="${pageContext.request.contextPath}/js/jquery.js"></script>
    <script src="${pageContext.request.contextPath}/js/bootstrap.js"></script>
    <script src="${pageContext.request.contextPath}/js/registerAjax.js"></script>
    <link  href="${pageContext.request.contextPath}/css/all.css" rel="stylesheet"/>
</head>
<body>
<div class="container">
    <div class="row">
        <div class="col-lg-10 col-xl-9 mx-auto">
            <div class="card card-signin flex-row ">
                <%--<div class="card-img-left d-none d-md-flex">
                    <!-- Background image for card set in CSS! -->
                </div>--%>
                <div class="card-body">
                    <h5 class="card-title text-center"><fmt:message key="register.register"/></h5>
                    <form class="form-signin">
                        <div class="form-label-group">
                            <input type="text" id="username" class="form-control" placeholder="Username" required autofocus>
                            <label for="username"><fmt:message key="register.username.field"/></label>
                        </div>

                        <div class="form-label-group">
                            <input type="text" id="email" class="form-control" placeholder="Email address" required>
                            <label for="email"><fmt:message key="register.email.field"/></label>
                        </div>

                        <hr>

                        <div class="form-label-group">
                            <input type="password" id="password" class="form-control" placeholder="Password" required>
                            <label for="password"><fmt:message key="header.inplaceholder.password"/></label>
                        </div>

                        <div class="form-label-group">
                            <input type="password" id="confirm_password" class="form-control" placeholder="Password" required>
                            <label for="confirm_password"><fmt:message key="register.confirm.field"/></label>
                        </div>

                        <nav class="navbar navbar-expand-lg mynavbar header" style="padding-bottom: 20px;">
                            <div class="collapse navbar-collapse" id="navbarResponsive">
                                <ul class="navbar-nav ml-auto">
                                    <li class="nav-item mybutton">
                                        <a class="nav-link" href="${pageContext.request.contextPath}/shows_you/?${pageContext.request.queryString}"><fmt:message key="header.home"/>
                                            <span class="sr-only">(current)</span>
                                        </a>
                                    </li>
                                    <li class="nav-item dropdown mybutton">
                                        <a class="nav-link dropdown-toggle" href="#" id="navbarDropdown" role="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                                            <fmt:message key="header.language"/>
                                        </a>
                                        <!-- Here's the magic. Add the .animate and .slide-in classes to your .dropdown-menu and you're all set! -->
                                        <div class="crystal shadow-c dropdown-menu dropdown-menu-right animate slideIn" aria-labelledby="navbarDropdown">
                                            <a class="dropdown-item" href="${pageContext.request.contextPath}/shows_you/go_registration"
                                               style="padding-left: 12px;">
                                                <img src="${pageContext.request.contextPath}/pic/langs/United-Kingdom.png"
                                                     style="margin-right: 24px"/><fmt:message key="header.lang.eng"/>
                                            </a>
                                            <a class="dropdown-item" href="${pageContext.request.contextPath}/shows_you/go_registration?curLang=uk"
                                               style="padding-left: 12px;">
                                                <img src="${pageContext.request.contextPath}/pic/langs/Ukraine.png"
                                                     style="margin-right: 24px"/><fmt:message key="header.lang.ukr"/>
                                            </a>
                                        </div>
                                    </li>
                                </ul>
                            </div>
                        </nav>

                        <button id="register-btn" class="btn btn-lg btn-primary btn-block text-uppercase" type="button"><fmt:message key="register.register"/></button>
                        <div id="errorInp" class="alert alert-danger alert-span" style="border-radius: 5rem;">
                            <span id="osp" class="alert-span"></span>
                        </div>
                        <a class="d-block text-center mt-2 small" href="${pageContext.request.contextPath}/shows_you/go_login?${pageContext.request.queryString}"><fmt:message key="header.login"/></a>
                    </form>

                </div>

            </div>
        </div>
    </div>
</div>
</body>
</html>
