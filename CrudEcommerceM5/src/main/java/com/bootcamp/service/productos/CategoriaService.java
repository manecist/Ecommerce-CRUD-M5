package com.bootcamp.service.productos;

import com.bootcamp.dao.productos.CategoriaDAO;
import com.bootcamp.modelo.productos.CategoriaDTO;

import java.util.List;

public class CategoriaService {
    private final CategoriaDAO dao = new CategoriaDAO();

    /**
     * LISTAR (Llama a listarTodas del DAO)
     * @return
     */
    public List<CategoriaDTO> listarTodas() {
        return dao.listarTodas();
    }

    /**
     * BUSCAR (Llama a buscarPorId del DAO)
     * @param id
     * @return
     */
    public CategoriaDTO buscarPorId(int id) {
        return dao.buscarPorId(id);
    }

    /**
     * GUARDAR
     * @param cat
     */
    public void guardar(CategoriaDTO cat) {
        if (cat.getNombreCategoria() == null || cat.getNombreCategoria().isBlank()) {
            throw new IllegalArgumentException("El nombre es obligatorio.");
        }
        if (dao.existeNombre(cat.getNombreCategoria().trim())) {
            throw new IllegalArgumentException("Ya existe una categoría con ese nombre.");
        }
        dao.guardar(cat);
    }

    /**
     * ACTUALIZAR
     * @param cat
     */
    public void actualizar(CategoriaDTO cat) {
        if (cat.getNombreCategoria() == null || cat.getNombreCategoria().isBlank()) {
            throw new IllegalArgumentException("El nombre no puede estar vacío.");
        }
        // Validamos si el nombre cambió para revisar si el nuevo ya existe
        CategoriaDTO existente = dao.buscarPorId(cat.getIdCategoria());
        if (!existente.getNombreCategoria().equalsIgnoreCase(cat.getNombreCategoria())) {
            if (dao.existeNombre(cat.getNombreCategoria().trim())) {
                throw new IllegalArgumentException("Ese nombre ya está en uso por otra categoría.");
            }
        }
        dao.actualizar(cat);
    }

    /**
     * ELIMINAR
     * @param id
     */
    public void eliminar(int id) {
        dao.eliminar(id);
    }
}