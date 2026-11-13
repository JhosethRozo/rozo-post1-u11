package com.universidad.refactoringu11.inventario;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import static org.junit.jupiter.api.Assertions.*;

class InventarioServiceTest {
    private InventarioService service;
    private Producto libro;

    @BeforeEach
    void setUp() {
        service = new InventarioService();
        libro = new Producto(1L, "Clean Code", TipoProducto.LIBRO, 45.0, 20, 5);
        service.agregarProducto(libro);
    }

    @Test void entrada_incrementaStockYRetornaOk() {
        assertEquals("OK_30", service.actualizarInventario(1L, 10, "ENTRADA"));
        assertEquals(30, libro.getStock());
    }
    @Test void salida_conStockSuficiente_descuentaYRetornaOk() {
        assertEquals("OK_12", service.actualizarInventario(1L, 8, "SALIDA"));
        assertEquals(12, libro.getStock());
    }
    @Test void salida_conStockInsuficiente_retornaErrorYNoModificaStock() {
        assertEquals("ERROR_STOCK_INSUFICIENTE", service.actualizarInventario(1L, 50, "SALIDA"));
        assertEquals(20, libro.getStock());
    }
    @Test void salida_dejaStockBajoElMinimo_activaReordenConCantidadCalculada() {
        assertEquals("OK_BAJO_STOCK_REORDEN_12", service.actualizarInventario(1L, 17, "SALIDA"));
        assertEquals(3, libro.getStock());
    }
    @Test void tipoMovimientoInvalido_retornaError() {
        assertEquals("ERROR_DATOS_INVALIDOS", service.actualizarInventario(1L, 5, "TRANSFERENCIA"));
        assertEquals(20, libro.getStock());
    }
    @Test void productoInexistente_retornaError() {
        assertEquals("ERROR_PRODUCTO_NO_EXISTE", service.actualizarInventario(99L, 5, "ENTRADA"));
    }
    @Test void idNulo_retornaError() {
        assertEquals("ERROR_DATOS_INVALIDOS", service.actualizarInventario(null, 5, "ENTRADA"));
    }
    @Test void cantidadCero_retornaError() {
        assertEquals("ERROR_DATOS_INVALIDOS", service.actualizarInventario(1L, 0, "SALIDA"));
        assertEquals(20, libro.getStock());
    }
    @Test void stockIgualAlMinimo_noActivaReorden() {
        assertEquals("OK_5", service.actualizarInventario(1L, 15, "SALIDA"));
    }
    @Test void salidaExacta_activaReordenDesdeStockCero() {
        assertEquals("OK_BAJO_STOCK_REORDEN_15", service.actualizarInventario(1L, 20, "SALIDA"));
        assertEquals(0, libro.getStock());
    }
    @Test void entradaNegativa_conservaComportamientoOriginal() {
        assertEquals("OK_17", service.actualizarInventario(1L, -3, "ENTRADA"));
    }
    @Test void salidaNegativa_conservaComportamientoOriginal() {
        assertEquals("OK_23", service.actualizarInventario(1L, -3, "SALIDA"));
    }
    @Test void movimientoNulo_lanzaExcepcionOriginal() {
        assertThrows(NullPointerException.class, () -> service.actualizarInventario(1L, 1, null));
        assertEquals(20, libro.getStock());
    }
    @Test void idNulo_cortocircuitaMovimientoNulo() {
        assertEquals("ERROR_DATOS_INVALIDOS", service.actualizarInventario(null, 1, null));
    }
    @Test void registraMovimientoYAlertaConTextoOriginal() {
        PrintStream original = System.out;
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        try (PrintStream captura = new PrintStream(bytes, true, StandardCharsets.UTF_8)) {
            System.setOut(captura);
            service.actualizarInventario(1L, 17, "SALIDA");
        } finally {
            System.setOut(original);
        }
        assertEquals("SALIDA de 17 unidades de Clean Code -> stock final: 3" + System.lineSeparator()
                + "ALERTA: Clean Code bajo stock minimo. Reordenar 12 unidades." + System.lineSeparator(),
                bytes.toString(StandardCharsets.UTF_8));
    }
}
