package com.universidad.refactoringu11.inventario;

public enum TipoProducto {
    LIBRO {
        @Override
        public double calcularRecargoEnvio(Producto producto) {
            return 0.0;
        }

        @Override
        public String generarEtiqueta(Producto producto) {
            return "[LIBRO] " + producto.getNombre() + " - Exento de IVA";
        }
    },
    ELECTRONICO {
        @Override
        public double calcularRecargoEnvio(Producto producto) {
            return producto.getPrecio() * 0.08;
        }

        @Override
        public String generarEtiqueta(Producto producto) {
            return "[ELECTRO] " + producto.getNombre() + " - Garantia 1 ano";
        }
    },
    ROPA {
        @Override
        public double calcularRecargoEnvio(Producto producto) {
            return producto.getPrecio() * 0.03;
        }

        @Override
        public String generarEtiqueta(Producto producto) {
            return "[ROPA] " + producto.getNombre() + " - Ver talla";
        }
    },
    ALIMENTO {
        @Override
        public double calcularRecargoEnvio(Producto producto) {
            return 2.50;
        }

        @Override
        public String generarEtiqueta(Producto producto) {
            return "[ALIMENTO] " + producto.getNombre() + " - Perecedero";
        }
    };

    public abstract double calcularRecargoEnvio(Producto producto);

    public abstract String generarEtiqueta(Producto producto);
}
