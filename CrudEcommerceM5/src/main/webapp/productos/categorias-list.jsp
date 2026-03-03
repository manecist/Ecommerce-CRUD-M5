<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ include file="../header.jsp" %>

<div class="container mt-5">
    <c:if test="${not empty param.msj or not empty error}">
        <div class="mb-4">
            <c:choose>
                <c:when test="${param.msj == 'save_ok'}">
                    <div class="alert alert-magica bg-white border-0 shadow-sm text-success">
                        <i class="bi bi-check-circle-fill me-2" style="color: #00d4aa;"></i> ¡Hechizo de guardado exitoso!
                    </div>
                </c:when>
                <c:when test="${param.msj == 'delete_ok'}">
                    <div class="alert alert-magica bg-white border-0 shadow-sm text-info">
                        <i class="bi bi-trash-fill me-2" style="color: #f480ff;"></i> La categoría se ha desvanecido.
                    </div>
                </c:when>
                <c:when test="${not empty error}">
                    <div class="alert alert-magica bg-white border-0 shadow-sm text-danger">
                        <i class="bi bi-exclamation-octagon-fill me-2" style="color: #6907ab;"></i> ${error}
                    </div>
                </c:when>
            </c:choose>
        </div>
    </c:if>

    <div class="row">
        <div class="col-md-4 mb-4">
            <div class="card card-magica-form shadow-lg">
                <div class="card-header py-3">
                    <h5 class="mb-0 fw-bold text-center">
                        ${categoriaEnEdicion.idCategoria != 0 ? '✨ Editar Esencia' : '💎 Nueva Categoría'}
                    </h5>
                </div>
                <div class="card-body p-4">
                    <form id="formCategoria" action="${pageContext.request.contextPath}/gestion-categorias" method="post" enctype="multipart/form-data">
                        <input type="hidden" name="idCategoria" value="${categoriaEnEdicion.idCategoria}">
                        <input type="hidden" name="imagenActual" value="${categoriaEnEdicion.imagenBanner}">

                        <div class="mb-3">
                            <label class="form-label label-magico text-uppercase">Nombre de la Categoría</label>
                            <input type="text" name="nombreCategoria" class="form-control"
                                   value="${categoriaEnEdicion.nombreCategoria}" placeholder="Ej: Pociones Astrales" required>
                        </div>

                        <div class="mb-3">
                            <label class="form-label label-magico text-uppercase">Banner Místico</label>

                            <ul class="nav nav-pills nav-fill mb-2 p-1" id="imgTabCat" role="tablist" style="border-radius: 15px; background-color: #f8f0ff; border: 1px solid #d9aaff;">
                                <li class="nav-item">
                                    <button class="nav-link active btn-sm fw-bold" id="galeria-cat-tab" data-bs-toggle="tab" data-bs-target="#galeriaCat" type="button">📜 Galería</button>
                                </li>
                                <li class="nav-item">
                                    <button class="nav-link btn-sm fw-bold" id="subir-cat-tab" data-bs-toggle="tab" data-bs-target="#subirCat" type="button">☁️ Subir</button>
                                </li>
                            </ul>

                            <div class="tab-content border p-3 bg-white" style="border-radius: 15px;">
                                <div class="tab-pane fade show active" id="galeriaCat">
                                    <select name="imagenExistente" class="form-select border-0 bg-light" onchange="cambiarImagen()">
                                        <option value="${categoriaEnEdicion.imagenBanner}">
                                            <c:out value="${categoriaEnEdicion.idCategoria == 0 ? '-- Elige un Banner --' : 'Actual: '.concat(categoriaEnEdicion.imagenBanner)}" />
                                        </option>
                                        <c:forEach var="img" items="${galeriaImagenes}">
                                            <option value="${img}">${img}</option>
                                        </c:forEach>
                                    </select>
                                </div>

                                <div class="tab-pane fade" id="subirCat">
                                    <input type="file" name="imagenBanner" class="form-control border-0 bg-light" accept="image/*">
                                </div>
                            </div>

                            <div class="preview-container-magico shadow-sm mt-3">
                                <small class="d-block mb-2 fw-bold text-center" style="color: #00d4aa;">VISTA PREVIA:</small>
                                <img id="preview" src="${pageContext.request.contextPath}/ASSETS/IMG/${not empty categoriaEnEdicion.imagenBanner ? categoriaEnEdicion.imagenBanner : 'categorias/default-cat.jpg'}"
                                     class="preview-img-magica d-block mx-auto" style="width: 100%; height: 100%; border-radius: 15px; object-fit: cover; border: 2px solid #afeeee;">
                            </div>
                        </div>

                        <div class="d-grid gap-2">
                            <button type="submit" class="btn btn-turquesa-magico py-2 fw-bold">
                                <i class="bi bi-stars me-1"></i> ${categoriaEnEdicion.idCategoria != 0 ? 'Actualizar Esencia' : 'Manifestar Categoría'}
                            </button>
                            <c:if test="${categoriaEnEdicion.idCategoria != 0}">
                                <a href="${pageContext.request.contextPath}/gestion-categorias" class="btn btn-link text-muted text-decoration-none text-center small">Cancelar Edición</a>
                            </c:if>
                        </div>
                    </form>
                </div>
            </div>
        </div>

        <div class="col-md-8">
            <div class="card border-0 shadow-lg" style="border-radius: 30px; overflow: hidden;">
                <div class="table-responsive">
                    <table class="table table-hover align-middle mb-0 text-center">
                        <thead class="table-light text-uppercase small fw-bold">
                        <tr style="background: #f8f0ff;">
                            <th class="py-3" style="color: #6907ab;">ID</th>
                            <th class="py-3" style="color: #6907ab;">Banner</th>
                            <th class="py-3" style="color: #6907ab;">Nombre</th>
                            <th class="py-3" style="color: #6907ab;">Acciones</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach var="c" items="${categorias}">
                            <tr>
                                <td class="fw-bold">#${c.idCategoria}</td>
                                <td>
                                    <img src="${pageContext.request.contextPath}/ASSETS/IMG/${c.imagenBanner}"
                                         alt="banner" style="width: 80px; height: 45px; object-fit: cover;"
                                         class="rounded-3 border shadow-sm">
                                </td>
                                <td class="fw-bold label-magico" style="font-size: 1.1rem;">${c.nombreCategoria}</td>
                                <td>
                                    <div class="btn-group gap-2">
                                        <a href="${pageContext.request.contextPath}/gestion-categorias?action=edit&idCategoria=${c.idCategoria}"
                                           class="btn btn-editar-magico btn-sm shadow-sm" style="border-radius: 10px !important;">
                                            <i class="bi bi-pencil-square"></i>
                                        </a>

                                        <button type="button" class="btn btn-eliminar-magico btn-sm shadow-sm" style="border-radius: 10px !important;"
                                                onclick="prepararBorradoCat(${c.idCategoria}, '${c.nombreCategoria}')"
                                                data-bs-toggle="modal" data-bs-target="#deleteCatModal">
                                            <i class="bi bi-trash3"></i>
                                        </button>
                                    </div>
                                </td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>
</div>

<div class="modal fade" id="deleteCatModal" tabindex="-1" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content modal-magico-danger">
            <div class="modal-header header-eliminar-modal">
                <h5 class="modal-title fw-bold text-white">
                    <i class="bi bi-exclamation-triangle-fill me-2"></i> ¿Desvanecer Categoría?
                </h5>
                <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal"></button>
            </div>

            <div class="modal-body p-4 text-center">
                <div class="mb-3">
                    <i class="bi bi-trash3 fs-1" style="color: #ff6b6b;"></i>
                </div>
                <p class="mb-1 text-muted small text-uppercase fw-bold">Vas a eliminar la esencia de:</p>
                <h5 id="nombreCatBorrar" class="fw-bold mb-3" style="color: #6907ab;"></h5>

                <div class="alert-magica-warning py-3 px-3 mb-0 small text-start">
                    <i class="bi bi-info-circle-fill me-2"></i>
                    Asegúrate de que no existan productos vinculados a esta categoría antes de proceder con el hechizo de desvanecimiento.
                </div>
            </div>

            <div class="modal-footer border-0 d-flex justify-content-center pb-4">
                <button type="button" class="btn btn-revisar-modal" data-bs-dismiss="modal">Conservar</button>
                <a id="btnConfirmarBorradoCat" href="#" class="btn btn-desvanecer-confirmar">
                    Sí, Eliminar <i class="bi bi-wind ms-1"></i>
                </a>
            </div>
        </div>
    </div>
</div>
<%@ include file="../footer.jsp" %>