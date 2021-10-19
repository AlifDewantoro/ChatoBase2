package com.gamatechno.chato.sdk.module.dialogs.EmojiBottomSheet.emoji.categories

import com.gamatechno.chato.sdk.module.dialogs.EmojiBottomSheet.emoji.categoryUnicodes.CategoryUnicodes


interface Category {
    val categoryName: String
    val categoryUnicode: List<CategoryUnicodes>
}
