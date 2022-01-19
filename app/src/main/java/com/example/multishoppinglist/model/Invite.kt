package com.example.multishoppinglist.model

data class Invite(
    var invited_by   : String    ?=  null,
    var group_name  : String    ?=  null,
    var user_type   : String    ?=  null
)
