import infra.primary.PowerPlantProductionResource;

class Main {
  public static void main(String[] args) {
    // TODO check inputs

    String from = args[0];
    String to = args[1];
    String format = args[2];

    PowerPlantProductionResource resource = new PowerPlantProductionResource();
    System.out.println(resource.aggregateProductions(from, to, format));
  }
}
