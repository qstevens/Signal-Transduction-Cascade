package Model;

public class GeneInteraction {

    private Gene geneOne;
    private Gene geneTwo;
    private String phenotype;

    public Gene getGeneOne() {
        return geneOne;
    }

    public void setGeneOne(Gene geneOne) {
        this.geneOne = geneOne;
    }

    public Gene getGeneTwo() {
        return geneTwo;
    }

    public void setGeneTwo(Gene geneTwo) {
        this.geneTwo = geneTwo;
    }

    public String getPhenotype() {
        return phenotype;
    }

    public void setPhenotype(String phenotype) {
        this.phenotype = phenotype;
    }
}
