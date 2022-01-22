package com.example.multishoppinglist.model

data class GroupItem(
    var id                  :String     ?= null,
    var item_added_by       :String     ?= null,
    var item_edited_by      :String     ?= null,
    var item_name           :String     ?= null,
    var item_description    :String     ?= null,
    var item_quantity       :String     ?= null,
    var item_price          :String     ?= null,
    var group_name          :String     ?= null,
    var item_checked        :String    ?= null
)
