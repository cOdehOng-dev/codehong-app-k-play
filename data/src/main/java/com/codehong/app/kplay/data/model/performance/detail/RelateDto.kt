package com.codehong.app.kplay.data.model.performance.detail

import org.simpleframework.xml.Element
import org.simpleframework.xml.Root

/**
 * 예매처
 */
@Root(name = "relate", strict = false)
data class RelateDto(

    @field:Element(name = "relatenm", required = false)
    var name: String? = null,

    @field:Element(name = "relateurl", required = false)
    var url: String? = null
)
