package Sort;

import Model.Gene;
import Model.GeneInteraction;

import java.util.ArrayList;
import java.util.Scanner;

public class OrderCascade {
    static ArrayList<Gene> geneList = new ArrayList<>();
    static ArrayList<GeneInteraction> interactionList = new ArrayList<>();
    static ArrayList<Gene> cascade = new ArrayList<>();
    private static ArrayList<String> relation = new ArrayList<>();

    static Gene firstGene;
    static Gene secondGene;
    static Gene mutation;
    static GeneInteraction interaction;

    public static void addRelations() {
        // Initializing the scanner object and collecting the mutation as an input
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the mutation: ");
        // initialize the mutation by creating a gene object for the mutation with associated
        // abstract booleans (ie. just plugging in boolean values that make sense)
        initializeMutation(scanner.nextLine());
        // A little disclaimer about how the program works at its UI, because it is
        // not very clear given the input-and-go format what values you actually
        // need to run this program.
        System.out.println("IMPORTANT: You will be prompted to input the mutant genes/signals in the signaling cascade.");
        System.out.println("If only one gene was mutated, leave the second input blank.");
        // Running the program on a while loop to continue collecting data until
        // the user has input 'q' for quit at the quit stage.
        while (true) {
            System.out.println("Enter a mutant gene/signal: ");
            String geneOne = scanner.nextLine();
            System.out.println("Enter a mutant gene/signal: ");
            String geneTwo = scanner.nextLine();
            System.out.println("-'M' for mutant, or 'WT' for wild type-");
            System.out.println("Enter the resulting phenotype: ");
            String phenotype = scanner.nextLine();
            // The 'getFirstGene()' and similarly, getSecondGene() functions are similar to
            // the initializeMutation() function used previously. It simply makes a new gene
            // if a gene with the same name as the input does not already exist. If the gene
            // already exists, then firstGene (or secondGene) are set to that respective gene.
            firstGene = getFirstGene(geneOne);
            secondGene = getSecondGene(geneTwo);
            // I wasn't too sure how to implement the Gene and GeneInteraction classes
            // elegantly without making them distinct. But considering that all the
            // information is in the GeneInteractions themselves, I probably
            // could have done without making a Gene class.
            // The setInteractions() function takes the first and second genes assigned
            // previously and makes a GeneInteraction object with both genes (no particular
            // distinction) and the associated phenotype.
            setInteractions(phenotype);
            // quit
            System.out.println("Enter 'q' to quit, or press Enter to add another interaction.");
            if (scanner.nextLine().equals("q")) {
                break;
            }
        }
    }

    // The mutation is always downstream to all other genes/signals that come from
    // the signaling cascade. So I add the whole geneList (except the mutation itself)
    // to the list of upstream genes for the mutation-causing gene.
    private static void loadUpstreamMutations() {
        for (Gene g : geneList) {
            if (!g.equals(mutation)) {
                mutation.addUpstreamGene(g);
            }
        }
    }

    // Makes a new Gene with the mutation's name, then assign it so that
    // if you delete the mutation gene, you get the mutation.
    // I add the mutation to the cascade early on because it is always
    // at the end of any given cascade.
    private static void initializeMutation(String name) {
        mutation = new Gene(name);
        mutation.setLossGivesMutant(true);
        geneList.add(mutation);
        cascade.add(mutation);
    }

    // Simply makes a new GeneInteraction object and plugs in values for it.
    // I could have included all of this in the constructor, but I wanted to
    // have all my ArrayLists in one place and use my functions without having
    // to worry too much about visibility. If I have time, I might just include
    // parameters to my GeneInteraction constructor and handle all it from there.
    private static void setInteractions(String phenotype) {
        interaction = new GeneInteraction();
        interaction.setGeneOne(firstGene);
        interaction.setGeneTwo(secondGene);
        interaction.setPhenotype(phenotype);
        interactionList.add(interaction);
    }

    // Goes through the geneList and sees if a Gene with the same name has been
    // made. If so, that Gene is returned. Otherwise, a new Gene with the given
    // name is created.
    private static Gene getFirstGene(String geneOne) {
        for (Gene g : geneList) {
            if (g.getName().equals(geneOne)) {
                return g;
            }
        }
        firstGene = new Gene(geneOne);
        geneList.add(firstGene);
        return firstGene;
    }

    private static Gene getSecondGene(String geneTwo) {
        if (geneTwo.equals("")) {
            return mutation;
        } else {
            for (Gene g : geneList) {
                if (g.getName().equals(geneTwo)) {
                    return g;
                }
            }
            secondGene = new Gene(geneTwo);
            geneList.add(secondGene);
            return secondGene;
        }
    }

    public static void findOrder() {
        while (cascade.size() < geneList.size()) {
            addNextGene();
        }
    }

    private static void addNextGene() {
        Gene lastGene = cascade.get(cascade.size() - 1);
        ArrayList<Gene> upstreamGenes = lastGene.getUpstreamGenes();

        for (Gene g : upstreamGenes) {
            if (noOtherDownstreamGenes(g)) {
                cascade.add(g);
            }
        }
    }

    private static boolean noOtherDownstreamGenes(Gene g) {
        ArrayList<Gene> downstreamGenes = g.getDownstreamGenes();
        for (Gene dsg : downstreamGenes) {
            if (!cascade.contains(dsg)) {
                return false;
            }
        }
        return true;
    }

    public static void findEpistasis() {
        Gene geneOne;
        Gene geneTwo;

        loadUpstreamMutations();
        loadDownstreamMutation();

        for (GeneInteraction interaction : interactionList) {
            geneOne = interaction.getGeneOne();
            geneTwo = interaction.getGeneTwo();
            if (geneTwo.equals(mutation)) {
                geneOne.setLossGivesMutant((interaction.getPhenotype().equals("M")));
            }
        }
        for (GeneInteraction interaction : interactionList) {
            geneOne = interaction.getGeneOne();
            geneTwo = interaction.getGeneTwo();
            if (!geneOne.equals(mutation) && !geneTwo.equals(mutation)) {
                updateRelation(interaction);
            }
        }
    }

    private static void loadDownstreamMutation() {
        for (Gene g : geneList) {
            if (!g.equals(mutation)) {
                g.addDownstreamGene(mutation);
            }
        }
    }

    private static void updateRelation(GeneInteraction interaction) {
        Gene geneOne = interaction.getGeneOne();
        Gene geneTwo = interaction.getGeneTwo();
        boolean geneOneLoss = geneOne.getLossGivesMutant();
        boolean geneTwoLoss = geneTwo.getLossGivesMutant();

        if ((geneOneLoss && !geneTwoLoss) || (geneTwoLoss && !geneOneLoss)) {
            if (interaction.getPhenotype().equals("M") == geneOne.getLossGivesMutant()) {
                geneOne.addUpstreamGene(geneTwo);
                geneTwo.addDownstreamGene(geneOne);
            } else {
                geneTwo.addUpstreamGene(geneOne);
                geneOne.addDownstreamGene(geneTwo);
            }
        }
    }

    public static void printGenes() {
        String cascadeOutput = "";
        for (int i = cascade.size() - 1; i >= 0; i--) {
            cascadeOutput = cascadeOutput + " " + cascade.get(i).getName();
            if (i > 0) {
                cascadeOutput = cascadeOutput + " " + relation.get(i);
            }
        }
        System.out.println(cascadeOutput);
    }

    public static void findRelations() {
        boolean tempBool = true;
        // If the mutation at previous gene in cascade produces the mutant phenotype, then
        // simply append "---->" if the current gene does likewise. Else, append "----|"
        // and change the tempBool to match the behavior of the current gene.
        for (Gene g : cascade) {
            if (g.getLossGivesMutant()==tempBool) {
                relation.add("---->");
            } else{
                relation.add("----|");
                tempBool = !tempBool;
            }
        }
    }
}