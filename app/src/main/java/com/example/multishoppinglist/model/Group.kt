package com.example.multishoppinglist.model

import java.util.*

data class Group(
    var created_by  : String    ?=  null,
    var group_name  : String    ?=  null,
    var user_type   : String    ?=  null
    )
