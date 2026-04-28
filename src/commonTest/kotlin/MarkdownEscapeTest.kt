import dev.schlaubi.alphabet.extractBoldText
import kotlin.test.Test
import kotlin.test.assertEquals

class MarkdownEscapeTest {
    @Test
    fun testNonBoldText() {
        val input = "hallo"
        val result = input.extractBoldText()
        assertEquals(input, result)
    }

    @Test
    fun testFullyBoldText() {
        val input = "**hallo**"
        val result = input.extractBoldText()
        assertEquals("hallo", result)
    }

    @Test
    fun testPartiallyBoldText() {
        val input = "**hal**lo"
        val result = input.extractBoldText()
        assertEquals("hal", result)
    }
    @Test
    fun testDoublePartiallyBoldText() {
        val input = "**hal**lo **lo**ha"
        val result = input.extractBoldText()
        assertEquals("hallo", result)
    }
    @Test
    fun testWierdlyBoldText() {
        val input = "diese Vorlesung dauert zu lange. Das ist ***q**ualvoll "
        val result = input.extractBoldText()
        assertEquals("q", result)
    }
}
