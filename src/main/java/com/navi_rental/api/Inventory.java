package com.navi_rental.api;

import java.util.HashMap;

public class Inventory<T1, T2> {
    private HashMap<T1, T2> inventory = new HashMap<T1, T2>();

    public HashMap<T1, T2> getInventory() {
        return inventory;
    }

    public void putInventory(T1 t1, T2 t2) {
        this.inventory.put(t1, t2);
    }
}
