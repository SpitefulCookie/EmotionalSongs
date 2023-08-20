package emotionalsongs;

/*
 * Progetto svolto da:
 *
 * Corallo Samuele 749719, Ateneo di Varese
 * Della Chiesa Mattia 749904, Ateneo di Varese
 *
 */

import java.io.Serial;
import java.io.Serializable;

/**
 * Class that represents an abstract emotion felt by the application's user while listening to a song.
 *
 * <p>The {@code Emozione} class serves as the base class for the various emotions felt by the application user.
 * Its purpose is to define the general behaviour its subclasses.
 *
 * @author <a href="https://github.com/SpitefulCookie">Della Chiesa Mattia</a>
 */
public abstract class Emozione implements Serializable {

    @Serial private static final long serialVersionUID = 1L;

    /**
     * Constructs a new instance of {@link Emozione}.
     */
    public Emozione(){
        super();
    }

    /**
     * Retrieves the emotion's score.
     *
     * @return The emotion's score.
     */
    protected abstract int getPunteggio();

    /**
     * Retrieves the comment associated with the emotion.
     *
     * @return The emotion's comment.
     */
    protected abstract String getCommento();

    /**
     * Sets the emotion's score.
     *
     * @param punteggio The emotion's score to set.
     */
    protected abstract void setPunteggio(int punteggio);

    /**
     * Sets the comment associated with the emotion.
     *
     * @param commento The comment to set for the emotion.
     */
    protected abstract void setCommento(String commento);
}

/**
 * Represents the emotion of Amazement.<br><br>
 *
 * <p>The {@code Amazement} class represents the specific emotion of amazement.
 * It defines methods to retrieve and set the emotion's score and comment.
 */
class Amazement extends Emozione {

    private int punteggio;
    private String commento;

    protected Amazement(int punteggio, String commento){
        this.punteggio = punteggio;
        this.commento = commento;
    }

    @Override protected int getPunteggio(){return this.punteggio;}
    @Override protected String getCommento() { return this.commento;}
    @Override protected void setPunteggio(int punteggio){this.punteggio = punteggio;}
    @Override protected void setCommento(String commento) {this.commento = commento;}

}

/**
 * Represents the emotion of Solemnity.<br><br>
 *
 * <p>The {@code Solemnity} class represents the specific emotion of amazement.
 * It defines methods to retrieve and set the emotion's score and comment.
 */
class Solemnity extends Emozione {

    private int punteggio;
    private String commento;

    protected Solemnity(int punteggio, String commento){
        this.punteggio = punteggio;
        this.commento = commento;
    }

    @Override protected int getPunteggio(){return this.punteggio;}
    @Override protected String getCommento() { return this.commento;}
    @Override protected void setPunteggio(int punteggio){this.punteggio = punteggio;}
    @Override protected void setCommento(String commento) {this.commento = commento;}

}

/**
 * Represents the emotion of Tenderness.<br><br>
 *
 * <p>The {@code Tenderness} class represents the specific emotion of amazement.
 * It defines methods to retrieve and set the emotion's score and comment.
 */
class Tenderness extends Emozione {

    private int punteggio;
    private String commento;

    protected Tenderness(int punteggio, String commento){
        this.punteggio = punteggio;
        this.commento = commento;
    }

    @Override protected int getPunteggio(){return this.punteggio;}
    @Override protected String getCommento() { return this.commento;}
    @Override protected void setPunteggio(int punteggio){this.punteggio = punteggio;}
    @Override protected void setCommento(String commento) {this.commento = commento;}

}

/**
 * Represents the emotion of Nostalgia.<br><br>
 *
 * <p>The {@code Nostalgia} class represents the specific emotion of amazement.
 * It defines methods to retrieve and set the emotion's score and comment.
 */
class Nostalgia extends Emozione {

    private int punteggio;
    private String commento;

    protected Nostalgia(int punteggio, String commento){
        this.punteggio = punteggio;
        this.commento = commento;
    }

    @Override protected int getPunteggio(){return this.punteggio;}
    @Override protected String getCommento() { return this.commento;}
    @Override protected void setPunteggio(int punteggio){this.punteggio = punteggio;}
    @Override protected void setCommento(String commento) {this.commento = commento;}

}

/**
 * Represents the emotion of Calmness.<br><br>
 *
 * <p>The {@code Calmness} class represents the specific emotion of amazement.
 * It defines methods to retrieve and set the emotion's score and comment.
 */
class Calmness extends Emozione {

    private int punteggio;
    private String commento;

    protected Calmness(int punteggio, String commento){
        this.punteggio = punteggio;
        this.commento = commento;
    }

    @Override protected int getPunteggio(){return this.punteggio;}
    @Override protected String getCommento() { return this.commento;}
    @Override protected void setPunteggio(int punteggio){this.punteggio = punteggio;}
    @Override protected void setCommento(String commento) {this.commento = commento;}

}

/**
 * Represents the emotion of Power.<br><br>
 *
 * <p>The {@code Power} class represents the specific emotion of amazement.
 * It defines methods to retrieve and set the emotion's score and comment.
 */
class Power extends Emozione {

    private int punteggio;
    private String commento;

    protected Power(int punteggio, String commento){
        this.punteggio = punteggio;
        this.commento = commento;
    }

    @Override protected int getPunteggio(){return this.punteggio;}
    @Override protected String getCommento() { return this.commento;}
    @Override protected void setPunteggio(int punteggio){this.punteggio = punteggio;}
    @Override protected void setCommento(String commento) {this.commento = commento;}

}

/**
 * Represents the emotion of Joy.<br><br>
 *
 * <p>The {@code Joy} class represents the specific emotion of amazement.
 * It defines methods to retrieve and set the emotion's score and comment.
 */
class Joy extends Emozione {

    private int punteggio;
    private String commento;

    protected Joy(int punteggio, String commento){
        this.punteggio = punteggio;
        this.commento = commento;
    }

    @Override protected int getPunteggio(){return this.punteggio;}
    @Override protected String getCommento() { return this.commento;}
    @Override protected void setPunteggio(int punteggio){this.punteggio = punteggio;}
    @Override protected void setCommento(String commento) {this.commento = commento;}

}

/**
 * Represents the emotion of Tension.<br><br>
 *
 * <p>The {@code Tension} class represents the specific emotion of amazement.
 * It defines methods to retrieve and set the emotion's score and comment.
 */
class Tension extends Emozione {

    private int punteggio;
    private String commento;

    protected Tension(int punteggio, String commento){
        this.punteggio = punteggio;
        this.commento = commento;
    }

    @Override protected int getPunteggio(){return this.punteggio;}
    @Override protected String getCommento() { return this.commento;}
    @Override protected void setPunteggio(int punteggio){this.punteggio = punteggio;}
    @Override protected void setCommento(String commento) {this.commento = commento;}

}

/**
 * Represents the emotion of Sadness.<br><br>
 *
 * <p>The {@code Sadness} class represents the specific emotion of amazement.
 * It defines methods to retrieve and set the emotion's score and comment.
 */
class Sadness extends Emozione {

    private int punteggio;
    private String commento;

    protected Sadness(int punteggio, String commento){
        this.punteggio = punteggio;
        this.commento = commento;
    }

    @Override protected int getPunteggio(){return this.punteggio;}
    @Override protected String getCommento() { return this.commento;}
    @Override protected void setPunteggio(int punteggio){this.punteggio = punteggio;}
    @Override protected void setCommento(String commento) {this.commento = commento;}

}