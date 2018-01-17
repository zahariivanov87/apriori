package com.data.mining.apriori;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import com.data.mining.application.Application;
import com.data.mining.item.Item;

import org.junit.Before;
import org.junit.Test;


public class AprioriTest {

    private List<Application> apps;

    @Before
    public void setUp() {
        apps = apps();
    }

    @Test
    public void testClassification() {

        Apriori apriori = new Apriori(apps);

        // Most common items are [Banana, Cucumber, Eggs]
        Set<Item> recomendedItems = apriori.recommend();

        assertNotNull(recomendedItems);
        assertEquals(3, recomendedItems.size());

        assertTrue(
                recomendedItems.stream().filter(item -> item.value.equals(Items.BANANA)).findFirst()
                        .isPresent());

        assertTrue(recomendedItems.stream().filter(item -> item.value.equals(Items.CUCUMBER))
                .findFirst()
                .isPresent());

        assertTrue(
                recomendedItems.stream().filter(item -> item.value.equals(Items.EGGS)).findFirst()
                        .isPresent());
    }

    @Test
    public void testWithHigherThreshold() {

        Application application = new Application(
                Arrays.asList(new Item(Items.BANANA, Items.EGGS)));
        Application application2 = new Application(
                Arrays.asList(new Item(Items.BANANA, Items.CUCUMBER)));
        Application application3 = new Application(
                Arrays.asList(new Item(Items.BANANA, Items.APPLE)));

        addApp(application);
        addApp(application2);
        addApp(application3);

        Apriori apriori = new Apriori(apps, 3);

        // Most common item is [Banana]. It is the only item that satisfies threshold = 3.
        Set<Item> recomendedItems = apriori.recommend();

        assertNotNull(recomendedItems);
        assertEquals(1, recomendedItems.size());

        assertTrue(
                recomendedItems.stream().filter(item -> item.value.equals(Items.BANANA)).findFirst()
                        .isPresent());
    }

    private class Items {

        static final String BANANA = "Banana";
        static final String APPLE = "Apple";
        static final String CUCUMBER = "Cucumber";
        static final String DATES = "Dates";
        static final String EGGS = "Eggs";

    }

    private static List<Application> apps() {

        Item a = new Item(Items.APPLE);
        Item b = new Item(Items.BANANA);
        Item c = new Item(Items.CUCUMBER);
        Item d = new Item(Items.DATES);
        Item e = new Item(Items.EGGS);

        Application first = new Application(new ArrayList<Item>(Arrays.asList(a, c, d)) {
        });
        Application second = new Application(new ArrayList<Item>(Arrays.asList(b, c, e)) {
        });
        Application third = new Application(new ArrayList<Item>(Arrays.asList(a, b, c, e)) {
        });
        Application fourth = new Application(new ArrayList<Item>(Arrays.asList(b, e)) {
        });

        List<Application> app = new ArrayList<Application>();
        app.add(first);
        app.add(second);
        app.add(third);
        app.add(fourth);

        return app;
    }

    private void addApp(Application app) {
        if (this.apps == null) {
            throw new IllegalArgumentException("'apps' can not be null!");
        }
        apps.add(app);
    }

}
