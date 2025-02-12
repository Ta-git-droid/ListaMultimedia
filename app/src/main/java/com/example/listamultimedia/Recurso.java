package com.example.listamultimedia;

import android.os.Parcel;
import android.os.Parcelable;


/**
 * Esta clase representa un recurso multimedia en una aplicación Android.
 * Un recurso puede ser de tipo vídeo, audio o un enlace web.
 * Implementa la interfaz Parcelable para permitir la serialización y deserialización de objetos para su posterior transferencia entre actividades o fragmentos en Android.
 * Esta clase ofrece constructores para la creación de recursos tanto locales (vídeo o audio) como en línea (web)
 * Métodos getter y setter para acceder y modificar sus propiedades, y métodos implementados para su serialización y deserialización
 */

public class Recurso implements Parcelable {

    // Atributos del recurso multimedia
    private String titulo;       // Título del recurso
    private String descripcion;  // Descripción del recurso
    private int idRecurso;       // Identificador del recurso en res/raw (para vídeos y audios)
    private String urlWeb;       // URL del recurso web (si es de tipo WEB)
    private TipoRecurso tipo;    // Tipo de recurso (VIDEO, AUDIO, WEB)
    private boolean expandido;   // Indica si los detalles han sido desplegados o no
    private int idIcono;         // Identificador del icono representativo del recurso

    /**
     * Enumeración que define los posibles tipos de recursos multimedia
     * Un recurso puede ser de tipo:
     * - VIDEO
     * - AUDIO
     * - WEB
     */
    public enum TipoRecurso {
        VIDEO,  // Recurso de tipo vídeo
        AUDIO,  // Recurso de tipo audio
        WEB     // Recurso de tipo enlace web
    }

    /**
     * Constructor utilizado para inicializar un recurso con los atributos básicos
     *
     * @param titulo   El título del recurso
     * @param descripcion La descripción del recurso
     * @param tipo     El tipo de recurso (VIDEO, AUDIO, WEB)
     * @param idIcono  El identificador del icono representativo del recurso
     */
    private void inicializar(String titulo, String descripcion, TipoRecurso tipo, int idIcono) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.tipo = tipo;
        this.expandido = false;  // Los detalles no están expandidos por defecto
        this.idIcono = idIcono;
    }

    /**
     * Constructor para crear un recurso local, ya sea de tipo vídeo o audio
     *
     * @param titulo     El título del recurso
     * @param descripcion La descripción del recurso
     * @param idRecurso  El identificador del recurso
     * @param tipo       El tipo de recurso (VIDEO o AUDIO)
     * @param idIcono    El identificador del icono representativo del recurso
     */
    public Recurso(String titulo, String descripcion, int idRecurso, TipoRecurso tipo, int idIcono) {
        inicializar(titulo, descripcion, tipo, idIcono);
        this.idRecurso = idRecurso;
    }

    /**
     * Constructor para crear un recurso de tipo web
     *
     * @param titulo     El título del recurso
     * @param descripcion La descripción del recurso
     * @param urlWeb     La URL del recurso web
     * @param tipo       El tipo de recurso (debe ser WEB)
     * @param idIcono    El identificador del icono representativo del recurso
     */
    public Recurso(String titulo, String descripcion, String urlWeb, TipoRecurso tipo, int idIcono) {
        inicializar(titulo, descripcion, tipo, idIcono);
        this.urlWeb = urlWeb;
    }


    /**
     * Devuelve el título del recurso
     *
     * @return El título del recurso
     */
    public String getTitulo() {
        return titulo;
    }

    /**
     * Devuelve la descripción del recurso
     *
     * @return La descripción del recurso
     */
    public String getDescripcion() {
        return descripcion;
    }

    /**
     * Devuelve el identificador del recurso en el directorio
     *
     * @return El identificador del recurso
     */
    public int getIdRecurso() {
        return idRecurso;
    }

    /**
     * Devuelve la URL del recurso web, en caso de que el recurso sea de tipo WEB
     *
     * @return La URL del recurso web
     */
    public String getUrlWeb() {
        return urlWeb;
    }

    /**
     * Devuelve el tipo de recurso (VIDEO, AUDIO o WEB)
     *
     * @return El tipo de recurso
     */
    public TipoRecurso getTipo() {
        return tipo;
    }

    /**
     * Indica si los detalles del recurso están expandidos en la interfaz de usuario
     *
     * @return true si los detalles están expandido
     */
    public boolean isExpandido() {
        return expandido;
    }

    /**
     * Establece el estado de expansión de los detalles del recurso
     *
     * @param expandido si los detalles deben ser expandidos
     */
    public void setExpandido(boolean expandido) {
        this.expandido = expandido;
    }

    /**
     * Devuelve el identificador del icono representativo del recurso
     *
     * @return El identificador del icono.
     */
    public int getIdIcono() {
        return idIcono;
    }

    /**
     * Constructor utilizado para reconstruir un objeto desde un Parcel
     * Este constructor se llama de forma automática cuando se desea deserializar el objeto
     *
     * @param in contiene los datos serializados del objeto
     */
    protected Recurso(Parcel in) {
        titulo = in.readString();
        descripcion = in.readString();
        idRecurso = in.readInt();
        urlWeb = in.readString();
        tipo = TipoRecurso.valueOf(in.readString());  // Recupera el valor del enum desde el nombre.
        expandido = in.readByte() != 0;  // Recupera el valor booleano como byte.
        idIcono = in.readInt();
    }

    /**
     * Escribe los datos del objeto para su serialización
     *
     * @param dest  En el se escribirán los datos
     * @param flags Flags de serialización adicionales
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(titulo);
        dest.writeString(descripcion);
        dest.writeInt(idRecurso);
        dest.writeString(urlWeb);
        dest.writeString(tipo.name());  // Almacena el nombre del enum.
        dest.writeByte((byte) (expandido ? 1 : 0));  // Convierte boolean a byte (1 o 0).
        dest.writeInt(idIcono);
    }

    /**
     * Describe el tipo de contenido especial que podría tener el objeto
     * En este caso, no hay contenido especial
     * Por contenido especial se entiende un archivo o algún tipo de seguridad especial
     * En este caso devolverá 0
     *
     * @return 0.
     */
    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * Objeto necesario para la deserialización del objetos
     * Permite crear un nuevo objeto
     */
    public static final Creator<Recurso> CREATOR = new Creator<Recurso>() {
        @Override
        public Recurso createFromParcel(Parcel in) {
            return new Recurso(in);  // Llama al constructor que deserializa el objeto.
        }
        @Override
        public Recurso[] newArray(int size) {
            return new Recurso[size];  // Crea un array de objetos Recurso del tamaño solicitado.
        }
    };
}