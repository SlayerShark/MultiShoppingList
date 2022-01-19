package com.example.multishoppinglist.model

data class GroupItem(
    var id:String               ?= null,
    var user_id:String          ?= null,
    var item_name:String        ?= null,
    var item_description:String ?= null,
    var item_quantity:String    ?= null,
    var group_name:String       ?= null
)