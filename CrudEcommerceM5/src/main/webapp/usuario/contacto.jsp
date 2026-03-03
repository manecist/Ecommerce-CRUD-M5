<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ include file="../header.jsp" %>

<main>
    <div class="container mt-5 mb-5">
        <div class="row justify-content-center">
            <div class="col-md-6">
                <c:if test="${param.msj == 'contacto_ok'}">
                    <div class="alert alert-magica-success shadow-sm border-0 mb-4 animate__animated animate__fadeInDown">
                        <div class="d-flex align-items-center">
                            <div class="icon-circle-success me-3">
                                <i class="bi bi-send-check-fill text-white fs-4"></i>
                            </div>
                            <div>
                                <h5 class="fw-bold mb-0" style="color: #008f72;">¡Mensaje Enviado!</h5>
                                <p class="mb-0 small text-muted">Tus palabras han cruzado el umbral. Los guardianes te responderán pronto.</p>
                            </div>
                        </div>
                    </div>
                </c:if>
                <div class="card shadow-lg card-magica-form border-0" id="formularioContacto">
                    <div class="card-header text-center py-3">
                        <h4 class="fw-bold mb-0">✨ Mensaje a la Alianza</h4>
                    </div>
                    <div class="card-body p-4 bg-white container-inferior-magic">

                        <form action="${pageContext.request.contextPath}/contacto"
                              method="POST"
                              id="contactForm"
                              class="needs-validation"
                              novalidate>

                            <div class="mb-3">
                                <label for="name" class="form-label label-magico text-uppercase">Nombre completo</label>
                                <input type="text" name="txtNombre" class="form-control input-magico" id="name"
                                       placeholder="Ej: Merlin Ambrosius" required>
                                <div class="invalid-feedback">
                                    Por favor, cuéntanos quién nos contacta.
                                </div>
                            </div>

                            <div class="mb-3">
                                <label for="email" class="form-label label-magico text-uppercase">Correo Electrónico</label>
                                <input type="email" name="txtEmail" class="form-control input-magico" id="email"
                                       placeholder="nombre@magia.com" required>
                                <div class="invalid-feedback">
                                    Necesitamos un correo místico válido para responderte.
                                </div>
                            </div>

                            <div class="mb-3">
                                <label for="subject" class="form-label label-magico text-uppercase">Asunto</label>
                                <input type="text" name="txtAsunto" class="form-control input-magico" id="subject"
                                       placeholder="¿Qué hechizo necesitas?" required>
                                <div class="invalid-feedback">
                                    Dinos brevemente el motivo de tu mensaje.
                                </div>
                            </div>

                            <div class="mb-3">
                                <label for="message" class="form-label label-magico text-uppercase">Mensaje</label>
                                <textarea name="txtMensaje" class="form-control input-magico" id="message" rows="4"
                                          placeholder="Escribe aquí tu petición..." required minlength="10"></textarea>
                                <div class="invalid-feedback">
                                    El mensaje debe ser más claro (mínimo 10 caracteres).
                                </div>
                            </div>

                            <div class="d-grid gap-2">
                                <button type="button" onclick="validarYMostrarContacto()" class="btn btn-enviar-magic py-2 fw-bold">
                                    Enviar Hechizo 🌙
                                </button>
                            </div>

                            <div class="text-center mt-3">
                                <a href="${pageContext.request.contextPath}/home" class="btn btn-link text-muted btn-sm text-decoration-none">
                                    <i class="bi bi-arrow-left"></i> Volver al inicio
                                </a>
                            </div>

                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>
</main>

<div class="modal fade" id="modalConfirmarContacto" tabindex="-1" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content border-0 shadow-lg container-modal">
            <div class="modal-header border-0 bg-light justify-content-center pt-4">
                <h5 class="modal-title fw-bold titulo-magico">✨ ¿Confirmar Envío?</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <div class="modal-body py-4 text-center">
                <i class="bi bi-send-check display-1 icon-turquesa"></i>
                <p class="mt-3">Tu mensaje será enviado a los guardianes de <b>Magical Alliance</b>.</p>
                <small class="text-muted">Nos pondremos en contacto contigo mediante una señal astral (o email).</small>
            </div>
            <div class="modal-footer border-0 justify-content-center pb-4 gap-3">
                <button type="button" class="btn btn-editar-magico px-4" data-bs-dismiss="modal">Revisar</button>
                <button type="button" class="btn btn-turquesa-magico px-4" onclick="enviarMensajeDefinitivo()">Sí, enviar 🚀</button>
            </div>
        </div>
    </div>
</div>

<%@ include file="../footer.jsp" %>