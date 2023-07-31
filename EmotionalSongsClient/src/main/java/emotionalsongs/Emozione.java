package emotionalsongs;


import java.util.ArrayList;

public abstract class Emozione {

    private Emozione[] emozioni = {new Joy(1,"test") }; // aggiungere emozioni rimanenti.

    public Emozione(){
        super();
    }

    public Emozione(String[] data){
        //  query handler, query su db con union
    }

    public Emozione(ArrayList<Emozione> emozioni){
        // costruttore usato a lato client. valorizza emozione per emozione.
    }

    protected abstract int getPunteggio();
    protected abstract String getCommento();

}

class Joy extends Emozione{

    private int punteggio = 0;
    private String commento = "";

    protected Joy(int punteggio, String commento){
        this.punteggio = punteggio;
        this.commento = commento;
    }

    @Override
    protected int getPunteggio(){
        return this.punteggio;
    }

    @Override
    protected String getCommento() {
        return "";
    }

}

class Sadness extends Emozione{

    @Override
    protected int getPunteggio() {
        return 0;
    }

    @Override
    protected String getCommento() {
        return null;
    }
}
