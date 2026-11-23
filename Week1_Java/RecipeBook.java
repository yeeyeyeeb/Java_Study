import java.util.*;
import java.util.stream.Collectors;
public class RecipeBook {
    public static ArrayList<Recipe> parse(ArrayList<String> lines){
        ArrayList<Recipe> ret = new ArrayList<>();
        if(lines == null) return ret;
        for(int i = 1; i < lines.size(); i++){
            String line = lines.get(i);
            if(line == null || line.trim().isEmpty())
                continue;
            String name;
            String[] a = line.split(",", 2);
            if(a.length<=1){
                continue;
            }
            else{
            name = a[0].trim();
            }
            if(a[1]==null || a[1].trim().isEmpty()){
                continue;
            }
            String[] b = a[1].split("\\|");
            ArrayList<String> ingr = new ArrayList<>(Arrays.asList(b));
            ingr.replaceAll(String::trim);
            ingr.removeIf(String::isEmpty);
            if(name.isEmpty() || ingr.isEmpty())
                continue;
            ret.add(new Recipe(name, ingr));               
        }
        return ret;
    }
    public static Recipe findByName(ArrayList<Recipe> recipes, String name){
        if(recipes == null || name == null) return null;
        Recipe reci = recipes.stream().filter(r -> name.equals(r.getName())).findFirst().orElse(null);
        return reci;
    }
    public static ArrayList<Recipe> filterByIngredient(ArrayList<Recipe> recipes, String ingredient){
        if(recipes == null || ingredient == null) return new ArrayList<>();
        ArrayList<Recipe> reci = recipes.stream().filter(r->r.getIngredients().contains(ingredient)).collect(Collectors.toCollection(ArrayList::new));
        return reci;
    }
}
