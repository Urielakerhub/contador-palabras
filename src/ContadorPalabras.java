import java.nio.file.Path;
import java.nio.file.Files;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

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

        System.out.println("Archivo: " + archivo.getFileName());
        System.out.println("Ruta: " + archivo.toAbsolutePath());
        System.out.println("----------------------------------------");

        Map<String, Integer> frecuencias = new HashMap<>();

        try (BufferedReader lector = Files.newBufferedReader(archivo)) {
            String linea;

            while ((linea = lector.readLine()) != null) {
                // Convertir a minusculas
                linea = linea.toLowerCase();

                // Eliminar cualquier caracter que no sea letra, numero o espacio
                linea = linea.replaceAll("[^\\p{L}\\p{N}\\s]", "");

                // Dividir en palabras usando espacios multiples como separador
                String[] palabras = linea.trim().split("\\s+");

                // Contar frecuencias
                for (String palabra : palabras) {
                    if (!palabra.isEmpty()) {
                        frecuencias.put(palabra, frecuencias.getOrDefault(palabra, 0) + 1);
                    }
                }
            }

        } catch (IOException e) {
            System.err.println("Error al leer el archivo: " + e.getMessage());
            return;
        }

        // Mostrar el resultado de las frecuencias al terminar
        System.out.println("\nFrecuencia de cada palabra:");
        for (Map.Entry<String, Integer> entry : frecuencias.entrySet()) {
            System.out.println(entry.getKey() + " : " + entry.getValue());
        }
    }
}
