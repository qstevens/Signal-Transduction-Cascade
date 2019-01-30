package Model;

import java.util.ArrayList;

public class Gene {
    private String name;
    private boolean isSuppressor;
    private boolean lossGivesMutant;
    private ArrayList<Gene> upstreamGenes = new ArrayList<>();
    private ArrayList<Gene> downstreamGenes = new ArrayList<>();

    public Gene(String name){
        this.name = name;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isSuppressor() {
        return isSuppressor;
    }

    public void setSuppressor(boolean suppressor) {
        isSuppressor = suppressor;
    }

    public boolean getLossGivesMutant() {
        return lossGivesMutant;
    }

    public void setLossGivesMutant(boolean suppressorOfMutation) {
        lossGivesMutant = suppressorOfMutation;
    }

    public void addUpstreamGene(Gene g){
        upstreamGenes.add(g);
    }

    public void addDownstreamGene(Gene g){
        downstreamGenes.add(g);
    }

    public ArrayList<Gene> getUpstreamGenes() {
        return upstreamGenes;
    }

    public ArrayList<Gene> getDownstreamGenes() {
        return downstreamGenes;
    }
}