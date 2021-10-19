package com.gamatechno.chato.sdk.module.dialogs.EmojiBottomSheet.emoji.categories

import com.gamatechno.chato.sdk.module.dialogs.EmojiBottomSheet.emoji.categoryUnicodes.ActivityCategoryUnicodes


data class ActivitiesCategory(
    override val categoryName: String,
    override val categoryUnicode: List<ActivityCategoryUnicodes> = enumValues<ActivityCategoryUnicodes>().toList()
) : Category
