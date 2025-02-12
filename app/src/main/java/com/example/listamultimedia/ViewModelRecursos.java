package com.example.listamultimedia;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;


/**
 * ViewModel para gestionar los recursos multimedia en la aplicación
 * Este ViewModel mantiene el estado del recurso multimedia
 * Se encarga de gestionar la información del estado entre cambios de configuración, utilizando la clase SavedStateHandle para almacenar y recuperar los datos
 * La clase utiliza LiveData para manejar el estado de las posiciones de reproducción y el estado de reproducción de los recursos multimedia
 * LiveData permite la actualización automática de la interfaz de usuario cuando estos valores cambian
 */

public class ViewModelRecursos extends ViewModel {

    // Objeto SavedStateHandle para guardar el estado de los datos entre cambios de configuración
    private final SavedStateHandle savedState;

    // LiveData para el recurso seleccionado
    private final MutableLiveData<Recurso> recursoSeleccionado = new MutableLiveData<>();

    // LiveData para las posiciones de video y audio
    private final MutableLiveData<Integer> posicionVideo = new MutableLiveData<>(0);
    private final MutableLiveData<Integer> posicionAudio = new MutableLiveData<>(0);

    // LiveData para saber si los recursos están reproduciéndose
    private final MutableLiveData<Boolean> videoReproduciendo = new MutableLiveData<>(false);
    private final MutableLiveData<Boolean> audioReproduciendo = new MutableLiveData<>(false);

    // LiveData para la URL de la web y su visibilidad
    private final MutableLiveData<String> urlWeb = new MutableLiveData<>();
    private final MutableLiveData<Boolean> webVisible = new MutableLiveData<>(false);

    /**
     * Constructor del ViewModel que recibe un SavedStateHandle
     * Este constructor inicializa el ViewModel
     * Verifica si existen datos previamente guardados en el estado los cuales se recuperan de acuerdo con las claves asociadas
     *
     * @param savedState Estado guardado para mantener los datos tras cambios de configuración
     *                   Este objeto se utiliza para almacenar y restaurar el estado del ViewModel entre cambios de configuración
     */
    public ViewModelRecursos(SavedStateHandle savedState) {
        this.savedState = savedState;
        // Restaura el estado guardado en SavedStateHandle si existe
        if (savedState.contains("recurso_seleccionado")) {
            recursoSeleccionado.setValue(savedState.get("recurso_seleccionado"));
        }
        if (savedState.contains("posicion_video")) {
            posicionVideo.setValue(savedState.get("posicion_video"));
        }
        if (savedState.contains("posicion_audio")) {
            posicionAudio.setValue(savedState.get("posicion_audio"));
        }
        if (savedState.contains("video_reproduciendo")) {
            videoReproduciendo.setValue(savedState.get("video_reproduciendo"));
        }
        if (savedState.contains("audio_reproduciendo")) {
            audioReproduciendo.setValue(savedState.get("audio_reproduciendo"));
        }
        if (savedState.contains("url_web")) {
            urlWeb.setValue(savedState.get("url_web"));
        }
        if (savedState.contains("web_visible")) {
            webVisible.setValue(savedState.get("web_visible"));
        }
    }

    /**
     * Obtiene el recurso actualmente seleccionado
     * Devuelve un LiveData que contiene el recurso actualmente seleccionado
     * Este LiveData puede ser observado por la interfaz de usuario para actualizarse automáticamente cuando el recurso seleccionado cambie
     *
     * @return LiveData con el recurso seleccionado
     */
    public LiveData<Recurso> getRecursoSeleccionado() {
        return recursoSeleccionado;
    }

    /**
     * Establece el recurso seleccionado
     * Al establecer un nuevo recurso como seleccionado, se actualiza el valor de LiveData
     * Se guarda en el estado utilizando el objeto SavedStateHandle para que pueda ser recuperado tras cambios de configuración
     *
     * @param recurso El recurso a seleccionar
     */
    public void setRecursoSeleccionado(Recurso recurso) {
        recursoSeleccionado.setValue(recurso);
        savedState.set("recurso_seleccionado", recurso); // Guardar el estado
    }

    /**
     * Establece la posición actual del video en la reproducción
     * Actualiza la posición de reproducción del video
     * El valor se guarda en el estado utilizando el objeto SavedStateHandle para ser recuperado después de cambios de configuración
     *
     * @param posicion La nueva posición en milisegundos
     */
    public void setPosicionVideo(int posicion) {
        posicionVideo.setValue(posicion);
        savedState.set("posicion_video", posicion); // Guardar el estado
    }

    /**
     * Obtiene la posición actual de reproducción del audio
     * Devuelve un LiveData que contiene la posición en milisegundos del audio
     * Este LiveData puede ser observado para actualizar la interfaz de usuario cuando la posición de reproducción cambie
     *
     * @return LiveData con la posición del audio en milisegundos
     */
    public LiveData<Integer> getPosicionAudio() {
        return posicionAudio;
    }

    /**
     * Establece la posición actual del audio en la reproducción
     * Este método actualiza la posición de reproducción del audio
     * El valor se guarda en el estado utilizando el objeto SavedStateHandle para ser recuperado después de cambios de configuración
     *
     * @param posicion La nueva posición en milisegundos
     */
    public void setPosicionAudio(int posicion) {
        posicionAudio.setValue(posicion);
        savedState.set("posicion_audio", posicion); // Guardar el estado
    }

    /**
     * Establece el estado de reproducción del video
     * Este método actualiza el estado de reproducción del video (si está en reproducción o pausado)
     * El valor se guarda en el estado utilizando el objeto SavedStateHandle para que pueda ser restaurado después de un cambio de configuración
     *
     * @param reproduciendo true si el video está en reproducción, false si está pausado
     */
    public void setVideoReproduciendo(boolean reproduciendo) {
        videoReproduciendo.setValue(reproduciendo);
        savedState.set("video_reproduciendo", reproduciendo); // Guardar el estado
    }

    /**
     * Obtiene el estado de reproducción del audio
     * Devuelve un LiveData que indica si el audio está siendo reproducido o está pausado
     * Este LiveData puede ser observado para actualizar la interfaz de usuario dependiendo de si el audio está reproduciéndose o no
     *
     * @return LiveData con el estado de reproducción del audio (true si está reproduciendo, false si está pausado)
     */
    public LiveData<Boolean> isAudioReproduciendo() {
        return audioReproduciendo;
    }

    /**
     * Establece el estado de reproducción del audio
     * Este método actualiza el estado de reproducción del audio (si está en reproducción o pausado)
     * El valor se guarda en el estado utilizando el objeto SavedStateHandle para que pueda ser restaurado después de un cambio de configuración
     *
     * @param reproduciendo true si el audio está en reproducción, false si está pausado
     */
    public void setAudioReproduciendo(boolean reproduciendo) {
        audioReproduciendo.setValue(reproduciendo);
        savedState.set("audio_reproduciendo", reproduciendo); // Guardar el estado
    }
}