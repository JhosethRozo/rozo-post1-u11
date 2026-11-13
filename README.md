# Refactorización guiada por pruebas — Unidad 11

**Jhoseth Esneider Rozo Carrillo · Patrones de Diseño de Software · UDES**

Proyecto Maven único en la raíz: `com.universidad:refactoring-u11`, Java 17, JAR.
Spring Boot Starter y Starter Test; sin Web, persistencia ni endpoints.

## Parte 1 — Decisiones de diseño

### 1. Extract Function (Extract Method) para el Long Method

`actualizarInventario` orquesta validación, búsqueda, cálculo/actualización del stock,
registro y resultado. Cada función extraída expresa una intención reconocible.
El cálculo de reorden queda en el método privado `calcularCantidadReorden`: es una
fórmula simple y estable (`stockMinimo * 3 - stock`), sin estado ni colaboradores.
Una clase independiente agregaría indirección sin aportar una razón de cambio actual.
Si aparecieran políticas por proveedor, plazos o demanda, extraería una
`CalculadoraReorden` con pruebas propias. No se cambia la validación original.

### 2. Replace Conditional with Polymorphism para el switch duplicado

Se elige un enum con métodos abstractos implementados por cada constante.
El conjunto es cerrado y estable: el compilador exige ambos comportamientos al
agregar un tipo. `ClasificadorProducto` y `EtiquetadoProducto` conservan sus firmas
y delegan; ninguno necesita cambiar para admitir otra constante.
Una interfaz de estrategia con una clase por tipo sería preferible si los tipos
necesitaran colaboradores inyectados por Spring; las constantes enum no son beans
y esa inyección complicaría el diseño actual. Hoy el enum evita esa jerarquía adicional.
