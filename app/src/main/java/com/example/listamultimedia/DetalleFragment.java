package com.example.listamultimedia;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.VideoView;


/**
 * Fragmento que gestiona la visualización de un recurso multimedia (video, audio o web) y los controles asociados
 * Se encarga de mostrar el detalle del recurso seleccionado desde otra parte de la aplicación
 * Este fragmento se comunica con un ViewModelRecursos para obtener el recurso seleccionado y restaurar el estado de reproducción cuando sea necesario
 */

public class DetalleFragment extends Fragment {

    private ViewModelRecursos viewModelRecursos;
    private VideoView videoVista;
    private LinearLayout layoutAudio;
    private Button botonReproducirAudio;
    private Button botonPausarAudio;
    private WebView vistaWeb;
    private Button botonVolver;
    private MediaPlayer reproductorAudio;

    /**
     * Constructor vacío requerido para la correcta instanciación del fragmento
     */
    public DetalleFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.fragment_detalle, container, false);

        // Inicialización de vistas
        videoVista = vista.findViewById(R.id.video_vista);
        layoutAudio = vista.findViewById(R.id.layout_audio);
        botonReproducirAudio = vista.findViewById(R.id.boton_reproducir_audio);
        botonPausarAudio = vista.findViewById(R.id.boton_pausar_audio);
        vistaWeb = vista.findViewById(R.id.vista_web);
        botonVolver = vista.findViewById(R.id.boton_volver);

        Log.d("DetalleFragment", "Fragmento de detalle creado e inicializando vistas.");

        // Inicializamos el ViewModel
        viewModelRecursos = new ViewModelProvider(requireActivity()).get(ViewModelRecursos.class);

        // Observar cambios en el recurso seleccionado
        viewModelRecursos.getRecursoSeleccionado().observe(getViewLifecycleOwner(), elemento -> {
            if (elemento != null) {
                Log.d("DetalleFragment", "Nuevo recurso seleccionado: " + elemento.getTipo());
                mostrarDetalle(elemento);
            }
        });

        // Configurar el botón de volver
        botonVolver.setOnClickListener(v -> {
            if(getActivity() instanceof ListaFragment.OnNavegacionListener) {
                Log.d("DetalleFragment", "Navegando de vuelta a la lista.");
                ((ListaFragment.OnNavegacionListener)getActivity()).mostrarLista();
            }
        });

        return vista;
    }

    /**
     * Muestra el detalle del recurso seleccionado, ocultando los demás
     *
     * @param elemento El recurso multimedia a mostrar
     */
    private void mostrarDetalle(Recurso elemento) {
        videoVista.setVisibility(View.GONE);
        layoutAudio.setVisibility(View.GONE);
        vistaWeb.setVisibility(View.GONE);

        switch (elemento.getTipo()) {
            case VIDEO:
                mostrarVideo(elemento);
                break;
            case AUDIO:
                mostrarAudio(elemento);
                break;
            case WEB:
                mostrarWeb(elemento);
                break;
        }
    }

    /**
     * Configura y muestra un recurso de tipo video
     *
     * @param elemento El recurso de video a reproducir
     */
    private void mostrarVideo(Recurso elemento) {
        videoVista.setVisibility(View.VISIBLE);
        try {
            Uri uri = Uri.parse("android.resource://" + requireContext().getPackageName() + "/" + elemento.getIdRecurso());
            videoVista.setVideoURI(uri);

            MediaController mediaController = new MediaController(requireContext());
            mediaController.setAnchorView(videoVista);
            videoVista.setMediaController(mediaController);

            videoVista.setOnPreparedListener(mp -> {
                Log.d("VideoDebug", "El vídeo está listo para reproducirse.");
                mp.setVideoScalingMode(MediaPlayer.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING);
                mp.start();
            });

            videoVista.setOnErrorListener((mp, what, extra) -> {
                Log.e("VideoDebug", "Error al reproducir el vídeo. Código: " + what + " Extra: " + extra);
                return true;
            });

            videoVista.setOnInfoListener((mp, what, extra) -> {
                if (what == MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START) {
                    Log.d("VideoDebug", "Renderizado de video iniciado.");
                }
                return true;
            });
        } catch (Exception e) {
            Log.e("VideoError", "Excepción: " + e.getMessage());
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d("DetalleFragment", "Fragmento pausado, guardando estado del reproductor.");

        if (videoVista != null) {
            viewModelRecursos.setPosicionVideo(videoVista.getCurrentPosition());
            viewModelRecursos.setVideoReproduciendo(videoVista.isPlaying());
        }
        if (reproductorAudio != null) {
            viewModelRecursos.setPosicionAudio(reproductorAudio.getCurrentPosition());
            viewModelRecursos.setAudioReproduciendo(reproductorAudio.isPlaying());
        }
    }

    /**
     * Configura y muestra un recurso de tipo audio
     *
     * @param elemento El recurso de audio a reproducir
     */
    private void mostrarAudio(Recurso elemento) {
        layoutAudio.setVisibility(View.VISIBLE);
        if (reproductorAudio != null) {
            reproductorAudio.release();
        }
        reproductorAudio = MediaPlayer.create(getContext(), elemento.getIdRecurso());

        Integer posicionAudio = viewModelRecursos.getPosicionAudio().getValue();
        if (posicionAudio != null && posicionAudio > 0) {
            reproductorAudio.seekTo(posicionAudio);
        }

        Boolean isAudioReproduciendo = viewModelRecursos.isAudioReproduciendo().getValue();
        if (isAudioReproduciendo != null && isAudioReproduciendo) {
            reproductorAudio.start();
        }

        botonReproducirAudio.setOnClickListener(v -> {
            Log.d("AudioDebug", "Reproduciendo audio.");
            reproductorAudio.start();
            viewModelRecursos.setAudioReproduciendo(true);
        });

        botonPausarAudio.setOnClickListener(v -> {
            if (reproductorAudio.isPlaying()) {
                Log.d("AudioDebug", "Audio pausado.");
                reproductorAudio.pause();
                viewModelRecursos.setAudioReproduciendo(false);
            }
        });
    }

    /**
     * Configura y muestra un recurso web en un WebView
     *
     * @param elemento El recurso web a mostrar
     */
    private void mostrarWeb(Recurso elemento) {
        vistaWeb.setVisibility(View.VISIBLE);

        // Configurar WebSettings
        WebSettings configuracionWeb = vistaWeb.getSettings();
        configuracionWeb.setJavaScriptEnabled(true);
        configuracionWeb.setDomStorageEnabled(true);

        // Asignar un WebViewClient para manejar la navegación interna
        vistaWeb.setWebViewClient(new WebViewClient());

        // Cargar la URL del recurso
        String url = elemento.getUrlWeb();
        vistaWeb.loadUrl(url);
        Log.d("DetalleFragment", "Cargando página web: " + url);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("DetalleFragment", "Liberando recursos de audio y video.");

        if (reproductorAudio != null) {
            if (reproductorAudio.isPlaying()) {
                reproductorAudio.stop();
            }
            reproductorAudio.release();
            reproductorAudio = null;
        }
        if (videoVista != null) {
            videoVista.stopPlayback();
        }
    }
}