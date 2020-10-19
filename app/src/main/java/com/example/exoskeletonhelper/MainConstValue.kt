package com.example.exoskeletonhelper

interface MainConstValue {
    val tabSelectedDrawableIdList: Array<Int>
        get() = arrayOf(
            R.drawable.ic_main_first_slected,
            R.drawable.ic_main_recovery_suggestion_slected,
            R.drawable.ic_main_recovery_action_slected,
            R.drawable.ic_main_equipment_slected
        )

    val tabUnselectedDrawableList: Array<Int>
        get() = arrayOf(
            R.drawable.ic_main_first_unslected,
            R.drawable.ic_main_recovery_suggestion_unslected,
            R.drawable.ic_main_recovery_action_unslected,
            R.drawable.ic_main_equipment_unslected

        )

    val tabText: Array<String> get() = arrayOf("首页", "康复计划", "康复动作","设备")
}