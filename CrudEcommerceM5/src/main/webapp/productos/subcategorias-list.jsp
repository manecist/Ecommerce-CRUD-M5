<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ include file="../header.jsp" %>

<div class="container mt-5">

    <c:if test="${not empty error or not empty param.msj}">
        <div class="mb-4">
            <c:choose>
                <c:when test="${not empty error}">
                    <div class="alert alert-magica bg-white border-0 shadow-sm text-danger">
                        <i class="bi bi-magic me-2 icon-morado"></i> <strong>¡Hechizo fallido!</strong> ${error}
                    </div>
                </c:when>
                <c:when test="${param.msj == 'save_ok'}">
                    <div class="alert alert-magica bg-white border-0 shadow-sm text-success">
                        <i class="bi bi-stars me-2 icon-turquesa"></i> ¡Subcategoría guardada correctamente!
                    </div>
                </c:when>
                <c:when test="${param.msj == 'delete_ok'}">
                    <div class="alert alert-magica bg-white border-0 shadow-sm text-info">
                        <i class="bi bi-trash2 me-2 icon-rosa"></i> Subcategoría desvanecida con éxito.
                    </div>
                </c:when>
                <c:when test="${param.msj == 'error_ref'}">
                    <div class="alert alert-magica bg-white border-0 shadow-sm text-danger">
                        <i class="bi bi-exclamation-octagon me-2 icon-morado"></i>
                        <strong>Error de Seguridad:</strong> Hay productos vinculados a esta esencia.
                    </div>
                </c:when>
            </c:choose>
        </div>
    </c:if>

    <div class="row">
        <div class="col-md-4 mb-4">
            <div class="card card-magica-form shadow-lg border-0">
                <div class="card-header text-center py-3">
                    <h5 class="mb-0 fw-bold">
                        ${not empty subcategoriaEnEdicion and subcategoriaEnEdicion.idSubcategoria != 0 ? '📝 Editar Esencia' : '➕ Nueva Subcategoría'}
                    </h5>
                </div>
                <div class="card-body p-4 bg-white container-inferior-magic">
                    <form action="${pageContext.request.contextPath}/gestion-subcategorias" method="post">
                        <input type="hidden" name="idSubcategoria" value="${not empty subcategoriaEnEdicion ? subcategoriaEnEdicion.idSubcategoria : 0}">

                        <div class="mb-3">
                            <label class="form-label label-magico text-uppercase">Categoría Padre</label>
                            <select name="idCategoriaAsociada" class="form-select input-magico" required>
                                <option value="" disabled ${empty subcategoriaEnEdicion ? 'selected' : ''}>-- Elige el Origen --</option>
                                <c:forEach var="cat" items="${categorias}">
                                    <option value="${cat.idCategoria}" ${subcategoriaEnEdicion.idCategoriaAsociada == cat.idCategoria ? 'selected' : ''}>
                                    ${cat.nombreCategoria}
                                    </option>
                                </c:forEach>
                            </select>
                        </div>

                        <div class="mb-4">
                            <label class="form-label label-magico text-uppercase">Nombre Subcategoría</label>
                            <input type="text" name="nombreSubcategoria" class="form-control input-magico"
                                   value="${subcategoriaEnEdicion.nombreSubcategoria}"
                                   placeholder="Ej: Varitas de Sauce" required>
                        </div>

                        <div class="d-grid gap-2">
                            <button type="submit" class="btn btn-turquesa-magico py-2 fw-bold">
                                <i class="bi bi-lightning-charge me-1"></i>
                                ${not empty subcategoriaEnEdicion and subcategoriaEnEdicion.idSubcategoria != 0 ? 'Actualizar' : 'Manifestar'}
                            </button>
                            <c:if test="${not empty subcategoriaEnEdicion and subcategoriaEnEdicion.idSubcategoria != 0}">
                                <a href="${pageContext.request.contextPath}/gestion-subcategorias" class="btn btn-link text-muted text-decoration-none text-center small">Cancelar Edición</a>
                            </c:if>
                        </div>
                    </form>
                </div>
            </div>
        </div>

        <div class="col-md-8">
            <div class="card shadow-lg border-0 card-gestion-magica">
                <div class="card-body p-0">
                    <div class="table-responsive">
                        <table class="table table-hover align-middle mb-0 text-center">
                            <thead class="table-light text-uppercase small fw-bold">
                            <tr class="fila-cabecera">
                                <th class="py-3">Origen (Categoría)</th>
                                <th class="py-3">Nombre Subcategoría</th>
                                <th class="py-3 pe-4">Acciones</th>
                            </tr>
                            </thead>
                            <tbody>
                            <c:forEach var="s" items="${subcategorias}">
                                <tr>
                                    <td>
                                        <span class="badge rounded-pill badge-rol bg-info text-dark">
                                            ${s.nombreCategoria}
                                        </span>
                                    </td>
                                    <td class="fw-bold text-dark">${s.nombreSubcategoria}</td>
                                    <td class="pe-4">
                                        <div class="btn-group gap-2">
                                            <a href="${pageContext.request.contextPath}/gestion-subcategorias?action=edit&idSubcategoria=${s.idSubcategoria}"
                                               class="btn btn-editar-magico btn-sm btn-redondo shadow-sm">
                                                <i class="bi bi-pencil-square"></i>
                                            </a>

                                            <button type="button"
                                                    class="btn btn-eliminar-magico btn-sm btn-redondo shadow-sm"
                                                    data-bs-toggle="modal"
                                                    data-bs-target="#deleteSubModal"
                                                    onclick="prepararBorradoSub(${s.idSubcategoria}, '${s.nombreSubcategoria}')">
                                                <i class="bi bi-trash3-fill"></i>
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
</div>

<div class="modal fade modal-eliminar-magico" id="deleteSubModal" tabindex="-1" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content border-0 shadow-lg container-modal">
            <div class="modal-header border-0 justify-content-center pt-4 header-modal-limpio">
                <h5 class="modal-title fw-bold text-white">⚠️ ¿Eliminar Subcategoría?</h5>
            </div>
            <div class="modal-body p-4 text-center">
                <p class="text-muted mb-1 small text-uppercase">Vas a desvanecer:</p>
                <h4 id="nombreSubBorrar" class="fw-bold mb-3 nombre-eliminar"></h4>
                <div class="alert alert-warning py-2 mb-0 small text-start border-0 alert-aviso">
                    <i class="bi bi-exclamation-triangle-fill me-2 text-warning"></i>
                    Asegúrate de que no haya productos usando esta subcategoría o el hechizo fallará.
                </div>
            </div>
            <div class="modal-footer border-0 bg-light justify-content-center pb-4 gap-3">
                <button type="button" class="btn btn-secondary rounded-pill px-4" data-bs-dismiss="modal">Conservar</button>
                <a id="btnConfirmarBorrado" href="#" class="btn btn-eliminar-magico px-4">Eliminar Definitivamente</a>
            </div>
        </div>
    </div>
</div>

<%@ include file="../footer.jsp" %>