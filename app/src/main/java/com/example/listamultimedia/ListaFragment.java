package com.example.listamultimedia;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;


/**
 * Fragmento que muestra una lista de recursos multimedia mediante un RecyclerView
 * Implementa la interfaz AdaptadorMultimedia.OnElementoSeleccionadoListener
 * Maneja la selección de un elemento de la lista y lo notifica al ViewModel y la actividad contenedora
 */

public class ListaFragment extends Fragment implements AdaptadorMultimedia.OnElementoSeleccionadoListener {

    private RecyclerView recyclerView; // RecyclerView para mostrar la lista de recursos
    private AdaptadorMultimedia adaptador; // Adaptador del RecyclerView
    private List<Recurso> listaElementos; // Lista de recursos multimedia disponibles
    private ViewModelRecursos viewModelRecursos; // ViewModel compartido con la actividad

    /**
     * Constructor vacío requerido para la correcta instanciación del fragmento
     */
    public ListaFragment() {
    }

    /**
     * Método llamado para crear la vista del fragmento
     * Se encarga de inflar el layout
     * Configurar el RecyclerView
     * Cargar los recursos multimedia
     *
     * @param inflater           Objeto LayoutInflater para inflar la vista
     * @param container          Contenedor padre donde se insertará el fragmento
     * @param savedInstanceState Estado guardado de la instancia anterior (si existe)
     * @return Vista inflada del fragmento
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d("ListaFragment", "onCreateView: Creando vista del fragmento de lista");

        // Inflamos el layout del fragmento
        View vista = inflater.inflate(R.layout.fragment_lista, container, false);

        // Configuración del RecyclerView
        recyclerView = vista.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Inicialización del ViewModel compartido con la actividad
        viewModelRecursos = new ViewModelProvider(requireActivity()).get(ViewModelRecursos.class);

        // Inicialización de la lista de elementos con los recursos multimedia
        listaElementos = new ArrayList<>();
        cargarRecursos();

        // Configuramos el adaptador con la lista de elementos y el listener
        adaptador = new AdaptadorMultimedia(listaElementos, this);
        recyclerView.setAdapter(adaptador);

        Log.d("ListaFragment", "onCreateView: Elementos cargados en la lista: " + listaElementos.size());

        return vista;
    }

    /**
     * Método para cargar los recursos multimedia en la lista
     * Se añaden elementos de tipo video, audio y web
     */
    private void cargarRecursos() {
        Log.d("ListaFragment", "cargarRecursos: Cargando recursos multimedia");

        listaElementos.add(new Recurso("Nubes", "Movimiento de las nubes en un día de cielo azul", R.raw.nubes, Recurso.TipoRecurso.VIDEO, R.drawable.video));
        listaElementos.add(new Recurso("Girasoles", "Campo de girasoles en un atardecer de verano", R.raw.girasoles, Recurso.TipoRecurso.VIDEO, R.drawable.video));
        listaElementos.add(new Recurso("Piano", "Pequeña pieza tocada en piano", R.raw.piano, Recurso.TipoRecurso.AUDIO, R.drawable.audio));
        listaElementos.add(new Recurso("Pista de audio", "Pequeña pieza para desconectar", R.raw.pista1, Recurso.TipoRecurso.AUDIO, R.drawable.audio));
        listaElementos.add(new Recurso("Android", "El apasionante mundo de Android", "https://developer.android.com/?hl=es-419", Recurso.TipoRecurso.WEB, R.drawable.web));
        listaElementos.add(new Recurso("Arduino", "Aprende arduino", "https://www.arduino.cc/", Recurso.TipoRecurso.WEB, R.drawable.web));

        Log.d("ListaFragment", "cargarRecursos: Se han añadido " + listaElementos.size() + " elementos");
    }

    /**
     * Método invocado cuando un elemento de la lista es seleccionado por el usuario
     * Se actualiza el ViewModel con el recurso seleccionado y se notifica a la actividad para mostrar el detalle
     *
     * @param recurso Recurso seleccionado por el usuario
     */
    @Override
    public void onElementoSeleccionado(Recurso recurso) {
        Log.d("ListaFragment", "onElementoSeleccionado: Recurso seleccionado: " + recurso.getTitulo());

        // Pasamos el recurso seleccionado al ViewModel
        viewModelRecursos.setRecursoSeleccionado(recurso);

        // Llamamos al método para mostrar el detalle en la actividad
        if (getActivity() instanceof OnNavegacionListener) {
            ((OnNavegacionListener) getActivity()).mostrarDetalle();
        } else {
            Log.w("ListaFragment", "onElementoSeleccionado: La actividad no implementa OnNavegacionListener");
        }
    }

    /**
     * Interfaz para la comunicación entre el fragmento y la actividad contenedora
     * Permite la navegación entre la lista y los detalles de los recursos
     */
    public interface OnNavegacionListener {
        void mostrarDetalle(); // Método para mostrar el detalle del recurso seleccionado.
        void mostrarLista(); // Método para volver a la lista de recursos.
    }
}