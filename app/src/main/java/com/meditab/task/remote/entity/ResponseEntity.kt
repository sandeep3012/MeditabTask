package com.meditab.task.remote.entity


data class ResponseEntity(
    var batchcomplete: String?,
    var pagesInfo: MutableList<PagesInfo>?
)

data class Continue(
    val `continue`: String?,
    val gpsoffset: Int?
)

data class Query(
    var pages: Pages?
)
data class Pages(
    val pagesInfo : MutableList<PagesInfo?>?
)
data class PagesInfo(
    val index: Int?,
    val ns: Int?,
    val pageid: Int?,
    val thumbnail: Thumbnail?,
    val title: String?
){
    override fun toString(): String {
        return "PagesInfo [index: ${this.index}, ns: ${this.ns}, pageid: ${this.pageid}, " +
                "thumbnail: ${this.thumbnail}, title: ${this.title}]"
    }
}

data class Thumbnail(
    val height: Int?,
    val source: String?,
    val width: Int?
){
    override fun toString(): String {
        return "Thumbnail [height: ${this.height}, source: ${this.source}, width: ${this.width}]"
    }
}