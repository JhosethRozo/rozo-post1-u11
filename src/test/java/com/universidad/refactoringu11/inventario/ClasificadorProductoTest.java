package com.universidad.refactoringu11.inventario;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ClasificadorProductoTest {
    private final ClasificadorProducto servicio = new ClasificadorProducto();
    @Test void libro_conservaResultadoOriginal() {
        Producto producto = new Producto(1L, "Producto", TipoProducto.LIBRO, 100.0, 10, 2);
        assertEquals(0.0, servicio.calcularRecargoEnvio(producto), 0.001);
    }
    @Test void electronico_conservaResultadoOriginal() {
        Producto producto = new Producto(1L, "Producto", TipoProducto.ELECTRONICO, 100.0, 10, 2);
        assertEquals(8.0, servicio.calcularRecargoEnvio(producto), 0.001);
    }
    @Test void ropa_conservaResultadoOriginal() {
        Producto producto = new Producto(1L, "Producto", TipoProducto.ROPA, 100.0, 10, 2);
        assertEquals(3.0, servicio.calcularRecargoEnvio(producto), 0.001);
    }
    @Test void alimento_conservaResultadoOriginal() {
        Producto producto = new Producto(1L, "Producto", TipoProducto.ALIMENTO, 100.0, 10, 2);
        assertEquals(2.50, servicio.calcularRecargoEnvio(producto), 0.001);
    }
    @Test void tipoNulo_lanzaExcepcionOriginal() {
        Producto producto = new Producto(1L, "Producto", null, 100.0, 10, 2);
        assertThrows(NullPointerException.class, () -> servicio.calcularRecargoEnvio(producto));
    }
}
