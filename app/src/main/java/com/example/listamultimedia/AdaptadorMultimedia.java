package com.example.listamultimedia;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;


/**
 * Adaptador para la lista de elementos multimedia en un RecyclerView
 * Maneja la visualización de los elementos y la interacción del usuario con ellos
 */

public class AdaptadorMultimedia extends RecyclerView.Adapter<AdaptadorMultimedia.ElementoViewHolder> {

    private List<Recurso> listaElementos;
    private OnElementoSeleccionadoListener listener;

    /**
     * Constructor del adaptador
     *
     * @param listaElementos Lista de recursos multimedia a mostrar
     * @param listener       Listener para manejar la selección de un elemento
     */
    public AdaptadorMultimedia(List<Recurso> listaElementos, OnElementoSeleccionadoListener listener) {
        this.listaElementos = listaElementos;
        this.listener = listener;
        Log.d("AdaptadorMultimedia", "Adaptador creado con " + listaElementos.size() + " elementos.");
    }

    /**
     * Infla el layout del ítem y crea un nuevo ViewHolder
     *
     * @param parent   El ViewGroup en el que se añadirá la nueva vista
     * @param viewType Tipo de vista del nuevo elemento
     * @return Un nuevo ViewHolder con la vista inflada
     */
    @NonNull
    @Override
    public ElementoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d("AdaptadorMultimedia", "Creando un nuevo ViewHolder.");
        View vista = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_elemento, parent, false);
        return new ElementoViewHolder(vista);
    }

    /**
     * Asocia los datos de un recurso multimedia a un ViewHolder
     *
     * @param holder   El ViewHolder que se va a actualizar
     * @param position La posición del elemento en la lista
     */
    @Override
    public void onBindViewHolder(ElementoViewHolder holder, int position) {
        Recurso elemento = listaElementos.get(position);
        Log.d("AdaptadorMultimedia", "Vinculando ViewHolder para el elemento en la posición: " + position);

        // Configurar icono, título y descripción
        holder.imagenIcono.setImageResource(elemento.getIdIcono());
        holder.tituloTextView.setText(elemento.getTitulo());
        holder.descripcionTextView.setText(elemento.getDescripcion());

        // Log de información del recurso
        Log.d("AdaptadorMultimedia", "Elemento en posición " + position + ": " +
                "Título = " + elemento.getTitulo() + ", Descripción = " + elemento.getDescripcion());

        // Manejamos el evento de clic para expandir o contraer detalles
        holder.itemView.setOnClickListener(v -> {
            boolean nuevoEstado = !elemento.isExpandido();
            elemento.setExpandido(nuevoEstado);
            notifyItemChanged(position);
            Log.d("AdaptadorMultimedia", "Elemento en posición " + position +
                    " expandido: " + nuevoEstado);
        });

        // Mostrar u ocultar detalles según el estado expandido
        boolean estaExpandido = elemento.isExpandido();
        holder.layoutDetalle.setVisibility(estaExpandido ? View.VISIBLE : View.GONE);
        Log.d("AdaptadorMultimedia", "Layout detalle en la posición " + position +
                " es visible: " + (estaExpandido ? "Sí" : "No"));

        // Manejar evento de clic en el botón "Ver detalle"
        holder.botonVerDetalle.setOnClickListener(v -> {
            Log.d("AdaptadorMultimedia", "Botón 'Ver detalle' clickeado en la posición " + position);
            listener.onElementoSeleccionado(elemento);
        });
    }

    /**
     * Devuelve el número total de elementos en la lista
     *
     * @return Cantidad de elementos en la lista
     */
    @Override
    public int getItemCount() {
        int itemCount = listaElementos.size();
        Log.d("AdaptadorMultimedia", "Número total de elementos en la lista: " + itemCount);
        return itemCount;
    }

    /**
     * ViewHolder que contiene la vista de cada elemento de la lista
     */
    public static class ElementoViewHolder extends RecyclerView.ViewHolder {
        ImageView imagenIcono;
        TextView tituloTextView;
        TextView descripcionTextView;
        LinearLayout layoutDetalle;
        Button botonVerDetalle;

        /**
         * Constructor del ViewHolder
         *
         * @param itemView La vista del elemento en la lista
         */
        public ElementoViewHolder(View itemView) {
            super(itemView);
            imagenIcono = itemView.findViewById(R.id.imagen_icono);
            tituloTextView = itemView.findViewById(R.id.titulo_texto);
            descripcionTextView = itemView.findViewById(R.id.descripcion_texto);
            layoutDetalle = itemView.findViewById(R.id.layout_detalle);
            botonVerDetalle = itemView.findViewById(R.id.boton_ver_detalle);
            Log.d("AdaptadorMultimedia", "ElementoViewHolder creado.");
        }
    }

    /**
     * Interfaz para manejar la selección de un elemento multimedia
     */
    public interface OnElementoSeleccionadoListener {
        void onElementoSeleccionado(Recurso elemento); // Método llamado cuando se selecciona un elemento multimedia.
    }
}