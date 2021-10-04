package com.davidm1a2.afraidofthedark.client.gui

enum class SpecialTextCharacters(private val token: String) {
    SPACE("space"),
    TAB("tab"),
    NEW_LINE("newline");

    override fun toString(): String {
        return "&$token;"
    }
}