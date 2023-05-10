package com.fifty.data.models

import com.fifty.data.responses.SkillDto
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

data class Skill(
    @BsonId
    val id: String = ObjectId().toString(),
    val name: String,
    val imageUrl: String
) {
    fun toSkillResponse(): SkillDto {
        return SkillDto(
            name = name,
            imageUrl = imageUrl
        )
    }
}
