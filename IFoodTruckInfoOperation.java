package com.foodtruck;

import java.sql.Date;
import java.util.List;

interface IFoodTruckInfoOperation {
    public List<FoodTruckInfo> getFoodTruckByCurrentDate();
    public void saveData(List<String> list);
}