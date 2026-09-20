package ru.urfu.droidpractice1.content

import androidx.annotation.StringRes

data class ArticleSectionData(
    val id: String,
    @StringRes val title: Int,
    @StringRes val description: Int,
    @StringRes val titleItems: Int,
    val listOfItems: List<Int>
)