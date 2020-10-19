package com.example.exoskeletonhelper

import java.security.MessageDigest

interface OnReciveTopicListener {

    fun onTopicRecive(top:String,message:String)
}