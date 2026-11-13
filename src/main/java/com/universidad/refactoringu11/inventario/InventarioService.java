package com.universidad.refactoringu11.inventario;

import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class InventarioService {

    private final List<Producto> productos = new ArrayList<>();
    private final List<String> movimientos = new ArrayList<>();

    public void agregarProducto(Producto producto) {
        productos.add(producto);
    }

    // Long Method: valida, busca, actualiza stock, calcula reorden
    // y registra/alerta, todo en un solo metodo (Bloater)
    public String actualizarInventario(Long productoId, int cantidad, String tipoMovimiento) {
        if (datosInvalidos(productoId, cantidad, tipoMovimiento)) return "ERROR_DATOS_INVALIDOS";
        Producto producto = buscarProducto(productoId);
        if (producto == null) return "ERROR_PRODUCTO_NO_EXISTE";
        int nuevoStock = calcularNuevoStock(producto, cantidad, tipoMovimiento);
        if (tipoMovimiento.equals("SALIDA") && nuevoStock < 0) return "ERROR_STOCK_INSUFICIENTE";
        producto.setStock(nuevoStock);
        registrarMovimiento(producto, cantidad, tipoMovimiento);
        return resolverResultado(producto);
    }

    private boolean datosInvalidos(Long productoId, int cantidad, String tipoMovimiento) {
        return productoId == null || cantidad == 0
                || (!tipoMovimiento.equals("ENTRADA") && !tipoMovimiento.equals("SALIDA"));
    }

    private Producto buscarProducto(Long productoId) {
        for (Producto producto : productos) {
            if (producto.getId().equals(productoId)) return producto;
        }
        return null;
    }

    private int calcularNuevoStock(Producto producto, int cantidad, String tipoMovimiento) {
        return tipoMovimiento.equals("ENTRADA")
                ? producto.getStock() + cantidad : producto.getStock() - cantidad;
    }

    private int calcularCantidadReorden(Producto producto) {
        return producto.getStockMinimo() * 3 - producto.getStock();
    }

    private String resolverResultado(Producto producto) {
        if (producto.getStock() < producto.getStockMinimo()) {
            int cantidadReorden = calcularCantidadReorden(producto);
            System.out.println("ALERTA: " + producto.getNombre()
                    + " bajo stock minimo. Reordenar " + cantidadReorden + " unidades.");
            return "OK_BAJO_STOCK_REORDEN_" + cantidadReorden;
        }
        return "OK_" + producto.getStock();
    }

    private void registrarMovimiento(Producto producto, int cantidad, String tipoMovimiento) {
        String registro = tipoMovimiento + " de " + cantidad + " unidades de "
                + producto.getNombre() + " -> stock final: " + producto.getStock();
        movimientos.add(registro);
        System.out.println(registro);
    }
}
