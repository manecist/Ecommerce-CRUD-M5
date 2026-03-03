<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ include file="../header.jsp" %>

<div class="container py-5">
    <div class="row justify-content-center">
        <div class="col-md-8 col-lg-7">

            <c:if test="${not empty error}">
                <div class="alert alert-magica bg-white border-0 shadow-sm mb-4 text-danger">
                    <i class="bi bi-exclamation-triangle-fill me-2" style="color: #6907ab;"></i> ${error}
                    <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                </div>
            </c:if>

            <div class="card shadow-lg card-magica-form border-0">
                <div class="card-header text-center py-3">
                    <h4 class="mb-0 fw-bold">
                        <c:choose>
                            <c:when test="${not empty usuario && usuario.id != 0}">
                                ✨ Actualizar Esencia del Usuario
                            </c:when>
                            <c:otherwise>
                                💎 Registrar Nueva Guardiana
                            </c:otherwise>
                        </c:choose>
                    </h4>
                </div>
                <div class="card-body p-4 p-md-5 bg-white" style="border-radius: 0 0 30px 30px;">

                    <form id="formUsuario" action="${pageContext.request.contextPath}/usuarios" method="post" class="needs-validation" novalidate>

                        <input type="hidden" name="action" value="${not empty usuario && usuario.id != 0 ? 'update' : 'insert'}">
                        <input type="hidden" name="id" id="idUsuarioInput" value="${not empty usuario ? usuario.id : 0}">

                        <div class="row g-3">
                            <div class="col-12">
                                <label class="form-label label-magico text-uppercase">Nombre Completo</label>
                                <input type="text" name="nombreCompleto" class="form-control input-magico"
                                       placeholder="Ej: Luna Amatista" value="${usuario.nombreCompleto}" required>
                                <div class="invalid-feedback">Por favor, ingresa el nombre de la guardian(a).</div>
                            </div>

                            <div class="col-md-6">
                                <label class="form-label label-magico text-uppercase">
                                    Fecha de Nacimiento <span id="verEdad" class="small" style="color: #00d4aa;"></span>
                                </label>
                                <input type="date" name="fechaNacimiento" class="form-control input-magico"
                                       value="${usuario.fechaNacimiento}" required>
                            </div>

                            <div class="col-md-6">
                                <label class="form-label label-magico text-uppercase">Teléfono Místico</label>
                                <input type="tel" name="telefono" class="form-control input-magico"
                                       placeholder="+569..." value="${usuario.telefono}" required>
                            </div>

                            <div class="col-12">
                                <label class="form-label label-magico text-uppercase">Correo Electrónico</label>
                                <input type="email" name="email" class="form-control input-magico"
                                       placeholder="cristal@magia.com" value="${usuario.email}" required>
                                <div class="invalid-feedback">Ingresa un correo místico válido.</div>
                            </div>

                            <div class="col-md-6">
                                <label class="form-label label-magico text-uppercase">Nombre de Usuario</label>
                                <input type="text" name="username" class="form-control input-magico"
                                       placeholder="Nombre de guardian(a)" value="${usuario.username}" required>
                            </div>

                            <div class="col-md-6">
                                <label class="form-label label-magico text-uppercase">Rango / Rol</label>
                                <c:choose>
                                    <c:when test="${sessionScope.usuarioLogueado.rol == 'admin'}">
                                        <select name="rol" class="form-select input-magico">
                                            <option value="usuario" ${usuario.rol == 'usuario' ? 'selected' : ''}>Usuario</option>
                                            <option value="admin" ${usuario.rol == 'admin' ? 'selected' : ''}>Administrador</option>
                                        </select>
                                    </c:when>
                                    <c:otherwise>
                                        <input type="text" class="form-control bg-light border-0" value="${not empty usuario ? usuario.rol : 'Usuario'}" readonly>
                                        <input type="hidden" name="rol" value="${not empty usuario ? usuario.rol : 'usuario'}">
                                    </c:otherwise>
                                </c:choose>
                            </div>

                            <div class="col-12">
                                <label class="form-label label-magico text-uppercase">Contraseña Sagrada</label>
                                <div class="input-group">
                                    <input type="password" id="passInput" name="password" class="form-control input-magico"
                                           placeholder="${not empty usuario && usuario.id != 0 ? '✨ Dejar en blanco para no cambiar' : '🌙 Seguridad Máxima'}"
                                           ${(empty usuario || usuario.id == 0) ? 'required' : ''}>

                                    <span class="input-group-text bg-white border-magico-right" style="cursor: pointer; border: 1px solid #6907ab; border-left: none; border-radius: 0 15px 15px 0;" onclick="togglePassword()">
                                        <i class="bi bi-eye-slash" id="toggleIcon" style="color: #6907ab;"></i>
                                    </span>
                                </div>
                                <div id="passwordHelp" class="form-text small" style="color: #6907ab; font-weight: bold;">
                                    ✨ Requisitos: 8+ caracteres, Mayúscula, Número y Símbolo (@#$...).
                                </div>
                            </div>
                        </div>

                        <div class="d-grid gap-2 mt-4 pt-3">
                            <button id="botonEnviar" type="button" class="btn btn-guardar-magico py-2 fw-bold" onclick="validarYEnviar()">
                                <i class="bi bi-magic me-2"></i>
                                ${not empty usuario && usuario.id != 0 ? 'Actualizar Perfil' : 'Manifestar Cuenta'}
                            </button>

                            <a href="${pageContext.request.contextPath}/home" class="btn btn-link text-muted text-decoration-none text-center small">
                                <i class="bi bi-arrow-left"></i> Volver a la Alianza
                            </a>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>

<div class="modal fade" id="confirmModal" tabindex="-1" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered modal-lg">
        <div class="modal-content border-0 shadow-lg" style="border-radius: 30px;">

            <div class="modal-header border-0 bg-dark justify-content-center pt-4 pb-3">
                <h4 class="modal-title fw-bold text-white text-center">
                    ✨ Confirmar Acción Mística ✨
                </h4>
            </div>

            <div class="modal-body text-center py-5">
                <i class="bi bi-stars display-1" style="color: #00d4aa;"></i>

                <p class="mt-4 mb-0 text-muted px-5 fs-5">
                    ¿Estás segura de que deseas ${not empty usuario && usuario.id != 0 ? 'actualizar los datos de tu esencia' : 'unirte a nuestra Alianza Mística'}?
                </p>
            </div>

            <div class="modal-footer border-0 justify-content-center pb-5 gap-4">
                <button type="button" class="btn btn-secondary rounded-pill px-5 py-2" data-bs-dismiss="modal">
                    Volver a Revisar
                </button>
                <button type="button" class="btn btn-turquesa-magico px-5 py-2 fw-bold" onclick="document.getElementById('formUsuario').submit();">
                    Sí, Proceder
                </button>
            </div>
        </div>
    </div>
</div>

<%@ include file="../footer.jsp" %>