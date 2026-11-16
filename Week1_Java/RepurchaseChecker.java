import java.util.ArrayList;

public class RepurchaseChecker{
    public static String check(FoodManager manager, String barcode){
        String asd = null;
       if(barcode==null || barcode.trim().isEmpty())
        return null;
       ArrayList<FoodItem> currentItem = manager.getAllItems();
       //참고사항 : NullPointerException을 피하기 위해 barcode라는 주어진것이 확정인 String객체의 메서드를 활용하는게 예외 발생을 줄여줌
       //item.getBarcode().equals(barcode)  --> X, barcode.equals(item.getBarcode()) --> O
       long count = currentItem.stream().filter(item -> barcode.equals(item.getBarcode()) &&item.isStored()).count();
       if(count !=0){
        asd = "이미 보관중인 같은 제품이 " + count + "개 있어요";
       }
       long discard = manager.countDiscarded(barcode);
       if(discard > 0){
        if(count!=0) 
        return asd + "\n"+"전에 " + currentItem.stream().filter(item -> barcode.equals(item.getBarcode()) && (!item.isStored())).count() + "번 중 " + discard+ "번 버렸어요";
        else
            return "전에 " + currentItem.stream().filter(item -> barcode.equals(item.getBarcode())).count() + "번 중 " + discard+ "번 버렸어요";
       }
        return asd;
    }
}