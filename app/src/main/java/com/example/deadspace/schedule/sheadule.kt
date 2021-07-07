package com.example.deadspace.schedule

import android.provider.DocumentsContract
import android.widget.ArrayAdapter
import org.jsoup.Jsoup
import org.jsoup.select.Elements
import java.io.IOException


class sheadule {
    /*private fun getHtmlFromWeb() {
        Thread(Runnable {
            var index = 0
            val arrayAdapter: ArrayAdapter<String>
            val stringBuilder = StringBuilder()
            val stringBuilderTitle = StringBuilder()
            var strings: MutableList<String> = mutableListOf()
            try {
                val doc: DocumentsContract.Document = Jsoup.connect("https://vkist.guap.ru/").get()
                val title: String = doc.title()
                val links: Elements = doc.select("article")
                stringBuilderTitle.append(title).append("\n")
                for (link in links) {
                    stringBuilder.append("\n").append("Link: ").append(link.select("a[href]").attr("href")).append(" \n").append("Text : ").append(link.select("h2").text())
                    strings.add(index, stringBuilder.toString())
                    index++
                    stringBuilder.clear()
                }
            } catch (e: IOException) {
                stringBuilder.append("Error : ").append(e.message).append("\n")
            }
            arrayAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, strings)
            runOnUiThread {
                textView.text = stringBuilderTitle.toString()
                listView.adapter = arrayAdapter
            }
        }).start()*/
}