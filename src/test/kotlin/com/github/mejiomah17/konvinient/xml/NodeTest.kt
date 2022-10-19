package com.github.mejiomah17.konvinient.xml

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import javax.xml.parsers.DocumentBuilderFactory

class NodeTest {
    private val builder = DocumentBuilderFactory.newInstance().newDocumentBuilder()

    @Test
    fun `parses node attributes`() {
        val node = Node.from(
            builder.parse(
                """
                <root x="bla">
                </root>
            """.trimIndent().byteInputStream()
            )
        )
        node.attributes["x"] shouldBe "bla"
    }

    @Test
    fun `parses node content`() {
        val node = Node.from(
            builder.parse("<root>blabla</root>".byteInputStream())
        )
        node.content shouldBe "blabla"
    }
    @Test
    fun `parses node name`() {
        val node = Node.from(
            builder.parse("<root>blabla</root>".byteInputStream())
        )
        node.name shouldBe "root"
    }

    @Test
    fun `parses child node`() {
        val node = Node.from(
            builder.parse("""
                <root>
                    <blabla x="4">hey</blabla>
                </root>
            """.trimIndent().byteInputStream())
        )
        val child = node.children.single()
        child.content shouldBe "hey"
        child.attributes["x"] shouldBe "4"
    }

    @Test
    fun `get by name works`() {
        val node = Node.from(
            builder.parse("""
                <root>
                    <blabla x="4">hey</blabla>
                </root>
            """.trimIndent().byteInputStream())
        )
        val child = node["blabla"]!!
        child.content shouldBe "hey"
        child.attributes["x"] shouldBe "4"
    }

    @Test
    fun `parses currencies`(){
        val node = Node.from(this.javaClass.classLoader.getResourceAsStream("currencies.xml")!!)
        val rubleName= node["CcyTbl"]!!.children.first {
            it["Ccy"]?.content.equals("RUB",true)
        }["CcyNm"]!!.content

        rubleName shouldBe "Russian Ruble"
    }
}