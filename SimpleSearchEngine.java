import java.util.*;
import java.io.File;

public class SimpleSearchEngine {
    Scanner input;
    Scanner reader;
    Map<String, List<Integer>> people;
    List<String> lines;
    File list;
    Search searcher;

    public SimpleSearchEngine() {
        this.input = new Scanner(System.in);
        this.searcher = new Search(null);
    }



    static class Search {
        private SearchStrategy strategy;

        public Search(SearchStrategy strategy) {
            this.strategy = strategy;
        }

        public void setStrategy(SearchStrategy strategy) {
            this.strategy = strategy;
        }

        public void conductSearch(String query) {
            this.strategy.search(query);
        }


    }

    interface SearchStrategy {

        void search(String query);

    }

    class All implements SearchStrategy {

        public void search(String query) {
            Set<Integer> indices = new HashSet<>();
            Set<String> words = new HashSet<>(Arrays.asList(query.split("\\s+")));
            for (String word : words) {
                for (Integer i : people.getOrDefault(word.toLowerCase(), Collections.emptyList())) {
                    for (String word2 : words) {
                        if (people.getOrDefault(word2.toLowerCase(), Collections.emptyList()).contains(i)) {
                            indices.add(i);
                        } else {
                            indices.remove(i);
                        }
                    }
                }
            }
            System.out.println(indices.size() + " people found: ");
            indices.forEach(index -> System.out.println(lines.get(index)));
            System.out.println();
            System.out.println();
        }
    }

    class Any implements SearchStrategy {

        public void search(String query) {
            Set<Integer> indices = new HashSet<>();
            Set<String> words = new HashSet<>(Arrays.asList(query.split("\\s+")));
            for (String word : words) {
                indices.addAll(people.getOrDefault(word.toLowerCase(), Collections.emptyList()));
            }
            System.out.println(indices.size() + " people found: ");
            indices.forEach(index -> System.out.println(lines.get(index)));
            System.out.println();
            System.out.println();
        }
    }

    class None implements SearchStrategy {

        public void search(String query) {
            Set<Integer> indices = new HashSet<>();
            Set<String> words = new HashSet<>(Arrays.asList(query.split("\\s+")));
            for (String word : words) {
                indices.addAll(people.getOrDefault(word.toLowerCase(), Collections.emptyList()));
            }
            System.out.println(indices.size() + " people found: ");
            for (int i = 0; i < lines.size(); i++) {
                if (!indices.contains(i)) {
                    System.out.println(lines.get(i));
                }
            }
            System.out.println();
            System.out.println();
        }
    }



    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        SimpleSearchEngine search = new SimpleSearchEngine();
        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("--data")) {
                search.setPathToFile(args[i+1]);
            }
        }
        search.addPeople();
        int choice = Integer.parseInt(input.nextLine());
        while (choice != 0) {
            search.menu(choice);
            choice = Integer.parseInt(input.nextLine());
        }
    }

    public void menu(int choice) {
        System.out.println("=== Menu ===");
        System.out.println("1. Find a person");
        System.out.println("2. Print all people");
        System.out.println("0. Exit");
        switch (choice) {
            case 0:
                System.out.println();
                System.out.println();
                System.out.println("Bye!");
                break;
            case 1:
                System.out.println();
                System.out.println();
                this.searchPeople();
                break;
            case 2:
                System.out.println();
                System.out.println();
                this.printPeople();
                break;
            default:
                System.out.println();
                System.out.println();
                System.out.println("Incorrect option! Try again.");
                System.out.println();
                System.out.println();
                break;
        }
    }

    public static boolean checkIfContains(String[] s1, String s2) {
        boolean check = false;
        for (String s : s1) {
            if (s.toLowerCase().equals(s2.toLowerCase())) {
                check = true;
                break;
            }
        }
        return check;
    }

    public void addPeople() {
        this.lines = new ArrayList<>();
        this.people = new HashMap<>();
        while (reader.hasNext()) {
            lines.add(reader.nextLine());
        }
        this.reader.close();
        for (int i = 0; i < lines.size(); i++) {
            List<Integer> indices = new ArrayList<>();
            String[] array1 = lines.get(i).split("\\s+");
            for (String s : array1) {
                for (int j = i; j < lines.size(); j++) {
                    String[] array2 = lines.get(j).split("\\s+");
                    if (checkIfContains(array2, s) && !indices.contains(j)) {
                        indices.add(j);
                    }
                }
                people.putIfAbsent(s.toLowerCase(), indices);
            }
        }
    }

    public void printPeople() {
        System.out.println("=== List of people ===");
        for (String person : lines) {
            System.out.println(person);
        }
        System.out.println();
        System.out.println();
    }

    public void searchPeople() {
        System.out.println("Select a matching strategy: ALL, ANY, NONE");
        String strategy = input.nextLine();
        switch (strategy) {
            case "ALL":
                searcher.setStrategy(new All());
                break;
            case "ANY":
                searcher.setStrategy(new Any());
                break;
            case "NONE":
                searcher.setStrategy(new None());
                break;
            default:
                System.out.println("Please choose a valid strategy");
                return;
        }
        System.out.println("Enter a name or email to search all suitable people.");
        String query = input.nextLine();
        searcher.conductSearch(query);
    }

    public void setPathToFile(String path){
        this.list = new File(path);
        try {
            this.reader = new Scanner(this.list);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}