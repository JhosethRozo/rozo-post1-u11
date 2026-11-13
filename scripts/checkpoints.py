"""Comprobaciones reproducibles de estructura, métricas e inmutabilidad de pruebas."""
from pathlib import Path
import hashlib
import re
import subprocess

ROOT = Path(__file__).resolve().parents[1]
P = 'src/main/java/com/universidad/refactoringu11/inventario/'
T = 'src/test/java/com/universidad/refactoringu11/inventario/'


def git(*args):
    return subprocess.check_output(['git', '-c', 'safe.directory=' + ROOT.as_posix(), *args],
                                   cwd=ROOT).decode('utf-8')


def metodo(texto):
    inicio = texto.index('    public String actualizarInventario(')
    a = texto.index('{', inicio)
    nivel = 1
    b = a + 1
    while nivel:
        nivel += (texto[b] == '{') - (texto[b] == '}')
        b += 1
    return texto[inicio:b]


def main():
    for etiqueta, texto in [('original', git('show', 'p1-original:' + P + 'InventarioService.java')),
                             ('actual', (ROOT / P / 'InventarioService.java').read_text(encoding='utf-8'))]:
        codigo = metodo(texto)
        limpio = re.sub(r'//[^\n]*', '', codigo)
        complejidad = 1 + len(re.findall(r'\bif\b|\bfor\b|\bwhile\b|\bcase\b|&&|\|\||\?', limpio))
        print(f'{etiqueta}: actualizarInventario = {len(codigo.splitlines())} lineas fisicas, CC={complejidad}')
        print(codigo)
        if etiqueta == 'actual':
            assert len(codigo.splitlines()) <= 10
    for clase in ['ClasificadorProducto', 'EtiquetadoProducto']:
        original = git('show', 'p1-original:' + P + clase + '.java')
        actual = (ROOT / P / (clase + '.java')).read_text(encoding='utf-8')
        print(f'{clase}: switch original={original.count("switch (")}, actual={actual.count("switch (")}')
        assert 'switch' not in actual and 'if (' not in actual
    for nombre in ['InventarioServiceTest', 'ClasificadorProductoTest', 'EtiquetadoProductoTest']:
        ruta = T + nombre + '.java'
        original = git('show', 'p1-caracterizacion:' + ruta).replace('\r\n', '\n')
        actual = (ROOT / ruta).read_text(encoding='utf-8').replace('\r\n', '\n')
        assert original == actual
        print(nombre + ': INMUTABLE SHA-256=' + hashlib.sha256(actual.encode()).hexdigest())
    for ruta in (ROOT / P).glob('*.java'):
        assert 'import com.universidad.refactoringu11.reportes' not in ruta.read_text(encoding='utf-8')
    print('Dominio sin imports de reportes: OK')
    print(git('log', '--oneline', '--reverse'))


if __name__ == '__main__':
    main()
