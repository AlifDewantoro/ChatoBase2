package com.gamatechno.chato.sdk.module.dialogs.EmojiBottomSheet.emoji.categories

import com.gamatechno.chato.sdk.module.dialogs.EmojiBottomSheet.emoji.categoryUnicodes.TravelPlacesCategoryUnicodes


data class TravelPlacesCategory(
    override val categoryName: String,
    override val categoryUnicode: List<TravelPlacesCategoryUnicodes> = enumValues<TravelPlacesCategoryUnicodes>().toList()
) : Category
