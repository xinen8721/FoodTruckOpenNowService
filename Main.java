package com.foodtruck;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.Scanner;

public class Main {
    final private static String fileName= "resources/data.csv";
    private static List<String> csvLines;
    private static List<String> commandList = Arrays.asList("show-open-foodtrucks", "next-page", "prev-page", "end");
    private static List<String> commandDescriptions = Arrays.asList("Show you ten open food trucks and address if there are any.", "Next page of result", "Previous page of result", "Complete and Exit");
    private static HashMap<String, String> commandLibrary  = new HashMap<>();
    private static FoodTruckInfoOperation foodTruckInfoOperation = null;
    private static List<List<FoodTruckInfo>> PAGES = new ArrayList<>();
    private static int PAGE_INDEX = 0;
    private static boolean ONGOING = false;

    public static void main(String[] args) {
        //Initializing the FoodTruckInfo data
        startSession();
        
        Scanner scanner = new Scanner(System.in);
        System.out.println("\n\nWelcome to FoodTruckOpenNow Service in San Francisco!\n\n");
        Scanner userInput = new Scanner(System.in);
        String command = "";
        while(!command.equalsIgnoreCase("End")) {
            System.out.println("waiting for your command, type \"help\" for more info");

            command = userInput.nextLine().toLowerCase();
            if (!command.isEmpty()) {
                switch(command) {
                    case "show-open-foodtrucks":
                        showOpenFoodTruckNow();
                        break;
                    case "next-page":
                        showNextPage();
                        break;
                    case "prev-page":
                        showPreviousPage();
                        break;
                    case "help" :
                        showCommands();
                        break;
                }
            }
        }
        endSession();
    }
    public static void startSession() {
        buildCommandLibrary();
        buildFoodTruckInfo();
    }

    public static void retrieveOpenFoodTrucks() {
        ONGOING = true;
        try {
            List<FoodTruckInfo> list = foodTruckInfoOperation.getFoodTruckByCurrentDate();
            System.out.println("There are " + (list == null ? 0 : list.size()) + " foodtrucks open now");
            List<FoodTruckInfo> page = new ArrayList<>();
            for (FoodTruckInfo foodTruckInfo : list) {
                page.add(foodTruckInfo);
                if(page.size() == 10) {
                    PAGES.add(page);
                    page = new ArrayList<>();
                }
            }

            //Deal with first page or last page where there are less than 10 results
            if(!page.isEmpty()) {
                PAGES.add(page);
            }
        } catch (Exception e) {
            System.out.println("Oops, currently there are no foodtrucks open");
        }
    }

    public static void dispaySinglePage(int PAGE_INDEX) {
        if(PAGE_INDEX == PAGES.size()) {
            System.out.println("\n\nWARNING. No more results, please enter \"show-open-foodtrucks\" to start again\n\n");
            PAGE_INDEX = 0;
            ONGOING = false;
            return;
        }
        int index = 1;
        System.out.println("PAGE "+ (PAGE_INDEX + 1));
        for(FoodTruckInfo foodTruckInfo : PAGES.get(PAGE_INDEX)) {
            System.out.println(index++ + "\nName: " + foodTruckInfo.getName() + "\nAddress: " + foodTruckInfo.getAddress() +"\n");
        }

    }

    public static void showOpenFoodTruckNow() {
        if(!ONGOING){
            retrieveOpenFoodTrucks();
        }
        dispaySinglePage(PAGE_INDEX++);
    }

    public static void showNextPage() {
        if(isValidStatus()) {
            dispaySinglePage(PAGE_INDEX++);
        }
    }

    public static void showPreviousPage() {
        if(isValidStatus()) {
            if (PAGE_INDEX - 2 >= 0) {
                PAGE_INDEX -= 2;
                dispaySinglePage(PAGE_INDEX++);
            } else {
                System.out.println("No previous results found.");
            }
        }
    }

    public static boolean isValidStatus() {
        if(!ONGOING) {
            System.out.println("please enter \"show-open-foodtrucks\" to start");
            return false;
        }
        return true;
    }

    public static void endSession() {
        System.out.println("Bye!");
    }

    public static void buildCommandLibrary() {
        for(int i=0; i<commandList.size(); i++) {
            commandLibrary.put(commandList.get(i), commandDescriptions.get(i));
        }
    }

    public static void buildFoodTruckInfo() {
        CSVParser csvParser = new CSVParser(fileName);
        csvLines = csvParser.parse();
        if(foodTruckInfoOperation == null) {
            foodTruckInfoOperation = new FoodTruckInfoOperation();
        }

        foodTruckInfoOperation.saveData(csvLines);
    }
    public static void showCommands() {
        if(commandLibrary.isEmpty()) {
            buildCommandLibrary();
        }
        for(Map.Entry<String, String> e : commandLibrary.entrySet()) {
            System.out.println(e.getKey() + " : " + e.getValue());
        }
    }
}
