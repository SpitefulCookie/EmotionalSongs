package emotionalsongs;

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

     static Emozione getEmotionInstanceFromEnumValue(int enumValue){
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

}