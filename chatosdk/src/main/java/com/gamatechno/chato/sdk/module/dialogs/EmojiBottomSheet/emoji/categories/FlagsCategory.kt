package com.gamatechno.chato.sdk.module.dialogs.EmojiBottomSheet.emoji.categories

import com.gamatechno.chato.sdk.module.dialogs.EmojiBottomSheet.emoji.categoryUnicodes.FlagsCategoryUnicodes


data class FlagsCategory(
    override val categoryName: String,
    override val categoryUnicode: List<FlagsCategoryUnicodes> = enumValues<FlagsCategoryUnicodes>().toList()
) : Category
