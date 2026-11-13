package com.universidad.refactoringu11.reportes;

import com.universidad.refactoringu11.inventario.InventarioService;
import com.universidad.refactoringu11.inventario.Producto;
import org.springframework.stereotype.Service;

@Service
public class ReporteBajoStockService {
    private final InventarioService inventarioService;

    public ReporteBajoStockService(InventarioService inventarioService) {
        this.inventarioService = inventarioService;
    }

    public String generarReporte() {
        StringBuilder reporte = new StringBuilder("REPORTE DE BAJO STOCK\n");
        for (Producto producto : inventarioService.obtenerProductosBajoStock()) {
            reporte.append(producto.getId()).append(" | ").append(producto.getNombre())
                    .append(" | stock: ").append(producto.getStock())
                    .append(" | minimo: ").append(producto.getStockMinimo()).append('\n');
        }
        return reporte.toString();
    }
}
