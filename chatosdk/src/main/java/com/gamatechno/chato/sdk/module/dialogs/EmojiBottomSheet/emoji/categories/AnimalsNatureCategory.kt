package com.gamatechno.chato.sdk.module.dialogs.EmojiBottomSheet.emoji.categories

import com.gamatechno.chato.sdk.module.dialogs.EmojiBottomSheet.emoji.categoryUnicodes.AnimalsNatureCategoryUnicodes


data class AnimalsNatureCategory(
    override val categoryName: String,
    override val categoryUnicode: List<AnimalsNatureCategoryUnicodes> = enumValues<AnimalsNatureCategoryUnicodes>().toList()
) : Category
