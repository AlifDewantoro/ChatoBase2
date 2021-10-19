package com.gamatechno.chato.sdk.module.dialogs.EmojiBottomSheet.emoji.categories

import com.gamatechno.chato.sdk.module.dialogs.EmojiBottomSheet.emoji.categoryUnicodes.SymbolsCategoryUnicodes


data class SymbolsCategory(
    override val categoryName: String,
    override val categoryUnicode: List<SymbolsCategoryUnicodes> = enumValues<SymbolsCategoryUnicodes>().toList()
) : Category
