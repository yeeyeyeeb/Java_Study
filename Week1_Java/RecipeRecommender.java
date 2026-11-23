import java.time.LocalDate;
import java.util.ArrayList;

public class RecipeRecommender {
	private static final int NEAR_DAYS = 3;

	public static boolean hasIngredient(ArrayList<FoodItem> items, String ingredient) {
		// 보관중인 음식 중 제품명에 ingredient가 포함된 것이 있으면 true
		for (FoodItem item : items) {
			if (item.isStored() && item.getName().contains(ingredient)) {
				return true;
			}
		}
		return false;
	}

	public static int countMatched(Recipe recipe, ArrayList<FoodItem> items) {
		// 레시피 재료 중 갖고 있는 재료 개수
		int count = 0;
		for (String ing : recipe.getIngredients()) {
			if (hasIngredient(items, ing)) {
				count++;
			}
		}
		return count;
	}

	private static boolean hasNearIngredient(ArrayList<FoodItem> items, String ingredient, LocalDate today) {
		// 해당 재료를 가진 보관중 음식 중 하나라도 임박(0~3일)이면 true
		for (FoodItem item : items) {
			if (item.isStored() && item.getName().contains(ingredient)
					&& DateCalculator.isNearExpiry(today, item.getExpiryDate(), NEAR_DAYS)) {
				return true;
			}
		}
		return false;
	}

	public static int calcScore(Recipe recipe, ArrayList<FoodItem> items, LocalDate today) {
		// 재료 1개당 +10, 임박이면 +5 추가, 재료 전부 있으면 +20
		int score = 0;
		int matched = 0;
		for (String ing : recipe.getIngredients()) {
			if (hasIngredient(items, ing)) {
				matched++;
				score += 10;
				if (hasNearIngredient(items, ing, today)) {
					score += 5;
				}
			}
		}
		if (matched > 0 && matched == recipe.getIngredients().size()) {
			score += 20;
		}
		return score;
	}

	public static ArrayList<Recipe> top3(ArrayList<Recipe> recipes, ArrayList<FoodItem> items, LocalDate today) {
		// 점수 1점 이상 중 높은 순 최대 3개, 같은 점수면 앞에 있는 것 먼저
		int[] scores = new int[recipes.size()];
		boolean[] picked = new boolean[recipes.size()];
		for (int i = 0; i < recipes.size(); i++) {
			scores[i] = calcScore(recipes.get(i), items, today);
		}

		ArrayList<Recipe> result = new ArrayList<>();
		for (int k = 0; k < 3; k++) {
			int best = -1;
			for (int i = 0; i < recipes.size(); i++) {
				if (picked[i] || scores[i] < 1) continue;
				if (best == -1 || scores[i] > scores[best]) {
					best = i;
				}
			}
			if (best == -1) break;
			picked[best] = true;
			result.add(recipes.get(best));
		}
		return result;
	}
}
