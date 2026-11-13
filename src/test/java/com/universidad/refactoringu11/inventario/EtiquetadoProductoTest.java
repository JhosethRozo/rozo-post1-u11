package com.universidad.refactoringu11.inventario;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class EtiquetadoProductoTest {
    private final EtiquetadoProducto servicio = new EtiquetadoProducto();
    @Test void libro_conservaResultadoOriginal() {
        Producto producto = new Producto(1L, "Producto", TipoProducto.LIBRO, 100.0, 10, 2);
        assertEquals("[LIBRO] Producto - Exento de IVA", servicio.generarEtiqueta(producto));
    }
    @Test void electronico_conservaResultadoOriginal() {
        Producto producto = new Producto(1L, "Producto", TipoProducto.ELECTRONICO, 100.0, 10, 2);
        assertEquals("[ELECTRO] Producto - Garantia 1 ano", servicio.generarEtiqueta(producto));
    }
    @Test void ropa_conservaResultadoOriginal() {
        Producto producto = new Producto(1L, "Producto", TipoProducto.ROPA, 100.0, 10, 2);
        assertEquals("[ROPA] Producto - Ver talla", servicio.generarEtiqueta(producto));
    }
    @Test void alimento_conservaResultadoOriginal() {
        Producto producto = new Producto(1L, "Producto", TipoProducto.ALIMENTO, 100.0, 10, 2);
        assertEquals("[ALIMENTO] Producto - Perecedero", servicio.generarEtiqueta(producto));
    }
    @Test void tipoNulo_lanzaExcepcionOriginal() {
        Producto producto = new Producto(1L, "Producto", null, 100.0, 10, 2);
        assertThrows(NullPointerException.class, () -> servicio.generarEtiqueta(producto));
    }
}
