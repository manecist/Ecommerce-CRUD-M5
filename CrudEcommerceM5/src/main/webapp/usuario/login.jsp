<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ include file="../header.jsp" %>

<div class="container py-5">
    <div class="row justify-content-center">
        <div class="col-md-5 col-lg-4">

            <c:if test="${param.logout == 'true'}">
                <div class="alert alert-magica bg-white border-0 shadow-sm mb-3 text-center" role="alert">
                    <i class="bi bi-magic me-2" style="color: #f480ff;"></i> ✨ ¡Sesión cerrada! Vuelve pronto.
                </div>
            </c:if>
            <c:if test="${not empty mensajeError}">
                const errorModal = new bootstrap.Modal(document.getElementById('errorLoginModal'));
                errorModal.show();
            </c:if>

            <div class="card card-login shadow-lg border-0">
                <div class="card-body p-4 text-center">
                    <img src="${pageContext.request.contextPath}/ASSETS/IMG/inicio sesion.png"
                         class="img-fluid mb-3 logo-login shadow-sm rounded-circle" alt="Logo" width="100">
                    <h3 class="fw-bold mb-4 titulo-magico">Iniciar Sesión</h3>

                    <form action="${pageContext.request.contextPath}/login" method="post" class="needs-validation" novalidate>

                        <div class="form-floating mb-3">
                            <input type="text" name="username" class="form-control input-magico" id="user" placeholder="Usuario" required>
                            <label for="user" class="label-magico">Nombre de Usuario</label>
                            <div class="invalid-feedback text-start">Ingresa tu nombre de guardian(a).</div>
                        </div>

                        <div class="input-group mb-4 has-validation">
                            <div class="form-floating flex-grow-1">
                                <input type="password" name="password" class="form-control input-magico" id="pass"
                                       placeholder="Contraseña" required
                                       pattern="^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&.]).{8,}$"
                                       style="border-top-right-radius: 0 !important; border-bottom-right-radius: 0 !important;">
                                <label for="pass" class="label-magico">Contraseña</label>
                            </div>
                            <button class="btn btn-outline-secondary border-start-0" type="button" id="btnToggle"
                                    style="border-top-right-radius: 10px; border-bottom-right-radius: 10px;">
                                <i class="bi bi-eye label-magico" id="eyeIcon"></i>
                            </button>

                            <div class="invalid-feedback text-start">
                                Mínimo 8 caracteres, una mayúscula, un número y un símbolo.
                            </div>
                        </div>

                        <button type="submit" class="btn btn-magico-login w-100 py-2 fw-bold shadow-sm">
                            <i class="bi bi-magic me-2"></i>Entrar a la Alianza
                        </button>
                    </form>

                    <div class="mt-3">
                        <a href="${pageContext.request.contextPath}/home" class="btn btn-link btn-sm text-decoration-none text-muted">
                            <i class="bi bi-eye me-1"></i> Continuar como Invitado
                        </a>
                    </div>

                    <div class="mt-4 border-top pt-3">
                        <p class="small text-muted mb-1">¿Eres nueva guardiana?</p>
                        <a href="${pageContext.request.contextPath}/usuarios?action=new"
                           class="btn btn-editar-magico btn-sm px-4 fw-bold">
                            Crea tu cuenta aquí
                        </a>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<div class="modal fade modal-eliminar-magico"
     id="errorLoginModal"
     data-trigger-error="${not empty mensajeError ? 'true' : 'false'}"
     tabindex="-1" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered modal-sm">
        <div class="modal-content border-0 shadow-lg">
            <div class="modal-body text-center p-4">
                <i class="bi bi-exclamation-octagon display-3" style="color: #f480ff;"></i>
                <h5 class="fw-bold mt-3 titulo-magico">¡Hechizo Fallido!</h5>
                <p class="text-muted small">${mensajeError}</p>
                <button type="button" class="btn btn-eliminar-magico btn-sm px-4" data-bs-dismiss="modal">Reintentar</button>
            </div>
        </div>
    </div>
</div>

<%@ include file="../footer.jsp" %>