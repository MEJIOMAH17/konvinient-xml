package com.github.mejiomah17.konvinient.xml

import com.github.mejiomah17.konvinient.xml.Node.Companion.parse
import org.w3c.dom.Document
import org.w3c.dom.Element
import org.w3c.dom.NamedNodeMap
import org.w3c.dom.NodeList
import java.io.File
import java.io.InputStream
import javax.xml.parsers.DocumentBuilderFactory

data class Node(
    val name: String,
    val attributes: Map<String, String>,
    val content: String?,
    val children: List<Node>
) {
    private val namesCache by lazy { children.associateBy { it.name } }

    operator fun get(childNodeName: String): Node? {
        return namesCache[childNodeName]
    }

    companion object {
        private val builder = DocumentBuilderFactory.newInstance().newDocumentBuilder()
        fun from(document: Document): Node {
            return document.documentElement.parse()
        }

        fun from(text: String): Node {
            return from(text.byteInputStream())
        }

        fun from(file: File): Node {
            return from(builder.parse(file))
        }

        fun from(inputStream: InputStream): Node {
            return from(builder.parse(inputStream))
        }

        private fun Element.parse(): Node {
            return Node(
                name = this.nodeName,
                attributes = this.attributes.parse(),
                content = this.textContent,
                children = this.parseChildNodes()
            )
        }

        private fun NamedNodeMap?.parse(): Map<String, String> {
            if (this == null) return emptyMap()
            return (0 until this.length).map {
                item(it)
            }.associate {
                it.nodeName to it.nodeValue
            }
        }

        private fun org.w3c.dom.Node.parseChildNodes(): List<Node> {
            val children = this.childNodes ?: return emptyList()
            return (0 until children.length).map {
                children.item(it)
            }.filterIsInstance<Element>().map {
                it.parse()
            }
        }
    }
}
