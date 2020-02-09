package com.home.home.ui.feed

class Filter {

    val filters = HashMap<FieldType, HashSet<FilterRule>>()

    fun addRule(fieldType: FieldType, filterRule: FilterRule) {
        val rules = filters[fieldType]
        rules?.let {
            rules.add(filterRule)
        } ?: hashSetOf(filterRule)
    }

    fun removeRule(fieldType: FieldType, filterRule: FilterRule) {
        filters[fieldType]?.remove(filterRule)
    }

    fun validate(value: String, fieldType: FieldType): Boolean {

        return filters[fieldType]?.let { rules ->
            when (fieldType) {
                FieldType.WEIGHT -> {
                    try {
                        val valInt = value.toInt()

                        for (rule in rules) {
                            when(rule) {
                                FilterRule.ANY -> false
                                FilterRule.LESS_THAN -> false // compare value to value inside rules
                                else -> false
                            }
                        }

                    } catch (e: NumberFormatException) {
                        return false
                    }

                    true
                }
                else -> false
            }
        } ?: false
    }
}