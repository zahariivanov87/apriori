package com.data.mining.apriori;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.stream.Collectors;

import com.data.mining.application.Application;
import com.data.mining.item.Item;

import javafx.util.Pair;

/**
 * Apriori algorithm implementation
 */
public class Apriori {

    private List<Application> applications;

    private int threshold;

    public Apriori(List<Application> applications) {
        this.applications = applications;
        // Default threshold is at least 60% of the size of the applications.
        this.threshold = applications.size() * 60 / 100;
    }

    public Apriori(List<Application> applications, int threshold) {
        this.applications = applications;
        this.threshold = threshold;
    }

    public Set<Item> recommend() {

        validate();

        Map<Item, Long> support = support();

        //If items after support's calculation contain only 1 element, no need to go further.
        if (support.size() == 1) {
            return new HashSet<>(Arrays.asList(support.keySet().iterator().next()));
        }

        Map<Pair<Item, Item>, Long> join = join(support);
        return itemsetsAfterSecondJoin(join).stream().flatMap(r -> r.stream().map(item -> item
        )).collect(Collectors.toSet());
    }

    private Map<Pair<Item, Item>, Long> join(Map<Item, Long> support) {

        Map<Pair<Item, Item>, Long> join = new LinkedHashMap<>();

        Queue<Item> items = new LinkedList<>(support.keySet());

        //Prune items
        while (!items.isEmpty()) {

            Item item = items.poll();

            for (Item i : items) {
                Pair<Item, Item> itemItemPair = new Pair<>(item, i);
                join.put(itemItemPair, countOccuranceOfPair(itemItemPair));
            }

        }

        return join;
    }

    private Long countOccuranceOfPair(Pair<Item, Item> itemsPair) {

        return applications.stream()
                .filter(app ->
                        app.items.stream().map(item -> item.value).collect(Collectors.toList())
                                .containsAll(Arrays.asList(itemsPair.getKey().value,
                                        itemsPair.getValue().value))).count();
    }

    private Set<Set<Item>> itemsetsAfterSecondJoin(Map<Pair<Item, Item>, Long>
            joinedPairs) {

        // Get all pairs that are under threshold
        List<Pair<Item, Item>> underThresholdPairs = joinedPairs.entrySet().stream()
                .filter(entry -> entry.getValue() < threshold).map(paid -> paid.getKey()).collect
                        (Collectors.toList());

        //Get all pairst that are above threshold
        List<Pair<Item, Item>> aboveThresholdPairs = joinedPairs.keySet().stream()
                .filter(pair -> !underThresholdPairs.contains(pair)).collect
                        (Collectors.toList());

        //Do Prune with union of pairs.
        // Check in DB for pairs that don't satisfy threshold and ignore them from result
        Queue<Pair<Item, Item>> items = new LinkedList<>(aboveThresholdPairs);

        //Store itemsets after last prune that satisfy frequency
        Set<Set<Item>> itemsAfterSecondJoin = new HashSet<>();

        while (!items.isEmpty()) {

            Pair<Item, Item> currentPair = items.poll();

            //merege with other items
            for (Pair<Item, Item> pair : items) {
                Set<Item> itemset = new HashSet<>();

                itemset.addAll(Arrays.asList(currentPair.getKey(), currentPair.getValue(), pair
                        .getKey(), pair.getValue()));

                if (underThresholdPairs.stream().filter(underThresholdPair -> itemset.stream().map
                        (item -> item.value).collect(Collectors.toList())
                        .containsAll(Arrays.asList(underThresholdPair.getKey().value,
                                underThresholdPair.getValue().value))).count() == 0) {
                    itemsAfterSecondJoin.add(itemset);
                }

            }
        }

        return itemsAfterSecondJoin;
    }

    private Map<Item, Long> support() {

        Map<Item, Long> itemSupport = new LinkedHashMap<>();

        List<String> processedItems = new ArrayList<>();

        for (Application currentApp : applications) {

            List<Item> items = currentApp.items;

            for (Item item : items) {
                if (processedItems.contains(item.value)) {
                    continue;
                }

                // Calculate number of occurrence of item
                long count = applications.stream()
                        .filter(app -> app.items.stream().filter(i -> i.value
                                .equals(item.value)).findFirst().isPresent()).count();
                // If count is more then occurrence store item
                if (count > threshold) {
                    itemSupport.put(item, count);
                }
                //No need to process one and the same item more than once.
                processedItems.add(item.value);
            }
        }
        return itemSupport;
    }

    private void validate() {
        if (this.applications == null || this.applications.isEmpty()) {
            throw new IllegalArgumentException("Applications can not be null or empty!");
        }
    }

}
