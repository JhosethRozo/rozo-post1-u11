"""Ejecuta comandos reales y captura su salida en un visor HTML local con Chrome."""
import html
import os
from pathlib import Path
import subprocess
import sys

sys.stdout.reconfigure(encoding='utf-8', errors='replace')

ROOT = Path(__file__).resolve().parents[1]
EVIDENCIA = ROOT / 'docs/evidencias'


def capturar(nombre, contenido):
    temporal = ROOT / '.tmp'
    temporal.mkdir(exist_ok=True)
    visor = temporal / (nombre + '.html')
    visor.write_text('<meta charset="utf-8"><style>body{background:#122033;color:#eef3ff;'
                     'font:16px Consolas;margin:30px}pre{white-space:pre-wrap;overflow-wrap:anywhere}'
                     'h1{font:24px Arial;color:#89eab7}</style><h1>Unidad 11 — ' + html.escape(nombre)
                     + '</h1><p>Captura de un visor local de salida real. Log completo adjunto.</p><pre>'
                     + html.escape(contenido) + '</pre>', encoding='utf-8')
    chrome = Path(os.environ.get('CHROME', r'C:\Program Files\Google\Chrome\Application\chrome.exe'))
    subprocess.run([str(chrome), '--headless', '--disable-gpu', '--no-pdf-header-footer',
                    '--hide-scrollbars', '--window-size=1440,1600',
                    '--user-data-dir=' + str(temporal / 'chrome'),
                    '--screenshot=' + str(EVIDENCIA / (nombre + '.png')), visor.as_uri()],
                   stdout=subprocess.DEVNULL, stderr=subprocess.DEVNULL, timeout=45, check=True)


def main():
    nombre, *comando = sys.argv[1:]
    EVIDENCIA.mkdir(parents=True, exist_ok=True)
    resultado = subprocess.run(comando, cwd=ROOT, stdout=subprocess.PIPE,
                               stderr=subprocess.STDOUT, encoding='utf-8', errors='replace')
    salida = '$ ' + ' '.join(comando) + '\n' + resultado.stdout + '\nExit code: ' + str(resultado.returncode)
    (EVIDENCIA / (nombre + '.log')).write_text(salida, encoding='utf-8')
    print('\n'.join(salida.splitlines()[-30:]))
    capturar(nombre, '\n'.join(salida.splitlines()[-48:]))
    sys.exit(resultado.returncode)


if __name__ == '__main__':
    main()
