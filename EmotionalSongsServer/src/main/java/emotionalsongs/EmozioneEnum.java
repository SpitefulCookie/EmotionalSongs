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

        if (this == AMAZEMENT) {
            return "Feeling of wonder or happiness";
        } else if (this == CALMNESS) {
            return "Relaxation, serenity, meditativeness";
        } else if (this == JOY) {
            return "Feels like dancing, bouncy feeling, animated, amused";
        } else if (this == NOSTALGIA) {
            return "Dreamy, melancholic, sentimental feelings";
        } else if (this == POWER) {
            return "Feeling strong, heroic, triumphant, energetic";
        } else if (this == SADNESS) {
            return "Feeling Depressed, sorrowful";
        } else if (this == SOLEMNITY) {
            return "Feeling of transcendence, inspiration. Thrills.";
        } else if (this == TENDERNESS) {
            return "Sensuality, affect, feeling of love";
        } else if (this == TENSION) {
            return "Feeling Nervous, impatient, irritated";
        } else {
            // Sie könnten einen Fallback-Wert zurückgeben oder eine Exception auslösen
            return "Unknown";
        }


    }

    /**
     * Returns the name of the emotion.
     *
     * @return an object of type {@code String} that representing the name of the emotion.
     */
    @Override
    public String toString(){

        return this.name();

    }

    /**
     * Creates an instance of a specific {@link Emozione} class based on the enum value.
     *
     * @param enumValue The index of the enum value.
     * @return An instance of the corresponding emotion class.
     */
     static Emozione getInstanceFromEnum(int enumValue){
         EmozioneEnum emotion = EmozioneEnum.values()[enumValue];

         if (emotion == EmozioneEnum.AMAZEMENT) {
             return new Amazement(0, "");
         } else if (emotion == EmozioneEnum.CALMNESS) {
             return new Calmness(0, "");
         } else if (emotion == EmozioneEnum.JOY) {
             return new Joy(0, "");
         } else if (emotion == EmozioneEnum.NOSTALGIA) {
             return new Nostalgia(0, "");
         } else if (emotion == EmozioneEnum.POWER) {
             return new Power(0, "");
         } else if (emotion == EmozioneEnum.SADNESS) {
             return new Sadness(0, "");
         } else if (emotion == EmozioneEnum.SOLEMNITY) {
             return new Solemnity(0, "");
         } else if (emotion == EmozioneEnum.TENDERNESS) {
             return new Tenderness(0, "");
         } else if (emotion == EmozioneEnum.TENSION) {
             return new Tension(0, "");
         } else {
             // Sie könnten einen Fallback-Wert zurückgeben oder eine Exception auslösen
             return null; // Oder `throw new IllegalArgumentException("Invalid enumValue");`
         }

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