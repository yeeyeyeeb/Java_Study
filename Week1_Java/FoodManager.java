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
            target.markEaten(date);   // [수정] setStatus/setResolvedDate 직접 호출 → FoodItem.markEaten() 사용 (문제 조건)
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
            target.markDiscarded(date);   // [수정] setStatus/setResolvedDate 직접 호출 → FoodItem.markDiscarded() 사용 (문제 조건)
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
    	// [수정] items.isStored() 조건 추가 → 이미 처리된 항목은 임박 목록에서 제외
    	return items.stream().filter(items -> items.isStored() && DateCalculator.isNearExpiry(today, items.getExpiryDate(), threshold)==true).collect(Collectors.toCollection(ArrayList::new));
    }
    public int autoDiscardOverdue(LocalDate today, int graceDays) {
    	ArrayList<FoodItem> autoDiscarded = new ArrayList<FoodItem>();
    	for(FoodItem item : items) {
    		// [수정] item.isStored() 조건 추가 → 이미 다 씀/버림 처리된 항목을 덮어쓰지 않게
    		if(item.isStored() && DateCalculator.isOverdue(today, item.getExpiryDate(), graceDays)) {
    			item.markAutoDiscarded(today);   // [수정] setStatus만 하던 것 → markAutoDiscarded(today)로 처리일까지 저장
    			autoDiscarded.add(item);
    		}
    	}
    	return autoDiscarded.size();
    }
    

}