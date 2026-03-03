package com.bootcamp.service.productos;
import com.bootcamp.dao.productos.ProductoDAO;
import com.bootcamp.modelo.productos.ProductoDTO;
import java.util.List;

public class ProductoService {

    private final ProductoDAO prodDao = new ProductoDAO();

    /**
     * CATÁLOGO PARA CLIENTES (Solo con Stock)
     * @param busqueda
     * @param catId
     * @param subId
     * @param orden
     * @return
     */
    public List<ProductoDTO> obtenerCatalogoCliente(String busqueda, Integer catId, Integer subId, String orden) {
        // Obtenemos la lista base del DAO
        List<ProductoDTO> lista = prodDao.listar(busqueda, catId, subId, orden);
        // Filtramos: Solo productos con stock > 0
        return lista.stream()
                .filter(p -> p.getStockProducto() > 0)
                .toList();
    }

    // Para el Admin - Ve incluso sin stock
    public List<ProductoDTO> obtenerCatalogoAdmin(String busqueda, Integer catId, Integer subId, String orden) {
        return prodDao.listar(busqueda, catId, subId, orden);
    }

    /**
     * GUARDAR O ACTUALIZAR
     * @param p
     */
    public void guardarOActualizar(ProductoDTO p) {
        if (p.getNombreProducto() == null || p.getNombreProducto().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del producto es obligatorio.");
        }
        if (p.getPrecioProducto() < 0) {
            throw new IllegalArgumentException("El precio debe ser un valor mágico positivo (mayor a 0).");
        }
        if (p.getStockProducto() < 0) {
            throw new IllegalArgumentException("El stock no puede ser negativo.");
        }
        if (p.getIdSubcategoriaAsociada() <= 0) {
            throw new IllegalArgumentException("Debe seleccionar una subcategoría válida.");
        }

        // Decisión: ¿Insertar o Editar?
        if (p.getIdProducto() == 0) {
            prodDao.guardar(p);
        } else {
            prodDao.actualizar(p);
        }
    }

    /**
     * BUSCAR POR ID
     * @param id
     * @return
     */
    public ProductoDTO obtenerPorId(int id) {
        ProductoDTO p = prodDao.buscarPorId(id);
        if (p == null) {
            throw new IllegalArgumentException("¡Producto no encontrado en el reino!");
        }
        return p;
    }

    /**
     * ELIMINAR
     * @param id
     */
    public void eliminarProducto(int id) {
        prodDao.eliminar(id);
    }
}