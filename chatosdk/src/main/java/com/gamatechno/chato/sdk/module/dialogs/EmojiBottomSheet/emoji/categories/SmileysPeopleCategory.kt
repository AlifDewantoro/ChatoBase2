package com.gamatechno.chato.sdk.module.dialogs.EmojiBottomSheet.emoji.categories

import com.gamatechno.chato.sdk.module.dialogs.EmojiBottomSheet.emoji.categoryUnicodes.SmileysPeopleCategoryUnicodes


data class SmileysPeopleCategory(
    override val categoryName: String,
    override val categoryUnicode: List<SmileysPeopleCategoryUnicodes> = enumValues<SmileysPeopleCategoryUnicodes>().toList()
) : Category
