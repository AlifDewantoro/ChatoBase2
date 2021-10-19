package com.gamatechno.chato.sdk.module.dialogs.EmojiBottomSheet.emoji.categories

import com.gamatechno.chato.sdk.module.dialogs.EmojiBottomSheet.emoji.categoryUnicodes.FoodDrinkCategoryUnicodes


data class FoodDrinkCategory(
    override val categoryName: String,
    override val categoryUnicode: List<FoodDrinkCategoryUnicodes> = enumValues<FoodDrinkCategoryUnicodes>().toList()
) : Category
