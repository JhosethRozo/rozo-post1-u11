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

## Ejecución y evidencia de la Parte 1

Requisitos: JDK 17+ y Maven 3.9+. Ejecutar desde la raíz:

```sh
mvn clean test
mvn package
java -jar target/refactoring-u11-0.0.1-SNAPSHOT.jar
```

No hay servidor HTTP: la aplicación inicia el contexto y termina. Las pruebas de
caracterización usan `new InventarioService()`, sin levantar Spring.

| Métrica | Original (`p1-original`) | Refactorizado |
|---|---:|---:|
| Líneas físicas del método, firma y cierre incluidos | 44 | 10 |
| Complejidad ciclomática local de ese método | 12 | 5 |
| Responsabilidades mezcladas en un bloque | 6 | Orquestación; operaciones delegadas |
| Switches sobre TipoProducto | 2 | 0 |
| Ramas de tipo repetidas estructuralmente | 4/4 (100%) | 0/4 (0%) |

Las seis responsabilidades eran validación, búsqueda, stock, cálculo de reorden,
auditoría y alerta. El porcentaje anterior mide coincidencia de las cuatro ramas
en los dos switches, **no** porcentaje de líneas duplicadas del repositorio.
La CC se calcula como `1 + if + for + while + case + && + || + ?` sobre el método,
excluyendo comentarios. Es una métrica local: las decisiones extraídas no desaparecen
del sistema. `scripts/checkpoints.py` permite reproducir conteos y hashes.

| Checkpoint | Resultado real | Evidencia (log y captura PNG del mismo nombre) |
|---|---|---|
| Original | Compila, aún con ambos smells | [01-original](docs/evidencias/01-original.log) |
| Caracterización antes del refactor | 25 pruebas, 0 fallos | [02-caracterizacion](docs/evidencias/02-caracterizacion.log) |
| Cada extracción, en orden 1–5 | 25 pruebas, 0 fallos en cada una | [Extracción 1](docs/evidencias/03-extraccion-1.log), [2](docs/evidencias/03-extraccion-2.log), [3](docs/evidencias/03-extraccion-3.log), [4](docs/evidencias/03-extraccion-4.log), [5](docs/evidencias/03-extraccion-5.log) |
| Polimorfismo | Las mismas 25 pruebas, 0 fallos | [04-polimorfismo](docs/evidencias/04-polimorfismo.log) |
| Estructura, hashes e historial | Método ≤10 líneas; switches eliminados; pruebas inmutables | [05-checkpoints-parte1](docs/evidencias/05-checkpoints-parte1.log) |

Las 25 pruebas son 15 de inventario, 5 de recargo y 5 de etiqueta: incluyen las
cuatro ramas en **ambas** clases, entradas inválidas, producto inexistente,
stock insuficiente, umbral exacto, stock cero, cantidades negativas y nulos,
y el texto exacto de auditoría/alerta. Se conservan incluso los comportamientos
extraños del original. Agregar validaciones sería otro cambio funcional.
El enunciado ilustra dos etiquetas y un total de 12, pero exige las cuatro ramas:
la suite amplía esos ejemplos desde el comienzo. Menciona también «cuatro pruebas
de caracterización», aunque nombra tres clases; aquí se conservan las tres completas.

Las capturas son fotografías de un visor HTML local que muestra logs reales;
no son capturas de un IDE ni resultados simulados. Los logs completos quedan
versionados junto a cada PNG; `scripts/verificar.py` ejecuta y captura comandos.
