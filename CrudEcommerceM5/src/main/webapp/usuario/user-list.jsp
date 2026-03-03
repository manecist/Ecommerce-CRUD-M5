<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ include file="../header.jsp" %>

<div class="container py-4">

    <div class="mb-4">
        <c:if test="${not empty param.msj or not empty mensajeExito or not empty mensajeError}">
            <c:choose>
                <c:when test="${param.msj == 'save_ok' or not empty mensajeExito}">
                    <div class="alert alert-magica bg-white border-0 shadow-sm text-success">
                        <i class="bi bi-check-circle-fill me-2 icon-turquesa"></i>
                        ✨ ¡Hechizo de guardado exitoso!
                    </div>
                </c:when>
                <c:when test="${param.msj == 'delete_ok'}">
                    <div class="alert alert-magica bg-white border-0 shadow-sm text-info">
                        <i class="bi bi-person-check-fill me-2 icon-rosa"></i>
                        🌙 La guardian(a) ha dejado la alianza.
                    </div>
                </c:when>
                <c:when test="${not empty mensajeError}">
                    <div class="alert alert-magica bg-white border-0 shadow-sm text-danger">
                        <i class="bi bi-exclamation-octagon-fill me-2 icon-morado"></i>
                        🔮 Hubo una interrupción: ${mensajeError}
                    </div>
                </c:when>
            </c:choose>
        </c:if>
    </div>

    <div class="card shadow-lg card-gestion-magica border-0">
        <div class="card-header bg-white py-4 d-flex justify-content-between align-items-center header-con-borde">
            <h5 class="mb-0 fw-bold titulo-magico">💎 Guardianes de la Alianza</h5>
            <a href="${pageContext.request.contextPath}/usuarios?action=new" class="btn btn-turquesa-magico px-4">
                <i class="bi bi-person-plus-fill me-1"></i> Invocar Guardian(a)
            </a>
        </div>

        <div class="card-body p-0">
            <div class="table-responsive">
                <table class="table table-hover align-middle mb-0 text-center">
                    <thead class="table-light text-uppercase small fw-bold">
                    <tr class="fila-cabecera">
                        <th class="py-3 ps-4 text-start col-nombre">Nombre Completo</th>
                        <th class="py-3 col-usuario">Usuario</th>
                        <th class="py-3">Email</th>
                        <th class="py-3">Rango</th>
                        <th class="py-3 pe-4">Acciones</th>
                    </tr>
                    </thead>

                    <tbody>
                    <c:forEach var="u" items="${usuarios}">
                        <tr>
                            <td data-label="Nombre" class="ps-4 text-start fw-bold text-dark">
                                ${u.nombreCompleto}
                            </td>
                            <td data-label="Usuario">
                                <span class="label-magico-small">@</span>${u.username}
                            </td>
                            <td data-label="Email" class="text-muted small">
                                ${u.email}
                            </td>
                            <td data-label="Rango">
                                <span class="badge rounded-pill ${u.rol == 'admin' ? 'bg-danger' : 'bg-info text-dark'}">
                                    ${u.rol == 'admin' ? '🌙 Admin' : '🌸 Guardian(a)'}
                                </span>
                            </td>
                            <td data-label="Acciones">
                                <div class="acciones-container">
                                    <a href="${pageContext.request.contextPath}/usuarios?action=edit&id=${u.id}"
                                       class="btn-accion-editar mx-2" title="Editar">
                                        <i class="bi bi-pencil-square fs-5"></i>
                                    </a>
                                    <a href="#" class="btn-accion-eliminar mx-2"
                                       onclick="prepararBorradoUsuario('${u.id}', '${u.nombreCompleto}')"
                                       data-bs-toggle="modal" data-bs-target="#deleteUserModal">
                                        <i class="bi bi-trash3 fs-5"></i>
                                    </a>
                                </div>
                            </td>
                        </tr>
                    </c:forEach>
                    </tbody>

                </table>
            </div>
        </div>

        <div class="card-footer bg-light text-muted small py-3 text-center border-0 footer-mistico">
            ✨ Conectados al Oráculo de Datos: <strong>ecommerce_m5</strong> ✨
        </div>
    </div>
</div>

<div class="modal fade" id="deleteUserModal" tabindex="-1" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content border-0 shadow-lg">
            <div class="modal-header bg-dark text-white border-bottom border-magico justify-content-center">
                <h5 class="modal-title fw-bold">⚠️ ¿Desvanecer Guardian(a)?</h5>
            </div>
            <div class="modal-body text-center p-4">
                <p class="text-muted mb-1 small text-uppercase">Estás por eliminar a:</p>
                <h4 id="nombreUsuarioBorrar" class="fw-bold mb-3" style="color: #6907ab;"></h4>
                <div class="alert alert-warning py-2 mb-0 small text-start border-0">
                    <i class="bi bi-exclamation-triangle-fill me-2 text-warning"></i>
                    Esta acción es irreversible. El(a) guardian(a) perderá su acceso.
                </div>
            </div>
            <div class="modal-footer border-0 bg-light justify-content-center pb-4 gap-3">
                <button type="button" class="btn btn-secondary rounded-pill px-4" data-bs-dismiss="modal">Cancelar</button>
                <a id="confirmarBorradoUserBtn" href="#" class="btn btn-eliminar-magico px-4">Sí, Eliminar</a>
            </div>
        </div>
    </div>
</div>

<%@ include file="../footer.jsp" %>