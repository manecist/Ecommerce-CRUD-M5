package com.bootcamp.modelo.productos;

import java.math.BigDecimal;

public class ProductoDTO {
    private int idProducto;
    private int idSubcategoriaAsociada;
    private String nombreProducto;
    private String descripcionProducto;
    private double precioProducto;
    private int stockProducto;
    private String imagenProducto;
    private String nombreSubcategoria;
    private String nombreCategoria;

    public ProductoDTO() {
    }

    // Constructor completo (Para Listados con JOIN)
    public ProductoDTO(int idProducto, int idSubcategoriaAsociada, String nombreProducto,
                       String descripcionProducto, double precioProducto, int stockProducto,
                       String imagenProducto, String nombreSubcategoria, String nombreCategoria) {
        this.idProducto = idProducto;
        this.idSubcategoriaAsociada = idSubcategoriaAsociada;
        this.nombreProducto = nombreProducto;
        this.descripcionProducto = descripcionProducto;
        this.precioProducto = precioProducto;
        this.stockProducto = stockProducto;
        this.imagenProducto = imagenProducto;
        this.nombreSubcategoria = nombreSubcategoria;
        this.nombreCategoria = nombreCategoria;
    }

    public int getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }

    public int getIdSubcategoriaAsociada() {
        return idSubcategoriaAsociada;
    }

    public void setIdSubcategoriaAsociada(int idSubcategoriaAsociada) {
        this.idSubcategoriaAsociada = idSubcategoriaAsociada;
    }

    public String getNombreProducto() {
        return nombreProducto;
    }

    public void setNombreProducto(String nombreProducto) {
        this.nombreProducto = nombreProducto;
    }

    public String getDescripcionProducto() {
        return descripcionProducto;
    }

    public void setDescripcionProducto(String descripcionProducto) {
        this.descripcionProducto = descripcionProducto;
    }

    public double getPrecioProducto() {
        return precioProducto;
    }

    public void setPrecioProducto(double precioProducto) {
        this.precioProducto = precioProducto;
    }

    public int getStockProducto() {
        return stockProducto;
    }

    public void setStockProducto(int stockProducto) {
        this.stockProducto = stockProducto;
    }

    public String getImagenProducto() {
        return imagenProducto;
    }

    public void setImagenProducto(String imagenProducto) {
        this.imagenProducto = imagenProducto;
    }

    public String getNombreSubcategoria() {
        return nombreSubcategoria;
    }

    public void setNombreSubcategoria(String nombreSubcategoria) {
        this.nombreSubcategoria = nombreSubcategoria;
    }

    public String getNombreCategoria() {
        return nombreCategoria;
    }

    public void setNombreCategoria(String nombreCategoria) {
        this.nombreCategoria = nombreCategoria;
    }
}