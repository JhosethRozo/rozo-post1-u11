package com.universidad.refactoringu11;

import com.universidad.refactoringu11.inventario.InventarioService;
import com.universidad.refactoringu11.inventario.Producto;
import com.universidad.refactoringu11.inventario.TipoProducto;
import com.universidad.refactoringu11.reportes.ReporteBajoStockService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class RefactoringU11ApplicationTest {
    @Autowired private InventarioService inventario;
    @Autowired private ReporteBajoStockService reportes;

    @Test
    void contextoArrancaYNotificaConReferenciasCircularesDeshabilitadas() {
        inventario.agregarProducto(new Producto(100L, "Integracion", TipoProducto.LIBRO, 30, 20, 5));
        assertEquals("OK_BAJO_STOCK_REORDEN_12", inventario.actualizarInventario(100L, 17, "SALIDA"));
        assertTrue(reportes.generarReporte().contains("100 | Integracion | reorden: 12"));
    }
}
