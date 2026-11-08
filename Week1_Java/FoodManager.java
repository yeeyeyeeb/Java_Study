import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import java.time.*;
import java.time.temporal.ChronoUnit;


public class FoodManager{
    private ArrayList<FoodItem> items;
    private int nextId;
    public FoodManager(){
        items = new ArrayList<FoodItem>();
        nextId = 1;
    }
    public int add(FoodItem item){
        item.setId(nextId);
        items.add(item);
        nextId++;
        return item.getId();
    }
    public int size(){
        return nextId-1;
    }
    public FoodItem findById(int id){
        return items.stream().filter(item->item.getId() == id).findFirst().orElse(null);
    }
    public ArrayList<FoodItem> getStoredItems(){
        //sorting 과정에서 arraylist의 filter기능 및 collect 기능을 이용하기 위해
        //list를 ArrayList형으로 강제형변환 하였음. 
    	return items.stream().filter(items -> items.getStatus() == 0).collect(Collectors.toCollection(ArrayList::new));
    }
    public boolean markEaten(int id, LocalDate date){
    	FoodItem target = items.stream().filter(item->item.getId() == id).findFirst().orElse(null);
    	if(target == null)
    		return false;
        if(!target.isStored())
            return false;
        else{
            target.setStatus(FoodItem.STATUS_EATEN);
            target.setResolvedDate(date);
            return true;
        }
    }
    public boolean markDiscarded(int id, LocalDate date){
    	FoodItem target = items.stream().filter(item->item.getId() == id).findFirst().orElse(null);
    	if(target == null)
    		return false;
        if(!target.isStored())
            return false;
        else{
            target.setStatus(FoodItem.STATUS_DISCARDED);
            target.setResolvedDate(date);
            return true;
        }
    }
    public int countDiscarded(String barcode){
        long discarded = items.stream().filter(item->item.getBarcode().compareTo(barcode)==0 && (item.getStatus()==FoodItem.STATUS_DISCARDED|| item.getStatus() == FoodItem.STATUS_AUTO_DISCARDED)).count();
        return (int)discarded;
    }
    public ArrayList<FoodItem> getNearExpiryItems(LocalDate today, int threshold){
    	//sorting 과정에서 arraylist의 filter기능 및 collect 기능을 이용하기 위해
        //list를 ArrayList형으로 강제형변환 하였음.
    	return items.stream().filter(items -> DateCalculator.isNearExpiry(today, items.getExpiryDate(), threshold)==true).collect(Collectors.toCollection(ArrayList::new));
    }
    public int autoDiscardOverdue(LocalDate today, int graceDays) {
    	ArrayList<FoodItem> autoDiscarded = new ArrayList<FoodItem>();
    	for(FoodItem item : items) {
    		if(DateCalculator.isOverdue(today, item.getExpiryDate(), graceDays)) {
    			item.setStatus(FoodItem.STATUS_AUTO_DISCARDED);
    			autoDiscarded.add(item);
    		}
    	}
    	return autoDiscarded.size();
    }
    

}
