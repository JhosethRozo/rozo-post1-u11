package com.universidad.refactoringu11.reportes;

import com.universidad.refactoringu11.inventario.InventarioService;
import com.universidad.refactoringu11.inventario.Producto;
import com.universidad.refactoringu11.inventario.NotificadorBajoStock;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class ReporteBajoStockService implements NotificadorBajoStock {
    private final List<String> alertas = new ArrayList<>();
    private final InventarioService inventarioService;

    public ReporteBajoStockService(InventarioService inventarioService) {
        this.inventarioService = inventarioService;
    }

    @Override
    public void notificar(Producto producto, int cantidadReorden) {
        alertas.add(producto.getId() + " | " + producto.getNombre()
                + " | reorden: " + cantidadReorden);
    }

    public String generarReporte() {
        StringBuilder reporte = new StringBuilder("REPORTE DE BAJO STOCK\n");
        for (Producto producto : inventarioService.obtenerProductosBajoStock()) {
            reporte.append(producto.getId()).append(" | ").append(producto.getNombre())
                    .append(" | stock: ").append(producto.getStock())
                    .append(" | minimo: ").append(producto.getStockMinimo()).append('\n');
        }
        reporte.append("ALERTAS REGISTRADAS\n");
        for (String alerta : alertas) {
            reporte.append(alerta).append('\n');
        }
        return reporte.toString();
    }
}
