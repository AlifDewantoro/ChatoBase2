package com.gamatechno.chato.sdk.module.dialogs.EmojiBottomSheet.emoji.categories

import com.gamatechno.chato.sdk.module.dialogs.EmojiBottomSheet.emoji.categoryUnicodes.ObjectsCategoryUnicodes


data class ObjectsCategory(
    override val categoryName: String,
    override val categoryUnicode: List<ObjectsCategoryUnicodes> = enumValues<ObjectsCategoryUnicodes>().toList()
) : Category
