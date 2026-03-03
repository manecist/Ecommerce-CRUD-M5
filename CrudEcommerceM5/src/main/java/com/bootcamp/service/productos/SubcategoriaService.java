package com.bootcamp.service.productos;
import com.bootcamp.dao.productos.SubcategoriaDAO;
import com.bootcamp.modelo.productos.SubcategoriaDTO;

import java.util.List;

public class SubcategoriaService {
    private final SubcategoriaDAO dao = new SubcategoriaDAO();

    // LISTAR TODAS
    public List<SubcategoriaDTO> listarTodas() {
        return dao.listarTodas();
    }

    // BUSCAR POR ID
    public SubcategoriaDTO buscarPorId(int id) {
        if (id <= 0) return null;
        return dao.buscarPorId(id);
    }

    //  GUARDAR
    public void guardar(SubcategoriaDTO sub) {
        validarSubcategoria(sub);
        // Podrías agregar aquí una validación extra si no quieres
        // nombres de subcategoría repetidos dentro de la misma categoría
        dao.guardar(sub);
    }

    //  ACTUALIZAR
    public void actualizar(SubcategoriaDTO sub) {
        validarSubcategoria(sub);
        if (sub.getIdSubcategoria() <= 0) {
            throw new IllegalArgumentException("ID de subcategoría no válido para actualizar.");
        }
        dao.actualizar(sub);
    }

    //  ELIMINAR
    public void eliminar(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("ID no válido para eliminar.");
        }
        dao.eliminar(id);
    }

    public List<SubcategoriaDTO> listarPorCategoria(int idCat) {
        return dao.listarPorCategoria(idCat);
    }

    // validación
    private void validarSubcategoria(SubcategoriaDTO sub) {
        if (sub.getNombreSubcategoria() == null || sub.getNombreSubcategoria().isBlank()) {
            throw new IllegalArgumentException("El nombre de la subcategoría es obligatorio.");
        }
        if (sub.getIdCategoriaAsociada() <= 0) {
            throw new IllegalArgumentException("Debe seleccionar una categoría padre válida.");
        }
    }
}