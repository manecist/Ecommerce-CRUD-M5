<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.0/font/bootstrap-icons.css">
    <link rel="icon" href="${pageContext.request.contextPath}/ASSETS/IMG/Carla-Rodriguez-Sailor-Moon-Eternal-moon.512.png" type="image/x-png">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/ASSETS/CSS/estiloHome.css">
    <title>Magical Alliance</title>
</head>
<body>
<header>
    <nav class="navbar navbar-expand-lg navbar-dark bg-dark shadow-sm navbar-mistica">
        <div class="container-fluid px-lg-5">

            <a class="navbar-brand d-flex align-items-center" href="${pageContext.request.contextPath}/home">
                <img src="${pageContext.request.contextPath}/ASSETS/IMG/Carla-Rodriguez-Sailor-Moon-Eternal-moon.512.png" width="50" height="50" class="me-2 logo-header">
                <span class="fw-bolder" id="titulo">Magical Alliance</span>
            </a>

            <button class="navbar-toggler border-0" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav">
                <span class="navbar-toggler-icon"></span>
            </button>

            <div class="collapse navbar-collapse" id="navbarNav">
                <ul class="navbar-nav me-auto">
                    <li class="nav-item">
                        <a class="nav-link nav-link-magico" href="${pageContext.request.contextPath}/home">Home</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link nav-link-magico" href="${pageContext.request.contextPath}/home#sobreNosotros">Nosotros</a>
                    </li>
                    <li class="nav-item dropdown">
                        <a class="nav-link dropdown-toggle nav-link-magico" href="#" data-bs-toggle="dropdown">Productos</a>
                        <ul class="dropdown-menu dropdown-menu-dark dropdown-magic">
                            <c:forEach var="cat" items="${listaCategorias}">
                                <li>
                                    <a class="dropdown-item" href="${pageContext.request.contextPath}/productos?action=list&idCategoria=${cat.idCategoria}">
                                        ${cat.nombreCategoria}
                                    </a>
                                </li>
                            </c:forEach>
                            <li><hr class="dropdown-divider"></li>
                            <li><a class="dropdown-item text-info-magic" href="${pageContext.request.contextPath}/productos?action=list">✨ Ver todo el catálogo</a></li>
                        </ul>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link nav-link-magico" href="${pageContext.request.contextPath}/home#tipBelleza">Tips</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link nav-link-magico" href="${pageContext.request.contextPath}/contacto">Contacto</a>
                    </li>
                </ul>

                <div class="navbar-nav ms-auto align-items-center gap-3">
                    <c:choose>
                        <c:when test="${not empty sessionScope.usuarioLogueado}">
                            <div class="nav-item dropdown">
                                <a class="btn btn-perfil-magic dropdown-toggle d-flex align-items-center" href="#" data-bs-toggle="dropdown">
                                    <i class="bi bi-person-circle me-2 fs-5"></i>
                                    <span>${sessionScope.usuarioLogueado.username}</span>
                                </a>
                                <ul class="dropdown-menu dropdown-menu-end dropdown-menu-dark dropdown-magic">
                                    <li><h6 class="dropdown-header text-turquesa">Rol: ${sessionScope.usuarioLogueado.rol}</h6></li>
                                    <li><a class="dropdown-item" href="${pageContext.request.contextPath}/usuarios?action=edit&id=${sessionScope.usuarioLogueado.id}">
                                        <i class="bi bi-gear me-2"></i>Mi Perfil</a>
                                    </li>
                                    <li><hr class="dropdown-divider"></li>
                                    <li><a class="dropdown-item link-logout-magic fw-bold" href="${pageContext.request.contextPath}/logout">
                                        <i class="bi bi-box-arrow-right me-2"></i>Cerrar Sesión</a>
                                    </li>
                                </ul>
                            </div>
                        </c:when>
                        <c:otherwise>
                            <a class="btn btn-login-circle" href="${pageContext.request.contextPath}/usuario/login.jsp" title="Iniciar Sesión">
                                <img src="${pageContext.request.contextPath}/ASSETS/IMG/inicio sesion.png" width="65" height="65">
                            </a>
                        </c:otherwise>
                    </c:choose>

                    <a class="btn btn-carrito-magic position-relative" href="${pageContext.request.contextPath}/carrito.jsp">
                        <img src="${pageContext.request.contextPath}/ASSETS/IMG/carrito.png" width="45" height="45">
                        <span id="numerito" class="badge rounded-pill bg-rosa-magic position-absolute top-0 start-100 translate-middle">0</span>
                    </a>
                </div>
            </div>
        </div>
    </nav>

    <c:if test="${sessionScope.usuarioLogueado.rol == 'admin'}">
        <div class="admin-bar-dark">
            <div class="container d-flex justify-content-center align-items-center py-2">
                <div class="d-flex align-items-center gap-4">
                    <span class="admin-badge-text"><i class="bi bi-shield-check me-1"></i> GESTION MÁGICA </span>
                    <nav class="admin-nav-links-dark">
                        <a href="${pageContext.request.contextPath}/usuarios?action=list"><i class="bi bi-people"></i> Usuarios</a>
                        <a href="${pageContext.request.contextPath}/productos?action=list"><i class="bi bi-bag-heart"></i> Productos</a>
                        <a href="${pageContext.request.contextPath}/gestion-categorias"><i class="bi bi-folder2-open"></i> Categorías</a>
                        <a href="${pageContext.request.contextPath}/gestion-subcategorias"><i class="bi bi-tags"></i> Subcategorías</a>
                    </nav>
                </div>
            </div>
        </div>
    </c:if>
</header>
