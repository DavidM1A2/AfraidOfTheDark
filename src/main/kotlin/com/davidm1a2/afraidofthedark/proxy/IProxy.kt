package com.davidm1a2.afraidofthedark.proxy

/**
 * Interface containing methods for the proxy. This proxy will be instantiated differently on the server and client
 */
interface IProxy {
    /**
     * Opens the "Insanity's Heights" book on the client side, does nothing server side
     */
    fun showInsanitysHeightsBook()
}