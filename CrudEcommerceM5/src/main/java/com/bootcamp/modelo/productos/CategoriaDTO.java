package com.bootcamp.modelo.productos;

public class CategoriaDTO {
    private int idCategoria;
    private String nombreCategoria;
    private String imagenBanner;

    public CategoriaDTO() {}

    public CategoriaDTO(int idCategoria, String nombreCategoria, String imagenBanner) {
        this.idCategoria = idCategoria;
        this.nombreCategoria = nombreCategoria;
        this.imagenBanner = imagenBanner;
    }

    // Getters y Setters (Obligatorios para el carrusel en el JSP)
    public int getIdCategoria() { return idCategoria; }
    public void setIdCategoria(int idCategoria) { this.idCategoria = idCategoria; }

    public String getNombreCategoria() { return nombreCategoria; }
    public void setNombreCategoria(String nombreCategoria) { this.nombreCategoria = nombreCategoria; }

    public String getImagenBanner() { return imagenBanner; }
    public void setImagenBanner(String imagenBanner) { this.imagenBanner = imagenBanner; }
}
