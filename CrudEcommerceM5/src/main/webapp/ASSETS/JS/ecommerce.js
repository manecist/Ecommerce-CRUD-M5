
/* =========================================
   1. UTILIDADES Y CONFIGURACIÓN
   ========================================= */
/* --- UTILIDAD GLOBAL: Detecta la raíz del proyecto --- */
function getPath() {
    const pathName = window.location.pathname;
    // Buscamos la primera "/" y la segunda para extraer el nombre del proyecto
    const contextPath = pathName.substring(0, pathName.indexOf("/", 1));

    // Si el contextPath es "/" o está vacío, devolvemos vacío,
    // si no, devolvemos el nombre (ej: /CrudEcommerce)
    return (contextPath === "/" || contextPath === "") ? "" : contextPath;
}

const mostrarModal = (id) => {
    const el = document.getElementById(id);
    if (el && typeof bootstrap !== 'undefined') {
        const modal = new bootstrap.Modal(el);
        modal.show();
    }
};

/* =========================================
   2. NAVEGACIÓN Y CARRITO
   ========================================= */
document.addEventListener("DOMContentLoaded", () => {
    // Báculo para subir
    const btnSubir = document.getElementById("btnSubir");
    if (btnSubir) {
        window.addEventListener('scroll', () => {
            btnSubir.style.display = (window.scrollY > 100) ? "block" : "none";
        });
    }

    // Persistencia del carrito
    const numerito = document.getElementById('numerito');
    if (numerito) numerito.innerText = localStorage.getItem('carritoCantidad') || "0";

    // Auto-cierre de alertas
    document.querySelectorAll('.alert-magica').forEach(alerta => {
        setTimeout(() => {
            const ins = bootstrap.Alert.getOrCreateInstance(alerta);
            if(ins) ins.close();
        }, 5000);
    });
});

function agregarAlCarrito(nombre) {
    const numerito = document.getElementById('numerito');
    if (numerito) {
        let total = parseInt(numerito.innerText) + 1;
        numerito.innerText = total;
        localStorage.setItem('carritoCantidad', total);
    }
    const toastEl = document.getElementById('toastCarrito');
    if (toastEl) {
        const toast = new bootstrap.Toast(toastEl);
        const mensaje = document.getElementById('mensajeToast');
        if(mensaje) mensaje.innerHTML = `<b>✨ ¡Hechizo!</b><br>${nombre} añadido.`;
        toast.show();
    }
}

/* =========================================
   3. GESTIÓN DE FORMULARIOS (PROD / CONT / USER)
   ========================================= */

// --- PRODUCTOS ---
function validarYConfirmar() {
    const form = document.getElementById('formProducto');
    if (form) {
        const precio = form.querySelector('input[name="precioProducto"]');
        if (precio) precio.value = precio.value.replace(',', '.');

        if (form.checkValidity()) {
            mostrarModal('modalConfirmarProducto');
        } else {
            form.classList.add('was-validated');
            const err = form.querySelector(':invalid');
            if (err) err.scrollIntoView({ behavior: 'smooth', block: 'center' });
        }
    }
}

function enviarFormulario() {
    const form = document.getElementById('formProducto');
    if (form) form.submit();
}

// --- CONTACTO ---
function validarYMostrarContacto() {
    const form = document.getElementById('contactForm');
    if (form && form.checkValidity()) {
        mostrarModal('modalConfirmarContacto');
    } else if (form) {
        form.classList.add('was-validated');
    }
}

function enviarMensajeDefinitivo() {
    const form = document.getElementById('contactForm');
    if (form) form.submit();
}

// --- USUARIOS (Esta es la que te falta para el user-form) ---
function validarYEnviar() {
    const form = document.getElementById('formUsuario');
    const passInput = document.getElementById('passInput');
    const idInput = document.getElementById('idUsuarioInput');

    if (!form || !passInput || !idInput) return;

    // Regex: 8+ carac, 1 Mayus, 1 Minus, 1 Num, 1 Símbolo
    const regex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&.])[A-Za-z\d@$!%*?&.]{8,}$/;

    // Si es nuevo (ID 0) O si el usuario escribió algo en el campo de pass
    if (idInput.value === "0" || passInput.value.length > 0) {
        if (!regex.test(passInput.value)) {
            passInput.setCustomValidity("Invalido");
        } else {
            passInput.setCustomValidity("");
        }
    } else {
        // Si está editando y el campo está vacío, es válido (no cambia pass)
        passInput.setCustomValidity("");
    }

    if (form.checkValidity()) {
        mostrarModal('confirmModal');
    } else {
        form.classList.add('was-validated');
        const err = form.querySelector(':invalid');
        if (err) err.scrollIntoView({ behavior: 'smooth', block: 'center' });
    }
}
/* =========================================
   4. VALIDACIONES ESPECIALES (EDAD Y PASS)
   ========================================= */
document.addEventListener("DOMContentLoaded", function() {
    const inputFecha = document.querySelector('input[name="fechaNacimiento"]');
    const spanEdad = document.getElementById("verEdad");

    if (inputFecha && spanEdad) {
        inputFecha.addEventListener("change", function() {
            const fecha = new Date(this.value);
            const hoy = new Date();
            let edad = hoy.getFullYear() - fecha.getFullYear();
            if (hoy.getMonth() < fecha.getMonth() || (hoy.getMonth() === fecha.getMonth() && hoy.getDate() < fecha.getDate())) edad--;

            if (this.value === "" || edad < 18 || edad > 105) {
                this.setCustomValidity("Invalido");
                spanEdad.innerText = "(⚠️ Fecha Inválida)";
                spanEdad.style.color = "#ff6b6b";
            } else {
                this.setCustomValidity("");
                spanEdad.innerText = "(✨ " + edad + " años)";
                spanEdad.style.color = "#00d4aa";
            }
        });
    }
});

function togglePassword() {
    const input = document.getElementById('passInput') || document.getElementById('pass');
    if (input) input.type = (input.type === 'password') ? 'text' : 'password';
}

/* =========================================
   5. IMÁGENES (VISTA PREVIA)
   ========================================= */

// 1. Para cuando eligen una imagen que ya existe en el servidor
function cambiarImagen() {
    const select = document.querySelector('select[name="imagenExistente"]');
    const preview = document.getElementById("preview");

    if (select && select.value && preview) {
        // Obtenemos la base de la URL actual (ej: /CrudEcommerce)
        const pathArray = window.location.pathname.split('/');
        const contextPath = "/" + pathArray[1];

        // Construimos la ruta absoluta
        preview.src = contextPath + "/ASSETS/IMG/" + select.value;

        const fileIn = document.querySelector('input[name="imagenProducto"]');
        if(fileIn) fileIn.value = "";
    }
}

//  Escuchador global para cuando el usuario sube una imagen nueva desde su PC
document.addEventListener("change", (e) => {
    // Caso 1: Cambio en el Select de Galería
    if (e.target.name === "imagenExistente") {
        const preview = document.getElementById("preview");
        if (e.target.value && preview) {
            const pathArray = window.location.pathname.split('/');
            const contextPath = "/" + pathArray[1];
            preview.src = contextPath + "/ASSETS/IMG/" + e.target.value;

            const fileIn = document.querySelector('input[name="imagenProducto"]');
            if(fileIn) fileIn.value = "";
        }
    }

    //  Subida de archivo (Tu código original que ya funcionaba)
    if (e.target.name === "imagenProducto") {
        const reader = new FileReader();
        reader.onload = (ev) => {
            const preview = document.getElementById("preview");
            if (preview) {
                preview.src = ev.target.result;
                const sel = document.querySelector('select[name="imagenExistente"]');
                if(sel) sel.value = "";
            }
        };
        if (e.target.files[0]) reader.readAsDataURL(e.target.files[0]);
    }
});
/* =========================================
    6. ELIMINACIÓN Y GLOBAL (UNIFICADO)
    ========================================= */

function configurarBorrado(id, nombre, tipo) {
    const mapeo = {
        'usuario': {
            texto: 'nombreUsuarioBorrar',
            btn: 'confirmarBorradoUserBtn',
            url: '/usuarios?action=delete&id='
        },
        'producto': {
            texto: 'nombreProdEliminar',
            btn: 'linkConfirmarEliminar',
            url: '/productos?action=delete&idProducto='
        },
        'subcategoria': {
            texto: 'nombreSubBorrar',
            btn: 'btnConfirmarBorrado',
            url: '/gestion-subcategorias?action=delete&idSubcategoria='
        },
        // AGREGAMOS CATEGORÍA AQUÍ (Mismo estilo que los anteriores)
        'categoria': {
            texto: 'nombreCatBorrar',
            btn: 'btnConfirmarBorradoCat',
            url: '/gestion-categorias?action=delete&idCategoria='
        }
    };

    const config = mapeo[tipo];
    if (!config) return;

    const txtEl = document.getElementById(config.texto);
    const btnEl = document.getElementById(config.btn);

    // 1. Inyectar el nombre del objeto a borrar en el modal
    if (txtEl) txtEl.innerText = nombre;

    // 2. Configurar el enlace de acción
    if (btnEl) {
        const finalUrl = getPath() + config.url + id;

        // Si el botón es un <a> (como en usuarios, subcategorías y categorías)
        if (btnEl.tagName === 'A') {
            btnEl.href = finalUrl;
        } else {
            // Si es un <button> (como en productos)
            btnEl.onclick = () => window.location.href = finalUrl;
        }
    }
}

/* --- PUERTAS DE ENTRADA (Llamadas desde los JSP) --- */

function prepararBorradoUsuario(id, nombre) {
    configurarBorrado(id, nombre, 'usuario');
}

function confirmarEliminar(id, nombre) {
    configurarBorrado(id, nombre, 'producto');
    mostrarModal('modalEliminar');
}

function prepararBorradoSub(id, nombre) {
    configurarBorrado(id, nombre, 'subcategoria');
}

// Nueva puerta para categorias-list.jsp
function prepararBorradoCat(id, nombre) {
    configurarBorrado(id, nombre, 'categoria');
}
/* =========================================
   ADAPTACIÓN PARA LOGIN EN ecommerce.js
   ========================================= */

document.addEventListener("DOMContentLoaded", () => {
    // 1. Si detectamos el parámetro ?logout=true en la URL, limpiamos el carrito
    const urlParams = new URLSearchParams(window.location.search);
    if (urlParams.get('logout') === 'true') {
        localStorage.removeItem('carritoCantidad');
        const numerito = document.getElementById('numerito');
        if (numerito) numerito.innerText = "0";
    }

    // 2. Manejo del botón Toggle en el Login (específico para el id="btnToggle")
    const btnToggle = document.getElementById('btnToggle');
    if (btnToggle) {
        btnToggle.addEventListener('click', function() {
            const passField = document.getElementById('pass');
            const icon = document.getElementById('eyeIcon');
            if (passField.type === 'password') {
                passField.type = 'text';
                icon.classList.replace('bi-eye', 'bi-eye-slash');
            } else {
                passField.type = 'password';
                icon.classList.replace('bi-eye-slash', 'bi-eye');
            }
        });
    }
});