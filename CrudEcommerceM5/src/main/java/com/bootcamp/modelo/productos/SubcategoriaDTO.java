package com.bootcamp.modelo.productos;

public class SubcategoriaDTO {
    private int idSubcategoria;
    private int idCategoriaAsociada; // Esta es la FK que apunta a Categoría
    private String nombreSubcategoria;
    private String nombreCategoria; // Solo para mostrar el nombre del padre en las tablas

    // 1. Constructor Vacío
    public SubcategoriaDTO() {}

    // 2. Constructor Largo
    public SubcategoriaDTO(int idSubcategoria, int idCategoriaAsociada, String nombreSubcategoria, String nombreCategoria) {
        this.idSubcategoria = idSubcategoria;
        this.idCategoriaAsociada = idCategoriaAsociada;
        this.nombreSubcategoria = nombreSubcategoria;
        this.nombreCategoria = nombreCategoria;
    }

    // 3. Constructor Corto
    public SubcategoriaDTO(int idSubcategoria, int idCategoriaAsociada, String nombreSubcategoria) {
        this.idSubcategoria = idSubcategoria;
        this.idCategoriaAsociada = idCategoriaAsociada;
        this.nombreSubcategoria = nombreSubcategoria;
    }

    // --- GETTERS Y SETTERS CORREGIDOS ---
    public int getIdSubcategoria() { return idSubcategoria; }
    public void setIdSubcategoria(int idSubcategoria) { this.idSubcategoria = idSubcategoria; }

    public int getIdCategoriaAsociada() { return idCategoriaAsociada; }
    public void setIdCategoriaAsociada(int idCategoriaAsociada) { this.idCategoriaAsociada = idCategoriaAsociada; }

    public String getNombreSubcategoria() { return nombreSubcategoria; }
    public void setNombreSubcategoria(String nombreSubcategoria) { this.nombreSubcategoria = nombreSubcategoria; }

    public String getNombreCategoria() { return nombreCategoria; }
    public void setNombreCategoria(String nombreCategoria) { this.nombreCategoria = nombreCategoria; }
}