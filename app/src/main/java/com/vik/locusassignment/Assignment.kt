package com.vik.locusassignment

class AssignmentModel : ArrayList<AssignmentModelItem>()

data class AssignmentModelItem(
    val dataMap: DataMap,
    val id: String,
    val title: String,
    val type: String
)

class DataMap(
    val options : ArrayList<String>
)