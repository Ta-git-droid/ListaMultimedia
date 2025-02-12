package com.example.listamultimedia;

import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;


/**
 * Clase principal de la aplicación
 * Gestiona la navegación entre fragmentos
 * Actúa como contenedor para los fragmentos de la aplicación
 * Permite la navegación entre la lista de elementos multimedia y los detalles de cada uno
 * Implementa la interfaz para manejar la interacción con el fragmento de lista y cambiar al fragmento de detalle cuando se seleccione un elemento
 */

public class MainActivity extends AppCompatActivity implements ListaFragment.OnNavegacionListener {

    /**
     * Método llamado cuando la actividad es creada por primera vez
     * Configura la actividad con un diseño de pantalla completa utilizando EdgeToEdge y establece la interfaz gráfica de usuario
     * @param savedInstanceState Estado previamente guardado de la actividad, utilizado para restaurar el estado
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("MainActivity", "onCreate: Actividad creada");

        // Activamos EdgeToEdge para aprovechar la pantalla completa
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Aseguramos que los Insets sean aplicados correctamente para una experiencia EdgeToEdge
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Añadir el fragmento de lista si es la primera vez que se lanza la actividad
        if (savedInstanceState == null) {
            Log.d("MainActivity", "onCreate: Cargando ListaFragment en el contenedor");
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.contenedor_fragmentos, new ListaFragment())
                    .commit();
        }
    }

    /**
     * Método para mostrar el fragmento de detalle cuando se selecciona un elemento de la lista
     * Reemplaza el fragmento actual con DetalleFragment y lo añade a la pila de retroceso para permitir volver a la lista
     */
    @Override
    public void mostrarDetalle() {
        Log.d("MainActivity", "mostrarDetalle: Reemplazando ListaFragment con DetalleFragment");
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.contenedor_fragmentos, new DetalleFragment())
                .addToBackStack(null) // Permite volver atrás a la lista
                .commit();
    }

    /**
     * Método para regresar al fragmento de lista desde el fragmento de detalle
     * Si hay fragmentos en la pila de retroceso, elimina el fragmento superior, volviendo así a la vista anterior (ListaFragment)
     */
    @Override
    public void mostrarLista() {
        Log.d("MainActivity", "mostrarLista: Volviendo al fragmento de lista");
        getSupportFragmentManager().popBackStack();
    }
}
