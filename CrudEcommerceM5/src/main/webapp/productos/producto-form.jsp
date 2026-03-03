<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ include file="../header.jsp" %>

<div class="container py-5">
    <div class="row justify-content-center">
        <div class="col-md-8 col-lg-7">
            <div class="card card-magica-form shadow-lg">
                <div class="card-header py-3">
                    <h5 class="mb-0 text-center fw-bold">
                        <c:choose>
                            <c:when test="${not empty producto.idProducto && producto.idProducto != 0}">
                                ✨ Modificar Artículo Mágico
                            </c:when>
                            <c:otherwise>
                                💎 Registrar Nuevo Tesoro
                            </c:otherwise>
                        </c:choose>
                    </h5>
                </div>

                <div class="card-body p-4">
                    <c:if test="${not empty error}">
                        <div class="alert alert-danger alert-dismissible fade show mb-4 shadow-sm border-0 animate__animated animate__shakeX"
                             style="border-radius: 15px; border-left: 5px solid #dc3545 !important;">
                            <div class="d-flex align-items-center">
                                <i class="bi bi-exclamation-octagon-fill fs-4 me-3"></i>
                                <div>
                                    <strong>¡Hechizo interrumpido!</strong><br>
                                    <span class="small">${error}</span>
                                </div>
                            </div>
                            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                        </div>
                    </c:if>

                    <form id="formProducto" action="${pageContext.request.contextPath}/productos"
                          method="post" enctype="multipart/form-data" class="needs-validation" novalidate>

                        <input type="hidden" name="idProducto" value="${not empty producto.idProducto ? producto.idProducto : 0}">
                        <input type="hidden" name="imagenActual" value="${producto.imagenProducto}">

                        <div class="row g-3">

                            <div class="col-12">
                                <label class="form-label fw-bold"><i class="bi bi-magic me-1"></i> NOMBRE DEL PRODUCTO</label>
                                <input type="text" name="nombreProducto"
                                       class="form-control ${not empty error and empty producto.nombreProducto ? 'is-invalid' : ''}"
                                       value="${producto.nombreProducto}" required>
                                <div class="invalid-feedback">Por favor, bautiza tu tesoro con un nombre.</div>
                            </div>

                            <div class="col-md-6">
                                <label class="form-label fw-bold"><i class="bi bi-coin me-1"></i> PRECIO (CLP)</label>
                                <div class="input-group">
                                    <span class="input-group-text">$</span>
                                    <input type="number" name="precioProducto"
                                           class="form-control ${not empty error and producto.precioProducto < 0 ? 'is-invalid' : ''}"
                                           step="1"
                                           min="0"
                                    value="<fmt:formatNumber value='${producto.precioProducto}' type='number' groupingUsed='false' maxFractionDigits='0' />"
                                    onfocus="this.select()" required>
                                    <div class="invalid-feedback">El precio no debe ser menor a 0.</div>
                                </div>
                            </div>

                            <div class="col-md-6">
                                <label class="form-label fw-bold"><i class="bi bi-box-seam me-1"></i> STOCK</label>
                                <input type="number" name="stockProducto"
                                       class="form-control ${not empty error and producto.stockProducto < 0 ? 'is-invalid' : ''}"
                                       step="1"
                                       min="0"
                                       value="${producto.stockProducto}"
                                       onfocus="this.select()" required>
                                <div class="invalid-feedback">No puedes tener stock negativo.</div>
                            </div>

                            <div class="col-12">
                                <label class="form-label fw-bold"><i class="bi bi-tags me-1"></i> SUBCATEGORÍA</label>
                                <select name="idSubcategoriaAsociada" class="form-select" required>
                                    <option value="" disabled ${producto.idSubcategoriaAsociada == 0 ? 'selected' : ''}>Selecciona...</option>
                                        <c:forEach var="sub" items="${subcategorias}">
                                            <option value="${sub.idSubcategoria}" ${producto.idSubcategoriaAsociada == sub.idSubcategoria ? 'selected' : ''}>
                                                ${sub.nombreCategoria} ➔ ${sub.nombreSubcategoria}
                                            </option>
                                        </c:forEach>
                                </select>
                            </div>

                            <div class="col-12">
                                <label class="form-label fw-bold"><i class="bi bi-image me-1"></i> IMAGEN</label>
                                <ul class="nav nav-pills nav-fill mb-2" id="imgTab" role="tablist">
                                    <li class="nav-item">
                                        <button class="nav-link active btn-sm fw-bold" data-bs-toggle="tab" data-bs-target="#galeria" type="button">📜 Galería</button>
                                    </li>
                                    <li class="nav-item">
                                        <button class="nav-link btn-sm fw-bold" data-bs-toggle="tab" data-bs-target="#subir" type="button">☁️ Subir Nueva</button>
                                    </li>
                                </ul>

                                <div class="tab-content border p-3 bg-white" style="border-radius: 15px;">
                                    <div class="tab-pane fade show active" id="galeria">
                                        <select name="imagenExistente" class="form-select">
                                            <option value="">--- Selecciona de la galería ---</option>
                                            <c:forEach var="img" items="${galeriaImagenes}">
                                                <option value="${img}" ${producto.imagenProducto == img ? 'selected' : ''}>${img}</option>
                                            </c:forEach>
                                        </select>
                                    </div>
                                    <div class="tab-pane fade" id="subir">
                                        <input type="file" name="imagenProducto" class="form-control" accept="image/*">
                                    </div>
                                </div>

                                <div class="preview-container text-center mt-3 p-2 border rounded bg-light">
                                    <p class="small text-muted mb-1">Vista Previa:</p>
                                    <img id="preview"
                                         src="${pageContext.request.contextPath}/ASSETS/IMG/${not empty producto.imagenProducto ? producto.imagenProducto : 'default.jpg'}"
                                         alt="Preview" style="width: 150px; height: 150px; object-fit: cover; border-radius: 15px;">
                                </div>
                            </div>

                            <div class="col-12">
                                <label class="form-label fw-bold label-magico">
                                    <i class="bi bi-chat-left-text me-1"></i> DESCRIPCIÓN
                                </label>
                                <textarea name="descripcionProducto"
                                          class="form-control input-magico-foco"
                                          rows="3"
                                          placeholder="Escribe la descripción del tesoro..."
                                          required>${producto.descripcionProducto}</textarea>
                            </div>
                        </div>

                        <div class="d-grid gap-2 mt-4">
                            <button type="button" onclick="validarYConfirmar()" class="btn btn-magico py-2 fw-bold shadow-sm">
                                ${producto.idProducto != 0 ? '💾 Actualizar Tesoro' : '🚀 Registrar Nuevo'}
                            </button>
                            <a href="${pageContext.request.contextPath}/productos" class="btn btn-link text-muted text-decoration-none">❌ Cancelar</a>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>

<div class="modal fade" id="modalConfirmarProducto" tabindex="-1" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content">
            <div class="modal-header bg-primary text-white">
                <h5 class="modal-title fw-bold"><i class="bi bi-check2-circle me-2"></i> ¿Confirmar Cambios?</h5>
                <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal"></button>
            </div>
            <div class="modal-body py-4 text-center">
                <p class="fs-5 mb-0">¿Estás seguro de guardar este artículo en la base de datos?</p>
            </div>
            <div class="modal-footer justify-content-center">
                <button type="button" class="btn btn-outline-secondary px-4" data-bs-dismiss="modal">Revisar</button>
                <button type="button" class="btn btn-primary px-4 fw-bold" onclick="enviarFormulario()">¡Sí, Guardar!</button>
            </div>
        </div>
    </div>
</div>

<%@ include file="../footer.jsp" %>