package com.example.geolocalisation;

public class SimulBeacon {
    private String uuid;
    private int numMaj;
    private int numMin;
    public SimulBeacon(String u , int m , int mi){
        setUuid(u);
        setNumMaj(m);
        setNumMin(mi);
    }
@Override
public String toString(){
        return "uuid=" + uuid + "\tmajor=" + numMaj + "\t" + numMin;
}
    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public int getNumMaj() {
        return numMaj;
    }

    public void setNumMaj(int numMaj) {
        this.numMaj = numMaj;
    }

    public int getNumMin() {
        return numMin;
    }

    public void setNumMin(int numMin) {
        this.numMin = numMin;
    }
}
