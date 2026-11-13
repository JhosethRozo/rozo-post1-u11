package com.universidad.refactoringu11.reportes;

import com.universidad.refactoringu11.inventario.InventarioService;
import com.universidad.refactoringu11.inventario.Producto;
import com.universidad.refactoringu11.inventario.TipoProducto;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ReporteBajoStockServiceTest {
    @Test
    void reporteVacio_incluyeEncabezadosSinAlertas() {
        var reporte = new ReporteBajoStockService(new InventarioService());
        assertEquals("REPORTE DE BAJO STOCK\nALERTAS REGISTRADAS\n", reporte.generarReporte());
    }

    @Test
    void notificacion_registraEventoYReporteConsultaSoloStockBajo() {
        var inventario = new InventarioService();
        var reporte = new ReporteBajoStockService(inventario);
        inventario.setNotificador(reporte);
        inventario.agregarProducto(new Producto(1L, "Libro", TipoProducto.LIBRO, 45, 20, 5));
        inventario.agregarProducto(new Producto(2L, "Cafe", TipoProducto.ALIMENTO, 15, 5, 5));

        assertEquals("OK_BAJO_STOCK_REORDEN_12", inventario.actualizarInventario(1L, 17, "SALIDA"));
        assertEquals("REPORTE DE BAJO STOCK\n1 | Libro | stock: 3 | minimo: 5\n"
                + "ALERTAS REGISTRADAS\n1 | Libro | reorden: 12\n", reporte.generarReporte());

        // El reporte consulta el stock actual; la alerta es una instantanea historica.
        assertEquals("OK_13", inventario.actualizarInventario(1L, 10, "ENTRADA"));
        assertEquals("REPORTE DE BAJO STOCK\nALERTAS REGISTRADAS\n1 | Libro | reorden: 12\n",
                reporte.generarReporte());
    }

    @Test
    void consultaBajoStock_noPermiteModificarLaColeccionInterna() {
        var inventario = new InventarioService();
        inventario.agregarProducto(new Producto(1L, "Libro", TipoProducto.LIBRO, 45, 3, 5));
        assertThrows(UnsupportedOperationException.class, () -> inventario.obtenerProductosBajoStock().clear());
        assertEquals(1, inventario.obtenerProductosBajoStock().size());
    }
}
