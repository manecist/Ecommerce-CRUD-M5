<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ include file="../header.jsp" %>

<main class="container mt-5">
    <div id="comienzoHome">
        <c:choose>
            <c:when test="${not empty productos}">
                <div class="container mt-5 shadow p-5 text-center" style="border-radius: 30px; background: rgba(255,255,255,0.8);">
                    <h1 class="display-3 fw-bolder text-uppercase">
                        ${not empty param.idCategoria ? productos[0].nombreCategoria : 'Catálogo Completo'}
                    </h1>
                    <div class="lead fst-italic">
                        <h4 class="fw-bolder">✨ Tu Magia, Tu Estilo: La Selección de Magical Alliance</h4>
                        <h6 class="px-lg-5">Cada artículo es un objeto de poder diseñado para infundir tu día a día con energía de fantasía.</h6>
                    </div>
                </div>
            </c:when>
            <c:otherwise>
                <div class="container mt-5 shadow p-5 text-center" style="border-radius: 30px;">
                    <h1 class="display-3 fw-bolder">Explora la Magia</h1>
                    <p class="lead">Nuevos tesoros están por llegar a esta sección.</p>
                </div>
            </c:otherwise>
        </c:choose>
    </div>

    <div class="container mt-3">
        <c:if test="${param.msj == 'save_ok'}">
            <div class="alert alert-success alert-dismissible fade show shadow-sm border-0" role="alert" style="border-radius: 15px; background-color: #d1e7dd; color: #0f5132;">
                <i class="bi bi-check-circle-fill me-2"></i>
                <strong>✨ ¡Hechizo realizado!</strong> El artículo ha sido guardado correctamente en la Alianza.
                <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
            </div>
        </c:if>

        <c:if test="${param.msj == 'error'}">
            <div class="alert alert-danger alert-dismissible fade show shadow-sm border-0" role="alert" style="border-radius: 15px;">
                <i class="bi bi-exclamation-triangle-fill me-2"></i>
                <strong>💥 Error:</strong> No se pudo completar la operación mágica. Revisa los datos.
                <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
            </div>
        </c:if>
    </div>

    <c:if test="${sessionScope.usuarioLogueado.rol == 'admin'}">
        <div class="d-flex justify-content-center my-4">
            <a href="${pageContext.request.contextPath}/productos?action=new"
               class="btn btn-turquesa-magico shadow-sm px-5 py-3 btn-lg">
                <i class="bi bi-plus-circle-fill me-2"></i> Registrar Nuevo Tesoro Mágico
            </a>
        </div>
    </c:if>

    <div class="container my-4">
        <form action="${pageContext.request.contextPath}/productos" method="GET" class="row g-2 align-items-center justify-content-center mb-4">
            <input type="hidden" name="action" value="list">
            <input type="hidden" name="idCategoria" value="${idCatActual}">

            <div class="col-md-4">
                <div class="input-group buscador-magico shadow-sm">
                    <span class="input-group-text bg-white border-end-0"><i class="bi bi-search"></i></span>
                    <input type="text" name="txtBuscar" class="form-control border-start-0"
                           placeholder="Buscar por nombre..." value="${param.txtBuscar}">
                </div>
            </div>

            <div class="col-md-3">
                <select name="orden" class="form-select selector-magico shadow-sm">
                    <option value="recom" ${param.orden == 'recom' ? 'selected' : ''}>✨ Recomendados</option>
                    <option value="az" ${param.orden == 'az' ? 'selected' : ''}>🔤 Nombre: A - Z</option>
                    <option value="za" ${param.orden == 'za' ? 'selected' : ''}>🔤 Nombre: Z - A</option>
                    <option value="pmin" ${param.orden == 'pmin' ? 'selected' : ''}>💰 Precio: Menor a Mayor</option>
                    <option value="pmax" ${param.orden == 'pmax' ? 'selected' : ''}>💰 Precio: Mayor a Menor</option>
                </select>
            </div>

            <div class="col-md-auto d-flex gap-2">
                <button type="submit" class="btn btn-filtrar-magic fw-bold">Filtrar</button>
                <a href="${pageContext.request.contextPath}/productos?action=list&idCategoria=${idCatActual}"
                   class="btn btn-limpiar-magic">Limpiar</a>
            </div>
        </form>

        <div class="text-center mb-3">
            <a href="${pageContext.request.contextPath}/productos?action=list"
               class="btn btn-outline-secondary rounded-pill">
                ✨ Ver Catálogo Completo (Todas las Categorías)
            </a>
        </div>

        <div class="d-flex flex-wrap justify-content-center gap-3 subcategorias-container">

            <c:choose>
                <c:when test="${empty idCatActual || idCatActual == 0}">
                    <c:forEach var="cat" items="${listaCategorias}">
                        <a href="${pageContext.request.contextPath}/productos?action=list&idCategoria=${cat.idCategoria}"
                           class="btn-filtro-magic">
                            ${cat.nombreCategoria}
                        </a>
                    </c:forEach>
                </c:when>

                <c:otherwise>
                    <a href="${pageContext.request.contextPath}/productos?action=list&idCategoria=${idCatActual}"
                       class="btn-filtro-magic ${idSubCatActual == 0 ? 'active' : ''}">
                        Ver todos los ${productos[0].nombreCategoria}
                    </a>
                    <c:forEach var="sub" items="${listaSubcategorias}">
                        <a href="${pageContext.request.contextPath}/productos?action=list&idCategoria=${idCatActual}&idSubcategoria=${sub.idSubcategoria}"
                           class="btn-filtro-magic ${idSubCatActual == sub.idSubcategoria ? 'active' : ''}">
                            ${sub.nombreSubcategoria}
                        </a>
                    </c:forEach>
                </c:otherwise>
            </c:choose>

        </div>
    </div>

    <div class="row g-4 mb-5">
        <c:forEach var="p" items="${productos}">
            <div class="col-md-6 col-lg-3">
                <div class="card h-100 shadow-sm border-0 card-magica">
                    <img src="${pageContext.request.contextPath}/ASSETS/IMG/${p.imagenProducto}"
                         class="card-img-top p-3 img-producto-redonda" alt="${p.nombreProducto}">
                    <div class="card-body text-center d-flex flex-column">
                        <h5 class="fw-bold">${p.nombreProducto}</h5>
                        <p class="text-muted small mb-2">${p.descripcionProducto}</p>
                        <span class="badge bg-light text-dark mb-3 mx-auto border">${p.nombreSubcategoria}</span>


                        <div class="mb-3 mt-auto">
                            <h3 class="fw-bold mb-1 precio-magico">
                                $ <fmt:formatNumber value="${p.precioProducto}" type="number" maxFractionDigits="0" />
                            </h3>
                            <c:choose>
                                <c:when test="${p.stockProducto > 0}">
                                    <small class="stock-disponible fw-bold">
                                        <i class="bi bi-stars"></i> Stock: ${p.stockProducto}
                                    </small>
                                </c:when>
                                <c:otherwise>
                                    <small class="stock-agotado fw-bold">
                                        <i class="bi bi-x-circle"></i> Agotado
                                    </small>
                                </c:otherwise>
                            </c:choose>
                        </div>

                        <c:choose>
                            <c:when test="${p.stockProducto > 0}">
                                <button onclick="agregarAlCarrito('${p.nombreProducto}')"
                                        class="btn btn-magico w-100 py-2 fw-bold shadow-sm rounded-pill text-white mb-2">
                                        Añadir 🌙
                                </button>
                            </c:when>
                            <c:otherwise>
                                <button class="btn btn-secondary w-100 py-2 rounded-pill disabled mb-2" disabled>Sin Stock</button>
                            </c:otherwise>
                        </c:choose>

                        <c:if test="${sessionScope.usuarioLogueado.rol == 'admin'}">
                            <div class="mt-3 pt-3 border-top d-flex gap-2">
                                <a href="${pageContext.request.contextPath}/productos?action=edit&idProducto=${p.idProducto}"
                                   class="btn btn-editar-magico btn-sm flex-fill rounded-pill">
                                    <i class="bi bi-pencil-square"></i>
                                </a>

                                <button onclick="confirmarEliminar(${p.idProducto}, '${p.nombreProducto}')"
                                        class="btn btn-eliminar-magico btn-sm flex-fill rounded-pill">
                                    <i class="bi bi-trash3"></i>
                                </button>
                            </div>
                        </c:if>
                    </div>
                </div>
            </div>
        </c:forEach>
    </div>
</main>

<div class="modal fade modal-eliminar-magico" id="modalEliminar" tabindex="-1" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content border-0 shadow-lg" style="border-radius: 25px;">
            <div class="modal-header text-white">
                <h5 class="modal-title fw-bold">⚠️ ¿Desvanecer este Tesoro?</h5>
                <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal"></button>
            </div>
            <div class="modal-body p-4 text-center">
                <p class="text-muted mb-2">Estás a punto de eliminar permanentemente:</p>
                <h5 id="nombreProdEliminar" class="fw-bold" style="color: #6907ab;"></h5>
            </div>
            <div class="modal-footer bg-light border-0 d-flex justify-content-center">
                <button type="button" class="btn btn-secondary rounded-pill px-4" data-bs-dismiss="modal">Conservar</button>
                <a id="linkConfirmarEliminar" href="#" class="btn btn-eliminar-magico px-4">Sí, Eliminar</a>
            </div>
        </div>
    </div>
</div>


<%@ include file="../footer.jsp" %>