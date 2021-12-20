
package tdtu.myquizapp;

public class TrafficSignModel {
    private String name;
    private int sets;


    public TrafficSignModel(String name, int sets) {
        this.name = name;
        this.sets = sets;

    }
    public TrafficSignModel(){

    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSets() {
        return sets;
    }

    public void setSets(int sets) {
        this.sets = sets;
    }


}

