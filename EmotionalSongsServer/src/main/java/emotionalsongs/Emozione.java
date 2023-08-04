package emotionalsongs;

import java.io.Serializable;

public abstract class Emozione implements Serializable {

    public Emozione(){
        super();
    }

    protected abstract int getPunteggio();
    protected abstract String getCommento();
    protected abstract void setPunteggio(int punteggio);
    protected abstract void setCommento(String commento);
}

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