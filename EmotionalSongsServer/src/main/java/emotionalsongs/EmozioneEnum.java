package emotionalsongs;

/*
 * Progetto svolto da:
 *
 * Corallo Samuele 749719, Ateneo di Varese
 * Della Chiesa Mattia 749904, Ateneo di Varese
 *
 */

/**
 * Enumeration representing different emotions.<br><br>
 *
 * This enumeration defines various emotions along with their descriptions.
 * It provides methods to retrieve a description and name for each emotion,
 * and also to create corresponding instances of specific {@link Emozione} subclasses.
 *
 * @author <a href="https://github.com/SpitefulCookie"> Della Chiesa Mattia</a>
 */
public enum EmozioneEnum{

    AMAZEMENT,
    SOLEMNITY,
    TENDERNESS,
    NOSTALGIA,
    CALMNESS,
    POWER,
    JOY,
    TENSION,
    SADNESS;

    /**
     * Returns the description of the emotion.
     *
     * @return an object of type {@code String} that contains a short description of the emotion.
     */
    public String description(){

        return switch (this) {
            case AMAZEMENT -> "Feeling of wonder or happiness";
            case CALMNESS -> "Relaxation, serenity, meditativeness";
            case JOY -> "Feels like dancing, bouncy feeling, animated, amused";
            case NOSTALGIA -> "Dreamy, melancholic, sentimental feelings";
            case POWER -> "Feeling strong, heroic, triumphant, energetic";
            case SADNESS -> "Feeling Depressed, sorrowful";
            case SOLEMNITY -> "Feeling of transcendence, inspiration. Thrills.";
            case TENDERNESS -> "Sensuality, affect, feeling of love";
            case TENSION -> "Feeling Nervous, impatient, irritated";
        };

    }

    /**
     * Returns the name of the emotion.
     *
     * @return an object of type {@code String} that representing the name of the emotion.
     */
    @Override
    public String toString(){

        return switch (this) {
            case AMAZEMENT -> "Amazement";
            case CALMNESS -> "Calmness";
            case JOY -> "Joy";
            case NOSTALGIA -> "Nostalgia";
            case POWER -> "Power";
            case SADNESS -> "Sadness";
            case SOLEMNITY -> "Solemnity";
            case TENDERNESS -> "Tenderness";
            case TENSION -> "Tension";
        };

    }

    /**
     * Creates an instance of a specific {@link Emozione} class based on the enum value.
     *
     * @param enumValue The index of the enum value.
     * @return An instance of the corresponding emotion class.
     */
     static Emozione getInstanceFromEnum(int enumValue){
        return switch (EmozioneEnum.values()[enumValue]) {
            case AMAZEMENT -> new Amazement(0, "");
            case CALMNESS -> new Calmness(0, "");
            case JOY -> new Joy(0, "");
            case NOSTALGIA -> new Nostalgia(0,"");
            case POWER -> new Power(0, "");
            case SADNESS -> new Sadness(0, "");
            case SOLEMNITY -> new Solemnity(0, "");
            case TENDERNESS -> new Tenderness(0, "");
            case TENSION -> new Tension(0, "");
        };
    }

    /**
     * Retrieves the name of the emotion class from an Emozione instance.<br><br>
     *
     * <p>This static method takes an instance of the {@link Emozione} class and returns the
     * simple name of the class representing the emotion. The simple name is obtained using the
     * {@link Class#getSimpleName()} method on the provided instance's class object.
     *
     * @param e The instance of the {@link Emozione} class representing an emotion.
     * @return The simple name of the emotion class.
     */
    static String getInstanceName(Emozione e){
        return e.getClass().getSimpleName();
    }

}