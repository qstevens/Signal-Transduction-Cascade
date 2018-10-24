package UI;

public class LinearSignalCascade {
    public static void main(String[] args) {
        Sort.OrderCascade.addRelations();
        Sort.OrderCascade.findEpistasis();
        Sort.OrderCascade.findOrder();
        Sort.OrderCascade.findRelations();
        Sort.OrderCascade.printGenes();
    }
}
