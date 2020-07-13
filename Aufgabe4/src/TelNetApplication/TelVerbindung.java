package TelNetApplication;

public class TelVerbindung implements Comparable<TelVerbindung> {
    Integer c; //Verbindungskosten
    TelKnoten u; //Anfangsknoten
    TelKnoten v; //Endknoten

    public TelVerbindung(TelKnoten u, TelKnoten v, int c) {
        this.c = c;
        this.u = u;
        this.v = v;
    }

    @Override
    public String toString() {
        return "Verbindungskosten: " + c + "\nAnfangsknoten: " + u + "\nEndknoten: " + v + "\n";
    }

    public int compareTo(TelVerbindung v) {
        return c.compareTo(v.c);
    }
}
