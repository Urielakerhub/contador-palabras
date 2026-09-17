import java.nio.file.Path;
import java.nio.file.Files;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class ContadorPalabras {
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Uso: java ContadorPalabras <archivo>");
            return;
        }

        Path archivo = Path.of(args[0]);

        if (!Files.exists(archivo)) {
            System.err.println("El archivo no existe: " + archivo.toAbsolutePath());
            return;
        }

        Map<String, Integer> frecuencias = new HashMap<>();
        int totalLineas = 0;
        int totalPalabras = 0;

        try (BufferedReader lector = Files.newBufferedReader(archivo)) {
            String linea;

            while ((linea = lector.readLine()) != null) {
                totalLineas++;

                // Convertir a minusculas
                linea = linea.toLowerCase();

                // Eliminar cualquier caracter que no sea letra, numero o espacio
                linea = linea.replaceAll("[^\\p{L}\\p{N}\\s]", "");

                if (linea.isBlank()) {
                    continue;
                }

                // Dividir en palabras usando espacios multiples como separador
                String[] palabras = linea.trim().split("\\s+");

                // Contar frecuencias
                for (String palabra : palabras) {
                    if (!palabra.isEmpty()) {
                        frecuencias.put(
                            palabra,
                            frecuencias.getOrDefault(palabra, 0) + 1
                        );
                        totalPalabras++;
                    }
                }
            }

        } catch (IOException e) {
            System.err.println("Error al leer el archivo: " + e.getMessage());
            return;
        }

        int palabrasDiferentes = frecuencias.size();

        String palabraMasFrecuente = "";
        int frecuenciaMaxima = 0;

        for (Map.Entry<String, Integer> entrada : frecuencias.entrySet()) {
            if (entrada.getValue() > frecuenciaMaxima) {
                frecuenciaMaxima = entrada.getValue();
                palabraMasFrecuente = entrada.getKey();
            }
        }

        Map<String, Integer> palabrasOrdenadas = new TreeMap<>(frecuencias);

        try {
            Path directorioSalida = Path.of("salida");
            Files.createDirectories(directorioSalida);

            Path archivoSalida = directorioSalida.resolve("reporte-documento.txt");

            try (PrintWriter escritor = new PrintWriter(Files.newBufferedWriter(archivoSalida))) {
                escritor.println("ANALISIS DEL DOCUMENTO");
                escritor.println("======================");
                escritor.println();
                escritor.println("Archivo: " + archivo.getFileName());
                escritor.println();
                escritor.println("Total de lineas: " + totalLineas);
                escritor.println("Total de palabras: " + totalPalabras);
                escritor.println("Palabras diferentes: " + palabrasDiferentes);
                escritor.println();
                escritor.println("Palabra mas frecuente:");
                escritor.println(palabraMasFrecuente + " (" + frecuenciaMaxima + ")");
                escritor.println();
                escritor.println("FRECUENCIA DE PALABRAS");
                escritor.println("----------------------");
                escritor.println();

                for (Map.Entry<String, Integer> entrada : palabrasOrdenadas.entrySet()) {
                    escritor.printf("%-20s %d%n", entrada.getKey(), entrada.getValue());
                }
            }

            System.out.println("Analisis completado con exito. Reporte generado en: salida/reporte-documento.txt");

        } catch (IOException e) {
            System.err.println("Error al generar el archivo de salida: " + e.getMessage());
        }
    }
}
